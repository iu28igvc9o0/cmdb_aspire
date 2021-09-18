package com.aspire.cmdb.agent.sync.schedule.cmdb;

import static com.aspire.ums.cmdb.sync.payload.SysOrgDTO.DataEntity;
import static com.aspire.ums.cmdb.sync.payload.SysOrgDTO.DataEntity.OrgListEntity;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import com.aspire.cmdb.agent.client.DepartmentServiceClient;
import com.aspire.cmdb.agent.client.LdapUserServiceClient;
import com.aspire.cmdb.agent.client.UserServiceClient;
import com.aspire.ums.cmdb.sync.payload.SysOrgDTO;
import com.aspire.ums.cmdb.util.JsonMapper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.migu.tsg.microservice.atomicservice.rbac.dto.DepartmentCreateRequest;
import com.rabbitmq.client.Channel;

import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.BoundMapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;

/**
 * 网管组织机构同步监听(通过RabbitMQ发送).
 *
 * @author jiangxuwen
 * @date 2020/5/20 11:37
 */
@Component
@Slf4j
@ConditionalOnProperty(name = "cmdb.enable.syncOsaFlag", havingValue = "true")
public class CmdbDepartmentSyncConsumer {

    @Autowired
    private DepartmentServiceClient departmentClient;

    @Autowired
    private UserServiceClient userClient;

    @Autowired
    private LdapUserServiceClient ldapUserServiceClient;

    private static final MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();

    static {
        mapperFactory.classMap(OrgListEntity.class, DepartmentCreateRequest.class).field("orgId", "departmentId")
                .field("orgName", "name").field("parentOrgId", "parentId").byDefault().register();
    }

    @RabbitListener(queues = "${organization.mq.queue.name}")
    public void consumer(Channel channel, Message message) throws IOException {
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        String messageBody = new String(message.getBody(), Charset.forName("utf-8"));
        // messageBody = messageBody.replaceAll("(\r\n)|\n", "");
        log.info(">>>>>>>[组织机构同步]收到rabbitMQ的消息:{}", messageBody);
        try {
            log.info("[组织机构同步]处理消息，入库");
            SysOrgDTO sysOrg = new JsonMapper().readValue(messageBody, SysOrgDTO.class);
            saveDepartment(sysOrg);
            // 确认收到消息，false只确认当前consumer一个消息收到，true确认所有consumer获得的消息
            channel.basicAck(deliveryTag, false);
            log.info("<<<<<<<<<<<[组织机构同步]处理完成!");
        } catch (Exception e) {
            if (message.getMessageProperties().getRedelivered()) {
                log.warn("消息已重复处理失败,拒绝再次接收");
                // 拒绝消息，requeue=false 表示不再重新入队，如果配置了死信队列则进入死信队列
                channel.basicReject(message.getMessageProperties().getDeliveryTag(), false);
            } else {
                log.info("消息即将再次返回队列处理");
                // requeue为是否重新回到队列，true重新入队
                channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, true);
            }
            log.error("[组织机构同步]消息确认失败!", e);
            // channel.basicNack(deliveryTag, false, true);
        }
    }

    private void saveDepartment(SysOrgDTO sysOrg) {
        try {
            DataEntity dataEntity = sysOrg.getData();
            List<OrgListEntity> orgListEntityList = dataEntity.getOrgList();
            log.info("[组织机构同步],待同步的数量:[{}]", orgListEntityList.size());
            persist(orgListEntityList);
        } catch (Exception e) {
            log.error("[组织机构同步]入库失败.", e);
        }
    }

    /**
     * 构建父子节点.
     * 
     * @param
     * @return
     */
    private static List<OrgListEntity> buildTree(List<OrgListEntity> list) {
        List<OrgListEntity> orgListEntityList = Lists.newArrayList();
        if (CollectionUtils.isEmpty(list)) {
            return Lists.newArrayList();
        }
        Map<String, OrgListEntity> orgListEntityMap = Maps.newHashMap();

        for (OrgListEntity domain : list) {
            orgListEntityMap.put(String.valueOf(domain.getOrgId()), domain);
        }

        for (OrgListEntity domain : list) {
            OrgListEntity child = domain;
            if (child.getParentOrgId() == null) {
                orgListEntityList.add(domain);
            } else {
                OrgListEntity parent = orgListEntityMap.get(String.valueOf(child.getParentOrgId()));
                // 容错处理，避免NPE
                if (parent == null) {
                    orgListEntityList.add(domain);
                } else {
                    parent.getChildren().add(child);
                }
            }
        }
        return orgListEntityList;
    }

    /**
     * 处理入库逻辑.
     * 
     * @param
     * @return
     */
    private void persist(List<OrgListEntity> list) {
        List<OrgListEntity> treeList = list;// buildTree(list);
        List<DepartmentCreateRequest> addList = Lists.newArrayList();
        List<DepartmentCreateRequest> updateList = Lists.newArrayList();
        List<DepartmentCreateRequest> deleteList = Lists.newArrayList();
        for (OrgListEntity domain : treeList) {

            DepartmentCreateRequest request = convert(domain);
            if ("1".equals(request.getDelFlag())) {
                deleteList.add(request);
            } else if ("0".equals(request.getDelFlag())) {
                addList.add(request);
            }
        }
        departmentClient.batchCreatedDepartment(addList);
        List<String> idList = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(deleteList)) {
            idList = deleteList.stream().map(e -> e.getDepartmentId()).collect(Collectors.toList());
            departmentClient.batchDeleteByPrimaryKey(idList);
        }
    }

    private DepartmentCreateRequest convert(OrgListEntity entity) {
        BoundMapperFacade<OrgListEntity, DepartmentCreateRequest> mapper = mapperFactory.getMapperFacade(OrgListEntity.class,
                DepartmentCreateRequest.class);
        DepartmentCreateRequest request = mapper.map(entity);
        if ("1".equals(entity.getIsOpen())) {
            request.setDelFlag("0");
        } else if ("2".equals(entity.getIsOpen())) {
            request.setDelFlag("1");
        }
        request.setDeptType(1);
        request.setNamespace("alauda");
        return request;
    }

}
