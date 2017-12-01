package amt.common.enums;

import amt.common.generic.Util;

import java.util.ArrayList;
import java.util.List;

import static amt.common.constants.ScriptPaths.*;

/**
 * Created by ahmed.motair on 11/23/2017.
 */
public enum Scripts {
    /**
     * <p>Clearing all The Database Tables</p>
     */
    CLEARING_ALL_TABLES(CLEARING_PATH + "/clearing_all_tables.sql"),
    /**
     * <p>Clearing the Tables associated with User</p>
     * <blockquote>
     * 1. User IP De-active Table <br>
     * 2. User IP Failure Table <br>
     * 3. User Login Log Table <br>
     * 4. User Table <br>
     * </blockquote>
     */
    CLEARING_USER_TABLE(CLEARING_PATH + "/clearing_user_table.sql"),

    ROLE_LOOKUP(LOOKUP_PATH + "/role.sql"),
    COURSE_TYPE_LOOKUP(LOOKUP_PATH + "/course_type.sql"),
    COURSE_LEVEL_LOOKUP(LOOKUP_PATH + "/course_level.sql"),
    MATERIAL_TYPE_LOOKUP(LOOKUP_PATH + "/material_type.sql"),
    CONTENT_STATUS_LOOKUP(LOOKUP_PATH + "/content_status.sql"),
    DATA_TYPE_LOOKUP(LOOKUP_PATH + "/data_type.sql"),
    SYSTEM_PARAMETER_LOOKUP(LOOKUP_PATH + "/system_parameter.sql"),
    ADMIN_USER_LOOKUP(LOOKUP_PATH + "/admin_user.sql")
    ;

    private String path;

    Scripts(String path){
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    public boolean equals(Scripts other) {
        return this.path.equals(other.path);
    }

    public String readScript() throws Exception {
        return Util.readResourceFile(this.path);
    }

    public static List getAllScripts() {
        List<String> scriptPaths = new ArrayList<>();

        Scripts[] scripts = Scripts.values();
        for (Scripts script : scripts) {
            scriptPaths.add(script.path);
        }
        return scriptPaths;
    }
}
