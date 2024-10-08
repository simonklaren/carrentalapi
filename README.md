# Car Rental API
---
### Gemaakt door:
**Simon Klaren**

Dit project is ontwikkeld door Simon Klaren. Voor vragen of opmerkingen kun je contact opnemen simonklaren.nl.

Deze Car Rental API biedt de mogelijkheid om voertuigen te beheren. De API ondersteunt volledige CRUD-functionaliteit (Create, Read, Update, Delete) en is ontwikkeld met Ktor en Kotlin.

### Functionaliteiten:
- Voertuigen toevoegen, opvragen, bijwerken en verwijderen.
- Toepassing van Ktor Exposed voor de opslag van voertuiggegevens.
- API-endpoints in JSON-formaat.

### Overzicht van de API-routes:

| HTTP Methode | Endpoint         | Beschrijving                           |
|--------------|------------------|---------------------------------------|
| `GET`        | `/vehicles`       | Haal alle beschikbare voertuigen op.   |
| `POST`       | `/vehicles`       | Voeg een nieuw voertuig toe.           |
| `PUT`        | `/vehicles/{id}`  | Werk een bestaand voertuig bij op ID.  |
| `DELETE`     | `/vehicles/{id}`  | Verwijder een voertuig op basis van ID.|

## Voorbeeld-requests en -responses

### 1. `GET /vehicles`
- **Beschrijving:** Haal een lijst van alle beschikbare voertuigen op.
- **Voorbeeld-response:**
  [
  {
  "id": 1,
  "brand": "Toyota",
  "model": "Corolla",
  "type": "ICE",
  "pricePerDay": 50.0
  },
  {
  "id": 2,
  "brand": "Tesla",
  "model": "Model 3",
  "type": "BEV",
  "pricePerDay": 100.0
  },
  {
  "id": 3,
  "brand": "Hyundai",
  "model": "Nexo",
  "type": "FCEV",
  "pricePerDay": 80.0
  }
  ]

### 2. `POST /vehicles`
- **Beschrijving:** Voeg een nieuw voertuig toe.
- **Voorbeeld-request:**
  {
  "id": 4,
  "brand": "Ford",
  "model": "Fiesta",
  "type": "ICE",
  "pricePerDay": 40.0
  }
- **Voorbeeld-response:**
  {
  "id": 4,
  "brand": "Ford",
  "model": "Fiesta",
  "type": "ICE",
  "pricePerDay": 40.0
  }

### 3. `PUT /vehicles/{id}`
- **Beschrijving:** Werk een bestaand voertuig bij op basis van ID.
- **Voorbeeld-request:**
  {
  "id": 4,
  "brand": "Ford",
  "model": "Fiesta",
  "type": "ICE",
  "pricePerDay": 50.0
  }
- **Voorbeeld-response:**
  {
  "id": 4,
  "brand": "Ford",
  "model": "Fiesta",
  "type": "ICE",
  "pricePerDay": 50.0
  }

### 4. `DELETE /vehicles/{id}`
- **Beschrijving:** Verwijder een voertuig op basis van ID.
- **Voorbeeld-response:**
  {
  "id": 4,
  "brand": "Ford",
  "model": "Fiesta",
  "type": "ICE",
  "pricePerDay": 50.0
  }
