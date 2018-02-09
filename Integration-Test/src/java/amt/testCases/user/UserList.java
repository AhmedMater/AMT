package amt.testCases.user;

import am.infrastructure.data.dto.filters.UserListFilter;
import am.infrastructure.data.dto.user.LoginData;
import am.infrastructure.data.view.resultset.UserListRS;
import am.infrastructure.data.view.ui.UserListUI;
import am.main.api.db.DBManager;
import am.main.data.dto.SortingInfo;
import am.main.session.AppSession;
import amt.common.DeploymentManger;
import amt.common.constants.Params;
import amt.common.constants.Rest;
import amt.common.enums.ITSource;
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
import static amt.common.enums.ITPhase.IT;

/**
 * Created by ahmed.motair on 1/5/2018.
 */
@RunWith(Arquillian.class)
public class UserList {
    @Inject private Repository repository;
    @Inject private DBManager dbManager;
    @Inject private DataGenerator dataGenerator;

    private static final String CLASS = "UserList";

    private AppSession appSession = new AppSession(ITSource.INTEGRATION_TEST, ARQUILLIAN, IT);

    @Deployment
    public static WebArchive createDeployment() {
        return DeploymentManger.AMTServicesWAR(Scripts.getAllScripts());
    }

    private void compareLists(List<Integer> expectedListIDs, List<UserListUI> actual){
        int counter = 0;
        for (UserListUI item : actual)
            if(!item.getUserID().equals(expectedListIDs.get(counter++)))
                Assert.fail("Expected User ID: " + expectedListIDs.get(--counter) + ", But Actual User ID: " + item.getUserID());
    }

    @Test
    @InSequence(1)
    public void preparingDatabaseData() throws Exception{
        try {
            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
            repository.executeScript(Scripts.ROLE_LOOKUP);

            repository.executeScript(Scripts.LIST_OF_39_COURSES);
            repository.executeScript(Scripts.STUDENT_USER_LOOKUP);
        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }


    @Test @InSequence(2)
    public void getAllUsers_NoFilters_DefaultSorting_Paging_1() throws Exception {
        try {
            UserListFilter filters = new UserListFilter();
            filters.setPageNum(0);
            filters.setSorting(new SortingInfo("Asc", "userName"));

            LoginData loginData = Params.studentLoginData;

            Response response = RestUtil.postSecured(Rest.COURSE.RESOURCE, Rest.COURSE.LIST, filters, loginData);
            Assert.assertEquals("Response Status is wrong", Response.Status.OK.getStatusCode(), response.getStatus());

            UserListRS resultSet = response.readEntity(UserListRS.class);
            Assert.assertEquals("Total Users is wrong", 0, resultSet.getPagination().getTotal());

            List<Integer> expectedListIDs = new ArrayList<>();
//            expectedListIDs.add();
//            expectedListIDs.add();
//            expectedListIDs.add();
//            expectedListIDs.add();
//            expectedListIDs.add();
//            expectedListIDs.add();
//            expectedListIDs.add();
//            expectedListIDs.add();
//            expectedListIDs.add();
//            expectedListIDs.add();

            compareLists(expectedListIDs, resultSet.getData());
        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }


}
