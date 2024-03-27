# Thread Report

> ⚠️ The screenshots are NOT up-to-date and may be missing.

## 🧵 Threads stats

+ ALL threads
+ Application threads
+ System threads

![](1-stats.png)

## 😸 Tomcat

If Tomcat is detected, there will be a section with Tomcat stats: for all Tomcat threads, and only for **Tomcat executor pool**.

## 😈 Daemon stats

![](2-daemon.png)

## 🧹 GC stats

![](3-gc.png)

## 🏊‍♂️ Pools

Resolve thread pools from similar thread names.

![](10-thread-pools.png)

## 🛑 Blocks

Analysis of all the blocks in the thread dump.
Finds locks and all the threads that are waiting for them.

![](4-blocks-a.png)

For each lock generates a report and  a graph like this (redacted for privacy):

![](4-blocks-b.png)

## 🔥 Flamegraph

Interactive flamegraph of the thread stack traces.

![](5-flamegraph.png)

## ☎️ Call Tree

![](6-calltree.png)

## 🚨 Exceptions

All captured exceptions.

![](7-exceptions.png)

## 🍪 CPU consuming threads

Sort threads by CPU consumption.

![](8-cpu.png)

## 🪞 Threads with identical stack trace

![](9-identical.png)

## 🔍 List of missing locks
