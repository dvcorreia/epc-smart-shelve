package main

import (
	"io/ioutil"
	"log"
	"net/http"

	"github.com/gorilla/websocket"
)

// C is the channel were the request will flow throught
var C = make(chan string)

var upgrader = websocket.Upgrader{
	ReadBufferSize:  1024,
	WriteBufferSize: 1024,
}

// RootEndpoint is the endpoint were the EPCIS messages will come to
func RootEndpoint(w http.ResponseWriter, r *http.Request) {
	log.Println("Data received from EPCIS ...")
	b, err := ioutil.ReadAll(r.Body)
	defer r.Body.Close()
	if err != nil {
		http.Error(w, err.Error(), 500)
		return
	}

	log.Println("Data received: " + string(b))
	C <- string(b)
}

// SubscribeEndpoint is where you can send subscribe msg the EPCIS repo
func SubscribeEndpoint(w http.ResponseWriter, r *http.Request) {
	return
}

// WsEndpoint is were you can subscribe to the websocket endpoint
func WsEndpoint(w http.ResponseWriter, r *http.Request) {
	// Ignore CORS
	upgrader.CheckOrigin = func(r *http.Request) bool { return true }

	// Upgrade this connection to a WebSocket
	ws, err := upgrader.Upgrade(w, r, nil)
	if err != nil {
		log.Println(err)
	}

	log.Println("Client connected using websockets ...")

	for {
		data := <-C
		log.Println("Sending data to client ...")

		env, err := ParseEPCISQueryDocument([]byte(data))
		if err != nil {
			log.Println(err)
		}

		if err := ws.WriteJSON(env); err != nil {
			log.Println(err)
			return
		}
	}
}

// GetObjectEventsbyLocationEndpoint ...
func GetObjectEventsbyLocationEndpoint(w http.ResponseWriter, r *http.Request) {
	params := r.URL.Query()
	locations, ok := params["location"]

	if !ok || len(locations[0]) < 1 {
		log.Println("URL Param 'location' is missing!")
		return
	}

	var data []byte
	var err error

	epcis := Epcis{"http://localhost:4080/epcis-repository-0.5.0/query"}

	eventTime, ok := params["time"]
	if !ok || len(eventTime[0]) < 1 {
		data, err = epcis.GetObjectEventsbyLocation(locations[0])
	} else {
		data, err = epcis.GetObjectEventbyLocationAndTime(locations[0], eventTime[0])
	}

	if err != nil {
		log.Println("Error retrieving data from EPCIS repository")
	}

	w.Header().Set("Content-Type", "application/json")
	w.Write(data)
}
