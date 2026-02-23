package edu.gcc.VBASH;

public class Course {
    private String department;
    private String courseCode;
    private String description;
    private String professor;
    private int credits;
    private int[] days;
    private int[] startTimes;
    private int[] duration;
    private String semester;

    public Course(String department, String courseCode, String description, String professor, int credits,
                  int[] days, int[] startTimes, int[] duration, String semester) { }

    public String getDepartment() { return null; }
    public String getCourseCode() { return null; }
    public String getDescription() { return null; }
    public String getProfessor() { return null; }
    public int getCredits() { return 0; }
    public int[] getDays() { return null; }
    public int[] getStartTimes() { return null; }
    public int[] getDuration() { return null; }
    public String getSemester() { return null; }

    public boolean willConflict(Course potentialCourse) { return true; }
}
