package edu.gcc.VBASH;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.*;

public class Search {
    private static ArrayList<Course> resultingCourses = null;
    private static ArrayList<String> previousKeySearchTerms = null;

    private static Filter currentFilter = new Filter("", "", "", -1, 1, null, null, "");

    // ----------------------------------------------------------------------------------------------------
    // Search Terms
    private static ArrayList<String> keySearchTerms = new ArrayList<>();

    public static void SetKeySearchTerms(ArrayList<String> newKeySearchTerms) { keySearchTerms = newKeySearchTerms; }

    private static boolean CheckInheritedKeySearchTerms() {
        if (previousKeySearchTerms == null) { return false; }

        for (String keySearchTerm: previousKeySearchTerms) { if (!keySearchTerms.contains(keySearchTerm)) { return false; } }
        return true;
    }

    // Retrieving Courses
    public static JSONArray search() throws IOException, ParseException {
        ArrayList<Course> queryResults = new ArrayList<Course>();

        // Use Previous Search as Basis
        if (CheckInheritedKeySearchTerms()) {
            for (Course course : resultingCourses) {
                if (keySearchTerms == null || keySearchTerms.isEmpty()) { queryResults.add(course); continue; }
                if (keySearchTermsFilter(course)) { queryResults.add(course); }
            }
        }

        // ReQuery Database
        else {
            // Read In
            FileReader sourceFile = new FileReader("backend/src/main/resources/private/data_wolfe.json");
            JSONArray courseList = (JSONArray) ((JSONObject) new JSONParser().parse(sourceFile)).get("classes");

            for (Object course : courseList) {
                // Create Course Object
                Course toAdd = courseCreator((JSONObject) course);

                // Filter by Key Search Terms
                if (keySearchTerms == null || keySearchTerms.isEmpty()) { queryResults.add(toAdd); continue; }
                if (keySearchTermsFilter(toAdd)) { queryResults.add(toAdd); }
            }
        }

        // Save Query Results
        resultingCourses = queryResults;
        previousKeySearchTerms = keySearchTerms;

        // Filter Results
        JSONArray toReturn = new JSONArray();
        for (Course course : resultingCourses) {
            if (currentFilter.filterCourse(course) && currentScheduleSemesterFilter(course)) {
                JSONObject temp = User.CourseToJSON(course);

                // Default Color
                temp.put("backgroundColor", "#F9FEFC");
                temp.put("color", "#8E1600");

                // Is a Major Requirement
                if (User.isMajorRequirement(course)) {
                    temp.replace("backgroundColor", "#C26969");
                    temp.put("color", "#F9FEFC");
                }

                // Is Being Taken
                if (User.isInCurrentSchedule(course)) {
                    temp.replace("backgroundColor", "#5C3232");
                    temp.put("color", "#F9FEFC");
                }

                // Already Taken Course
                if (User.isTakenCourse(course)) {
                    temp.replace("backgroundColor", "#524646");
                    temp.put("color", "#F9FEFC");
                }

                toReturn.add(temp);
            }
        }

        return toReturn;
    }

    // Helper Methods
    private static Course courseCreator(JSONObject course) {
        return new Course(
                /* Course Name */   course.get("name").toString(),
                /* Department */    course.get("subject").toString(),
                /* Course Number */ (course.get("subject") + course.get("number").toString() + " " + course.get("section")),
                /* Description */   "No Description Provided",
                /* Professors */    professorParsing((JSONArray) course.get("faculty")),
                /* Credits */       Math.toIntExact((long) course.get("credits")),
                /* Days */          dayParsing((JSONArray) course.get("times")),
                /* Start Times */   startTimeParsing((JSONArray) course.get("times")),
                /* Duration */      durationParsing((JSONArray) course.get("times")),
                /* Semester */      course.get("semester").toString()
        );
    }

    public static void setFilter(Filter newFilter) {
        currentFilter = newFilter;
    }

    // Transforming Data
    private static String[] professorParsing(JSONArray professors) {
        String[] toReturn = new String[professors.size()];

        // Parsing and Pulling Strings
        for (int i = 0; i < professors.size(); i++) { toReturn[i] = professors.get(i).toString(); }
        return toReturn;
    }

    private static int dayParsing(JSONArray dates) {
        // Prime Indexing
        int toReturn = 1;
        Map<String, Integer> dayMap = Map.of("M", 2, "T", 3, "W", 5, "R", 7, "F", 11);

        // Multiplying by the 'nth' Prime Number ([toReturn] % [Day's Prime] == 0 Means a Class Occurs on that Day)
        for (Object date : dates) { toReturn *= dayMap.get(((JSONObject) date).get("day").toString()); }

        return toReturn;
    }

    private static int[] startTimeParsing(JSONArray dates) {
        // Array Indexing
        int[] toReturn = new int[5];
        Map<String, Integer> dayMap = Map.of("M", 0, "T", 1, "W", 2, "R", 3, "F", 4);

        // Calculating Start Time (Minutes) and Placing it in a 'Day'
        for (Object date : dates) { toReturn[dayMap.get(((JSONObject) date).get("day").toString())] = timeParsing(((JSONObject) date).get("start_time").toString()); }
        return toReturn;
    }

    private static int[] durationParsing(JSONArray dates) {
        // Array Indexing
        int[] toReturn = new int[5];
        Map<String, Integer> dayMap = Map.of("M", 0, "T", 1, "W", 2, "R", 3, "F", 4);

        // Calculating Difference in Time (Minutes) and Placing it in a 'Day'
        for (Object date : dates) { toReturn[dayMap.get(((JSONObject) date).get("day").toString())] = timeParsing((String) ((JSONObject) date).get("end_time")) - timeParsing((String) ((JSONObject) date).get("start_time")); }
        return toReturn;
    }

    public static int timeParsing(String time) {
        String text = new Scanner(time).nextLine();

        // Manual Calculation of Minutes (Hour * 60 + Minute)
        return (((int) text.charAt(0) - (int) '0') * 10 + ((int) text.charAt(1) - (int) '0')) * 60 + (((int) text.charAt(3) - (int) '0') * 10 + ((int) text.charAt(4)) - (int) '0');
    }

    // Filtering by Key Search Terms
    private static boolean keySearchTermsFilter(Course course) {
        // Finding Key Search Terms in Course Attributes
        for (String term : keySearchTerms) {
            if (course.getCourseName().toLowerCase().contains(term.toLowerCase())) { continue; }
            if (course.getDepartment().toLowerCase().contains(term.toLowerCase())) { continue; }
            if (course.getCourseCode().toLowerCase().contains(term.toLowerCase())) { continue; }
            if (course.getDescription().toLowerCase().contains(term.toLowerCase())) { continue; }

            boolean foundProfessor = false;
            for (String professor : course.getProfessors()) { if (professor.toLowerCase().contains(term.toLowerCase())) { foundProfessor = true; break; } }
            if (foundProfessor) { continue; }

            return false;
        }

        // All Key Search Terms Found
        return true;
    }

    private static boolean currentScheduleSemesterFilter(Course course) { return User.getCurrentSchedule().getSemester().equals(course.getSemester()); }
}