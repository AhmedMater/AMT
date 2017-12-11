package amt.model;

import amt.generic.Gen;

import java.text.MessageFormat;

/**
 * Created by ahmed.motair on 12/11/2017.
 */
public class CourseRef {
    private Integer courseRefID;
    private String courseID;
    private Integer num;
    private String name;
    private String type;
    private String url;

    public CourseRef() {
    }
    public CourseRef(Integer num, String name, String type, String url) {
        this.num = num;
        this.name = name;
        this.type = type;
        this.url = url;
    }

    public Integer getCourseRefID() {
        return courseRefID;
    }
    public void setCourseRefID(Integer courseRefID) {
        this.courseRefID = courseRefID;
    }

    public String getCourseID() {
        return courseID;
    }
    public void setCourseID(String courseID) {
        this.courseID = courseID;
    }

    public Integer getNum() {
        return num;
    }
    public void setNum(Integer num) {
        this.num = num;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        if(name.length()>100)
            this.name = name.substring(0,100);
        else
            this.name = name;
    }

    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }

    public String generateSQL()throws Exception{
        String INSERT_STATEMENT = "INSERT INTO course_reference (reference_id, course_id, " +
                "reference_num, reference_name, reference_type, reference_url) " +
                "VALUES (''{0}'', ''{1}'', ''{2}'', ''{3}'', ''{4}'', ''{5}'');";

        return MessageFormat.format(INSERT_STATEMENT, courseRefID.toString(), courseID,
               num.toString(), Gen.escapeSingleQuote(name), type, Gen.escapeSingleQuote(url));
    }
}
