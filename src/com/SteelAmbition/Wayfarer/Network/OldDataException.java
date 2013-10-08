package com.SteelAmbition.Wayfarer.Network;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: Tom
 * Date: 6/10/13
 * Time: 1:52 PM
 * To change this template use File | Settings | File Templates.
 */
public class OldDataException extends Exception {
    public OldDataException(String msg) {
        super(msg);
    }

    public OldDataException(IOException e) {
        super(e);
    }
}