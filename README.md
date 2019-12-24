# Authorization in microservices intercommunication with Spring Security OAuth2 and Auth0

This sample demonstrates:

- Using [Auth0](https://auth0.com/) as authorization server in microservices based architecture
- Using and extending Spring Security to validate JWTs
- Using Spring Cloud Gateway for routing the requests
- Protecting APIs to only allow authorized access

As a resource server a sample project [01-Authorization-MVC](https://github.com/auth0-samples/auth0-spring-security5-api-sample/tree/master/01-Authorization-MVC) 
created by Auth0 team is used 

## Prerequisites

- Java 8 or greater
- An Auth0 account

## Setup

> For complete instructions and additional information, please refer to the [Spring 5 API Security Quickstart](https://auth0.com/docs/quickstart/backend/java-spring-security5).

### Create an Auth0 API

In the [APIs](https://manage.auth0.com/dashboard/#/apis) section of the Auth0 dashboard, click Create API. Provide a 
name and an identifier for your API, for example `https://quickstarts/api`. Leave the Signing Algorithm as RS256.
Register "Machine-To-Machine" application within the created API and make sure the application is granted `read:messages`
scope when authenticating with the Auth0 server.

### Configure the project

The project needs to be configured with your Auth0 domain and API Identifier.

To do this, in `src/main/resources/application.yml` file inside `resource-server` project replace the values with your 
own Auth0 domain and API Identifier:

```yaml
auth0:
  audience: {API_IDENTIFIER}

spring:
  security:
    oauth2:
      resourceserver:
        jwt:
          # Note the trailing slash is important!
          issuer-uri: https://{DOMAIN}/
```

## Running

You can run the application as an executable jar file. In order to launch Redis instance which is used as a token cache, 
execute `docker-compose up -d` command.
