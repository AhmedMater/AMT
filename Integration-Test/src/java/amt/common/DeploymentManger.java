package amt.common;

import amt.common.constants.Params;
import amt.common.constants.Rest;
import amt.common.constants.ScriptPaths;
import amt.common.enums.Method;
import amt.common.enums.Scripts;
import amt.common.generic.DataGenerator;
import amt.common.generic.Repository;
import amt.common.generic.RestUtil;
import amt.common.generic.Util;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;

import java.io.File;
import java.util.List;

/**
 * Created by ahmed.motair on 11/18/2017.
 */
public class DeploymentManger {

    private static final String APPS_PATH = "F:\\Giza-Projects\\AMT\\AMT-Code\\target\\";

    private static final String AMT_SERVICES_PATH = "AMT-Services.war";
    private static final String AMT_LOGGER_PATH = "AMT-LoggerData.war";
    private static final String BASE_URL = "http://localhost/AMT-Services/api";

    private static Class[] classes = {
            Repository.class, DataGenerator.class, RestUtil.class, Params.class,
            Util.class, ScriptPaths.class, Scripts.class, Error.class, Method.class, Rest.class
    };

    public static WebArchive AMTServicesWAR(List<String> ... resourceFiles) {
        WebArchive archive = ShrinkWrap.
                createFromZipFile(WebArchive.class,
                new File(AMT_SERVICES_PATH)).addClasses(classes);

        for (List<String> resources : resourceFiles)
            for (String resource: resources)
                archive.addAsResource(resource, resource);

        return archive;
    }

    public static WebArchive AMTLoggerWAR(List<String> ... resourceFiles) {
        WebArchive archive = ShrinkWrap.createFromZipFile(WebArchive.class,
                new File(AMT_LOGGER_PATH)).addClasses(classes);

        for (List<String> resources : resourceFiles)
            for (String resource: resources)
                archive.addAsResource(resource, resource);

        return archive;
    }
}
