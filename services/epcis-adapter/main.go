package main

import (
	"log"
	"net/http"
)

func main() {
	log.Println("Server starting ...")

	//http.HandleFunc("/", RootEndpoint)
	//http.HandleFunc("/sub", SubscribeEndpoint)
	//http.HandleFunc("/ws", WsEndpoint)

	http.HandleFunc("/query/location", GetObjectEventsbyLocationEndpoint)
	log.Fatal(http.ListenAndServe(":3080", nil))

	/* repo := Epcis{"http://localhost:4080/epcis-repository-0.5.0/query"}
	data, _ := repo.GetObjectEventsbyLocation("urn:epc:tag:sgtin-96:1.76300544.07470.1")
	fmt.Println(string(data)) */
}
