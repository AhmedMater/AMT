package amt.common.generic;


import am.infrastructure.data.dto.LoginData;
import am.main.common.validation.FormValidation;
import am.rest.filters.LoggingFilter;
import amt.common.constants.Rest;
import amt.common.enums.Method;
import org.glassfish.jersey.client.ClientConfig;
import org.junit.Assert;
import org.unitils.thirdparty.org.apache.commons.io.IOUtils;

import javax.ws.rs.client.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Created by ahmed.motair on 11/19/2017.
 */
public class Util {
    public static String readResourceFile(String path) throws Exception{
        InputStream stream = Util.class.getResourceAsStream("/" + path);
        String data = IOUtils.toString(stream, "UTF-8");
        stream.close();
        return data;
    }

    public static Response restGETClient(String resource, String path, Map<String, Object> queryParams) throws Exception{
        return restClient(resource, path, false, null, Method.GET, queryParams, null, null);
    }

    public static Response restPOSTClient(String resource, String path,Object payload) throws Exception{
        return restClient(resource, path, false, null, Method.POST, null, null, payload);
    }

    public static Response restGETSecuredClient(String resource, String path, Map<String, Object> queryParams, LoginData loginData) throws Exception{
        Response response = restClient(Rest.USER.RESOURCE, Rest.USER.REGISTER, false, null, Method.POST, null, null, loginData);
        String token = response.readEntity(String.class);

        return restClient(resource, path, true, token, Method.GET, queryParams, null, null);
    }

    public static Response restPOSTSecuredClient(String resource, String path, Object payload, LoginData loginData) throws Exception{
        Response response = restClient(Rest.USER.RESOURCE, Rest.USER.REGISTER, false, null, Method.POST, null, null, loginData);
        String token = response.readEntity(String.class);

        return restClient(resource, path, true, token, Method.POST, null, null, payload);
    }

    private static Response restClient(String resource, String path, boolean secured, String token,
               Method method, Map<String, Object> queryParams, List<String> pathParams, Object payload) throws Exception{
        Invocation.Builder invocationBuilder;
        Response response = null;

        Client client = ClientBuilder.newClient(new ClientConfig().register(LoggingFilter.class));

        if(pathParams != null)
            path = MessageFormat.format(path, pathParams);

        WebTarget webTarget = client.target(resource).path(path);

        if (queryParams != null)
            for (String key : queryParams.keySet())
                webTarget = webTarget.queryParam(key, queryParams.get(key));

        if (secured)
            invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON).header("Authorization", "Bearer " + token);
        else
            invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);

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

    public static void callRestForFormValidation(String rest, String path, Object data, FormValidation expected) throws Exception{
        Response response = restPOSTClient(rest, path, data);
        Assert.assertEquals("Response Status failed", Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());

        FormValidation actual = response.readEntity(FormValidation.class);
        Util.validateInvalidFormField(actual, expected);
    }
    public static void callRestForStringError(String rest, String path, Object data, String expectedError) throws Exception{
        Response response = restPOSTClient(rest, path, data);
        Assert.assertEquals("Response Status failed", Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());

        String actualError = response.readEntity(String.class);
        Assert.assertEquals("Error returned doesn't match", expectedError, actualError);
    }

    public static Boolean isEqualDates(Date expected, Date actual){
        long diff = actual.getTime() - expected.getTime();
        return (Math.abs(diff) < 10000);
    }

    public static void validateInvalidFormField(FormValidation actual, FormValidation expected){
        Assert.assertEquals("Main Error failed", expected.getMainError(), actual.getMainError());

        if(expected.getFormErrors().size() != actual.getFormErrors().size())
            Assert.fail("Form validation errors aren't complete");

        boolean subErrorPassed = false;
        for (Object expectedError : expected.getFormErrors()) {
            for (Object actualError : actual.getFormErrors()) {
                if(expectedError.equals(actualError)) {
                    subErrorPassed = true;
                    break;
                }
            }

            if(!subErrorPassed)
                Assert.fail("Form Validation Error '" + expectedError + "' is n't found");
            else
                subErrorPassed = false;
        }
    }

    public static String generateString(int charNum){
        char[] chars = "abcdefghijklmnopqrstuvwxyz".toCharArray();
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < charNum; i++) {
            char c = chars[random.nextInt(chars.length)];
            sb.append(c);
        }
        return sb.toString();
    }
}
