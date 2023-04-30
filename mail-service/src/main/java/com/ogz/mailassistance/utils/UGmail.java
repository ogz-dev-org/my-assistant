package com.ogz.mailassistance.utils;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;

public class UGmail {

    public static com.google.api.services.gmail.Gmail get(GoogleCredential credential){
        return new com.google.api.services.gmail.Gmail.Builder(new NetHttpTransport(), GsonFactory.getDefaultInstance(), credential)
                .setApplicationName("My-Asisstance-Mail-Service")
                .build();
    }

}
