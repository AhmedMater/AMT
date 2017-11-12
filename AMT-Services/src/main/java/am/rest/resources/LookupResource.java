package am.rest.resources;

import am.api.components.AppLogger;
import am.api.components.ErrorHandler;
import am.api.components.InfoHandler;
import am.infrastructure.data.enums.Roles;
import am.infrastructure.data.hibernate.model.lookup.CourseLevel;
import am.infrastructure.data.hibernate.model.lookup.CourseType;
import am.infrastructure.data.hibernate.model.lookup.DataType;
import am.infrastructure.data.hibernate.model.lookup.MaterialType;
import am.session.AppSession;
import am.session.Interface;
import am.session.Phase;
import am.session.Source;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * Created by ahmed.motair on 11/5/2017.
 */
@Path("/lookup")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class LookupResource {
    private static final String CLASS = "LookupResource";

    @Inject private ErrorHandler errorHandler;
    @Inject private InfoHandler infoHandler;
    @Inject private AppLogger logger;
    @Inject private am.api.components.db.DBManager DBManager;

    @Path("/courseTypes")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllCourseTypes() throws Exception {
        String FN_NAME = "getAllCourseTypes";
        AppSession session = new AppSession(Source.APP_SERVICES, Interface.REST, Phase.REGISTRATION, CLASS, FN_NAME, errorHandler, infoHandler);
        try{
            List<CourseType> results = DBManager.findAll(session, CourseType.class, true);
            return Response.ok().entity(results).build();
        }catch (Exception ex){
            throw ex;
//            Exception exc = ConfigUtils.businessException(logger, session, ex, EC.AMT_0013, userRegisterData.fullName());
//            throw exc;
        }
    }

    @Path("/courseLevels")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllCourseLevels() throws Exception {
        String FN_NAME = "getAllCourseLevels";
        AppSession session = new AppSession(Source.APP_SERVICES, Interface.REST, Phase.REGISTRATION, CLASS, FN_NAME, errorHandler, infoHandler);
        try{
            List<CourseLevel> results = DBManager.findAll(session, CourseLevel.class, true);
            return Response.ok().entity(results).build();
        }catch (Exception ex){
            throw ex;
//            Exception exc = ConfigUtils.businessException(logger, session, ex, EC.AMT_0013, userRegisterData.fullName());
//            throw exc;
        }
    }

    @Path("/materialTypes")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllMaterialTypes() throws Exception {
        String FN_NAME = "getAllMaterialTypes";
        AppSession session = new AppSession(Source.APP_SERVICES, Interface.REST, Phase.REGISTRATION, CLASS, FN_NAME, errorHandler, infoHandler);
        try{
            List<MaterialType> results = DBManager.findAll(session, MaterialType.class, true);
            return Response.ok().entity(results).build();
        }catch (Exception ex){
            throw ex;
//            Exception exc = ConfigUtils.businessException(logger, session, ex, EC.AMT_0013, userRegisterData.fullName());
//            throw exc;
        }
    }

    @Path("/userRoles")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllUserRoles() throws Exception {
        String FN_NAME = "getAllUserRoles";
        AppSession session = new AppSession(Source.APP_SERVICES, Interface.REST, Phase.REGISTRATION, CLASS, FN_NAME, errorHandler, infoHandler);
        try{
            List<Roles> results = DBManager.findAll(session, Roles.class, true);
            return Response.ok().entity(results).build();
        }catch (Exception ex){
            throw ex;
//            Exception exc = ConfigUtils.businessException(logger, session, ex, EC.AMT_0013, userRegisterData.fullName());
//            throw exc;
        }
    }

    @Path("/dataTypes")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllDataTypes() throws Exception {
        String FN_NAME = "getAllDataTypes";
        AppSession session = new AppSession(Source.APP_SERVICES, Interface.REST, Phase.REGISTRATION, CLASS, FN_NAME, errorHandler, infoHandler);
        try{
            List<DataType> results = DBManager.findAll(session, DataType.class, true);
            return Response.ok().entity(results).build();
        }catch (Exception ex){
            throw ex;
//            Exception exc = ConfigUtils.businessException(logger, session, ex, EC.AMT_0013, userRegisterData.fullName());
//            throw exc;
        }
    }
}
