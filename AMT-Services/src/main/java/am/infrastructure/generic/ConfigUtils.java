package am.infrastructure.generic;

//import am.exception.GeneralException;


/**
 * Created by ahmed.motair on 9/7/2017.
 */
public class ConfigUtils {
    private static final String CLASS = "ConfigUtils";
//
//    /**
//     * Reads Resource File into Properties Object
//     * @param file Resource File Name
//     * @return Properties Object
//     */
//    public static Properties readResourceFiles(AppSession session, String file) throws Exception {
//        String FN_NAME = "readResourceFiles";
//        try {
//            Properties properties = new Properties();
//            AppLogger.startDebugLog(session, CLASS, FN_NAME, file);
//
//            AppLogger.info(session, CLASS, FN_NAME, SystemMsg.INFO_0003, file);
//
//            if(file == null || file.isEmpty())
//                throw new GeneralException(MessageFormat.format(SystemMsg.ERROR_0010, file));
//
//            try {
//                InputStream input = ConfigUtils.class.getClassLoader().getResourceAsStream(file);
//                properties.load(input);
//                input.close();
//            } catch (FileNotFoundException e) {
//                throw new GeneralException(e, MessageFormat.format(SystemMsg.ERROR_0011, file));
//            } catch (IOException e) {
//                throw new GeneralException(MessageFormat.format(SystemMsg.ERROR_0012, file, e.getMessage()));
//            }
//
//            AppLogger.info(session, CLASS, FN_NAME, SystemMsg.INFO_0004, file);
//
//            AppLogger.endDebugLog(session, CLASS, FN_NAME, properties);
//            return properties;
//        } catch (Exception ex){
//            throw new GeneralException(MessageFormat.format(SystemMsg.ERROR_0013, file, ex.getMessage()));
//        }
//    }
//
//    /**
//     * Replaces the Placeholders in a Message with values
//     * @param session AppSession
//     * @param message String message that has placeholders or no
//     * @param args values to be replaced with the placeholders in the message
//     * @return String message formatted
//     * @throws GeneralException - IO_0007 - If the message needs arguments for placeholders that aren't provided
//     * @throws GeneralException - IO_0008 - If the message doesn't have placeholders for the arguments provided
//     * @throws GeneralException - IO_0009 - If the message has placeholders less than arguments provided
//     * @throws GeneralException - IO_0010 - If the message has placeholders more than arguments provided
//     */
//    public static String formatMsg(AppSession session, String message, Object ... args) throws Exception{
//        String FN_NAME = "formatMsg";
//        try {
//            AppLogger.startDebugLog(session, CLASS, FN_NAME, message, args);
//            AppLogger.info(session, CLASS, FN_NAME, SystemMsg.INFO_0011, message);
//
//            Matcher matcher = Pattern.compile("\\{[0-9]+\\}").matcher(message);
//            String _message = "";
//
//            int counter = 0;
//            while (matcher.find())
//                counter++;
//
//            if (counter == 0) {
//                if (args == null || args.length == 0)
//                    _message = message;
//                else
//                    throw new GeneralException(SystemMsg.ERROR_0032);
//            } else {
//                if (args == null || args.length == 0)
//                    throw new GeneralException(SystemMsg.ERROR_0033);
//                else if (args.length == counter)
//                    _message = MessageFormat.format(message, args);
//                else if (args.length > counter)
//                    throw new GeneralException(SystemMsg.ERROR_0034);
//                else if (args.length < counter)
//                    throw new GeneralException(SystemMsg.ERROR_0035);
//            }
//
//            AppLogger.info(session, CLASS, FN_NAME, SystemMsg.INFO_0012, message);
//            AppLogger.endDebugLog(session, CLASS, FN_NAME, _message);
//            return _message;
//        }catch (Exception ex){
//            throw new GeneralException(ex, MessageFormat.format(SystemMsg.ERROR_0017, message, ex.getMessage()));
//        }
//    }
//
//    /**
//     * Reads value from Property File
//     * @param session AppSession
//     * @param propertyFile Properties Object
//     * @param property String property
//     * @return String value of the key
//     * @throws GeneralException - ERROR_0030 - If the Property file isn't loaded or Empty
//     * @throws GeneralException - ERROR_0031 - If the Property isn't found in the file
//     */
//    public static String readValueFromPropertyFile(AppSession session, Properties propertyFile, String property, String fileName) throws Exception{
//        String FN_NAME = "readValueFromPropertyFile";
//        try {
//            AppLogger.startDebugLog(session, CLASS, FN_NAME, property, fileName);
//            AppLogger.info(session, CLASS, FN_NAME, SystemMsg.INFO_0009, property, fileName);
//
//            String value = "";
//
//            if (propertyFile == null || propertyFile.isEmpty())
//                throw new GeneralException(MessageFormat.format(SystemMsg.ERROR_0030, fileName));
//            else if (!propertyFile.containsKey(property))
//                throw new GeneralException(MessageFormat.format(SystemMsg.ERROR_0031, property, fileName));
//            else
//                value = propertyFile.getProperty(property);
//
//            AppLogger.info(session, CLASS, FN_NAME, SystemMsg.INFO_0010, property, fileName);
//            AppLogger.endDebugLog(session, CLASS, FN_NAME, value);
//            return value;
//        } catch (Exception ex){
//            throw new GeneralException(ex, MessageFormat.format(SystemMsg.ERROR_0016, property, fileName, ex.getMessage()));
//        }
//    }

}
