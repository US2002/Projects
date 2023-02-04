
// REFERENCE
/**
 * Article on Medium -
 * https://apogiatzis.medium.com/shamirs-secret-sharing-a-numeric-example-walkthrough-a59b288c34c4
 * GFG -
 * https://www.geeksforgeeks.org/shamirs-secret-sharing-algorithm-cryptography/
 * Wikipedia - https://en.wikipedia.org/wiki/Shamir%27s_secret_sharing
 */

import java.math.*;

public class code {

    public static String convertStringToHex(String str) {
        StringBuffer hex = new StringBuffer();

        // loop chars one by one
        for (char temp : str.toCharArray()) {

            // convert char to int, for char `a` decimal 97
            int decimal = (int) temp;

            // convert int to hex, for decimal 97 hex 61
            hex.append(Integer.toHexString(decimal));
        }

        return hex.toString();
    }

    public static String convertHexToString(String hex) {

        StringBuilder result = new StringBuilder();

        // split into two chars per loop, hex, 0A, 0B, 0C...
        for (int i = 0; i < hex.length() - 1; i += 2) {

            String tempInHex = hex.substring(i, (i + 2));

            // convert hex to decimal
            int decimal = Integer.parseInt(tempInHex, 16);

            // convert the decimal to char
            result.append((char) decimal);

        }

        return result.toString();
    }

    public static void main(String[] args) {
        String A = "SeCrEt";
        String hex = convertStringToHex(A);
        BigInteger decimal = new BigInteger(hex, 16);
        System.out.println(hex + " " + decimal);
    }
}
