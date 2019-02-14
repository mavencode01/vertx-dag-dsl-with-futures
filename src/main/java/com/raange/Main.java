package com.raange;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;


public class Main extends AbstractVerticle {

    public void start(Future<Void> start) {
        System.out.println(config().encode());

        JsonArray tasks = config().getJsonArray("tasks");

        boolean startSet = false;
        MyTimedTask startt = new MyTimedTask(vertx, "start", new JsonObject(), 1L, false);;
        MyTask next = new MyTimedTask(vertx, "next", new JsonObject(), 1L, false);;

        MyTimedTask ptask1 = new MyTimedTask(vertx, "ptask1", new JsonObject().put("name", "ptask1"), 3000L, false);
        MyTimedTask ptask2 = new MyTimedTask(vertx, "ptask2", new JsonObject().put("name", "ptask2"), 3000L, false);
        MyTimedTask ptask3 = new MyTimedTask(vertx, "ptask3", new JsonObject().put("name", "ptask3"), 3000L, false);
        MyTimedTask ptask4 = new MyTimedTask(vertx, "ptask4", new JsonObject().put("name", "ptask4"), 6000L, false);
        MyTimedTask ptask5 = new MyTimedTask(vertx, "ptask5", new JsonObject().put("name", "ptask5"), 6000L, false);

        MyCompositeTimedTask compositeTask2 = new MyCompositeTimedTask(vertx, "parallel2");
        compositeTask2.add(ptask4, ptask5);

        MyCompositeTimedTask compositeTask1 = new MyCompositeTimedTask(vertx, "parallel1");
        compositeTask1.add(ptask1, ptask2, ptask3);

        for(int i =0; i < tasks.size(); i++) {
            JsonObject task = tasks.getJsonObject(i);

            MyTimedTask taskk = new MyTimedTask(vertx, task.getString("name"), task, task.getLong("sleepTime"), task.getBoolean("fail"));

            if(!startSet) {
                startSet = true;

                next = startt.next(taskk);

                // We do this here just see parallel tasks execute and complete before the sequence continues.
                next = next.next(compositeTask1);
                next = next.next(compositeTask2);
            } else {
                next = next.next(taskk);
            }
        }

        startt.execute(new JsonObject());


        // Have to see why this does not execute...
        next.future().setHandler(event -> {
            if(event.succeeded())
                System.out.println("Result: " + event.result().encodePrettily());
            else
                event.cause().printStackTrace();
        });
    }
}
