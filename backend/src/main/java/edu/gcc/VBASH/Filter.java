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

    public boolean filterTime(int[] startTimesInt){
        //run through each course in the list of courses, check if that course's time match the given time
        //display the courses that meet this
        //for each loop? if course.getStartTimes()

        return true;
    }

    public boolean filterDay(int daysInt){
        //run through each couse in the list of coursed, check if each course's days match with given day
        //display the courses that meet this


        return true;
    }
}
