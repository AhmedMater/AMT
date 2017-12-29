package amt.testCases.course;

import am.infrastructure.data.dto.filters.CourseListFilter;
import am.infrastructure.data.dto.user.LoginData;
import am.infrastructure.data.view.resultset.CourseListRS;
import am.infrastructure.data.view.ui.CourseListUI;
import am.main.api.AppConfigManager;
import am.main.api.ErrorHandler;
import am.main.api.InfoHandler;
import am.main.api.db.DBManager;
import am.main.session.AppSession;
import am.shared.session.Phase;
import amt.common.DeploymentManger;
import amt.common.constants.Params;
import amt.common.constants.Rest;
import amt.common.enums.Scripts;
import amt.common.generic.DataGenerator;
import amt.common.generic.Repository;
import amt.common.generic.RestUtil;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

import static am.main.data.enums.Interface.ARQUILLIAN;
import static am.main.data.enums.Source.INTEGRATION_TEST;

/**
 * Created by ahmed.motair on 12/11/2017.
 */
@RunWith(Arquillian.class)
public class CourseList {
    @Inject private Repository repository;
    @Inject private DBManager dbManager;
    @Inject private AppConfigManager appConfigManager;
    @Inject private DataGenerator dataGenerator;
    @Inject private ErrorHandler errorHandler;
    @Inject private InfoHandler infoHandler;

    private static final String CLASS = "CourseList";

    private AppSession appSession = new AppSession(INTEGRATION_TEST, ARQUILLIAN, Phase.INTEGRATION_TEST, errorHandler, infoHandler);

    @Deployment
    public static WebArchive createDeployment() {
        return DeploymentManger.createDeployment(Scripts.getAllScripts());
    }

    @Test
    @InSequence(1)
    public void preparingDatabaseData() throws Exception{
        try {
            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
            repository.executeScript(Scripts.COURSE_LEVEL_LOOKUP);
            repository.executeScript(Scripts.COURSE_TYPE_LOOKUP);
            repository.executeScript(Scripts.CONTENT_STATUS_LOOKUP);
            repository.executeScript(Scripts.MATERIAL_TYPE_LOOKUP);
            repository.executeScript(Scripts.ROLE_LOOKUP);

            repository.executeScript(Scripts.LIST_OF_39_COURSES);
            repository.executeScript(Scripts.STUDENT_USER_LOOKUP);
        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }

    /**
     * By Default the List will be ordered Descending by Course Creation Date
     * @throws Exception
     */
    @Test
    @InSequence(2)
    public void getAllCourses_NoFilters() throws Exception{
        try {
            CourseListFilter filters = new CourseListFilter();
            filters.setPageNum(0);

            LoginData loginData = Params.studentLoginData;

            Response response = RestUtil.postSecured(Rest.COURSE.RESOURCE, Rest.COURSE.LIST, filters, loginData);
            Assert.assertEquals("Response Status is wrong", Response.Status.OK.getStatusCode(), response.getStatus());

            CourseListRS resultSet = response.readEntity(CourseListRS.class);
            List<String> expectedCourses = new ArrayList<>();
            expectedCourses.add("Cor0000028AcIn20171217");
            expectedCourses.add("Cor0000029PrBe20171214");
            expectedCourses.add("Cor0000030PrBe20171213");
            expectedCourses.add("Cor0000034PrAd20171025");
            expectedCourses.add("Cor0000035PrBe20171025");
            expectedCourses.add("Cor0000031AcAd20171023");
            expectedCourses.add("Cor0000032PrBe20171022");
            expectedCourses.add("Cor0000033PrBe20171021");
            expectedCourses.add("Cor0000004PrAd20171006");
            expectedCourses.add("Cor0000005AcAd20171002");

            int counter = 0;
            for (CourseListUI course : resultSet.getData())
                if(!course.getCourseID().equals(expectedCourses.get(counter++)))
                    Assert.fail("Expected Course ID: " + expectedCourses.get(--counter) + ", But Actual Course ID: " + course.getCourseID());
        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }
}
