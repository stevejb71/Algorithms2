#!/usr/bin/env bash
java -cp lib/algs4.jar:target/scala-2.11/classes MoveToFront - < src/test/resources/burrows/abra.txt | java -cp lib/algs4.jar:target/scala-2.11/classes MoveToFront +