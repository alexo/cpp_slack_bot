package com.ppb.cpp.db;

import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.PostConstruct;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static com.google.auth.oauth2.GoogleCredentials.fromStream;
import static com.google.firebase.FirebaseApp.initializeApp;
import static com.google.firebase.FirebaseOptions.builder;
import static java.lang.System.getenv;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.stream.Collectors.toList;
import static java.util.stream.StreamSupport.stream;

@Controller
public class FirestoreController {

    private static final String FIREBASE_KEY = "firebaseKey";

    private Firestore firestoreDB;

    @PostConstruct
    private void initFirestore() throws IOException {
        final var serviceAccount = new ByteArrayInputStream(getenv().get(FIREBASE_KEY).getBytes(UTF_8));
        final var credentials = fromStream(serviceAccount);

        final var options = builder()
            .setCredentials(credentials)
            .build();
        initializeApp(options);

        firestoreDB = FirestoreClient.getFirestore();
    }

    @RequestMapping(value = "firestore", method = RequestMethod.GET)
    public String messages(Model model) {
        model.addAttribute("firestoreDocs", stream(firestoreDB.collection("endpoints").listDocuments().spliterator(), false)
                .map(DocumentReference::getId)
                .collect(toList()));
        return "firestore";
    }



}
