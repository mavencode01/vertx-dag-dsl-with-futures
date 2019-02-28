package com.xxx.task.impl;

import com.xxx.task.Task;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;

public class SleepTask extends Task {
    private Long delay;

    public SleepTask(Vertx vertx, JsonObject definition) {
        this(vertx, definition, null);
    }

    public SleepTask(Vertx vertx, JsonObject definition, Task next) {
        super(vertx, definition, next);

        this.delay = this.definition.getJsonObject("config").getLong("delay");
    }

    @Override
    public Future<JsonObject> run(JsonObject previousJobOutput) {
        System.out.println("Task: " + name + ", Previous job output: " + previousJobOutput.encode());
        System.out.println("Task: " + name + ", Definition: " + definition.encode());
        System.out.println("Task: " + name + ", Delay: " + delay);

        final Future<JsonObject> future = Future.future();

        getVertx().setTimer(delay, l -> {
            System.out.println("Task: " + name + ", Completed!");
            future.complete(new JsonObject().put("task", name).put("delayed", delay));
        });

        return future;
    }
}
