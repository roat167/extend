
<h1 align="left">Extend REST API DEMO</h1>
<ul align="left">
  <li>List all virtual cards available to your user, including the available balance remaining. In this case, we would expect to see just one virtual card returned.</li>
  <li>List the transactions associated with your virtual card.</li>
  <li>View the details for each individual transaction you’ve made.</li>
</ul>

## Table of Contents

- [Prerequisites](#prerequisites)
- [Technology](#technology)
- [Installation](#installation)
- [Running application with snapshot](#run-snapshot)
- [Running a specific test](#tests)
- [Swagger](#swagger)
- [Environment variables](#environment-variables)

# Prerequisites
- Java: JDK `11` or later version installed
- Maven: `3` or later version installed

## Technology
This project was started with [Spring Initializr](https://start.spring.io/#!type=maven-project&language=java&platformVersion=3.0.0&packaging=jar&jvmVersion=11&groupId=com&artifactId=extend&name=extend&description=Extend%20REST%20API%20DEMO&packageName=com.extend&dependencies=lombok,security).

## Installation

```shell
# clone the repository and access the directory.
$ git clone https://github.com/roat167/extend.git && cd extend

# download dependencies
$ mvn install -DskipTests

# run the application
$ mvn spring-boot:run

# run the tests
$ mvn test

# to build for production
$ mvn clean package

# to generate the coverage report after testing (available at: target/site/clover/clover/index.html)
$ mvn clover:clover
```
## Running snapshot

```shell
# clone the repository and access the directory.
$ git clone https://github.com/roat167/extend.git && cd extend

# to build snapshot
$ mvn clean package

# Run
$ java -jar target/extend-0.0.1-SNAPSHOT.jar
```

## Running a specific test
use the parameter `-Dtest=<class>#<method>`

```
$ mvn test -Dtest=UserControllerTest#login_givenValidUserRequest_shouldReturnResponseWithToken
```


## Swagger
Once the application is up, it is available at: [localhost:8080/swagger-ui.html](localhost:8080/swagger-ui.html)


## Environment variables
              |
> spring variables are defined in: [**application.properties**](./src/main/resources/application.properties)
>
> ```shell
> # to change the value of some environment variable at runtime
> # on execution, just pass it as a parameter. (like --SERVER_PORT=80).
> $ java -jar api-4.1.2.jar --SERVER_PORT=80
> ```


> [All options of `aplication.properties` here](https://docs.spring.io/spring-boot/docs/current/reference/html/common-application-properties.html).
>
> [All **features** of Spring Boot](https://docs.spring.io/spring-boot/docs/current/reference/html/spring-boot-features.html).

### Reference Documentation
For further reference, please consider the following sections:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/3.0.0-SNAPSHOT/maven-plugin/reference/html/)
* [Create an OCI image](https://docs.spring.io/spring-boot/docs/3.0.0-SNAPSHOT/maven-plugin/reference/html/#build-image)

