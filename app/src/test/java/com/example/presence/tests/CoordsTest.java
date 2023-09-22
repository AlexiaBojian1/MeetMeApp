package com.example.presence.tests;

import android.os.Parcel;

import com.example.presence.objects.Coords;

import org.junit.Test;


import static org.junit.Assert.assertEquals;

public class CoordsTest {

    @Test
    public void testConstructorAndGetters() {
        Coords coords = new Coords(12.345, -67.890);
        assertEquals(12.345, coords.getLatitude(), 0.0);
        assertEquals(-67.890, coords.getLongitude(), 0.0);
    }

    @Test
    public void test2ConstructorAndGetters() {
        Coords coords = new Coords(0.0, 0.0);
        assertEquals(0.0, coords.getLatitude(), 0.0);
        assertEquals(0.0, coords.getLongitude(), 0.0);
    }

    @Test
    public void test3ConstructorAndGetters() {
        Coords coords = new Coords(0.0, 0.0);
        assertEquals(0.1, coords.getLatitude(), 0.1);
        assertEquals(0.1, coords.getLongitude(), 0.1);
    }


    @Test
    public void testSetters() {
        Coords coords = new Coords(12.345, -67.890);
        coords.setLatitude(98.765);
        coords.setLongitude(-43.210);
        assertEquals(98.765, coords.getLatitude(), 0.0);
        assertEquals(-43.210, coords.getLongitude(), 0.0);
    }

    @Test
    public void test2Setters() {
        Coords coords = new Coords(0.0, -67.890);
        coords.setLatitude(0.0);
        coords.setLongitude(0.0);
        assertEquals(0.0, coords.getLatitude(), 0.0);
        assertEquals(0.0, coords.getLongitude(), 0.0);
    }

    @Test
    public void test3Setters() {
        Coords coords = new Coords(0.0, -67.890);
        coords.setLatitude(0.1);
        coords.setLongitude(0.1);
        assertEquals(0.0, coords.getLatitude(), 0.1);
        assertEquals(0.0, coords.getLongitude(), 0.1);
    }
}
