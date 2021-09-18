
import com.migu.tsg.microservice.atomicservice.common.util.HttpsUtil;


/**
 * @BelongsProject: mirror-rbac
 * @BelongsPackage: PACKAGE_NAME
 * @Author: baiwenping
 * @CreateTime: 2020-03-19 16:53
 * @Description: ${Description}
 */
public class TestHttps {

    public static void main(String[] args) {
        String url = "https://10.12.70.40:8080/auth/realms/demo_realm/protocol/openid-connect/token";
        StringBuilder param = new StringBuilder("client_id=");
        param.append("prod_vue").append("&grant_type=password&username=").append("alauda");
        param.append("&password=");
        param.append("62c8ad0a15d9d1ca38d5dee762a16e01");
        System.out.println(HttpsUtil.doPostForKeycloak(url, param.toString()));
    }
}
