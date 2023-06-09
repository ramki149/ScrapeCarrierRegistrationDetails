# USDOT WEB SCRAPING 

# Prerequisite
* JDK1.8 https://www.oracle.com/java/technologies/javase/javase8-archive-downloads.html
* Kafka [OPTIONAL] https://kafka.apache.org/quickstart - Please find the running commands at the end of the section
* MAVEN - https://maven.apache.org/download.cgi - To run .JAR build on local

# Application Use Case
* Application support added to scrape the vehicle type breakouts and information about goods carried. 
* Support added to enable standard REST APIs to scrape the "Carrier Registration Details" page for each operator in the "Motor Carrier Census Information" file. 
* Application pulls the lists of cargo carried(CargoCarried.csv) as well as the vehicle type breakout table(VehicleTypeBreakdown.csv).

# API DESIGN
* Real time REST Endpoint http://localhost:8080//carrier/{dot_number} scrapes cargo & vehicle breakout info for single dot number.
* Batch Job REST End point http://localhost:8080/executebatch provides support for batch/bulk scraping. It reads MotorCarrierCensusInformation.csv for DOT numbers
* Async Kafka Stream processing support added to enable ASYNC scraping in case Web Page site is down or our local server is undergoing maintenance.

# Technical Design
* @RestController CensusApiGateway.java file adds support for REST API Endpoints
* @Component ConvertToCsv.java uses OPENCSV library for CSV write
* Serivce class VehicleTypeBreakdownInfo.java provides scaraping algorithm
* @KafkaListener TopicListener.java provides support for Kafka Consumer support. It listens to TOPIC 'census-cargo-topic'

# CI/CD Design [Not Tested]
* Application shall run on Dockerized containers and thereby can be easily deployed to AWS based ECS clusters
* Docker file added to run .JAR [Requires testing & deployment]
* Jenkins file added to enabled for Jenkins pipeline based deployment automation

# AWS Cloud Infrastructure [Not implemented yet]
* APIs are deployed under AWS ECS
* For Machine Learning/Batch Processing support, AWS Lambda(implemented using Python boto3) deployed to observe "Carrier Registration Details" page for availablity.
* Lambda shall trigger AWS EMR to run batch processing. It can use SPOT EC2 instances to save COST.
* Spark based AWS EMR shall run the scraping algoirthm https://docs.aws.amazon.com/emr/latest/ReleaseGuide/emr-spark.html

# Fault Tolerence
* In order to handle the problem of USDOT site goes down, 
	1) Download 'FMCSA_CENSUS1_2023Apr' file when the site is available.
	2) Extract DOT numbers, and use light weight Kafka Consumer to read Carrier Registration Detail and store it in the Kafka TOPIC(S)
	3) Above can be achived via Java code Document doc = Jsoup.connect("https://ai.fmcsa.dot.gov/SMS/Carrier/" + carrierId + "/CarrierRegistration.aspx").get();
	4) Store Document as HTML text and do Scrape operation without need for USDOT site.
	5) Another way to scrape is via execute scrape batch processing which can be done without need  of USDOT website.
	6) It is highly recommended to run scrape operation during offline hours (like midnight) to avoid overloading USDOT site

# Commands to run the application
* git clone https://github.com/ramki149/ScrapeCarrierRegistrationDetails
* cd ScrapeCarrierRegistrationDetails
* mvn clean compile install package
* java -jar cargointerview-0.0.1-SNAPSHOT
	[This starts the application at 8080. Sample http requests below]
* To hit real time API http://localhost:8080/carrier/{DOT_number}  [ex. http://localhost:8080/carrier/1000357]
* To begin batch processing, Push "Carrier Census Information" file contents to MotorCarrierCensusInformation.csv. Now call http://localhost:8080/executebatch
* For Kafka streaming (optional)
	**Zookeeper start:**
	C:\YOUR LOCAL FOLDER PATH\kafka_2.13-3.4.0\.\bin\windows\zookeeper-server-start.bat .\config\zookeeper.properties
	**Server start**
	C:\YOUR LOCAL FOLDER PATH\kafka_2.13-3.4.0\.\bin\windows\kafka-server-start.bat .\config\server.properties
	**Create the Topic:**
	C:\YOUR LOCAL FOLDER PATH\kafka_2.13-3.4.0\.\bin\windows\kafka-topics.bat --create --bootstrap-server localhost:2181 --replication-factor 1 --partitions 1 --topic census-cargo-topic
	**Produce the message**
	C:\YOUR LOCAL FOLDER PATH\kafka_2.13-3.4.0\.\bin\windows\kafka-console-producer.bat --broker-list localhost:9092 --topic census-cargo-topic
	**Consume the topic:**
	C:\YOUR LOCAL FOLDER PATH\kafka_2.13-3.4.0\.\bin\windows\kafka-console-consumer.bat --bootstrap-server localhost:9092 --topic census-cargo-topic --from-beginning
	**[Type DOT number to PRODUCER] - This will call scraping API**
