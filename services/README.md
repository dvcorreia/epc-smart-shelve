# Platform

## To run

0. `cd` in to `./services`
1. Run `docker-compose up --build`
2. Configure the reader. You can use the configuration software developed in `dev` directory of with Eclipse and [LLRP commander](https://fosstrak.github.io/llrp/index.html)
3. Configure the middleware using the [Standalone client](https://fosstrak.github.io/fc/download.html) (could not get the Web-Based Client to configure the middleware)
4. Configure the capture application to send `ECReports` to the EPCIS repository
5. Verify in the terminal if everything is sending requests

## Services

### capture

The capture service containerizes the [Fosstrak Capturing Application](https://fosstrak.github.io/capturingapp/index.html) inside a Docker container.
Inside it can also be found a directory called **messages** with some `ECReport` examples.
In the **drools** directory it can be found the drools rules used. Some are not working fully working. Fosstrak runs on a old drools version which is hard to get good documentation for.

### epcis

Contains some files for the [EPCIS repository](https://fosstrak.github.io/epcis/index.html) service containerized with Docker. 

### epcis-adapter

Is a very crude and incomplete implementation in go of the [Fosstrak EPCIS web adapter](https://fosstrak.github.io/epcis/docs/webadapter-guide.html) which I could not get to work.
The service is implemented in Golang and serves as a proxy between EPCIS repository and web application. HTTP request are converter in EPCIS queries and sent to the EPCIS repository. The requested data is delivered in XML format which is converted to JSON and sent to the web application.
There is also an web sockets implementation for "real-time" data fetch, but is not used.

### epcis-db

EPCIS database were EPCIS data is stored. It runs on a mysql db instance.
In the current `docker-compose` description this service `Dockerfile` is not used and instead is used the `cloud4things/fosstrak_db` image.

### middleware

Containerizes the [Fosstrak ALE Middleware](https://fosstrak.github.io/fc/index.html) with Docker. 
Static `ECSpec` and `LRSpec` definitions do not seem to work. I've tried running the service in a few different versions of Java and Tomcat, without success.

### web

Contains a web application capable of monitoring the shelve inventory.
Developed in React Js and hosted with NGINX.


### others

In the `docker-compose.yml` file we can see other services.
A reverse `proxy` with NGINX used has centralized public endpoint for the platform.
A `dump` which saves all traffic in the platform network for analysis.
A `adminer` used to visualize DB data.