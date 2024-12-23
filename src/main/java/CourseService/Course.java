package CourseService;

public class Course {
    private String courseId;
    private String name;
    private String description;
    private int credits;
    private String type;
    private String department;
    private String prerequisites;
    private String language;

    public Course(String courseId, String name, String description, int credits, String type, String department, String prerequisites, String language) {
        this.courseId = courseId;
        this.name = name;
        this.description = description;
        this.credits = credits;
        this.type = type;
        this.department = department;
        this.prerequisites = prerequisites;
        this.language = language;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getCredits() {
        return credits;
    }

    public void setCredits(int credits) {
        this.credits = credits;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getPrerequisites() {
        return prerequisites;
    }

    public void setPrerequisites(String prerequisites) {
        this.prerequisites = prerequisites;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }
}