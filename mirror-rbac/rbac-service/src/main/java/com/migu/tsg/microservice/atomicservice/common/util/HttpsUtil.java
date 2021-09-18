package com.migu.tsg.microservice.atomicservice.common.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Maps;
import com.migu.tsg.microservice.atomicservice.common.constant.CommonConstants;
import com.migu.tsg.microservice.atomicservice.common.exception.BaseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StreamUtils;

import javax.net.ssl.*;
import java.io.*;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Map;

/**
 * @BelongsProject: mirror-rbac
 * @BelongsPackage: com.migu.tsg.microservice.atomicservice.common.util
 * @Author: baiwenping
 * @CreateTime: 2020-03-19 17:14
 * @Description: ${Description}
 */
@Slf4j
public class HttpsUtil {
    public static Map<String, Object> doPostForKeycloak(String uri, String data) {
        Map<String, Object> map = Maps.newHashMap();
        OutputStream outputStream = null;
        try {
            HttpsURLConnection httpsConn = getHttpsURLConnection(uri, "POST");
//            setBytesToStream(httpsConn.getOutputStream(), data.getBytes());
            outputStream = httpsConn.getOutputStream();
            StreamUtils.copy(data.getBytes(), outputStream);
            outputStream.flush();
            String result = new String (getBytesFromStream(httpsConn.getInputStream()));
            ObjectMapper objectMapper = new ObjectMapper();

            map.putAll(objectMapper.readValue(result, Map.class));
            map.put("code", "0000");
        } catch (IOException e) {
            map.put("code", "9999");
            map.put("message", e.getMessage());
            log.error("", e);
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {

                }
            }
            return map;
        }

    }

//    public static byte[] doGet(String uri) throws IOException {
//        HttpsURLConnection httpsConn = getHttpsURLConnection(uri, "GET");
//        return getBytesFromStream(httpsConn.getInputStream());
//    }
//
//    public static byte[] doPost(String uri, String data) throws IOException {
//        HttpsURLConnection httpsConn = getHttpsURLConnection(uri, "POST");
//        setBytesToStream(httpsConn.getOutputStream(), data.getBytes());
//        return getBytesFromStream(httpsConn.getInputStream());
//    }

    private static HttpsURLConnection getHttpsURLConnection(String uri, String method) throws IOException {
        SSLContext ctx = null;
        try {
            ctx = SSLContext.getInstance("TLS");
            ctx.init(new KeyManager[0], new TrustManager[] { new DefaultTrustManager() }, new SecureRandom());
        } catch (KeyManagementException e) {
            log.error("", e);
            throw new BaseException(e.getMessage());
        } catch (NoSuchAlgorithmException e) {
            log.error("", e);
            throw new BaseException(e.getMessage());
        }
        SSLSocketFactory ssf = ctx.getSocketFactory();

        URL url = new URL(uri);
        HttpsURLConnection httpsConn = (HttpsURLConnection) url.openConnection();
        httpsConn.setSSLSocketFactory(ssf);
        httpsConn.setHostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String arg0, SSLSession arg1) {
                return true;
            }
        });
        httpsConn.setRequestMethod(method);
        httpsConn.setDoInput(true);
        httpsConn.setDoOutput(true);
        return httpsConn;
    }

    private static byte[] getBytesFromStream(InputStream is) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            byte[] kb = new byte[1024];
            int len;
            while ((len = is.read(kb)) != -1) {
                baos.write(kb, 0, len);
            }
            byte[] bytes = baos.toByteArray();

            return bytes;
        }finally {
            try {
                baos.close();
            } catch (IOException e) {

            }
            try {
                is.close();
            } catch (IOException e) {

            }
        }


    }

//    private static void setBytesToStream(OutputStream os, byte[] bytes) throws IOException {
//        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
//        try {
//            byte[] kb = new byte[1024];
//            int len;
//            while ((len = bais.read(kb)) != -1) {
//                os.write(kb, 0, len);
//            }
//            os.flush();
//        }finally {
//            try {
//                os.close();
//            } catch (IOException e) {
//
//            }
//            try {
//                bais.close();
//            } catch (IOException e) {
//
//            }
//        }
//    }

    private static final class DefaultTrustManager implements X509TrustManager {
        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return CommonConstants.X509CERTIFICATE_NULLS;
        }
    }
}
