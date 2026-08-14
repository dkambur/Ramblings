#!/usr/bin/env bash
set -euo pipefail

API_URL="${API_URL:-http://localhost:8080/deck-api/deck/draw}"
DECK_TYPE="uno"
DECK_STATE="rO0ABXcNAAN1bm8AAABsAAAAAHVyAAJbSnggBLUSsXWTAgAAeHAAAAAAdXEAfgAAAAAAAv//////////AAAP//////8="

echo "ordinal,colour,rank"

while true; do
  payload=$(
    jq -nc \
      --arg deckState "$DECK_STATE" \
      --arg deckType "$DECK_TYPE" \
      '{deckState: $deckState, deckType: $deckType}'
  )

  response=$(
    curl --fail-with-body --silent --show-error \
      --request PATCH "$API_URL" \
      --header 'Content-Type: application/json' \
      --data "$payload"
  ) || {
    echo "Deck exhausted or API error." >&2
    exit 0
  }

  ordinal=$(jq -r '.card.ordinal // empty' <<<"$response")
  colour=$(jq -r '.card.colour // empty' <<<"$response")
  rank=$(jq -r '.card.rank // ""' <<<"$response")
  DECK_STATE=$(jq -r '.deck.deckState // empty' <<<"$response")
  DECK_TYPE=$(jq -r '.deck.deckType // "uno"' <<<"$response")

  if [[ -z "$ordinal" || -z "$colour" || -z "$DECK_STATE" ]]; then
    echo "Unexpected API response:" >&2
    jq . <<<"$response" >&2
    exit 1
  fi

  printf '%s,%s,%s\n' "$ordinal" "$colour" "$rank"
done