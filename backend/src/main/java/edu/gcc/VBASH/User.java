package edu.gcc.VBASH;

import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class User {
    private Iterable<Schedule> savedSchedules;
    private Schedule candidateSchedule;

    private String username;
    private int passwordHash;

    // Format for the data file should ensure that a data set always starts with Name: ----- to
    // separate the data points
    public void saveSchedule(String scheduleName) throws FileNotFoundException {
        //TODO: Work on replacement function
        try{
            FileWriter fw = new FileWriter("backend/src/main/java/edu/gcc/VBASH/scheduleStore", true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter out = new PrintWriter(bw);
            Iterable<Course> courses = candidateSchedule.getCourses();
            out.println("Name: " + candidateSchedule.getName() + " ");
            while(candidateSchedule.getCourses().iterator().hasNext()){
                out.println(candidateSchedule.getCourses().iterator().next().getCourseCode() + " ");
            }
            out.close();
    }
    catch (IOException e) {
        System.out.println("failed to save");
        }}

    public void loadSchedule(String scheduleName) throws IOException, ParseException {
        //Goes to the file in question and initializes a scanner
        File file = new File("backend/src/main/java/edu/gcc/VBASH/scheduleStore");
        Scanner slate = new Scanner(file);
        //boolean to track when you're at the right spot in the file (I had fun with the name)
        boolean wethereyet = false;
        //keeps the while loop active until you get to the right spot, and once you're there it stops after
        //getting all the data
        //TODO: simplify - maybe assign position values to individual names to make accessing the data quicker
        boolean stopper = true;
        int track = 0; //keeps track of when it reaches the data spot and after it
        while (stopper){
            //if its the end of the file, it stops.
            if(!slate.hasNext()) {
                stopper = false;
            }
            //when it reaches the right name, it starts reading data
            if (slate.next() == "Name:"){
                if (slate.next() == scheduleName){
                    wethereyet = true;
                    track +=1;
                }
                else{ //if it's the wrong name, it doesn't do anything and keeps moving
                    wethereyet = false;
                    //if you've already finished reading the requested data and hit another data set,
                    //this ends the loop
                    if (track == 2)
                        stopper = false;
                }
            }
            //TODO: Impliment the String to String[] IN .getCourse() and simplify
            //overrights any previously existing schedule to replace it with the saved information
            if (wethereyet && track == 1){
                String code = slate.next();
                Iterable<Course> temp;
                ArrayList<Course> moreTempThanTemp = new ArrayList<>();
                moreTempThanTemp.add(candidateSchedule.getCourse(code));
                temp = moreTempThanTemp;
                candidateSchedule = new Schedule(scheduleName, temp);
                track +=1;
            }
            //if it's not the first time through, it reads the data and adds the classes to candidateSchedule
            else if (wethereyet){
                String code = slate.next();
                candidateSchedule.addCourse(candidateSchedule.getCourse(code));
            }
        }
    }

    public void loadSchedule(Schedule generatedSchedule) { return; }

    public void newSchedule() { return; }
    public Iterable<Schedule> getSchedules() { return null; }
}
