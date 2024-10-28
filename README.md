# HeliosX Demo Consultation Service

This is a demo HeliosX Consultation Service app for a technical test, providing API endpoints for
managing and submitting consultations. The project includes endpoints to retrieve consultations by
reference and submit answers to determine eligibility.

The code is formatted using the Google Java Style Guide, and includes Swagger UI for API documentation.

---

## Getting Started

### Prerequisites

- Docker *OR* Java 23 suing Maven wrapper

### Running the Application

#### Run with Docker

To build and run the application in a Docker container, execute the following 2 commands in project
root:

```bash
docker build -t heliosx-consultation-service . 
docker run -p 8080:8080 heliosx-consultation-service
```

The application will now be running at http://localhost:8080

#### Run in IDE with Maven wrapper

Open the project in your IDE (e.g., IntelliJ IDEA, Eclipse).
Build and run the application using Maven:

```bash
./mvnw clean install
./mvnw spring-boot:run
```

The application will now be running at http://localhost:8080

### Documentation

The application includes Swagger UI for API documentation. You can access it at:

```bash
http://localhost:8080/swagger-ui.html
````

This interface provides interactive documentation to test and understand available API endpoints.

To access the openapi spec directly, visit:

```bash
http://localhost:8080/v3/api-docs
```

### Using the API

The application provides 2 endpoints, one to fetch a consultation,
one to submit answers. Please note that the consultation IDs must match,
and answer IDs must match those of the questions.

#### 1. Get Consultation by Reference

curl:

```bash
curl -X 'GET' \
  'http://localhost:8080/api/consultation/GENOVIAN_PEAR' \
  -H 'accept: application/json'
```

Swagger
endpoint: http://localhost:8080/swagger-ui/index.html#/consultation-controller/getConsultationByReference

```bash
GENOVIAN_PEAR
```

#### 2. Submit Consultation Answers

curl:

```bash
curl -X 'POST' \
  'http://localhost:8080/api/consultation' \
  -H 'accept: application/json' \
  -H 'Content-Type: application/json' \
  -d '{
  "id": "f47ac10b-58cc-4372-a567-0e02b2c3d479",
  "email": "robfoulsham@gmail.com",
  "answers": [
    {
      "questionId": "d2d08c24-54af-4430-a584-15055ce2babf",
      "answer": true
    },
    {
      "questionId": "488b98ba-553d-43f7-ba09-f7bbbb3cfba9",
      "answer": true
    },
    {
      "questionId": "842fe0aa-4f94-4e1c-8a1f-cf60736d099f",
      "answer": false
    }
  ]
}'
```

Swagger
endpoint: http://localhost:8080/swagger-ui/index.html#/consultation-controller/submitConsultation

```bash
{
  "id": "f47ac10b-58cc-4372-a567-0e02b2c3d479",
  "email": "robfoulsham@gmail.com",
  "answers": [
    {
      "questionId": "d2d08c24-54af-4430-a584-15055ce2babf",
      "answer": true
    },
    {
      "questionId": "488b98ba-553d-43f7-ba09-f7bbbb3cfba9",
      "answer": true
    },
    {
      "questionId": "842fe0aa-4f94-4e1c-8a1f-cf60736d099f",
      "answer": false
    }
  ]
}
```

### Thoughts and Considerations for the Tech Test

- My general approach was to create a simple, clean, and maintainable solution that met the requirements
while also leaving room for future expansion. I wanted to ensure the system was reusable for any number of products.

- I have ensured only one consultation can be submitted per email address.
This is to prevent multiple submissions from the same user, and to prevent abuse of the system, or
attempts to circumvent the eligibility logic.

- I have not presented any feedback to the user on the reasons for the eligibility of their submission.
This may be required and can be added in future iterations.

- I separated the DTOs from the entities to ensure the API is not tightly coupled to the database schema.
This is not really necessary with the current implementation, but good to do from early on.

- I created some basic error handling to ensure the API returns appropriate responses in case of errors.
I did not spend that much time on this, but it's something that I'd expand on in a production system.

- I wrote basic tests for the service layer to ensure the eligibility logic is working as expected.
In reality I'd like to see more tests including tests from the http layer against the entire app context.

- I have documented the API using inline Swagger annotations. These are great for small APIs or docs without much detail
but I feel they make the controller classes unreadable. With more time, and in an actual system I'd move these
additional docs to a separate file. Ideally I'd actually drive the development from an openAPI spec (API first development).

- I tried to spend the requested amount of time on this, though my efforts were spread across various breaks across few days 
while doing my current job. This means it is hard to say exactly how much time I spent.

- There is some more work to do around processing the answers. Currently, if an answer is submitted 
which is not part of the consultation, the submission is still accepted and persisted. There should be custom 
error messaging for this (even though it wouldn't happen in practice)



