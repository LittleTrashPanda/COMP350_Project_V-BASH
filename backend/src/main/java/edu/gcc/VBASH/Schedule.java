package edu.gcc.VBASH;

import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.ArrayList;import java.util.Iterator;import java.util.List;

public class Schedule {
    private String name;
    private static ArrayList<Course> courses = new ArrayList<Course>();

    public Schedule(String name, Iterable<Course> courses) { }

    public static ArrayList<Course> getCourseList(){
        return courses;
    }

    public boolean checkCourseConflict(Course potentialCourse) {
        //goes through each course on courses, checking if there are any conflicts
        for (Course course : courses) {
            if (course.willConflict(potentialCourse)) {
                return true;
            }
        }

        //if there are no conflicts, return such
        return false;
    }
    public void addCourse(Course potentialCourse) {
        //checks if there is a course conflict. If there is, the method does nothing yet.
        if (!checkCourseConflict(potentialCourse)){
            //adds to the course arraylist
            courses.add(potentialCourse);
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
    public void replaceCourse(Course newCourse, List<Course> conflicts) {
        for (Course oldCourse : conflicts) {
            removeCourse(oldCourse);
        }

        addCourse(newCourse);
    }
    public static Iterable<Course> getCourses() throws IOException { return courses; }

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
