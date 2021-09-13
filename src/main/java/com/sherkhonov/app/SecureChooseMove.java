package com.sherkhonov.app;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;

public class SecureChooseMove {
    private final SecureRandom random = new SecureRandom();
    private final String HMAC_ALGO = "HmacSHA256";
    private String key = "";

    public int getSecureMove(List<String> moves) throws NoSuchAlgorithmException, InvalidKeyException {
        int movePos = getRandomMove(moves);
        byte[] bytes = new byte[128 / 8];
        random.nextBytes(bytes);
        Mac sha256 = Mac.getInstance(HMAC_ALGO);
        SecretKeySpec secretKey = new SecretKeySpec(bytes, HMAC_ALGO);
        sha256.init(new SecretKeySpec(bytes, HMAC_ALGO));
        System.out.println("HMAC:\n" + bytesToHex(sha256.doFinal(moves.get(movePos).getBytes())));
        key = bytesToHex(secretKey.getEncoded());
        return movePos;
    }

    private int getRandomMove(List<String> m){
        return random.nextInt(m.size());
    }

    private String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder(bytes.length * 2);
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString().toUpperCase();
    }

    public String getKey() {
        return key;
    }
}
