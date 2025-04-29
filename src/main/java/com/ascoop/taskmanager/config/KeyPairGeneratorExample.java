package com.ascoop.taskmanager.config;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Base64;

public class KeyPairGeneratorExample {
    public static void main(String[] args) throws Exception {
        // Créer un générateur de clés RSA
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);  // Taille de la clé (2048 bits)

        // Générer la paire de clés
        KeyPair keyPair = keyPairGenerator.generateKeyPair();

        // Récupérer les clés publique et privée
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();

        // Afficher les clés sous forme Base64
        String publicKeyBase64 = Base64.getEncoder().encodeToString(publicKey.getEncoded());
        String privateKeyBase64 = Base64.getEncoder().encodeToString(privateKey.getEncoded());

        System.out.println("Public Key: \n" + publicKeyBase64);
        System.out.println("Private Key: \n" + privateKeyBase64);
    }
}
