package com.xxx;

import com.xxx.task.impl.ParallelCompositeTask;
import com.xxx.task.impl.SleepTask;
import com.xxx.task.Task;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;

public class Main extends AbstractVerticle {

    public void start(Future<Void> start) {
        Task task01 = new SleepTask(vertx, new JsonObject().put("name", "task01").put("config", new JsonObject().put("delay", 1000)));
        Task task02 = new SleepTask(vertx, new JsonObject().put("name", "task02").put("config", new JsonObject().put("delay", 1000)));
        Task task03 = new SleepTask(vertx, new JsonObject().put("name", "task03").put("config", new JsonObject().put("delay", 1000)));
        task01.next(task02);
        task02.next(task03);

        Task c1Task01 = new SleepTask(vertx, new JsonObject().put("name", "c1Task01").put("config", new JsonObject().put("delay", 10000)));
        Task c1Task02 = new SleepTask(vertx, new JsonObject().put("name", "c1Task02").put("config", new JsonObject().put("delay", 10000)));
        Task c1Task03a = new SleepTask(vertx, new JsonObject().put("name", "c1Task03a").put("config", new JsonObject().put("delay", 3000)));
        Task c1Task03b = new SleepTask(vertx, new JsonObject().put("name", "c1Task03b").put("config", new JsonObject().put("delay", 3000)));
        Task c1Task03c = new SleepTask(vertx, new JsonObject().put("name", "c1Task03c").put("config", new JsonObject().put("delay", 3000)));
        c1Task03a.next(c1Task03b);
        c1Task03b.next(c1Task03c);

        Task c2Task01a = new SleepTask(vertx, new JsonObject().put("name", "c2Task01a").put("config", new JsonObject().put("delay", 10000)));
        Task c2Task01b = new SleepTask(vertx, new JsonObject().put("name", "c2Task01b").put("config", new JsonObject().put("delay", 10000)));
        Task c2Task02 = new SleepTask(vertx, new JsonObject().put("name", "c2Task02").put("config", new JsonObject().put("delay", 3000)));

        c2Task01a.next(c2Task01b);

        ParallelCompositeTask composite01 = new ParallelCompositeTask(vertx, new JsonObject().put("name", "composite01"));
        ParallelCompositeTask composite02 = new ParallelCompositeTask(vertx, new JsonObject().put("name", "composite02"));

        composite02.add(c2Task01a, c2Task02);
        composite01.add(c1Task01, c1Task02, c1Task03a, composite02);

        Task task04 = new SleepTask(vertx, new JsonObject().put("name", "task04").put("config", new JsonObject().put("delay", 1000)));
        composite01.next(task04);

        task03.next(composite01);

        task01.execute(new JsonObject()).setHandler(result -> {
            if(result.succeeded()) {
                System.out.println("Result: " + result.result().encode());
            } else {
                result.cause().printStackTrace();
            }
        });
    }
}
