//package am.infrastructure.generic;
//
//import am.session.AppSession;
//import am.api.logger.AppLogger;
//import am.api.error.EC;
//import am.exception.DBException;
//import am.api.info.IC;
//
//import javax.annotation.PostConstruct;
//import javax.enterprise.context.ApplicationScoped;
//import javax.persistence.*;
//import javax.persistence.TransactionRequiredException;
//import javax.transaction.*;
//import javax.validation.ConstraintViolation;
//import javax.validation.ConstraintViolationException;
//import java.io.Serializable;
//import java.api.ArrayList;
//import java.api.List;
//import java.api.Set;
//
//
///**
// * Created by ahmed.motair on 9/7/2017.
// */
//@ApplicationScoped
//public class GenericRepository implements Serializable {
//    private final static String CLASS = "GenericRepository";
//    private final static String DB_INSERT = "Insert";
//    private final static String DB_UPDATE = "Update";
//    private final static String DB_DELETE = "Delete";
//    private EntityManager em;
//
//    @PersistenceUnit(unitName = "amt")
//    private EntityManagerFactory emf;
//
//    public EntityManager getEm() {
//        return emf.createEntityManager();
//    }
//
//    @PostConstruct
//    private void setEntityManager() {
//        em = emf.createEntityManager(SynchronizationType.SYNCHRONIZED);
//    }
//
//    public EntityManagerFactory getEmf()
//    {
//        return emf;
//    }
//
//    @Transactional
//    public <T> T persist(AppSession session, T toBeInserted) throws Exception {
//        String FN_NAME = "persist";
//        try {
//            AppLogger.startDebugLog(session, CLASS, FN_NAME, toBeInserted);
//            try {
//                em.persist(toBeInserted);
//                em.flush();
//                AppLogger.info(session, CLASS, FN_NAME, IC.DB_0001, toBeInserted.getClass().getSimpleName());
//            } catch (EntityExistsException ex) {
//                throw new DBException(session, ex, EC.DB_0001, toBeInserted);
//            } catch (IllegalArgumentException ex) {
//                throw new DBException(session, ex, EC.DB_0002, toBeInserted.getClass().getSimpleName());
//            } catch (TransactionRequiredException ex) {
//                throw new DBException(session, ex, EC.DB_0003);
//            } catch (PersistenceException ex) {
//                throw new DBException(session, ex, EC.DB_0004, DB_INSERT);
//            } catch (ConstraintViolationException ex) {
//                Set<ConstraintViolation<?>> violations = ex.getConstraintViolations();
//
//                List<String> errors = new ArrayList<>();
//                for (ConstraintViolation<?> violation : violations)
//                    errors.add(violation.getMessageTemplate());
//
//                throw new DBException(session, ex, EC.DB_0005, errors.toString(), toBeInserted);
//            }
//
//            AppLogger.endDebugLog(session, CLASS, FN_NAME, toBeInserted);
//            return toBeInserted;
//        }catch (Exception ex){
//            AppLogger.info(session, CLASS, FN_NAME, IC.DB_0002, toBeInserted);
//            throw ex;
//        }
//    }
//
//    public <T> T find(AppSession session, Class<T> className, Object identifier)throws DBException {
//        String FN_NAME = "find";
//        try {
//            AppLogger.startDebugLog(session, CLASS, FN_NAME, className, identifier);
//
//            T foundObject;
//            try {
//                foundObject = em.find(className, identifier);
//                AppLogger.info(session, CLASS, FN_NAME, IC.DB_0003, className, identifier);
//            } catch (IllegalArgumentException ex) {
//                throw new DBException(session, ex, EC.DB_0006, className, identifier, className);
//            }
//
//            AppLogger.endDebugLog(session, CLASS, FN_NAME, foundObject);
//            return foundObject;
//        }catch (Exception ex){
//            AppLogger.info(session, CLASS, FN_NAME, IC.DB_0004, className, identifier);
//            throw ex;
//        }
//    }
//
//    @Transactional
//    public <T> T merge(AppSession session, T toBeUpdated)throws DBException{
//        String FN_NAME = "merge";
//        try {
//            AppLogger.startDebugLog(session, CLASS, FN_NAME, toBeUpdated);
//
//            try {
//                toBeUpdated = em.merge(toBeUpdated);
//                em.flush();
//                AppLogger.info(session, CLASS, FN_NAME, IC.DB_0007, toBeUpdated);
//            }catch (IllegalArgumentException ex){
//                throw new DBException(session, ex, EC.DB_0007);
//            }catch (TransactionRequiredException ex){
//                throw new DBException(session, ex, EC.DB_0003);
//            } catch (PersistenceException ex) {
//                throw new DBException(session, ex, EC.DB_0004, DB_UPDATE);
//            }
//
//            AppLogger.endDebugLog(session, CLASS, FN_NAME, toBeUpdated);
//            return toBeUpdated;
//        }catch (Exception ex){
//            AppLogger.info(session, CLASS, FN_NAME, IC.DB_0006, toBeUpdated);
//            throw ex;
//        }
//    }
//
//    @Transactional
//    public void remove(AppSession session, Object toBeRemoved)throws DBException {
//        String FN_NAME = "remove";
//        try {
//            AppLogger.startDebugLog(session, CLASS, FN_NAME, toBeRemoved);
//
//            try {
//                if (!em.contains(toBeRemoved))
//                    throw new DBException(session, EC.DB_0002);
//
//                em.remove(toBeRemoved);
//                em.flush();
//                AppLogger.info(session, CLASS, FN_NAME, IC.DB_0007, toBeRemoved);
//            } catch (IllegalArgumentException ex) {
//                throw new DBException(session, ex, EC.DB_0007);
//            } catch (TransactionRequiredException ex) {
//                throw new DBException(session, ex, EC.DB_0003);
//            } catch (PersistenceException ex) {
//                throw new DBException(session, ex, EC.DB_0004, DB_DELETE);
//            }
//
//            AppLogger.endDebugLog(session, CLASS, FN_NAME);
//        }catch (Exception ex){
//            AppLogger.info(session, CLASS, FN_NAME, IC.DB_0008, toBeRemoved);
//        }
//    }
//
//}
