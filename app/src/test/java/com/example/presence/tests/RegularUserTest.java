package com.example.presence.tests;

import com.example.presence.objects.RegularUser;

import junit.framework.TestCase;

import org.junit.Test;

public class RegularUserTest extends TestCase {

        @Test
        public void testRegularUserConstructorWithPhoneNumberAndNickname() {
            RegularUser regularUser = new RegularUser("1234567890", "John");
            assertNotNull(regularUser);
            assertEquals("1234567890", regularUser.getPhoneNumber());
            assertEquals("John", regularUser.getNickname());

        }

        @Test
        public void testRegularUserConstructorWithPhoneNumber() {
            RegularUser regularUser = new RegularUser("1234567890");
            assertNotNull(regularUser);
            assertEquals("1234567890", regularUser.getPhoneNumber());
            assertNull(regularUser.getNickname());

        }


}

