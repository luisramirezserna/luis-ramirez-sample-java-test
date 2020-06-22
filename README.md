# luis-ramirez-sample-java-test
Transaction API to test java skills

# Description
This API tests the java developer skills by exposing functionality for multiple Transaction task like add, get, report 

https://github.com/cesaralcancio/simple-test for more details  


# Requisites 
Please have installed following on you computer:

    1. Java 1.8
    2. Maven
    3. Git
        
# Download code
Please clone the repository by executing the following command on a terminal:

    git clone https://github.com/luisramirezserna/luis-ramirez-sample-java-test.git
    
# Run the application
	1.- move to the application forlder:
    	cd luis-ramirez-sample-java-test/
    
    2.- start app (dependencies will be downloaded)
    	a) directly 
    		mvn spring-boot:run
    	
    	b) generate executable jar file
    		mvn clean install
    		java -jar target/clip-transaction-api-0.0.1-SNAPSHOT.jar
    	 
# Test Application
Open another Terminal and execute the following commands:

	1.- API AUTHENTICATION
			
		the API has security implemented (JWT) in order to do execute all tasks available first you need to do a request to the authentication endpoint using credendials,
		if the authenticate is success the api will provide a token to use it on every request			
		{"name" : "clipuser","password" : "clip@JWT02"}
		
		Request : 
			curl --location --request POST 'http://localhost:8181/clip/authenticate' --header 'Content-Type: application/json' --data-raw '{"name" : "clipuser","password" : "clip@JWT02"}'
		
		Response : 	
			{"accessToken": "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJjbGlwdXNlciIsImV4cCI6MTU5MjYwOTI3NywiaWF0IjoxNTkyNTkxMjc3fQ.dPT1NUeUUwRf563jhlLR2Tzl2BCMlXa6aMuXag_ZIdjEQtvfS5ln2K3aWdDiNagKm6BriBNMOiCp7ki_Bb1L0Q"}
		
		use the accessToken on Authorization request header for next requests

    2.- ADD TRANSACTION 
    
    	this endpoint will allow you to add a transaction to the file, provide a user id in the url and the following data on body : {"amount" : 100,"description" : "test description","date" : "2019-12-12"}
    	
    	URL :
    		http://localhost:8181/clip/transaction/add/{userID}
    		
    		{userID} : integer value			
    				
		Request : 
			curl --location --request POST 'http://localhost:8181/clip/transaction/add/12345' --header 'Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJjbGlwdXNlciIsImV4cCI6MTU5MjYxODgyNywiaWF0IjoxNTkyNjAwODI3fQ.sSte3vG560eJY4rb0nP4xEwr18C_XSIw1oiSBizrbiuzlRG6Jk9M0krA_D4_y0SnptLBCGn3m6hbYqqrSgc7QA' --header 'Content-Type: application/json' \--data-raw '{    "amount" : 100,"description" : "test description","date" : "2019-12-12"}'
		
		Response : 	
			{"amount":100.0,"description":"test description","date":"2019-12-12","userID":12345,"transactionID":"44edb3c4-60e6-4373-8c0f-bc47e3d110fc"}
    		
    		multiple validations are done in the request like amount or date should not be empty or if the date format is not valid.
    		you will see an error message if this happens
    		
	3.- SHOW TRANSACTION

		this endpoint will show you a transaction based on the user id and the transaction id provided previously when the transaction was added 
		
		URL : 
			http://localhost:8181/clip/transaction/get/{userID}/{transactionID}
			
			{userID} : integer value
			{transactionID} : UUID value (example 44edb3c4-60e6-4373-8c0f-bc47e3d110fc)
		
		Request :
			curl --location --request GET 'http://localhost:8181/clip/transaction/get/12345/44edb3c4-60e6-4373-8c0f-bc47e3d110fc' --header 'Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJjbGlwdXNlciIsImV4cCI6MTU5MjYxODgyNywiaWF0IjoxNTkyNjAwODI3fQ.sSte3vG560eJY4rb0nP4xEwr18C_XSIw1oiSBizrbiuzlRG6Jk9M0krA_D4_y0SnptLBCGn3m6hbYqqrSgc7QA'
			
		Response : 
			{"amount":100.0,"description":"test description","date":"2019-12-12","userID":12345,"transactionID":"44edb3c4-60e6-4373-8c0f-bc47e3d110fc"}
			
	4.-	LIST TRANSACTIONS
	
		this endpoint will show you the transaction list associated to the user id provided on the URL
		
		URL :
			http://localhost:8181/clip/transaction/get/{userID}
			
			{userID} : integer value
		
		Request : 
			curl --location --request GET 'http://localhost:8181/clip/transaction/get/12345' --header 'Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJjbGlwdXNlciIsImV4cCI6MTU5MjYxODgyNywiaWF0IjoxNTkyNjAwODI3fQ.sSte3vG560eJY4rb0nP4xEwr18C_XSIw1oiSBizrbiuzlRG6Jk9M0krA_D4_y0SnptLBCGn3m6hbYqqrSgc7QA'
			
		Response :
			[{"amount":100.0,"description":"test description","date":"2019-12-12","userID":12345,"transactionID":"44edb3c4-60e6-4373-8c0f-bc47e3d110fc"},{"amount":100.0,"description":"test description","date":"2019-12-12","userID":12345,"transactionID":"6fafea04-cb93-4ce9-af09-00d424a7837a"}]
			
	5.-	SUM TRANSACTIONS
	
		This endpoint will show you the sum for all the transaction amounts associated to the provided user id
		
		URL : 
			http://localhost:8181/clip/transaction/get/sum/{userID}
			
			{userID} : integer value
			
		Request : 
			curl --location --request GET 'http://localhost:8181/clip/transaction/get/sum/12345' --header 'Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJjbGlwdXNlciIsImV4cCI6MTU5MjYxODgyNywiaWF0IjoxNTkyNjAwODI3fQ.sSte3vG560eJY4rb0nP4xEwr18C_XSIw1oiSBizrbiuzlRG6Jk9M0krA_D4_y0SnptLBCGn3m6hbYqqrSgc7QA'
		
		Response : 
			{"userID":12345,"sum":200.0}
			
	6.- TRANSACTIONS REPORT SERVICE
	
		This endpoint will show you a report for all the transactions associated to the user id accumulated by week, the week starts on Friday and finishes on Thursday or if it is the first day of the month then it should start the next week
		
		URL :
			http://localhost:8181/clip/transaction/get/report/{userID}
			
			{userID} : integer value
			
		Request : 
			curl --location --request GET 'http://localhost:8181/clip/transaction/get/report/12345' --header 'Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJjbGlwdXNlciIsImV4cCI6MTU5MjYxODgyNywiaWF0IjoxNTkyNjAwODI3fQ.sSte3vG560eJY4rb0nP4xEwr18C_XSIw1oiSBizrbiuzlRG6Jk9M0krA_D4_y0SnptLBCGn3m6hbYqqrSgc7QA'
			
		Response : 
			[{"amount":100.0,"userID":12345,"weekStartDate":"2019-12-06","weekFinishDate":"2019-12-12","quantity":1,"totalAmount":0.0}]	
		
	7.-	RANDMON SINGLE TRANSACTION
		
		This endpoint will show you a random transaction associated to any user id
		
		URL :
			http://localhost:8181/clip/transaction/get/report/
		
		Request :
			curl --location --request GET 'http://localhost:8181/clip/transaction/get/' --header 'Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJjbGlwdXNlciIsImV4cCI6MTU5MjYxODgyNywiaWF0IjoxNTkyNjAwODI3fQ.sSte3vG560eJY4rb0nP4xEwr18C_XSIw1oiSBizrbiuzlRG6Jk9M0krA_D4_y0SnptLBCGn3m6hbYqqrSgc7QA'
			
		Response : 
			{"amount":100.0,"description":"test description","date":"2019-12-12","userID":12345,"transactionID":"44edb3c4-60e6-4373-8c0f-bc47e3d110fc"}	
			

You can use the file "Clip Transaction API.postman_collection.json" added in the project folder to import all the request in post man to test the API in a better way
remember to run first the authentication and use the token in the response for next requests 

The data saved with the API will be store on the file "ClipData.txt" under the project folder, you do not have to create the file, it will be created automatically when requesting the ADD TRANSACTION for first time
if you request any other action and the file is not created yet, you will see an error message

The project provides a data file with data pre-loaded, it contains transactions to show the weekly report as the example provided on https://github.com/cesaralcancio/simple-test.
The file name is "ClipDataPreLoaded.txt" in order to use it just rename the file to "ClipData.txt"										

any question or comment send email to larsdesagitario@gmail.com

Thanks

