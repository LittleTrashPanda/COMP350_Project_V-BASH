package edu.gcc.VBASH;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class User {
    // Identifiers
    private static String username;
    private static int passwordHash;

    // User Schedules
    private static ArrayList<Schedule> savedSchedules = new ArrayList<Schedule>();
    private static Schedule candidateSchedule = new Schedule("default", new ArrayList<Course>());

    // Retrieving and Resetting Schedules
    public static Schedule getSchedule() { return candidateSchedule; }

    public static void newSchedule() { candidateSchedule = new Schedule("default", new ArrayList<Course>()); }

    public static void resetSchedule() throws IOException, ParseException {loadSchedule("");}

    //for the pdf
    public static String getTimes() throws IOException {
        StringBuilder out = new StringBuilder();
        ArrayList<Course> courses = candidateSchedule.getCourses();
        String week = "MTWRF";
        for(int i = 0; i<courses.size(); i++){
            StringBuilder daysofweek = new StringBuilder();
            for (int j = 0; j<5; j++){
                if (courses.get(i).getStartTimes()[j] != 0){
                    daysofweek.append(week.charAt(i));
                }

            }
            int sTime = courses.get(i).getStartTimes()[0]/60;
            String eTime ="" + sTime + ":"+((courses.get(i).getStartTimes()[0] / sTime) - 10);
            out.append(sTime + " - " + eTime + "\n");
        }

        return out.toString();
    }

    // Format for the data file should ensure that a data set always starts with Name: ----- to separate the data points
    public static void saveSchedule() throws IOException, ParseException {
        // Legacy
        /* System.out.println("Starting to Save");
        //TODO: Work on replacement function
        try{
            FileWriter fw = new FileWriter("backend/src/main/java/edu/gcc/VBASH/scheduleStore", true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter out = new PrintWriter(bw);
            out.println("Name: " + candidateSchedule.getName() + " ");
            while(candidateSchedule.getCourses().iterator().hasNext()){
                out.println(candidateSchedule.getCourses().iterator().next().getCourseCode() + " ");
            }
            out.close();
        }
        catch (IOException e) {
            System.out.println("failed to save");
        }

        System.out.println("Saved Schedule");

        JSONObject shell;
        JSONParser parser = new JSONParser();

        //Reads in the data from the old file so that you can have multiple schedules saved
        try{
            FileReader reader = new FileReader("backend/src/main/resources/private/userSchedules.json");
            shell = (JSONObject) parser.parse(reader);
            reader.close();
        }
        catch(Exception e){
            shell = new JSONObject();
        } */

        // Adding Schedule Values
        JSONObject schedule = new JSONObject();
        schedule.put("name", candidateSchedule.getName());

        JSONArray courseList = new JSONArray();
        for (Course course : candidateSchedule.getCourses()) { courseList.add(CourseToJSON(course)); }
        schedule.put("classes", courseList);

        // Formatting
        JSONObject shell = new JSONObject();
        shell.put(candidateSchedule.getName(), schedule);

        // Reading Existing Files
        FileReader sourceFile = new FileReader("backend/src/main/resources/private/userSchedules.json");
        JSONObject preExisting = new JSONObject();
        try { preExisting = (JSONObject) new JSONParser().parse(sourceFile); } catch (Exception e) { }
        try { preExisting.remove(candidateSchedule.getName()); } catch (Exception e) { }
        preExisting.put(candidateSchedule.getName(), shell);

        // Write to the File
        FileWriter newFile = new FileWriter("backend/src/main/resources/private/userSchedules.json");
        newFile.write(preExisting.toJSONString());
        newFile.close();
    }

    // Save Helper
    private static JSONObject CourseToJSON(Course course) {
        JSONObject toReturn = new JSONObject();
        toReturn.put("courseName",  course.getCourseName());
        toReturn.put("department",  course.getDepartment());
        toReturn.put("courseCode",  course.getCourseCode());
        toReturn.put("description", course.getDescription());

        JSONArray professors = new JSONArray();
        for (String professor : course.getProfessors()) { professors.add(professor); }
        toReturn.put("professors", professors);

        toReturn.put("credits",     course.getCredits());
        toReturn.put("days",        course.getDays());

        JSONArray startTimes = new JSONArray();
        for (int time : course.getStartTimes()) { startTimes.add(time); }
        toReturn.put("startTimes", startTimes);

        JSONArray duration = new JSONArray();
        for (int time : course.getDuration()) { duration.add(time); }
        toReturn.put("duration", duration);

        toReturn.put("semester",    course.getSemester());

        return toReturn;
    }

    // Loading Schedule from JSON
    public static void loadSchedule(String scheduleName) throws IOException, ParseException {
        newSchedule();

        // Legacy
        /* //Goes to the file in question and initializes a scanner
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
        } */

        // Finding Schedule
        FileReader sourceFile = new FileReader("backend/src/main/resources/private/userSchedules.json");
        JSONObject readIn = (JSONObject) ((JSONObject) new JSONParser().parse(sourceFile)).get(scheduleName);
        if (readIn == null) { return; }

        // Reading from Schedule
        JSONObject foundSchedule = (JSONObject) readIn.get(scheduleName);
        candidateSchedule.setName(foundSchedule.get("name").toString());
        for (Object course : (JSONArray) foundSchedule.get("classes")) { candidateSchedule.addCourse(JSONToCourse((JSONObject) course)); }
    }

    // Load Helper
    private static Course JSONToCourse(JSONObject course) {
        // Pre-Handling
        String[] professors = new String[((JSONArray) course.get("professors")).size()];
        int index = 0;
        for (Object professor : (JSONArray) course.get("professors")) { professors[index] = professor.toString(); index++; }

        int[] startTimes = new int[5];
        for (int i = 0; i < 5; i++) { startTimes[i] = Math.toIntExact((long) ((JSONArray) course.get("startTimes")).get(i)); }

        int[] duration = new int[5];
        for (int i = 0; i < 5; i++) { duration[i] = Math.toIntExact((long) ((JSONArray) course.get("duration")).get(i)); }

        // Constructor
        return new Course(
                course.get("courseName").toString(),
                course.get("department").toString(),
                course.get("courseCode").toString(),
                course.get("description").toString(),
                professors,
                Math.toIntExact((long) course.get("credits")),
                Math.toIntExact((long) course.get("days")),
                startTimes,
                duration,
                course.get("semester").toString()
        );
    }
}
