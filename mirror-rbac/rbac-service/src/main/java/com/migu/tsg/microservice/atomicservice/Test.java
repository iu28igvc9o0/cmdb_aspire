package com.migu.tsg.microservice.atomicservice;

import java.util.ArrayList;
import java.util.List;

import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.representations.idm.EventRepresentation;
import org.keycloak.representations.idm.UserRepresentation;

public class Test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

        // 建立连接
        Keycloak kcMaster = Keycloak.getInstance("http://10.12.70.40:8180/auth", "master", "admin", "admin", "admin-cli");
        // 切换到所需要的realm
        RealmResource realmResource = kcMaster.realm("demo_realm");

        // 获取keycloak上的events 可以做账号登录登出日志保存和查询
//       List<EventRepresentation> eventRepresentations = realmResource.getEvents();
//       for ( EventRepresentation e : eventRepresentations) {
//           System.out.println(e.getType() + e.getDetails()); // type 有 LOGIN LOGIN_ERROR LOGOUT
//       }
        // 根据前台传入的用户名获取keycloak上的 User对象
        UserRepresentation updateUser = realmResource.users().search("zxh2").get(0);

        // 设置user是否启用 可用做启用/账号的锁定和解锁(不知道解锁和启用是不是一个概念，只是条件不同？)
        updateUser.setEnabled(false);

//        设置setRequiredActions 可以做密码超过3个月强制修改 （RequiredActions 设置成UPDATE_PASSWORD后当前用户登录时会被要求更新密码）
//        List<String> requireAction = new ArrayList<>();
//        requireAction.add("UPDATE_PASSWORD");
//        updateUser.setRequiredActions(requireAction);

        // 设置后的user更新到keycloak上面
        realmResource.users().get(updateUser.getId()).update(updateUser);
	}

}
