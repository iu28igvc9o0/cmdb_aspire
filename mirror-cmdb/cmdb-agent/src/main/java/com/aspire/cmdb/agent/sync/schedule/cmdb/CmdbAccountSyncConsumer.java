package com.aspire.cmdb.agent.sync.schedule.cmdb;

import static com.aspire.ums.cmdb.sync.payload.AccountInfoDTO.AccountInfoData;
import static com.aspire.ums.cmdb.sync.payload.AccountInfoDTO.AccountInfoData.AccountDetailInfo;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import com.aspire.cmdb.agent.client.DepartmentServiceClient;
import com.aspire.cmdb.agent.client.LdapUserServiceClient;
import com.aspire.cmdb.agent.client.UserServiceClient;
import com.aspire.ums.cmdb.sync.payload.AccountInfoDTO;
import com.aspire.ums.cmdb.sync.payload.CmdbOptType;
import com.aspire.ums.cmdb.util.JsonMapper;
import com.google.common.collect.Lists;
import com.migu.tsg.microservice.atomicservice.ldap.dto.InsertLdapMemberRequest;
import com.migu.tsg.microservice.atomicservice.ldap.dto.UpdateLdapMemberRequest;
import com.migu.tsg.microservice.atomicservice.rbac.dto.UserBatchCreateRequest;
import com.migu.tsg.microservice.atomicservice.rbac.dto.UserCreateRequest;
import com.migu.tsg.microservice.atomicservice.rbac.dto.UserUpdateRequest;
import com.rabbitmq.client.Channel;

import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.BoundMapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;

/**
 * 网管人员账号同步监听(通过RabbitMQ发送).
 *
 * @author jiangxuwen
 * @date 2020/5/20 11:37
 */
@Component
@Slf4j
@ConditionalOnProperty(name = "cmdb.enable.syncOsaFlag", havingValue = "true")
public class CmdbAccountSyncConsumer {

    private static final String DEFAULT_NAMESPACE = "alauda";

    @Autowired
    private DepartmentServiceClient departmentClient;

    @Autowired
    private UserServiceClient userClient;

    @Autowired
    private LdapUserServiceClient ldapUserServiceClient;

    private static final MapperFactory MAPPER_FACTORY = new DefaultMapperFactory.Builder().build();

    private static final MapperFactory MAPPER_FACTORY2 = new DefaultMapperFactory.Builder().build();

    private static final MapperFactory MAPPER_FACTORY3 = new DefaultMapperFactory.Builder().build();

    private static final MapperFactory MAPPER_FACTORY4 = new DefaultMapperFactory.Builder().build();

    static {
        MAPPER_FACTORY.classMap(AccountDetailInfo.class, UserUpdateRequest.class).field("id", "uuid").field("loginName", "code")
                .field("loginName", "ldapId").field("email", "mail").field("mobile", "phone").field("mobile", "mobile").byDefault()
                .register();

        MAPPER_FACTORY2.classMap(AccountDetailInfo.class, UserCreateRequest.class).field("id", "userId").field("loginName", "code")
                .field("email", "mail").field("mobile", "phone").field("mobile", "mobile").field("loginName", "ldapId").byDefault()
                .register();

        MAPPER_FACTORY3.classMap(AccountDetailInfo.class, InsertLdapMemberRequest.class).field("name", "name")
                .field("loginName", "username").field("mobile", "mobile").byDefault().register();

        MAPPER_FACTORY4.classMap(AccountDetailInfo.class, UpdateLdapMemberRequest.class).field("name", "name")
                .field("mobile", "mobile").byDefault().register();
    }

    @RabbitListener(queues = "${account.mq.queue.name}")
    public void consumer(Channel channel, Message message) throws IOException {
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        String messageBody = new String(message.getBody(), Charset.forName("utf-8"));
        // messageBody = messageBody.replaceAll("(\r\n)|\n", "");
        log.info(">>>>>>>[人员账号同步]收到rabbitMQ的消息:{}", messageBody);
        try {
            log.info("[人员账号同步]处理消息，入库");
            AccountInfoDTO accountInfo = new JsonMapper().readValue(messageBody, AccountInfoDTO.class);
            saveAccount(accountInfo);
            // 确认收到消息，false只确认当前consumer一个消息收到，true确认所有consumer获得的消息
            channel.basicAck(deliveryTag, false);
            log.info("<<<<<<<<<<<[人员账号同步]处理完成!");
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
            log.error("[人员账号同步]消息确认失败!", e);
            // channel.basicNack(deliveryTag, false, true);
        }
    }

    private void saveAccount(AccountInfoDTO accountInfo) {
        try {
            AccountInfoData dataEntity = accountInfo.getData();
            List<AccountDetailInfo> addList = dataEntity.getAddList();
            List<AccountDetailInfo> updateList = dataEntity.getUpdateList();
            List<AccountDetailInfo> removeList = dataEntity.getRemoveList();
            log.info("[人员账号同步],待新增的数量:[{}]", addList.size());
            log.info("[人员账号同步],待修改的数量:[{}]", updateList.size());
            log.info("[人员账号同步],待删除的数量:[{}]", removeList.size());
            persist(addList, CmdbOptType.OPT_ADD);
            persist(updateList, CmdbOptType.OPT_MODIFY);
            persist(removeList, CmdbOptType.OPT_DEL);
        } catch (Exception e) {
            log.error("[人员账号同步]入库失败.", e);
        }
    }

    /**
     * 处理入库逻辑.
     * 
     * @param
     * @return
     */
    private void persist(List<AccountDetailInfo> list, CmdbOptType optType) {
        UserBatchCreateRequest createRequest = new UserBatchCreateRequest();
        List<UserCreateRequest> userList = Lists.newArrayList();
        for (AccountDetailInfo domain : list) {
            UserCreateRequest request = convert(domain);
            userList.add(request);
        }
        createRequest.setListUser(userList);
        if (CmdbOptType.OPT_ADD == optType) {
            userClient.batchCreatedUser(createRequest);
            addLdapUser(list);
        }
        if (CmdbOptType.OPT_MODIFY == optType) {
            list.forEach(e -> {
                UserUpdateRequest updateRequest = convert4Update(e);
                userClient.modifyByPrimaryKey(e.getId(), updateRequest);
            });
            updateLdapUser(list);
        }
        if (CmdbOptType.OPT_DEL == optType) {
            userList.forEach(e -> userClient.deleteByPrimaryKey(e.getUserId()));
            deleteLdapUser(list);
        }
    }

    private UserCreateRequest convert(AccountDetailInfo entity) {
        BoundMapperFacade<AccountDetailInfo, UserCreateRequest> mapper = MAPPER_FACTORY2.getMapperFacade(AccountDetailInfo.class,
                UserCreateRequest.class);
        UserCreateRequest request = mapper.map(entity);
        request.setNamespace(DEFAULT_NAMESPACE);
        request.setUserType(1);
        return request;
    }

    private UserUpdateRequest convert4Update(AccountDetailInfo entity) {
        BoundMapperFacade<AccountDetailInfo, UserUpdateRequest> mapper = MAPPER_FACTORY.getMapperFacade(AccountDetailInfo.class,
                UserUpdateRequest.class);
        UserUpdateRequest request = mapper.map(entity);
        request.setNamespace(DEFAULT_NAMESPACE);
        request.setUserType(1);
        return request;
    }

    private void addLdapUser(List<AccountDetailInfo> list) {
        List<InsertLdapMemberRequest> request = Lists.newArrayList();
        list.forEach(e -> {
            request.add(convertLdap(e));
        });
        ldapUserServiceClient.insertLdapMembers(DEFAULT_NAMESPACE, request);
    }

    private void updateLdapUser(List<AccountDetailInfo> list) {
        list.forEach(e -> {
            UpdateLdapMemberRequest request = convertLdap4Update(e);
            ldapUserServiceClient.updateLdapMember(DEFAULT_NAMESPACE, e.getLoginName(), request);
        });
    }

    private void deleteLdapUser(List<AccountDetailInfo> list) {
        list.forEach(e -> ldapUserServiceClient.deleteLdapMember(DEFAULT_NAMESPACE, e.getLoginName()));
    }

    private InsertLdapMemberRequest convertLdap(AccountDetailInfo entity) {
        BoundMapperFacade<AccountDetailInfo, InsertLdapMemberRequest> mapper = MAPPER_FACTORY3
                .getMapperFacade(AccountDetailInfo.class, InsertLdapMemberRequest.class);
        InsertLdapMemberRequest request = mapper.map(entity);
        request.setType("user.alauda");
        request.setProjects(Arrays.asList("miguums_p1"));
        return request;
    }

    private UpdateLdapMemberRequest convertLdap4Update(AccountDetailInfo entity) {
        BoundMapperFacade<AccountDetailInfo, UpdateLdapMemberRequest> mapper = MAPPER_FACTORY4
                .getMapperFacade(AccountDetailInfo.class, UpdateLdapMemberRequest.class);
        UpdateLdapMemberRequest request = mapper.map(entity);
        return request;
    }
}
