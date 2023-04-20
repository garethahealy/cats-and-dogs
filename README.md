# cats-and-dogs

This project uses Quarkus, the Supersonic Subatomic Java Framework.

If you want to learn more about Quarkus, please visit its website: https://quarkus.io/ .

## Running the application in dev mode

You can run your application in dev mode that enables live coding using:
```shell script
./mvnw compile quarkus:dev
```

> **_NOTE:_**  Quarkus now ships with a Dev UI, which is available in dev mode only at http://localhost:8080/q/dev/.

## Packaging and running the application

The application can be packaged using:
```shell script
./mvnw package
```
It produces the `quarkus-run.jar` file in the `target/quarkus-app/` directory.
Be aware that it’s not an _über-jar_ as the dependencies are copied into the `target/quarkus-app/lib/` directory.

The application is now runnable using `java -jar target/quarkus-app/quarkus-run.jar`.

## Related Guides

- Camel Log ([guide](https://access.redhat.com/documentation/en-us/red_hat_integration/2.latest/html/camel_extensions_for_quarkus_reference/extensions-log)): Log messages to the underlying logging mechanism
- Camel JTA ([guide](https://access.redhat.com/documentation/en-us/red_hat_integration/2.latest/html/camel_extensions_for_quarkus_reference/extensions-jta)): Enclose Camel routes in transactions using Java Transaction API (JTA) and Narayana transaction manager
- Camel Core ([guide](https://access.redhat.com/documentation/en-us/red_hat_integration/2.latest/html/camel_extensions_for_quarkus_reference/extensions-core)): Camel core functionality and basic Camel languages: Constant, ExchangeProperty, Header, Ref, Simple and Tokenize
- Camel JPA ([guide](https://access.redhat.com/documentation/en-us/red_hat_integration/2.latest/html/camel_extensions_for_quarkus_reference/extensions-jpa)): Store and retrieve Java objects from databases using Java Persistence API (JPA)
- Camel Direct ([guide](https://access.redhat.com/documentation/en-us/red_hat_integration/2.latest/html/camel_extensions_for_quarkus_reference/extensions-direct)): Call another endpoint from the same Camel Context synchronously
- Camel MicroProfile Health ([guide](https://access.redhat.com/documentation/en-us/red_hat_integration/2.latest/html/camel_extensions_for_quarkus_reference/extensions-microprofile-health)): Expose Camel health checks via MicroProfile Health
- Camel MicroProfile Metrics ([guide](https://access.redhat.com/documentation/en-us/red_hat_integration/2.latest/html/camel_extensions_for_quarkus_reference/extensions-microprofile-metrics)): Expose metrics from Camel routes
- Camel Data Format ([guide](https://access.redhat.com/documentation/en-us/red_hat_integration/2.latest/html/camel_extensions_for_quarkus_reference/extensions-dataformat)): Use a Camel Data Format as a regular Camel Component
- Camel Jackson ([guide](https://access.redhat.com/documentation/en-us/red_hat_integration/2.latest/html/camel_extensions_for_quarkus_reference/extensions-jackson)): Marshal POJOs to JSON and back using Jackson
- Camel Mock ([guide](https://access.redhat.com/documentation/en-us/red_hat_integration/2.latest/html/camel_extensions_for_quarkus_reference/extensions-mock)): Test routes and mediation rules using mocks
- Camel Netty ([guide](https://access.redhat.com/documentation/en-us/red_hat_integration/2.latest/html/camel_extensions_for_quarkus_reference/extensions-netty)): Socket level networking using TCP or UDP with Netty 4.x
- Camel Rest ([guide](https://access.redhat.com/documentation/en-us/red_hat_integration/2.latest/html/camel_extensions_for_quarkus_reference/extensions-rest)): Expose REST services and their OpenAPI Specification or call external REST services

## Notes

```
podman run --arch=amd64 -it -p 5672:5672 -p 8161:8161 -e AMQ_USER=admin -e AMQ_PASSWORD=password -e AMQ_QUEUES="incoming,outgoing" -e AMQ_EXTRA_ARGS=--nio registry.redhat.io/amq7/amq-broker-rhel8:7.11.0
```