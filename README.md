
# Car Rental API
---

### Gemaakt door:
**Simon Klaren**

Dit project is ontwikkeld door Simon Klaren. Voor vragen of opmerkingen kun je contact opnemen via de site: [simonklaren.nl](https://simonklaren.nl).

Deze Car Rental API biedt de mogelijkheid om voertuigen te beheren. De API ondersteunt volledige CRUD-functionaliteit (Create, Read, Update, Delete) en is ontwikkeld met Ktor en Kotlin.

### Functionaliteiten:
- Volledige CRUD-functionaliteit voor voertuigen (toevoegen, opvragen, bijwerken en verwijderen).
- Ondersteuning voor **JWT-authenticatie** voor gebruikersregistratie en -login.
- **Foto-upload functionaliteit** voor voertuigen.
- Toepassing van Ktor Exposed voor de opslag van voertuiggegevens.
- API-endpoints in JSON-formaat.

### Overzicht van de API-routes:

| HTTP Methode | Endpoint               | Beschrijving                                |
|--------------|------------------------|--------------------------------------------|
| `GET`        | `/vehicles`            | Haal alle beschikbare voertuigen op.       |
| `POST`       | `/vehicles`            | Voeg een nieuw voertuig toe.               |
| `PUT`        | `/vehicles/{id}`       | Werk een bestaand voertuig bij op ID.      |
| `DELETE`     | `/vehicles/{id}`       | Verwijder een voertuig op basis van ID.    |
| `POST`       | `/register`            | Registreer een nieuwe gebruiker.           |
| `POST`       | `/login`               | Log in met bestaande gebruikersgegevens.   |
| `POST`       | `/vehicles/{id}/upload-image` | Upload een afbeelding voor een voertuig op basis van ID. |

## Voorbeeld-requests en -responses

### 1. `GET /vehicles`
- **Beschrijving:** Haal een lijst van alle beschikbare voertuigen op.
- **Voorbeeld-response:**
  ```json
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
  ```

### 2. `POST /vehicles`
- **Beschrijving:** Voeg een nieuw voertuig toe.
- **Voorbeeld-request:**
  ```json
  {
    "brand": "Ford",
    "model": "Fiesta",
    "type": "ICE",
    "pricePerDay": 40.0
  }
  ```
- **Voorbeeld-response:**
  ```json
  {
    "id": 4,
    "brand": "Ford",
    "model": "Fiesta",
    "type": "ICE",
    "pricePerDay": 40.0
  }
  ```

### 3. `PUT /vehicles/{id}`
- **Beschrijving:** Werk een bestaand voertuig bij op basis van ID.
- **Voorbeeld-request:**
  ```json
  {
    "brand": "Ford",
    "model": "Fiesta",
    "type": "ICE",
    "pricePerDay": 50.0
  }
  ```
- **Voorbeeld-response:**
  ```json
  {
    "id": 4,
    "brand": "Ford",
    "model": "Fiesta",
    "type": "ICE",
    "pricePerDay": 50.0
  }
  ```

### 4. `DELETE /vehicles/{id}`
- **Beschrijving:** Verwijder een voertuig op basis van ID.
- **Voorbeeld-response:**
  ```json
  {
    "id": 4,
    "brand": "Ford",
    "model": "Fiesta",
    "type": "ICE",
    "pricePerDay": 50.0
  }
  ```

### 5. `POST /auth/register`
- **Beschrijving:** Registreer een nieuwe gebruiker.
- **Voorbeeld-request:**
  ```json
  {
    "email": "user@example.com",
    "password": "securepassword"
  }
  ```
- **Voorbeeld-response:** (indicatief)
  ```json
  {
    "message": "User registered successfully"
  }
  ```

### 6. `POST /auth/login`
- **Beschrijving:** Log in met bestaande gebruikersgegevens.
- **Voorbeeld-request:**
  ```json
  {
    "email": "user@example.com",
    "password": "securepassword"
  }
  ```
- **Voorbeeld-response:** (indicatief, inclusief JWT-token)
  ```json
  {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
  }
  ```

### 7. `POST /vehicles/{id}/upload-image`
- **Beschrijving:** Upload een afbeelding voor een voertuig op basis van ID.
- **Voorbeeld-response:** (indicatief)
  ```json
  {
    "message": "Image uploaded successfully",
    "imagePath": "/uploads/vehicles/4/image.jpg"
  }
  ```

## Authenticatie
De API gebruikt **JWT-authenticatie** voor beveiligde endpoints. Na een succesvolle login ontvangt de gebruiker een JWT-token, dat moet worden toegevoegd aan de `Authorization` header bij verzoeken naar beveiligde routes.

**Voorbeeld:**  
```
Authorization: Bearer <JWT-token>
```
