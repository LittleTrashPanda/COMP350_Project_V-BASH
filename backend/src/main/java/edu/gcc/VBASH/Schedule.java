package edu.gcc.VBASH;

import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;import java.util.Iterator;import java.util.List;

public class Schedule {
    private String name;
    private ArrayList<Course> courses = new ArrayList<Course>();
    private String currentSemester;

    public Schedule(String name, ArrayList<Course> courses, String currentSemester) {
        this.name = name;
        this.courses = courses;
        this.currentSemester = currentSemester;
    }

    public ArrayList<Course> getCourseList(){
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

    public void removeAllCourses() { courses = new ArrayList<Course>(); }

    public ArrayList<Course> getCourses() { return courses; }

    public ArrayList<Course> getCurrentCourses() {
        ArrayList<Course> toReturn = new ArrayList<Course>();
        for (Course course : courses) { if (currentSemester.equals(course.getSemester())) { toReturn.add(course); } }
        return toReturn;
    }

    // Naming Convention
    public String getName() { return name; }
    public void setName(String newName) { if (!newName.isEmpty()) { name = newName; } }

    public static Course getCourse(String cCode) throws IOException, ParseException {
        Search search = new Search();
        ArrayList<String> tempList = new ArrayList<>();
        tempList.add(cCode);
        search.SetKeySearchTerms(tempList);
        Iterable<Course> temp = search.search();
        //unless smth goes wrong, the code above should only return one class
        return temp.iterator().next();
    }

    public String getSemester() { return currentSemester; }
    public void setSemester(String currentSemester) {
        System.out.print(currentSemester);
        this.currentSemester = currentSemester;
    }
}
