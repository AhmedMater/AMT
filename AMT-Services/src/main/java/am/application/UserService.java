package am.application;

import am.api.components.AppLogger;
import am.api.components.DBManager;
import am.api.components.Validator;
import am.api.enums.EC;
import am.api.enums.IC;
import am.exception.BusinessException;
import am.infrastructure.data.enums.Roles;
import am.infrastructure.data.hibernate.model.user.Role;
import am.infrastructure.data.hibernate.model.user.Users;
import am.repository.UserRepository;
import am.session.AppSession;

import javax.inject.Inject;
import java.util.Date;
import java.util.List;

/**
 * Created by ahmed.motair on 9/23/2017.
 */
public class UserService {
    private static final String CLASS = "UserService";

    @Inject private DBManager DBManager;
    @Inject private AppLogger logger;
    @Inject private UserRepository userRepository;
    @Inject private Validator validator;

    public void register(AppSession appSession, String firstName, String lastName, String username, String password, String email) throws Exception{
        String FN_NAME = "register";
        AppSession session = appSession.updateSession(CLASS, FN_NAME);
        logger.startDebug(session, firstName, lastName, username, (password == null) ? "Null" : "Password", email);

        if(userRepository.checkUsernameInDatabase(session, username))
            throw new BusinessException(session, EC.AMT_0001, username);
        else
            logger.info(session, IC.AMT_0004, username);

        if(userRepository.checkEmailInDatabase(session, email))
            throw new BusinessException(session, EC.AMT_0002, email);
        else
            logger.info(session, IC.AMT_0005, email);

        List<String> errorList = validator.validateForm(session);
        if(errorList.size() > 0)
            throw new BusinessException(session, EC.AMT_0003, errorList.toArray());
        else
            logger.info(session, IC.AMT_0006);

        //TODO: Insert the Username, Password and Email in the Database
        Users user = new Users();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setUsername(username);
        user.setPassword(password);
        user.setEmail(email);
        user.setRole(new Role(Roles.STUDENT));
        user.setActive(true);
        user.setCreationDate(new Date());

        DBManager.persist(session, user, true);
        logger.info(session, IC.AMT_0007, user.getFullName());

        logger.endDebug(session);
    }

}
