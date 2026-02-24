package edu.gcc.VBASH;

public class Filter {
    private String department;
    private String courseCode;
    private String professor;
    private int credits;
    private int[] days;
    private int[] startTimes;
    private int[] duration;
    private String semester;

    public Filter(
            String department,
            String courseCode,
            String professor,
            int credits,
            int[] days,
            int[] startTimes,
            int[] duration,
            String semester
    ) { }

    public boolean filterCourse(Course checkCourse) { return true;}

}
