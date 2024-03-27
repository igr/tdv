
# just displays the recipes
_default:
    @just --list

clean:
    ./gradlew clean

build:
    ./gradlew build

test:
    ./gradlew test

release: clean build
    ./gradlew shadowJar
    ls app/build/libs/*.jar

run target:
    java -jar app/build/libs/tdv-0.5.0-all.jar {{target}}

examples:
    just run in/Thread.print.20230908190546
    just run in/Thread.print.20240216120818
