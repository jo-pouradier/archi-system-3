# CardService

This service is responsible for managing the cards of the users.

## Endpoints

All endpoints are prefixed with `/card`.

### GET '/'

Just return a Status 200.

### GET '/getCards'

Return all cards.

### GET '/:uudi'

Get the card with the given uuid. If the card does not exist, return a NOT_FOUND status.

### GET '/getCardsByOwnerUUID'

Get all cards of the user with the given uuid. If the user does not exist, return a NOT_FOUND status.

### POST '/addCard'

Add a new Card
body:
```json
{
  "name": "first-card",
  "description": "a test card2",
  "imageUrl": "",
  "family": "fire",
  "affinity": "water",
  "hp": 100,
  "attack": 45,
  "defense": 30,
  "energy": 25
}
```

### DELETE '/:uuid'

Delete the card with the given uuid. If the card does not exist, return a NOT_FOUND status. 