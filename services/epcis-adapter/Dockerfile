FROM golang
WORKDIR $GOPATH/src/epcis-adapter/
COPY . .

# Fetch dependencies.
RUN go get -d -v

EXPOSE 3080

RUN go build .

ENTRYPOINT ["./epcis-adapter"]