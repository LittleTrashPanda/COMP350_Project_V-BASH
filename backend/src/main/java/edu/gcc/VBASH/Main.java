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
    }

    public void run(){ return; }
}
