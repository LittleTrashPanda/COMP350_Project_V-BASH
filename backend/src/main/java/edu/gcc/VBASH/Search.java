package edu.gcc.VBASH;

import java.util.Map;

public class Search {
    private Map<String, String[]> professorByDepartment;
    private Map<String, String[]> courseCodeByDepartment;
    private String[] keyTerms;

    private Iterable<Course> filteredCourses;

    private Filter currentFilter;
    private Iterable<Course> resultingCourses;

    public Iterable<Course> filterCourses (Filter providedFilter) { return null; }
    public Iterable<Course> search(){
        return null;
    }
    public void resetFilter(){

    }
}
