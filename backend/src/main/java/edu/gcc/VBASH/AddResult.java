package edu.gcc.VBASH;

import java.util.ArrayList;public class AddResult {
    private boolean success;
    private String message;
    private ArrayList<Course> conflicts;

    public AddResult(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public AddResult(boolean success, String message, ArrayList<Course> conflicts) {
        this.success = success;
        this.message = message;
        this.conflicts = conflicts;
    }

    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
    public ArrayList<Course> getCourses() { return conflicts; }
}


