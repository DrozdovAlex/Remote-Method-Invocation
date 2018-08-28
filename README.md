# Inbacks-rmi

The implementation of the client and the server interacting through a fictional binary protocol, in order to provide a primitive remote call functions.

---

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes. See deployment for notes on how to deploy the project on a live system.

### Prerequisites

- Install [Java 1.8](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)

- Install [Maven](https://maven.apache.org/)

- Use [JUnit 4](https://junit.org/junit4/)

### Installing

- Clone this repo to your local machine using:
```
$ git clone https://bitbucket.org/inbacks/inbacks-rmi.git
$ mvn clean package
```

---

## Running the tests

- Start tests
```
$ mvn test
```

#### RemoteCallTest

- Send requests and verify the correctness of their responses

#### HighLoadTest

- Load the server by concurrently connecting many clients and their requests

---

## Support

Reach out to us at one of the following places!

- Alex Drozdov: [vip.alexd@gmail.com](mailto:vip.alexd@gmail.com)

- Aleksandr Kirilenko: [alexkirnsu@gmail.com](mailto:alexkirnsu@gmail.com)