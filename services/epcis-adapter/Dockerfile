FROM golang

WORKDIR /go/src/app
COPY . .

RUN go get -d -v
RUN go build -o main .

EXPOSE 3080

CMD ["/go/src/app/main"]