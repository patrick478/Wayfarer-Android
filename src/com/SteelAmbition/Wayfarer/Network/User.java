package com.SteelAmbition.Wayfarer.Network;

import android.util.Base64;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Represents a User of the application, as stored on the server.
 */
public class User {

    // This user's details.
    private String id;
    private String email;
    private String password;
    private String name;
    private String subjectId;

    // Used to keep track of what has changed since the user's information was retrieved.
    private String storedEmail, storedPassword, storedName, storedSubjectId;

    // The basic authentication header for this user.
    private BasicHeader authHeader;

    /**
     * Attempts to create a user on the server using the provided information.
     *
     * @param email     The email address of the user -- is unique to all users
     * @param name      The name of the user
     * @param password  The password of the user
     * @throws AlreadyExistsException   Thrown if a user already exists with that email
     * @throws NetworkFailureException
     */
    public User(String email, String name, String password)
            throws AlreadyExistsException, NetworkFailureException {

        // Validate input
        if (email == null || !ServerAccess.isEmailValid(email))
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
        } catch (JSONException e) { e.printStackTrace(); }

        try {
            // Execute request, expecting 201 CREATED
            HttpResponse response = ServerAccess.doRequest("PUT", "users", HttpStatus.SC_CREATED, json, null);

            // Flesh out fields from the response
            this.password = password;
            storedPassword = password;
            updateFromEntity(response.getEntity());

            // Construct authentication header
            authHeader = new BasicHeader("Authorization",
                    "Basic "+ Base64.encodeToString((this.email+":"+password).getBytes(), Base64.NO_WRAP));
        } catch (AuthenticationException e) { /* won't be thrown */ }
    }

    /**
     * Attempts to get an existing User from the server using the provided authentication
     * details.
     * @param email     The email of the user
     * @param password  The password of the user
     * @throws AuthenticationException  Thrown if the user doesn't exist or details are wrong
     * @throws NetworkFailureException
     */
    public User(String email, String password)
            throws AuthenticationException, NetworkFailureException {

        // Validate input
        if (email == null || !ServerAccess.isEmailValid(email))
            throw new IllegalArgumentException("Provided email is not valid");
        if (password == null || password.isEmpty())
            throw new IllegalArgumentException("Must provide a password");

        // Construct authentication header
        authHeader = new BasicHeader("Authorization",
            "Basic "+ Base64.encodeToString((email+":"+password).getBytes(), Base64.NO_WRAP));

        try {
            // Execute request, expecting 200 OK
            HttpResponse response = ServerAccess.doRequest("GET", "users", HttpStatus.SC_OK, null, authHeader);

            // Flesh out fields from the response
            this.password = password;
            storedPassword = password;
            updateFromEntity(response.getEntity());
        } catch (AlreadyExistsException e) { /* won't be thrown */ }
    }

    /**
     * Updates the user on the server with any fields that have been changed since
     * this User's construction or the last call to update.
     * @throws AuthenticationException
     * @throws NetworkFailureException
     */
    public void update() throws AuthenticationException, NetworkFailureException {

        // Create body
        JSONObject json = new JSONObject();
        try {
            if (!email.equals(storedEmail))         json.put("email", email);
            if (!name.equals(storedName))           json.put("name", name);
            if (!password.equals(storedPassword))   json.put("password", password);
            if (subjectId != null && !subjectId.equals(storedSubjectId))
                json.put("subjectId", subjectId);
        } catch (JSONException e) { e.printStackTrace(); }

        // Nothing to update: just return
        if (json.length() == 0) {
            return;
        }

        // Execute request; expecting 200 OK
        try {
            HttpResponse response = ServerAccess.doRequest("POST", "users", HttpStatus.SC_OK, json, authHeader);

            // All current user info is returned, so might as well update everything
            updateFromEntity(response.getEntity());
            storedPassword = password;

            // Reconstruct authentication header
            authHeader = new BasicHeader("Authorization",
                    "Basic "+ Base64.encodeToString((this.email+":"+password).getBytes(), Base64.NO_WRAP));
        } catch (AlreadyExistsException e) { /* won't be thrown */ }
    }

    /**
     * Deletes this user from the server. Permanent!
     * @throws AuthenticationException
     * @throws NetworkFailureException
     */
    public void delete() throws AuthenticationException, NetworkFailureException {
        try {
            HttpResponse response = ServerAccess.doRequest("DELETE", "users", HttpStatus.SC_OK, null, authHeader);
        } catch (AlreadyExistsException e) { /* won't be thrown */ }
    }

    /**
     * Retrieves the Subject that this user is watching from the server, or null if this user
     * is not watching a subject.
     * @return  The subject or null if they don't exist.
     * @throws AuthenticationException
     * @throws NetworkFailureException
     */
    public Subject getSubject() throws AuthenticationException, NetworkFailureException  {
        if (subjectId == null)
            return null;
        try {
            return new Subject(this);
        } catch (DoesNotExistException e) {
            return null; //won't happen but handle anyway
        }
    }

    /**
     * Updates this user from the (JSON) body of an HTTP response.
     */
    private void updateFromEntity(HttpEntity entity) {
        try {
            JSONObject json = new JSONObject(EntityUtils.toString(entity));
            // Update fields
            id = json.getString("id");
            email = json.getString("email");
            name = json.getString("name");
            subjectId = (json.has("subjectId")) ? json.getString("subjectId") : null;

            // Update stored representation of user
            storedEmail = this.email;
            storedName = this.name;
            storedSubjectId = this.subjectId;
        } catch (JSONException|IOException e) {
            throw new IllegalArgumentException(e);
        }
    }

    /* Getters and setters.  To update this user, set the fields you want to change
     * and then call update() to commit the changes to the server. */
    public String getId()                       { return id; }
    public String getName()                     { return name; }
    public String getSubjectId()                { return subjectId; }
    public String getEmail()                    { return email; }
    public String getPassword()                 { return password; }
    public BasicHeader getAuthHeader()          { return authHeader; }
    public void setName(String name)            { this.name = name; }
    public void setSubjectId(String subjectId)  { this.subjectId = subjectId; }
    public void setEmail(String email)          { this.email = email; }
    public void setPassword(String password)    { this.password = password; }
}
