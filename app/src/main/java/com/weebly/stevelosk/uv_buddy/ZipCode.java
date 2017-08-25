package com.weebly.stevelosk.uv_buddy;

/**
 * Created by steve on 8/25/2017.
 */

public class ZipCode {

    public static boolean isValidZipCode (String zip) {

        int len = zip.length();

        /* Must be a 5 digit code, or a 5 and 4 code.  A 5 and 4 code will be either
          9 digits (all number characters), or 10 (with the space or hyphen)
        */
        if (len != 5 && len != 9 && len != 10) {
            return false;
        }
        // if it has a hyphen, or whatever, remove it.
        if (len == 10) {
            String str1 = zip.substring(0, 5);
            String str2 = zip.substring(6);
            zip = str1 + str2;
        }

        // characters at this point should all be numeric digits.  If they are all digits,
        // they can be successfully parsed as an int.  Otherwise, it is not a valid U.S. zipCode

        try {
            Integer.parseInt(zip);
            // if successful, it is valid
            return true;
        }
        catch (NumberFormatException e) {
            return false;
        }
    }
}
