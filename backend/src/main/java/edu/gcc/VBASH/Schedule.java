package edu.gcc.VBASH;

import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.ArrayList;import java.util.Iterator;

public class Schedule {
    private String name;
    private static Iterable<Course> courses = new ArrayList<Course>();

    public Schedule(String name, Iterable<Course> courses) { }

    public boolean checkCourseConflict(Course potentialCourse) { return false; }
    public void addCourse(Course potentialCourse) { return; }

    public void removeCourse(Course enrolledCourse) {
        Iterator<Course> couseIterator = courses.iterator();
        while (couseIterator.hasNext()) {
            if(couseIterator.next().equals(enrolledCourse)) {
                couseIterator.remove();
            }
        }
    }

    public void replaceCourse(Course oldCourse, Course newCourse) { return; }

    public static Iterable<Course> getCourses() throws IOException, ParseException { return courses; }

    public String getName() { return null; }
    public void setName(String newName) { name = newName; }
}
