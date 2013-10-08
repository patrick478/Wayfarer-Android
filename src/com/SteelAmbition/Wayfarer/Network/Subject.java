package com.SteelAmbition.Wayfarer.Network;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Represents a subject; that is, a person at risk whom is subject of one or more user's efforts.
 */
public class Subject {

    // Subject fields
    private String id;
    private String name;
    private JSONObject datapool;
    private JSONObject state;   // can be converted to a string easily with toString()

    // The user whom this subject is mapped to
    private User user;

    // Keep track of state as it exists on the server
    private JSONObject storedState;

    /**
     * Creates a subject on the server with the given name for the given user.
     * @param user  The user for whom to create this subject for.
     * @param name  The name of the subject.
     * @throws AuthenticationException  If the user fails to authenticate
     * @throws NetworkFailureException
     */
    public Subject(User user, String name)
            throws AuthenticationException, NetworkFailureException {

        // Validate input
        if (user == null)
            throw new IllegalArgumentException("Must provide a user");
        if (name == null || name.isEmpty())
            throw new IllegalArgumentException("Must provide a name");

        // Create body
        JSONObject json = new JSONObject();
        try {
            json.put("name", name);
        } catch (JSONException e) { e.printStackTrace(); }

        try {
            // Execute request, expecting 201 CREATED
            HttpResponse response = ServerAccess.doRequest("PUT", "subjects", HttpStatus.SC_CREATED,
                    json, user.getAuthHeader());

            // Flesh out fields from the response
            this.user = user;
            updateFromEntity(response.getEntity());
        } catch (AlreadyExistsException e) { /* won't be thrown */ }

    }

    /**
     * Attempts to get the subject watched by the provided user, from the server.
     * Note that if the user has had their subjectId changed, the user should first
     * be updated.
     * @param user  The subject will be this user's subject.
     * @throws AuthenticationException
     * @throws NetworkFailureException
     */
    public Subject(User user) throws AuthenticationException,
            DoesNotExistException, NetworkFailureException {

        // Validate input
        if (user == null)
            throw new IllegalArgumentException("Must provide a user");
        if (user.getSubjectId() == null)
            throw new DoesNotExistException("User does not have a subject");

        try {
            // Execute request, expecting 200 OK
            HttpResponse response = ServerAccess.doRequest("GET", "subjects", HttpStatus.SC_OK,
                    null, user.getAuthHeader());

            // Flesh out fields from the response
            this.user = user;
            updateFromEntity(response.getEntity());
        } catch (AlreadyExistsException e) { /* won't be thrown */ }

        // The server guarantees that if a user has a subjectId, they exist; therefore,
        // we don't have to specifically test for that.  HOWEVER, on the client a user may have
        // their subjectId changed before caling update on the user.  It is up to the client to
        // avoid getting a subject without first updating.
    }

    /**
     * Commits the user's current state to the server.  If the user's state has been updated
     * since it was last retrieved, an exception is thrown.
     * @throws OldDataException         Thrown if the server's state has been changed since last
     *                                  retrieved.
     * @throws AuthenticationException
     * @throws NetworkFailureException
     */
    public void update() throws OldDataException, AuthenticationException, NetworkFailureException {

        // Nothing to update: just refresh the subject's information.
        if (state == null || state.equals(storedState)) {
            try {
                HttpResponse response = ServerAccess.doRequest("GET", "subjects", HttpStatus.SC_OK,
                        null, user.getAuthHeader());
                updateFromEntity(response.getEntity());
            } catch (AlreadyExistsException e) { /* won't be thrown */ }

        // State has changed and we need to update it.
        } else {

            // Get the latest state from the server and compare against the stored state.
            // If they're not equal then somebody else has updated the server state for
            // this subject.
            try {
                HttpResponse response = ServerAccess.doRequest("GET", "subjects", HttpStatus.SC_OK,
                        null, user.getAuthHeader());
                JSONObject json = new JSONObject(EntityUtils.toString(response.getEntity()));
                if (json.has("state") && !json.getJSONObject("state").equals(storedState)) {
                    throw new OldDataException("State updated on server since last retrieved");
                }
            } catch (AlreadyExistsException e) { /* won't be thrown */
            } catch (JSONException|IOException e) {
                throw new IllegalArgumentException(e);
            }

            // Create body
            JSONObject json = new JSONObject();
            try {
                json.put("state", state);
            } catch (JSONException e) { e.printStackTrace(); }

            // Execute request; expecting 200 OK
            try {
                HttpResponse response = ServerAccess.doRequest("POST", "subjects", HttpStatus.SC_OK,
                        json, user.getAuthHeader());
                updateFromEntity(response.getEntity());
            } catch (AlreadyExistsException e) { /* won't be thrown */ }
        }
    }

    /**
     * Updates this subject from the (JSON) body of an HTTP response.
     */
    private void updateFromEntity(HttpEntity entity) {
        try {
            JSONObject json = new JSONObject(EntityUtils.toString(entity));
            id = json.getString("id");
            name = json.getString("name");
            datapool = (json.has("datapool")) ? json.getJSONObject("datapool") : null;
            state = (json.has("state")) ? json.getJSONObject("state") : null;
            storedState = state;
        } catch (JSONException|IOException e) {
            throw new IllegalArgumentException(e);
        }
    }

    /** Getters and setters.  Use update() to commit state to server */
    public JSONObject getDatapool() {
        return datapool;
    }
    public String getDatapoolString() {
        return datapool!=null ? datapool.toString() : null;
    }
    public JSONObject getState() {
        return state;
    }
    public String getStateString() {
        return state!=null ? state.toString() : null;
    }
    public void setState(JSONObject json) {
        state = json;
    }
    public void setState(String json) throws JSONException {
        state = new JSONObject(json);
    }
    public User getUser() {
        return user;
    }
    public String getId() {
        return id;
    }
}
