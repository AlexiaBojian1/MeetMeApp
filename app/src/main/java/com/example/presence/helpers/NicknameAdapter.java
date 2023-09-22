package com.example.presence.helpers;

import android.app.Activity;
import android.os.Parcelable;
import android.util.Log;
import android.widget.Toast;

import com.example.presence.objects.RegularUser;
import com.example.presence.activities.HomeActivity;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * This class is used to perform various user-related actions on the database.
 */
public class NicknameAdapter {
    /** Stores the nickname that has been found in the database.*/
    private String nickname;
    /** Stores the number of times this nickname has already been used by users in the database.*/
    private int nameCount = 0;
    /** The activity that uses this NicknameAdapter.*/
    private Activity activity;

    public NicknameAdapter(Activity activity) {
        this.activity = activity;
    }

    /**
     * Gets the nickname of a user from the database.
     *
     * @param phoneNumber  Phone number of the account we want the nickname from
     */
    public Task<QuerySnapshot> findNickname(String phoneNumber) throws IllegalArgumentException {
        // Get a handle on the database
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        // Find the entry that has a phone number equal to phoneNumber
        Query userByPhone = db.collection(LabelGetter.dbUserLbl).whereEqualTo(
                LabelGetter.phoneNumberLbl, phoneNumber);
        return userByPhone.get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            // set the nickname
                            nickname = document.getString(LabelGetter.nicknameLbl);
                        }
                    } else {
                        throw new IllegalArgumentException("no nickname in database for " +
                                phoneNumber);
                    }
                });
    }

    /**
     * Updates the nickname of the user with the specified phone number in the database.
     *
     * @param phoneNumber  The phone number of the user whose nickname is to be changed
     * @param newNickname  The nickname to change to
     * @throws IllegalArgumentException when there already exists a user with the newNickname
     */
    public void updateNickname(String phoneNumber, String newNickname)
            throws IllegalArgumentException {
        // count the number of users that already have this nickname, should be 0
        countNameOccurrences(newNickname).addOnCompleteListener(task1 -> {
            if (getNameCount() == 0) {
                // get a handle on the database
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                // update the nickname of the user
                db.collection(LabelGetter.dbUserLbl).document(phoneNumber).
                        update(LabelGetter.nicknameLbl, newNickname).
                        addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(activity, "Nickname changed",
                                        Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(activity, "Error changing username: " +
                                        task.getException(), Toast.LENGTH_SHORT).show();
                            }
                        });
            } else {
                Toast.makeText(activity, "Nickname already taken", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Checks if a nickname is unique in the database.
     * @param nickname  The nickname to check for uniqueness
     * @modifies nameCount
     */
    public Task<QuerySnapshot> countNameOccurrences(String nickname)
            throws IllegalArgumentException {
        // get a handle on the database
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        // find the users with this nickname
        Query userByPhone = db.collection("users").whereEqualTo(
                LabelGetter.nicknameLbl, nickname);
        return userByPhone.get()
                .addOnCompleteListener(task -> {
                        nameCount = 0;
                        // count the number of users with this nickname
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            nameCount++;
                        }
                });
    }

    /**
     * Deletes all database entries that mention this account.
     *
     * @param phoneNumber  Phone number of the account to be deleted
     */
    public void deleteAccountData(String phoneNumber) {
        // get a handle on the database
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        // query all documents with the specified phone number
        for (String path : LabelGetter.collectionPaths) {
            CollectionReference collection = db.collection(path);
            Query query = collection
                    .whereEqualTo(LabelGetter.phoneNumberLbl, phoneNumber);

            query.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    // delete all found documents
                    QuerySnapshot querySnapshot = task.getResult();
                    for (DocumentSnapshot doc : querySnapshot.getDocuments()) {
                        doc.getReference().delete();
                    }
                } else {
                    Toast.makeText(activity, "Error getting documents: " +
                            task.getException(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    /**
     * Uploads a new user to the database.
     *
     * @param name  Nickname of the new user
     * @param number  Phone number of the new user
     */
    public void registerUser(String name, String number) {
        // now that we both have phone number and name, we can register new user.
        FirebaseFirestore db = FirebaseFirestore.getInstance(); // get a handle on the database
        Map<String, Object> newUser = new HashMap<>(); // create a new user
        newUser.put(LabelGetter.phoneNumberLbl, number); // set its properties
        newUser.put(LabelGetter.nicknameLbl, name);
        newUser.put(LabelGetter.blockedLbl, false);

        // add the new user to the database:
        ArrayList<Parcelable> dataToPass = new ArrayList() {{
            add(new RegularUser(number));
        }};
        db.collection(LabelGetter.dbUserLbl).document(number)
                .set(newUser).addOnCompleteListener(new UploadCompleteHandler(activity,
                        HomeActivity.class, dataToPass));
    }

    /**
     * Returns the nickname found in {@code findNickname()}.
     *
     * @return The nickname associated with the phone number last passed to findNickname()
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * Returns the number of accounts found in {@code countNameOccurrences()}.
     *
     * @return The number of accounts found with the nickname last passed to countNameOccurrences()
     */
    public int getNameCount() {
        return nameCount;
    }
}
