package amt.common;

import amt.common.constants.Method;
import amt.common.constants.Resources;
import amt.common.constants.Rest;
import amt.common.constants.SQL;
import amt.common.generic.Repository;
import amt.common.generic.Util;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;

import java.io.File;
import java.util.List;

/**
 * Created by ahmed.motair on 11/18/2017.
 */
public class Deployment {

    private static final String AMT_SERVICES_PATH = "F:\\Giza-Projects\\AMT\\AMT-Code\\target\\AMT-Services.war";
    private static final String BASE_URL = "http://localhost/AMT-Services/api";

    private static Class[] classes = {
            Repository.class, Util.class, SQL.class, Error.class, Method.class, Resources.class, Rest.class
    };

    public static WebArchive createDeployment(List<String> ... resourceFiles) {
        WebArchive archive = ShrinkWrap.createFromZipFile(WebArchive.class,
                new File(AMT_SERVICES_PATH)).addClasses(classes);

        for (List<String> resources : resourceFiles)
            for (String resource: resources)
                archive.addAsResource(resource, resource);

        return archive;
    }
}
