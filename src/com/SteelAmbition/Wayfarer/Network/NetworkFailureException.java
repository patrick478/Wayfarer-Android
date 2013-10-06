package com.SteelAmbition.Wayfarer.Network;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: Tom
 * Date: 6/10/13
 * Time: 2:33 PM
 * To change this template use File | Settings | File Templates.
 */
public class NetworkFailureException extends Exception {
    public NetworkFailureException(String msg) {
        super(msg);
    }

    public NetworkFailureException(IOException e) {
        super(e);
    }
}

