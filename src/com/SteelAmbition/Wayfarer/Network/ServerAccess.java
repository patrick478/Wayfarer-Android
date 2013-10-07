package com.SteelAmbition.Wayfarer.Network;

import android.util.Base64;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.*;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * This class provides many static methods for communicating with the server and
 * performing common tasks.
 *
 * Typically the first method called on app launch will either be login() or
 * createUser(), both of which "log the user in" and allow most of the other methods
 * to function using the stored authentication information.
 */
public class ServerAccess {

    // The base server URL
    public static final String SERVER_URL = "http://wayfarer-server.herokuapp.com";
    //public static final String SERVER_URL = "http://10.0.2.2:5000"; //localhost

<<<<<<< HEAD
=======
    // The user currently logged in
    private static User currentUser;
    private static Header currentAuthHeader;

    /**
     * Gets the currently logged in user.  ServerAccess.login() (or createUser() NEEDS to
     * have been called otherwise this will return null, as there is no logged in user.
     */
    public static User getCurrentUser() {
        return currentUser;
    }

>>>>>>> 15623f46261c09a94a53be0db83cd06b6330cb0a
    /**
     * Internal helper method to reduce repeated code.
     * Creates a connection to the server with optional body and authentication header
     * and returns the response, if it contains the expected HTTP code.
     * Appropriate exceptions are thrown.  It is up to the calling function to determine
     * which exceptions are possible and discard the others.
     *
     * @param verb          The http verb to use, supported: GET/PUT/POST/DELETE
     * @param uri           The uri on the end of the url eg. 'users'
     * @param codeExpected  The HTTP code expected on success
     * @param body          If not null, the body to attach to PUT/POST requests
     * @param authHeader    If not null, the authentication header to use.
     *
     * @return  The response, or null if there was a problem.
     */
<<<<<<< HEAD
    public static HttpResponse doRequest(String verb, String uri, int codeExpected, JSONObject body,
=======
    private static HttpResponse doRequest(String verb, String uri, int codeExpected, JSONObject body,
>>>>>>> 15623f46261c09a94a53be0db83cd06b6330cb0a
                                          Header authHeader)
            throws AuthenticationException, AlreadyExistsException, NetworkFailureException {

        // Construct request based on verb required, attaching json body if supplied
        HttpRequestBase request;
        try {
            switch (verb.toUpperCase()) {
                case "GET":
                    request = new HttpGet(SERVER_URL+"/"+uri);
                    break;
                case "POST":
                    request = new HttpPost(SERVER_URL+"/"+uri);
                    if (body != null) {
                        request.setHeader("Content-type", "application/json");
                        ((HttpPost)request).setEntity(new StringEntity(body.toString()));
                    }
                    break;
                case "PUT":
                    request = new HttpPut(SERVER_URL+"/"+uri);
                    if (body != null) {
                        request.setHeader("Content-type", "application/json");
                        ((HttpPut)request).setEntity(new StringEntity(body.toString()));
                    }
                    break;
                case "DELETE":
                    request = new HttpDelete(SERVER_URL+"/"+uri);
                    break;
                default: throw new IllegalArgumentException("Only get/post/put/delete allowed");
            }
        } catch (UnsupportedEncodingException e) { e.printStackTrace(); return null; }

        // Attach authentication header if supplied
        if (authHeader!=null) request.setHeader(authHeader);

        // Execute the request
        try {
            HttpResponse response = new DefaultHttpClient().execute(request);

            // Success, return the response
            if (response.getStatusLine().getStatusCode() == codeExpected) {
                return response;

            // Authentication failure
            } else if (response.getStatusLine().getStatusCode() == HttpStatus.SC_UNAUTHORIZED) {
                throw new AuthenticationException(response.getStatusLine().getReasonPhrase());

             // Already exists
            } else if (response.getStatusLine().getStatusCode() == HttpStatus.SC_FORBIDDEN) {
                throw new AlreadyExistsException(response.getStatusLine().getReasonPhrase());

            // Other failure
            } else {
                throw new NetworkFailureException(response.getStatusLine().getReasonPhrase());
            }
        } catch (IOException e) {
            throw new NetworkFailureException(e);
        }
    }
<<<<<<< HEAD
=======

    /**
     * Logs the user with the given email and password in. The combination must
     * already exist on the server.
     *
     * Once logged in, all other methods in this class will operate on this user.
     *
     * @param email     Email of the user to log in
     * @param password  Password of the user to log in
     * @return          The User if successful, or null if unsuccessful.
     */
    public static User login(String email, String password)
            throws AuthenticationException, NetworkFailureException {

        // Validate input
        if (email == null || !Utility.isEmailValid(email))
            throw new IllegalArgumentException("Provided email is not valid");
        if (password == null || password.isEmpty())
            throw new IllegalArgumentException("Must provide a password");

        // Authentication header
        BasicHeader authHeader = new BasicHeader("Authorization",
            "Basic "+Base64.encodeToString((email + ":" + password).getBytes(),Base64.NO_WRAP));

        // Execute request
        User user = null;
        HttpResponse response = null;
        try {
            response = doRequest("GET", "users", HttpStatus.SC_OK, null, authHeader);
        } catch (AlreadyExistsException e) { /* won't be thrown */ }

        // Successful "login"
        if (response != null) {
            user = new User(response.getEntity());
            currentUser = user;
            currentAuthHeader = authHeader;
        }
        return user;
    }

>>>>>>> 15623f46261c09a94a53be0db83cd06b6330cb0a
    /**
     * Attempts to connect to the server, returning True if successful.
     * This is a good way to test if (a) the server is up and (b) the app can connect
     * to it.
     */
    public static boolean connectionTest() {
        try {
            HttpResponse response = new DefaultHttpClient().execute(new HttpGet(SERVER_URL));
            return (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK);
        } catch (IOException e) {
            return false;
        }
    }

<<<<<<< HEAD
=======
    /**
     * Creates a user with the given email (must be unique), name and password.  The new
     * user will automatically be "logged in" and stored as the current user, so that
     * login() doesn't have to be called.
     *
     * @param email     The email of the user, must be unique
     * @param name      The name of the user
     * @param password  The user's chosen password
     *
     * @return  The created user if successful, null otherwise.
     *
     * @throws AlreadyExistsException   If the email is already tied to a user
     * @throws NetworkFailureException  If there was an error communicating with server
     */
    public static User createUser(String email, String name, String password)
            throws AlreadyExistsException, NetworkFailureException {

        // Validate input
        if (email == null || !Utility.isEmailValid(email))
            throw new IllegalArgumentException("Provided email is not valid");
        if (name == null || name.isEmpty())
            throw new IllegalArgumentException("Must provide a name");
        if (password == null || password.isEmpty())
            throw new IllegalArgumentException("Must provide a password");

        // Create body
        JSONObject json = new JSONObject();
        try {
            json.put("email", email);
            json.put("name", name);
            json.put("password", password);
        } catch (JSONException e) { e.printStackTrace(); return null; }

        // Execute request
        User user = null;
        HttpResponse response = null;
        try {
            response = doRequest("GET", "users", HttpStatus.SC_OK, json, null);
        } catch (AuthenticationException e) { /* won't be thrown */ }

        // Successful user creation
        if (response != null) {
            user = new User(response.getEntity());
            currentUser = user;
            // Construct an authentication header for them, too.
            currentAuthHeader = new BasicHeader("Authorization",
                    "Basic "+Base64.encodeToString((email + ":" + password).getBytes(),Base64.NO_WRAP));
        }
        return user;
    }

    /**
     * Retrieves the currently authenticated user.  This shouldn't need to see any use
     * because login() already retrieves the user after authenticating and getCurrentUser()
     * retrieves a locally-stored copy of a user after authenticating.
     */
    public static User getUser() throws AuthenticationException, NetworkFailureException {
        User user = null;
        try {
            HttpResponse response = doRequest("GET", "users", HttpStatus.SC_OK, null, currentAuthHeader);
            if (response != null) {
                user = new User(response.getEntity());
            }
        } catch (AlreadyExistsException e) { /* won't be thrown */ }
        return user;
    }

    /**
     * Deletes the currently authenticated user and logs them out, forgetting any stored
     * user information in ServerAccess.
     * @return  True if deletion was successful.
     */
    public static boolean deleteUser() throws AuthenticationException, NetworkFailureException {
        boolean success = false;
        try {
            HttpResponse response = doRequest("DELETE", "users", HttpStatus.SC_OK, null, currentAuthHeader);
            if (response != null) {
                success = true;
                currentUser = null;
                currentAuthHeader = null;
            }
        } catch (AlreadyExistsException e) { /* won't be thrown */ }
        return success;
    }


>>>>>>> 15623f46261c09a94a53be0db83cd06b6330cb0a
}
