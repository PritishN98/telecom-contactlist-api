package com.telecom.api.endpoints;

import com.telecom.api.utilities.ConfigReader;

/**
 * Maintains all REST API route constants for the Contact List Application.
 */
public class Routes {

    public static final String BASE_URL = ConfigReader.getBaseUrl();

    // User Endpoints
    public static final String ADD_USER = BASE_URL + "/users";
    public static final String GET_USER_PROFILE = BASE_URL + "/users/me";
    public static final String UPDATE_USER = BASE_URL + "/users/me";
    public static final String LOGIN_USER = BASE_URL + "/users/login";
    public static final String LOGOUT_USER = BASE_URL + "/users/logout";

    // Contact Endpoints
    public static final String ADD_CONTACT = BASE_URL + "/contacts";
    public static final String GET_CONTACT_LIST = BASE_URL + "/contacts";
    public static final String GET_CONTACT = BASE_URL + "/contacts/{contactId}";
    public static final String UPDATE_FULL_CONTACT = BASE_URL + "/contacts/{contactId}";
    public static final String UPDATE_PARTIAL_CONTACT = BASE_URL + "/contacts/{contactId}";
    public static final String DELETE_CONTACT = BASE_URL + "/contacts/{contactId}";
}
