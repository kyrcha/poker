#!/bin/sh
javac -sourcepath "/src/info/kyrcha/tiltnet/calculators/" *.java
java -cp ".:src/" info.kyrcha.tiltnet.caclulators.Hand
