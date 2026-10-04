package com.telecom.api.tests;

import java.util.List;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.telecom.api.endpoints.ContactEndpoints;
import com.telecom.api.endpoints.UserEndpoints;
import com.telecom.api.payload.Contact;
import com.telecom.api.payload.User;

import io.restassured.response.Response;

/**
 * Functional tests focusing on the Contact management endpoints.
 */
public class ContactTests {

    private String token;
    private String contactId;

    @BeforeClass
    public void setupUser() {
        String email = "contact_test_" + System.currentTimeMillis() + "@fake.com";
        User user = new User("Contact", "Tester", email, "secretPass123");
        Response res = UserEndpoints.createUser(user);
        token = res.jsonPath().getString("token");
    }

    @Test(priority = 1, description = "Verify Adding a New Contact")
    public void testAddNewContact() {
        Contact contact = new Contact(
                "Jane", "Smith", "1985-05-15",
                "jsmith@fake.com", "9005551234",
                "500 Broadway", "Suite 200",
                "New York", "NY", "10012", "USA"
        );

        Response response = ContactEndpoints.addContact(contact, token);
        Assert.assertEquals(response.getStatusCode(), 201);
        contactId = response.jsonPath().getString("_id");
        Assert.assertNotNull(contactId);
    }

    @Test(priority = 2, dependsOnMethods = {"testAddNewContact"}, description = "Verify Fetching Contact List")
    public void testGetContactsList() {
        Response response = ContactEndpoints.getContactList(token);
        Assert.assertEquals(response.getStatusCode(), 200);
        List<Map<String, Object>> list = response.jsonPath().getList("$");
        Assert.assertTrue(list.size() > 0);
    }

    @Test(priority = 3, dependsOnMethods = {"testAddNewContact"}, description = "Verify Deleting a Contact")
    public void testDeleteContact() {
        Response response = ContactEndpoints.deleteContact(contactId, token);
        Assert.assertEquals(response.getStatusCode(), 200);

        // Verify contact is gone (404 Not Found)
        Response verifyRes = ContactEndpoints.getContact(contactId, token);
        Assert.assertEquals(verifyRes.getStatusCode(), 404);
    }
}
