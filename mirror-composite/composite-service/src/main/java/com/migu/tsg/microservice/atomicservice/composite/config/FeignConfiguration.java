package com.migu.tsg.microservice.atomicservice.composite.config;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.Base64Utils;

import com.migu.tsg.microservice.atomicservice.composite.clientservice.FeignExceptionDecoder;
import com.migu.tsg.microservice.atomicservice.composite.controller.authcontext.RequestAuthContext;
import com.migu.tsg.microservice.atomicservice.composite.controller.util.JsonUtil;
import com.migu.tsg.microservice.atomicservice.composite.controller.util.ResourceAuthHelper;

import feign.Logger;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import feign.codec.ErrorDecoder;
import rx.exceptions.CompositeException;

/**
 * Feign全局配置 Project Name:composite-service File Name:FeignConfiguration.java Package
 * Name:com.migu.tsg.microservice.atomicservice.composite.config ClassName: FeignConfiguration <br/>
 * date: 2017年10月23日 下午7:53:52 <br/>
 * Feign全局配置
 * 
 * @author pengguihua
 * @version
 * @since JDK 1.6
 */
@Configuration
public class FeignConfiguration {

    public static final String RES_FILTER_HEADER = "head_resFilter";

    @Bean
    public RequestInterceptor requestInterceptor(ResourceAuthHelper resAuthHelper) {
        return new AuthInfoInterceptor(resAuthHelper);
    }

    private static class AuthInfoInterceptor implements RequestInterceptor {

        private ResourceAuthHelper resAuthHelper;

        public AuthInfoInterceptor(ResourceAuthHelper resAuthHelper) {
            this.resAuthHelper = resAuthHelper;
        }

        @Override
        public void apply(RequestTemplate template) {
            RequestAuthContext context = RequestAuthContext.currentRequestAuthContext();
            if (true) {
                return;
            }
            RequestAuthContext.RequestHeadUser user = context.getUser();
            template.header("head_orgAccount", user.getNamespace());
            template.header("head_userName", user.getUsername());
            template.header("head_isAdmin", String.valueOf(user.isAdmin()));
            template.header("head_isSuperUser", String.valueOf(user.isSuperUser()));

            // 如果为管理员, 或者配置为不需要解析资源权限配置, 直接返回
            if (user.isSuperUser() || user.isAdmin() || !Boolean.TRUE.equals(context.getLoadResFilter())) {
                return;
            }

            // 加载资源权限过滤配置, 放入HTTP请求头
            String cacheResFilterConfig = context.getCtxCacheResFilterConfig();
            if (StringUtils.isNotBlank(cacheResFilterConfig)) {
                template.header(RES_FILTER_HEADER, cacheResFilterConfig);
                return;
            }

            Map<String, Set<String>> resFilterConfig = resAuthHelper.verifyGetResource(user);
            if (MapUtils.isEmpty(resFilterConfig)) {
                return;
            }
            resFilterConfig = hanldeRoomConfig(resFilterConfig);
            try {
                byte[] jsonByteArr = JsonUtil.toJacksonJson(resFilterConfig).getBytes("UTF-8");
                String safeBase64 = Base64Utils.encodeToUrlSafeString(jsonByteArr);
                context.setCtxCacheResFilterConfig(safeBase64);
                template.header(RES_FILTER_HEADER, safeBase64);
            } catch (UnsupportedEncodingException e) {
                throw new CompositeException(e);
            }
        }

        private Map<String, Set<String>> hanldeRoomConfig(final Map<String, Set<String>> resFilterConfig) {
            Set<String> areaSet = resFilterConfig.get(ResourceAuthHelper.AREA);
            if (CollectionUtils.isEmpty(areaSet)) {
                return resFilterConfig;
            }
            List<String> concatList = areaSet.stream().filter(area -> StringUtils.isNotBlank(area) && area.indexOf("_") > 0)
                    .collect(Collectors.toList());
            Set<String> roomList = concatList.stream().map(area -> {
                areaSet.remove(area);
                int idx = area.indexOf("_");
                String idcType = area.substring(0, idx);
                areaSet.add(idcType);
                return area.substring(idx + 1);
            }).collect(Collectors.toSet());
            resFilterConfig.put(ResourceAuthHelper.ROOM, roomList);
            return resFilterConfig;
        }
    }

    @Bean
    public ErrorDecoder customErrorDecoder() {
        return new FeignExceptionDecoder();
    }

    @Bean
    public Logger.Level level() {
        return Logger.Level.FULL;
    }
}
