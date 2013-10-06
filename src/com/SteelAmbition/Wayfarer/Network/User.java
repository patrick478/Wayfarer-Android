package com.SteelAmbition.Wayfarer.Network;

import org.apache.http.HttpEntity;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Represents a User of the application, as stored on the server... minus their password.
 */
public class User {

    private String id;
    private String email;
    private String name;
    private String subjectId;

    /**
     * Creates a blank user.
     */
    public User() {
    }

    /**
     * Creates a user with the specified information.
     */
    public User(String id, String email, String name, String subjectId) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.subjectId = subjectId;
    }

    /**
     * Creates a user from the (JSON) body of an HTTP response.
     */
    public User(HttpEntity entity) {
        try {
            JSONObject json = new JSONObject(EntityUtils.toString(entity));
            id = json.getString("id");
            email = json.getString("email");
            name = json.getString("name");
            subjectId = (json.has("subjectId")) ? json.getString("subjectId") : null;
        } catch (JSONException|IOException e) {
            throw new IllegalArgumentException(e);
        }
    }

    /* Getters and setters */

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getSubjectId() {
        return subjectId;
    }
    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
}
