package amt.common.generic;


import am.infrastructure.data.dto.user.LoginData;
import am.main.common.validation.FormValidation;
import am.main.exception.AMError;
import org.junit.Assert;
import org.unitils.thirdparty.org.apache.commons.io.IOUtils;

import javax.ws.rs.core.Response;
import java.io.InputStream;
import java.util.Date;
import java.util.List;
import java.util.Random;

import static amt.common.constants.Error.NOT_AUTHORIZED;

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

    public static void postSecuredFormValidation(String rest, String path, LoginData loginData, Object data, FormValidation expected) throws Exception{
        Response response;

        if(loginData != null)
            response = RestUtil.postSecured(rest, path, data, loginData);
        else
            response = RestUtil.post(rest, path, data);

        Assert.assertEquals("Response Status failed", Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());

        AMError amError = response.readEntity(AMError.class);

        if(amError == null || amError.getValidation() == null)
            Assert.fail("No Error Returned for this Rest Call");

        FormValidation actual = amError.getValidation();
        Util.validateInvalidFormField(actual, expected);
    }
    public static void postSecuredFormValidation(String rest, String path, LoginData loginData, Object data, List<String> pathParams, FormValidation expected) throws Exception{
        Response response;

        if(loginData != null)
            response = RestUtil.postSecured(rest, path, data, pathParams, loginData);
        else
            response = RestUtil.post(rest, path, data, pathParams);

        Assert.assertEquals("Response Status failed", Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());

        AMError amError = response.readEntity(AMError.class);

        if(amError == null || amError.getValidation() == null)
            Assert.fail("No Error Returned for this Rest Call");

        FormValidation actual = amError.getValidation();
        Util.validateInvalidFormField(actual, expected);
    }
    public static void postFormValidation(String rest, String path, Object data, FormValidation expected) throws Exception{
        postSecuredFormValidation(rest, path, null, data, expected);
    }

    public static void postSecuredStringError(String rest, String path, LoginData loginData, Object data, String expectedError) throws Exception{
        postSecuredStringError(rest, path, loginData, null, data, expectedError);
    }
    public static void postSecuredStringError(String rest, String path, LoginData loginData, List<String> pathParams, Object data, String expectedError) throws Exception{
        Response response;

        if(loginData != null)
            response = RestUtil.postSecured(rest, path, data, pathParams, loginData);
        else
            response = RestUtil.post(rest, path, data, pathParams);

        if(expectedError.equals(NOT_AUTHORIZED))
            Assert.assertEquals("Response Status failed", Response.Status.UNAUTHORIZED.getStatusCode(), response.getStatus());
        else
            Assert.assertEquals("Response Status failed", Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());

        String actualErrorStr;
        if(expectedError.equals(NOT_AUTHORIZED))
            actualErrorStr = response.readEntity(String.class);
        else
            actualErrorStr = response.readEntity(AMError.class).getMessage();
        Assert.assertEquals("Error returned doesn't match", expectedError, actualErrorStr);
    }
    public static void postStringError(String rest, String path, Object data, String expectedError) throws Exception{
        postSecuredStringError(rest, path, null, data, expectedError);
    }

    public static void getSecuredFormValidation(String rest, String path, LoginData loginData, FormValidation expected) throws Exception{
        Response response;

        if(loginData != null)
            response = RestUtil.getSecured(rest, path, loginData);
        else
            response = RestUtil.get(rest, path);

        Assert.assertEquals("Response Status failed", Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());

        AMError amError = response.readEntity(AMError.class);

        if(amError == null || amError.getValidation() == null)
            Assert.fail("No Error Returned for this Rest Call");

        FormValidation actual = amError.getValidation();
        Util.validateInvalidFormField(actual, expected);
    }
    public static void getFormValidation(String rest, String path, FormValidation expected) throws Exception{
        getSecuredFormValidation(rest, path, null, expected);
    }

    public static void getSecuredStringError(String rest, String path, LoginData loginData, String expectedError) throws Exception{
        getSecuredStringError(rest, path, loginData, null, expectedError);
    }
    public static void getSecuredStringError(String rest, String path, LoginData loginData, List<String> pathParams, String expectedError) throws Exception{
        Response response;

        if(loginData != null)
            response = RestUtil.getSecured(rest, path, loginData, pathParams);
        else
            response = RestUtil.get(rest, path, pathParams);

        if(expectedError.equals(NOT_AUTHORIZED))
            Assert.assertEquals("Response Status failed", Response.Status.UNAUTHORIZED.getStatusCode(), response.getStatus());
        else
            Assert.assertEquals("Response Status failed", Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());

        String actualErrorStr;
        if(expectedError.equals(NOT_AUTHORIZED))
            actualErrorStr = response.readEntity(String.class);
        else
            actualErrorStr = response.readEntity(AMError.class).getMessage();
        Assert.assertEquals("Error returned doesn't match", expectedError, actualErrorStr);
    }
    public static void getStringError(String rest, String path, String expectedError) throws Exception{
        getSecuredStringError(rest, path, null, expectedError);
    }
    public static void getStringError(String rest, String path, List<String> pathParams, String expectedError) throws Exception{
        getSecuredStringError(rest, path, null, pathParams, expectedError);
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
