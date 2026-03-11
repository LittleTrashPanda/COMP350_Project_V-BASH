package edu.gcc.VBASH;

public class Course {
    // Course Attributes
    private String courseName;
    private String department;
    private String courseCode;
    private String description;
    private String[] professors;
    private int credits;
    private int days;
    private int[] startTimes;
    private int[] duration;
    private String semester;

    // ----------------------------------------------------------------------------------------------------

    public Course() {}

    // Constructor
    public Course(
            String courseName,
            String department,
            String courseCode,
            String description,
            String[] professors,
            int credits,
            int days,
            int[] startTimes,
            int[] duration,
            String semester
    ) {
        this.courseName = courseName;
        this.department = department;
        this.courseCode = courseCode;
        this.description = description;
        this.professors = professors;
        this.credits = credits;
        this.days = days;
        this.startTimes = startTimes;
        this.duration = duration;
        this.semester = semester;
    }

    // ----------------------------------------------------------------------------------------------------

    // Get Attributes
    public String getCourseName() { return courseName; }
    public String getDepartment() { return department; }
    public String getCourseCode() { return courseCode; }
    public String getDescription() { return description; }
    public String[] getProfessors() { return professors; }
    public int getCredits() { return credits; }
    public int getDays() { return days; }
    public int[] getStartTimes() { return startTimes; }
    public int[] getDuration() { return duration; }
    public String getSemester() { return semester; }

    // ----------------------------------------------------------------------------------------------------
    public void setCourseName(String courseName) { this.courseName = courseName; }
    public void setDepartment(String department) { this.department = department; }
    public void setCourseCode(String courseCode) { this.courseCode = courseCode; }
    public void setDescription(String description) { this.description = description; }
    public void setProfessors(String[] professors) { this.professors = professors; }
    public void setCredits(int credits) { this.credits = credits; }
    public void setDays(int days) { this.days = days; }
    public void setStartTimes(int[] startTimes) { this.startTimes = startTimes; }
    public void setDuration(int[] duration) { this.duration = duration; }
    public void setSemester(String semester) { this.semester = semester; }
    // Testing Overlap
    public boolean willConflict(Course potentialCourse) { return true; }
}
