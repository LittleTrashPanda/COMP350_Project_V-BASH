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
    private String[] keyTerms;

    private static Iterable<Course> resultingCourses;

    private Iterable<Course> filteredCourses;
    private Filter currentFilter;

    public Iterable<Course> filterCourses (Filter providedFilter) { return null; }

    // ----------------------------------------------------------------------------------------------------

    // Accessing Database
    public static void search() throws IOException, ParseException {
        FileReader sourceFile = new FileReader("backend/src/main/resources/data_wolfe.json");
        JSONArray courseList = (JSONArray) ((JSONObject) new JSONParser().parse(sourceFile)).get("classes");

        /*
        JSONObject test = (JSONObject) courseList.get(new Random().nextInt(courseList.size()));

        System.out.println((String) test.get("name"));
        System.out.println((String) test.get("subject"));
        System.out.println(test.get("subject") + test.get("number").toString());
        System.out.println();
        System.out.println(Arrays.toString(professorParsing((JSONArray) test.get("faculty"))));
        System.out.println(Math.toIntExact((long) test.get("credits")));
        System.out.println(dayParsing((JSONArray) test.get("times")));
        System.out.println(Arrays.toString(startTimeParsing((JSONArray) test.get("times"))));
        System.out.println(Arrays.toString(durationParsing((JSONArray) test.get("times"))));
        System.out.println((String) test.get("semester"));
        */

        ArrayList<Course> toReturn = new ArrayList<Course>();
        for (Object course : courseList) { toReturn.add(courseCreator((JSONObject) course)); }
        resultingCourses = toReturn;
    }

    // Helper Methods
    private static Course courseCreator(JSONObject course) {
        return new Course(
                course.get("name").toString(),
                course.get("subject").toString(),
                (course.get("subject") + course.get("number").toString()),
                "No Description Provided",
                professorParsing((JSONArray) course.get("faculty")),
                Math.toIntExact((long) course.get("credits")),
                dayParsing((JSONArray) course.get("times")),
                startTimeParsing((JSONArray) course.get("times")),
                durationParsing((JSONArray) course.get("times")),
                course.get("semester").toString()
        );
    }

    // Transforming Data
    private static String[] professorParsing(JSONArray professors) {
        String[] toReturn = new String[professors.size()];
        for (int i = 0; i < professors.size(); i++) { toReturn[i] = professors.get(i).toString(); }
        return toReturn;
    }

    private static int dayParsing(JSONArray dates) {
        int toReturn = 1;
        Map<String, Integer> dayMap = Map.of("M", 2, "T", 3, "W", 5, "R", 7, "F", 11);

        for (Object date : dates) { toReturn *= dayMap.get(((JSONObject) date).get("day").toString()); }

        return toReturn;
    }

    private static int[] startTimeParsing(JSONArray dates) {
        int[] toReturn = new int[5];
        Map<String, Integer> dayMap = Map.of("M", 0, "T", 1, "W", 2, "R", 3, "F", 4);

        for (Object date : dates) { toReturn[dayMap.get(((JSONObject) date).get("day").toString())] = timeParsing(((JSONObject) date).get("end_time").toString()); }
        return toReturn;
    }

    private static int[] durationParsing(JSONArray dates) {
        int[] toReturn = new int[5];
        Map<String, Integer> dayMap = Map.of("M", 0, "T", 1, "W", 2, "R", 3, "F", 4);

        for (Object date : dates) { toReturn[dayMap.get(((JSONObject) date).get("day").toString())] = timeParsing((String) ((JSONObject) date).get("end_time")) - timeParsing((String) ((JSONObject) date).get("start_time")); }
        return toReturn;
    }

    private static int timeParsing(String time) {
        String text = new Scanner(time).nextLine();
        return (((int) text.charAt(0)) * 10 + ((int) text.charAt(1))) * 60 + ((int) text.charAt(3) * 10 + (int) text.charAt(4));
    }

    // ----------------------------------------------------------------------------------------------------

    public static void main(String[] args) throws IOException, ParseException {
        try { search(); }
        catch (Exception e) { System.out.print(e); }
    }

    public void resetFilter(){

    }
}
