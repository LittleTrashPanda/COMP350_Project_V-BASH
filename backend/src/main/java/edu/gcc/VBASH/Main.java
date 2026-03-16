package edu.gcc.VBASH;

import io.javalin.Javalin;

import java.util.ArrayList;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        Javalin app = Javalin.create(config -> { config.staticFiles.add("public"); }).start(7000);
        registerSearch(app);
    }

    public static void registerSearch(Javalin app) {
        app.get("/health", ctx -> ctx.json(Map.of("status", "ok")));
        app.get("/search", ctx -> ctx.json(Search.search()));
        app.get("/calendar", ctx -> ctx.json(Schedule.getCourses()));

        app.post("/keySearchTerms", ctx -> {
            String[] keySearchTerms = ctx.body().split("\s+");
            Search.SetKeySearchTerms(keySearchTerms);
            ctx.status(201);
        });
        app.post("/addCourse", ctx -> {
            Course c = ctx.bodyAsClass(Course.class);

            Schedule schedule = new Schedule("default", Schedule.getCourses());
            //Detect "no meeting times"
            if (c.getDays() == 1) {
                ctx.json(new AddResult(false,
                        "This course has no scheduled meeting times."));
                return;
            }
            //Detect time conflict
            if (schedule.checkCourseConflict(c)) {
                ArrayList<Course> conflicts = new  ArrayList<Course>();

                for(Course course : schedule.getCourses()){
                    if(schedule.checkCourseConflict(course)){
                        conflicts.add(course);
                    }
                }

                ctx.json(new AddResult(false,
                        "This course conflicts with an existing course in your schedule.", conflicts));
                return;
            }
            //If no conflict add the course
            schedule.addCourse(c);
            ctx.json(new AddResult(true, "Course added successfully."));
        });

        app.post("/removeCourse", ctx -> {
            Course c = ctx.bodyAsClass(Course.class);

            Schedule schedule = new Schedule("default", Schedule.getCourses());

            int before = ((ArrayList<Course>) Schedule.getCourses()).size();
            schedule.removeCourse(c);
            int after = ((ArrayList<Course>) Schedule.getCourses()).size();

            if (before == after) {
                ctx.json(new AddResult(false, "Course was not found in your schedule."));
            } else {
                ctx.json(new AddResult(true, "Course removed from schedule."));
            }
        });

        app.post("/replaceCourse", ctx -> {
            Schedule schedule = new Schedule("default", Schedule.getCourses());
            Course toAdd = ctx.bodyAsClass(Course.class);
            ArrayList<Course> conflicts = new ArrayList<Course>();

            for(Course course : schedule.getCourses()){
                if(schedule.checkCourseConflict(course)){
                    conflicts.add(course);
                }
            }

            int before =  ((ArrayList<Course>) Schedule.getCourses()).size();
            schedule.replaceCourse(toAdd, conflicts);
            int after =  ((ArrayList<Course>) Schedule.getCourses()).size();

            if (before == after) {
                ctx.json(new AddResult(false, "Course was not found in your schedule."));
            } else {
                ctx.json(new AddResult(true, "Course removed from schedule."));
            }
        });


    }

    public void run(){ return; }
}
