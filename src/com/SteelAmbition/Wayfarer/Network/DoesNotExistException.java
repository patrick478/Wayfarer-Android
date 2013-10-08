package com.SteelAmbition.Wayfarer.Network;

import java.io.IOException;

public class DoesNotExistException extends Exception {
    public DoesNotExistException(String msg) {
        super(msg);
    }

    public DoesNotExistException(IOException e) {
        super(e);
    }
}