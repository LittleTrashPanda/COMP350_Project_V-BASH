package edu.gcc.VBASH;

import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.ArrayList;import java.util.Iterator;

public class Schedule {
    private String name;
    private static Iterable<Course> courses = new ArrayList<Course>();

    public Schedule(String name, Iterable<Course> courses) { }

    public boolean checkCourseConflict(Course potentialCourse) {
        //goes through each course on courses, checking if there are any conflicts
        Iterator<Course> iterator = courses.iterator();
        while(iterator.hasNext()){
            if(iterator.next().willConflict(potentialCourse)){
                return true;
            }
        }

        //if there are no conflicts, return such
        return false;
    }
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
    public void removeCourse(Course enrolledCourse) {
        Iterator<Course> iterator = courses.iterator();
        while (iterator.hasNext()) {
            if(iterator.next().equals(enrolledCourse)){
                iterator.remove();
            }
        }

    }
    public void replaceCourse(Course oldCourse, Course newCourse) {
        if(!checkCourseConflict(newCourse)) {
            addCourse(newCourse);
        }

        removeCourse(oldCourse);
    }
    public static Iterable<Course> getCourses() throws IOException, ParseException { return courses; }

    public String getName() { return name; }
    public void setName(String newName) { name = newName; }

    public static Course getCourse(String[] cCode) throws IOException, ParseException {
        Search search = new Search();
        search.SetKeySearchTerms(cCode);
        Iterable<Course> temp = search.search();
        //unless smth goes wrong, the code above should only return one class
        return temp.iterator().next();
    }
}
