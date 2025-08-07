# Ramblings

See https://kambur.ie/tips/shuffled/

# Tests

## Create Deck

```
curl -X POST http://localhost:8080/deck-api/deck/create -vvv  -H 'Content-Type: application/json'  -d '{
"deckType": "standard52"
}'
```