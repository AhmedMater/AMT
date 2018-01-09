package amt;

import am.main.common.ConfigParam;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.naming.InitialContext;

/**
 * Created by ahmed.motair on 1/6/2018.
 */
@Singleton
@Startup
public class AMTInitializer {
    @PostConstruct
    public void load() {
        try {
            InitialContext ctx = new InitialContext();
            ConfigParam.APP_CONFIG_PATH = (String) ctx.lookup("AMTConfigPath");

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
}
