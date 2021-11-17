package com.example;

import static java.lang.System.getProperty;
import static java.nio.charset.StandardCharsets.UTF_8;


import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import javax.annotation.PostConstruct;
import org.springframework.stereotype.Controller;
import com.google.cloud.firestore.*;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class FirebaseController {

    private static final String FIREBASE_KEY = "firebaseKey";

    private Firestore firestoreDB;

    @PostConstruct
    private void initFirestore() throws IOException {
        InputStream serviceAccount = new ByteArrayInputStream(getProperty(FIREBASE_KEY).getBytes(UTF_8));
        GoogleCredentials credentials = GoogleCredentials.fromStream(serviceAccount);

        FirebaseOptions options = FirebaseOptions.builder()
            .setCredentials(credentials)
            .build();
        FirebaseApp.initializeApp(options);

        firestoreDB = FirestoreClient.getFirestore();
    }

    @RequestMapping(value = "firestore", method = RequestMethod.GET)
    public String messages(Model model) {
        model.addAttribute("firestoreDocs", StreamSupport.stream(firestoreDB.collection("endpoints").listDocuments().spliterator(), false)
                .map(DocumentReference::getId)
                .collect(Collectors.toList()));

        return "firestore";
    }



}
