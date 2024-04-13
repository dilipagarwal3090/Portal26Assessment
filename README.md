# Welcome

## About 
This project demonstrates the ability to create functional software that solves real business problems. It's live on AWS. The goal is to showcase skills in building backend services. The system created is scalable, reliable, and secure, reflecting key aspects of SaaS platforms.

At present this system supports two API's:

1. CreateEvent API: This serves as a webhook endpoint for accepting data from external customers.
2. QueryEvent API: A query endpoint, enabling users to retrieve data stored within the system.

### Coding best practices that were taken care of while building the project
1. Design Pattern: ACBDA design pattern was used while developing, the project ensures a structured and organized approach to software architecture, enhancing maintainability and scalability.
2. Proper Formatting: The codebase maintains a consistent and readable format, ensuring clarity and ease of comprehension.
3. Proper Exception Handling: Comprehensive exception handling mechanisms are in place to gracefully manage unexpected errors and exceptions.
4. Input Validation: Input validation procedures are implemented to verify the integrity of incoming requests. In instances where input data does not meet the expected criteria, the system is equipped to promptly respond with a BadRequest (HTTP 400) status code, ensuring data integrity and security.
5. Proper Error Codes display to user

### Key benefits of choosing the directory structure as it is currently
1. Testability: Each component can be independently unit tested, which simplifies the testing process and improves test coverage. Unit testing becomes more straightforward because each component has well-defined inputs and outputs, making it easier to isolate and test individual functionalities.
2. Maintainability: The clear separation of concerns and modular structure enhance code maintainability. Developers can quickly locate and modify specific parts of the codebase without needing to understand the entire application. Additionally, the codebase becomes more resistant to bugs and errors, as changes in one component are less likely to have unintended consequences in other parts of the system.
3. Dependency Injection (Dagger): Using a dependency injection framework like Dagger facilitates the management of dependencies between components. It promotes loosely coupled code and simplifies the configuration and management of dependencies, leading to cleaner and more maintainable code.
4. Error Handling: Having a separate translator for exceptions centralizes and standardizes error handling within the application. It provides a consistent way to handle exceptions across different components, improving code readability and maintainability.

### Key Features of CDK Package
1. Modular Stack Structure: The CDK package adopts a modular approach by creating separate stacks for various resources like Lambda functions, API Gateway endpoints, and DynamoDB tables. This separation of concerns allows for better organization and management of resources within the application.
2. Lambda Construct: Each Lambda function is encapsulated within a construct, ensuring consistency and maintainability across the application. By abstracting away the implementation details and handling security requirements within the construct, new Lambda functions can be easily added by simply creating a new object instance, without the need for extensive code changes.
3. DynamoDB Construct: Similarly, DynamoDB tables are created using constructs that abstract away the complexity of table creation. With minimal code, new tables can be provisioned by specifying only the table name and desired properties. This simplifies the process of adding new data storage components to the application.
4. Type Safety and Data Integrity: The package emphasizes the use of enums, interfaces, and types to ensure data integrity at each level of the application. By defining clear data structures and enforcing type safety, potential errors and inconsistencies in data handling are mitigated. This approach enhances code readability, maintainability, and overall robustness of the application.

## Testing

### Create Event API 
  * Endpoint: POST https://zdrla73dj1.execute-api.eu-west-1.amazonaws.com/prod/v1/webhooks/{tenant_name}/events
  * Request:
    ```
    {
      "eventTimestamp":"2024-01-11T01:42:50.234200+00:00",
      "userId":"johndoe",
      "url": "https://chat.openai.com/backend-api/conversation",
      "body":"what is the capital of India?"
    }
    ```
  * Response:
    ```
    {
      status: 200,
      body: success
    }
    ```
### Query Event API 
  * Endpoint: POST https://zdrla73dj1.execute-api.eu-west-1.amazonaws.com/prod/v1/{tenant_name}/query
  * Request:
    ```
    {
      "fromDate": "2024-01-10",
      "toDate": "2024-01-13",
      "userId": "johndoe",
      "domain": "openai.com"
    }
    ```
  * Response:
    ```
    {
      status: 200,
      body: [
              {
                  "event_timestamp": "2024-01-11T01:42:50.234200+00:00",
                  "user_id": "johndoe",
                  "body": "what is the capital of India?"
              },
              {
                  "event_timestamp": "2024-01-11T01:42:50.234200+00:00",
                  "user_id": "johndoe",
                  "body": "what is the capital of India?"
              },
          ]
    }
    ```
* We can filter out the data using userId, domain that means request with or without userId and domain will also work.

# Steps to deploy this project
1. Run this command in Maven package `mvn clean package`
2. Run these command in CDK package
* `npm install aws-cdk-lib`
* `cdk synth`
* `cdk deploy --all`


