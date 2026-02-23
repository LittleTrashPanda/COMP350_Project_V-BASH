package edu.gcc.VBASH;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.Map;

public class Search {
    private Map<String, String[]> professorByDepartment;
    private Map<String, String[]> courseCodeByDepartment;
    private String[] keyTerms;

    private Iterable<Course> filteredCourses;

    private Filter currentFilter;
    private Iterable<Course> resultingCourses;

    public Iterable<Course> filterCourses (Filter providedFilter) { return null; }
    public static Iterable<Course> search() throws IOException, ParseException {
        FileReader sourceFile = new FileReader("backend/src/main/resources/data_wolfe.json");
        JSONObject classList = (JSONObject) new JSONParser().parse(sourceFile);
        System.out.print(classList);
        return null;
    }

    public void resetFilter(){

    }
}
