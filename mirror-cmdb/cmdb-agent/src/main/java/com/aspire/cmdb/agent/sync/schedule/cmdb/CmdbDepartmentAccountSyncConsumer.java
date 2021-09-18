package com.aspire.cmdb.agent.sync.schedule.cmdb;

import static com.aspire.ums.cmdb.sync.payload.DepartmentAccountInfoDTO.DepartmentAccountData;
import static com.aspire.ums.cmdb.sync.payload.DepartmentAccountInfoDTO.DepartmentAccountData.DepartmentAccountDetail;
import static com.aspire.ums.cmdb.sync.payload.DepartmentAccountInfoDTO.DepartmentAccountData.DepartmentAccountDetail.OrgInfo;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.aspire.cmdb.agent.client.DepartmentServiceClient;
import com.aspire.cmdb.agent.client.LdapUserServiceClient;
import com.aspire.cmdb.agent.client.UserServiceClient;
import com.aspire.ums.cmdb.sync.payload.DepartmentAccountInfoDTO;
import com.aspire.ums.cmdb.util.JsonMapper;
import com.google.common.collect.Lists;
import com.migu.tsg.microservice.atomicservice.rbac.dto.UserBatchCreateRequest;
import com.migu.tsg.microservice.atomicservice.rbac.dto.UserCreateRequest;
import com.rabbitmq.client.Channel;

import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;

/**
 * 网管部门-人员关联关系同步监听(通过RabbitMQ发送).
 *
 * @author jiangxuwen
 * @date 2020/5/20 11:37
 */
@Component
@Slf4j
@ConditionalOnProperty(name = "cmdb.enable.syncOsaFlag", havingValue = "true")
public class CmdbDepartmentAccountSyncConsumer {

    @Autowired
    private DepartmentServiceClient departmentClient;

    @Autowired
    private UserServiceClient userClient;

    @Autowired
    private LdapUserServiceClient ldapUserServiceClient;

    private MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();

    Pattern loginNamePattern1 = Pattern.compile("^dw", Pattern.CASE_INSENSITIVE);

    Pattern loginNamePattern2 = Pattern.compile("^dw_", Pattern.CASE_INSENSITIVE);

    @RabbitListener(queues = "${orgAccount.mq.queue.name}")
    public void consumer(Channel channel, Message message) throws IOException {
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        String messageBody = new String(message.getBody(), Charset.forName("utf-8"));
        // messageBody = messageBody.replaceAll("(\r\n)|\n", "");
        log.info(">>>>>>>[部门-人员账号同步]收到rabbitMQ的消息:{}", messageBody);
        try {
            log.info("[部门-人员账号同步]处理消息，入库");
            DepartmentAccountInfoDTO accountInfo = new JsonMapper().readValue(messageBody, DepartmentAccountInfoDTO.class);
            saveDepartmentAccount(accountInfo);
            // 确认收到消息，false只确认当前consumer一个消息收到，true确认所有consumer获得的消息
            channel.basicAck(deliveryTag, false);
            log.info("<<<<<<<<<<<[部门-人员账号同步]处理完成!");
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
            log.error("[部门-人员账号同步]消息确认失败!", e);
            // channel.basicNack(deliveryTag, false, true);
        }
    }

    private void saveDepartmentAccount(DepartmentAccountInfoDTO accountInfo) {
        try {
            DepartmentAccountData dataEntity = accountInfo.getData();
            List<DepartmentAccountDetail> orgUserList = dataEntity.getOrgUserList();
            log.info("[部门-人员账号同步],待同步的数量:[{}]", orgUserList.size());
            persist(orgUserList);
        } catch (Exception e) {
            log.error("[部门-人员账号同步]入库失败.", e);
        }
    }

    /**
     * 处理入库逻辑.
     * 
     * @param
     * @return
     */
    private void persist(List<DepartmentAccountDetail> list) {
        UserBatchCreateRequest createRequest = new UserBatchCreateRequest();
        List<UserCreateRequest> listUser = Lists.newArrayList();
        list.forEach(e -> listUser.addAll(convert(e)));
        createRequest.setListUser(listUser);
        log.info("[部门-人员账号同步],待入库的数量:{}", listUser.size());
        if (CollectionUtils.isEmpty(listUser)) {
            return;
        }
        userClient.batchInsertDepartmentUser(createRequest);
    }

    private List<UserCreateRequest> convert(DepartmentAccountDetail entity) {
        List<UserCreateRequest> createRequestList = Lists.newArrayList();
        List<OrgInfo> orgList = entity.getOrgList();
        // List<String> orgIdList = orgList.stream().map(e -> String.valueOf(e.getOrgId())).collect(Collectors.toList());
        orgList.forEach(e -> {
            UserCreateRequest request = new UserCreateRequest();
            String loginName = entity.getPortalUserId();
            Matcher matcher1 = loginNamePattern1.matcher(loginName);
            // 检查是否dw开头 dw_开头不修改账号名, dw开头自动加上下划线
            if (matcher1.find()) {
                if (!loginNamePattern2.matcher(loginName).find()) {
                    loginName = matcher1.replaceFirst("dw_");
                }
            }
            request.setCode(loginName);
            request.setDeptId(String.valueOf(e.getOrgId()));
            createRequestList.add(request);
        });

        return createRequestList;
    }
}
