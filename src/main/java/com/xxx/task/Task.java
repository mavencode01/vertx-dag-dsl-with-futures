package com.xxx.task;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;

public abstract class Task {
    protected Vertx vertx;
    protected String name;
    protected JsonObject definition;
    protected Task next;

    public Task(Vertx vertx, JsonObject definition) {
        this(vertx, definition,null);
    }

    public Task(Vertx vertx, JsonObject definition, Task next) {
        this.vertx = vertx;
        this.name = definition.getString("name");
        this.definition = definition;
        this.next = next;
    }

    public Vertx getVertx() {
        return vertx;
    }

    public void setVertx(Vertx vertx) {
        this.vertx = vertx;
    }

    public String getName() {
        return name;
    }

    public JsonObject getDefinition() {
        return definition;
    }

    public void setDefinition(JsonObject definition) {
        this.definition = definition;
    }

    public void next(Task next) {
        this.next = next;
    }

    public abstract Future<JsonObject> run(JsonObject in);

    public Future<JsonObject> execute(JsonObject in) {
        Future<JsonObject> future = Future.future();

        if(next != null)
            run(in)
                    .compose(next::execute)
                    .setHandler(result -> {
                        if(result.succeeded()) {
                            future.complete(result.result());
                        } else {
                            future.fail(result.cause());
                        }
                    });
        else
            run(in).setHandler(result -> {
                if(result.succeeded()) {
                    future.complete(result.result());
                } else {
                    future.fail(result.cause());
                }
            });

        return future;
    }
}
