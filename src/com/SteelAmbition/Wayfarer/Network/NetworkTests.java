package com.SteelAmbition.Wayfarer.Network;

import junit.framework.Assert;
import android.test.AndroidTestCase;

/**
 * A suite of tests for networking.
 */
public class NetworkTests extends AndroidTestCase {

    /**
     * Simply tests that the server can be accessed.
     */
    public void testConnectivity() throws Throwable {
        Assert.assertTrue(ServerAccess.connectionTest());
    }

    /**
     * Tests logging in a user successfully and then deleting them.
     * Useful to test in this order, so as to allow createUser's test to work,
     * but might fail if the test user doesn't exist.
     */
    public void testLoginDeleteSuccess() throws Throwable {
        User user = null;
        try {
            // Log in
            user = ServerAccess.login("unittest@test.com", "12345");
        } catch (AuthenticationException|NetworkFailureException e) {
            Assert.fail();
        }

        try {
            // Delete user
            Assert.assertTrue(ServerAccess.deleteUser());
        } catch (AuthenticationException|NetworkFailureException e) {
            Assert.fail();
        }
        // Ensure they don't exist
        try {
            user = ServerAccess.login("unittest@test.com", "12345");
            Assert.fail();
        } catch (AuthenticationException e) {
            // good :D
        } catch (NetworkFailureException e) {
            Assert.fail();
        }
    }

    /**
     * Tests creating a user. If the user already exists, thats ok.
     */
    public void testCreateUserSuccess() throws Throwable {
        User user = null;
        try {
            user = ServerAccess.createUser("unittest@test.com", "Unittest", "12345");
        } catch (AlreadyExistsException e) {
            // not ideal for the test, but acceptable
        } catch (NetworkFailureException e) {
            Assert.fail();
        }
        if (user != null)
            Assert.assertEquals(user.getName(), "Unittest");
    }
    /**
     * Tests logging in a user with wrong password
     */
    public void testLoginFailurePassword() throws Throwable {
        User user = null;
        try {
            user = ServerAccess.login("unittest@test.com", "god");
        } catch (AuthenticationException e) {
            // good
        } catch (NetworkFailureException e) {
            Assert.fail();
        }
        Assert.assertEquals(user, null);
    }

    /**
     * Tests logging in a user with user that doesn't exist
     */
    public void testLoginFailureEmail() throws Throwable {
        User user = null;
        try {
            user = ServerAccess.login("bababooi@test.com", "12345");
        } catch (AuthenticationException e) {
            // good
        } catch (NetworkFailureException e) {
            Assert.fail();
        }
        Assert.assertEquals(user, null);
    }

}