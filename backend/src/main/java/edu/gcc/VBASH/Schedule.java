package edu.gcc.VBASH;

import org.json.simple.parser.ParseException;

import java.util.ArrayList;import java.util.Iterator;
import java.io.IOException;
import java.util.ArrayList;

public class Schedule {
    private String name;
    private static Iterable<Course> courses = new ArrayList<Course>();

    public Schedule(String name, Iterable<Course> courses) {
        this.name = name;
        Schedule.courses = courses;
    }

    public boolean checkCourseConflict(Course potentialCourse) {
        if (potentialCourse.getDays() == 1) {
            return true;
        }

        for (Course existing : courses) {

            // skip courses with no times
            if (existing.getDays() == 1) continue;

            // check each day index (0 = M, 1 = T, ...)
            for (int i = 0; i < 5; i++) {

                int start1 = existing.getStartTimes()[i];
                int start2 = potentialCourse.getStartTimes()[i];
                int dur1 = existing.getDuration()[i];
                int dur2 = potentialCourse.getDuration()[i];

                if (dur1 == 0 || dur2 == 0) continue;

                int end1 = start1 + dur1;
                int end2 = start2 + dur2;

                if (start1 < end2 && end1 > start2) {
                    return true;
                }
            }
        }

        return false;

    }
    public void addCourse(Course potentialCourse) {
        // Only add if no conflict
        if (!checkCourseConflict(potentialCourse)) {

            // Convert Iterable to ArrayList safely
            ArrayList<Course> tempSchedule = new ArrayList<>();
            for (Course c : courses) {
                tempSchedule.add(c);
            }

            // Add the new course
            tempSchedule.add(potentialCourse);

            // Replace the static courses reference
            courses = tempSchedule;
        }
    }

    public void removeCourse(Course enrolledCourse) {
        ArrayList<Course> tempSchedule = new ArrayList<>();

        for (Course c : courses) {
            if (!c.equals(enrolledCourse)) {
                tempSchedule.add(c);
            }
        }

        courses = tempSchedule;
    }

    public void replaceCourse(Course oldCourse, Course newCourse) {
        removeCourse(oldCourse);
        addCourse(newCourse);
    }

    public static Iterable<Course> getCourses() {
        return courses;
    }


    public String getName() { return name; }
    public void setName(String newName) { name = newName; }
}
