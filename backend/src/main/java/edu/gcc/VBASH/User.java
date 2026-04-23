package edu.gcc.VBASH;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.io.*;
import java.util.ArrayList;
import java.util.Objects;

/* DATA STRUCTURE */

/*
* JSONObject allUsers {
*   username : JSONObject User {
*       "username" : String username
*
*       "passwordHash" : int passwordHash
*
*       "schedules" : JSONObject schedules {
*           "name" : String (Schedule) name
*
*           "courses" : JSONArray courses {
*               N/A : JSONObject course
*           }
*       }
*
*       "takenCourses" : JSONArray takenCourses {
*           N/A : JSONObject takenCourse
*       }
*
*       "major" : String major
*   }
* }
*/

public class User {
    // Identifiers
    private static String username = "default";
    private static int passwordHash = 0;

    // User Data
    private static ArrayList<Course> takenCourses = new ArrayList<>();
    private static String major = "";
    private static ArrayList<Course> majorCourses = new ArrayList<>();

    // User Schedules
    private static ArrayList<Schedule> schedules = new ArrayList<Schedule>();
    private static Schedule candidateSchedule = new Schedule("default", new ArrayList<Course>(), "2025_Spring");

    // Create New User
    public static boolean createUser(String username, int passwordHash) throws IOException {
        // Reading Existing Files
        FileReader sourceFile = new FileReader("backend/src/main/resources/private/userSchedules.json");
        JSONObject allUsers = new JSONObject();
        try { allUsers = (JSONObject) new JSONParser().parse(sourceFile); } catch (Exception e) { }

        // Fail if User Already Exists
        if (allUsers.containsKey(username)) { return false; }

        // Add Basic User Values
        JSONObject newUser = new JSONObject();
        newUser.put("username", username);
        newUser.put("passwordHash", passwordHash);
        newUser.put("schedules", new JSONObject());
        newUser.put("takenCourses", new JSONArray());
        newUser.put("major", "");
        newUser.put("majorCourses", new JSONArray());

        // Add User to Data
        allUsers.put(username, newUser);

        // Write to the File
        FileWriter newFile = new FileWriter("backend/src/main/resources/private/userSchedules.json");
        newFile.write(allUsers.toJSONString());
        newFile.close();

        // Successful Creation
        return true;
    }


    // Load User Data
    public static boolean loadUser(String tryUsername, int tryPasswordHash) throws IOException, ParseException {
        // Reading Existing Files
        FileReader sourceFile = new FileReader("backend/src/main/resources/private/userSchedules.json");
        JSONObject allUsers = new JSONObject();
        try { allUsers = (JSONObject) new JSONParser().parse(sourceFile); } catch (Exception e) { }

        // No Such User
        if (!allUsers.containsKey(tryUsername)) { return false; }

        // Load User Data
        JSONObject targetUser = (JSONObject) allUsers.get(tryUsername);

        // Incorrect Password
        if ((int) targetUser.get("passwordHash") != tryPasswordHash) { return false; }

        // Load Username
        username = tryUsername;

        // Load Password Hash;
        passwordHash = tryPasswordHash;

        // Load Schedules
        JSONObject JSONSchedules = (JSONObject) targetUser.get("schedules");
        schedules = new ArrayList<Schedule>();
        for (Object scheduleName : JSONSchedules.keySet()) { schedules.add(JSONToSchedule((JSONObject) JSONSchedules.get(scheduleName))); }

        if (schedules.isEmpty()) { newCandidateSchedule(); }
        else { candidateSchedule = schedules.getFirst(); }

        // Load Taken Courses
        takenCourses = new ArrayList<Course>();
        for (Object takenCourse : (JSONArray) targetUser.get("takenCourses")) { takenCourses.add(JSONToCourse((JSONObject) takenCourse)); }

        // Load Major
        major = targetUser.get("major").toString();

        // Load Major Courses
        majorCourses = new ArrayList<Course>();
        for (Object majorCourse : (JSONArray) targetUser.get("majorCourses")) { majorCourses.add(JSONToCourse((JSONObject) majorCourse)); }


        // Successful Load
        return true;
    }

    // JSON to Schedule
    public static Schedule JSONToSchedule(JSONObject JSONSchedule) {
        Schedule toReturn = new Schedule();

        // Load Schedule Name
        toReturn.setName(JSONSchedule.get("name").toString());

        // Load Schedule Courses
        for (Object course : (JSONArray) JSONSchedule.get("courses")) { toReturn.addCourse(JSONToCourse((JSONObject) course)); }

        // Return Schedule
        return toReturn;
    }

    // JSON to Course
    public static Course JSONToCourse(JSONObject course) {
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


    // Saving User Data
    public static void saveUserData() throws IOException {
        // Reading Existing Files
        FileReader sourceFile = new FileReader("backend/src/main/resources/private/userSchedules.json");
        JSONObject allUsers = new JSONObject();
        try { allUsers = (JSONObject) new JSONParser().parse(sourceFile); } catch (Exception e) { }

        // Create User if They Don't Exist
        if (!allUsers.containsKey(username)) { createUser(username, passwordHash); }

        // Load Existing User Values
        JSONObject user = (JSONObject) allUsers.get(username);

        // Add Schedules
        JSONObject JSONSchedules = new JSONObject();
        for (Schedule schedule : schedules) { JSONSchedules.put(schedule.getName(), ScheduleToJSON(schedule)); }
        user.put("schedules", JSONSchedules);

        // Add Taken Courses
        JSONArray JSONTakenCourses = new JSONArray();
        for (Course takenCourse : takenCourses) { JSONTakenCourses.add(CourseToJSON(takenCourse)); }
        user.put("takenCourses", JSONTakenCourses);

        // Add Major
        user.put("major", major);

        // Add User
        allUsers.replace(username, user);

        // Write to the File
        FileWriter newFile = new FileWriter("backend/src/main/resources/private/userSchedules.json");
        newFile.write(allUsers.toJSONString());
        newFile.close();
    }

    // Schedule to JSON
    public static JSONObject ScheduleToJSON(Schedule schedule) {
        JSONObject toReturn = new JSONObject();

        // Add Name
        toReturn.put("name", schedule.getName());

        // Add Courses
        JSONArray classes = new JSONArray();
        for (Course course : schedule.getCourses()) { classes.add(CourseToJSON(course)); }

        // Return Schedule
        return toReturn;
    }

    // Course to JSON
    public static JSONObject CourseToJSON(Course course) {
        JSONObject toReturn = new JSONObject();

        // Add Values
        toReturn.put("courseName", course.getCourseName());
        toReturn.put("department", course.getDepartment());
        toReturn.put("courseCode", course.getCourseCode());
        toReturn.put("description", course.getDescription());

        JSONArray professors = new JSONArray();
        for (String professor : course.getProfessors()) { professors.add(professor); }
        toReturn.put("professors", professors);

        toReturn.put("credits", course.getCredits());
        toReturn.put("days", course.getDays());

        JSONArray startTimes = new JSONArray();
        for (int time : course.getStartTimes()) { startTimes.add(time); }
        toReturn.put("startTimes", startTimes);

        JSONArray duration = new JSONArray();
        for (int time : course.getDuration()) { duration.add(time); }
        toReturn.put("duration", duration);

        toReturn.put("semester", course.getSemester());

        return toReturn;
    }


    // Retrieving User Data
    public static JSONObject getUserData() {
        JSONObject toReturn = new JSONObject();

        toReturn.put("username", username);
        toReturn.put("takenCourses", takenCourses);
        toReturn.put("major", major);

        return toReturn;
    }


    // Add Taken Course
    public static void addTakenCourse(Course newCourse) { takenCourses.add(newCourse); }

    public static void addTakenCourse(String newCourseName) { takenCourses.add(new Course(newCourseName)); }

    // Remove Taken Course
    public static void removeTakenCourse(Course courseToRemove) { takenCourses.removeIf(course -> Objects.equals(course.getCourseName(), courseToRemove.getCourseName())); }

    public static void removeTakenCourse(String courseName) { takenCourses.removeIf(course -> Objects.equals(course.getCourseName(), courseName)); }

    // Check if Taken
    public static boolean isTakenCourse(Course course) { return takenCourses.contains(course); }


    // Set Major
    public static void setMajor(String newMajor) { major = newMajor; }

    // Check if Major Requirement
    public static boolean isMajorRequirement(Course course) { return majorCourses.contains(course); }


    // Load Major Course
    public static void loadMajor() throws IOException, ParseException {
        // Read-In Courses
        FileReader sourceFile = new FileReader("backend/src/main/resources/private/majorCourses.json");
        JSONArray JSONMajorCourses = (JSONArray) ((JSONObject) new JSONParser().parse(sourceFile)).get(major);

        majorCourses = new ArrayList<Course>();
        for (Object majorCourse : JSONMajorCourses) { majorCourses.add(JSONToCourse((JSONObject) majorCourse)); }
    }


    // Retrieving, Deleting, and Saving Schedules
    public static Schedule getCurrentSchedule() { return candidateSchedule; }

    public static ArrayList<Schedule> getSchedules() { return schedules; }

    public static void loadSchedule(String scheduleName) { for (Schedule schedule : schedules) { if (schedule.getName().equals(scheduleName)) { candidateSchedule = schedule; } } }

    public static void newCandidateSchedule() { candidateSchedule = new Schedule("default", new ArrayList<Course>(), "2025_Fall"); }

    public static void deleteSchedule() {
        schedules.remove(candidateSchedule);
        newCandidateSchedule();
    }

    // Legacy
    /*
    // Saving Schedule in JSON
    public static void saveSchedule() throws IOException {
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
        }

        // Adding Schedule Values
        JSONObject schedule = new JSONObject();
        schedule.put("name", candidateSchedule.getName());

        JSONArray courseList = new JSONArray();
        for (Course course : candidateSchedule.getCourses()) { courseList.add(CourseToJSON(course)); }
        schedule.put("classes", courseList);

        // Reading Existing Files
        FileReader sourceFile = new FileReader("backend/src/main/resources/private/userSchedules.json");
        JSONObject preExistingUsers = new JSONObject();
        JSONObject preExistingUserData = new JSONObject();
        JSONObject preExistingUserSchedules = new JSONObject();

        // Obtain References if They Exist
        try { preExistingUsers = (JSONObject) new JSONParser().parse(sourceFile); } catch (Exception e) { }
        try { preExistingUserData = (JSONObject) preExistingUsers.get(username); } catch (Exception e) { }
        try { preExistingUserSchedules = (JSONObject) preExistingUserData.get("schedule"); } catch (Exception e) { }

        // Write Data
        preExistingUserSchedules.replace(candidateSchedule.getName(), schedule);
        preExistingUserData.replace("schedule", preExistingUserSchedules);
        preExistingUsers.replace(username, preExistingUserData);

        // Write to the File
        FileWriter newFile = new FileWriter("backend/src/main/resources/private/userSchedules.json");
        newFile.write(preExistingUsers.toJSONString());
        newFile.close();
    }

    // Loading Schedule from JSON
    public static void loadSchedule(String scheduleName) throws IOException, ParseException {
        newCandidateSchedule();

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
        }

        // Finding Specific Schedule
        FileReader sourceFile = new FileReader("backend/src/main/resources/private/userSchedules.json");
        JSONObject preExistingUsers = (JSONObject) new JSONParser().parse(sourceFile);
        JSONObject preExistingUserData = (JSONObject) preExistingUsers.get(username);
        JSONObject preExistingUserSchedules = (JSONObject) preExistingUserData.get("schedule");
        JSONObject preExistingUserSchedule = (JSONObject) preExistingUserSchedules.get(scheduleName);
        if (preExistingUserSchedule == null) { return; }

        // Reading from Schedule
        candidateSchedule.setName(preExistingUserSchedule.get("name").toString());
        for (Object course : (JSONArray) preExistingUserSchedule.get("classes")) { candidateSchedule.addCourse(JSONToCourse((JSONObject) course)); }
    }
    */
}