FROM openjdk:17-oracle
ADD target/*.jar news_hacker_v3.jar
ENTRYPOINT ["java","-jar","news_hacker_v3.jar"]
