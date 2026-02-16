# Ramblings

See https://kambur.ie/tips/shuffled/

# Tests

This is to be automated one day :)

## Create Deck

```bash
curl -X POST http://localhost:8080/deck-api/deck/create -H 'Content-Type: application/json'  -d '{
"deckType": "standard52"
}'
```

## Draw a card

```bash
curl -X PATCH http://localhost:8080/deck-api/deck/draw  -H 'Content-Type: application/json'  -d '{
"deckState":"rO0ABXcUAApzdGFuZGFyZDUyAAAANAAAAAB1cgACW0p4IAS1ErF1kwIAAHhwAAAAAHVxAH4AAAAAAAEAD////////w==","deckType":"standard52"
}'
```

## Return a card
```bash
curl -X PUT http://localhost:8080/deck-api/deck/return  -H 'Content-Type: application/json'  -d '{
"card":{"rank":"KING","suit":"CLUBS"},"deck":{"deckState":"rO0ABXcUAApzdGFuZGFyZDUyAAAAMwAAAAB1cgACW0p4IAS1ErF1kwIAAHhwAAAAAQAAAAACAAAAdXEAfgAAAAAAAQAP////////","deckType":"standard52"}
}'
```

## Health monitoring 
```bash
curl http://localhost:8080/deck-api/health
```

# Adding new type of Deck

## Core
Implement `Card` and `OrderedDeck`.

Modify `resources/META-INF/services/ie.kambur.Cards.core.interfaces.OrderedDeck` to include the name of the class


## Rest/Json
Serialisation of ShuffledDeck should suffice but one must implement `CardJsonSerialiser` like `StandardCardJsonSerialiser`

# Useful stuff

## Tomcat manual deploy

```bash
bin/catalina.sh start
build/libs/deck-api.war ~/opt/tomcat/webapps 
```

## K8s service peekaboo

```bash
wget http://ramblings-app.ramblings.svc.cluster.local:8080/deck-api/health
```

```bash
curl -X POST https://deck-api.api.kambur.ie/deck-api/deck/create -H 'Content-Type: application/json'  -d '{
"deckType": "standard52"
}'

curl -X PUT https://deck-api.api.kambur.ie/deck-api/deck/return  -H 'Content-Type: application/json'  -d '{
"card":{"rank":"KING","suit":"CLUBS"},"deck":{"deckState":"rO0ABXcUAApzdGFuZGFyZDUyAAAAMwAAAAB1cgACW0p4IAS1ErF1kwIAAHhwAAAAAQAAAAACAAAAdXEAfgAAAAAAAQAP////////","deckType":"standard52"}
}'

curl -X PATCH  https://deck-api.api.kambur.ie/deck-api/deck/draw  -H 'Content-Type: application/json'  -d '{
"deckState":"rO0ABXcUAApzdGFuZGFyZDUyAAAANAAAAAB1cgACW0p4IAS1ErF1kwIAAHhwAAAAAHVxAH4AAAAAAAEAD////////w==","deckType":"standard52"
}'
```