FROM maven:3.8.5-openjdk-17 AS build

WORKDIR /usr/src/Moneta_Media

#COPY settings.xml /usr/share/maven/conf/settings.xml

COPY pom.xml .

COPY src ./src

RUN mvn -B clean package -DskipTests

FROM openjdk:17-jdk-slim

WORKDIR /Moneta_Media

COPY --from=build /usr/src/Moneta_Media/target/Moneta_Media-0.0.1-SNAPSHOT.jar Moneta_Media.jar

ENTRYPOINT ["java","-jar","Moneta_Media.jar"]