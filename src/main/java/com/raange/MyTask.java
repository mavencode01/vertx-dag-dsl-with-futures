package com.raange;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;

public interface MyTask {

    Future<JsonObject> future();

    MyTask next(MyTask next);

    Future<JsonObject> execute(JsonObject j);
}
