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
Maven 3.9

### Time spent
- 1.5h on MVC (basic functionality, basic logging, layered structure, documentation)
- around 3h on observability, resilience, integration tests

### Run instructions
Assuming you have Maven 3.9+ installed
1. Build project:
```bash
mvn clean package 
```
2. Run project - use one of the following commands:  

a. For local use:
```bash
mvn spring-boot:run -Dspring-boot.run.profiles=local
```
b. On test environment (test profile is a default one):
```bash
mvn spring-boot:run
```
c. On prod environment:
```bash
mvn spring-boot:run -Dspring-boot.run.profiles=prod
```
3. Example request:  
   http://localhost:8080/airports/lookup?code=KATL

Example response:  
200 OK  
```text
{
    "data": {
        "info": {
            "KATL": [
                {
                    "site_number": "03640.*A",
                    "type": "AIRPORT",
                    "facility_name": "HARTSFIELD - JACKSON ATLANTA INTL",
                    "faa_ident": "ATL",
                    "icao_ident": "KATL",
                    "region": "ASO",
                    "district_office": "ATL",
                    "state": "GA",
                    "state_full": "GEORGIA",
                    "county": "FULTON",
                    "city": "ATLANTA",
                    "ownership": "PU",
                    "use": "PU",
                    "manager": "BALRAM BHEODARI",
                    "manager_phone": "404-530-6600",
                    "latitude": "33-38-12.1186N",
                    "latitude_sec": "121092.1186N",
                    "longitude": "084-25-40.3104W",
                    "longitude_sec": "303940.3104W",
                    "elevation": "1026",
                    "magnetic_variation": "05W",
                    "tpa": "",
                    "vfr_sectional": "ATLANTA",
                    "boundary_artcc": "ZTL",
                    "boundary_artcc_name": "ATLANTA",
                    "responsible_artcc": "ZTL",
                    "responsible_artcc_name": "ATLANTA",
                    "fss_phone_number": "",
                    "fss_phone_numer_tollfree": "1-800-WX-BRIEF",
                    "notam_facility_ident": "ATL",
                    "status": "O",
                    "certification_typedate": "I E S 05/1973",
                    "customs_airport_of_entry": "N",
                    "military_joint_use": "N",
                    "military_landing": "Y",
                    "lighting_schedule": "",
                    "beacon_schedule": "SS-SR",
                    "control_tower": "Y",
                    "unicom": "122.950",
                    "ctaf": "",
                    "effective_date": "11/04/2021"
                }
            ]
        }
    },
    "error": null
}
```

4. Observability endpoints:  
   http://localhost:8080/actuator/info  
   http://localhost:8080/actuator/health  
   http://localhost:8080/actuator/loggers (non-prod spring profiles)  
   http://localhost:8080/actuator/prometheus  

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
- Task evaluation might be for both: how much is done in 90 mins (team lead time management skills - priority focus) or how many criteria met (senior/tech lead roles - hands-on focus)

### Implementation
- Layered structure: controller, service, provider, client
- 3d party integration with https://api.aviationapi.com
- Observability - Spring Actuator endpoints: health, info, loggers (non-prod profiles)
- Observability - exposed prometheus
- Observability - logback with spring (text/json logs for different profiles), logged latency
- Observability - distributed tracing with micrometer tracing (in prod should be collected centrally)
- Resilience4j for retries, circuit breaker and rate limiter
- Tests - Integration and E2E

### Trade-offs
- [Time limit] JsonNode used to handle JSON response from third party - probably we'll need fields mapping on our side
- [Time limit] logs injection with user input (ICAO code) - that needs either input validation or no input logging
- [Time limit] no security implemented
- [Out of scope] no dockerfile created, no ci/cd setup
- [Priority] swagger docs and API clear error codes (on top of existing ResponseDto and ErrorDto) was deprioritized in favor of observability/resilience requirements (production-readiness)

### AI usage note
The source code was written all by me.  
The AI was used for faster integration tests generation (the baseline test - final result was still tweaked by me), and also trade-offs discussions and best practices in observability and resilience (along with forums and official docs)

### Next Steps
- Add swagger for API documentation, polish 4xx/5xx responses
- Add validation for input ICAO code - that will lower incorrect requests earlier than they reach 3d party side
- Add caching (Redis or other) - GET endpoint with rarely changed info is a perfect candidate for caching - that will significantly reduce the load for 3d party requests. Proposed TTL 1h-24h
- /actuator/prometheus needs to be available internally on prod, but unavailable from the public network
- E2E test needs to be excluded from CI build
- Expose logs tracing outside microservice (currently in logs only)
- Remove hardcoded values, extract test data
- Switch properties to YAML ?
- Add service discovery ?
- Add dependency management in Maven for easier support
