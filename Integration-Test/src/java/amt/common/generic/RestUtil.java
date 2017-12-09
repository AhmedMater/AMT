package amt.common.generic;

import am.infrastructure.data.dto.user.LoginData;
import am.infrastructure.data.view.AuthenticatedUser;
import am.rest.filters.LoggingFilter;
import amt.common.constants.Rest;
import amt.common.enums.Method;
import org.glassfish.jersey.client.ClientConfig;
import org.junit.Assert;

import javax.ws.rs.client.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.text.MessageFormat;
import java.util.List;
import java.util.Map;

/**
 * Created by ahmed.motair on 11/28/2017.
 */
public class RestUtil {
    public static Response get(String resource, String path, Map<String, Object> queryParams) throws Exception{
        return restClient(resource, path, false, null, Method.GET, queryParams, null, null);
    }
    public static Response get(String resource, String path, List<String> pathParams) throws Exception{
        return restClient(resource, path, false, null, Method.GET, null, pathParams, null);
    }
    public static Response get(String resource, String path, Map<String, Object> queryParams, List<String> pathParams) throws Exception{
        return restClient(resource, path, false, null, Method.GET, queryParams, pathParams, null);
    }
    public static Response get(String resource, String path) throws Exception{
        return restClient(resource, path, false, null, Method.GET, null, null, null);
    }

    public static Response post(String resource, String path, Object payload) throws Exception{
        return restClient(resource, path, false, null, Method.POST, null, null, payload);
    }
    public static Response post(String resource, String path, Object payload, List<String> pathParam) throws Exception{
        return restClient(resource, path, false, null, Method.POST, null, pathParam, payload);
    }

    public static Response getSecured(String resource, String path, LoginData loginData, Map<String, Object> queryParams, List<String> pathParams) throws Exception{
        Response response = restClient(Rest.USER.RESOURCE, Rest.USER.LOGIN, false, null, Method.POST, null, null, loginData);
        String token = response.readEntity(AuthenticatedUser.class).getToken();

        return restClient(resource, path, true, token, Method.GET, queryParams, pathParams, null);
    }
    public static Response getSecured(String resource, String path, LoginData loginData, List<String> pathParams) throws Exception{
        return getSecured(resource, path, loginData, null, pathParams);
    }
    public static Response getSecured(String resource, String path, LoginData loginData, Map<String, Object> queryParams) throws Exception{
        return getSecured(resource, path, loginData, queryParams, null);
    }
    public static Response getSecured(String resource, String path, LoginData loginData) throws Exception{
        return getSecured(resource, path, loginData, null, null);
    }

    public static Response postSecured(String resource, String path, Object payload, LoginData loginData) throws Exception{
        return postSecured(resource, path, payload, null, loginData);
    }
    public static Response postSecured(String resource, String path, Object payload, List<String> pathParams, LoginData loginData) throws Exception{
        Response response = restClient(Rest.USER.RESOURCE, Rest.USER.LOGIN, false, null, Method.POST, null, null, loginData);
        Assert.assertEquals("Login failed", Response.Status.OK.getStatusCode(), response.getStatus());
        String token = response.readEntity(AuthenticatedUser.class).getToken();

        return restClient(resource, path, true, token, Method.POST, null, pathParams, payload);
    }

    private static Response restClient(String resource, String path, boolean secured, String token,
                                       Method method, Map<String, Object> queryParams, List<String> pathParams, Object payload) throws Exception{
        Invocation.Builder invocationBuilder;

        Client client = ClientBuilder.newClient(new ClientConfig().register(LoggingFilter.class));

        if(pathParams != null)
            path = MessageFormat.format(path, pathParams.toArray());

        WebTarget webTarget = client.target(resource).path(path);

        if (queryParams != null)
            for (String key : queryParams.keySet())
                webTarget = webTarget.queryParam(key, queryParams.get(key));

        if (secured)
            invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON).header("Authorization", "Bearer " + token);
        else
            invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);

        Response response = null;
        if (method.equals(Method.PUT))
            response = invocationBuilder.put(Entity.entity(payload, MediaType.APPLICATION_JSON + ";charset=utf-8"));
        else if (method.equals(Method.POST))
            response = invocationBuilder.post(Entity.entity(payload, MediaType.APPLICATION_JSON + ";charset=utf-8"));
        else if (method.equals(Method.GET))
            response = invocationBuilder.get();
        else
            throw new Exception("Invalid REST Method");

        if (response != null && response.getStatus() == 404)
            throw new Exception("Backend System is Down!!!");


        return response;
    }

}
