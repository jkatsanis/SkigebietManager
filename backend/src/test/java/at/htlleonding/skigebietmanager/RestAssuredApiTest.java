package at.htlleonding.skigebietmanager;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.*;

@QuarkusTest
public class RestAssuredApiTest {

    @Test
    public void testUserEndpoints() {
        String uniqueEmail = "testuser-" + UUID.randomUUID() + "@example.com";
        // Create a new user
        String userId = given()
            .contentType(ContentType.JSON)
            .body("{\"name\":\"Test User\",\"email\":\"" + uniqueEmail + "\"}")
            .when()
            .post("/api/users")
            .then()
            .statusCode(201)
            .extract()
            .path("id")
            .toString();

        // Get the user
        given()
            .when()
            .get("/api/users/" + userId)
            .then()
            .statusCode(200)
            .body("name", is("Test User"))
            .body("email", is(uniqueEmail));

        // Get user by email
        given()
            .when()
            .get("/api/users/email/" + uniqueEmail)
            .then()
            .statusCode(200)
            .body("name", is("Test User"))
            .body("email", is(uniqueEmail));

        // Update the user
        String updatedEmail = "updated-" + UUID.randomUUID() + "@example.com";
        given()
            .contentType(ContentType.JSON)
            .body("{\"name\":\"Updated User\",\"email\":\"" + updatedEmail + "\"}")
            .when()
            .put("/api/users/" + userId)
            .then()
            .statusCode(200)
            .body("name", is("Updated User"))
            .body("email", is(updatedEmail));

        // Delete the user
        given()
            .when()
            .delete("/api/users/" + userId)
            .then()
            .statusCode(204);
    }

    @Test
    public void testSkiLiftEndpoints() {
        // Create a new ski lift
        String skiLiftId = given()
            .contentType(ContentType.JSON)
            .body("{\"name\":\"Test Lift\",\"typ\":\"Sessellift\",\"kapazitaet\":1000}")
            .when()
            .post("/api/skilifts")
            .then()
            .statusCode(201)
            .extract()
            .path("id")
            .toString();

        // Get the ski lift
        given()
            .when()
            .get("/api/skilifts/" + skiLiftId)
            .then()
            .statusCode(200)
            .body("name", is("Test Lift"))
            .body("typ", is("Sessellift"))
            .body("kapazitaet", is(1000));

        // Update the ski lift
        given()
            .contentType(ContentType.JSON)
            .body("{\"name\":\"Updated Lift\",\"typ\":\"Schlepplift\",\"kapazitaet\":800}")
            .when()
            .put("/api/skilifts/" + skiLiftId)
            .then()
            .statusCode(200)
            .body("name", is("Updated Lift"))
            .body("typ", is("Schlepplift"))
            .body("kapazitaet", is(800));

        // Delete the ski lift
        given()
            .when()
            .delete("/api/skilifts/" + skiLiftId)
            .then()
            .statusCode(204);
    }

    @Test
    public void testPisteEndpoints() {
        // First create a ski lift for the piste
        String skiLiftId = given()
            .contentType(ContentType.JSON)
            .body("{\"name\":\"Test Lift\",\"typ\":\"Sessellift\",\"kapazitaet\":1000}")
            .when()
            .post("/api/skilifts")
            .then()
            .statusCode(201)
            .extract()
            .path("id")
            .toString();

        // Create a new piste
        String pisteId = given()
            .contentType(ContentType.JSON)
            .body("{\"name\":\"Test Piste\",\"schwierigkeitsgrad\":\"Mittel\",\"laenge\":2.5,\"skiLift\":{\"id\":" + skiLiftId + "}}")
            .when()
            .post("/api/pisten")
            .then()
            .statusCode(201)
            .extract()
            .path("id")
            .toString();

        // Get the piste
        given()
            .when()
            .get("/api/pisten/" + pisteId)
            .then()
            .statusCode(200)
            .body("name", is("Test Piste"))
            .body("schwierigkeitsgrad", is("Mittel"))
            .body("laenge", is(2.5f));

        // Get pistes by ski lift
        given()
            .when()
            .get("/api/pisten/skilift/" + skiLiftId)
            .then()
            .statusCode(200)
            .body("size()", is(1))
            .body("[0].name", is("Test Piste"));

        // Update the piste
        given()
            .contentType(ContentType.JSON)
            .body("{\"name\":\"Updated Piste\",\"schwierigkeitsgrad\":\"Schwer\",\"laenge\":3.0,\"skiLift\":{\"id\":" + skiLiftId + "}}")
            .when()
            .put("/api/pisten/" + pisteId)
            .then()
            .statusCode(200)
            .body("name", is("Updated Piste"))
            .body("schwierigkeitsgrad", is("Schwer"))
            .body("laenge", is(3.0f));

        // Delete the piste
        given()
            .when()
            .delete("/api/pisten/" + pisteId)
            .then()
            .statusCode(204);

        // Clean up the ski lift
        given()
            .when()
            .delete("/api/skilifts/" + skiLiftId)
            .then()
            .statusCode(204);
    }

    @Test
    public void testTicketEndpoints() {
        String uniqueEmail = "ticketuser-" + UUID.randomUUID() + "@example.com";
        // First create a user for the ticket
        String userId = given()
            .contentType(ContentType.JSON)
            .body("{\"name\":\"Test User\",\"email\":\"" + uniqueEmail + "\"}")
            .when()
            .post("/api/users")
            .then()
            .statusCode(201)
            .extract()
            .path("id")
            .toString();

        // Create a new ticket
        String ticketId = given()
            .contentType(ContentType.JSON)
            .body("{\"ticketType\":\"Ganztages\",\"date\":\"2024-03-20\",\"validFrom\":\"08:00\",\"validUntil\":\"16:00\",\"user\":{\"id\":" + userId + "}}")
            .when()
            .post("/api/tickets")
            .then()
            .statusCode(201)
            .extract()
            .path("id")
            .toString();

        // Get the ticket
        given()
            .when()
            .get("/api/tickets/" + ticketId)
            .then()
            .statusCode(200)
            .body("ticketType", is("Ganztages"))
            .body("date", is("2024-03-20"))
            .body("validFrom", is("08:00:00"))
            .body("validUntil", is("16:00:00"));

        // Get tickets by user
        given()
            .when()
            .get("/api/tickets/user/" + userId)
            .then()
            .statusCode(200)
            .body("size()", is(1))
            .body("[0].ticketType", is("Ganztages"));

        // Update the ticket
        given()
            .contentType(ContentType.JSON)
            .body("{\"ticketType\":\"Halbtages\",\"date\":\"2024-03-20\",\"validFrom\":\"12:00\",\"validUntil\":\"16:00\",\"user\":{\"id\":" + userId + "}}")
            .when()
            .put("/api/tickets/" + ticketId)
            .then()
            .statusCode(200)
            .body("ticketType", is("Halbtages"))
            .body("validFrom", is("12:00"));

        // Delete the ticket
        given()
            .when()
            .delete("/api/tickets/" + ticketId)
            .then()
            .statusCode(204);

        // Clean up the user
        given()
            .when()
            .delete("/api/users/" + userId)
            .then()
            .statusCode(204);
    }

    @Test
    public void testStatisticsEndpoint() {
        given()
            .when()
            .get("/api/statistics")
            .then()
            .statusCode(200)
            .body(notNullValue());
    }
} 