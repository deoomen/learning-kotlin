FROM gradle:8.0-jdk19 AS build
COPY --chown=gradle:gradle ./app /RecipeSearch
WORKDIR /RecipeSearch
RUN gradle clean build jar --info

FROM openjdk:19-slim
WORKDIR /RecipeSearch
COPY --from=build /RecipeSearch/build/libs/*.jar RecipeSearch.jar
CMD ["java", "-jar", "RecipeSearch.jar"]
