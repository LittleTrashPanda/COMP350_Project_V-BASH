package edu.gcc.VBASH;

import io.javalin.Javalin;

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

            if (schedule.checkCourseConflict(c)) {
                ctx.json(new AddResult(false,
                        "Course cannot be added (conflict or no meeting times)."));
                return;
            }

            schedule.addCourse(c);
            ctx.json(new AddResult(true, "Course added successfully."));
        });

    }
    public boolean filterTime(int[] startTimesInt){
        //run through each course in the list of courses, check if that course's time match the given time
        //display the courses that meet this
        //for each loop? if course.getStartTimes()

        return true;
    }

    public boolean filterDay(int daysInt){
        //run through each couse in the list of coursed, check if each course's days match with given day
        //display the courses that meet this


        return true;
    }
    public void run(){ return; }
}
