/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.Scheduler;

import org.springframework.stereotype.Component;

/**
 * Represents a patient's appointment schedule as a 2D matrix.
 *
 * <p>
 * The patient calendar uses the following semantics:</p>
 * <ul>
 * <li>1 = patient requests this time slot</li>
 * <li>0 = patient is available (no appointment needed)</li>
 * </ul>
 *
 * <p>
 * This component is automatically initialized with a predefined schedule upon
 * instantiation. The schedule can be retrieved or updated as needed.</p>
 *
 * @author guero
 */
@Component
public class PatientSchedule {

    /**
     * Patient calendar 2D array. Holds patient's availability.
     */
    private int[][] patientCalendar;

    /**
     * *
     * Constructor calls instantiatePatientCaledar method to instantiate the
     * patient's calendar.
     */
    public PatientSchedule() {
        instantiatePatientCaledar();
    }

    /**
     * Method to instantiate the patient's calendar.
     */
    public void instantiatePatientCaledar() {

        patientCalendar = new int[][]{{1, 0, 1, 0},
        {1, 0, 1, 0},
        {1, 0, 1, 0},
        {0, 1, 1, 0},
        {0, 1, 1, 0},
        {0, 0, 1, 0},
        {0, 0, 1, 0},
        {0, 0, 1, 0},
        {0, 0, 1, 0},
        {0, 0, 1, 1},
        {0, 0, 1, 1},
        {0, 0, 1, 1},
        {0, 0, 1, 1},
        {1, 0, 1, 0},
        {1, 0, 1, 0}
        };

    }

    /**
     * Getter to get the patient calendar.
     *
     * @return the patient's calendar.
     */
    public int[][] getPatientCalendar() {

        return this.patientCalendar;
    }

    /**
     * *
     * Method to update the Patient's schedule.
     *
     * @param updatedMatrix
     */
    public void updatePatientSchedule(int[][] updatedMatrix) {
        this.patientCalendar = updatedMatrix;

    }
}
