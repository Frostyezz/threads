# Multi-Threaded Execution Simulator

## Overview

This project aims to develop a simulator for the simultaneous execution of multiple threads (processes). The simulation is not tied to the system clock, and time is abstracted into units. The processor and I/O devices operate independently, allowing for concurrent execution.

## Problem Description

- The simulation involves a processor that executes one operation per time unit.
- I/O devices perform one operation per time unit.
- The simulation can handle up to 26 processes, each with unique characteristics.
- Each process is defined by a name, an alias, start time, the number of phases, and a matrix of phase details.

## Process Execution

- Each process is represented by its alias.
- Processes start execution at a specified time and have a variable number of phases with unique behaviors.
- Each phase consists of CPU and I/O operations repeated a certain number of times.
- The execution of a process involves alternating between CPU and I/O operations in a cyclic manner.

## Priority Queues

- Processes are assigned to three priority queues: Q1, Q2, and Q3.
- Processes in Q1 have the highest priority, followed by Q2 and Q3.
- Processes are ordered alphabetically if they enter a queue simultaneously.
- Each queue has a specified time allocation (q1, q2, q3).
- Penalization and prioritization mechanisms based on predefined limits (PENALTY_LIMIT, AWARD_LIMIT).

## I/O Queue

- There is a single I/O queue for I/O execution.
- Processes in this queue are processed in the order of entry.
- A process occupies I/O until it completes the allocated time units.

## Input and Output

- Input data is read from a text file named "input".
- Output is generated in an HTML file named "output.htm," containing both input and output details.

---

**Note**: This project involves implementing a multi-threaded execution simulator based on the specified requirements. The simulator will read input data from a file, perform the simulation, and output the results in an HTML file.
