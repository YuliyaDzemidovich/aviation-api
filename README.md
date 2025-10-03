# Aviation API Wrapper

[Overview](#overview)  
[Requirements](#requirements)  
[Proposed solution](#solution)


### Overview

Example microservice on Java + Spring Boot microservice that integrates with a public aviation
data API to retrieve information about airports based on ICAO codes

## Requirements
### Task

Design and implement a microservice that:
- Accepts HTTP requests to fetch airport details using an ICAO code.
- Queries the public aviation data API at https://aviationapi.com to retrieve the data.
- The response format is up to you, but it should be clean and documented,
containing key airport information (e.g., name, location, ICAO/IATA, etc.).
- Handles upstream API failures gracefully (timeouts, retries, rate limits, etc.).

### Evaluation criteria

Key focus areas:
- Scalability: clean service layering, statelessness, readiness for load.
- Resilience: retry logic, circuit breakers, fallback strategies.
- Extensibility: your code should not be tightly coupled to one provider.
- Observability: basic logging, error transparency, metrics readiness.

### Assumptions

- No frontend is required.
- You can assume the third-party API is accessible but may be unstable.
- Only the ICAO code lookup is in scope.
- No user management is required.

## Solution

### Chosen Tech stack

Java 21  
Spring Boot 3.5.6  
Maven  

### Time spent
- 1.5h on MVC (basic functionality, basic logging, layered structure, documentation)

### Testing

Run all tests:
```bash
mvn test
```

There are two types of tests - integration and E2E. Since we have sometimes unstable 3d party, but also would like our CI build to not depend on E2E test with that 3d party integration.  

1. Integration tests (spring boot context + mocked provider) can be run with command:
```bash
mvn test -Dgroups=integration
```

2. E2E test (spring boot context + real provider) can be run with command:
```bash
mvn test -Dgroups=e2e
```

### My assumptions
- it's a public endpoint. I assume basic auth (like DDoS protection) will be available on the firewall/gateway level

### Implementation
- Layered structure: controller, service, provider, client
- 3d party integration with https://api.aviationapi.com
- Observability - Spring Actuator endpoints: health, info, loggers (non-prod profiles)
- Observability - exposed prometheus
- Resilience4j for retries, circuit breaker and rate limiter
- Tests - Integration and E2E

### Trade-offs
- [Time limit] JsonNode used to handle JSON response from third party - probably we'll need fields mapping on our side
- [Time limit] logs injection with user input (ICAO code) - that needs either input validation or no input logging
- [Time limit] no security implemented
- [Time limit] no dockerfile created, no ci/cd setup

### Next Steps
- Add validation for input ICAO code - that will lower incorrect requests earlier than they reach 3d party side
- Add caching (Redis or other) - GET endpoint with rarely changed info is a perfect candidate for caching - that will significantly reduce the load for 3d party requests. Proposed TTL 1h-24h
- /actuator/prometheus needs to be available internally on prod, but unavailable from the public network
- Add swagger for API documentation
- E2E test needs to be excluded from CI build
- Remove hardcoded values, extract test data
- Switch properties to YAML ?
- Add service discovery ?
