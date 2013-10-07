package com.SteelAmbition.Wayfarer.tests;

import android.test.AndroidTestCase;
import com.SteelAmbition.Wayfarer.Network.*;
import junit.framework.Assert;

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

}