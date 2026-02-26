package edu.gcc.VBASH;

import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class Schedule {
    private String name;
    private static Iterable<Course> courses;

    public Schedule(String name, Iterable<Course> courses) { }

    public boolean checkCourseConflict(Course potentialCourse) { return false; }
    public void addCourse(Course potentialCourse) { return; }
    public void removeCourse(Course enrolledCourse) { return; }
    public void replaceCourse(Course oldCourse, Course newCourse) { return; }

    public static Iterable<Course> getCourses() throws IOException, ParseException {
        ArrayList<Course> test = new ArrayList<Course>();

        test.add((Course) ((ArrayList) Search.search()).get(180));
        test.add((Course) ((ArrayList) Search.search()).get(400));
        return test;
    }

    public String getName() { return null; }
    public void setName(String newName) { name = newName; }
}
