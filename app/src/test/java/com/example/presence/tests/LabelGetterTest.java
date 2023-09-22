package com.example.presence.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import com.example.presence.helpers.LabelGetter;
import com.example.presence.objects.AppUser;
import com.example.presence.objects.Coords;
import com.example.presence.objects.RegularUser;
import com.example.presence.objects.TextMessage;

import org.junit.Test;


public class LabelGetterTest {

    @Test
    public void testGetLabelWithAppUser() {
        AppUser user;
        user = new RegularUser("1234","nickname");
        String expectedLabel = "appUser";
        String actualLabel = LabelGetter.getLabel(user);
        assertEquals(expectedLabel, actualLabel);
    }
    @Test
    public void testGetLabelWithMessage() {
        Coords coords = new Coords(12.345, -67.890);
        TextMessage message = new TextMessage("1234",coords,"1234","123",2143,1324,"2003-01-23", "Eiffel Tower");
        String expectedLabel = "message";
        String actualLabel = LabelGetter.getLabel(message);
        assertEquals(expectedLabel, actualLabel);
    }

    @Test
    public void testGetLabelWithInvalidInput() {
        Object invalidObject = new Object();
        assertThrows(IllegalArgumentException.class, () -> {
            LabelGetter.getLabel(invalidObject);
        });
    }
}
