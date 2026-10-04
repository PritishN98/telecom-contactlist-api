package com.telecom.api.endpoints;

import java.util.HashMap;
import java.util.Map;

import com.telecom.api.payload.User;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import static io.restassured.RestAssured.given;

/**
 * Encapsulates RestAssured requests for all User-related endpoints.
 */
public class UserEndpoints {

    public static Response createUser(User payload) {
        return given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(payload)
            .when()
                .post(Routes.ADD_USER);
    }

    public static Response getUserProfile(String token) {
        return given()
                .header("Authorization", "Bearer " + token)
                .accept(ContentType.JSON)
            .when()
                .get(Routes.GET_USER_PROFILE);
    }

    public static Response updateUser(User payload, String token) {
        return given()
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(payload)
            .when()
                .patch(Routes.UPDATE_USER);
    }

    public static Response loginUser(String email, String password) {
        Map<String, String> credentials = new HashMap<>();
        credentials.put("email", email);
        credentials.put("password", password);

        return given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(credentials)
            .when()
                .post(Routes.LOGIN_USER);
    }

    public static Response logoutUser(String token) {
        return given()
                .header("Authorization", "Bearer " + token)
            .when()
                .post(Routes.LOGOUT_USER);
    }
}
