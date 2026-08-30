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

```bash
curl -X POST http://localhost:8080/deck-api/deck/create -H 'Content-Type: application/json'  -d '{
"deckType": "uno"       
}'
```

## Draw a card

```bash
curl -X PATCH http://localhost:8080/deck-api/deck/draw  -H 'Content-Type: application/json'  -d '{
"deckState":"rO0ABXcUAApzdGFuZGFyZDUyAAAANAAAAAB1cgACW0p4IAS1ErF1kwIAAHhwAAAAAHVxAH4AAAAAAAEAD////////w==","deckType":"standard52"
}'
```


```bash
curl -X PATCH http://localhost:8080/deck-api/deck/draw  -H 'Content-Type: application/json'  -d '{
"deckState":"rO0ABXcNAAN1bm8AAABsAAAAAHVyAAJbSnggBLUSsXWTAgAAeHAAAAAAdXEAfgAAAAAAAv//////////AAAP//////8=","deckType":"uno"
}'
```

## Return a card

### StandardCard format
```bash
curl -X PUT http://localhost:8080/deck-api/deck/return  -H 'Content-Type: application/json'  -d '{
"card":{"rank":"KING","suit":"CLUBS"},"deck":{"deckState":"rO0ABXcUAApzdGFuZGFyZDUyAAAAMwAAAAB1cgACW0p4IAS1ErF1kwIAAHhwAAAAAQAAAAACAAAAdXEAfgAAAAAAAQAP////////","deckType":"standard52"}
}'
```

### UnoCard format
```bash
curl -X PUT http://localhost:8080/deck-api/deck/return  -H 'Content-Type: application/json'  -d '{
"card":{"colour":"RED","rank":"KING","ordinal":0},"deck":{"deckState":"<base64>","deckType":"uno"}
}'
```

## Health monitoring 
```bash
curl http://localhost:8080/deck-api/health
```

# Adding new type of Deck

## Core
Implement `Card` and `OrderedDeck`.

Modify `src/main/resources/META-INF/services/ie.kambur.Cards.core.interfaces.OrderedDeck` to include the class name.

## Rest/Json
Serialisation of ShuffledDeck should suffice but one must implement `CardJsonSerialiser` like `StandardCardJsonSerialiser`.

Register the serialiser in `src/main/resources/META-INF/services/ie.kambur.Cards.service.interfaces.CardJsonSerialiser`.

# Useful stuff

## Tomcat manual deploy

```bash
bin/catalina.sh start
build/libs/deck-api.war ~/opt/tomcat/webapps 
```

## Deploymnet with container
See `docker/Dockerfile`. 

As simple as
```commandline
COPY build/libs/deck-api.war /usr/local/tomcat/webapps/
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

