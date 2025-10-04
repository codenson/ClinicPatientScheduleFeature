package com.Scheduler;

import jakarta.annotation.PostConstruct;
import java.io.FileOutputStream;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.core.io.ClassPathResource;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import static org.apache.poi.ss.usermodel.CellType.BLANK;
import static org.apache.poi.ss.usermodel.CellType.BOOLEAN;
import static org.apache.poi.ss.usermodel.CellType.ERROR;
import static org.apache.poi.ss.usermodel.CellType.FORMULA;
import static org.apache.poi.ss.usermodel.CellType.NUMERIC;
import static org.apache.poi.ss.usermodel.CellType.STRING;

@Component
/**
 * ClinicCalendar represents the clinic's schedule as a 2D integer matrix
 * and provides operations to load the calendar, test whether a patient
 * schedule can be placed, compress patient matrices, and update the
 * calendar to reserve slots.
 *
 * <p>
 * Calendar semantics: 0 = available slot, 1 = occupied slot.
 * </p>
 *
 * <p>
 * In production this class is a Spring component and the calendar
 * can be initialized from an Excel file. In unit tests it is fine to
 * instantiate the class directly and call {@link #setUpcalendar(int[][])}.
 * </p>
 */
public class ClinicCalendar {

    /** The clinic calendar matrix. 0 = free, 1 = occupied. */
    private int[][] calendar = {{1}};
        private int[][] compressedPatientcalendar = {{1}};

    /** Default rows used when loading from Excel. */
    private final int rows = 37;

    /** Default columns used when loading from Excel. */
    private final int cols = 4;

    /** The start row found for the last successful match, or -1 if none. */
    private int foundStartRow = -1;

    /** Path to calendar file on the classpath, injected from properties. */
    @Value("${calendar.file.path}")
    private String filePath;

    @PostConstruct
    public void init() {
        System.out.println("Loading calendar from: " + filePath);

        try {
            ClassPathResource resource = new ClassPathResource(filePath);

            // Debug: Check if file exists
            // System.out.println("File exists: " + resource.exists());
            // System.out.println("File path: " + resource.getPath());

            try (InputStream is = resource.getInputStream()) {
                loadFromExcel(is);
                System.out.println("Excel loaded successfully!");
                // parseMatrixSum();
                printFirstValues();
            }
        } catch (IOException e) {
            // System.err.println("ERROR loading Excel from " + filePath + ": " +
            // e.getMessage());
            e.printStackTrace();
            calendar = new int[rows][cols];
        }
    }

    public void setUpcalendar(int[][] arr) {
        this.calendar = arr;

    }

    /**
     * Replace the in-memory clinic calendar with the provided matrix.
     *
     * @param arr the new calendar matrix (rows x cols). Caller is responsible
     *            for ensuring valid dimensions.
     */

    private void loadFromExcel(InputStream is) throws IOException {
        calendar = new int[rows][cols];

        try (Workbook workbook = new XSSFWorkbook(is)) {
            Sheet sheet = workbook.getSheetAt(0);
            for (int i = 0; i < rows; i++) {
                Row row = sheet.getRow(i);
                if (row == null)
                    continue;
                for (int j = 0; j < cols; j++) {
                    Cell cell = row.getCell(j);
                    if (cell == null) {
                        calendar[i][j] = 0;
                        continue;
                    }
                    if (cell.getCellType() == CellType.NUMERIC) {
                        calendar[i][j] = (int) cell.getNumericCellValue();
                    } else if (cell.getCellType() == CellType.STRING) {
                        try {
                            calendar[i][j] = Integer.parseInt(cell.getStringCellValue());
                        } catch (NumberFormatException e) {
                            calendar[i][j] = 0;
                        }
                    } else {
                        calendar[i][j] = 0;
                    }
                }
            }
        }
        // parseMatrixSum();
    }

    private String getCellValueAsString(Cell cell) {
        if (cell == null)
            return "null";

        switch (cell.getCellType()) {
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return "DATE: " + cell.getDateCellValue();
                }
                return String.valueOf(cell.getNumericCellValue());
            case STRING:
                return "'" + cell.getStringCellValue() + "'";
            case FORMULA:
                try {
                    return "FORMULA=" + cell.getNumericCellValue();
                } catch (Exception e) {
                    return "FORMULA_ERROR";
                }
            case BLANK:
                return "BLANK";
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case ERROR:
                return "ERROR";
            default:
                return "UNKNOWN";
        }
    }

    private int parseCellValue(Cell cell) {
        if (cell == null) {
            return 0;
        }

        switch (cell.getCellType()) {
            case NUMERIC:
                return (int) cell.getNumericCellValue();

            case STRING:
                String value = cell.getStringCellValue().trim();
                if (value.isEmpty()) {
                    return 0;
                }
                try {
                    return Integer.parseInt(value);
                } catch (NumberFormatException e) {
                    System.err.println("Cannot parse: '" + value + "'");
                    return 0;
                }

            case FORMULA:
                try {
                    return (int) cell.getNumericCellValue();
                } catch (Exception e) {
                    System.err.println("Formula error: " + e.getMessage());
                    return 0;
                }

            case BLANK:
                return 0;

            default:
                return 0;
        }
    }

    private void printFirstValues() {
        System.out.println("\nCalendar array (first 5 rows):");
        for (int i = 0; i < calendar.length; i++) {
            System.out.print("Row " + i + ": ");
            for (int j = 0; j < calendar[i].length; j++) {
                System.out.print(calendar[i][j] + "\t");
            }
            System.out.println();
        }
    }

    public int[][] getCalendar() {
        return calendar;
    }

    /**
     * Return the current clinic calendar matrix. Note: this returns a direct
     * reference to the internal array (no defensive copy).
     *
     * @return the calendar matrix
     */

    public void updateCell(int row, int col, int value) {
        if (calendar != null && row >= 0 && row < rows && col >= 0 && col < cols) {
            calendar[row][col] = value;
        }
    }

    /**
     * Set a single calendar cell to the given value. No action is taken if
     * the indices are out of bounds.
     *
     * @param row   0-based row index
     * @param col   0-based column index
     * @param value cell value (0 or 1)
     */

    public String calendarToString() {
        if (calendar == null)
            return "Calendar is empty";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < calendar.length; i++) {
            for (int j = 0; j < calendar[i].length; j++) {
                sb.append(String.format("%4d", calendar[i][j]));
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    /**
     * Method to schedule patient if patient calender doesn't collide  with the clinic's calender. 
     * It calls  patientScheduleMatch() to check schedule conflict doesn't exist. 
     * Global compressedPatientcalendar copies the compressed patient calender without first or last rows with all zeros. 
     *
     * @return Boolean if the patient was scheduled. 
     */

    public boolean patientreservation(int[][] patientCalendar) {

        patientScheduleMatch(patientCalendar);
        System.out.println("com.Scheduler.ClinicCalendar.patientreservation() :: *** " + foundStartRow);
        if (foundStartRow != -1) {
            //System.err
                    ///.println("************************table Updated *************************************************");
            updateCalendar(compressedPatientcalendar, foundStartRow);
            
            //System.err.println(this.calendar);

            foundStartRow = -1;
            ///this.printFirstValues();
            return true;
        }

        return false;

    }

    /**
     * Try to reserve slots for the provided patient calendar. If a match is
     * found the internal calendar is updated and true is returned.
     *
     * @param patientCalendar matrix describing requested slots (0/1)
     * @return true if reservation was made, false otherwise
     */

    /**
     * Helper method to create a compressed patient calendar without
     * leading/trailing zero rows
     */
    private int[][] compressPatientCalendar(int[][] patientCalendar) {
        int firstNonZeroRow = -1;
        int lastNonZeroRow = -1;

        // Find first row with at least one '1'
        for (int row = 0; row < patientCalendar.length; row++) {
            for (int col = 0; col < patientCalendar[row].length; col++) {
                if (patientCalendar[row][col] == 1) {
                    firstNonZeroRow = row;
                    break;
                }
            }
            if (firstNonZeroRow != -1)
                break;
        }

        // Find last row with at least one '1'
        for (int row = patientCalendar.length - 1; row >= 0; row--) {
            for (int col = 0; col < patientCalendar[row].length; col++) {
                if (patientCalendar[row][col] == 1) {
                    lastNonZeroRow = row;
                    break;
                }
            }
            if (lastNonZeroRow != -1)
                break;
        }

        // If no '1' found, return empty array or original
        if (firstNonZeroRow == -1 || lastNonZeroRow == -1) {
            return new int[0][patientCalendar[0].length];
        }

        // Create compressed array
        int compressedRows = lastNonZeroRow - firstNonZeroRow + 1;
        int[][] compressed = new int[compressedRows][patientCalendar[0].length];

        for (int i = 0; i < compressedRows; i++) {
            for (int j = 0; j < patientCalendar[0].length; j++) {
                compressed[i][j] = patientCalendar[firstNonZeroRow + i][j];
            }
        }
        compressedPatientcalendar = compressed; 

        ///System.out.println("DEBUG: Compressed patient calendar from " + patientCalendar.length +
              ///  " rows to " + compressedRows + " rows (rows " + firstNonZeroRow + "-" + lastNonZeroRow + ")");

        return compressed;
    }

    /**
     * Method to check and make a reservation for the patient
     * 
     * @param patientCalendar Matrix of patient schedule.
     * @return
     */
    public boolean patientScheduleMatch(int[][] patientCalendar) {
        // Compress the patient calendar first
        int[][] compressedPatient = compressPatientCalendar(patientCalendar);

        // If compressed calendar is empty, match anywhere
        if (compressedPatient.length == 0) {
            foundStartRow = 0;
            return true;
        }

        // Basic dimension check
        if (compressedPatient.length > calendar.length || compressedPatient[0].length > calendar[0].length) {
            System.out.println("DEBUG: Compressed patient calendar doesn't fit in clinic calendar");
            return false;
        }

        int patientRows = compressedPatient.length;
        int patientCols = compressedPatient[0].length;

     //   System.out.println("DEBUG: Clinic rows: " + calendar.length + ", Compressed patient rows: " + patientRows);
      //  System.out.println("DEBUG: Will check startRow from 0 to " + (calendar.length - patientRows));

        // Now search with the compressed calendar
        for (int startRow = 0; startRow <= calendar.length - patientRows; startRow++) {
            System.out.println("\nDEBUG: Checking startRow = " + startRow);

            boolean noCollision = true;

            for (int pRow = 0; pRow < patientRows && noCollision; pRow++) {
                for (int pCol = 0; pCol < patientCols && noCollision; pCol++) {
                    int cRow = startRow + pRow;
                    int cCol = pCol;
                    int clinicSlot = calendar[cRow][cCol];
                    int patientSlot = compressedPatient[pRow][pCol];

                    if (patientSlot == 1 && clinicSlot == 1) {
                        System.out.println("  DEBUG: COLLISION at pRow=" + pRow + ", pCol=" + pCol +
                                " (clinic[" + cRow + "][" + cCol + "]=1, patient=1)");
                        noCollision = false;
                    }
                }
            }

            if (noCollision) {
                System.out.println("  DEBUG: *** MATCH FOUND at startRow = " + startRow + " ***");
                foundStartRow = startRow;
                return true;
            }
        }

        System.out.println("\nDEBUG: No match found in entire calendar");
        return false;
    }

    /**
     * Find a place in the calendar where the patient's compressed schedule
     * can be placed without colliding with existing occupied slots.
     *
     * @param patientCalendar the patient's schedule matrix (0/1)
     * @return true if the patient can be scheduled, false otherwise
     */

    public int[][] updateCalendar(int[][] patientCalendar, int startRow) {
        System.out.println("Patient Calendar before Updates ");
        //this.printFirstValues();
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

                    // 2. Calculate the corresponding indices in the clinic calendar
                    int cRow = startRow + pRow;
                    int cCol = pCol;

                    // 3. Mark the clinic's slot as occupied (1)
                    // We assume startRow was valid and there's no collision,
                    // so we just overwrite the 0 with a 1.
                    // A safety check could ensure cRow and cCol are in bounds,
                    // but the successful check implies they are.
                    calendar[cRow][cCol] = 1;
                }
            }
        }
        this.printFirstValues();

        return calendar; // Return the modified calendar
    }

    public void UpdateMatrix(int[][] upadtedMatrix) {
        this.calendar = upadtedMatrix;

        System.err.println("Updated values  : ");
        printFirstValues();

    }

}
