# vertx-dag-dsl-with-futures
Prototype dsl to build a dag using vertx.io futures.

This an early attempt to build some sort of DSL to build a "DAG" of vertx.io futures. Currently you can create a sequential list of tasks using a JSON array and they will execute in sequence. You can also manually inject composite parallel tasks.


![Dag](
https://raw.githubusercontent.com/javadevmtl/vertx-dag-dsl-with-futures/master/dag.png)
