package com.xxx;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;

public class MyTimedTask implements MyTask {

    Vertx vertx;
    String name;
    JsonObject j;
    long sleepTime;
    boolean fail;

    Future<JsonObject> future;
    MyTask next;

    public MyTimedTask(Vertx vertx, String name, JsonObject j, long sleepTime, boolean fail) {
        this.vertx = vertx;
        this.name = name;
        this.j = j;
        this.sleepTime = sleepTime;
        this.fail = fail;
        this.future = Future.future();
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
        vertx.setTimer(sleepTime, l -> {
            System.out.println("Tasking(" + name + "), Current Task input: " + this.j.encode() +", Previous Task Result: " + j.encode());

            if(!fail) {
                future.complete(new JsonObject().put("completed", name));
            }
            else {
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
