package edu.gcc.VBASH;

import io.javalin.Javalin;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        Javalin app = Javalin.create(config -> { config.staticFiles.add("public"); }).start(7000);
        registerSearch(app);
    }

    public static void registerSearch(Javalin app) {
        app.get("/search", ctx -> ctx.json(Search.search()));

        app.get("/getTimes", ctx -> {
            ctx.json(User.getTimes());
        });

        app.post("/keySearchTerms", ctx -> {
            ArrayList<String> keySearchTerms = new ArrayList<>();
            for (String keySearchTerm : ctx.body().split("\s+")) { keySearchTerms.add(keySearchTerm); }

            Search.SetKeySearchTerms(keySearchTerms);
            ctx.status(201);
        });

        app.post("/setFilters", ctx -> {
            Filter providedFilter = ctx.bodyAsClass(Filter.class);
            Search.setFilter(providedFilter);
            ctx.status(201);
        });

        app.get("/loadCalendar", ctx -> ctx.json(User.getSchedule().getCourses()));

        app.post("/addCourse", ctx -> {
            Course toAdd = ctx.bodyAsClass(Course.class);

            //Detect "no meeting times"
            if (toAdd.getDays() == 1) {
                ctx.json(new AddResult(false, "This course has no scheduled meeting times."));
                return;
            }
            //Detect time conflict
            if (User.getSchedule().checkCourseConflict(toAdd)) {
                ArrayList<Course> conflicts = new  ArrayList<Course>();

                for(Course course : User.getSchedule().getCourses()){
                    if(course.willConflict(toAdd)){
                        conflicts.add(course);
                    }
                }

                ctx.json(new AddResult(false,
                        "This course conflicts with an existing course in your schedule.", conflicts));
                return;
            }

            //If no conflict add the course
            User.getSchedule().addCourse(toAdd);
            ctx.json(new AddResult(true, "Course added successfully."));
        });

        app.post("/removeCourse", ctx -> {
            Course toRemove = ctx.bodyAsClass(Course.class);

            int before = ((ArrayList<Course>) User.getSchedule().getCourses()).size();
            User.getSchedule().removeCourse(toRemove);
            int after = ((ArrayList<Course>) User.getSchedule().getCourses()).size();

            if (before == after) {
                ctx.json(new AddResult(false, "Course was not found in your schedule."));
            } else {
                ctx.json(new AddResult(true, "Course removed from schedule."));
            }
        });

        app.post("/replaceCourse", ctx -> {
            Course toAdd = ctx.bodyAsClass(Course.class);
            ArrayList<Course> conflicts = new ArrayList<Course>();

            for(Course course : User.getSchedule().getCourses()){
                if(course.willConflict(toAdd)){
                    conflicts.add(course);
                }
            }

            User.getSchedule().replaceCourse(toAdd, conflicts);
            ctx.json(new AddResult(true, "Course replaced successfully."));
        });

        // Naming the Open Schedule
        app.post("/nameCurrentSchedule", ctx -> { User.getSchedule().setName(ctx.body()); });

        // Saving the Open Schedule
        app.post("/saveSchedule", ctx -> { User.saveSchedule(); });

        // Load Schedule
        app.post("/loadSchedule", ctx -> { User.loadSchedule(ctx.body()); });

        app.post("/resetSchedule", ctx -> { User.resetSchedule();});
    }
}
