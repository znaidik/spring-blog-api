# Restful Blog API using Spring Boot, Spring Data, JPA and H2 Database

## About

This project exposes some REST CRUD service for a Blog.
It demonstrates the use of Spring-boot & Java 8.
An h2 in memory database has been used to store the pet shop data.

**Important** : The project requires maven 3.x and a Java 8 jdk.

## 1. Installation

* Clone the github repository :
```
git clone https://github.com/znaidik/spring-blog-api.git

```

* Enter to the project folder
```
cd spring-blog-api
```

* Launch mvn clean install to build the project
```
./mvnw clean install
or
mvn clean install

```
By default mvn clean install runs also the test units included in the project.
In the case of some unit test failed, you can run the following command :
```
./mvnw clean install -DskipTests
or
mvn clean install -DskipTests
```

## 2. Set up database configuration
The configuration file [application.properties](/spring-blog-api/src/main/resources/application.properties) allows you to change default parameters.

You can set-up the database configuration :
```
#datasource
spring.datasource.url=jdbc:h2:mem:springblogdb
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.h2.console.enabled=true
```
If you would like to use another database please don't forget to add the driver dependency in the pom.xml.

Ex :
```
<dependency>
 <groupId>com.oracle.jdbc</groupId>
 <artifactId>ojdbc7</artifactId>
 <version>12.1.0.2</version>
</dependency>
```

## 3. Launch the REST server

* Run mvn spring-boot:run in the spring-blog-api project to launch spring-boot server (a Tomcat is bundled by default)
```
cd spring-blog-api

./mvnw spring-boot:run
or
mvn spring-boot:run
```

## 4. REST services documentation (Swagger)
The Rest services are documented with the Swagger api documentation available on these URL, once you run the server : [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

You can also test directly the service with the swagger ui.

## 5. Run test units
To run only the test units run mvn test :
```
./mvnw test
or
mvn test
```
