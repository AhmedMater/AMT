package amt.testCases.course;

import am.infrastructure.data.dto.course.CourseData;
import am.infrastructure.data.dto.course.CoursePRData;
import am.infrastructure.data.dto.course.CourseRefData;
import am.infrastructure.data.dto.user.LoginData;
import am.infrastructure.data.dto.user.UserRegisterData;
import am.infrastructure.data.enums.ContentStatus;
import am.infrastructure.data.enums.Roles;
import am.infrastructure.data.hibernate.model.course.Course;
import am.infrastructure.data.hibernate.model.course.CoursePreRequisite;
import am.infrastructure.data.hibernate.model.course.CourseReference;
import am.infrastructure.data.hibernate.model.lookup.CourseLevel;
import am.infrastructure.data.hibernate.model.lookup.CourseType;
import am.infrastructure.data.hibernate.model.lookup.MaterialType;
import am.infrastructure.data.hibernate.model.user.Users;
import am.main.api.AppConfigManager;
import am.main.api.ErrorHandler;
import am.main.api.InfoHandler;
import am.main.api.db.DBManager;
import am.main.common.validation.FormValidation;
import am.main.session.AppSession;
import am.shared.session.Phase;
import amt.common.DeploymentManger;
import amt.common.constants.Error;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import static am.main.data.enums.Interface.ARQUILLIAN;
import static am.main.data.enums.Source.INTEGRATION_TEST;
import static amt.common.constants.Error.COURSE.*;
import static amt.common.constants.Error.LOOKUP_NOT_FOUND;
import static amt.common.constants.Error.TEST_CASE;

/**
 * Created by ahmed.motair on 11/23/2017.
 */
@RunWith(Arquillian.class)
public class NewCourse {
    @Inject private Repository repository;
    @Inject private DBManager dbManager;
    @Inject private AppConfigManager appConfigManager;
    @Inject private DataGenerator dataGenerator;
    @Inject private ErrorHandler errorHandler;
    @Inject private InfoHandler infoHandler;

    private static final String CLASS = "NewCourse";

    private AppSession appSession = new AppSession(INTEGRATION_TEST, ARQUILLIAN, Phase.INTEGRATION_TEST, errorHandler, infoHandler);

    @Deployment
    public static WebArchive createDeployment() {
        return DeploymentManger.createDeployment(Scripts.getAllScripts());
    }

    private CourseData createValidCourseData(){
        List<CoursePRData> coursePRDataList = new ArrayList<>();
        coursePRDataList.add(new CoursePRData(1, "Intro to Algebra", "Co", "https://www.youtube.com/playlist?list=PLA5A1D544934F701B"));
        coursePRDataList.add(new CoursePRData(2, "Intro to Geometry", "Co", "https://www.youtube.com/playlist?list=PLA0B3471F221DBEBA"));
        coursePRDataList.add(new CoursePRData(3, "Elements of Programming Interviews", "Bo", "https://www.amazon.com/gp/product/1517671272/"));

        List<CourseRefData> courseRefDataList = new ArrayList<>();
        courseRefDataList.add(new CourseRefData(1, "Effective Java - 3rd Edition", "Bo", "https://www.amazon.com/gp/product/0134685997"));
        courseRefDataList.add(new CourseRefData(2, "Java Intermediate Tutorials", "Co", "https://www.youtube.com/playlist?list=PL27BCE863B6A864E3"));
        courseRefDataList.add(new CourseRefData(3, "Core Java SE 9 for the Impatient - 2nd Edition", "Bo", "https://www.amazon.com/Core-Java-SE-Impatient-2nd/dp/0134694724"));

        CourseData courseData = new CourseData();
        courseData.setCourseName("Java SE");
        courseData.setCourseLevel("Be");
        courseData.setCourseType("Pr");
        courseData.setEstimatedDuration(30);
        courseData.setDescription("Course Description");
        courseData.setPreRequisites(coursePRDataList);
        courseData.setReferences(courseRefDataList);
        courseData.setEstimatedMinPerDay(20);

//        LocalDateTime startDateTime = LocalDateTime.now().plusDays(1);
//        Date startDate = Date.from(startDateTime.atZone(ZoneId.systemDefault()).toInstant());
//        courseData.setStartDate(startDate);
        return courseData;
    }
    private void compareCourseReferences(Set<CourseReference> courseReferenceSet, List<CourseRefData> courseRefDataList){
        boolean found = false;

        Assert.assertEquals("Some Course References aren't inserted", courseReferenceSet.size(), courseRefDataList.size());

        for (CourseRefData corRefData : courseRefDataList) {
            for (CourseReference corRef : courseReferenceSet) {
                if(corRefData.getNum().equals(corRef.getNum())){
                    Assert.assertEquals("Course Reference Name isn't correct", corRefData.getName(), corRef.getName());
                    Assert.assertEquals("Course Reference Type isn't correct", corRefData.getType(), corRef.getType().getType());
                    Assert.assertEquals("Course Reference URL isn't correct", corRefData.getUrl(), corRef.getUrl());
                    found = true;
                    break;
                }
            }

            if(!found)
                Assert.fail("Course Reference " + corRefData.getName() + " is expected to be inserted in DB with course creation");
            else
                found = false;
        }
    }
    private void compareCoursePreRequisites(Set<CoursePreRequisite> coursePreRequisiteSet, List<CoursePRData> coursePRDataList){
        boolean found = false;

        Assert.assertEquals("Some Course Pre-Requisites aren't inserted", coursePreRequisiteSet.size(), coursePRDataList.size());

        for (CoursePRData corPRData : coursePRDataList) {
            for (CoursePreRequisite corPR : coursePreRequisiteSet) {
                if(corPRData.getNum().equals(corPR.getNum())){
                    Assert.assertEquals("Course Pre-Requisite Name isn't correct", corPRData.getName(), corPR.getName());
                    Assert.assertEquals("Course Pre-Requisite Type isn't correct", corPRData.getType(), corPR.getType().getType());
                    Assert.assertEquals("Course Pre-Requisite URL isn't correct", corPRData.getUrl(), corPR.getUrl());
                    found = true;
                    break;
                }
            }

            if(!found)
                Assert.fail("Course Pre-Requisite " + corPRData.getName() + " is expected to be inserted in DB with course creation");
            else
                found = false;
        }
    }
    private LoginData createTutorUser() throws Exception{
        repository.executeScript(Scripts.ROLE_LOOKUP);

        UserRegisterData student = new UserRegisterData("Ahmed", "Mater",
                "Ahmed_Mater", "123456", "ahmed.motair@gizasystems.com");
        Users studentUser = dataGenerator.registerUser(student);

        dataGenerator.upgradeUserRole_ToTutor(studentUser.getUserID());
        return new LoginData("Ahmed_Mater", "123456");
    }

    @Test
    @InSequence(1)
    public void startClearingAllDBTables() throws Exception{
        try {
            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }

    @Test @InSequence(2)
    public void createCourse_ByTutor(){
        String TEST_CASE_NAME = "createCourse_ByStudent";
        try {
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
            repository.executeScript(Scripts.ROLE_LOOKUP);

            UserRegisterData student = new UserRegisterData("Ahmed", "Mater",
                    "Ahmed_Mater", "123456", "ahmed.motair@gizasystems.com");
            Users studentUser = dataGenerator.registerUser(student);

            dataGenerator.upgradeUserRole_ToTutor(studentUser.getUserID());

            LoginData tutorLoginData = new LoginData("Ahmed_Mater", "123456");

            repository.executeScript(Scripts.CONTENT_STATUS_LOOKUP);
            repository.executeScript(Scripts.COURSE_TYPE_LOOKUP);
            repository.executeScript(Scripts.COURSE_LEVEL_LOOKUP);
            repository.executeScript(Scripts.MATERIAL_TYPE_LOOKUP);
            repository.executeScript(Scripts.DATA_TYPE_LOOKUP);
            repository.executeScript(Scripts.SYSTEM_PARAMETER_LOOKUP);

            CourseData courseData = createValidCourseData().clone();

            Response response = RestUtil.postSecured(Rest.COURSE.RESOURCE, Rest.COURSE.NEW, courseData, tutorLoginData);
            Assert.assertEquals("The Response Status isn't correct", Response.Status.OK.getStatusCode(), response.getStatus());

            Course course = dbManager.find(session, Course.class, "Cor0000001PrBe" + new SimpleDateFormat("yyyyMMdd").format(new Date()), false);
            Assert.assertNotNull("Course isn't inserted in Database", course);

            Assert.assertEquals("Course Name isn't correct", courseData.getCourseName(), course.getCourseName());
            Assert.assertEquals("Course Estimated Duration isn't correct", courseData.getEstimatedDuration(), course.getEstimatedDuration());
            Assert.assertEquals("Course Level isn't correct", courseData.getCourseLevel(), course.getCourseLevel().getLevel());
            Assert.assertEquals("Course Type isn't correct", courseData.getCourseType(), course.getCourseType().getType());
            Assert.assertEquals("Course Status isn't correct", ContentStatus.FUTURE.status(), course.getCourseStatus().getStatus());
            Assert.assertEquals("Course Description isn't correct", courseData.getDescription(), course.getDescription());
            Assert.assertEquals("Course Actual Duration isn't correct", new Integer(0), course.getActualDuration());
            Assert.assertEquals("Course Tutor isn't correct", tutorLoginData.getUsername(), course.getCreatedBy().getUsername());
            Assert.assertEquals("Course Creator Role isn't correct", Roles.TUTOR.role(), course.getCreatedBy().getRole().getRole());
            Assert.assertNull("Course Last Update Date isn't correct", course.getLastUpdateDate());
            Assert.assertNull("Course Start Date isn't correct", course.getStartDate());
            Assert.assertNull("Course Due Date isn't correct", course.getDueDate());

//            LocalDateTime dueDateTime = LocalDateTime.now().plusDays(90);
//            Date expectedDueDate = Date.from(dueDateTime.atZone(ZoneId.systemDefault()).toInstant());
//
//            Assert.assertEquals("Course Min Per Day isn't correct", courseData.getEstimatedMinPerDay(), course.getMinPerDay());
//            Util.isEqualDates(courseData.getStartDate(), course.getStartDate());
//            Util.isEqualDates(expectedDueDate, course.getDueDate());

            compareCourseReferences(course.getReferences(), courseData.getReferences());
            compareCoursePreRequisites(course.getPreRequisites(), courseData.getPreRequisites());

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
        }catch (Exception ex){
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test @InSequence(3)
    public void createCourse_ByAdmin() {
        String TEST_CASE_NAME = "createCourse_ByStudent";
        try {
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
            repository.executeScript(Scripts.ROLE_LOOKUP);

            LoginData adminLoginData = dataGenerator.getAdminLoginData();

            Util.postSecuredStringError(Rest.COURSE.RESOURCE, Rest.COURSE.NEW, adminLoginData, new CourseData(), Error.NOT_AUTHORIZED);

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
        } catch (Exception ex) {
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test @InSequence(4)
    public void createCourse_ByOwner() {
        String TEST_CASE_NAME = "createCourse_ByStudent";
        try {
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
            repository.executeScript(Scripts.ROLE_LOOKUP);

            LoginData ownerLoginData = dataGenerator.getOwnerLoginData();

            Util.postSecuredStringError(Rest.COURSE.RESOURCE, Rest.COURSE.NEW, ownerLoginData, new CourseData(), Error.NOT_AUTHORIZED);

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
        } catch (Exception ex) {
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test @InSequence(5)
    public void createCourse_ByStudent() {
        String TEST_CASE_NAME = "createCourse_ByStudent";
        try {
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
            repository.executeScript(Scripts.ROLE_LOOKUP);

            UserRegisterData student = new UserRegisterData("Ahmed", "Mater",
                    "Ahmed_Mater", "123456", "ahmed.motair@gizasystems.com");
            Users studentUser = dataGenerator.registerUser(student);

            LoginData studentLoginData = new LoginData("Ahmed_Mater", "123456");

            Util.postSecuredStringError(Rest.COURSE.RESOURCE, Rest.COURSE.NEW, studentLoginData, new CourseData(), Error.NOT_AUTHORIZED);

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
        } catch (Exception ex) {
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test @InSequence(6)
    public void createCourse_CourseName_InvalidValue(){
        String TEST_CASE_NAME = "createCourse_CourseName_InvalidValue";
        try {
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
            LoginData tutorLoginData = createTutorUser();

            CourseData courseData = createValidCourseData().clone();
            courseData.setCourseName("Java$%#@^SE");

            FormValidation expected = new FormValidation(NEW_COURSE_VAL, INVALID_COURSE_NAME);
            Util.postSecuredFormValidation(Rest.COURSE.RESOURCE, Rest.COURSE.NEW, tutorLoginData, courseData, expected);

            Course course = dbManager.find(session, Course.class, "Cor0000001PrBe" + new SimpleDateFormat("yyyyMMdd").format(new Date()), false);
            Assert.assertNull("Course is inserted in Database", course);

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
        }catch (Exception ex){
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test @InSequence(7)
    public void createCourse_CourseName_EmptyString(){
        String TEST_CASE_NAME = "createCourse_CourseName_EmptyString";
        try {
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
            LoginData tutorLoginData = createTutorUser();

            CourseData courseData = createValidCourseData().clone();
            courseData.setCourseName("");

            FormValidation expected = new FormValidation(NEW_COURSE_VAL, EMPTY_STR_COURSE_NAME);
            Util.postSecuredFormValidation(Rest.COURSE.RESOURCE, Rest.COURSE.NEW, tutorLoginData, courseData, expected);

            Course course = dbManager.find(session, Course.class, "Cor0000001PrBe" + new SimpleDateFormat("yyyyMMdd").format(new Date()), false);
            Assert.assertNull("Course is inserted in Database", course);

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
        }catch (Exception ex){
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test @InSequence(8)
    public void createCourse_CourseName_MinLength(){
        String TEST_CASE_NAME = "createCourse_CourseName_MinLength";
        try {
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
            LoginData tutorLoginData = createTutorUser();

            CourseData courseData = createValidCourseData().clone();
            courseData.setCourseName(Util.generateString(2));

            FormValidation expected = new FormValidation(NEW_COURSE_VAL, LENGTH_COURSE_NAME);
            Util.postSecuredFormValidation(Rest.COURSE.RESOURCE, Rest.COURSE.NEW, tutorLoginData, courseData, expected);

            Course course = dbManager.find(session, Course.class, "Cor0000001PrBe" + new SimpleDateFormat("yyyyMMdd").format(new Date()), false);
            Assert.assertNull("Course is inserted in Database", course);

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
        }catch (Exception ex){
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test @InSequence(9)
    public void createCourse_CourseName_MaxLength(){
        String TEST_CASE_NAME = "createCourse_CourseName_MaxLength";
        try {
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
            LoginData tutorLoginData = createTutorUser();

            CourseData courseData = createValidCourseData().clone();
            courseData.setCourseName(Util.generateString(105));

            FormValidation expected = new FormValidation(NEW_COURSE_VAL, LENGTH_COURSE_NAME);
            Util.postSecuredFormValidation(Rest.COURSE.RESOURCE, Rest.COURSE.NEW, tutorLoginData, courseData, expected);

            Course course = dbManager.find(session, Course.class, "Cor0000001PrBe" + new SimpleDateFormat("yyyyMMdd").format(new Date()), false);
            Assert.assertNull("Course is inserted in Database", course);

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
        }catch (Exception ex){
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test @InSequence(10)
    public void createCourse_CourseName_Required(){
        String TEST_CASE_NAME = "createCourse_CourseName_Required";
        try {
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
            LoginData tutorLoginData = createTutorUser();

            CourseData courseData = createValidCourseData().clone();
            courseData.setCourseName(null);

            FormValidation expected = new FormValidation(NEW_COURSE_VAL, REQUIRED_COURSE_NAME);
            Util.postSecuredFormValidation(Rest.COURSE.RESOURCE, Rest.COURSE.NEW, tutorLoginData, courseData, expected);

            Course course = dbManager.find(session, Course.class, "Cor0000001PrBe" + new SimpleDateFormat("yyyyMMdd").format(new Date()), false);
            Assert.assertNull("Course is inserted in Database", course);

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
        }catch (Exception ex){
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }


    @Test @InSequence(11)
    public void createCourse_CourseLevel_InvalidValue(){
        String TEST_CASE_NAME = "createCourse_CourseLevel_InvalidValue";
        try {
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
            LoginData tutorLoginData = createTutorUser();

            CourseData courseData = createValidCourseData().clone();
            courseData.setCourseLevel("12");

            FormValidation expected = new FormValidation(NEW_COURSE_VAL, INVALID_COURSE_LEVEL);
            Util.postSecuredFormValidation(Rest.COURSE.RESOURCE, Rest.COURSE.NEW, tutorLoginData, courseData, expected);

            Course course = dbManager.find(session, Course.class, "Cor0000001PrBe" + new SimpleDateFormat("yyyyMMdd").format(new Date()), false);
            Assert.assertNull("Course is inserted in Database", course);

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
        }catch (Exception ex){
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test @InSequence(12)
    public void createCourse_CourseLevel_EmptyString(){
        String TEST_CASE_NAME = "createCourse_CourseLevel_EmptyString";
        try {
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
            LoginData tutorLoginData = createTutorUser();

            CourseData courseData = createValidCourseData().clone();
            courseData.setCourseLevel("");

            FormValidation expected = new FormValidation(NEW_COURSE_VAL, EMPTY_STR_COURSE_LEVEL);
            Util.postSecuredFormValidation(Rest.COURSE.RESOURCE, Rest.COURSE.NEW, tutorLoginData, courseData, expected);

            Course course = dbManager.find(session, Course.class, "Cor0000001PrBe" + new SimpleDateFormat("yyyyMMdd").format(new Date()), false);
            Assert.assertNull("Course is inserted in Database", course);

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
        }catch (Exception ex){
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test @InSequence(13)
    public void createCourse_CourseLevel_MinLength(){
        String TEST_CASE_NAME = "createCourse_CourseLevel_MinLength";
        try {
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
            LoginData tutorLoginData = createTutorUser();

            CourseData courseData = createValidCourseData().clone();
            courseData.setCourseLevel("s");

            FormValidation expected = new FormValidation(NEW_COURSE_VAL, LENGTH_COURSE_LEVEL);
            Util.postSecuredFormValidation(Rest.COURSE.RESOURCE, Rest.COURSE.NEW, tutorLoginData, courseData, expected);

            Course course = dbManager.find(session, Course.class, "Cor0000001PrBe" + new SimpleDateFormat("yyyyMMdd").format(new Date()), false);
            Assert.assertNull("Course is inserted in Database", course);

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
        }catch (Exception ex){
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test @InSequence(14)
    public void createCourse_CourseLevel_MaxLength(){
        String TEST_CASE_NAME = "createCourse_CourseLevel_MaxLength";
        try {
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
            LoginData tutorLoginData = createTutorUser();

            CourseData courseData = createValidCourseData().clone();
            courseData.setCourseLevel("sdsd");

            FormValidation expected = new FormValidation(NEW_COURSE_VAL, LENGTH_COURSE_LEVEL);
            Util.postSecuredFormValidation(Rest.COURSE.RESOURCE, Rest.COURSE.NEW, tutorLoginData, courseData, expected);

            Course course = dbManager.find(session, Course.class, "Cor0000001PrBe" + new SimpleDateFormat("yyyyMMdd").format(new Date()), false);
            Assert.assertNull("Course is inserted in Database", course);

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
        }catch (Exception ex){
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test @InSequence(15)
    public void createCourse_CourseLevel_Required(){
        String TEST_CASE_NAME = "createCourse_CourseLevel_Required";
        try {
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
            LoginData tutorLoginData = createTutorUser();

            CourseData courseData = createValidCourseData().clone();
            courseData.setCourseLevel(null);

            FormValidation expected = new FormValidation(NEW_COURSE_VAL, REQUIRED_COURSE_LEVEL);
            Util.postSecuredFormValidation(Rest.COURSE.RESOURCE, Rest.COURSE.NEW, tutorLoginData, courseData, expected);

            Course course = dbManager.find(session, Course.class, "Cor0000001PrBe" + new SimpleDateFormat("yyyyMMdd").format(new Date()), false);
            Assert.assertNull("Course is inserted in Database", course);

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
        }catch (Exception ex){
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }


    @Test @InSequence(16)
    public void createCourse_CourseType_InvalidValue(){
        String TEST_CASE_NAME = "createCourse_CourseType_InvalidValue";
        try {
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
            LoginData tutorLoginData = createTutorUser();

            CourseData courseData = createValidCourseData().clone();
            courseData.setCourseType("12");

            FormValidation expected = new FormValidation(NEW_COURSE_VAL, INVALID_COURSE_TYPE);
            Util.postSecuredFormValidation(Rest.COURSE.RESOURCE, Rest.COURSE.NEW, tutorLoginData, courseData, expected);

            Course course = dbManager.find(session, Course.class, "Cor0000001PrBe" + new SimpleDateFormat("yyyyMMdd").format(new Date()), false);
            Assert.assertNull("Course is inserted in Database", course);

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
        }catch (Exception ex){
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test @InSequence(17)
    public void createCourse_CourseType_EmptyString(){
        String TEST_CASE_NAME = "createCourse_CourseType_EmptyString";
        try {
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
            LoginData tutorLoginData = createTutorUser();

            CourseData courseData = createValidCourseData().clone();
            courseData.setCourseType("");

            FormValidation expected = new FormValidation(NEW_COURSE_VAL, EMPTY_STR_COURSE_TYPE);
            Util.postSecuredFormValidation(Rest.COURSE.RESOURCE, Rest.COURSE.NEW, tutorLoginData, courseData, expected);

            Course course = dbManager.find(session, Course.class, "Cor0000001PrBe" + new SimpleDateFormat("yyyyMMdd").format(new Date()), false);
            Assert.assertNull("Course is inserted in Database", course);

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
        }catch (Exception ex){
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test @InSequence(18)
    public void createCourse_CourseType_MinLength(){
        String TEST_CASE_NAME = "createCourse_CourseType_MinLength";
        try {
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
            LoginData tutorLoginData = createTutorUser();

            CourseData courseData = createValidCourseData().clone();
            courseData.setCourseType("s");

            FormValidation expected = new FormValidation(NEW_COURSE_VAL, LENGTH_COURSE_TYPE);
            Util.postSecuredFormValidation(Rest.COURSE.RESOURCE, Rest.COURSE.NEW, tutorLoginData, courseData, expected);

            Course course = dbManager.find(session, Course.class, "Cor0000001PrBe" + new SimpleDateFormat("yyyyMMdd").format(new Date()), false);
            Assert.assertNull("Course is inserted in Database", course);

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
        }catch (Exception ex){
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test @InSequence(19)
    public void createCourse_CourseType_MaxLength(){
        String TEST_CASE_NAME = "createCourse_CourseType_MaxLength";
        try {
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
            LoginData tutorLoginData = createTutorUser();

            CourseData courseData = createValidCourseData().clone();
            courseData.setCourseType("sasx");

            FormValidation expected = new FormValidation(NEW_COURSE_VAL, LENGTH_COURSE_TYPE);
            Util.postSecuredFormValidation(Rest.COURSE.RESOURCE, Rest.COURSE.NEW, tutorLoginData, courseData, expected);

            Course course = dbManager.find(session, Course.class, "Cor0000001PrBe" + new SimpleDateFormat("yyyyMMdd").format(new Date()), false);
            Assert.assertNull("Course is inserted in Database", course);

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
        }catch (Exception ex){
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test @InSequence(20)
    public void createCourse_CourseType_Required(){
        String TEST_CASE_NAME = "createCourse_CourseType_Required";
        try {
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
            LoginData tutorLoginData = createTutorUser();

            CourseData courseData = createValidCourseData().clone();
            courseData.setCourseType(null);

            FormValidation expected = new FormValidation(NEW_COURSE_VAL, REQUIRED_COURSE_TYPE);
            Util.postSecuredFormValidation(Rest.COURSE.RESOURCE, Rest.COURSE.NEW, tutorLoginData, courseData, expected);

            Course course = dbManager.find(session, Course.class, "Cor0000001PrBe" + new SimpleDateFormat("yyyyMMdd").format(new Date()), false);
            Assert.assertNull("Course is inserted in Database", course);

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
        }catch (Exception ex){
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }


    @Test @InSequence(21)
    public void createCourse_CourseDescription_EmptyString(){
        String TEST_CASE_NAME = "createCourse_CourseDescription_EmptyString";
        try {
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
            LoginData tutorLoginData = createTutorUser();

            CourseData courseData = createValidCourseData().clone();
            courseData.setDescription("");

            FormValidation expected = new FormValidation(NEW_COURSE_VAL, EMPTY_STR_COURSE_DESCRIPTION);
            Util.postSecuredFormValidation(Rest.COURSE.RESOURCE, Rest.COURSE.NEW, tutorLoginData, courseData, expected);

            Course course = dbManager.find(session, Course.class, "Cor0000001PrBe" + new SimpleDateFormat("yyyyMMdd").format(new Date()), false);
            Assert.assertNull("Course is inserted in Database", course);

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
        }catch (Exception ex){
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test @InSequence(22)
    public void createCourse_CourseDescription_MaxLength(){
        String TEST_CASE_NAME = "createCourse_CourseDescription_MaxLength";
        try {
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
            LoginData tutorLoginData = createTutorUser();

            CourseData courseData = createValidCourseData().clone();
            courseData.setDescription(Util.generateString(210));

            FormValidation expected = new FormValidation(NEW_COURSE_VAL, LENGTH_COURSE_DESCRIPTION);
            Util.postSecuredFormValidation(Rest.COURSE.RESOURCE, Rest.COURSE.NEW, tutorLoginData, courseData, expected);

            Course course = dbManager.find(session, Course.class, "Cor0000001PrBe" + new SimpleDateFormat("yyyyMMdd").format(new Date()), false);
            Assert.assertNull("Course is inserted in Database", course);

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
        }catch (Exception ex){
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }


//    @Test @InSequence(23)
//    public void createCourse_CourseEstimatedDuration_InvalidValue(){
//        String TEST_CASE_NAME = "createCourse_CourseEstimatedDuration_InvalidValue";
//        try {
//            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);
//
//            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
//            LoginData tutorLoginData = createTutorUser();
//
//            CourseData courseData = createValidCourseData().clone();
//
//
//            FormValidation expected = new FormValidation(NEW_COURSE_VAL, INVALID_COURSE_NAME);
//            Util.postSecuredFormValidation(Rest.COURSE.RESOURCE, Rest.COURSE.NEW, tutorLoginData, courseData, expected);
//
//            Course course = dbManager.find(session, Course.class, "Cor0000001PrBe" + new SimpleDateFormat("yyyyMMdd").format(new Date()), false);
//            Assert.assertNull("Course is inserted in Database", course);
//
//            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
//        }catch (Exception ex){
//            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
//        }
//    }

    @Test @InSequence(24)
    public void createCourse_CourseEstimatedDuration_MinValue(){
        String TEST_CASE_NAME = "createCourse_CourseEstimatedDuration_MinValue";
        try {
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
            LoginData tutorLoginData = createTutorUser();

            CourseData courseData = createValidCourseData().clone();
            courseData.setEstimatedDuration(3);

            FormValidation expected = new FormValidation(NEW_COURSE_VAL, MIN_VALUE_COURSE_DURATION);
            Util.postSecuredFormValidation(Rest.COURSE.RESOURCE, Rest.COURSE.NEW, tutorLoginData, courseData, expected);

            Course course = dbManager.find(session, Course.class, "Cor0000001PrBe" + new SimpleDateFormat("yyyyMMdd").format(new Date()), false);
            Assert.assertNull("Course is inserted in Database", course);

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
        }catch (Exception ex){
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test @InSequence(25)
    public void createCourse_CourseEstimatedDuration_Required(){
        String TEST_CASE_NAME = "createCourse_CourseEstimatedDuration_Required";
        try {
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
            LoginData tutorLoginData = createTutorUser();

            CourseData courseData = createValidCourseData().clone();
            courseData.setEstimatedDuration(null);

            FormValidation expected = new FormValidation(NEW_COURSE_VAL, REQUIRED_COURSE_DURATION);
            Util.postSecuredFormValidation(Rest.COURSE.RESOURCE, Rest.COURSE.NEW, tutorLoginData, courseData, expected);

            Course course = dbManager.find(session, Course.class, "Cor0000001PrBe" + new SimpleDateFormat("yyyyMMdd").format(new Date()), false);
            Assert.assertNull("Course is inserted in Database", course);

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
        }catch (Exception ex){
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }


    @Test @InSequence(26)
    public void createCourse_CourseMinPerDay_InvalidValue(){
        String TEST_CASE_NAME = "createCourse_CourseMinPerDay_InvalidValue";
        try {
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
            LoginData tutorLoginData = createTutorUser();

            CourseData courseData = createValidCourseData().clone();
            courseData.setEstimatedMinPerDay(-1);

            FormValidation expected = new FormValidation(NEW_COURSE_VAL, INVALID_MIN_PER_DAY, MIN_VALUE_MIN_PER_DAY);
            Util.postSecuredFormValidation(Rest.COURSE.RESOURCE, Rest.COURSE.NEW, tutorLoginData, courseData, expected);

            Course course = dbManager.find(session, Course.class, "Cor0000001PrBe" + new SimpleDateFormat("yyyyMMdd").format(new Date()), false);
            Assert.assertNull("Course is inserted in Database", course);

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
        }catch (Exception ex){
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test @InSequence(27)
    public void createCourse_CourseMinPerDay_MinValue(){
        String TEST_CASE_NAME = "createCourse_CourseMinPerDay_MinValue";
        try {
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
            LoginData tutorLoginData = createTutorUser();

            CourseData courseData = createValidCourseData().clone();
            courseData.setEstimatedMinPerDay(4);

            FormValidation expected = new FormValidation(NEW_COURSE_VAL, MIN_VALUE_MIN_PER_DAY);
            Util.postSecuredFormValidation(Rest.COURSE.RESOURCE, Rest.COURSE.NEW, tutorLoginData, courseData, expected);

            Course course = dbManager.find(session, Course.class, "Cor0000001PrBe" + new SimpleDateFormat("yyyyMMdd").format(new Date()), false);
            Assert.assertNull("Course is inserted in Database", course);

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
        }catch (Exception ex){
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test @InSequence(28)
    public void createCourse_CourseMinPerDay_Required(){
        String TEST_CASE_NAME = "createCourse_CourseMinPerDay_Required";
        try {
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
            LoginData tutorLoginData = createTutorUser();

            CourseData courseData = createValidCourseData().clone();
            courseData.setEstimatedMinPerDay(null);

            FormValidation expected = new FormValidation(NEW_COURSE_VAL, REQUIRED_MIN_PER_DAY);
            Util.postSecuredFormValidation(Rest.COURSE.RESOURCE, Rest.COURSE.NEW, tutorLoginData, courseData, expected);

            Course course = dbManager.find(session, Course.class, "Cor0000001PrBe" + new SimpleDateFormat("yyyyMMdd").format(new Date()), false);
            Assert.assertNull("Course is inserted in Database", course);

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
        }catch (Exception ex){
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }


    @Test @InSequence(29)
    public void createCourse_CoursePreRequisiteOrder_InvalidValue(){
        String TEST_CASE_NAME = "createCourse_CourseName_InvalidValue";
        try {
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
            LoginData tutorLoginData = createTutorUser();

            CourseData courseData = createValidCourseData().clone();
            courseData.getPreRequisites().get(0).setNum(-1);

            FormValidation expected = new FormValidation(NEW_COURSE_VAL, MIN_VALUE_COURSE_PRE_REQUISITE_ORDER, INVALID_COURSE_PRE_REQUISITE_ORDER);
            Util.postSecuredFormValidation(Rest.COURSE.RESOURCE, Rest.COURSE.NEW, tutorLoginData, courseData, expected);

            Course course = dbManager.find(session, Course.class, "Cor0000001PrBe" + new SimpleDateFormat("yyyyMMdd").format(new Date()), false);
            Assert.assertNull("Course is inserted in Database", course);

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
        }catch (Exception ex){
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test @InSequence(30)
    public void createCourse_CoursePreRequisiteOrder_MinValue(){
        String TEST_CASE_NAME = "createCourse_CoursePreRequisiteOrder_MinValue";
        try {
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
            LoginData tutorLoginData = createTutorUser();

            CourseData courseData = createValidCourseData().clone();
            courseData.getPreRequisites().get(0).setNum(0);

            FormValidation expected = new FormValidation(NEW_COURSE_VAL, MIN_VALUE_COURSE_PRE_REQUISITE_ORDER, INVALID_COURSE_PRE_REQUISITE_ORDER);
            Util.postSecuredFormValidation(Rest.COURSE.RESOURCE, Rest.COURSE.NEW, tutorLoginData, courseData, expected);

            Course course = dbManager.find(session, Course.class, "Cor0000001PrBe" + new SimpleDateFormat("yyyyMMdd").format(new Date()), false);
            Assert.assertNull("Course is inserted in Database", course);

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
        }catch (Exception ex){
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test @InSequence(31)
    public void createCourse_CoursePreRequisiteOrder_Required(){
        String TEST_CASE_NAME = "createCourse_CoursePreRequisiteOrder_Required";
        try {
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
            LoginData tutorLoginData = createTutorUser();

            CourseData courseData = createValidCourseData().clone();
            courseData.getPreRequisites().get(0).setNum(null);

            FormValidation expected = new FormValidation(NEW_COURSE_VAL, REQUIRED_COURSE_PRE_REQUISITE_ORDER);
            Util.postSecuredFormValidation(Rest.COURSE.RESOURCE, Rest.COURSE.NEW, tutorLoginData, courseData, expected);

            Course course = dbManager.find(session, Course.class, "Cor0000001PrBe" + new SimpleDateFormat("yyyyMMdd").format(new Date()), false);
            Assert.assertNull("Course is inserted in Database", course);

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
        }catch (Exception ex){
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test @InSequence(32)
    public void createCourse_CoursePreRequisiteName_InvalidValue(){
        String TEST_CASE_NAME = "createCourse_CoursePreRequisiteName_InvalidValue";
        try {
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
            LoginData tutorLoginData = createTutorUser();

            CourseData courseData = createValidCourseData().clone();
            courseData.getPreRequisites().get(0).setName("Cor$%#^@ASD");

            FormValidation expected = new FormValidation(NEW_COURSE_VAL, INVALID_COURSE_PRE_REQUISITE_NAME);
            Util.postSecuredFormValidation(Rest.COURSE.RESOURCE, Rest.COURSE.NEW, tutorLoginData, courseData, expected);

            Course course = dbManager.find(session, Course.class, "Cor0000001PrBe" + new SimpleDateFormat("yyyyMMdd").format(new Date()), false);
            Assert.assertNull("Course is inserted in Database", course);

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
        }catch (Exception ex){
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test @InSequence(33)
    public void createCourse_CoursePreRequisiteName_EmptyString(){
        String TEST_CASE_NAME = "createCourse_CoursePreRequisiteName_EmptyString";
        try {
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
            LoginData tutorLoginData = createTutorUser();

            CourseData courseData = createValidCourseData().clone();
            courseData.getPreRequisites().get(0).setName("");

            FormValidation expected = new FormValidation(NEW_COURSE_VAL, EMPTY_STR_COURSE_PRE_REQUISITE_NAME);
            Util.postSecuredFormValidation(Rest.COURSE.RESOURCE, Rest.COURSE.NEW, tutorLoginData, courseData, expected);

            Course course = dbManager.find(session, Course.class, "Cor0000001PrBe" + new SimpleDateFormat("yyyyMMdd").format(new Date()), false);
            Assert.assertNull("Course is inserted in Database", course);

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
        }catch (Exception ex){
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test @InSequence(34)
    public void createCourse_CoursePreRequisiteName_MinLength(){
        String TEST_CASE_NAME = "createCourse_CoursePreRequisiteName_MinLength";
        try {
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
            LoginData tutorLoginData = createTutorUser();

            CourseData courseData = createValidCourseData().clone();
            courseData.getPreRequisites().get(0).setName(Util.generateString(2));

            FormValidation expected = new FormValidation(NEW_COURSE_VAL, LENGTH_COURSE_PRE_REQUISITE_NAME);
            Util.postSecuredFormValidation(Rest.COURSE.RESOURCE, Rest.COURSE.NEW, tutorLoginData, courseData, expected);

            Course course = dbManager.find(session, Course.class, "Cor0000001PrBe" + new SimpleDateFormat("yyyyMMdd").format(new Date()), false);
            Assert.assertNull("Course is inserted in Database", course);

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
        }catch (Exception ex){
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test @InSequence(35)
    public void createCourse_CoursePreRequisiteName_MaxLength(){
        String TEST_CASE_NAME = "createCourse_CoursePreRequisiteName_MaxLength";
        try {
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
            LoginData tutorLoginData = createTutorUser();

            CourseData courseData = createValidCourseData().clone();
            courseData.getPreRequisites().get(0).setName(Util.generateString(110));

            FormValidation expected = new FormValidation(NEW_COURSE_VAL, LENGTH_COURSE_PRE_REQUISITE_NAME);
            Util.postSecuredFormValidation(Rest.COURSE.RESOURCE, Rest.COURSE.NEW, tutorLoginData, courseData, expected);

            Course course = dbManager.find(session, Course.class, "Cor0000001PrBe" + new SimpleDateFormat("yyyyMMdd").format(new Date()), false);
            Assert.assertNull("Course is inserted in Database", course);

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
        }catch (Exception ex){
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test @InSequence(36)
    public void createCourse_CoursePreRequisiteName_Required(){
        String TEST_CASE_NAME = "createCourse_CoursePreRequisiteName_Required";
        try {
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
            LoginData tutorLoginData = createTutorUser();

            CourseData courseData = createValidCourseData().clone();
            courseData.getPreRequisites().get(0).setName(null);

            FormValidation expected = new FormValidation(NEW_COURSE_VAL, REQUIRED_COURSE_PRE_REQUISITE_NAME);
            Util.postSecuredFormValidation(Rest.COURSE.RESOURCE, Rest.COURSE.NEW, tutorLoginData, courseData, expected);

            Course course = dbManager.find(session, Course.class, "Cor0000001PrBe" + new SimpleDateFormat("yyyyMMdd").format(new Date()), false);
            Assert.assertNull("Course is inserted in Database", course);

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
        }catch (Exception ex){
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test @InSequence(37)
    public void createCourse_CoursePreRequisiteType_InvalidValue(){
        String TEST_CASE_NAME = "createCourse_CoursePreRequisiteType_InvalidValue";
        try {
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
            LoginData tutorLoginData = createTutorUser();

            CourseData courseData = createValidCourseData().clone();
            courseData.getPreRequisites().get(0).setType("12");

            FormValidation expected = new FormValidation(NEW_COURSE_VAL, INVALID_COURSE_PRE_REQUISITE_TYPE);
            Util.postSecuredFormValidation(Rest.COURSE.RESOURCE, Rest.COURSE.NEW, tutorLoginData, courseData, expected);

            Course course = dbManager.find(session, Course.class, "Cor0000001PrBe" + new SimpleDateFormat("yyyyMMdd").format(new Date()), false);
            Assert.assertNull("Course is inserted in Database", course);

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
        }catch (Exception ex){
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test @InSequence(38)
    public void createCourse_CoursePreRequisiteType_EmptyString(){
        String TEST_CASE_NAME = "createCourse_CoursePreRequisiteType_EmptyString";
        try {
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
            LoginData tutorLoginData = createTutorUser();

            CourseData courseData = createValidCourseData().clone();
            courseData.getPreRequisites().get(0).setType("");

            FormValidation expected = new FormValidation(NEW_COURSE_VAL, EMPTY_STR_COURSE_PRE_REQUISITE_TYPE);
            Util.postSecuredFormValidation(Rest.COURSE.RESOURCE, Rest.COURSE.NEW, tutorLoginData, courseData, expected);

            Course course = dbManager.find(session, Course.class, "Cor0000001PrBe" + new SimpleDateFormat("yyyyMMdd").format(new Date()), false);
            Assert.assertNull("Course is inserted in Database", course);

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
        }catch (Exception ex){
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test @InSequence(39)
    public void createCourse_CoursePreRequisiteType_MinLength(){
        String TEST_CASE_NAME = "createCourse_CoursePreRequisiteType_MinLength";
        try {
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
            LoginData tutorLoginData = createTutorUser();

            CourseData courseData = createValidCourseData().clone();
            courseData.getPreRequisites().get(0).setType("a");

            FormValidation expected = new FormValidation(NEW_COURSE_VAL, LENGTH_COURSE_PRE_REQUISITE_TYPE);
            Util.postSecuredFormValidation(Rest.COURSE.RESOURCE, Rest.COURSE.NEW, tutorLoginData, courseData, expected);

            Course course = dbManager.find(session, Course.class, "Cor0000001PrBe" + new SimpleDateFormat("yyyyMMdd").format(new Date()), false);
            Assert.assertNull("Course is inserted in Database", course);

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
        }catch (Exception ex){
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test @InSequence(40)
    public void createCourse_CoursePreRequisiteType_MaxLength(){
        String TEST_CASE_NAME = "createCourse_CoursePreRequisiteType_MaxLength";
        try {
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
            LoginData tutorLoginData = createTutorUser();

            CourseData courseData = createValidCourseData().clone();
            courseData.getPreRequisites().get(0).setType("asd");

            FormValidation expected = new FormValidation(NEW_COURSE_VAL, LENGTH_COURSE_PRE_REQUISITE_TYPE);
            Util.postSecuredFormValidation(Rest.COURSE.RESOURCE, Rest.COURSE.NEW, tutorLoginData, courseData, expected);

            Course course = dbManager.find(session, Course.class, "Cor0000001PrBe" + new SimpleDateFormat("yyyyMMdd").format(new Date()), false);
            Assert.assertNull("Course is inserted in Database", course);

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
        }catch (Exception ex){
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test @InSequence(41)
    public void createCourse_CoursePreRequisiteType_Required(){
        String TEST_CASE_NAME = "createCourse_CoursePreRequisiteType_Required";
        try {
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
            LoginData tutorLoginData = createTutorUser();

            CourseData courseData = createValidCourseData().clone();
            courseData.getPreRequisites().get(0).setType(null);

            FormValidation expected = new FormValidation(NEW_COURSE_VAL, REQUIRED_COURSE_PRE_REQUISITE_TYPE);
            Util.postSecuredFormValidation(Rest.COURSE.RESOURCE, Rest.COURSE.NEW, tutorLoginData, courseData, expected);

            Course course = dbManager.find(session, Course.class, "Cor0000001PrBe" + new SimpleDateFormat("yyyyMMdd").format(new Date()), false);
            Assert.assertNull("Course is inserted in Database", course);

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
        }catch (Exception ex){
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test @InSequence(42)
    public void createCourse_CoursePreRequisiteURL_InvalidValue(){
        String TEST_CASE_NAME = "createCourse_CoursePreRequisiteURL_InvalidValue";
        try {
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
            LoginData tutorLoginData = createTutorUser();

            CourseData courseData = createValidCourseData().clone();
            courseData.getPreRequisites().get(0).setUrl("Ahmed");

            FormValidation expected = new FormValidation(NEW_COURSE_VAL, INVALID_COURSE_PRE_REQUISITE_URL);
            Util.postSecuredFormValidation(Rest.COURSE.RESOURCE, Rest.COURSE.NEW, tutorLoginData, courseData, expected);

            Course course = dbManager.find(session, Course.class, "Cor0000001PrBe" + new SimpleDateFormat("yyyyMMdd").format(new Date()), false);
            Assert.assertNull("Course is inserted in Database", course);

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
        }catch (Exception ex){
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test @InSequence(43)
    public void createCourse_CoursePreRequisiteURL_EmptyString(){
        String TEST_CASE_NAME = "createCourse_CoursePreRequisiteURL_EmptyString";
        try {
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
            LoginData tutorLoginData = createTutorUser();

            CourseData courseData = createValidCourseData().clone();
            courseData.getPreRequisites().get(0).setUrl("");

            FormValidation expected = new FormValidation(NEW_COURSE_VAL, EMPTY_STR_COURSE_PRE_REQUISITE_URL);
            Util.postSecuredFormValidation(Rest.COURSE.RESOURCE, Rest.COURSE.NEW, tutorLoginData, courseData, expected);

            Course course = dbManager.find(session, Course.class, "Cor0000001PrBe" + new SimpleDateFormat("yyyyMMdd").format(new Date()), false);
            Assert.assertNull("Course is inserted in Database", course);

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
        }catch (Exception ex){
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test @InSequence(44)
    public void createCourse_CoursePreRequisiteURL_MinLength(){
        String TEST_CASE_NAME = "createCourse_CoursePreRequisiteURL_MinLength";
        try {
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
            LoginData tutorLoginData = createTutorUser();

            CourseData courseData = createValidCourseData().clone();
            courseData.getPreRequisites().get(0).setUrl("http");

            FormValidation expected = new FormValidation(NEW_COURSE_VAL, LENGTH_COURSE_PRE_REQUISITE_URL);
            Util.postSecuredFormValidation(Rest.COURSE.RESOURCE, Rest.COURSE.NEW, tutorLoginData, courseData, expected);

            Course course = dbManager.find(session, Course.class, "Cor0000001PrBe" + new SimpleDateFormat("yyyyMMdd").format(new Date()), false);
            Assert.assertNull("Course is inserted in Database", course);

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
        }catch (Exception ex){
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test @InSequence(45)
    public void createCourse_CoursePreRequisiteURL_MaxLength(){
        String TEST_CASE_NAME = "createCourse_CoursePreRequisiteURL_MaxLength";
        try {
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
            LoginData tutorLoginData = createTutorUser();

            CourseData courseData = createValidCourseData().clone();
            courseData.getPreRequisites().get(0).setUrl("https://www.youtube.com/watch?v=VVV0Pa4PkP0" + Util.generateString(210));

            FormValidation expected = new FormValidation(NEW_COURSE_VAL, LENGTH_COURSE_PRE_REQUISITE_URL);
            Util.postSecuredFormValidation(Rest.COURSE.RESOURCE, Rest.COURSE.NEW, tutorLoginData, courseData, expected);

            Course course = dbManager.find(session, Course.class, "Cor0000001PrBe" + new SimpleDateFormat("yyyyMMdd").format(new Date()), false);
            Assert.assertNull("Course is inserted in Database", course);

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
        }catch (Exception ex){
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test @InSequence(46)
    public void createCourse_CoursePreRequisiteURL_Required(){
        String TEST_CASE_NAME = "createCourse_CourseName_Required";
        try {
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
            LoginData tutorLoginData = createTutorUser();

            CourseData courseData = createValidCourseData().clone();
            courseData.getPreRequisites().get(0).setUrl(null);

            FormValidation expected = new FormValidation(NEW_COURSE_VAL, REQUIRED_COURSE_PRE_REQUISITE_URL);
            Util.postSecuredFormValidation(Rest.COURSE.RESOURCE, Rest.COURSE.NEW, tutorLoginData, courseData, expected);

            Course course = dbManager.find(session, Course.class, "Cor0000001PrBe" + new SimpleDateFormat("yyyyMMdd").format(new Date()), false);
            Assert.assertNull("Course is inserted in Database", course);

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
        }catch (Exception ex){
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test @InSequence(47)
    public void createCourse_CourseReferenceOrder_InvalidValue(){
        String TEST_CASE_NAME = "createCourse_CourseReferenceOrder_InvalidValue";
        try {
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
            LoginData tutorLoginData = createTutorUser();

            CourseData courseData = createValidCourseData().clone();
            courseData.getReferences().get(0).setNum(-1);

            FormValidation expected = new FormValidation(NEW_COURSE_VAL, INVALID_COURSE_REFERENCE_ORDER, MIN_VALUE_COURSE_REFERENCE_ORDER);
            Util.postSecuredFormValidation(Rest.COURSE.RESOURCE, Rest.COURSE.NEW, tutorLoginData, courseData, expected);

            Course course = dbManager.find(session, Course.class, "Cor0000001PrBe" + new SimpleDateFormat("yyyyMMdd").format(new Date()), false);
            Assert.assertNull("Course is inserted in Database", course);

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
        }catch (Exception ex){
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test @InSequence(48)
    public void createCourse_CourseReferenceOrder_MinValue(){
        String TEST_CASE_NAME = "createCourse_CourseReferenceOrder_MinValue";
        try {
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
            LoginData tutorLoginData = createTutorUser();

            CourseData courseData = createValidCourseData().clone();
            courseData.getReferences().get(0).setNum(0);

            FormValidation expected = new FormValidation(NEW_COURSE_VAL, INVALID_COURSE_REFERENCE_ORDER, MIN_VALUE_COURSE_REFERENCE_ORDER);
            Util.postSecuredFormValidation(Rest.COURSE.RESOURCE, Rest.COURSE.NEW, tutorLoginData, courseData, expected);

            Course course = dbManager.find(session, Course.class, "Cor0000001PrBe" + new SimpleDateFormat("yyyyMMdd").format(new Date()), false);
            Assert.assertNull("Course is inserted in Database", course);

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
        }catch (Exception ex){
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test @InSequence(49)
    public void createCourse_CourseReferenceOrder_Required(){
        String TEST_CASE_NAME = "createCourse_CourseReferenceOrder_Required";
        try {
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
            LoginData tutorLoginData = createTutorUser();

            CourseData courseData = createValidCourseData().clone();
            courseData.getReferences().get(0).setNum(null);

            FormValidation expected = new FormValidation(NEW_COURSE_VAL, REQUIRED_COURSE_REFERENCE_ORDER);
            Util.postSecuredFormValidation(Rest.COURSE.RESOURCE, Rest.COURSE.NEW, tutorLoginData, courseData, expected);

            Course course = dbManager.find(session, Course.class, "Cor0000001PrBe" + new SimpleDateFormat("yyyyMMdd").format(new Date()), false);
            Assert.assertNull("Course is inserted in Database", course);

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
        }catch (Exception ex){
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test @InSequence(50)
    public void createCourse_CourseReferenceName_InvalidValue(){
        String TEST_CASE_NAME = "createCourse_CourseReferenceName_InvalidValue";
        try {
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
            LoginData tutorLoginData = createTutorUser();

            CourseData courseData = createValidCourseData().clone();
            courseData.getReferences().get(0).setName("Cor$%#^@ASD");

            FormValidation expected = new FormValidation(NEW_COURSE_VAL, INVALID_COURSE_REFERENCE_NAME);
            Util.postSecuredFormValidation(Rest.COURSE.RESOURCE, Rest.COURSE.NEW, tutorLoginData, courseData, expected);

            Course course = dbManager.find(session, Course.class, "Cor0000001PrBe" + new SimpleDateFormat("yyyyMMdd").format(new Date()), false);
            Assert.assertNull("Course is inserted in Database", course);

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
        }catch (Exception ex){
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test @InSequence(51)
    public void createCourse_CourseReferenceName_EmptyString(){
        String TEST_CASE_NAME = "createCourse_CourseReferenceName_EmptyString";
        try {
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
            LoginData tutorLoginData = createTutorUser();

            CourseData courseData = createValidCourseData().clone();
            courseData.getReferences().get(0).setName("");

            FormValidation expected = new FormValidation(NEW_COURSE_VAL, EMPTY_STR_COURSE_REFERENCE_NAME);
            Util.postSecuredFormValidation(Rest.COURSE.RESOURCE, Rest.COURSE.NEW, tutorLoginData, courseData, expected);

            Course course = dbManager.find(session, Course.class, "Cor0000001PrBe" + new SimpleDateFormat("yyyyMMdd").format(new Date()), false);
            Assert.assertNull("Course is inserted in Database", course);

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
        }catch (Exception ex){
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test @InSequence(52)
    public void createCourse_CourseReferenceName_MinLength(){
        String TEST_CASE_NAME = "createCourse_CourseReferenceName_MinLength";
        try {
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
            LoginData tutorLoginData = createTutorUser();

            CourseData courseData = createValidCourseData().clone();
            courseData.getReferences().get(0).setName(Util.generateString(2));

            FormValidation expected = new FormValidation(NEW_COURSE_VAL, LENGTH_COURSE_REFERENCE_NAME);
            Util.postSecuredFormValidation(Rest.COURSE.RESOURCE, Rest.COURSE.NEW, tutorLoginData, courseData, expected);

            Course course = dbManager.find(session, Course.class, "Cor0000001PrBe" + new SimpleDateFormat("yyyyMMdd").format(new Date()), false);
            Assert.assertNull("Course is inserted in Database", course);

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
        }catch (Exception ex){
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test @InSequence(53)
    public void createCourse_CourseReferenceName_MaxLength(){
        String TEST_CASE_NAME = "createCourse_CourseReferenceName_MaxLength";
        try {
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
            LoginData tutorLoginData = createTutorUser();

            CourseData courseData = createValidCourseData().clone();
            courseData.getReferences().get(0).setName(Util.generateString(110));

            FormValidation expected = new FormValidation(NEW_COURSE_VAL, LENGTH_COURSE_REFERENCE_NAME);
            Util.postSecuredFormValidation(Rest.COURSE.RESOURCE, Rest.COURSE.NEW, tutorLoginData, courseData, expected);

            Course course = dbManager.find(session, Course.class, "Cor0000001PrBe" + new SimpleDateFormat("yyyyMMdd").format(new Date()), false);
            Assert.assertNull("Course is inserted in Database", course);

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
        }catch (Exception ex){
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test @InSequence(54)
    public void createCourse_CourseReferenceName_Required(){
        String TEST_CASE_NAME = "createCourse_CourseReferenceName_Required";
        try {
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
            LoginData tutorLoginData = createTutorUser();

            CourseData courseData = createValidCourseData().clone();
            courseData.getReferences().get(0).setName(null);

            FormValidation expected = new FormValidation(NEW_COURSE_VAL, REQUIRED_COURSE_REFERENCE_NAME);
            Util.postSecuredFormValidation(Rest.COURSE.RESOURCE, Rest.COURSE.NEW, tutorLoginData, courseData, expected);

            Course course = dbManager.find(session, Course.class, "Cor0000001PrBe" + new SimpleDateFormat("yyyyMMdd").format(new Date()), false);
            Assert.assertNull("Course is inserted in Database", course);

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
        }catch (Exception ex){
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test @InSequence(55)
    public void createCourse_CourseReferenceType_InvalidValue(){
        String TEST_CASE_NAME = "createCourse_CourseReferenceType_InvalidValue";
        try {
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
            LoginData tutorLoginData = createTutorUser();

            CourseData courseData = createValidCourseData().clone();
            courseData.getReferences().get(0).setType("12");

            FormValidation expected = new FormValidation(NEW_COURSE_VAL, INVALID_COURSE_REFERENCE_TYPE);
            Util.postSecuredFormValidation(Rest.COURSE.RESOURCE, Rest.COURSE.NEW, tutorLoginData, courseData, expected);

            Course course = dbManager.find(session, Course.class, "Cor0000001PrBe" + new SimpleDateFormat("yyyyMMdd").format(new Date()), false);
            Assert.assertNull("Course is inserted in Database", course);

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
        }catch (Exception ex){
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test @InSequence(56)
    public void createCourse_CourseReferenceType_EmptyString(){
        String TEST_CASE_NAME = "createCourse_CourseReferenceType_EmptyString";
        try {
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
            LoginData tutorLoginData = createTutorUser();

            CourseData courseData = createValidCourseData().clone();
            courseData.getReferences().get(0).setType("");

            FormValidation expected = new FormValidation(NEW_COURSE_VAL, EMPTY_STR_COURSE_REFERENCE_TYPE);
            Util.postSecuredFormValidation(Rest.COURSE.RESOURCE, Rest.COURSE.NEW, tutorLoginData, courseData, expected);

            Course course = dbManager.find(session, Course.class, "Cor0000001PrBe" + new SimpleDateFormat("yyyyMMdd").format(new Date()), false);
            Assert.assertNull("Course is inserted in Database", course);

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
        }catch (Exception ex){
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test @InSequence(57)
    public void createCourse_CourseReferenceType_MinLength(){
        String TEST_CASE_NAME = "createCourse_CourseReferenceType_MinLength";
        try {
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
            LoginData tutorLoginData = createTutorUser();

            CourseData courseData = createValidCourseData().clone();
            courseData.getReferences().get(0).setType("a");

            FormValidation expected = new FormValidation(NEW_COURSE_VAL, LENGTH_COURSE_REFERENCE_TYPE);
            Util.postSecuredFormValidation(Rest.COURSE.RESOURCE, Rest.COURSE.NEW, tutorLoginData, courseData, expected);

            Course course = dbManager.find(session, Course.class, "Cor0000001PrBe" + new SimpleDateFormat("yyyyMMdd").format(new Date()), false);
            Assert.assertNull("Course is inserted in Database", course);

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
        }catch (Exception ex){
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test @InSequence(58)
    public void createCourse_CourseReferenceType_MaxLength(){
        String TEST_CASE_NAME = "createCourse_CourseReferenceType_MaxLength";
        try {
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
            LoginData tutorLoginData = createTutorUser();

            CourseData courseData = createValidCourseData().clone();
            courseData.getReferences().get(0).setType("asd");

            FormValidation expected = new FormValidation(NEW_COURSE_VAL, LENGTH_COURSE_REFERENCE_TYPE);
            Util.postSecuredFormValidation(Rest.COURSE.RESOURCE, Rest.COURSE.NEW, tutorLoginData, courseData, expected);

            Course course = dbManager.find(session, Course.class, "Cor0000001PrBe" + new SimpleDateFormat("yyyyMMdd").format(new Date()), false);
            Assert.assertNull("Course is inserted in Database", course);

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
        }catch (Exception ex){
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test @InSequence(59)
    public void createCourse_CourseReferenceType_Required(){
        String TEST_CASE_NAME = "createCourse_CourseReferenceType_Required";
        try {
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
            LoginData tutorLoginData = createTutorUser();

            CourseData courseData = createValidCourseData().clone();
            courseData.getReferences().get(0).setType(null);

            FormValidation expected = new FormValidation(NEW_COURSE_VAL, REQUIRED_COURSE_REFERENCE_TYPE);
            Util.postSecuredFormValidation(Rest.COURSE.RESOURCE, Rest.COURSE.NEW, tutorLoginData, courseData, expected);

            Course course = dbManager.find(session, Course.class, "Cor0000001PrBe" + new SimpleDateFormat("yyyyMMdd").format(new Date()), false);
            Assert.assertNull("Course is inserted in Database", course);

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
        }catch (Exception ex){
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test @InSequence(60)
    public void createCourse_CourseReferenceURL_InvalidValue(){
        String TEST_CASE_NAME = "createCourse_CourseReferenceURL_InvalidValue";
        try {
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
            LoginData tutorLoginData = createTutorUser();

            CourseData courseData = createValidCourseData().clone();
            courseData.getReferences().get(0).setUrl("Ahmed");

            FormValidation expected = new FormValidation(NEW_COURSE_VAL, INVALID_COURSE_REFERENCE_URL);
            Util.postSecuredFormValidation(Rest.COURSE.RESOURCE, Rest.COURSE.NEW, tutorLoginData, courseData, expected);

            Course course = dbManager.find(session, Course.class, "Cor0000001PrBe" + new SimpleDateFormat("yyyyMMdd").format(new Date()), false);
            Assert.assertNull("Course is inserted in Database", course);

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
        }catch (Exception ex){
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test @InSequence(61)
    public void createCourse_CourseReferenceURL_EmptyString(){
        String TEST_CASE_NAME = "createCourse_CourseReferenceURL_EmptyString";
        try {
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
            LoginData tutorLoginData = createTutorUser();

            CourseData courseData = createValidCourseData().clone();
            courseData.getReferences().get(0).setUrl("");

            FormValidation expected = new FormValidation(NEW_COURSE_VAL, EMPTY_STR_COURSE_REFERENCE_URL);
            Util.postSecuredFormValidation(Rest.COURSE.RESOURCE, Rest.COURSE.NEW, tutorLoginData, courseData, expected);

            Course course = dbManager.find(session, Course.class, "Cor0000001PrBe" + new SimpleDateFormat("yyyyMMdd").format(new Date()), false);
            Assert.assertNull("Course is inserted in Database", course);

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
        }catch (Exception ex){
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test @InSequence(62)
    public void createCourse_CourseReferenceURL_MinLength(){
        String TEST_CASE_NAME = "createCourse_CourseReferenceURL_MinLength";
        try {
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
            LoginData tutorLoginData = createTutorUser();

            CourseData courseData = createValidCourseData().clone();
            courseData.getReferences().get(0).setUrl("http");

            FormValidation expected = new FormValidation(NEW_COURSE_VAL, LENGTH_COURSE_REFERENCE_URL);
            Util.postSecuredFormValidation(Rest.COURSE.RESOURCE, Rest.COURSE.NEW, tutorLoginData, courseData, expected);

            Course course = dbManager.find(session, Course.class, "Cor0000001PrBe" + new SimpleDateFormat("yyyyMMdd").format(new Date()), false);
            Assert.assertNull("Course is inserted in Database", course);

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
        }catch (Exception ex){
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test @InSequence(63)
    public void createCourse_CourseReferenceURL_MaxLength(){
        String TEST_CASE_NAME = "createCourse_CourseReferenceURL_MaxLength";
        try {
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
            LoginData tutorLoginData = createTutorUser();

            CourseData courseData = createValidCourseData().clone();
            courseData.getReferences().get(0).setUrl("https://www.youtube.com/watch?v=VVV0Pa4PkP0" + Util.generateString(210));

            FormValidation expected = new FormValidation(NEW_COURSE_VAL, LENGTH_COURSE_REFERENCE_URL);
            Util.postSecuredFormValidation(Rest.COURSE.RESOURCE, Rest.COURSE.NEW, tutorLoginData, courseData, expected);

            Course course = dbManager.find(session, Course.class, "Cor0000001PrBe" + new SimpleDateFormat("yyyyMMdd").format(new Date()), false);
            Assert.assertNull("Course is inserted in Database", course);

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
        }catch (Exception ex){
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test @InSequence(64)
    public void createCourse_CourseReferenceURL_Required(){
        String TEST_CASE_NAME = "createCourse_CourseName_Required";
        try {
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
            LoginData tutorLoginData = createTutorUser();

            CourseData courseData = createValidCourseData().clone();
            courseData.getReferences().get(0).setUrl(null);

            FormValidation expected = new FormValidation(NEW_COURSE_VAL, REQUIRED_COURSE_REFERENCE_URL);
            Util.postSecuredFormValidation(Rest.COURSE.RESOURCE, Rest.COURSE.NEW, tutorLoginData, courseData, expected);

            Course course = dbManager.find(session, Course.class, "Cor0000001PrBe" + new SimpleDateFormat("yyyyMMdd").format(new Date()), false);
            Assert.assertNull("Course is inserted in Database", course);

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
        }catch (Exception ex){
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test @InSequence(65)
    public void createCourse_CourseLevel_NotFound(){
        String TEST_CASE_NAME = "createCourse_CourseLevel_NotFound";
        try {
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
            LoginData tutorLoginData = createTutorUser();

            repository.executeScript(Scripts.CONTENT_STATUS_LOOKUP);
            repository.executeScript(Scripts.COURSE_TYPE_LOOKUP);
            repository.executeScript(Scripts.COURSE_LEVEL_LOOKUP);
            repository.executeScript(Scripts.MATERIAL_TYPE_LOOKUP);
            repository.executeScript(Scripts.DATA_TYPE_LOOKUP);
            repository.executeScript(Scripts.SYSTEM_PARAMETER_LOOKUP);

            CourseData courseData = createValidCourseData().clone();
            courseData.setCourseLevel("Am");

            String expectedStrError = MessageFormat.format(LOOKUP_NOT_FOUND, CourseLevel.class.getSimpleName(), "Am");
            Util.postSecuredStringError(Rest.COURSE.RESOURCE, Rest.COURSE.NEW, tutorLoginData, courseData, expectedStrError);

            Course course = dbManager.find(session, Course.class, "Cor0000001PrBe" + new SimpleDateFormat("yyyyMMdd").format(new Date()), false);
            Assert.assertNull("Course is inserted in Database", course);

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
        }catch (Exception ex){
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test @InSequence(66)
    public void createCourse_CourseType_NotFound(){
        String TEST_CASE_NAME = "createCourse_CourseType_NotFound";
        try {
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
            LoginData tutorLoginData = createTutorUser();

            repository.executeScript(Scripts.CONTENT_STATUS_LOOKUP);
            repository.executeScript(Scripts.COURSE_TYPE_LOOKUP);
            repository.executeScript(Scripts.COURSE_LEVEL_LOOKUP);
            repository.executeScript(Scripts.MATERIAL_TYPE_LOOKUP);
            repository.executeScript(Scripts.DATA_TYPE_LOOKUP);
            repository.executeScript(Scripts.SYSTEM_PARAMETER_LOOKUP);

            CourseData courseData = createValidCourseData().clone();
            courseData.setCourseType("Am");

            String expectedStrError = MessageFormat.format(LOOKUP_NOT_FOUND, CourseType.class.getSimpleName(), "Am");
            Util.postSecuredStringError(Rest.COURSE.RESOURCE, Rest.COURSE.NEW, tutorLoginData, courseData, expectedStrError);

            Course course = dbManager.find(session, Course.class, "Cor0000001PrBe" + new SimpleDateFormat("yyyyMMdd").format(new Date()), false);
            Assert.assertNull("Course is inserted in Database", course);

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
        }catch (Exception ex){
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test @InSequence(67)
    public void createCourse_ReferenceType_NotFound(){
        String TEST_CASE_NAME = "createCourse_ReferenceType_NotFound";
        try {
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
            LoginData tutorLoginData = createTutorUser();

            repository.executeScript(Scripts.CONTENT_STATUS_LOOKUP);
            repository.executeScript(Scripts.COURSE_TYPE_LOOKUP);
            repository.executeScript(Scripts.COURSE_LEVEL_LOOKUP);
            repository.executeScript(Scripts.MATERIAL_TYPE_LOOKUP);
            repository.executeScript(Scripts.DATA_TYPE_LOOKUP);
            repository.executeScript(Scripts.SYSTEM_PARAMETER_LOOKUP);

            CourseData courseData = createValidCourseData().clone();
            courseData.getReferences().get(0).setType("Am");

            String expectedStrError = MessageFormat.format(LOOKUP_NOT_FOUND, MaterialType.class.getSimpleName(), "Am");
            Util.postSecuredStringError(Rest.COURSE.RESOURCE, Rest.COURSE.NEW, tutorLoginData, courseData, expectedStrError);

            Course course = dbManager.find(session, Course.class, "Cor0000001PrBe" + new SimpleDateFormat("yyyyMMdd").format(new Date()), false);
            Assert.assertNull("Course is inserted in Database", course);

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
        }catch (Exception ex){
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test @InSequence(68)
    public void createCourse_PreRequisiteType_NotFound(){
        String TEST_CASE_NAME = "createCourse_PreRequisiteType_NotFound";
        try {
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
            LoginData tutorLoginData = createTutorUser();

            repository.executeScript(Scripts.CONTENT_STATUS_LOOKUP);
            repository.executeScript(Scripts.COURSE_TYPE_LOOKUP);
            repository.executeScript(Scripts.COURSE_LEVEL_LOOKUP);
            repository.executeScript(Scripts.MATERIAL_TYPE_LOOKUP);
            repository.executeScript(Scripts.DATA_TYPE_LOOKUP);
            repository.executeScript(Scripts.SYSTEM_PARAMETER_LOOKUP);

            CourseData courseData = createValidCourseData().clone();
            courseData.getPreRequisites().get(0).setType("Am");

            String expectedStrError = MessageFormat.format(LOOKUP_NOT_FOUND, MaterialType.class.getSimpleName(), "Am");
            Util.postSecuredStringError(Rest.COURSE.RESOURCE, Rest.COURSE.NEW, tutorLoginData, courseData, expectedStrError);

            Course course = dbManager.find(session, Course.class, "Cor0000001PrBe" + new SimpleDateFormat("yyyyMMdd").format(new Date()), false);
            Assert.assertNull("Course is inserted in Database", course);

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
        }catch (Exception ex){
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }
}
