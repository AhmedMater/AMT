package am.infrastructure.data.enums;

/**
 * Created by ahmed.motair on 11/30/2017.
 */
public enum ContentStatus {
    DONE("Do", "Done"),
    IN_PROGRESS("Pr", "In Progress"),
    FUTURE("Fu", "Future");

    private String status;
    private String description;

    ContentStatus(String status, String description){
        this.status = status;
        this.description = description;
    }

    public String status() {
        return status;
    }
    public String description() {
        return description;
    }

}
