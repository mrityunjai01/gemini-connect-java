# Introduction

I have not used any AI assistance. The assignment seems to be short and doable regardless.

## Run

JARs are cross-platform, the `target/` folder contains a self-contained executable jar packaged using maven. Run command using a JDK:

```bash
java -jar target/qd-1.0-SNAPSHOT-jar-with-dependencies.jar
```

If it doesn't run on the java installed in your system, you have two ways:

1. Build (possibly changing hte version in pom.xml )
2. Use a different jar within target/ folder: I have saved multiple jars for versions like java 8, java 17, java 25

## Build

```bash
mvn clean package
```

Let's say you want to compile for a diff version of java:

```xml

    <properties>
        <maven.compiler.source>25</maven.compiler.source>
        <maven.compiler.target>25</maven.compiler.target>
    </properties>
```

Change 25 here to some other version;
