package com.Scheduler;

import jakarta.annotation.PostConstruct;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.core.io.ClassPathResource;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import org.springframework.stereotype.Service;

@Service
/**
 * ClinicCalendar represents the clinic's schedule as a 2D integer matrix and
 * provides operations to load the calendar, test whether a patient schedule can
 * be placed, compress patient matrices, and update the calendar to reserve
 * slots.
 *
 * <p>
 * Calendar semantics: 0 = available slot, 1 = occupied slot.
 * </p>
 */
public class ClinicCalendar {

    private final ExcelParser excelParser;
    /**
     * The clinic calendar matrix. 0 = free, 1 = occupied.
     */
    private int[][] calendar;
    private int[][] compressedPatientcalendar ; 

    /**
     * The start row found for the last successful match, or -1 if none.
     */
    private int foundStartRow = -1;

    public ClinicCalendar(ExcelParser excelParser) {
        this.excelParser = excelParser;

    }

    @PostConstruct
    public void init() {
        calendar = excelParser.getParsedExcelTable();
    }

    public void setUpcalendar(int[][] arr) {
        this.calendar = arr;

    }

    /**
     * Method to print the 2D Clinic's calendar.
     */
    private void printMatrixValues() {
        System.out.println("\nCalendar array (rows):");
        for (int i = 0; i < calendar.length; i++) {
            System.out.print("Row " + i + ": ");
            for (int j = 0; j < calendar[i].length; j++) {
                System.out.print(calendar[i][j] + "\t");
            }
            System.out.println();
        }
    }


    /**
     * Calendar getter
     *
     * @return 2d array of the clinic's calendar
     */
    public int[][] getCalendar() {
        return calendar;
    }

    /**
     * Method to schedule patient if patient calender doesn't collide with the
     * clinic's calender. It calls patientScheduleMatch() to check schedule
     * conflict doesn't exist. Global compressedPatientcalendar copies the
     * compressed patient calender without first or last rows with all zeros.
     *
     * @return Boolean if the patient was scheduled.
     */
    public boolean patientreservation(int[][] patientCalendar) {

        patientScheduleMatch(patientCalendar);
        if (foundStartRow != -1) {
            updateCalendar(compressedPatientcalendar, foundStartRow);
            foundStartRow = -1;
            return true;
        }

        return false;

    }

    /**
     * Removes leading and trailing empty rows from the patient calendar.
     *
     * <p>
     * Returns a new array containing only the rows from the first appointment
     * to the last appointment (inclusive). If no appointments exist, returns an
     * empty array.</p>
     *
     * <p>
     * <b>Side Effect:</b> Updates {@code compressedPatientcalendar} field.</p>
     *
     * @param patientCalendar patient schedule (1 = appointment, 0 = empty)
     * @return compressed calendar with empty leading/trailing rows removed
     */
    protected int[][] compressPatientCalendar(int[][] patientCalendar) {
        int firstNonZeroRow = -1;
        int lastNonZeroRow = -1;
        int count = 0;

        for (int row = 0; row < patientCalendar.length; row++) {
            if (Arrays.toString(patientCalendar[row]).contains("1")) {
                if (firstNonZeroRow == -1) {
                    firstNonZeroRow = row;

                }
                lastNonZeroRow = row;
            }

        }
        int compressSize = 0;
        if (firstNonZeroRow == lastNonZeroRow) {

            if (firstNonZeroRow == -1 && lastNonZeroRow == -1) {
                return new int[0][0];
            }
            compressSize = 1;

        } else if (firstNonZeroRow == -1 && lastNonZeroRow == -1) {
            return new int[0][0];

        } else if (firstNonZeroRow >= 0 && lastNonZeroRow >= 0) {

            compressSize = lastNonZeroRow - firstNonZeroRow + 1;

        }

        int[][] compressed = new int[compressSize][patientCalendar[0].length];
        for (int row = 0; row < compressed.length; row++) {
            compressed[row] = patientCalendar[firstNonZeroRow++];

        }
        compressedPatientcalendar = compressed;
        return compressed;

    }

    /**
     * Finds the first available position in the clinic calendar where the
     * patient's schedule can be placed without conflicts.
     *
     * <p>
     * Compresses the patient calendar (removes empty rows) and searches
     * top-to-bottom for a valid placement. If found, sets
     * {@link #foundStartRow} to the starting position.</p>
     *
     * @param patientCalendar 2D array of original patient calendar.
     * @return true if a non-colliding placement exists, false otherwise
     */
    public boolean patientScheduleMatch(int[][] patientCalendar) {
        // Patient calendar Compressed first
        int[][] compressedPatient = compressPatientCalendar(patientCalendar);
        if (compressedPatient.length == 0) {
            foundStartRow = -1;
            return true;
        }

        for (int clinicRow = 0; clinicRow < this.calendar.length - compressedPatient.length + 1; clinicRow++) {
            boolean collision = false;
            for (int patientCalendarRow = 0; patientCalendarRow < compressedPatient.length && !collision; patientCalendarRow++) {
                for (int patientCol = 0; patientCol < compressedPatient[0].length && !collision; patientCol++) {

                    if (compressedPatient[patientCalendarRow][patientCol] == 1 && this.calendar[clinicRow + patientCalendarRow][patientCol] == 1) {
                        collision = true;
                    }

                }

            }
            if (!collision) {
                this.foundStartRow = clinicRow;
                return true;

            }
        }

        return false;
    }

    /**
     * Method to update/merge patient calendar with the clinic's.
     *
     * @param patientCalendar The updated compressed patient calendar.
     * @param startRow The first row the merge where the merge of patient's
     * calendar merges with the patient's.
     * @return The clinic's merged/updated calendar.
     */
    public int[][] updateCalendar(int[][] patientCalendar, int startRow) {
        int patientRows = patientCalendar.length;
        int patientCols = patientCalendar[0].length;

        /**
         * Apply an updated entire matrix to replace the clinic calendar.
         *
         * @param upadtedMatrix the new calendar matrix
         */
        // 1. Iterate through the patient's schedule
        for (int pRow = 0; pRow < patientRows; pRow++) {
            for (int pCol = 0; pCol < patientCols; pCol++) {

                // Check if the patient requested this slot (value is 1)
                if (patientCalendar[pRow][pCol] == 1) {
                    int cRow = startRow + pRow;
                    int cCol = pCol;
                    calendar[cRow][cCol] = 1;
                }
            }
        }

        return calendar; // Return the modified calendar
    }

    /**
     * Method used to update the matrix.
     *
     * @param upadtedMatrix
     */
    public void UpdateMatrix(int[][] upadtedMatrix) {
        this.calendar = upadtedMatrix;

    }

}
