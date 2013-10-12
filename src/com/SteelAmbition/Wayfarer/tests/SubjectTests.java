package com.SteelAmbition.Wayfarer.tests;

import android.test.AndroidTestCase;
import com.SteelAmbition.Wayfarer.Network.*;
import junit.framework.Assert;
import org.json.JSONObject;

/**
 * A suite of tests for subject networking.
 */
public class SubjectTests extends AndroidTestCase {

    /**
     * Simply tests that the server can be accessed.
     */
    public void testConnectivity() throws Throwable {
        Assert.assertTrue(ServerAccess.connectionTest());
    }

    /**
     * Create a subject
     */
    public void testCreateSubjectSuccess() throws Throwable {
        // Create/get a test user.
        User user = null;
        try {
            user = new User("createsubject@test.com", "name", "password");
        } catch (AlreadyExistsException e) {
            user = new User("createsubject@test.com", "password");
        }
        // Create a subject for them
        new Subject(user, "Test");
    }

    /**
     * Get a subject via constructor
     */
    public void testGetSubjectConstructorSuccess() throws Throwable {
        // Create/get a test user.
        User user = null;
        try {
            user = new User("createsubject@test.com", "name", "password");
        } catch (AlreadyExistsException e) {
            user = new User("createsubject@test.com", "password");
        }
        // Create a subject for them
        new Subject(user, "Test");
        // Get that subject
        new Subject(user);
    }

    /**
     * Get a subject via user method
     */
    public void testGetSubjectMethodSuccess() throws Throwable {
        // Create/get a test user.
        User user = null;
        try {
            user = new User("createsubject@test.com", "name", "password");
        } catch (AlreadyExistsException e) {
            user = new User("createsubject@test.com", "password");
        }
        // Create a subject for them
        new Subject(user, "Test");
        // Get that subject
        user.getSubject();
    }

    /**
     * Get a subject that doesn't exist via constructor
     */
    public void testGetSubjectConstructorFail() throws Throwable {
        // Create/get a test user.
        User user = null;
        try {
            user = new User("subjectnoexist@test.com", "name", "password");
        } catch (AlreadyExistsException e) {
            user = new User("subjectnoexist@test.com", "password");
        }
        // Get a nonexistent subject
        try {
            new Subject(user);
            Assert.fail(); //bad if got to here
        } catch (DoesNotExistException e) {
            // good
        }
    }

    /**
     * Get a subject that doesn't exist via method
     */
    public void testGetSubjectMethodFail() throws Throwable {
        // Create/get a test user.
        User user = null;
        try {
            user = new User("subjectnoexist@test.com", "name", "password");
        } catch (AlreadyExistsException e) {
            user = new User("subjectnoexist@test.com", "password");
        }
        // Get a nonexistent subject
        user.getSubject();
    }

    /**
     * Update a subject to latest copy with no changes
     */
    public void testUpdateNoChanges() throws Throwable {
        // Create/get a test user.
        User user = null;
        try {
            user = new User("subupdate1@test.com", "name", "password");
        } catch (AlreadyExistsException e) {
            user = new User("subupdate1@test.com", "password");
        }
        // Create a subject
        Subject subject = new Subject(user,"Test");
        // Update for no good reason
        subject.update();
    }

    /**
     * Update a subject with a new state
     */
    public void testUpdateNewState() throws Throwable {
        // Create/get a test user.
        User user = null;
        try {
            user = new User("subupdate2@test.com", "name", "password");
        } catch (AlreadyExistsException e) {
            user = new User("subupdate2@test.com", "password");
        }
        // Create a subject
        Subject subject = new Subject(user,"Test");
        // Update state
        JSONObject state = new JSONObject();
        JSONObject stateContents = new JSONObject();
        stateContents.put("test1","boop");
        state.put("state", stateContents);
        subject.setState(state);
        subject.update();
        // Check results
        Assert.assertEquals(state.toString(),subject.getState().toString());
        subject = new Subject(user);
        Assert.assertEquals(state.toString(),subject.getState().toString());
    }

    /**
     * Update a subject with a new state, but it having been changed already.
     */
    public void testUpdateStateConflict() throws Throwable {
        // Create/get a pair of test users.
        User user1 = null;
        try {
            user1 = new User("subupdate3a@test.com", "name", "password");
        } catch (AlreadyExistsException e) {
            user1 = new User("subupdate3a@test.com", "password");
        }
        User user2 = null;
        try {
            user2 = new User("subupdate3b@test.com", "name", "password");
        } catch (AlreadyExistsException e) {
            user2 = new User("subupdate3b@test.com", "password");
        }
        // Create a subject for user1
        Subject subject1 = new Subject(user1,"Test");
        // Add that subject to user2
        user2.setSubjectId(subject1.getId());
        user2.update();
        // Now get a fresh reference to that subject for user 2
        Subject subject2 = new Subject(user2);

        // User 1 updates subject's state successfully
        JSONObject state = new JSONObject();
        JSONObject stateContents = new JSONObject();
        stateContents.put("test1","boop");
        state.put("state", stateContents);
        subject1.setState(state);
        subject1.update();

        // User 2 tries to update subject's state but conflicts with user 1's.
        state = new JSONObject();
        stateContents = new JSONObject();
        stateContents.put("test2","floooorp!");
        state.put("state", stateContents);
        subject2.setState(state);
        try {
            subject2.update();
            Assert.fail(); //bad if got up to here
        } catch (OldDataException e) {
            // good
        }
    }

    /**
     * Update a subject with a new state, but it having been changed already,
     * then refresh to the latest data.
     */
    public void testUpdateStateThenRefresh() throws Throwable {
        // As above test, set up 2 users to point to 1 subject and get a
        // second reference to that subject.
        User user1 = null;
        try {
            user1 = new User("subupdate4a@test.com", "name", "password");
        } catch (AlreadyExistsException e) {
            user1 = new User("subupdate4a@test.com", "password");
        }
        User user2 = null;
        try {
            user2 = new User("subupdate4b@test.com", "name", "password");
        } catch (AlreadyExistsException e) {
            user2 = new User("subupdate4b@test.com", "password");
        }
        Subject subject1 = new Subject(user1,"Test");
        user2.setSubjectId(subject1.getId());
        user2.update();
        Subject subject2 = new Subject(user2);

        // User 1 updates subject's state successfully
        JSONObject state = new JSONObject();
        JSONObject stateContents = new JSONObject();
        stateContents.put("test1","boop");
        state.put("state", stateContents);
        subject1.setState(state);
        subject1.update();

        // User 2 tries to update subject's state but conflicts with user 1's.
        state = new JSONObject();
        stateContents = new JSONObject();
        stateContents.put("test2","floooorp!");
        state.put("state", stateContents);
        subject2.setState(state);
        try {
            subject2.update();
            Assert.fail(); //bad if got up to here
        } catch (OldDataException e) {
            // good
        }

        // User 2 refreshes their subject reference, check to see it's the same
        // as what user 1's subject reference uses.
        subject2.refresh();
        Assert.assertEquals(subject1.getState().toString(),
                subject2.getState().toString());
    }
}