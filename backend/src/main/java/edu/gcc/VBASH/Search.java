package edu.gcc.VBASH;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.*;

public class Search {
    private Map<String, String[]> professorByDepartment;
    private Map<String, String[]> courseCodeByDepartment;

    private static Iterable<Course> resultingCourses;

    private Iterable<Course> filteredCourses;
    private static Filter currentFilter;

    public Iterable<Course> filterCourses (Filter providedFilter) { return null; }

    // ----------------------------------------------------------------------------------------------------
    // Search Terms
    private static String[] keySearchTerms;
    private static SearchFilters incomingFilters;

    public static void setFilters(SearchFilters filters) {
        incomingFilters = filters;
    }

    public static SearchFilters getFilters() {
        return incomingFilters;
    }


    public static void SetKeySearchTerms(String[] newKeySearchTerms) { keySearchTerms = newKeySearchTerms; }

    // Accessing Database
    public static Iterable<Course> search() throws IOException, ParseException {
        FileReader sourceFile = new FileReader("backend/src/main/resources/private/data_wolfe.json");
        JSONArray courseList = (JSONArray) ((JSONObject) new JSONParser().parse(sourceFile)).get("classes");

        SearchFilters sf = getFilters();

        if (sf == null) {
            // No filters sent yet → allow everything
            currentFilter = new Filter(null, null, null, -1, null, null, null, null);
        } else {

            // Safe parse credits
            int creditValue = -1;
            if (sf.credits != null && !sf.credits.isBlank()) {
                int parsed = Integer.parseInt(sf.credits);
                creditValue = (parsed == 0) ? -1 : parsed;

            }

            // Safe parse dept/prof/courseCode
            String dept = (sf.dept == null || sf.dept.isBlank()) ? null : sf.dept;
            String professor = (sf.professor == null || sf.professor.isBlank()) ? null : sf.professor;
            String courseCode = (sf.courseCode == null || sf.courseCode.isBlank()) ? null : sf.courseCode;

            // Safe parse days
            int[] days = null;
            if (sf.selectedDays != null && !sf.selectedDays.isEmpty()) {
                days = new int[5];
                for (int d : sf.selectedDays) {
                    if (d == 1) days[0] = 2;
                    if (d == 2) days[1] = 3;
                    if (d == 3) days[2] = 5;
                    if (d == 5) days[3] = 7;
                    if (d == 7) days[4] = 11;
                }
            }
            // Safe parse time (start time only)
            int[] filterStartTimes = null;
            int[] filterDurations = null;

            if (sf.time != null && !sf.time.isBlank()) {

                // Convert "08:00" into minutes
                int parsedStart = timeParsing(sf.time);

                // Build the 5-element arrays
                filterStartTimes = new int[5];
                filterDurations = new int[5];

                // Put the selected time into ALL days
                // (or only specific days if you want)
                for (int i = 0; i < 5; i++) {
                    filterStartTimes[i] = parsedStart;
                    filterDurations[i] = 60;   // default 1 hour window
                }
            }

            currentFilter = new Filter(
                    dept,
                    courseCode,
                    professor,
                    creditValue,
                    days,
                    filterStartTimes,
                    filterDurations,
                    null
            );

        }

        ArrayList<Course> toReturn = new ArrayList<Course>();
        for (Object course : courseList) {
            Course toAdd = courseCreator((JSONObject) course);

            if (!currentFilter.filterCourse(toAdd)) continue;

            if (keySearchTerms == null || keySearchTerms.length == 0) {
                toReturn.add(toAdd);
                continue;
            }

            if (keySearchTermsFilter(toAdd)) {
                toReturn.add(toAdd);
            }
        }

        return toReturn;
    }

    // Helper Methods
    private static Course courseCreator(JSONObject course) {
        return new Course(
                course.get("name").toString(), // Course Name
                course.get("subject").toString(), // Department
                (course.get("subject") + course.get("number").toString() + " " + course.get("section")), // Course Number
                "No Description Provided", // Description
                professorParsing((JSONArray) course.get("faculty")), // Professors
                Math.toIntExact((long) course.get("credits")), // Credits
                dayParsing((JSONArray) course.get("times")), // Days
                startTimeParsing((JSONArray) course.get("times")), // Start Times
                durationParsing((JSONArray) course.get("times")), // Duration
                course.get("semester").toString() // Semester
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
        int toReturn = 1;
        Map<String, Integer> dayMap = Map.of("M", 2, "T", 3, "W", 5, "R", 7, "F", 11);

        // Multiplying by the 'nth' Prime Number; [toReturn] % [Day's Prime] == 0 Means a Class Occurs on that Day
        for (Object date : dates) { toReturn *= dayMap.get(((JSONObject) date).get("day").toString()); }

        return toReturn;
    }

    private static int[] startTimeParsing(JSONArray dates) {
        int[] toReturn = new int[5];
        Map<String, Integer> dayMap = Map.of("M", 0, "T", 1, "W", 2, "R", 3, "F", 4);

        // Calculating Start Time and Placing it in a 'Day'
        for (Object date : dates) { toReturn[dayMap.get(((JSONObject) date).get("day").toString())] = timeParsing(((JSONObject) date).get("start_time").toString()); }
        return toReturn;
    }

    private static int[] durationParsing(JSONArray dates) {
        int[] toReturn = new int[5];
        Map<String, Integer> dayMap = Map.of("M", 0, "T", 1, "W", 2, "R", 3, "F", 4);

        // Calculating Time Difference and Placing it in a 'Day'
        for (Object date : dates) { toReturn[dayMap.get(((JSONObject) date).get("day").toString())] = timeParsing((String) ((JSONObject) date).get("end_time")) - timeParsing((String) ((JSONObject) date).get("start_time")); }
        return toReturn;
    }

    public static int timeParsing(String time) {
        String text = new Scanner(time).nextLine();

        // Manual Calculation of Minutes
        return (((int) text.charAt(0) - (int) '0') * 10 + ((int) text.charAt(1) - (int) '0')) * 60 + (((int) text.charAt(3) - (int) '0') * 10 + ((int) text.charAt(4)) - (int) '0');
    }

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

    // ----------------------------------------------------------------------------------------------------

    public void resetFilter(){

    }
}
