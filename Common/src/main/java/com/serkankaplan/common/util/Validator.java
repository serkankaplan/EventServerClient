package com.serkankaplan.common.util;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Validator
 * Created by serkan on 08/09/16.
 */
public class Validator {

    public static Integer portValidator(String port) throws IllegalArgumentException {
        Integer portInt = null;
        try {
            portInt = Integer.valueOf(port);
        } catch (NumberFormatException nfe) {
            throw new IllegalArgumentException("Numeric port parameter is required");
        }
        if (portInt == null || portInt > 65535  || portInt < 1024) { // the first 1024 ports restricted to the root user only
            throw new IllegalArgumentException("Port parameter needs to be between 1025 - 65535");
        }
        return portInt;
    }

    public static String hostValidator(String host) throws IllegalArgumentException {
        try {
            InetAddress.getByName(host);
        } catch (UnknownHostException nhe) {
            throw new IllegalArgumentException("Unknown hosts");
        }
        return host;
    }
}
