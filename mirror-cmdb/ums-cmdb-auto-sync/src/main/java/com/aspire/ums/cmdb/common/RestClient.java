package com.aspire.ums.cmdb.common;

import org.apache.http.HttpEntity;
import org.apache.http.client.entity.EntityBuilder;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.log4j.Logger;

/**
 * @author lupeng
 *
 */
public class RestClient {
	private final Logger logger = Logger.getLogger(RestClient.class);
  
    private HttpClientBuilder builder = HttpClientBuilder.create();
    
    private static RestClient restClient = new RestClient();

    public static RestClient getInstance()
    {
        return restClient;
    }
    

    public CloseableHttpResponse post(String url, String body) throws Exception {
        return request(HttpMethod.POST, url, body);
    }

    public CloseableHttpResponse get(String url) throws Exception {
        return request(HttpMethod.GET, url, null);
    }

    private CloseableHttpResponse request(HttpMethod method, String url, String body)
            throws Exception {
        HttpUriRequest request;
        switch (method) {
        case POST:
            HttpEntity entity = EntityBuilder.create().setContentType(ContentType.APPLICATION_JSON)
                    .setContentEncoding("UTF-8").setText(body).build();
            RequestBuilder tmpBuilder = RequestBuilder.post(url);
            request = tmpBuilder.setEntity(entity).build();
            break;
        case GET:
            RequestBuilder tmp = RequestBuilder.get(url);
            request = tmp.build();
            break;
        case DELETE:
            tmp = RequestBuilder.delete(url);
            request = tmp.build();
            break;
        case PATCH:
            HttpEntity eb = EntityBuilder.create().setText(body).build();
            tmp = RequestBuilder.patch(url);
            request = tmp.setEntity(eb).build();
            break;
        default:
            throw new RuntimeException("cannot pattern method");
        }
        return builder.build().execute(request);
    }

    private enum HttpMethod {
        POST, GET, DELETE, UPDATE, PATCH;
    }

}
