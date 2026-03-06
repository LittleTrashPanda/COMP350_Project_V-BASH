package edu.gcc.VBASH;

import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.ArrayList;

public class Schedule {
    private String name;
    private static Iterable<Course> courses = new ArrayList<Course>();

    public Schedule(String name, Iterable<Course> courses) { }

    public boolean checkCourseConflict(Course potentialCourse) { return false; }
    public void addCourse(Course potentialCourse) {
        //checks if there is a course conflict. If there is, the method does nothing yet.
        if (!checkCourseConflict(potentialCourse)){

            //Maybe not needed, but creates a copy of courses and a mutable schedule list since we can't
            //add directly to courses
            ArrayList<Course> tempSchedule = new ArrayList<Course>();
            Iterable<Course> courseCopy = new ArrayList<Course>();
            courseCopy = courses;

            //goes through the copied list of courses we now have, and fills out the temporary schedule
            while(courseCopy.iterator().hasNext()) {
                tempSchedule.add(courseCopy.iterator().next());
            }
            //Adds the potential course, and sets the values of courses equal to the tempSchedule
            tempSchedule.add(potentialCourse);
            courses = tempSchedule;

        }
         }
    public void removeCourse(Course enrolledCourse) { return; }
    public void replaceCourse(Course oldCourse, Course newCourse) { return; }

    public static Iterable<Course> getCourses() throws IOException, ParseException { return courses; }

    public String getName() { return null; }
    public void setName(String newName) { name = newName; }
}
