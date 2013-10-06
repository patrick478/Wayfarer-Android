package com.SteelAmbition.Wayfarer;

import junit.framework.Test;
import junit.framework.TestSuite;
import android.test.suitebuilder.TestSuiteBuilder;

/**
 * A suite containing all tests for the app.
 */
public class Tests extends TestSuite {
    public static Test suite() {
        return new TestSuiteBuilder(Tests.class).includeAllPackagesUnderHere().build();
    }
}
