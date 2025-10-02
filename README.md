# Aviation API Wrapper

[Overview](#overview)  
[Task](#task)  
[Evaluation criteria](#evaluation-criteria)  
[Assumptions](#assumptions)  
[Chosen Tech stack](#chosen-tech-stack)


### Overview

Example microservice on Java + Spring Boot microservice that integrates with a public aviation
data API to retrieve information about airports based on ICAO codes

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

### Chosen Tech stack

Java 21  
Spring Boot 3.5.6  
Maven  
