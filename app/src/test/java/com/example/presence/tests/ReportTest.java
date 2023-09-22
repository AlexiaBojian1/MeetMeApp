package com.example.presence.tests;

import android.os.Parcel;

import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;

import com.example.presence.objects.Report;


public class ReportTest {

    @Test
    public void testGettersAndSetters() {
        // Create a new report
        Report report = new Report("id123","message123","report content","1234567890","0987654321");

        // Set the values using setters
        report.setId("id123");


        // Verify that the getters return the expected values
        assertEquals("id123", report.getId());
        assertEquals("message123", report.getMessageId());
        assertEquals("report content", report.getReportContent());
        assertEquals("1234567890", report.getReportedPhoneNumber());
        assertEquals("0987654321", report.getReporterPhoneNumber());
    }

    @Test
    public void test2GettersAndSetters() {
        // Create a new report
        Report report = new Report("id123","message123","report content","1234567890","");

        // Set the values using setters
        report.setId("id123");


        // Verify that the getters return the expected values
        assertEquals("id123", report.getId());
        assertEquals("message123", report.getMessageId());
        assertEquals("report content", report.getReportContent());
        assertEquals("1234567890", report.getReportedPhoneNumber());
        assertEquals("", report.getReporterPhoneNumber());
    }
    @Test
    public void test3GettersAndSetters() {
        // Create a new report
        Report report = new Report("id123","message123","report content","","0987654321");

        // Set the values using setters
        report.setId("id123");


        // Verify that the getters return the expected values
        assertEquals("id123", report.getId());
        assertEquals("message123", report.getMessageId());
        assertEquals("report content", report.getReportContent());
        assertEquals("", report.getReportedPhoneNumber());
        assertEquals("0987654321", report.getReporterPhoneNumber());
    }

    @Test
    public void test4GettersAndSetters() {
        // Create a new report
        Report report = new Report("id123","message123","","1234567890","0987654321");

        // Set the values using setters
        report.setId("id123");


        // Verify that the getters return the expected values
        assertEquals("id123", report.getId());
        assertEquals("message123", report.getMessageId());
        assertEquals("", report.getReportContent());
        assertEquals("1234567890", report.getReportedPhoneNumber());
        assertEquals("0987654321", report.getReporterPhoneNumber());
    }

    @Test
    public void test5GettersAndSetters() {
        // Create a new report
        Report report = new Report("id123","","report content","1234567890","0987654321");

        // Set the values using setters
        report.setId("id123");


        // Verify that the getters return the expected values
        assertEquals("id123", report.getId());
        assertEquals("", report.getMessageId());
        assertEquals("report content", report.getReportContent());
        assertEquals("1234567890", report.getReportedPhoneNumber());
        assertEquals("0987654321", report.getReporterPhoneNumber());
    }

    @Test
    public void test6GettersAndSetters() {
        // Create a new report
        Report report = new Report("","message123","report content","1234567890","0987654321");

        // Set the values using setters
        report.setId("");


        // Verify that the getters return the expected values
        assertEquals("", report.getId());
        assertEquals("message123", report.getMessageId());
        assertEquals("report content", report.getReportContent());
        assertEquals("1234567890", report.getReportedPhoneNumber());
        assertEquals("0987654321", report.getReporterPhoneNumber());
    }
}
