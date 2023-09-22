package com.example.presence.tests;

import android.os.Parcel;

import com.example.presence.activities.HomeActivity;
import com.example.presence.objects.AppUser;
import com.example.presence.objects.Coords;
import com.example.presence.objects.RegularUser;
import com.google.firebase.FirebaseApp;

import org.junit.Before;
import org.junit.Test;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class AppUserTest {

    // Create a mock user object for testing
    private AppUser user;

    @Before
    public void setup() {
//        FirebaseApp.initializeApp();
        user = new RegularUser("+1234567890", "TestUser");
    }

    @Test
    public void testGetPhoneNumber() {

        assertEquals("+1234567890", user.getPhoneNumber());
    }

    @Test
    public void testGetNickname() {
        assertEquals("TestUser", user.getNickname());
    }

    @Test
    public void testBlockUser() {
        // Ensure user is not initially blocked
        assertFalse(user.isBlocked());

        // Block the user
        user.block();

        // Ensure the user is now blocked
        assertTrue(user.isBlocked());

        // Unblock the user
        user.block();

        // Ensure the user is now unblocked
        assertFalse(user.isBlocked());
    }
}
