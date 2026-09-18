#!/usr/bin/env bash
set -e
javac -cp "lib/junit-4.13.2.jar:lib/hamcrest-core-1.3.jar" -d bin src/*.java
java -cp "bin:lib/junit-4.13.2.jar:lib/hamcrest-core-1.3.jar" org.junit.runner.JUnitCore BigNumTest