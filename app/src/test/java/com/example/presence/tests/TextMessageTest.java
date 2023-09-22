package com.example.presence.tests;

import com.example.presence.objects.Coords;
import com.example.presence.objects.TextMessage;
import com.google.firebase.firestore.DocumentSnapshot;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Calendar;

public class TextMessageTest {
    TextMessage testMessage;
    @Before
    public void setUp() {
        Calendar cal = Calendar.getInstance();
        String date = cal.get(Calendar.MONTH) + " " + cal.get(Calendar.DAY_OF_MONTH) + ", \n"
                + cal.get(Calendar.YEAR) + ", " + cal.get(Calendar.HOUR) + ":" + cal.get(Calendar.MINUTE);
        testMessage = new TextMessage("123456789", new Coords(42.3601, -71.0589), "Hello world!", "test-id", 5, 2, date, "Boston, MA");
    }


    @Test
    public void testGetText() {
        // Create a TextMessage with a known text
        String text = "Hello world!";
        TextMessage message = new TextMessage("1234567890", new Coords(0, 0), text, "id", 0, 0, "2022-04-20", "Home");

        // Check that the getText method returns the expected text
        assertEquals(text, message.getText());
    }

    @Test
    public void test2GetText() {
        // Create a TextMessage with a known text
        String text = " ";
        TextMessage message = new TextMessage("1234567890", new Coords(0, 0), text, "id", 0, 0, "2022-04-20", "Home");

        // Check that the getText method returns the expected text
        assertEquals(text, message.getText());
    }

    @Test
    public void test3GetText() {
        // Create a TextMessage with a known text
        String text = "message";
        TextMessage message = new TextMessage("", new Coords(0, 0), text, "id", 0, 0, "2022-04-20", "Home");

        // Check that the getText method returns the expected text
        assertEquals(text, message.getText());
    }

    @Test
    public void test4GetText() {
        // Create a TextMessage with a known text
        String text = "whitout id";
        TextMessage message = new TextMessage("1234567890", new Coords(0, 0), text, "", 0, 0, "2022-04-20", "Home");

        // Check that the getText method returns the expected text
        assertEquals(text, message.getText());
    }

    @Test
    public void test5GetText() {
        // Create a TextMessage with a known text
        String text = "uii";
        TextMessage message = new TextMessage("1234567890", new Coords(0, 0), text, "id", 10, 10, "2022-04-20", "Home");

        // Check that the getText method returns the expected text
        assertEquals(text, message.getText());
    }

    @Test
    public void test6GetText() {
        // Create a TextMessage with a known text
        String text = "Hello world!";
        TextMessage message = new TextMessage("", new Coords(0, 0), text, "", 0, 0, "", "");

        // Check that the getText method returns the expected text
        assertEquals(text, message.getText());
    }

    @Test
    public void testGetCoordinates() {
        Coords expected = new Coords(42.3601, -71.0589);
        Coords actual = testMessage.getCoordinates();
        assertEquals(expected.getLatitude(), actual.getLatitude(), 0.0);
        assertEquals(expected.getLongitude(), actual.getLongitude(), 0.0);
    }

    @Test
    public void testGetVotes() {
        int expected = 3;
        int actual = testMessage.getVotes();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetUpvotes() {
        int expected = 5;
        int actual = testMessage.getUpvotes();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetDownvotes() {
        int expected = 2;
        int actual = testMessage.getDownvotes();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetId() {
        String expected = "test-id";
        String actual = testMessage.getId();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetCreatorPhoneNumber() {
        String expected = "123456789";
        String actual = testMessage.getCreatorPhoneNumber();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetDate() {
        Calendar cal = Calendar.getInstance();
        String date = cal.get(Calendar.MONTH) + " " + cal.get(Calendar.DAY_OF_MONTH) + ", \n"
                + cal.get(Calendar.YEAR) + ", " + cal.get(Calendar.HOUR) + ":" + cal.get(Calendar.MINUTE);
        String expected = date;
        String actual = testMessage.getDate();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetLocationName() {
        String expected = "Boston, MA";
        String actual = testMessage.getLocationName();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetDistanceToCoordinates() {
        Coords coords = new Coords(42.3601, -71.0589);
        double expected = 0.0;
        double actual = testMessage.getDistanceToCoordinates(coords);
        assertEquals(expected, actual, 0.0);
    }


}
