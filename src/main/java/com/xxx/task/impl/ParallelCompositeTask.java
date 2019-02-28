package com.xxx.task.impl;

import com.xxx.task.Task;
import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ParallelCompositeTask extends Task {
    private List<Task> tasks;

    public ParallelCompositeTask(Vertx vertx, JsonObject definition) {
        this(vertx, definition, null);
    }

    public ParallelCompositeTask(Vertx vertx, JsonObject definition, Task next) {
        super(vertx, definition, next);
        tasks = new ArrayList<>();
    }

    public void add(Task... tasks){
        this.tasks.addAll(Arrays.asList(tasks));
    }

    @Override
    public Future<JsonObject> run(JsonObject in) {
        System.out.println("Task: " + name + ", Previous job output: " + in.encode());

        Future<JsonObject> future = Future.future();
        List<Future> futures = new ArrayList<>();

        for(Task t: tasks) {
            futures.add(t.execute(in));
        }

        CompositeFuture.all(futures).setHandler(result -> {
            if(result.succeeded()) {
                JsonArray results = new JsonArray();

                for(Object o : result.result().list()) {
                    results.add((JsonObject)o);
                }
                System.out.println("Task: " + name + ", Completed!");

                future.complete(new JsonObject().put("results", results));
            } else {
                future.fail(result.cause());
            }
        });

        return future;
    }
}
