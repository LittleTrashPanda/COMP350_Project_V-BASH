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

    public boolean filterTime(Course checkCourse){
        //run through the filter's startTimes and see if at least
        //one start time matches the startTimes of checkCourse


        //if there are no startTimes in the filter, then display the class
        if(startTimes == null){return true;}

        int timeMatches = 0;
        for(int i = 0; i < checkCourse.getStartTimes().length; i++){

            for(int time : startTimes){
                //if both the course startTime and the filter startTime match,
                //then up the number of matches
                //also checks if the filter's startTime is within the duration of course's time
                int curCourseTime = checkCourse.getStartTimes()[i];
                if(curCourseTime == time || (time <= curCourseTime + duration[i] && time >= curCourseTime)){
                    timeMatches++;
                }

            }
        }

        //as long as the start time matched at least once, then the course is displayed, else not displayed
        return timeMatches >= 1;
    }

    public boolean filterDay(Course checkCourse){
        //check if checkCourse days match the filters days based of if it contains the days that you want,
        //so if you select Monday, it will show you all the classes that are on Mondays,
        //but if you select MWF, it shows you all classes that take place on each of those days,
        //even if a class doesn't take place on all of those days

        //if there are no days in the filter, then display the class
        if(days == null){return true;}

        //checks how many matches there are by days
        int daysMatches = 0;
        int[] primesArr = {2, 3, 5, 7, 11};

        //going through each of the days in filter, check that we are using that day,
        //if day is being used, use the corresponding prime factor to see if checkCourse
        //also has that day, if so then increase the days matched
        for(int i = 0; i<days.length; i++){
            if(days[i] != 0){
                if(checkCourse.getDays() % primesArr[i] == 0){
                    daysMatches++;
                }
            }
        }

        return daysMatches >= 1;
    }
}
