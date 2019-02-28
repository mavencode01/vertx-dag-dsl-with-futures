# vertx-dag-dsl-with-futures
Prototype dsl to build a dag using vertx.io futures.

This an early attempt to build some sort of DSL to build a "DAG" of vertx.io futures. You can create simple sequential flows or complex execution trees.

Example dag:

![Flow](
https://raw.githubusercontent.com/javadevmtl/vertx-dag-dsl-with-futures/master/flow.png)

To create your own tasks simply extend the Task class and implement the abstract run() method. As an example see the SleepTask 
