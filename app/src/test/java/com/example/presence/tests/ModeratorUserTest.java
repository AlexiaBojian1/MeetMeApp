package com.example.presence.tests;

import static org.junit.Assert.assertEquals;

import com.example.presence.objects.ModeratorUser;

import org.junit.Before;
import org.junit.Test;

public class ModeratorUserTest {

    private ModeratorUser moderatorUser;

    @Before
    public void setUp() throws Exception {
        moderatorUser = new ModeratorUser("1234567890", "Test Moderator");
    }

    @Test
    public void testGetPhoneNumber() {
        assertEquals("1234567890", moderatorUser.getPhoneNumber());
    }

    @Test
    public void testGetNickname() {
        assertEquals("Test Moderator", moderatorUser.getNickname());
    }

}
