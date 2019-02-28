# vertx-dag-dsl-with-futures
Prototype dsl to build a dag using vertx.io futures.

This an early attempt to build some sort of DSL to build a "DAG"/flow using vertx.io futures. You can create simple sequential flows or complex execution trees.

Example flow:

![Flow](
https://raw.githubusercontent.com/javadevmtl/vertx-dag-dsl-with-futures/master/flow.png)

To create your own tasks simply extend the **Task** class and implement the abstract **run()** method. As an example see the SleepTask.

The tasks do not have to deal with any composition. Simply implement the **run()** method. The task **execute()** method will traverse the tree and call **run()**

## Simple Task Implementation

```
/**
 * This is a simple task that does nothing and completes immediately.
 */
public class SimpleTask extends Task {
    public SimpleTask(Vertx vertx, JsonObject definition) {
        this(vertx, definition, null);
    }

    public SimpleTask(Vertx vertx, JsonObject definition, Task next) {
        super(vertx, definition, next);

        this.delay = this.definition.getJsonObject("config").getLong("delay");
    }

    @Override
    public Future<JsonObject> run(JsonObject previousJobOutput) {
        final Future<JsonObject> future = Future.future();

        future.complete(new jsonObject().put("task", name).put("status", "done"));

        return future;
    }
}
```
