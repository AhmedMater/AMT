package amt.testCases.course;

import am.infrastructure.data.dto.filters.CourseListFilter;
import am.infrastructure.data.dto.user.LoginData;
import am.infrastructure.data.view.resultset.CourseListRS;
import am.infrastructure.data.view.ui.CourseListUI;
import am.main.api.AppConfigManager;
import am.main.api.ErrorHandler;
import am.main.api.InfoHandler;
import am.main.api.db.DBManager;
import am.main.api.validation.FormValidation;
import am.main.common.RegExp;
import am.main.data.dto.SortingInfo;
import am.main.session.AppSession;
import am.shared.enums.Phase;
import amt.common.DeploymentManger;
import amt.common.constants.Error;
import amt.common.constants.Params;
import amt.common.constants.Rest;
import amt.common.enums.Scripts;
import amt.common.generic.DataGenerator;
import amt.common.generic.Repository;
import amt.common.generic.RestUtil;
import amt.common.generic.Util;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;
import javax.ws.rs.core.Response;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import static am.main.data.enums.Interface.ARQUILLIAN;
import static am.main.data.enums.Source.INTEGRATION_TEST;
import static amt.common.constants.Error.COURSE.COURSE_LIST_VAL;

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

    private void compareCourseLists(List<String> expectedCourseIDs, List<CourseListUI> actual){
        int counter = 0;
        for (CourseListUI course : actual)
            if(!course.getCourseID().equals(expectedCourseIDs.get(counter++)))
                Assert.fail("Expected Course ID: " + expectedCourseIDs.get(--counter) + ", But Actual Course ID: " + course.getCourseID());
    }

    @Test @InSequence(1)
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

    @Test @InSequence(2)
    public void getAllCourses_NoFilters_DefaultSorting_Paging_1() throws Exception {
        try {
            CourseListFilter filters = new CourseListFilter();
            filters.setPageNum(0);
            filters.setSorting(new SortingInfo("Asc", "courseName"));

            LoginData loginData = Params.studentLoginData;

            Response response = RestUtil.postSecured(Rest.COURSE.RESOURCE, Rest.COURSE.LIST, filters, loginData);
            Assert.assertEquals("Response Status is wrong", Response.Status.OK.getStatusCode(), response.getStatus());

            CourseListRS resultSet = response.readEntity(CourseListRS.class);
            Assert.assertEquals("Total Courses is wrong", 39, resultSet.getPagination().getTotal());
            
            List<String> expectedCourseIDs = new ArrayList<>();
            expectedCourseIDs.add("Cor0000028AcIn20171217");
            expectedCourseIDs.add("Cor0000029PrBe20171214");
            expectedCourseIDs.add("Cor0000038AcBe20170616");
            expectedCourseIDs.add("Cor0000022PrAd20170722");
            expectedCourseIDs.add("Cor0000031AcAd20171023");
            expectedCourseIDs.add("Cor0000019AcAd20170910");
            expectedCourseIDs.add("Cor0000018PrIn20170919");
            expectedCourseIDs.add("Cor0000020PrAd20170307");
            expectedCourseIDs.add("Cor0000005AcAd20171002");
            expectedCourseIDs.add("Cor0000036PrAd20170612");

            compareCourseLists(expectedCourseIDs, resultSet.getData());
        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }

    @Test @InSequence(3)
    public void getAllCourses_NoFilters_DefaultSorting_Paging_2() throws Exception{
        try {
            CourseListFilter filters = new CourseListFilter();
            filters.setPageNum(1);
            filters.setSorting(new SortingInfo("Asc", "courseName"));

            LoginData loginData = Params.studentLoginData;

            Response response = RestUtil.postSecured(Rest.COURSE.RESOURCE, Rest.COURSE.LIST, filters, loginData);
            Assert.assertEquals("Response Status is wrong", Response.Status.OK.getStatusCode(), response.getStatus());

            CourseListRS resultSet = response.readEntity(CourseListRS.class);
            Assert.assertEquals("Total Courses is wrong", 39, resultSet.getPagination().getTotal());
            
            List<String> expectedCourseIDs = new ArrayList<>();
            expectedCourseIDs.add("Cor0000008PrBe20170417");
            expectedCourseIDs.add("Cor0000024AcAd20170723");
            expectedCourseIDs.add("Cor0000006AcAd20161202");
            expectedCourseIDs.add("Cor0000039AcAd20170613");
            expectedCourseIDs.add("Cor0000025AcBe20170315");
            expectedCourseIDs.add("Cor0000014PrBe20170415");
            expectedCourseIDs.add("Cor0000007AcBe20170418");
            expectedCourseIDs.add("Cor0000015AcAd20170416");
            expectedCourseIDs.add("Cor0000004PrAd20171006");
            expectedCourseIDs.add("Cor0000034PrAd20171025");

            compareCourseLists(expectedCourseIDs, resultSet.getData());
        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }

    @Test @InSequence(4)
    public void getAllCourses_NoFilters_DefaultSorting_Paging_3() throws Exception{
        try {
            CourseListFilter filters = new CourseListFilter();
            filters.setPageNum(2);
            filters.setSorting(new SortingInfo("Asc", "courseName"));

            LoginData loginData = Params.studentLoginData;

            Response response = RestUtil.postSecured(Rest.COURSE.RESOURCE, Rest.COURSE.LIST, filters, loginData);
            Assert.assertEquals("Response Status is wrong", Response.Status.OK.getStatusCode(), response.getStatus());

            CourseListRS resultSet = response.readEntity(CourseListRS.class);
            Assert.assertEquals("Total Courses is wrong", 39, resultSet.getPagination().getTotal());
            
            List<String> expectedCourseIDs = new ArrayList<>();
            expectedCourseIDs.add("Cor0000037PrBe20170614");
            expectedCourseIDs.add("Cor0000021PrAd20170311");
            expectedCourseIDs.add("Cor0000017AcBe20170919");
            expectedCourseIDs.add("Cor0000002AcIn20161102");
            expectedCourseIDs.add("Cor0000030PrBe20171213");
            expectedCourseIDs.add("Cor0000026AcAd20170424");
            expectedCourseIDs.add("Cor0000009PrAd20170421");
            expectedCourseIDs.add("Cor0000012PrAd20170414");
            expectedCourseIDs.add("Cor0000013PrIn20170413");
            expectedCourseIDs.add("Cor0000001PrIn20161102");

            compareCourseLists(expectedCourseIDs, resultSet.getData());
        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }

    @Test @InSequence(5)
    public void getAllCourses_NoFilters_DefaultSorting_Paging_4() throws Exception{
        try {
            CourseListFilter filters = new CourseListFilter();
            filters.setPageNum(3);
            filters.setSorting(new SortingInfo("Asc", "courseName"));

            LoginData loginData = Params.studentLoginData;

            Response response = RestUtil.postSecured(Rest.COURSE.RESOURCE, Rest.COURSE.LIST, filters, loginData);
            Assert.assertEquals("Response Status is wrong", Response.Status.OK.getStatusCode(), response.getStatus());

            CourseListRS resultSet = response.readEntity(CourseListRS.class);
            Assert.assertEquals("Total Courses is wrong", 39, resultSet.getPagination().getTotal());
            
            List<String> expectedCourseIDs = new ArrayList<>();
            expectedCourseIDs.add("Cor0000023PrIn20170721");
            expectedCourseIDs.add("Cor0000003AcIn20161101");
            expectedCourseIDs.add("Cor0000033PrBe20171021");
            expectedCourseIDs.add("Cor0000032PrBe20171022");
            expectedCourseIDs.add("Cor0000010PrAd20170412");
            expectedCourseIDs.add("Cor0000016PrBe20170918");
            expectedCourseIDs.add("Cor0000035PrBe20171025");
            expectedCourseIDs.add("Cor0000027PrBe20170423");
            expectedCourseIDs.add("Cor0000011PrIn20170413");

            compareCourseLists(expectedCourseIDs, resultSet.getData());
        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }

                    /* Course Name Filter with other Filters */
    @Test @InSequence(6)
    public void getAllCourses_CourseName_Filter() throws Exception{
        try {
            CourseListFilter filters = new CourseListFilter();
            filters.setCourseName("Al");
            filters.setPageNum(0);
            filters.setSorting(new SortingInfo("Asc", "courseName"));

            LoginData loginData = Params.studentLoginData;

            Response response = RestUtil.postSecured(Rest.COURSE.RESOURCE, Rest.COURSE.LIST, filters, loginData);
            Assert.assertEquals("Response Status is wrong", Response.Status.OK.getStatusCode(), response.getStatus());

            CourseListRS resultSet = response.readEntity(CourseListRS.class);
            Assert.assertEquals("Total Courses is wrong", 8, resultSet.getPagination().getTotal());

            List<String> expectedCourseIDs = new ArrayList<>();
            expectedCourseIDs.add("Cor0000028AcIn20171217");
            expectedCourseIDs.add("Cor0000029PrBe20171214");
            expectedCourseIDs.add("Cor0000022PrAd20170722");
            expectedCourseIDs.add("Cor0000031AcAd20171023");
            expectedCourseIDs.add("Cor0000008PrBe20170417");
            expectedCourseIDs.add("Cor0000025AcBe20170315");
            expectedCourseIDs.add("Cor0000013PrIn20170413");
            expectedCourseIDs.add("Cor0000027PrBe20170423");

            compareCourseLists(expectedCourseIDs, resultSet.getData());
        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }

    @Test @InSequence(7)
    public void getAllCourses_CourseName_CDateFrom_Filter() throws Exception{
        try {
            Assert.fail("Not Implemented Yet");
        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }

    @Test @InSequence(8)
    public void getAllCourses_CourseName_CDateTo_Filter() throws Exception{
        try {
            Assert.fail("Not Implemented Yet");
        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }

    @Test @InSequence(9)
    public void getAllCourses_CourseName_SDateFrom_Filter() throws Exception{
        try {
            Assert.fail("Not Implemented Yet");
        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }

    @Test @InSequence(10)
    public void getAllCourses_CourseName_SDateTo_Filter() throws Exception{
        try {
            Assert.fail("Not Implemented Yet");
        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }

    @Test @InSequence(11)
    public void getAllCourses_CourseName_Type_Filter() throws Exception{
        try {
            CourseListFilter filters = new CourseListFilter();
            filters.setCourseName("Al");
            filters.setCourseType("Pr");
            filters.setPageNum(0);
            filters.setSorting(new SortingInfo("Asc", "courseName"));

            LoginData loginData = Params.studentLoginData;

            Response response = RestUtil.postSecured(Rest.COURSE.RESOURCE, Rest.COURSE.LIST, filters, loginData);
            Assert.assertEquals("Response Status is wrong", Response.Status.OK.getStatusCode(), response.getStatus());

            CourseListRS resultSet = response.readEntity(CourseListRS.class);
            Assert.assertEquals("Total Courses is wrong", 5, resultSet.getPagination().getTotal());

            List<String> expectedCourseIDs = new ArrayList<>();
            expectedCourseIDs.add("Cor0000029PrBe20171214");
            expectedCourseIDs.add("Cor0000022PrAd20170722");
            expectedCourseIDs.add("Cor0000008PrBe20170417");
            expectedCourseIDs.add("Cor0000013PrIn20170413");
            expectedCourseIDs.add("Cor0000027PrBe20170423");

            compareCourseLists(expectedCourseIDs, resultSet.getData());
        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }

    @Test @InSequence(12)
    public void getAllCourses_CourseName_Level_Filter() throws Exception{
        try {
            CourseListFilter filters = new CourseListFilter();
            filters.setCourseName("Al");
            filters.setCourseLevel("Be");
            filters.setPageNum(0);
            filters.setSorting(new SortingInfo("Asc", "courseName"));

            LoginData loginData = Params.studentLoginData;

            Response response = RestUtil.postSecured(Rest.COURSE.RESOURCE, Rest.COURSE.LIST, filters, loginData);
            Assert.assertEquals("Response Status is wrong", Response.Status.OK.getStatusCode(), response.getStatus());

            CourseListRS resultSet = response.readEntity(CourseListRS.class);
            Assert.assertEquals("Total Courses is wrong", 4, resultSet.getPagination().getTotal());

            List<String> expectedCourseIDs = new ArrayList<>();
            expectedCourseIDs.add("Cor0000029PrBe20171214");
            expectedCourseIDs.add("Cor0000008PrBe20170417");
            expectedCourseIDs.add("Cor0000025AcBe20170315");
            expectedCourseIDs.add("Cor0000027PrBe20170423");

            compareCourseLists(expectedCourseIDs, resultSet.getData());
        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }

                /* Course Type Filter with other Filters */
    @Test @InSequence(13)
    public void getAllCourses_CorType_Filter() throws Exception{
        try {
            CourseListFilter filters = new CourseListFilter();
            filters.setCourseType("Ac");
            filters.setPageNum(0);
            filters.setSorting(new SortingInfo("Asc", "courseName"));

            LoginData loginData = Params.studentLoginData;

            Response response = RestUtil.postSecured(Rest.COURSE.RESOURCE, Rest.COURSE.LIST, filters, loginData);
            Assert.assertEquals("Response Status is wrong", Response.Status.OK.getStatusCode(), response.getStatus());

            CourseListRS resultSet = response.readEntity(CourseListRS.class);
            Assert.assertEquals("Total Courses is wrong", 15, resultSet.getPagination().getTotal());

            List<String> expectedCourseIDs = new ArrayList<>();
            expectedCourseIDs.add("Cor0000028AcIn20171217");
            expectedCourseIDs.add("Cor0000038AcBe20170616");
            expectedCourseIDs.add("Cor0000031AcAd20171023");
            expectedCourseIDs.add("Cor0000019AcAd20170910");
            expectedCourseIDs.add("Cor0000005AcAd20171002");
            expectedCourseIDs.add("Cor0000024AcAd20170723");
            expectedCourseIDs.add("Cor0000006AcAd20161202");
            expectedCourseIDs.add("Cor0000039AcAd20170613");
            expectedCourseIDs.add("Cor0000025AcBe20170315");
            expectedCourseIDs.add("Cor0000007AcBe20170418");

            compareCourseLists(expectedCourseIDs, resultSet.getData());
        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }

    @Test @InSequence(14)
    public void getAllCourses_CorType_Level_Filter() throws Exception{
        try {
            CourseListFilter filters = new CourseListFilter();
            filters.setCourseLevel("Be");
            filters.setCourseType("Ac");
            filters.setPageNum(0);
            filters.setSorting(new SortingInfo("Asc", "courseName"));

            LoginData loginData = Params.studentLoginData;

            Response response = RestUtil.postSecured(Rest.COURSE.RESOURCE, Rest.COURSE.LIST, filters, loginData);
            Assert.assertEquals("Response Status is wrong", Response.Status.OK.getStatusCode(), response.getStatus());

            CourseListRS resultSet = response.readEntity(CourseListRS.class);
            Assert.assertEquals("Total Courses is wrong", 4, resultSet.getPagination().getTotal());

            List<String> expectedCourseIDs = new ArrayList<>();
            expectedCourseIDs.add("Cor0000038AcBe20170616");
            expectedCourseIDs.add("Cor0000025AcBe20170315");
            expectedCourseIDs.add("Cor0000007AcBe20170418");
            expectedCourseIDs.add("Cor0000017AcBe20170919");

            compareCourseLists(expectedCourseIDs, resultSet.getData());
        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }

    @Test @InSequence(15)
    public void getAllCourses_CorType_CDateFrom_Filter() throws Exception{
        try {
            Assert.fail("Not Implemented Yet");
        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }

    @Test @InSequence(16)
    public void getAllCourses_CorType_CDateTo_Filter() throws Exception{
        try {
            Assert.fail("Not Implemented Yet");
        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }

    @Test @InSequence(17)
    public void getAllCourses_CorType_SDateFrom_Filter() throws Exception{
        try {
            Assert.fail("Not Implemented Yet");
        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }

    @Test @InSequence(18)
    public void getAllCourses_CorType_SDateTo_Filter() throws Exception{
        try {
            Assert.fail("Not Implemented Yet");
        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }

                /* Course Level Filter with other Filters */
    @Test @InSequence(19)
    public void getAllCourses_CorLev_Filter() throws Exception{
        try {
            CourseListFilter filters = new CourseListFilter();
            filters.setCourseLevel("Be");
            filters.setPageNum(0);
            filters.setSorting(new SortingInfo("Asc", "courseName"));

            LoginData loginData = Params.studentLoginData;

            Response response = RestUtil.postSecured(Rest.COURSE.RESOURCE, Rest.COURSE.LIST, filters, loginData);
            Assert.assertEquals("Response Status is wrong", Response.Status.OK.getStatusCode(), response.getStatus());

            CourseListRS resultSet = response.readEntity(CourseListRS.class);
            Assert.assertEquals("Total Courses is wrong", 14, resultSet.getPagination().getTotal());

            List<String> expectedCourseIDs = new ArrayList<>();
            expectedCourseIDs.add("Cor0000029PrBe20171214");
            expectedCourseIDs.add("Cor0000038AcBe20170616");
            expectedCourseIDs.add("Cor0000008PrBe20170417");
            expectedCourseIDs.add("Cor0000025AcBe20170315");
            expectedCourseIDs.add("Cor0000014PrBe20170415");
            expectedCourseIDs.add("Cor0000007AcBe20170418");
            expectedCourseIDs.add("Cor0000037PrBe20170614");
            expectedCourseIDs.add("Cor0000017AcBe20170919");
            expectedCourseIDs.add("Cor0000030PrBe20171213");
            expectedCourseIDs.add("Cor0000033PrBe20171021");

            compareCourseLists(expectedCourseIDs, resultSet.getData());
        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }

    @Test @InSequence(20)
    public void getAllCourses_CorLev_CDateFrom_Filter() throws Exception{
        try {
            Assert.fail("Not Implemented Yet");
        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }

    @Test @InSequence(21)
    public void getAllCourses_CorLev_CDateTo_Filter() throws Exception{
        try {
            Assert.fail("Not Implemented Yet");
        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }

    @Test @InSequence(22)
    public void getAllCourses_CorLev_SDateFrom_Filter() throws Exception{
        try {
            Assert.fail("Not Implemented Yet");
        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }

    @Test @InSequence(23)
    public void getAllCourses_CorLev_SDateTo_Filter() throws Exception{
        try {
            Assert.fail("Not Implemented Yet");
        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }

            /* Course Creation Date From Filter with other Filters */

    @Test @InSequence(24)
    public void getAllCourses_CorCDateFrom_Filter() throws Exception{
        try {
            Assert.fail("Not Implemented Yet");
        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }

    @Test @InSequence(25)
    public void getAllCourses_CorCDateFrom_CDateTo_Filter() throws Exception{
        try {
            Assert.fail("Not Implemented Yet");
        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }

    @Test @InSequence(26)
    public void getAllCourses_CorCDateFrom_SDateFrom_Filter() throws Exception{
        try {
            Assert.fail("Not Implemented Yet");
        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }

    @Test @InSequence(27)
    public void getAllCourses_CorCDateFrom_SDateTo_Filter() throws Exception{
        try {
            Assert.fail("Not Implemented Yet");
        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }

                    /* Course Creation Date To Filter with other Filters */

    @Test @InSequence(28)
    public void getAllCourses_CorCDateTo_Filter() throws Exception{
        try {
            Assert.fail("Not Implemented Yet");
        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }

    @Test @InSequence(29)
    public void getAllCourses_CorCDateTo_SDateFrom_Filter() throws Exception{
        try {
            Assert.fail("Not Implemented Yet");
        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }

    @Test @InSequence(30)
    public void getAllCourses_CorCDateTo_SDateTo_Filter() throws Exception{
        try {
            Assert.fail("Not Implemented Yet");
        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }

                    /* Course Start Date From Filter with other Filters */

    @Test @InSequence(31)
    public void getAllCourses_CorSDateFrom_Filter() throws Exception{
        try {
            Assert.fail("Not Implemented Yet");
        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }

    @Test @InSequence(32)
    public void getAllCourses_CorSDateFrom_SDateTo_Filter() throws Exception{
        try {
            Assert.fail("Not Implemented Yet");
        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }

                    /* Course Start Date To Filter with other Filters */

    @Test @InSequence(33)
    public void getAllCourses_CorSDateTo_Filter() throws Exception{
        try {
            Assert.fail("Not Implemented Yet");
        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }

                    /* Course Name & Type Filter with other Filters */

    @Test @InSequence(34)
    public void getAllCourses_CourseName_Type_Level_Filter() throws Exception{
        try {
            CourseListFilter filters = new CourseListFilter();
            filters.setCourseName("Al");
            filters.setCourseType("Pr");
            filters.setCourseLevel("Be");
            filters.setPageNum(0);
            filters.setSorting(new SortingInfo("Asc", "courseName"));

            LoginData loginData = Params.studentLoginData;

            Response response = RestUtil.postSecured(Rest.COURSE.RESOURCE, Rest.COURSE.LIST, filters, loginData);
            Assert.assertEquals("Response Status is wrong", Response.Status.OK.getStatusCode(), response.getStatus());

            CourseListRS resultSet = response.readEntity(CourseListRS.class);
            Assert.assertEquals("Total Courses is wrong", 3, resultSet.getPagination().getTotal());

            List<String> expectedCourseIDs = new ArrayList<>();
            expectedCourseIDs.add("Cor0000029PrBe20171214");
            expectedCourseIDs.add("Cor0000008PrBe20170417");
            expectedCourseIDs.add("Cor0000027PrBe20170423");

            compareCourseLists(expectedCourseIDs, resultSet.getData());
        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }
    
    @Test @InSequence(35)
    public void getAllCourses_CourseName_Type_CDateFrom_Filter() throws Exception{
        try {
            Assert.fail("Not Implemented Yet");
        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }

    @Test @InSequence(36)
    public void getAllCourses_CourseName_Type_CDateTo_Filter() throws Exception{
        try {
            Assert.fail("Not Implemented Yet");
        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }

    @Test @InSequence(37)
    public void getAllCourses_CourseName_Type_SDateFrom_Filter() throws Exception{
        try {
            Assert.fail("Not Implemented Yet");
        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }

    @Test @InSequence(38)
    public void getAllCourses_CourseName_Type_SDateTo_Filter() throws Exception{
        try {
            Assert.fail("Not Implemented Yet");
        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }

                /* Course Name & Level Filter with other Filters */
    @Test @InSequence(39)
    public void getAllCourses_CourseName_Level_CDateFrom_Filter() throws Exception{
        try {
            Assert.fail("Not Implemented Yet");
        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }

    @Test @InSequence(40)
    public void getAllCourses_CourseName_Level_CDateTo_Filter() throws Exception{
        try {
            Assert.fail("Not Implemented Yet");
        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }

    @Test @InSequence(41)
    public void getAllCourses_CourseName_Level_SDateFrom_Filter() throws Exception{
        try {
            Assert.fail("Not Implemented Yet");
        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }

    @Test @InSequence(42)
    public void getAllCourses_CourseName_Level_SDateTo_Filter() throws Exception{
        try {
            Assert.fail("Not Implemented Yet");
        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }
    

                /* Course Name & Creation Date From Filter with other Filters */

    @Test @InSequence(43)
    public void getAllCourses_CourseName_CDateFrom_CDateTo_Filter() throws Exception{
        try {
            Assert.fail("Not Implemented Yet");
        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }

    @Test @InSequence(44)
    public void getAllCourses_CourseName_CDateFrom_SDateFrom_Filter() throws Exception{
        try {
            Assert.fail("Not Implemented Yet");
        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }

    @Test @InSequence(45)
    public void getAllCourses_CourseName_CDateFrom_SDateTo_Filter() throws Exception{
        try {
            Assert.fail("Not Implemented Yet");
        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }
    
                /* Course Name & Creation Date To Filter with other Filters */
                
    @Test @InSequence(46)
    public void getAllCourses_CourseName_CDateTo_SDateFrom_Filter() throws Exception{
        try {
            Assert.fail("Not Implemented Yet");
        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }

    @Test @InSequence(47)
    public void getAllCourses_CourseName_CDateTo_SDateTo_Filter() throws Exception{
        try {
            Assert.fail("Not Implemented Yet");
        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }

                /* Course Type & Start Date From Filter with other Filters */
                
    @Test @InSequence(48)
    public void getAllCourses_CourseName_SDateFrom_SDateTo_Filter() throws Exception{
        try {
            Assert.fail("Not Implemented Yet");
        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }
                
                /* Course Type & Level Filter with other Filters */

    @Test @InSequence(49)
    public void getAllCourses_CorType_Level_CDateFrom_Filter() throws Exception{
        try {
            Assert.fail("Not Implemented Yet");
        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }

    @Test @InSequence(50)
    public void getAllCourses_CorType_Level_CDateTo_Filter() throws Exception{
        try {
            Assert.fail("Not Implemented Yet");
        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }

    @Test @InSequence(51)
    public void getAllCourses_CorType_Level_SDateFrom_Filter() throws Exception{
        try {
            Assert.fail("Not Implemented Yet");
        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }

    @Test @InSequence(52)
    public void getAllCourses_CorType_Level_SDateTo_Filter() throws Exception{
        try {
            Assert.fail("Not Implemented Yet");
        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }
                
                /* Course Type & Creation Date From Filter with other Filters */

    @Test @InSequence(53)
    public void getAllCourses_CorType_CDateFrom_CDateTo_Filter() throws Exception{
        try {
            Assert.fail("Not Implemented Yet");
        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }

    @Test @InSequence(54)
    public void getAllCourses_CorType_CDateFrom_SDateFrom_Filter() throws Exception{
        try {
            Assert.fail("Not Implemented Yet");
        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }

    @Test @InSequence(55)
    public void getAllCourses_CorType_CDateFrom_SDateTo_Filter() throws Exception{
        try {
            Assert.fail("Not Implemented Yet");
        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }
                
                /* Course Type & Creation Date To Filter with other Filters */

    @Test @InSequence(56)
    public void getAllCourses_CorType_CDateTo_SDateFrom_Filter() throws Exception{
        try {
            Assert.fail("Not Implemented Yet");
        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }

    @Test @InSequence(57)
    public void getAllCourses_CorType_CDateTo_SDateTo_Filter() throws Exception{
        try {
            Assert.fail("Not Implemented Yet");
        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }
                
                /* Course Type & Start Date From Filter with other Filters */

    @Test @InSequence(58)
    public void getAllCourses_CorType_SDateFrom_SDateTo_Filter() throws Exception{
        try {
            Assert.fail("Not Implemented Yet");
        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }
                
                /* Course Level & Creation Date From Filter with other Filters */

    @Test @InSequence(59)
    public void getAllCourses_CorLevel_CDateFrom_CDateTo_Filter() throws Exception{
        try {
            Assert.fail("Not Implemented Yet");
        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }

    @Test @InSequence(60)
    public void getAllCourses_CorLevel_CDateFrom_SDateFrom_Filter() throws Exception{
        try {
            Assert.fail("Not Implemented Yet");
        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }

    @Test @InSequence(61)
    public void getAllCourses_CorLevel_CDateFrom_SDateTo_Filter() throws Exception{
        try {
            Assert.fail("Not Implemented Yet");
        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }
                
                /* Course Level & Creation Date To Filter with other Filters */

    @Test @InSequence(62)
    public void getAllCourses_CorLevel_CDateTo_SDateFrom_Filter() throws Exception{
        try {
            Assert.fail("Not Implemented Yet");
        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }

    @Test @InSequence(63)
    public void getAllCourses_CorLevel_CDateTo_SDateTo_Filter() throws Exception{
        try {
            Assert.fail("Not Implemented Yet");
        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }
                
                /* Course Level & Start Date From Filter with other Filters */

    @Test @InSequence(64)
    public void getAllCourses_CorLevel_SDateFrom_SDateTo_Filter() throws Exception{
        try {
            Assert.fail("Not Implemented Yet");
        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }

        /* Course Creation Date From & Creation Date To Filter with other Filters */

    @Test @InSequence(65)
    public void getAllCourses_CorCDateFrom_CDateTo_SDateFrom_Filter() throws Exception{
        try {
            Assert.fail("Not Implemented Yet");
        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }

    @Test @InSequence(66)
    public void getAllCourses_CorCDateFrom_CDateTo_SDateTo_Filter() throws Exception{
        try {
            Assert.fail("Not Implemented Yet");
        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }

        /* Course Creation Date From & Start Date From Filter with other Filters */

    @Test @InSequence(67)
    public void getAllCourses_CorCDateFrom_SDateFrom_SDateTo_Filter() throws Exception{
        try {
            Assert.fail("Not Implemented Yet");
        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }

        /* Course Creation Date To & Start Date From Filter with other Filters */

    @Test @InSequence(68)
    public void getAllCourses_CorCDateTo_SDateFrom_SDateTo_Filter() throws Exception{
        try {
            Assert.fail("Not Implemented Yet");
        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }

        /* Course Name & Type & Level Filter with other Filters */

    @Test @InSequence(69)
    public void getAllCourses_CorName_Type_Level_CDateFrom_Filter() throws Exception{
        try {
            Assert.fail("Not Implemented Yet");
        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }

    @Test @InSequence(70)
    public void getAllCourses_CorName_Type_Level_CDateTo_Filter() throws Exception{
        try {
            Assert.fail("Not Implemented Yet");
        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }

    @Test @InSequence(71)
    public void getAllCourses_CorName_Type_Level_SDateFrom_Filter() throws Exception{
        try {
            Assert.fail("Not Implemented Yet");
        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }

    @Test @InSequence(72)
    public void getAllCourses_CorName_Type_Level_SDateTo_Filter() throws Exception{
        try {
            Assert.fail("Not Implemented Yet");
        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }

        /* Course Type & Level & Creation Date From Filter with other Filters */

    @Test @InSequence(73)
    public void getAllCourses_CorType_Level_CDateFrom_CDateTo_Filter() throws Exception{
        try {
            Assert.fail("Not Implemented Yet");
        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }

    @Test @InSequence(74)
    public void getAllCourses_CorType_Level_CDateFrom_SDateFrom_Filter() throws Exception{
        try {
            Assert.fail("Not Implemented Yet");
        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }

    @Test @InSequence(75)
    public void getAllCourses_CorType_Level_CDateFrom_SDateTo_Filter() throws Exception{
        try {
            Assert.fail("Not Implemented Yet");
        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }

        /* Course Level & Creation Date From & Creation Date To Filter with other Filters */

    @Test @InSequence(75)
    public void getAllCourses_CorLevel_CDateFrom_CDateTo_SDateFrom_Filter() throws Exception{
        try {
            Assert.fail("Not Implemented Yet");
        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }

    @Test @InSequence(76)
    public void getAllCourses_CorLevel_CDateFrom_CDateTo_SDateTo_Filter() throws Exception{
        try {
            Assert.fail("Not Implemented Yet");
        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }

    /* Course Creation Date From & Creation Date To & Start Date From Filter with other Filters */

    @Test @InSequence(77)
    public void getAllCourses_CorCDateFrom_CDateTo_SDateFrom_SDateTo_Filter() throws Exception{
        try {
            Assert.fail("Not Implemented Yet");
        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }

     /* Course Name & Type & Level & Creation Date From Filter with other Filters */

    @Test @InSequence(78)
    public void getAllCourses_CorName_Type_Level_CDateFrom_CDateTo_Filter() throws Exception{
        try {
            Assert.fail("Not Implemented Yet");
        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }

    @Test @InSequence(79)
    public void getAllCourses_CorName_Type_Level_CDateFrom_SDateFrom_Filter() throws Exception{
        try {
            Assert.fail("Not Implemented Yet");
        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }

    @Test @InSequence(80)
    public void getAllCourses_CorName_Type_Level_CDateFrom_SDateTo_Filter() throws Exception{
        try {
            Assert.fail("Not Implemented Yet");
        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }

        /* Course Type & Level & Cre Date From & Cre Date To Filter with other Filters */

    @Test @InSequence(81)
    public void getAllCourses_CorType_Level_CDateFrom_CDateTo_SDateFrom_Filter() throws Exception{
        try {
            Assert.fail("Not Implemented Yet");
        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }

    @Test @InSequence(82)
    public void getAllCourses_CorType_Level_CDateFrom_CDateTo_SDateTo_Filter() throws Exception{
        try {
            Assert.fail("Not Implemented Yet");
        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }

        /* Course Level & Cre Date From & Cre Date To & St Date From Filter with other Filters */

    @Test @InSequence(83)
    public void getAllCourses_CorLevel_CDateFrom_CDateTo_SDateFrom_SDateTo_Filter() throws Exception{
        try {
            Assert.fail("Not Implemented Yet");
        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }

     /* Course Name & Type & Level & Cre Date From & Cre Date To Filter with other Filters */

    @Test @InSequence(84)
    public void getAllCourses_CorName_Type_Level_CDateFrom_CDateTo_SDateFrom_Filter() throws Exception{
        try {
            Assert.fail("Not Implemented Yet");
        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }

    @Test @InSequence(85)
    public void getAllCourses_CorName_Type_Level_CDateFrom_CDateTo_SDateTo_Filter() throws Exception{
        try {
            Assert.fail("Not Implemented Yet");
        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }

        /* Course Type & Level & Cre Date From & Cre Date To & St Date From Filter with other Filters */

    @Test @InSequence(86)
    public void getAllCourses_CorType_Level_CDateFrom_CDateTo_SDateFrom_SDateTo_Filter() throws Exception{
        try {
            Assert.fail("Not Implemented Yet");
        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }

     /* Course Name & Type & Level & Cre Date From & Cre Date To & St Date From & St Date To Filter with other Filters */

    @Test @InSequence(87)
    public void getAllCourses_CorName_Type_Level_CDateFrom_CDateTo_SDateFrom_SDateTo_Filter() throws Exception{
        try {
            Assert.fail("Not Implemented Yet");
        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }


    @Test @InSequence(88)
    public void getAllCourses_NoFilters_Sorting_By_ActualDuration() throws Exception{
        try {
            Assert.fail("Not Implemented Yet");
//            CourseListFilter filters = new CourseListFilter();
//            filters.setPageNum(0);
//            filters.setSorting(new SortingInfo("Asc", "courseName"));
//
//            LoginData loginData = Params.studentLoginData;
//
//            Response response = RestUtil.postSecured(Rest.COURSE.RESOURCE, Rest.COURSE.LIST, filters, loginData);
//            Assert.assertEquals("Response Status is wrong", Response.Status.OK.getStatusCode(), response.getStatus());
//
//            CourseListRS resultSet = response.readEntity(CourseListRS.class);
//            Assert.assertEquals("Total Courses is wrong", 39, resultSet.getPagination().getTotal());
//
//            List<String> expectedCourseIDs = new ArrayList<>();
//            expectedCourseIDs.add("");
//            expectedCourseIDs.add("");
//            expectedCourseIDs.add("");
//            expectedCourseIDs.add("");
//            expectedCourseIDs.add("");
//            expectedCourseIDs.add("");
//            expectedCourseIDs.add("");
//            expectedCourseIDs.add("");
//            expectedCourseIDs.add("");
//            expectedCourseIDs.add("");
//
//            compareCourseLists(expectedCourseIDs, resultSet.getData());
        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }

    @Test @InSequence(89)
    public void getAllCourses_NoFilters_Sorting_By_StartDate() throws Exception{
        try {
            Assert.fail("Not Implemented Yet");
//            CourseListFilter filters = new CourseListFilter();
//            filters.setPageNum(0);
//            filters.setSorting(new SortingInfo("Asc", "courseName"));
//
//            LoginData loginData = Params.studentLoginData;
//
//            Response response = RestUtil.postSecured(Rest.COURSE.RESOURCE, Rest.COURSE.LIST, filters, loginData);
//            Assert.assertEquals("Response Status is wrong", Response.Status.OK.getStatusCode(), response.getStatus());
//
//            CourseListRS resultSet = response.readEntity(CourseListRS.class);
//            Assert.assertEquals("Total Courses is wrong", 39, resultSet.getPagination().getTotal());
//
//            List<String> expectedCourseIDs = new ArrayList<>();
//            expectedCourseIDs.add("");
//            expectedCourseIDs.add("");
//            expectedCourseIDs.add("");
//            expectedCourseIDs.add("");
//            expectedCourseIDs.add("");
//            expectedCourseIDs.add("");
//            expectedCourseIDs.add("");
//            expectedCourseIDs.add("");
//            expectedCourseIDs.add("");
//            expectedCourseIDs.add("");
//
//            compareCourseLists(expectedCourseIDs, resultSet.getData());
        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }

    @Test @InSequence(90)
    public void getAllCourses_NoFilters_Sorting_By_Progress() throws Exception{
        try {
            Assert.fail("Not Implemented Yet");
//            CourseListFilter filters = new CourseListFilter();
//            filters.setPageNum(0);
//            filters.setSorting(new SortingInfo("Asc", "courseName"));
//
//            LoginData loginData = Params.studentLoginData;
//
//            Response response = RestUtil.postSecured(Rest.COURSE.RESOURCE, Rest.COURSE.LIST, filters, loginData);
//            Assert.assertEquals("Response Status is wrong", Response.Status.OK.getStatusCode(), response.getStatus());
//
//            CourseListRS resultSet = response.readEntity(CourseListRS.class);
//            Assert.assertEquals("Total Courses is wrong", 39, resultSet.getPagination().getTotal());
//
//            List<String> expectedCourseIDs = new ArrayList<>();
//            expectedCourseIDs.add("");
//            expectedCourseIDs.add("");
//            expectedCourseIDs.add("");
//            expectedCourseIDs.add("");
//            expectedCourseIDs.add("");
//            expectedCourseIDs.add("");
//            expectedCourseIDs.add("");
//            expectedCourseIDs.add("");
//            expectedCourseIDs.add("");
//            expectedCourseIDs.add("");
//
//            compareCourseLists(expectedCourseIDs, resultSet.getData());
        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }


    @Test @InSequence(91)
    public void getAllCourses_InvalidFilters_CourseName_EmptyStr() throws Exception{
        try {
            CourseListFilter filters = new CourseListFilter();
            filters.setCourseName(" ");
            filters.setPageNum(0);
            filters.setSorting(new SortingInfo("Asc", "courseName"));

            LoginData loginData = Params.studentLoginData;

            String expectErrorMsg = MessageFormat.format(Error.FV.EMPTY_STR, CourseListFilter.FIELDS.get("courseName"));

            FormValidation expected = new FormValidation(COURSE_LIST_VAL, expectErrorMsg);
            Util.postSecuredFormValidation(Rest.COURSE.RESOURCE, Rest.COURSE.LIST, loginData, filters, expected);
        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }

    @Test @InSequence(92)
    public void getAllCourses_InvalidFilters_CourseName_MaxLength() throws Exception{
        try {
            CourseListFilter filters = new CourseListFilter();
            filters.setCourseName(Util.generateString(105));
            filters.setPageNum(0);
            filters.setSorting(new SortingInfo("Asc", "courseName"));

            LoginData loginData = Params.studentLoginData;

            String expectErrorMsg = MessageFormat.format(Error.FV.MAX_LENGTH, filters.getCourseName(),
                    CourseListFilter.FIELDS.get("courseName"), 100);

            FormValidation expected = new FormValidation(COURSE_LIST_VAL, expectErrorMsg);
            Util.postSecuredFormValidation(Rest.COURSE.RESOURCE, Rest.COURSE.LIST, loginData, filters, expected);
        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }

    @Test @InSequence(93)
    public void getAllCourses_InvalidFilters_CourseName_InvalidValue() throws Exception{
        try {
            CourseListFilter filters = new CourseListFilter();
            filters.setCourseName("Ahmed@#!@Ali");
            filters.setPageNum(0);
            filters.setSorting(new SortingInfo("Asc", "courseName"));

            LoginData loginData = Params.studentLoginData;

            String expectErrorMsg = MessageFormat.format(Error.FV.REGEX, filters.getCourseName(),
                    CourseListFilter.FIELDS.get("courseName"), RegExp.MESSAGES.get(RegExp.CONTENT_NAME));

            FormValidation expected = new FormValidation(COURSE_LIST_VAL, expectErrorMsg);
            Util.postSecuredFormValidation(Rest.COURSE.RESOURCE, Rest.COURSE.LIST, loginData, filters, expected);
        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }


    @Test @InSequence(94)
    public void getAllCourses_InvalidFilters_CorLev_EmptyStr() throws Exception{
        try {
            CourseListFilter filters = new CourseListFilter();
            filters.setCourseLevel(" ");
            filters.setPageNum(0);
            filters.setSorting(new SortingInfo("Asc", "courseName"));

            LoginData loginData = Params.studentLoginData;

            String expectErrorMsg = MessageFormat.format(Error.FV.EMPTY_STR, CourseListFilter.FIELDS.get("courseLevel"));

            FormValidation expected = new FormValidation(COURSE_LIST_VAL, expectErrorMsg);
            Util.postSecuredFormValidation(Rest.COURSE.RESOURCE, Rest.COURSE.LIST, loginData, filters, expected);
        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }

    @Test @InSequence(95)
    public void getAllCourses_InvalidFilters_CorLev_MinLength() throws Exception{
        try {
            CourseListFilter filters = new CourseListFilter();
            filters.setCourseLevel("x");
            filters.setPageNum(0);
            filters.setSorting(new SortingInfo("Asc", "courseName"));

            LoginData loginData = Params.studentLoginData;

            String expectErrorMsg = MessageFormat.format(Error.FV.EQ_LENGTH, filters.getCourseLevel(),
                    CourseListFilter.FIELDS.get("courseLevel"), 2);

            FormValidation expected = new FormValidation(COURSE_LIST_VAL, expectErrorMsg);
            Util.postSecuredFormValidation(Rest.COURSE.RESOURCE, Rest.COURSE.LIST, loginData, filters, expected);
        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }

    @Test @InSequence(96)
    public void getAllCourses_InvalidFilters_CorLev_MaxLength() throws Exception{
        try {
            CourseListFilter filters = new CourseListFilter();
            filters.setCourseLevel("azx");
            filters.setPageNum(0);
            filters.setSorting(new SortingInfo("Asc", "courseName"));

            LoginData loginData = Params.studentLoginData;

            String expectErrorMsg = MessageFormat.format(Error.FV.EQ_LENGTH, filters.getCourseLevel(),
                    CourseListFilter.FIELDS.get("courseLevel"), 2);

            FormValidation expected = new FormValidation(COURSE_LIST_VAL, expectErrorMsg);
            Util.postSecuredFormValidation(Rest.COURSE.RESOURCE, Rest.COURSE.LIST, loginData, filters, expected);
        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }

    @Test @InSequence(97)
    public void getAllCourses_InvalidFilters_CorLev_InvalidValue() throws Exception{
        try {
            CourseListFilter filters = new CourseListFilter();
            filters.setCourseLevel("12");
            filters.setPageNum(0);
            filters.setSorting(new SortingInfo("Asc", "courseName"));

            LoginData loginData = Params.studentLoginData;

            String expectErrorMsg = MessageFormat.format(Error.FV.REGEX, filters.getCourseLevel(),
                    CourseListFilter.FIELDS.get("courseLevel"), RegExp.MESSAGES.get(RegExp.LOOKUP));

            FormValidation expected = new FormValidation(COURSE_LIST_VAL, expectErrorMsg);
            Util.postSecuredFormValidation(Rest.COURSE.RESOURCE, Rest.COURSE.LIST, loginData, filters, expected);
        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }


    @Test @InSequence(98)
    public void getAllCourses_InvalidFilters_CorType_EmptyStr() throws Exception{
        try {
            CourseListFilter filters = new CourseListFilter();
            filters.setCourseType(" ");
            filters.setPageNum(0);
            filters.setSorting(new SortingInfo("Asc", "courseName"));

            LoginData loginData = Params.studentLoginData;

            String expectErrorMsg = MessageFormat.format(Error.FV.EMPTY_STR, CourseListFilter.FIELDS.get("courseType"));

            FormValidation expected = new FormValidation(COURSE_LIST_VAL, expectErrorMsg);
            Util.postSecuredFormValidation(Rest.COURSE.RESOURCE, Rest.COURSE.LIST, loginData, filters, expected);
        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }

    @Test @InSequence(99)
    public void getAllCourses_InvalidFilters_CorType_MinLength() throws Exception{
        try {
            CourseListFilter filters = new CourseListFilter();
            filters.setCourseType("x");
            filters.setPageNum(0);
            filters.setSorting(new SortingInfo("Asc", "courseName"));

            LoginData loginData = Params.studentLoginData;

            String expectErrorMsg = MessageFormat.format(Error.FV.EQ_LENGTH, filters.getCourseType(),
                    CourseListFilter.FIELDS.get("courseType"), 2);

            FormValidation expected = new FormValidation(COURSE_LIST_VAL, expectErrorMsg);
            Util.postSecuredFormValidation(Rest.COURSE.RESOURCE, Rest.COURSE.LIST, loginData, filters, expected);
        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }

    @Test @InSequence(100)
    public void getAllCourses_InvalidFilters_CorType_MaxLength() throws Exception{
        try {
            CourseListFilter filters = new CourseListFilter();
            filters.setCourseType("azx");
            filters.setPageNum(0);
            filters.setSorting(new SortingInfo("Asc", "courseName"));

            LoginData loginData = Params.studentLoginData;

            String expectErrorMsg = MessageFormat.format(Error.FV.EQ_LENGTH, filters.getCourseType(),
                    CourseListFilter.FIELDS.get("courseType"), 2);

            FormValidation expected = new FormValidation(COURSE_LIST_VAL, expectErrorMsg);
            Util.postSecuredFormValidation(Rest.COURSE.RESOURCE, Rest.COURSE.LIST, loginData, filters, expected);
        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }

    @Test @InSequence(101)
    public void getAllCourses_InvalidFilters_CorType_InvalidValue() throws Exception{
        try {
            CourseListFilter filters = new CourseListFilter();
            filters.setCourseType("12");
            filters.setPageNum(0);
            filters.setSorting(new SortingInfo("Asc", "courseName"));

            LoginData loginData = Params.studentLoginData;

            String expectErrorMsg = MessageFormat.format(Error.FV.REGEX, filters.getCourseType(),
                    CourseListFilter.FIELDS.get("courseType"), RegExp.MESSAGES.get(RegExp.LOOKUP));

            FormValidation expected = new FormValidation(COURSE_LIST_VAL, expectErrorMsg);
            Util.postSecuredFormValidation(Rest.COURSE.RESOURCE, Rest.COURSE.LIST, loginData, filters, expected);
        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }

    @Test @InSequence(102)
    public void getAllCourses_Default_Sorting() throws Exception{
        try {
            CourseListFilter filters = new CourseListFilter();
            filters.setPageNum(0);

            LoginData loginData = Params.studentLoginData;

            Response response = RestUtil.postSecured(Rest.COURSE.RESOURCE, Rest.COURSE.LIST, filters, loginData);
            Assert.assertEquals("Response Status is wrong", Response.Status.OK.getStatusCode(), response.getStatus());

            CourseListRS resultSet = response.readEntity(CourseListRS.class);
            Assert.assertEquals("Total Courses is wrong", 39, resultSet.getPagination().getTotal());

            List<String> expectedCourseIDs = new ArrayList<>();
            expectedCourseIDs.add("Cor0000028AcIn20171217");
            expectedCourseIDs.add("Cor0000029PrBe20171214");
            expectedCourseIDs.add("Cor0000030PrBe20171213");
            expectedCourseIDs.add("Cor0000034PrAd20171025");
            expectedCourseIDs.add("Cor0000035PrBe20171025");
            expectedCourseIDs.add("Cor0000031AcAd20171023");
            expectedCourseIDs.add("Cor0000032PrBe20171022");
            expectedCourseIDs.add("Cor0000033PrBe20171021");
            expectedCourseIDs.add("Cor0000004PrAd20171006");
            expectedCourseIDs.add("Cor0000005AcAd20171002");

            compareCourseLists(expectedCourseIDs, resultSet.getData());
        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }


    @Test @InSequence(103)
    public void getAllCourses_InvalidFilters_PageNum_Negative() throws Exception{
        try {
            CourseListFilter filters = new CourseListFilter();
            filters.setPageNum(-1);
            filters.setSorting(new SortingInfo("Asc", "courseName"));

            LoginData loginData = Params.studentLoginData;

            String expectErrorMsg = MessageFormat.format(Error.FV.POSITIVE_NUM_AND_ZERO, -1,
                    CourseListFilter.FIELDS.get("pageNum"));

            FormValidation expected = new FormValidation(COURSE_LIST_VAL, expectErrorMsg);
            Util.postSecuredFormValidation(Rest.COURSE.RESOURCE, Rest.COURSE.LIST, loginData, filters, expected);
        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }

    @Test @InSequence(104)
    public void getAllCourses_InvalidFilters_PageNum_NullValue() throws Exception{
        try {
            CourseListFilter filters = new CourseListFilter();
            filters.setPageNum(null);
            filters.setSorting(new SortingInfo("Asc", "courseName"));

            LoginData loginData = Params.studentLoginData;

            String expectErrorMsg = MessageFormat.format(Error.FV.REQUIRED, CourseListFilter.FIELDS.get("pageNum"));

            FormValidation expected = new FormValidation(COURSE_LIST_VAL, expectErrorMsg);
            Util.postSecuredFormValidation(Rest.COURSE.RESOURCE, Rest.COURSE.LIST, loginData, filters, expected);
        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }


    @Test @InSequence(105)
    public void getAllCourses_InvalidFilters_SortingBy_NullValue() throws Exception{
        try {
            CourseListFilter filters = new CourseListFilter();
            filters.setPageNum(0);
            filters.setSorting(new SortingInfo("Asc", null));

            LoginData loginData = Params.studentLoginData;

            String expectErrorMsg = MessageFormat.format(Error.FV.REQUIRED, SortingInfo.FIELDS.get("by"));

            FormValidation expected = new FormValidation(COURSE_LIST_VAL, expectErrorMsg);
            Util.postSecuredFormValidation(Rest.COURSE.RESOURCE, Rest.COURSE.LIST, loginData, filters, expected);
        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }

    @Test @InSequence(106)
    public void getAllCourses_InvalidFilters_SortingBy_EmptyStr() throws Exception{
        try {
            CourseListFilter filters = new CourseListFilter();
            filters.setPageNum(0);
            filters.setSorting(new SortingInfo("Asc", " "));

            LoginData loginData = Params.studentLoginData;

            String expectErrorMsg = MessageFormat.format(Error.FV.EMPTY_STR, SortingInfo.FIELDS.get("by"));

            FormValidation expected = new FormValidation(COURSE_LIST_VAL, expectErrorMsg);
            Util.postSecuredFormValidation(Rest.COURSE.RESOURCE, Rest.COURSE.LIST, loginData, filters, expected);
        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }


    @Test @InSequence(107)
    public void getAllCourses_InvalidFilters_SortingDirection_EmptyStr() throws Exception{
        try {
            CourseListFilter filters = new CourseListFilter();
            filters.setPageNum(0);
            filters.setSorting(new SortingInfo(" ", "courseName"));

            LoginData loginData = Params.studentLoginData;

            String expectErrorMsg = MessageFormat.format(Error.FV.EMPTY_STR, SortingInfo.FIELDS.get("direction"));

            FormValidation expected = new FormValidation(COURSE_LIST_VAL, expectErrorMsg);
            Util.postSecuredFormValidation(Rest.COURSE.RESOURCE, Rest.COURSE.LIST, loginData, filters, expected);
        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }

    @Test @InSequence(108)
    public void getAllCourses_InvalidFilters_SortingDirection_MinLength() throws Exception{
        try {
            CourseListFilter filters = new CourseListFilter();
            filters.setPageNum(0);
            filters.setSorting(new SortingInfo("as", "courseName"));

            LoginData loginData = Params.studentLoginData;

            String expectErrorMsg = MessageFormat.format(Error.FV.MIN_MAX_LENGTH, "as",
                    SortingInfo.FIELDS.get("direction"), 3, 4);

            FormValidation expected = new FormValidation(COURSE_LIST_VAL, expectErrorMsg);
            Util.postSecuredFormValidation(Rest.COURSE.RESOURCE, Rest.COURSE.LIST, loginData, filters, expected);
        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }

    @Test @InSequence(109)
    public void getAllCourses_InvalidFilters_SortingDirection_MaxLength() throws Exception{
        try {
            CourseListFilter filters = new CourseListFilter();
            filters.setPageNum(0);
            filters.setSorting(new SortingInfo("asdasd", "courseName"));

            LoginData loginData = Params.studentLoginData;

            String expectErrorMsg = MessageFormat.format(Error.FV.MIN_MAX_LENGTH, "asdasd",
                    SortingInfo.FIELDS.get("direction"), 3, 4);

            FormValidation expected = new FormValidation(COURSE_LIST_VAL, expectErrorMsg);
            Util.postSecuredFormValidation(Rest.COURSE.RESOURCE, Rest.COURSE.LIST, loginData, filters, expected);
        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }

    @Test @InSequence(110)
    public void getAllCourses_InvalidFilters_SortingDirection_InvalidValue() throws Exception{
        try {
            CourseListFilter filters = new CourseListFilter();
            filters.setPageNum(0);
            filters.setSorting(new SortingInfo("afd", "courseName"));

            LoginData loginData = Params.studentLoginData;

            String expectErrorMsg = MessageFormat.format(Error.FV.REGEX, "afd",
                    SortingInfo.FIELDS.get("direction"), RegExp.MESSAGES.get(RegExp.ORDER_DIRECTION));

            FormValidation expected = new FormValidation(COURSE_LIST_VAL, expectErrorMsg);
            Util.postSecuredFormValidation(Rest.COURSE.RESOURCE, Rest.COURSE.LIST, loginData, filters, expected);
        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }

}
