
// REFERENCE
/**
 * Article on Medium -
 * https://apogiatzis.medium.com/shamirs-secret-sharing-a-numeric-example-walkthrough-a59b288c34c4
 * GFG -
 * https://www.geeksforgeeks.org/shamirs-secret-sharing-algorithm-cryptography/
 * Wikipedia - https://en.wikipedia.org/wiki/Shamir%27s_secret_sharing
 * LinkedIn - https://www.linkedin.com/pulse/how-do-pirates-share-treasure-map-dont-trust-each-other-buchanan
 */

import java.math.BigInteger;
import java.util.*;
import java.io.*;

public final class Shamir {

    public static String S = "";

    public final class SecretShare {
        public SecretShare(final int num, final BigInteger share) {
            this.num = num;
            this.share = share;
        }

        public int getNum() {
            return num;
        }

        public BigInteger getShare() {
            return share;
        }

        @Override
        public String toString() {
            S += "Secret Share [num=" + num + ", share=" + share + "]\n";
            return "Secret Share [num=" + num + ", share=" + share + "]";
        }

        private final int num;
        private final BigInteger share;
    }

    public Shamir(final int k, final int n) {
        this.k = k;
        this.n = n;

        random = new Random();
    }

    public SecretShare[] split(final BigInteger secret) {
        final int modLength = secret.bitLength() + 1;

        P = new BigInteger(modLength, CERTAINTY, random);
        final BigInteger[] coeff = new BigInteger[k - 1];

        System.out.println("Prime Number & (a0): " + P);

        for (int i = 0; i < k - 1; i++) {
            coeff[i] = randomZp(P);
            System.out.println("a" + (i + 1) + ": " + coeff[i]);
        }

        final SecretShare[] shares = new SecretShare[n];
        for (int i = 1; i <= n; i++) {
            BigInteger accum = secret;

            for (int j = 1; j < k; j++) {
                final BigInteger t1 = BigInteger.valueOf(i).modPow(BigInteger.valueOf(j), P);
                final BigInteger t2 = coeff[j - 1].multiply(t1).mod(P);

                accum = accum.add(t2).mod(P);
            }
            shares[i - 1] = new SecretShare(i - 1, accum);
            System.out.println(shares[i - 1]);
        }

        return shares;
    }

    public BigInteger getP() {
        return P;
    }

    public BigInteger reconstruction(final SecretShare[] shares, final BigInteger primeNum) {
        BigInteger accum = BigInteger.ZERO;
        S += "\n\nReconstruction....\n";
        for (int i = 0; i < k; i++) {
            BigInteger num = BigInteger.ONE;
            BigInteger den = BigInteger.ONE;

            for (int j = 0; j < k; j++) {
                if (i != j) {
                    num = num.multiply(BigInteger.valueOf(-j - 1)).mod(primeNum);
                    den = den.multiply(BigInteger.valueOf(i - j)).mod(primeNum);
                }
            }
            S += "den: " + den + ", num: " + den + ", inv: " + den.modInverse(primeNum) + "\n";
            // System.out.println("den: " + den + ", num: " + den + ", inv: " +
            // den.modInverse(primeNum));
            final BigInteger value = shares[i].getShare();

            final BigInteger tmp = value.multiply(num).multiply(den.modInverse(primeNum)).mod(primeNum);
            accum = accum.add(primeNum).add(tmp).mod(primeNum);

            S += "value: " + value + ", tmp: " + tmp + ", accum: " + accum + "\n";
            // System.out.println("value: " + value + ", tmp: " + tmp + ", accum: " +
            // accum);
        }

        return accum;
    }

    private BigInteger randomZp(final BigInteger p) {
        while (true) {
            final BigInteger r = new BigInteger(p.bitLength(), random);
            if (r.compareTo(BigInteger.ZERO) > 0 && r.compareTo(p) < 0) {
                return r;
            }
        }
    }

    public static String convertStringToHex(String str) {
        StringBuffer hex = new StringBuffer();

        for (char temp : str.toCharArray()) {
            int decimal = (int) temp;
            hex.append(Integer.toHexString(decimal));
        }

        return hex.toString();
    }

    public static String convertHexToString(String hex) {

        StringBuilder result = new StringBuilder();
        for (int i = 0; i < hex.length() - 1; i += 2) {
            String tempInHex = hex.substring(i, (i + 2));
            int decimal = Integer.parseInt(tempInHex, 16);
            result.append((char) decimal);
        }
        return result.toString();
    }

    public static String FileReader() {
        String plain = "";
        try {
            FileReader reader = new FileReader("file.txt");
            int i;
            while ((i = reader.read()) != -1) {
                plain += (char) i;
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return plain;
    }

    public static void plainTextWriter(String result) {
        try {
            File plaintext = new File("PlaintText.txt");
            System.out.println("File created: " + plaintext.getName());

            String plain = result;
            FileWriter writer = new FileWriter(plaintext);
            writer.write(plain);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void sharesWriter(String shares) {
        try {
            File share = new File("Shares.txt");
            System.out.println("File created: " + share.getName());

            String plain = shares;
            FileWriter writer = new FileWriter(share);
            writer.write(plain);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private BigInteger P;

    private int k;
    private int n;
    private Random random;

    private static final int CERTAINTY = 10;

    public static void main(final String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.print("Number of persons in a group: ");
        int N = sc.nextInt();
        System.out.print("Number of minimum person required to unlock: ");
        int K = sc.nextInt();
        final Shamir shamir = new Shamir(K, N);

        // String Initialisation
        String text = FileReader();
        String hex = convertStringToHex(text);

        final BigInteger secret = new BigInteger(hex, 16);
        final SecretShare[] shares = shamir.split(secret);
        final BigInteger prime = shamir.getP();

        final Shamir shamir2 = new Shamir(K, N);
        final BigInteger result = shamir2.reconstruction(shares, prime);
        sharesWriter(S);

        String plaintext = convertHexToString(result.toString(16));
        System.out.println("Decoded Secret is stored in: ");

        plainTextWriter(plaintext);
    }
}