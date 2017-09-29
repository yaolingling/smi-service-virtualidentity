### smi-service-virtualidentity

A virtual identity (MAC,IQN,WWN) generation and reservation system.

A docker container for this service is avalable at:  https://hub.docker.com/r/rackhd/virtualidentity/

Copyright © 2017 Dell Inc. or its subsidiaries.  All Rights Reserved. 

### Purpose

The Virtual Identity docker container runs a spring-boot micro-service that exposes a REST API for a user or application to reserve and assign virtual identities (MAC Addresses, IQN's, WWN's).

This code generates a default global pools for MAC addresses on startup using a pre-defined starting pattern.   
A user can create their own pool with their own starting pattern if desired and use it.  Pools can be expanded at a later date.  Pools can exist for MAC addresses, IQN’s, and WWN’s.

A user has the ability to “Reserve” a quantity of virtual identities from a pool for a UsageID (can be a generic identifier for the process or a specific system identifier) from any of the virtual pools. 

The typical use-case is to reserve the expected quantity of virtual identities from a pool for a workflow as part of a two-phase process (reservation then assignment).  The two phase process insures the total quantity is available for the whole process.  

Reserved virtual identities can expire and be reclaimed if not released or assigned within a time limit.
A user can “Assign” one or more virtual Identities from a pool to a specific usage Id for the device where it is actually used.  

A user can “Release” a virtual identity back to the pool if it is no longer needed.  Additionally, the option to release all of the virtual identities for a specific usage ID is provided.

### How to Launch

##### Option 1. Linking to a postgres database:
1. Start the postgres database first
~~~
docker run --name postgresql -e POSTGRES_PASSWORD=foo -d postgres:9.6.1-alpine
~~~
2. Start the virtualidentity docker container with the link option
~~~
sudo docker run --name virtualidentity -p 0.0.0.0:46015:46015 --link=postgresql:db -e SPRING_PROFILES_ACTIVE=linked -e DB_POSTGRES_PASSWORD=foo -d rackhd/virtualidentity:latest
~~~

##### Option 2. Passing connection settings in with the environment tag:
~~~
docker run --name virtualidentity -p 0.0.0.0:46015:46015 -e DB_POSTGRES_PASSWORD=foo -e DB_PORT_5432_TCP_PORT=5432 -e DB_PORT_5432_TCP_ADDR=1.2.3.4 -d rackhd/virtualidentity:latest
~~~
~~~

~~~
##### Option 3. Docker Compose (without Conusl K/V store) example:
~~~
version: '2'

services:
  virtualidentity:
    container_name: virtualidentity
    image: rackhd/virtualidentity:latest
    depends_on:
    - postgresql
    ports:
    - 46015:46015
    volumes:
    - /var/log/dell/:/var/log/dell/
    mem_limit: 512m
    environment:
    - _JAVA_OPTIONS=-Xmx64m -Xss256k
    - "JAVA_OPTS=-Dspring.datasource.url=jdbc:postgresql://1.2.3.4:5432/postgres -Dspring.datasource.password=Dell123$"

  postgresql:
    container_name: postgresql
    image: postgres:9.6.1-alpine
    ports:
    - 5432:5432
    mem_limit: 512m
    environment:
      - POSTGRES_PASSWORD=Dell123$$
~~~
~~~
 
~~~
##### Option 4. Docker Compose using Conusl K/V store for properties example:
~~~
version: '2'

services:
  virtualidentity:
    container_name: virtualidentity
    image: rackhd/virtualidentity:latest
    depends_on:
    - consul
    - postgresql
    network_mode: "host"
    ports:
    - 46015:46015
    extra_hosts:
    - "service-registry:<<replace with consul ip address>>"
    volumes:
    - /var/log/dell/:/var/log/dell/
    #- /etc/ssl/:/etc/ssl
    mem_limit: 512m
    environment:
    - "SPRING_PROFILES_ACTIVE=consul"
    - _JAVA_OPTIONS=-Xmx64m -Xss256k

  postgresql:
    container_name: postgresql
    image: postgres:9.6.1-alpine
    ports:
    - 5432:5432
    mem_limit: 512m
    environment:
      - POSTGRES_PASSWORD=Dell123$$
~~~
~~~

~~~ 
Example properties posted to config\virtualidentity\data of the consul K\V store
~~~
server:
  port: 46015
 # ssl:
 #   key-store: /etc/ssl/keystore.jks
 #   key-store-password: changeit

# remove this section if enforcing security
security:
  ignored:
   - /**

spring:
  jpa:
    database: POSTGRESQL
    properties:
      hibernate:
        default_schema: virtualidentity
    show-sql: true
    generate-ddl: true
    hibernate:
      ddl-auto: update
  datasource:
    platform: postgres
    url: jdbc:postgresql://1.2.3.4:5432/postgres
    username: postgres
    password: Dell123$
~~~
~~~

~~~
Example security section with Keycloak OAuth2 enabled
~~~
security:
  # ignored:
  # - /**
  oauth2:
    client:
      clientId: "spring-boot-demos"
      clientSecret: "2f53b44a-774a-4394-8a98-138476503d24"
      accessTokenUri: "http://100.68.123.174:8089/auth/realms/Test1/protocol/openid-connect/token"
      userAuthorizationUri: "http://100.68.123.174:8089/auth/realms/Test1/protocol/openid-connect/auth"
      tokenName: "oauth_token"
      authenticationScheme: "header"
      clientAuthenticationScheme: "header"
    resource:
      userInfoUri: "http://100.68.123.174:8089/auth/realms/Test1/protocol/openid-connect/userinfo"
~~~

---

### How to Use


#### API Definitions

A swagger UI is provided by the microservice at http://<ip>:46015/swagger-ui.html

---

### Licensing
This docker microservice is available under the [Apache 2.0 License](http://www.apache.org/licenses/LICENSE-2.0.txt). 

The microservice makes use of some dependent libraries that may be covered other other licenses, including the Hibernate Core library which is licensed under the [LGPL 2.1 license](https://www.gnu.org/licenses/old-licenses/lgpl-2.1.txt).

Source code for this microservice is available in repositories at https://github.com/RackHD.  

In order to comply with the requirements of applicable licenses, the source for dependent libraries used by this microservice is available for download at:  https://bintray.com/rackhd/binary/download_file?file_path=smi-service-virtualnetwork-dependency-sources-devel.zip    

Additionally the binary and source jars for all dependent packages are available for download on Maven Central.

### Support
Slack Channel: codecommunity.slack.com
