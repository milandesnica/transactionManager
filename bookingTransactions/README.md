Docker image
============

To build the image, stand at the root of the project and build the project with:
mvn clean install

Build the image with:
docker build -t <image_name> .

Start the container with:
docker run  -p 8080:8080 --name <container_name> <image_name>

About the system
================
This Spring Boot API, defined in the `TransactionController` class, provides the following functionalities:

1. **Receive Batch Transactions**:
    - **Endpoint**: `POST /transaction`
    - **Description**: Accepts a batch of transactions in (almost)CSV format as a request body. It uses the `CSVService` to parse the CSV data into a list of `Transaction` objects and then sends them to the `TransactionService` for processing.
    - **Response**:
        - If any transactions are rejected during processing, it returns a JSON response with a list of rejected transactions and a `400 Bad Request` status.
        - If all transactions are processed successfully, it returns a `200 OK` status.

2. **Get All Users**:
    - **Endpoint**: `GET /users`
    - **Description**: Retrieves a list of all users from the `TransactionService`.
    - **Response**: Returns a JSON response containing a list of all user objects.



To test
=======
1. Run the application as described above (or start it in intellij)
2. use postman or curl: to get all users (so you know what you can test on): curl  localhost:8080/users --header 'Content-Type: application/json'
3. To post a batch of transactions: curl --location 'localhost:8080/transaction' \
   --header 'Content-Type: text/plain' \
   --data-raw '"Kia,Karlsson,kia@karlsson.com,109,TR0002"'
4. To post multiple transactions the command could look like:
   curl --location 'localhost:8080/transaction' \
   --header 'Content-Type: text/plain' \
   --data-raw '"John,Doe,john@doe.com,0,TR0001"
   "John,Doe1,john@doe1.com,0,TR0001"
   "John,Doe2,john@doe2.com,0,TR0003"
   "John,Doe,john@doe.com,0,TR0004"
   "John,Doe,john@doe.com,0,TR0005"
   '
Limitations
===========
The application is very simple first iteration.
1. You cannot add, remove or update users from API (so you need to restart the application to reset the data)
2. The application does not have any security
3. The application does not have any logging