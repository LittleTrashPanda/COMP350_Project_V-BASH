package edu.gcc.VBASH;

public class Schedule {
    private String name;
    private Iterable<Course> courses;

    public Schedule(String name, Iterable<Course> courses) { }

    public boolean checkCourseConflict(Course potentialCourse) { return true; }
    public void addCourse(Course potentialCourse) { return; }
    public void removeCourse(Course enrolledCourse) { return; }
    public void replaceCourse(Course oldCourse, Course newCourse) { return; }

    public Iterable<Course> getCourses() { return null; }

    public String getName() { return null; }
    public void setName(String newName) { return; }
}
