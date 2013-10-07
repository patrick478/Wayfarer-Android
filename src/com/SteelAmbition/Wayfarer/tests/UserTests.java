package com.SteelAmbition.Wayfarer.tests;

import com.SteelAmbition.Wayfarer.Network.*;
import junit.framework.Assert;
import android.test.AndroidTestCase;

/**
 * A suite of tests for user networking.
 */
public class UserTests extends AndroidTestCase {

    /**
     * Simply tests that the server can be accessed.
     */
    public void testConnectivity() throws Throwable {
        Assert.assertTrue(ServerAccess.connectionTest());
    }

    /**
     * Tests creating a user. If the user already exists, thats ok.
     */
    public void testCreateUserSuccess() throws Throwable {
        try {
            new User("created@test.com", "name", "password");
        } catch (AlreadyExistsException e) {
            // not ideal for the test, but acceptable
        } catch (NetworkFailureException e) {
            Assert.fail();
        }
    }

    /**
     * Tests creating a duplicate-email user.
     */
    public void testCreateUserAlreadyExists() throws Throwable {
        try {
            new User("created@test.com", "name", "password");
            Assert.fail(); //bad if it got to here
        } catch (AlreadyExistsException e) {
            // good
        } catch (NetworkFailureException e) {
            Assert.fail();
        }
    }

    /**
     * Tests getting a user.
     */
    public void testGetUserSuccess() throws Throwable {
        // try to create user
        try {
            new User("gettest@test.com", "name", "password");
        } catch (AlreadyExistsException e) {
            // don't care
        } catch (NetworkFailureException e) {
            Assert.fail();
        }
        // log in as them
        try {
            new User("gettest@test.com", "password");
        } catch (AuthenticationException e) {
            Assert.fail();
        } catch (NetworkFailureException e) {
            Assert.fail();
        }
    }

    /**
     * Tests getting a user that doesn't exist
     */
    public void testGetUserNonexistent() throws Throwable {
        try {
            new User("ballsballsballs@balls.com", "balls!");
            Assert.fail(); //bad if it got to here
        } catch (AuthenticationException e) {
            // good
        } catch (NetworkFailureException e) {
            Assert.fail();
        }
    }

    /**
     * Tests getting a user with wrong password
     */
    public void testGetUserBadPassword() throws Throwable {
        // try to create user
        try {
            new User("badpass@test.com", "name", "password");
        } catch (AlreadyExistsException e) {
            // don't care
        } catch (NetworkFailureException e) {
            Assert.fail();
        }
        try {
            new User("badpass@test.com", "modern major general");
            Assert.fail(); //bad if it got to here
        } catch (AuthenticationException e) {
            // good
        } catch (NetworkFailureException e) {
            Assert.fail();
        }
    }

    /**
     * Tests updating a user with nothing to update.
     */
    public void testUpdateUserNothing() throws Throwable {
        // try to create user
        try {
            new User("updated@test.com", "name", "password");
        } catch (AlreadyExistsException e) {
            // don't care
        } catch (NetworkFailureException e) {
            Assert.fail();
        }
        // update them
        try {
            User user = new User("updated@test.com", "password");
            user.update();
        } catch (AuthenticationException e) {
            Assert.fail();
        } catch (NetworkFailureException e) {
            Assert.fail();
        }
    }

    /**
     * Tests updating a user.
     * Assumes user exists
     */
    public void testUpdateUserSuccess() throws Throwable {
        // try to create user
        try {
            new User("updated2@test.com", "name", "password");
        } catch (AlreadyExistsException e) {
            // don't care
        } catch (NetworkFailureException e) {
            Assert.fail();
        }
        // Get and update user
        try {
            User user = new User("updated2@test.com", "password");
            user.setName("Bob");
            user.update();
        } catch (AuthenticationException e) {
            Assert.fail();
        } catch (NetworkFailureException e) {
            Assert.fail();
        }
        // Get again and test
        try {
            User user = new User("updated2@test.com", "password");
            Assert.assertEquals(user.getName(), "Bob");
        } catch (AuthenticationException e) {
            Assert.fail();
        } catch (NetworkFailureException e) {
            Assert.fail();
        }
    }

    /**
     * Tests deleting a user.
     * Assumes user exists
     */
    public void testDeleteUserSuccess() throws Throwable {
        // try to create user
        try {
            new User("delete@test.com", "name", "password");
        } catch (AlreadyExistsException e) {
            // don't care
        } catch (NetworkFailureException e) {
            Assert.fail();
        }
        // Get and delete user
        User user = null;
        try {
            user = new User("delete@test.com", "password");
        } catch (AuthenticationException e) {
            Assert.fail();
        } catch (NetworkFailureException e) {
            Assert.fail();
        }
        try {
            user.delete();
        } catch (AuthenticationException e) {
            Assert.fail();
        } catch (NetworkFailureException e) {
            Assert.fail();
        }
        // Try log in to deleted user
        try {
            user = new User("delete@test.com", "password");
            Assert.fail();
        } catch (AuthenticationException e) {
            // good
        } catch (NetworkFailureException e) {
            Assert.fail();
        }
    }


}