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

    // Testing Overlap
    public boolean willConflict(Course potentialCourse) {

        //going through each startTime for our course, check each course time
        //for the potential course seeing if they have the same startTime
        //or if our course takes place over the duration of potentialCourse
        //also checks if the potentialCourse falls within duration of our course
        for(int time : startTimes){
            for(int i = 0; i < potentialCourse.getStartTimes().length; i++){
                int curCourseTime = potentialCourse.getStartTimes()[i];
                if(curCourseTime == time
                        || (time <= curCourseTime + potentialCourse.getDuration()[i] && time >= curCourseTime)
                        || (curCourseTime <= time + duration[i] && curCourseTime >= time)
                ){
                    return true;
                }
            }
        }

        //if none of the times overlap, return false
        return false;
    }
}
