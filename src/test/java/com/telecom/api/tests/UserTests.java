package com.telecom.api.tests;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.telecom.api.endpoints.UserEndpoints;
import com.telecom.api.payload.User;

import io.restassured.response.Response;

/**
 * Functional tests focusing on the User endpoints and negative scenarios.
 */
public class UserTests {

    private String token;
    private String userEmail;

    @Test(priority = 1, description = "Verify User Registration with Valid Data")
    public void testUserRegistration() {
        userEmail = "user_" + System.currentTimeMillis() + "@fake.com";
        User user = new User("John", "Tester", userEmail, "password123");

        Response response = UserEndpoints.createUser(user);
        Assert.assertEquals(response.getStatusCode(), 201);
        token = response.jsonPath().getString("token");
        Assert.assertNotNull(token);
    }

    @Test(priority = 2, dependsOnMethods = {"testUserRegistration"}, description = "Verify Fetching Logged-in User Profile")
    public void testGetProfile() {
        Response response = UserEndpoints.getUserProfile(token);
        Assert.assertEquals(response.getStatusCode(), 200);
        Assert.assertEquals(response.jsonPath().getString("email"), userEmail);
    }

    @Test(priority = 3, description = "Negative Test: Login with Invalid Password")
    public void testInvalidLogin() {
        Response response = UserEndpoints.loginUser(userEmail != null ? userEmail : "nonexistent@fake.com", "WrongPassword!");
        Assert.assertEquals(response.getStatusCode(), 401, "Expected 401 Unauthorized for invalid login credentials");
    }
}
