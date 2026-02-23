package edu.gcc.VBASH;

public class User {
    private Iterable<Schedule> savedSchedules;
    private Schedule candidateSchedule;

    private String username;
    private int passwordHash;


    public void saveSchedule(String scheduleName) { return; }
    public void loadSchedule(String scheduleName) { return; }
    public void loadSchedule(Schedule generatedSchedule) { return; }

    public void newSchedule() { return; }
    public Iterable<Schedule> getSchedules() { return null; }
}
