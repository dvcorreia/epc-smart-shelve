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

	log.Println(string(b))
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
		if err := ws.WriteJSON(data); err != nil {
			log.Println(err)
			return
		}
	}
}
