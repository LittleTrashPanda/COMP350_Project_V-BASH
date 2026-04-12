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

    public Course() { }

    // Constructor
    public Course(String courseName, String department, String courseCode, String description, String[] professors, int credits, int days, int[] startTimes, int[] duration, String semester) {
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

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Course other = (Course) obj;

        return courseCode.equals(other.courseCode)
                && courseName.equals(other.courseName);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(courseCode, courseName);
    }


    public boolean willConflict(Course potentialCourse) {
        // If they are in different Semesters, they can't overlap
        if (!(semester.equals(potentialCourse.getSemester()))) { return false; }

        //going through each startTime for our course, check each course time
        //for the potential course seeing if they have the same startTime
        //or if our course takes place over the duration of potentialCourse
        //also checks if the potentialCourse falls within duration of our course
            for(int i = 0; i < 5; i++){
                int curCourseTime = potentialCourse.getStartTimes()[i];
                int time = startTimes[i];

                if(curCourseTime == 0 || time == 0) continue;

                if(time < curCourseTime + potentialCourse.getDuration()[i] && curCourseTime < time + duration[i]){
                    return true;
                }
            }

        //if none of the times overlap, return false
        return false;
    }
}