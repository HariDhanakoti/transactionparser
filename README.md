Application parses the csv file and insert each row as record in database.

Application will insert the records to the in memory database after parsing the entries from the CSV file. I have exposed few endpoints to check the balance and listing all transactions.


# Tech stack used
* Java 8
* Gradle 5.0
* JPA
* Groovy for unit testing
* Jacoco for coverage report
* Spring boot framework
* Super CSV to read and parse CSV files
* H2 - In memory database

### To build:

gradle clean build

### To run:

gradle clean run

### To run unit and endpoint test:
gradle clean test

### API:

1. Get available balance - Http POST method with limits start and end date and time and taking accountId as the path param

> http://locahost:9080/api/{accountId}/getAvailableBalance

> API will return the available balance which will include the reversal transaction

> Test the API using curl command:
```curl -d '{"startDate":"20/10/2018 11:20:44", "endDate":"21/10/2018 12:20:44"}' -H "Content-Type: application/json" -X POST http://localhost:9080/api/ACC334455/getAvailableBalance```

> Output received in JSON:
```{"accountId":"ACC334455","availableBalance":53.25,"fromDate":"20/10/2018 11:20:44","toDate":"21/10/2018 12:20:44"}```
---
2. Get current balance - Http POST method with limits start and end date and time and taking accountId as the path param

> API will return the available balance which will doesn't include the reversal transaction

> http://locahost:9080/api/{accountId}/getCurrentBalance

> Test the API using curl command:
```curl -d '{"startDate":"20/10/2018 11:20:44", "endDate":"21/10/2018 12:20:44"}' -H "Content-Type: application/json" -X POST http://localhost:9080/api/ACC334455/getCurrentBalance```

Output received in JSON:

```{"accountId":"ACC334455","availableBalance":42.75,"fromDate":"20/10/2018 11:20:44","toDate":"21/10/2018 12:20:44"}```
---
3. Get all transactions from database

> curl -X GET http://localhost:9080/api/getAllTransactions

> API will return the balances available balance which will doesn't include the reversal transaction

> Test the API using curl command:

> curl -X GET http://localhost:9080/api/getAllTransactions

Output received in JSON:
```{"transactionId":"TX10001","fromAccountId":"ACC334455","toAccountId":"ACC778899","createdAt":"2018-10-20T01:47:55.000+0000","amount":25.00,"transactionType":"PAYMENT","relatedTransaction":null},{"transactionId":"TX10002","fromAccountId":"ACC334455","toAccountId":"ACC998877","createdAt":"2018-10-20T06:33:43.000+0000","amount":10.50,"transactionType":"PAYMENT","relatedTransaction":null},{"transactionId":"TX10003","fromAccountId":"ACC998877","toAccountId":"ACC778899","createdAt":"2018-10-20T07:00:00.000+0000","amount":5.00,"transactionType":"PAYMENT","relatedTransaction":null},{"transactionId":"TX10004","fromAccountId":"ACC334455","toAccountId":"ACC998877","createdAt":"2018-10-20T08:45:00.000+0000","amount":10.50,"transactionType":"REVERSAL","relatedTransaction":"TX10002"},{"transactionId":"TX10005","fromAccountId":"ACC334455","toAccountId":"ACC778899","createdAt":"2018-10-20T22:30:00.000+0000","amount":7.25,"transactionType":"PAYMENT","relatedTransaction":null}]```
---
4. List down the transactions in the given time frame (start and end date and time)

> API will return all the transactions found int he given time frame

> http://locahost:9080/api/getCugetTimeBoundTransactionsrentBalance

> Test the API using curl command:

> curl -d '{"startDate":"20/10/2018 11:20:44", "endDate":"21/10/2018 12:20:44"}' -H "Content-Type: application/json" -X POST http://localhost:9080/api/getTimeBoundTransactions

Output received in JSON:
```[{"transactionId":"TX10001","fromAccountId":"ACC334455","toAccountId":"ACC778899","createdAt":"2018-10-20T01:47:55.000+0000","amount":25.00,"transactionType":"PAYMENT","relatedTransaction":null},{"transactionId":"TX10002","fromAccountId":"ACC334455","toAccountId":"ACC998877","createdAt":"2018-10-20T06:33:43.000+0000","amount":10.50,"transactionType":"PAYMENT","relatedTransaction":null},{"transactionId":"TX10003","fromAccountId":"ACC998877","toAccountId":"ACC778899","createdAt":"2018-10-20T07:00:00.000+0000","amount":5.00,"transactionType":"PAYMENT","relatedTransaction":null},{"transactionId":"TX10004","fromAccountId":"ACC334455","toAccountId":"ACC998877","createdAt":"2018-10-20T08:45:00.000+0000","amount":10.50,"transactionType":"REVERSAL","relatedTransaction":"TX10002"}]```


### Future Work:
------------

- The JSON responses can be displayed on UI in a presentable manner.
- Including api tests using npm mocha.
- Include checkstyle to provide a presentabl unique style of code.
- Can add CI and CD feature to the branch.