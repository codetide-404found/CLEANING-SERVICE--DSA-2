# Member 4 — Stack, Queue, Circular Queue & Deque

This folder contains the service scheduling and audit logging piece of the
campus cleaning dispatch system. It answers one question: once a
cleaning request comes in, what order does it get handled in, and how do we
keep a record of what happened.

## What is in here

CleaningRequest.java is the shared object that moves through every
structure below. It represents one job, holding an ID, a location, a
category, an urgency level, a submitted time and a status.

CustomQueue.java is a linked list based first in first out queue. It
represents the normal dispatch line, where whoever has been waiting longest
gets handled first.

CircularQueue.java is an array based queue with a fixed capacity. It
represents a bounded holding buffer, for example a dispatch board that only
has room for a set number of pending requests at once. When it fills up,
front and rear wrap back around to the start of the array instead of
running out of room.

CustomDeque.java is a doubly linked list based double ended queue. Routine
requests join the back like a normal queue, but an urgent request such as a
hazardous spill can be pushed straight to the front so it jumps the line.

CustomStack.java is a linked list based last in first out stack. It acts as
the undo and audit log. Every action the system takes gets pushed onto it,
so the most recent action is always the first one available to undo.

EmptyStructureException.java and StructureFullException.java are custom
exceptions thrown when an invalid operation is attempted, such as popping
an empty stack or enqueuing into a full circular queue.

Member4Demo.java runs straight through, top to bottom, and prints the
trace output for all four structures using realistic campus cleaning
examples. This is the evidence that shows front and rear movement in the
queues, an urgent insertion in the deque, and an undo log walkthrough in
the stack.

Member4Tests.java is a self contained test harness with no external
dependencies. It checks a normal case, a boundary case, and an invalid
input case for each of the four structures, and prints a pass or fail
result for every check.

## How to build and run

From inside this folder, compile everything with

    javac *.java

Then run the demo to see the trace output with

    java Member4Demo

Or run the tests to see the pass and fail results with

    java Member4Tests

Both should run without needing any external libraries, since none of the
four structures use java.util.Stack, java.util.Queue or java.util.ArrayDeque.
Everything is built from scratch using plain nodes, arrays and pointers.