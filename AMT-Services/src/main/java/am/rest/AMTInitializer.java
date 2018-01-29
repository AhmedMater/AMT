package am.rest;

import am.main.common.ConfigParam;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.naming.InitialContext;

import static am.infrastructure.data.enums.impl.AMTSource.AMT_SERVICES;

/**
 * Created by ahmed.motair on 1/22/2018.
 */
@Singleton
@Startup
public class AMTInitializer {
    @PostConstruct
    public void load() {
        String FN_NAME = "load";
        try {

            InitialContext ctx = new InitialContext();
            ConfigParam.setConfiguration((String) ctx.lookup("AMTConfigPath"), AMT_SERVICES);
        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }
}
