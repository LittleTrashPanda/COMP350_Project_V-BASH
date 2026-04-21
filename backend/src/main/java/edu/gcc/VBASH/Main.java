package edu.gcc.VBASH;

import io.javalin.Javalin;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        Javalin app = Javalin.create(config -> { config.staticFiles.add("public"); }).start(7000);
        registerSearch(app);
    }

    public static void registerSearch(Javalin app) {
        // Search Courses
        app.get("/search", ctx -> ctx.json(Search.search()));

        // Set Key Search Terms
        app.post("/keySearchTerms", ctx -> {
            ArrayList<String> keySearchTerms = new ArrayList<>();
            for (String keySearchTerm : ctx.body().split("\s+")) { keySearchTerms.add(keySearchTerm); }

            Search.SetKeySearchTerms(keySearchTerms);
            ctx.status(201);
        });

        // Set Search Filters
        app.post("/setFilters", ctx -> {
            Filter providedFilter = ctx.bodyAsClass(Filter.class);
            Search.setFilter(providedFilter);
            ctx.status(201);
        });


        // Retrieve Open Schedule
        app.get("/loadCalendar", ctx -> ctx.json(User.getCurrentSchedule().getCurrentCourses()));

        // Set Semester for Open Schedule
        app.post("/setCurrentSemester", ctx -> User.getCurrentSchedule().setSemester(ctx.body()));


        // Add Course to Schedule
        app.post("/addCourse", ctx -> {
            Course toAdd = ctx.bodyAsClass(Course.class);

            //Detect "no meeting times"
            if (toAdd.getDays() == 1) {
                ctx.json(new AddResult(false, "This course has no scheduled meeting times."));
                return;
            }
            //Detect time conflict
            if (User.getCurrentSchedule().checkCourseConflict(toAdd)) {
                ArrayList<Course> conflicts = new  ArrayList<Course>();
                for(Course course : User.getCurrentSchedule().getCourses()){
                    if(course.willConflict(toAdd)){
                        conflicts.add(course);
                    }
                }
                ctx.json(new AddResult(false,
                        "This course conflicts with an existing course in your schedule.", conflicts));
                return;
            }

            //If no conflict add the course
            User.getCurrentSchedule().addCourse(toAdd);
            ctx.json(new AddResult(true, "Course added successfully."));
        });

        // Remove Course from Schedule
        app.post("/removeCourse", ctx -> {
            Course toRemove = ctx.bodyAsClass(Course.class);

            int before = ((ArrayList<Course>) User.getCurrentSchedule().getCourses()).size();
            User.getCurrentSchedule().removeCourse(toRemove);
            int after = ((ArrayList<Course>) User.getCurrentSchedule().getCourses()).size();

            if (before == after) {
                ctx.json(new AddResult(false, "Course was not found in your schedule."));
            } else {
                ctx.json(new AddResult(true, "Course removed from schedule."));
            }
        });

        // Replace Course in Schedule
        app.post("/replaceCourse", ctx -> {
            Course toAdd = ctx.bodyAsClass(Course.class);
            ArrayList<Course> conflicts = new ArrayList<Course>();

            for(Course course : User.getCurrentSchedule().getCourses()){
                if(course.willConflict(toAdd)){
                    conflicts.add(course);
                }
            }

            User.getCurrentSchedule().replaceCourse(toAdd, conflicts);
            ctx.json(new AddResult(true, "Course replaced successfully."));
        });


        // Create a New Schedule
        app.post("/newSchedule", ctx -> {
            User.saveUserData();
            User.newCandidateSchedule();
        });

        // Name the Open Schedule
        app.post("/nameCurrentSchedule", ctx -> { User.getCurrentSchedule().setName(ctx.body()); });

        // Save the Open Schedule
        app.post("/saveSchedule", ctx -> { User.saveUserData(); });

        // Load a Saved Schedule
        app.post("/loadSchedule", ctx -> {
            User.saveUserData();
            User.loadSchedule(ctx.body());
        });

        // Delete the Open Schedule
        app.post("/deleteSchedule", ctx -> { User.deleteSchedule();});


        // Retrieve User Data
        app.get("/userData", ctx -> ctx.json(User.getUserData()));


        // Add a Taken Course
        app.post("/takenCourse", ctx -> { User.addTakenCourse(ctx.body()); });

        // Remove a Taken Course
        app.post("/notTakenCourse", ctx -> { User.removeTakenCourse(ctx.body()); });


        // Set User's Major
        app.post("/major", ctx -> { User.setMajor(ctx.body()); });
    }
}
