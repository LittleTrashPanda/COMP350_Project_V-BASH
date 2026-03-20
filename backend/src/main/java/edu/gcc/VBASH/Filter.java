package edu.gcc.VBASH;

public class Filter {
    public String department;
    public String courseCode;
    public String professor;
    public int credits;
    public int days;
    public int[] startTimes;
    public int[] duration;
    public String semester;

    public Filter() { }

    // Constructor
    public Filter(String department, String courseCode, String professor, int credits, int days, int[] startTimes, int[] duration, String semester) {
        this.department = department;
        this.courseCode = courseCode;
        this.professor = professor;
        this.credits = credits;
        this.days = days;
        this.startTimes = startTimes;
        this.duration = duration;
        this.semester = semester;
    }

    // Primary Filter Call
    public boolean filterCourse(Course checkCourse) {
        return (
                filterTime(checkCourse) &&
                filterDay(checkCourse) &&
                filterDept(checkCourse) &&
                filterProf(checkCourse) &&
                filterCreditNum(checkCourse) &&
                filterCourseCode(checkCourse) &&
                filterSemester(checkCourse)
        );
    }

    // ----------------------------------------------------------------------------------------------------
    // Helper Filter Calls

    // Legacy
    /* public boolean filterKeyWordTD(Course checkCourse, String keyWord){
        if (checkCourse == null)
            return false;
        if (checkCourse.getDescription().contains(keyWord) || checkCourse.getCourseName().contains(keyWord)
                || checkCourse.getDescription().toLowerCase().contains(keyWord) ||
                checkCourse.getCourseName().toLowerCase().contains(keyWord))
            return true;
        if (keyWord == null || keyWord == ""){
            return true;
        }
        return false;
    } */

    // Semester
    public boolean filterSemester(Course checkCourse) {
        if (checkCourse == null) { return false; }
        if (semester == null || semester.isEmpty()) { return true; }
        return semester.equals(checkCourse.getSemester());
    }

    // Professors
    public boolean filterProf(Course checkCourse){
        if (checkCourse == null)
            return false;
        if (professor == null || professor.isEmpty())
            return true;
        for (int i = 0; i<checkCourse.getProfessors().length; i++){
            if (checkCourse.getProfessors()[i].toLowerCase().contains(professor.toLowerCase()))
                return true;
        }
        return false;
    }

    // Time Slot
    public boolean filterTime(Course checkCourse){
        //run through the filter's startTimes and see if at least
        //one start time matches the startTimes of checkCourse


        //if there are no startTimes in the filter, then display the class
        if(startTimes == null){return true;}
        boolean isEmpty = true;
        for (int i = 0; i<checkCourse.getStartTimes().length; i++){
            if (duration[i] == 0) {continue;}
            isEmpty= false;
        }
        if (isEmpty){return true;}

        int timeMatches = 0;
        for(int i = 0; i < 5; i++){
            //if both the course startTime and the filter startTime match,
            //then up the number of matches
            //also checks if the filter's startTime is within the duration of course's time
            if (duration[i] < 0) {return false;}

            int curCourseTime = checkCourse.getStartTimes()[i];
            int time = startTimes[i];

            if(curCourseTime == 0 || time == 0) continue;

            if(time < curCourseTime + checkCourse.getDuration()[i] && curCourseTime < time + duration[i]){
                timeMatches++;
            }
        }

        //as long as the start time matched at least once, then the course is displayed, else not displayed
        return timeMatches >= 1;
    }

    // Day
    public boolean filterDay(Course checkCourse){
        //check if checkCourse days match the filters days based of if it contains the days that you want,
        //so if you select Monday, it will show you all the classes that are on Mondays,
        //but if you select MWF, it shows you all classes that take place on each of those days,
        //even if a class doesn't take place on all of those days

        //if there are no days in the filter, then display the class
        if(days == 1){return true;}

        //prime to day
        int[] primesArr = {2, 3, 5, 7, 11};

        //going through each of the days in filter, check that we are using that day,
        //if day is being used, use the corresponding prime factor to see if checkCourse
        //also has that day
        for (int i = 0; i < primesArr.length; i++) {
            if (days % primesArr[i] != 0) { continue; }
            if(checkCourse.getDays() % primesArr[i] == 0) { return true; }
        }

        return false;
    }


    // Department
    public boolean filterDept(Course checkCourse){
        // Example Dept: COMP

        // if not using dept as a filter, course passes the check.
        if(department == null || department.isEmpty()){return true;}

        // if the checked course's dept is the same as the dept in the filter, returns
        return (checkCourse.getCourseCode().substring(0, 4).equalsIgnoreCase(department.strip()));

    }

    // Credits
    public boolean filterCreditNum(Course checkCourse){
        // Example Credit Num: 3

        // if not using number of credits as a filter, course passes the check.
        if(credits == -1){return true;}

        // if the checked course's number of credits is the same as the number of credits in the filter, returns
        return (credits == checkCourse.getCredits());
    }

    // Course Code
    public boolean filterCourseCode(Course checkCourse){
        // Example Course Code: 300

        // if not using course code as a filter, course passes the check.
        if (courseCode == null || courseCode.isBlank()) return true;

        // Normalize both: lowercase + remove spaces
        String course = checkCourse.getCourseCode()
                .toLowerCase()
                .replaceAll("\\s+", "");   // "COMP340 A" -> "comp340a"

        String filter = courseCode
                .toLowerCase()
                .strip()
                .replaceAll("\\s+", "");   // "comp 340" -> "comp340"

        return course.contains(filter);

    }
}
