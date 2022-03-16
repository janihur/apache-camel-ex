#!/bin/bash

declare -r DIR="$1"

mkdir -p $DIR/src/main/java
mkdir -p $DIR/src/main/resources

cat > $DIR/pom.xml <<EOF
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <parent>
        <groupId>net.jani-hur</groupId>
        <artifactId>camel3-ex</artifactId>
        <version>1.0</version>
    </parent>

    <artifactId>${DIR}</artifactId>
    <packaging>jar</packaging>

    <dependencies>
        <!-- CAMEL -->
        <dependency>
            <groupId>org.apache.camel</groupId>
            <artifactId>camel-direct</artifactId>
            <version>\${camel.version}</version>
        </dependency>
        <dependency>
            <groupId>org.apache.camel</groupId>
            <artifactId>camel-main</artifactId>
            <version>\${camel.version}</version>
        </dependency>
        <dependency>
            <groupId>org.apache.camel</groupId>
            <artifactId>camel-timer</artifactId>
            <version>\${camel.version}</version>
        </dependency>
        <!-- OTHER -->
    </dependencies>
</project>
EOF

cat > $DIR/src/main/java/App.java <<EOF
// https://camel.apache.org/components/latest/others/main.html
import org.apache.camel.main.Main;

public class App {
    public static void main(String[] args) throws Exception {
        Main main = new Main();
        main.configure().addRoutesBuilder(Routes.routes());
        main.run(new String[]{"-duration", "5"});
    }
}
EOF

cat > $DIR/src/main/java/Routes.java <<EOF
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.Exchange;

public class Routes {
    public static RouteBuilder routes() {
        return
            new RouteBuilder() {
                public void configure() {
                    from("timer:mainRouteTrigger?repeatCount=1")
                    .routeId("MainRouteTrigger")
                        .to("direct:mainRoute")
                    .end()
                    ;

                    from("direct:mainRoute")
                    .routeId("MainRoute")
                        .log("MainRoute BEGINS: BODY: ${body}")
                        .log("MainRoute ENDS: BODY: ${body}")
                    .end()
                    ;
                }
            };
    }
}
EOF

cat > $DIR/src/main/resources/log4j2.properties <<EOF
appender.out.type = Console
appender.out.name = out
appender.out.layout.type = PatternLayout
appender.out.layout.pattern = [%30.30t] %-30.30c{1} %-5p %m%n
rootLogger.level = INFO
rootLogger.appenderRef.out.ref = out
EOF
