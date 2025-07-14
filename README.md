# Campaign App

## Setup

1. **Generate Proto Stubs**  
   ```bash
   cd proto-catalog
   mvn clean install
    ```
---
### Documentation

| Service               | Swagger UI URL                          | Raw OpenAPI JSON URL               |
|-----------------------|-----------------------------------------|------------------------------------|
| Campaign Service      | `http://localhost:10000/docs/campaign-service`   | `http://localhost:10000/v3/api-docs`  |
| Stats Service         | `http://localhost:10000/docs/stats-service`      | `http://localhost:10000/v3/api-docs`  |
| Auth Service          | `http://localhost:10000/docs/auth-service`       | `http://localhost:10000/v3/api-docs`  |

--

### Auth Service (via API Gateway)

_All calls go through the gateway at_ `http://localhost:10000`

| HTTP   | Path                         | Description                                               | Request Body                            | Response Body                   |
|--------|------------------------------|-----------------------------------------------------------|-----------------------------------------|---------------------------------|
| POST   | `/auth/register`             | Create a new user and return a JWT token.                 | `{ "email": "...", "password": "..." }` | `{ "token": "..." }`            |
| POST   | `/auth/login`                | Authenticate existing user and return JWT.                | `{ "email": "...", "password": "..." }` | `{ "token": "..." }`            |
| GET    | `/auth/validate`             | Validate the provided JWT.                                | — (Authorization header: `Bearer ...`)  | HTTP 200 OK (empty body)        |
| GET    | `/auth/all`                  | **(ADMIN only)** List all users.                          | — (Authorization header)                | `[{ UserResponseDTO }, …]`      |
| POST   | `/auth/edit/{id}`            | **(ADMIN only)** Update a user’s email/password/role.     | `{ "email?", "password?", "role?" }`    | `{ UserResponseDTO }`           |

---

### Campaign Service (via API Gateway)

_All calls go through the gateway at_ `http://localhost:10000`

#### Campaigns

| HTTP   | Path                                            | Description                                         | Request Body                                           | Response Body                                  |
|--------|-------------------------------------------------|-----------------------------------------------------|--------------------------------------------------------|-----------------------------------------------|
| GET    | `http://localhost:10000/api/campaign/all`       | Retrieve **all** campaigns.                         | —                                                      | `[{ CampaignResponseDTO }, …]`                |
| GET    | `http://localhost:10000/api/campaign/active`    | Retrieve only **active** campaigns.                 | —                                                      | `[{ CampaignResponseDTO }, …]`                |
| GET    | `http://localhost:10000/api/campaign/{id}`      | Retrieve a single campaign by its UUID.             | —                                                      | `{ CampaignResponseDTO }`                     |
| POST   | `http://localhost:10000/api/campaign/new`       | Create a new campaign.                              | `{ name, description?, product, keywords[], bid_amount, campaign_amount?, city, radius? }` | `{ CampaignResponseDTO }`                     |
| PATCH  | `http://localhost:10000/api/campaign/update/{id}` | Update fields of an existing campaign.              | Same schema as “new” (all fields optional)             | `{ CampaignResponseDTO }`                     |
| POST   | `http://localhost:10000/api/campaign/{id}/start`  | Mark a campaign as **active**.                      | —                                                      | `{ CampaignResponseDTO }` (with `active=true`) |
| POST   | `http://localhost:10000/api/campaign/{id}/stop`   | Mark a campaign as **inactive**.                    | —                                                      | `{ CampaignResponseDTO }` (with `active=false`)|
| DELETE | `http://localhost:10000/api/campaign/delete/{id}` | Permanently delete a campaign by its UUID.          | —                                                      | HTTP 200 OK (empty body)                      |

#### Products

| HTTP   | Path                                                 | Description                                  | Request Body                          | Response Body                           |
|--------|------------------------------------------------------|----------------------------------------------|---------------------------------------|-----------------------------------------|
| GET    | `http://localhost:10000/api/product/get`             | List **all** products.                       | —                                     | `[{ ProductResponseDTO }, …]`           |
| GET    | `http://localhost:10000/api/product/get/{id}`        | Retrieve a product by its UUID.              | —                                     | `{ ProductResponseDTO }`                |
| POST   | `http://localhost:10000/api/product/new`             | Create a new product.                        | `{ name, description? }`              | `{ ProductResponseDTO }`                |
| PATCH  | `http://localhost:10000/api/product/update/{id}`     | Update fields of an existing product.        | `{ name?, description? }`             | `{ ProductResponseDTO }`                |
| DELETE | `http://localhost:10000/api/product/delete/{id}`     | Delete a product by its UUID.                | —                                     | HTTP 200 OK (empty body)                |

#### Cities

| HTTP   | Path                                               | Description                                  | Request Body                          | Response Body                           |
|--------|----------------------------------------------------|----------------------------------------------|---------------------------------------|-----------------------------------------|
| GET    | `http://localhost:10000/api/city/get`              | List **all** cities.                         | —                                     | `[{ CityResponseDTO }, …]`              |
| GET    | `http://localhost:10000/api/city/get/{id}`         | Retrieve a city by its UUID.                 | —                                     | `{ CityResponseDTO }`                   |
| POST   | `http://localhost:10000/api/city/add`              | Add a new city (with geolocation).           | `{ name, latitude, longitude }`       | `{ CityResponseDTO }`                   |
| PATCH  | `http://localhost:10000/api/city/update/{id}`      | Update fields of an existing city.           | `{ name?, latitude?, longitude? }`    | `{ CityResponseDTO }`                   |
| DELETE | `http://localhost:10000/api/city/delete/{id}`      | Delete a city by its UUID.                   | —                                     | HTTP 200 OK (empty body)                |

---

### Stats Service (via API Gateway)

_All calls go through the gateway at_ `http://localhost:10000`

| HTTP   | Path                                           | Description                                              | Request Body | Response Body                         |
|--------|------------------------------------------------|----------------------------------------------------------|--------------|---------------------------------------|
| GET    | `http://localhost:10000/api/stats/all`         | Retrieve **all** stats entries.                          | —            | `[{ StatsResponseDTO }, …]`           |
| GET    | `http://localhost:10000/api/stats/{id}`        | Retrieve stats for a specific entity (by UUID).          | —            | `{ StatsResponseDTO }`                |
| POST   | `http://localhost:10000/api/stats/{id}/click`  | Increment the “click” count for the given stats entity.  | —            | `{ StatsResponseDTO }` (updated)      |
---
