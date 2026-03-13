package edu.gcc.VBASH;

public class AddResult {
    private boolean success;
    private String message;

    public AddResult(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
}


