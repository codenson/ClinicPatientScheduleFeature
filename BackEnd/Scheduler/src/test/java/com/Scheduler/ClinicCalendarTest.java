/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package com.Scheduler;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
// No Spring testing annotations/imports needed for this unit test
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

/**
 *
 * @author guero
 */
// @SpringBootTest
// @ExtendWith(SpringExtension.class)
public class ClinicCalendarTest {

    // private ClinicCalendar clinicCalendar;

    @Mock
    private ExcelParser excelParser;
    private ClinicCalendar clinicCalendar;

    /// @Autowired
    // private PatientSchedule patientSchedule;

    public ClinicCalendarTest() {

    }

    @BeforeAll
    public static void setUpClass() {
    }

    @AfterAll
    public static void tearDownClass() {
    }

    @BeforeEach
    public void setUp() {
        // Create a plain instance for unit tests (no Spring context).
        // ClinicCalendar is a Spring component in production, but here
        // we instantiate it directly so @Autowired/@PostConstruct are not required.
        // clinicCalendar = new ClinicCalendar();

        MockitoAnnotations.openMocks(this);
        clinicCalendar = new ClinicCalendar(excelParser);
    }

    @AfterEach
    public void tearDown() {
    }

    @Test
    public void testAddAppointment() {
        int[][] clinic = { { 0 }, { 1 } };
        int[][] patient = { { 1 }, { 0 } };
        clinicCalendar.setUpcalendar(clinic);
        assertTrue(clinicCalendar.patientScheduleMatch(patient));

        int[][] clinic1 = { { 0, 0, 0, 0 }, { 1, 0, 0, 1 } };
        int[][] patient1 = { { 1, 1, 1, 1 } };
        clinicCalendar.setUpcalendar(clinic1);
        assertTrue(clinicCalendar.patientScheduleMatch(patient1));

        int[][] clinic2 = { { 1, 1, 1, 1 }, { 1, 0, 0, 1 } };
        int[][] patient2 = { { 1, 1, 1, 1 } };
        clinicCalendar.setUpcalendar(clinic2);
        assertFalse(clinicCalendar.patientScheduleMatch(patient2));

        int[][] clinic3 = { { 1, 1, 1, 1 },
                { 1, 0, 0, 1 },
                { 1, 0, 0, 1 },
                { 1, 0, 0, 1 },
                { 1, 0, 0, 1 },
                { 1, 1, 1, 1 },
                { 0, 0, 0, 0 },
                { 0, 0, 0, 0 } };
        int[][] patient3 = {
                { 0, 0, 0, 0 },
                { 0, 0, 0, 0 },
                { 0, 0, 0, 0 },
                { 1, 0, 0, 0 },
                { 0, 0, 0, 0 }
        };
        clinicCalendar.setUpcalendar(clinic3);
        assertTrue(clinicCalendar.patientScheduleMatch(patient3));

        int[][] clinic4 = { { 1, 1, 1, 1 },
                { 1, 0, 0, 1 },
                { 1, 0, 0, 1 },
                { 1, 0, 0, 1 },
                { 1, 0, 0, 1 },
                { 1, 1, 1, 1 },
                { 0, 0, 0, 0 },
                { 0, 0, 0, 0 },
                { 0, 0, 0, 0 },
                { 0, 0, 0, 0 },
                { 0, 0, 0, 0 }, };
        int[][] patient4 = {
                { 0, 0, 0, 0 },
                { 0, 0, 0, 0 },
                { 0, 0, 0, 0 },
                { 0, 0, 0, 0 },
                { 1, 0, 1, 0 },
                { 0, 0, 0, 1 }
        };
        clinicCalendar.setUpcalendar(clinic4);
        assertTrue(clinicCalendar.patientScheduleMatch(patient4));

        int[][] clinic5 = { { 1, 1, 1, 1 },
                { 1, 0, 0, 1 },
                { 1, 0, 0, 1 },
                { 1, 0, 0, 1 },
                { 1, 0, 0, 1 },
                { 1, 1, 1, 1 },
                { 0, 0, 0, 0 },
                { 0, 0, 0, 0 },
                { 0, 0, 0, 0 },
                { 0, 0, 0, 0 },
                { 0, 0, 0, 0 }, };
        int[][] patient5 = {
                { 0, 0, 0, 0 },
                { 0, 0, 0, 0 },
                { 0, 0, 0, 0 },
                { 0, 0, 0, 0 },
                { 0, 0, 0, 0 },
                { 0, 0, 0, 0 },
                { 0, 0, 0, 0 },
                { 1, 0, 0, 1 }
        };
        clinicCalendar.setUpcalendar(clinic5);
        assertTrue(clinicCalendar.patientScheduleMatch(patient5));

        int[][] clinic6 = { { 1 }, { 1 } };
        int[][] patient6 = { { 1 }, { 1 } };
        clinicCalendar.setUpcalendar(clinic6);
        assertFalse(clinicCalendar.patientScheduleMatch(patient6));

        int[][] clinic7 = { { 0 }, { 0 } };
        int[][] patient7 = { { 1 }, { 1 } };
        clinicCalendar.setUpcalendar(clinic7);
        assertTrue(clinicCalendar.patientScheduleMatch(patient7));

        int[][] clinic8 = { { 0 }, { 0 } };
        int[][] patient8 = { { 0 }, { 0 } };
        clinicCalendar.setUpcalendar(clinic8);
        assertTrue(clinicCalendar.patientScheduleMatch(patient8));

    }

    @Test
    public void testAddAppointmentSpecialCase() {

        int[][] clinic9 = {
                { 1, 0, 0, 1 }, // row 0
                { 1, 0, 0, 1 }, // row 1
                { 1, 0, 0, 1 }, // row 2
                { 1, 0, 0, 1 }, // row 3
                { 1, 1, 1, 1 }, // row 4
                { 1, 0, 0, 0 }, // row 5
                { 1, 0, 0, 0 }, // row 6
                { 1, 0, 0, 0 }, // rpw 7
                { 1, 0, 0, 0 }, // row 8
                { 1, 0, 0, 0 }, // row 9
                { 1, 0, 0, 0 }, // row 10
                { 1, 0, 0, 1 }, // row 11
                { 1, 0, 0, 1 }, // row 12
                { 1, 0, 0, 1 }, // row 13
                { 1, 0, 0, 1 }, // row 14
                { 1, 1, 1, 1 }, // row 15
                { 1, 0, 0, 0 }, // row 16
                { 1, 0, 0, 0 }, // row 17
                { 1, 0, 0, 0 }, // row 18
                { 1, 0, 0, 0 }, // row 19
                { 1, 0, 0, 0 }, // row20
                { 1, 0, 0, 1 }, // row 21
                { 1, 0, 0, 1 }, // row 22
                { 1, 0, 0, 1 }, // row 23
                { 1, 0, 0, 1 }, // row 24
                { 1, 1, 1, 1 }, // row 25
                { 1, 0, 0, 0 }, // row 26
                { 1, 1, 1, 1 }, // row 27
                { 0, 0, 0, 0 }, // row 28
                { 0, 0, 0, 0 }, // row 29
                { 0, 0, 0, 0 }, // row30
                { 0, 0, 0, 0 }, // row31
                { 0, 0, 0, 0 }, // row32
                { 0, 0, 0, 0 }, // row33
                { 0, 0, 0, 0 }, // row34
                { 0, 0, 0, 0 }, // row35
                { 0, 0, 0, 0 }, // row36
                { 1, 1, 1, 1 } // row37

        };
        int[][] patient9 = {
                { 0, 0, 0, 0 },
                { 0, 0, 0, 0 },
                { 0, 0, 0, 0 },
                { 0, 0, 0, 0 },
                { 0, 0, 0, 0 },
                { 0, 0, 0, 0 },
                { 0, 0, 0, 0 },
                { 0, 0, 0, 0 },
                { 0, 0, 0, 0 },
                { 0, 0, 0, 0 },
                { 1, 0, 0, 0 },
                { 1, 0, 0, 0 },
                { 1, 0, 0, 0 },
                { 0, 0, 0, 0 },
                { 0, 0, 0, 0 }
        };

        clinicCalendar.setUpcalendar(clinic9);
        assertTrue(clinicCalendar.patientScheduleMatch(patient9));
        // clinicCalendar.updateCalendar(patient9, 0);
        clinicCalendar.patientreservation(patient9);

    }

    @Test
    public void testCompressedMatrix() {
        int[][] clinic9 = { { 1, 1, 1, 1 },
                { 1, 0, 0, 1 }, // row 0
                { 1, 0, 0, 1 }, // row 1
                { 1, 0, 0, 1 }, // row 2
                { 1, 0, 0, 1 }, // row 3
                { 1, 1, 1, 1 }, // row 4
                { 1, 0, 0, 0 }, // row 5
                { 1, 0, 0, 0 }, // row 6
                { 1, 0, 0, 0 }, // rpw 7
                { 1, 0, 0, 0 }, // row 8
                { 1, 0, 0, 0 }, // row 9
                { 1, 0, 0, 0 }, // row 10
                { 1, 0, 0, 1 }, // row 11
                { 1, 0, 0, 1 }, // row 12
                { 1, 0, 0, 1 }, // row 13
                { 1, 0, 0, 1 }, // row 14
                { 1, 1, 1, 1 }, // row 15
                { 1, 0, 0, 0 }, // row 16
                { 1, 0, 0, 0 }, // row 17
                { 1, 0, 0, 0 }, // row 18
                { 1, 0, 0, 0 }, // row 19
                { 1, 0, 0, 0 }, // row20
                { 1, 0, 0, 1 }, // row 21
                { 1, 0, 0, 1 }, // row 22
                { 1, 0, 0, 1 }, // row 23
                { 1, 0, 0, 1 }, // row 24
                { 1, 1, 1, 1 }, // row 25
                { 1, 0, 0, 0 }, // row 26
                { 1, 1, 1, 1 }, // row 27
                { 0, 0, 0, 0 }, // row 28
                { 0, 0, 0, 0 }, // row 29
                { 0, 0, 0, 0 }, // row30
                { 0, 0, 0, 0 }, // row31
                { 0, 0, 0, 0 }, // row32
                { 0, 0, 0, 0 }, // row33
                { 0, 0, 0, 0 }, // row34
                { 0, 0, 0, 0 }, // row35
                { 0, 0, 0, 0 },// row36

        };
        int[][] patient9 = {
                { 0, 0, 0, 0 },
                { 0, 0, 0, 0 },
                { 0, 0, 0, 0 },
                { 0, 0, 0, 0 },
                { 0, 0, 0, 0 },
                { 0, 0, 0, 0 },
                { 0, 0, 0, 0 },
                { 0, 0, 0, 0 },
                { 0, 0, 0, 0 },
                { 0, 0, 0, 0 },
                { 1, 0, 0, 0 },
                { 1, 0, 0, 0 },
                { 1, 0, 0, 0 },
                { 0, 0, 0, 0 },
                { 0, 0, 0, 0 }
        };
        clinicCalendar.compressPatientCalendar(patient9);
        // clinicCalendar.setUpcalendar(clinic1);

    }

    @Test
    public void testCompression() {
        int[][] clinic = {
                { 0, 0 },
                { 0, 0 },
                { 1, 0 },
                { 0, 0 },
                { 0, 0 }
        };

        int[][] sol1 = {
                { 1, 0 } };

        assertArrayEquals(sol1, clinicCalendar.compressPatientCalendar(clinic));

        int[][] clinic2 = {
                { 1, 0 },
                { 0, 0 },
                { 0, 0 },
                { 0, 0 },
                { 0, 0 }
        };

        int[][] sol2 = {
                { 1, 0 } };

        assertArrayEquals(sol2, clinicCalendar.compressPatientCalendar(clinic2));

        int[][] clinic3 = {
                { 1, 0 },
                { 0, 1 },
                { 0, 0 },
                { 0, 0 },
                { 0, 0 }
        };
        int[][] sol3 = {
                { 1, 0 },
                { 0, 1 } };

        assertArrayEquals(sol3, clinicCalendar.compressPatientCalendar(clinic3));

        int[][] clinic4 = {
                { 1, 0, 0, 1 },
                { 0, 1, 0, 0 },
                { 0, 0, 0, 0 },
                { 0, 0 },
                { 1, 0 }
        };
        int[][] sol4 = {
                { 1, 0, 0, 1 },
                { 0, 1, 0, 0 },
                { 0, 0, 0, 0 },
                { 0, 0 },
                { 1, 0 } };
        assertArrayEquals(sol4, clinicCalendar.compressPatientCalendar(clinic4));
        int[][] clinic5 = {
                { 0, 0, 0, 0 },
                { 0, 0, 0, 0 },
                { 0, 0, 0, 0 },
                { 1, 0 },
                { 1, 0 }
        };
        int[][] sol5 = {
                { 1, 0 },
                { 1, 0 } };
        assertArrayEquals(sol5, clinicCalendar.compressPatientCalendar(clinic5));

        int[][] clinic6 = {
                { 0, 0, 0, 0 },
                { 0, 0, 0, 0 },
                { 0, 0, 0, 0 },
                { 0, 0 },
                { 1, 0 }
        };
        assertArrayEquals(sol1, clinicCalendar.compressPatientCalendar(clinic6));

        int[][] clinic7 = {
                { 0, 0, 0, 0 },
                { 0, 0, 0, 0 },
                { 0, 0, 0, 0 },
                { 0, 0 },
                { 0, 0 }
        };
        int[][] sol7 = {};

        assertArrayEquals(sol7, clinicCalendar.compressPatientCalendar(clinic7));

    }

    @Test
    public void testPatientReservation() {
        int[][] clinic1 = {
                { 0, 0, 0, 0 },
                { 0, 0, 0, 0 },
                { 0, 0, 0, 0 },
                { 0, 0, 0, 0 },
                { 0, 0, 0, 0 },
        };
        int[][] patient1 = {
                { 0, 0, 0, 0 },
                { 0, 0, 0, 0 },

        };
        clinicCalendar.UpdateMatrix(clinic1);

        assertFalse(clinicCalendar.patientreservation(patient1));

        int[][] clinic2 = {
                { 1, 1, 1, 1 },
                { 1, 1, 1, 1 },
                { 1, 1, 1, 1 },
                { 1, 1, 1, 1 }

        };
        int[][] patient2 = {
                { 1, 1, 1, 1 },
                { 1, 1, 1, 1 }

        };
        clinicCalendar.UpdateMatrix(clinic2);

        assertFalse(clinicCalendar.patientreservation(patient2));

    }
}
