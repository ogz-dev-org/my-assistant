package com.ogz.message.GoogleUserVerifier;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import org.ogz.constants.ClientID;

import java.util.Collections;

public class GoogleUserVerifier {
    private static GoogleUserVerifier instance;
    private final GoogleIdTokenVerifier verifier;
    private GoogleUserVerifier(){
        verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new GsonFactory())
                // Specify the CLIENT_ID of the app that accesses the backend:
                .setAudience(Collections.singletonList(ClientID.WebAppClientID))
                // Or, if multiple clients access the backend:
                //.setAudience(Arrays.asList(CLIENT_ID_1, CLIENT_ID_2, CLIENT_ID_3))
                .build();
    }

    public static GoogleUserVerifier getInstance() {
        if (instance == null) instance = new GoogleUserVerifier();
        return instance;
    }

    public GoogleIdTokenVerifier getVerifier() {
        return verifier;
    }

}
