package com.aspire.ums.cmdb.ipCollect.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.util.Map;
import java.util.TreeMap;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpStatus;

import com.aspire.ums.cmdb.ipCollect.constants.VmwareConfigConstant;
import com.aspire.ums.cmdb.ipCollect.exception.CollectException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EasyOpsAPI {

    String ACCESS_KEY, SECRET_KEY;

    String URL;

    Map<String, String> headers;

    public EasyOpsAPI() {
        this(VmwareConfigConstant.ACCESS_KEY, VmwareConfigConstant.SECRET_KEY, VmwareConfigConstant.URL);
    }

    public EasyOpsAPI(String ACCESS_KEY, String SECRET_KEY, String URL) {

        Map<String, String> hd = new TreeMap<String, String>();
        hd.put("host", "openapi.easyops-only.com");
        hd.put("Content-Type", "application/json");
        hd.put("source", "CMDB");
        this.ACCESS_KEY = ACCESS_KEY;
        this.SECRET_KEY = SECRET_KEY;
        this.URL = URL;
        this.headers = hd;
    }

    private String genSignature(String uri, long request_time, Map<String, String> data, String requestBody, String method) {

        String body_content = "";
        String url_params = "";
        String str_sign = "";
        String signature = "";
        if ("GET".equals(method)) {
            for (Object key : data.keySet()) {
                url_params = url_params + key + data.get(key.toString());
            }

        }
        if ("PUT".equals(method) || "POST".equals(method)) {
            body_content = MD5(requestBody);
        }
        str_sign = method + "\n" + uri + "\n" + url_params + "\n" + this.headers.get("Content-Type") + "\n" + body_content + "\n"
                + request_time + "\n" + this.ACCESS_KEY;
        signature = HmacSHA1Encrypt(str_sign, this.SECRET_KEY);
        return signature;
    }

    private String sendGet(String url, Map<String, String> data, String requestBody, String method) throws CollectException {
        String result = null;
        String param = "";
        OutputStream outputStream = null;
        InputStream inputStream = null;
        BufferedReader bufferedReader = null;
        HttpURLConnection connection = null;
        StringBuilder sb = new StringBuilder();
        int timeout = 90000;
        for (Object ks : data.keySet()) {
            param += "&" + ks + "=" + data.get(ks.toString());
        }
        param = param.substring(1);
        System.setProperty("sun.net.http.allowRestrictedHeaders", "true");
        // FIXME:在Tomcat的catalina.sh文件中，加入 JAVA_OPTS="$JAVA_OPTS -Dsun.net.http.allowRestrictedHeaders=true"
        try {
            URL realUrl = new URL(url + "?" + param);
//            log.info("easyOpsAPI send url={}", realUrl.toString());
//             log.info("requestBody==={}", requestBody);
            connection = (HttpURLConnection) realUrl.openConnection();
            connection.setRequestMethod(method);
            connection.setConnectTimeout(timeout);
            connection.setReadTimeout(timeout);

            for (Object hd : this.headers.keySet()) {
                connection.setRequestProperty(hd.toString(), this.headers.get(hd.toString()));
            }

            connection.setDoInput(true);
            if ("GET".equals(method)) {
                connection.setDoOutput(false);
            } else {
                connection.setDoOutput(true);
            }
            connection.connect();

            if ("POST".equals(method)) {
                outputStream = connection.getOutputStream();
                outputStream.write(requestBody.getBytes("utf-8"));
                outputStream.flush();
            }

            // log.info("responseCode={}", connection.getResponseCode());
            if (HttpStatus.SC_OK != connection.getResponseCode()) {
                log.error("error=={}", IOUtils.toString(connection.getErrorStream()));
            }
            inputStream = connection.getInputStream();
            if (inputStream != null) {
                // log.info("response content={}", inputStream.toString());
                bufferedReader = new BufferedReader(new InputStreamReader((inputStream)));
                char[] charBuffer = new char[128];
                int byteReads = -1;
                while ((byteReads = bufferedReader.read(charBuffer)) > 0) {
                    sb.append(charBuffer, 0, byteReads);
                }
            } else {
                sb.append("");
            }
            result = sb.toString();
        } catch (Exception e) {
            // log.error("", e);
            throw new CollectException(e);
        } finally {
            // luowenbo 2020.07.17 资源未释放缺陷
            if (null != connection) {
                connection.disconnect();
            }
            if (null != outputStream) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    log.error("", e);
                }
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    log.error("", e);
                }
            }
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    log.error("", e);
                }
            }
        }
        return result;
    }

    public static String HmacSHA1Encrypt(String encryptText, String encryptKey) {
        byte[] data = encryptKey.getBytes();
        SecretKey secretKey = new SecretKeySpec(data, "HmacSHA1");
        try {
            Mac mac = Mac.getInstance("HmacSHA1");
            mac.init(secretKey);
            byte[] text = encryptText.getBytes();
            return Hex.encodeHexString(mac.doFinal(text));
        } catch (Exception ex) {
            log.error("", ex);
            return null;
        }
    }

    private String MD5(String str) {
        String result = "";
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update((str).getBytes("UTF-8"));
            byte b[] = md5.digest();

            int i;
            StringBuffer buf = new StringBuffer("");

            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0) {
                    i += 256;
                }
                if (i < 16) {
                    buf.append("0");
                }
                buf.append(Integer.toHexString(i));
            }

            result = buf.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    public String doApi(String uri, Map<String, String> data, String requestBody, String method) {
        long request_time = System.currentTimeMillis() / 1000L;
        String signature = this.genSignature(uri, request_time, data, requestBody, method);
        String url = this.URL + uri;
        data.put("accesskey", this.ACCESS_KEY);
        data.put("signature", signature);
        data.put("expires", request_time + "");
        String response = sendGet(url, data, requestBody, method);
//        log.info("response==={}", response);
        return response;
    }
}
