FROM eclipse-temurin:17-jre

RUN mkdir /app

WORKDIR /app

ADD ./api/target/vreme-api-1.0.0-SNAPSHOT.jar /app

EXPOSE 8086

CMD ["java", "-jar", "vreme-api-1.0.0-SNAPSHOT.jar"]
#ENTRYPOINT ["java", "-jar", "vreme-api-1.0.0-SNAPSHOT.jar"]
#CMD java -jar vreme-api-1.0.0-SNAPSHOT.jar
