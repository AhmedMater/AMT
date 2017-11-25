package am.infrastructure.data.enums;

/**
 * Created by mohamed.elewa on 5/18/2016.
 */
public enum Roles {
    ADMIN("Ad", "Admin"),
    STUDENT("St", "Student"),
    TUTOR("Tu", "Tutor");

    private String role;
    private String description;

    Roles(String role, String description){
        this.role = role;
        this.description = description;
    }
    public String description(){return description;}
    public String role(){return role;}

    @Override public String toString() { return description; }
}
