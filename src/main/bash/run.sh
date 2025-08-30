#!/bin/bash

# Initial deck state
DECK_STATE="rO0ABXcUAApzdGFuZGFyZDUyAAAANAAAAAB1cgACW0p4IAS1ErF1kwIAAHhwAAAAAHVxAH4AAAAAAAEAD////////w=="

# API endpoint
URL="http://localhost:8080/deck-api/deck/draw"

echo "Starting to draw 53 cards..."
echo

for i in {1..53}; do
    echo "Draw #$i:"

    # Make the API call and capture the response
    RESPONSE=$(curl -s -X PATCH "$URL" \
        -H 'Content-Type: application/json' \
        -d "{\"deckState\":\"$DECK_STATE\",\"deckType\":\"standard52\"}")

    # Print the response
    echo "$RESPONSE"

    # Extract the new deck state from the response using jq
    # If jq is not available, uncomment the alternative method below
    NEW_DECK_STATE=$(echo "$RESPONSE" | jq -r '.deck.deckState')

    # Alternative method without jq (uncomment if jq is not available):
    # NEW_DECK_STATE=$(echo "$RESPONSE" | sed -n 's/.*"deckState":"\([^"]*\)".*/\1/p')

    # Check if we got a valid response
    if [ "$NEW_DECK_STATE" != "null" ] && [ "$NEW_DECK_STATE" != "" ]; then
        DECK_STATE="$NEW_DECK_STATE"
    else
        echo "Error: Could not extract deckState from response"
        echo "Response was: $RESPONSE"
        break
    fi

    echo "Next deckState: $DECK_STATE"
    echo "----------------------------------------"

    # Optional: Add a small delay to be nice to the server
    # sleep 0.1
done

echo "Finished drawing cards!"