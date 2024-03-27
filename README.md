# Thread Dump Viewer

🚀 **Offline** Thread Dump Analyzer.

Disclaimer: While I am making this for my own use, I will _try_ to fix reported issues, as much time allows me to 🤷‍♂️.

☕️ You can still [buy me a coffee](https://www.buymeacoffee.com/oblac) :)

## Usage

Download the fat jar and run it:

```shell
java -jar tdv-X.Y.Z-all.jar <thread-dump-file>[.gz]
```

The input may be a 1) raw thread dump or 2) a _gzipped_ thread dump.

The report will be generated in the `out` subfolder as HTML file.
It is a huge report, and it is not beautiful, but it is functional.

## TDV Report

The report will contain the following sections:

+ [Thread Stats](doc/report.md#-threads-stats) - general stats about threads (all, application, system)
+ [Tomcat](doc/report.md#-tomcat) - Tomcat stats (if detected)
+ [Daemon Stats](doc/report.md#-daemon-stats) - stats about daemon threads
+ [GC Stats](doc/report.md#-gc-stats) - stats about garbage collection
+ [Pools](doc/report.md#-pools) - thread pools
+ [Blocks](doc/report.md#-blocks) - blocks analysis
+ [Flamegraph](doc/report.md#-flamegraph) - interactive flamegraph
+ [Call Tree](doc/report.md#-call-tree) - call tree
+ [Exceptions](doc/report.md#-exceptions) - all captured exceptions
+ [CPU consuming threads](doc/report.md#-cpu-consuming-threads) - threads sorted by CPU consumption
+ [Threads with identical stack trace](doc/report.md#-threads-with-identical-stack-trace) - threads
+ [List of missing locks](doc/report.md#-list-of-missing-locks) - locks that are not released


## TODO

+ [X] Stats per thread pool
+ [ ] Deadlocks detection (as soon as I get a thread dump with a deadlock)
+ [ ] Detect complex deadlocks (e.g. A -> B -> C -> A)
+ [ ] Thread histogram per priorities
+ [ ] Latest executed methods?]
