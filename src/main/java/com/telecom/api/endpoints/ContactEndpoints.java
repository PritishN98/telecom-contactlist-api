package com.telecom.api.endpoints;

import com.telecom.api.payload.Contact;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import static io.restassured.RestAssured.given;

/**
 * Encapsulates RestAssured requests for all Contact-related endpoints.
 */
public class ContactEndpoints {

    public static Response addContact(Contact payload, String token) {
        return given()
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(payload)
            .when()
                .post(Routes.ADD_CONTACT);
    }

    public static Response getContactList(String token) {
        return given()
                .header("Authorization", "Bearer " + token)
                .accept(ContentType.JSON)
            .when()
                .get(Routes.GET_CONTACT_LIST);
    }

    public static Response getContact(String contactId, String token) {
        return given()
                .header("Authorization", "Bearer " + token)
                .pathParam("contactId", contactId)
                .accept(ContentType.JSON)
            .when()
                .get(Routes.GET_CONTACT);
    }

    public static Response updateContactFull(String contactId, Contact payload, String token) {
        return given()
                .header("Authorization", "Bearer " + token)
                .pathParam("contactId", contactId)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(payload)
            .when()
                .put(Routes.UPDATE_FULL_CONTACT);
    }

    public static Response updateContactPartial(String contactId, Object payload, String token) {
        return given()
                .header("Authorization", "Bearer " + token)
                .pathParam("contactId", contactId)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(payload)
            .when()
                .patch(Routes.UPDATE_PARTIAL_CONTACT);
    }

    public static Response deleteContact(String contactId, String token) {
        return given()
                .header("Authorization", "Bearer " + token)
                .pathParam("contactId", contactId)
            .when()
                .delete(Routes.DELETE_CONTACT);
    }
}
