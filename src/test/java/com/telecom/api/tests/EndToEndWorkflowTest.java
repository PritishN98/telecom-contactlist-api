package com.telecom.api.tests;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.telecom.api.endpoints.ContactEndpoints;
import com.telecom.api.endpoints.UserEndpoints;
import com.telecom.api.payload.Contact;
import com.telecom.api.payload.User;

import io.restassured.response.Response;

/**
 * End-to-End Test Suite executing the complete 10-step testing flow defined in the Capstone specification:
 * Add User -> Get user profile -> Update user -> Login user -> Add Contact -> Get contact list -> Get contact -> Update full contact -> Update partial contact -> Logout User
 */
public class EndToEndWorkflowTest {

    private static String token;
    private static String contactId;
    private static String userEmail;
    private static final String initialPassword = "myPassword";
    private static final String newPassword = "myNewPassword";

    @Test(priority = 1, description = "Test Case 1: Add New User (POST /users)")
    public void testCase1_addNewUser() {
        // Unique email generation to prevent "Email address is already in use" errors across runs
        userEmail = "testuser_" + System.currentTimeMillis() + "@fake.com";

        User userPayload = new User("Test", "User", userEmail, initialPassword);

        Response response = UserEndpoints.createUser(userPayload);
        response.then().log().all();

        // Validate Status Code 201 Created
        Assert.assertEquals(response.getStatusCode(), 201, "Expected status code 201 Created");

        // Validate response structure and extract Token
        token = response.jsonPath().getString("token");
        Assert.assertNotNull(token, "Authentication token should not be null");
        Assert.assertFalse(token.isEmpty(), "Authentication token should not be empty");

        String returnedEmail = response.jsonPath().getString("user.email");
        Assert.assertEquals(returnedEmail, userEmail, "Returned email does not match requested email");

        System.out.println("TC1 Passed: User created successfully. Token generated: " + token);
    }

    @Test(priority = 2, dependsOnMethods = {"testCase1_addNewUser"}, description = "Test Case 2: Get user Profile (GET /users/me)")
    public void testCase2_getUserProfile() {
        Response response = UserEndpoints.getUserProfile(token);
        response.then().log().all();

        // Validate Status Code 200 OK
        Assert.assertEquals(response.getStatusCode(), 200, "Expected status code 200 OK");

        String email = response.jsonPath().getString("email");
        String firstName = response.jsonPath().getString("firstName");
        String lastName = response.jsonPath().getString("lastName");

        Assert.assertEquals(email, userEmail);
        Assert.assertEquals(firstName, "Test");
        Assert.assertEquals(lastName, "User");

        System.out.println("TC2 Passed: Profile retrieved successfully for " + email);
    }

    @Test(priority = 3, dependsOnMethods = {"testCase1_addNewUser"}, description = "Test Case 3: Update User (PATCH /users/me)")
    public void testCase3_updateUser() {
        userEmail = "updated_" + System.currentTimeMillis() + "@fake.com";
        User updatePayload = new User("Updated", "Username", userEmail, newPassword);

        Response response = UserEndpoints.updateUser(updatePayload, token);
        response.then().log().all();

        // Validate Status Code 200 OK
        Assert.assertEquals(response.getStatusCode(), 200, "Expected status code 200 OK");

        String updatedFirst = response.jsonPath().getString("firstName");
        String updatedLast = response.jsonPath().getString("lastName");
        String updatedMail = response.jsonPath().getString("email");

        Assert.assertEquals(updatedFirst, "Updated");
        Assert.assertEquals(updatedLast, "Username");
        Assert.assertEquals(updatedMail, userEmail);

        System.out.println("TC3 Passed: User updated successfully.");
    }

    @Test(priority = 4, dependsOnMethods = {"testCase3_updateUser"}, description = "Test Case 4: Log In User (POST /users/login)")
    public void testCase4_loginUser() {
        Response response = UserEndpoints.loginUser(userEmail, newPassword);
        response.then().log().all();

        // Validate Status Code 200 OK
        Assert.assertEquals(response.getStatusCode(), 200, "Expected status code 200 OK");

        // Refresh token from login
        token = response.jsonPath().getString("token");
        Assert.assertNotNull(token, "Login token should not be null");

        System.out.println("TC4 Passed: User logged in successfully. New token: " + token);
    }

    @Test(priority = 5, dependsOnMethods = {"testCase4_loginUser"}, description = "Test Case 5: Add Contact (POST /contacts)")
    public void testCase5_addContact() {
        Contact contactPayload = new Contact(
                "John", "Doe", "1970-01-01",
                "jdoe@fake.com", "8005555555",
                "1 Main St.", "Apartment A",
                "Anytown", "KS", "12345", "USA"
        );

        Response response = ContactEndpoints.addContact(contactPayload, token);
        response.then().log().all();

        // Validate Status Code 201 Created
        Assert.assertEquals(response.getStatusCode(), 201, "Expected status code 201 Created");

        contactId = response.jsonPath().getString("_id");
        Assert.assertNotNull(contactId, "Contact _id should not be null");

        Assert.assertEquals(response.jsonPath().getString("firstName"), "John");
        Assert.assertEquals(response.jsonPath().getString("lastName"), "Doe");
        Assert.assertEquals(response.jsonPath().getString("email"), "jdoe@fake.com");

        System.out.println("TC5 Passed: Contact created successfully with ID: " + contactId);
    }

    @Test(priority = 6, dependsOnMethods = {"testCase5_addContact"}, description = "Test Case 6: Get Contact List (GET /contacts)")
    public void testCase6_getContactList() {
        Response response = ContactEndpoints.getContactList(token);
        response.then().log().all();

        // Validate Status Code 200 OK
        Assert.assertEquals(response.getStatusCode(), 200, "Expected status code 200 OK");

        List<Map<String, Object>> contacts = response.jsonPath().getList("$");
        Assert.assertNotNull(contacts, "Contact list should not be null");
        Assert.assertTrue(contacts.size() > 0, "Contact list should contain at least one contact");

        System.out.println("TC6 Passed: Contact list retrieved. Total contacts: " + contacts.size());
    }

    @Test(priority = 7, dependsOnMethods = {"testCase5_addContact"}, description = "Test Case 7: Get Contact (GET /contacts/{contactId})")
    public void testCase7_getContact() {
        Response response = ContactEndpoints.getContact(contactId, token);
        response.then().log().all();

        // Validate Status Code 200 OK
        Assert.assertEquals(response.getStatusCode(), 200, "Expected status code 200 OK");

        Assert.assertEquals(response.jsonPath().getString("_id"), contactId);
        Assert.assertEquals(response.jsonPath().getString("firstName"), "John");
        Assert.assertEquals(response.jsonPath().getString("lastName"), "Doe");

        System.out.println("TC7 Passed: Contact retrieved successfully by ID: " + contactId);
    }

    @Test(priority = 8, dependsOnMethods = {"testCase5_addContact"}, description = "Test Case 8: Update Contact Full (PUT /contacts/{contactId})")
    public void testCase8_updateContactFull() {
        Contact fullUpdatePayload = new Contact(
                "Amy", "Miller", "1992-02-02",
                "amiller@fake.com", "8005554242",
                "13 School St.", "Apt. 5",
                "Washington", "QC", "A1A1A1", "Canada"
        );

        Response response = ContactEndpoints.updateContactFull(contactId, fullUpdatePayload, token);
        response.then().log().all();

        // Validate Status Code 200 OK
        Assert.assertEquals(response.getStatusCode(), 200, "Expected status code 200 OK");

        Assert.assertEquals(response.jsonPath().getString("firstName"), "Amy");
        Assert.assertEquals(response.jsonPath().getString("lastName"), "Miller");
        Assert.assertEquals(response.jsonPath().getString("email"), "amiller@fake.com");
        Assert.assertEquals(response.jsonPath().getString("country"), "Canada");

        System.out.println("TC8 Passed: Contact fully updated successfully.");
    }

    @Test(priority = 9, dependsOnMethods = {"testCase5_addContact"}, description = "Test Case 9: Update Contact Partial (PATCH /contacts/{contactId})")
    public void testCase9_updateContactPartial() {
        Map<String, Object> partialPayload = new HashMap<>();
        partialPayload.put("firstName", "Anna");

        Response response = ContactEndpoints.updateContactPartial(contactId, partialPayload, token);
        response.then().log().all();

        // Validate Status Code 200 OK
        Assert.assertEquals(response.getStatusCode(), 200, "Expected status code 200 OK");

        Assert.assertEquals(response.jsonPath().getString("firstName"), "Anna");
        Assert.assertEquals(response.jsonPath().getString("lastName"), "Miller"); // preserved

        System.out.println("TC9 Passed: Contact partially updated successfully with firstName: Anna");
    }

    @Test(priority = 10, dependsOnMethods = {"testCase1_addNewUser"}, description = "Test Case 10: Logout User (POST /users/logout)")
    public void testCase10_logoutUser() {
        Response response = UserEndpoints.logoutUser(token);
        response.then().log().all();

        // Validate Status Code 200 OK
        Assert.assertEquals(response.getStatusCode(), 200, "Expected status code 200 OK");

        System.out.println("TC10 Passed: User logged out successfully.");

        // Verify token is invalidated
        Response verifyInvalid = UserEndpoints.getUserProfile(token);
        Assert.assertEquals(verifyInvalid.getStatusCode(), 401, "Expected 401 Unauthorized after logout");
    }
}
