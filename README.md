# Reversi
This is a simple project I'm using to learn Kotlin and a few other technologies.

## Build/run instructions
Run this command:
   `./gradlew -t jsRun`

It should build and open a new browser window.

## Tests
I ran through the steps outlined in https://kotlinlang.org/docs/js-running-tests.html,
but couldn't get it to work (it looked like a dependency issue related to kotlin.test).
The steps outlined in https://www.jetbrains.com/guide/java/tutorials/writing-junit5-tests/setting-up-gradle-junit5/
didn't work either (in part because the video published on 2021-01-01 references an "Add Gradle Plugin" item in the ⌘N menu
that doesn't exist in my IntelliJ installation).

Fortunately, I was able to get Kotest working instead.

Run this command to run the tests:
   `./gradlew -t check`