package com.xxx;

import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MyCompositeTimedTask implements MyTask {

    Vertx vertx;
    String name;

    List<MyTask> tasks;

    Future<JsonObject> future;
    CompositeFuture compositeFuture;
    MyTask next;

    public MyCompositeTimedTask(Vertx vertx, String name) {
        this.vertx = vertx;
        this.name = name;

        tasks = new ArrayList<>();

        this.future = Future.future();
    }

    public void add(MyTask... tasks){
        this.tasks.addAll(Arrays.asList(tasks));

        List<Future> futures = new ArrayList<>();

        for(MyTask t : tasks) {
            futures.add(t.future());
        }

        compositeFuture = CompositeFuture.all(futures);
    }

    @Override
    public Future<JsonObject> future() {

        if(future == null)
            future = Future.future();

        return future;
    }

    @Override
    public MyTask next(MyTask next) {
        this.next = next;

        return next;
    }

    @Override
    public Future<JsonObject> execute(JsonObject j) {
        System.out.println("Tasking(" + name + "), Previous Task Result: " + j.encode());

        for(MyTask t: tasks) {
            t.execute(j);
        }

        compositeFuture.setHandler(event -> {
            if(event.succeeded()) {
                future.complete(new JsonObject().put("completed", name));
            } else {
                future.fail("(" + name + ") failed!");
            }
        });

        if(next != null) {
            return future.compose(next::execute);
        } else {
            return future;
        }
    }
}
