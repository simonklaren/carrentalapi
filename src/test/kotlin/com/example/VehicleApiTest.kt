package com.example

import io.ktor.server.application.*
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlin.test.Test
import kotlin.test.assertEquals

class VehicleApiTest {

    @Test
    fun testGetVehicles() {
        withTestApplication(Application::module) {
            handleRequest(HttpMethod.Get, "/vehicles").apply {
                assertEquals(HttpStatusCode.OK, response.status())
                assertEquals(
                    """[{"id":1,"brand":"Toyota","model":"Corolla","type":"ICE","pricePerDay":50.0},{"id":2,"brand":"Tesla","model":"Model 3","type":"BEV","pricePerDay":100.0},{"id":3,"brand":"Hyundai","model":"Nexo","type":"FCEV","pricePerDay":80.0}]""",
                    response.content
                )
            }
        }
    }

    @Test
    fun testAddVehicle() {
        withTestApplication(Application::module) {
            handleRequest(HttpMethod.Post, "/vehicles") {
                addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                setBody("""{"id": 4, "brand": "Ford", "model": "Fiesta", "type": "ICE", "pricePerDay": 40.0}""")
            }.apply {
                assertEquals(HttpStatusCode.OK, response.status())
                assertEquals(
                    """{"id":4,"brand":"Ford","model":"Fiesta","type":"ICE","pricePerDay":40.0}""",
                    response.content
                )
            }
        }
    }

    @Test
    fun testUpdateVehicle() {
        withTestApplication(Application::module) {
            // Voeg eerst een voertuig toe dat we kunnen updaten
            handleRequest(HttpMethod.Post, "/vehicles") {
                addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                setBody("""{"id": 5, "brand": "Honda", "model": "Civic", "type": "ICE", "pricePerDay": 60.0}""")
            }

            // Update het zojuist toegevoegde voertuig
            handleRequest(HttpMethod.Put, "/vehicles/5") {
                addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                setBody("""{"id": 5, "brand": "Honda", "model": "Civic", "type": "ICE", "pricePerDay": 80.0}""")
            }.apply {
                assertEquals(HttpStatusCode.OK, response.status())
                assertEquals(
                    """{"id":5,"brand":"Honda","model":"Civic","type":"ICE","pricePerDay":80.0}""",
                    response.content
                )
            }
        }
    }

    @Test
    fun testDeleteVehicle() {
        withTestApplication(Application::module) {
            // Voeg eerst een voertuig toe dat we kunnen verwijderen
            handleRequest(HttpMethod.Post, "/vehicles") {
                addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                setBody("""{"id": 6, "brand": "Nissan", "model": "Leaf", "type": "BEV", "pricePerDay": 90.0}""")
            }

            // Verwijder het zojuist toegevoegde voertuig
            handleRequest(HttpMethod.Delete, "/vehicles/6").apply {
                assertEquals(HttpStatusCode.OK, response.status())
            }

            // Controleer of het voertuig niet meer bestaat door een GET-request te doen
            handleRequest(HttpMethod.Get, "/vehicles").apply {
                assertEquals(HttpStatusCode.OK, response.status())
                // Controleer dat het voertuig met id = 6 niet meer in de lijst staat
                assertEquals(false, response.content!!.contains(""""id":6"""))
            }
        }
    }



}
