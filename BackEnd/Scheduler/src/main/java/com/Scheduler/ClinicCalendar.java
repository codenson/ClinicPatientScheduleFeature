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
public class ClinicCalendar {

    private static int[][] calendar;
    private final int rows = 37;
    private final int cols = 4;
    static  int  foundStartRow = -1; 

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
    //    parseMatrixSum();
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

    public void updateCell(int row, int col, int value) {
        if (calendar != null && row >= 0 && row < rows && col >= 0 && col < cols) {
            calendar[row][col] = value;
        }
    }

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


    
    
    
    public  boolean  patientreservation(int[][] patientCalendar){
    
         patientScheduleMatch(patientCalendar); 
         System.out.println("com.Scheduler.ClinicCalendar.patientreservation() :: *** "+ foundStartRow);
         if (foundStartRow != -1){
             System.err.println("************************table Updated *************************************************");
             updateCalendar(patientCalendar,  foundStartRow); 
             
                 foundStartRow = -1;      
                 this.printFirstValues();
                 return true; 
         }

     return false; 
    
    }
    
/**
 * Helper method to create a compressed patient calendar without leading/trailing zero rows
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
        if (firstNonZeroRow != -1) break;
    }
    
    // Find last row with at least one '1'
    for (int row = patientCalendar.length - 1; row >= 0; row--) {
        for (int col = 0; col < patientCalendar[row].length; col++) {
            if (patientCalendar[row][col] == 1) {
                lastNonZeroRow = row;
                break;
            }
        }
        if (lastNonZeroRow != -1) break;
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
    
    System.out.println("DEBUG: Compressed patient calendar from " + patientCalendar.length + 
                      " rows to " + compressedRows + " rows (rows " + firstNonZeroRow + "-" + lastNonZeroRow + ")");
    
    return compressed;
}

/**
 * Method to check and make a reservation for the patient 
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
    
    System.out.println("DEBUG: Clinic rows: " + calendar.length + ", Compressed patient rows: " + patientRows);
    System.out.println("DEBUG: Will check startRow from 0 to " + (calendar.length - patientRows));
    
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
    
//   /**
//    * first attempt ;
// * Method to check and make a reservation for the patient 
// * @param patientCalendar Matrix of patient schedule. 
// * @return
// */
//public boolean patientScheduleMatch(int[][] patientCalendar) {
//    // 1. Basic Dimension Check: The patient calendar must fit within the clinic calendar
//    if (patientCalendar.length > calendar.length || patientCalendar[0].length > calendar[0].length) {
//        // Patient's schedule is larger than the clinic's calendar
//        return false;
//    }
//    int clinicRows = calendar.length;
//    int patientRows = patientCalendar.length;
//    int patientCols = patientCalendar[0].length;
//    
//    // Iterate through all possible starting row positions (i) in the clinic calendar
//    // The starting row 'i' can go up to: clinicRows - patientRows
//    for (int startRow = 0; startRow <= clinicRows - patientRows; startRow++) {
//        
//        // Assume this startRow is a valid fit (no collision) until proven otherwise
//        boolean noCollision = true; 
//        
//        // 2. Check for Collisions for the current 'startRow'
//        // Iterate through each cell of the patientCalendar
//        for (int pRow = 0; pRow < patientRows; pRow++) {
//            for (int pCol = 0; pCol < patientCols; pCol++) {
//                
//                // The corresponding clinic cell in the master calendar
//                int cRow = startRow + pRow;
//                int cCol = pCol;
//                int clinicSlot = calendar[cRow][cCol];
//                int patientSlot = patientCalendar[pRow][pCol];
//                
//                // Collision Check: Patient needs this slot (1) but clinic slot is occupied (1)
//                // If patient doesn't need the slot (0), we don't care about clinic's value
//                if (patientSlot == 1 && clinicSlot == 1) {
//                    noCollision = false; // Collision found!
//                    break; // Stop checking this patient row, move to the next 'startRow'
//                }
//            }
//            
//            if (!noCollision) {
//                break; // Collision found in this sub-region, move to the next 'startRow'
//            }
//        }
//        
//        // 3. Success Condition: If the inner loops finished without a collision
//        if (noCollision) {
//            foundStartRow = startRow; 
//            return true; // Found a starting position that works!
//        }
//    }
//    
//    return false; // No valid starting position found
//}
//    /** Original 
//     * Method to check and make a reservation for the patient 
//     * @param patientCalendar Matrix of patient schedule. 
//     * @return
//     */
//    public  boolean patientScheduleMatch(int[][] patientCalendar) {
//    // 1. Basic Dimension Check: The patient calendar must fit within the clinic calendar
//    if (patientCalendar.length > calendar.length || patientCalendar[0].length > calendar[0].length) {
//        // Patient's schedule is larger than the clinic's calendar
//        return false;
//    }
//
//    int clinicRows = calendar.length;
//    int patientRows = patientCalendar.length;
//    int patientCols = patientCalendar[0].length;
//    
//    // Assuming the columns (time slots/days) of the patient calendar match
//    // the first 'patientCols' of the clinic calendar.
//    // If clinic calendar has more columns, you might need an inner column loop.
//    // For simplicity, we assume column size is the same or the check is only on the first N columns.
//    
//    // Iterate through all possible starting row positions (i) in the clinic calendar
//    // The starting row 'i' can go up to: clinicRows - patientRows
//    for (int startRow = 0; startRow <= clinicRows - patientRows; startRow++) {
//        
//        // Assume this startRow is a valid fit (no collision) until proven otherwise
//        boolean noCollision = true; 
//        
//        // 2. Check for Collisions for the current 'startRow'
//        // Iterate through each cell of the patientCalendar
//        for (int pRow = 0; pRow < patientRows; pRow++) {
//            for (int pCol = 0; pCol < patientCols; pCol++) {
//                
//                // The corresponding clinic cell in the master calendar
//                int cRow = startRow + pRow;
//                int cCol = pCol; // Assuming patientCols == clinicCols, otherwise adjust
//
//                int clinicSlot = calendar[cRow][cCol];
//                int patientSlot = patientCalendar[pRow][pCol];
//
//                // Collision Check: Both have a '1' in the same slot
//                // 1 means occupied/needed, so 1 AND 1 is a clash.
//                if (clinicSlot == 1 && patientSlot == 1) {
//                    noCollision = false; // Collision found!
//                    break; // Stop checking this patient row, move to the next 'startRow'
//                }
//            }
//            
//            if (!noCollision) {
//                break; // Collision found in this sub-region, move to the next 'startRow'
//            }
//        }
//        
//        // 3. Success Condition: If the inner loops finished without a collision
//        if (noCollision) {
//             foundStartRow = startRow; 
//            return true; // Found a starting position that works!
//        }
//    }
//    
        
//
//    // 4. Failure Condition: Checked all possible starting rows and found a collision every time
//    return false;
//}
    
    public  int[][] updateCalendar(int[][] patientCalendar, int startRow) {
    int patientRows = patientCalendar.length;
    int patientCols = patientCalendar[0].length;

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

    return calendar; // Return the modified calendar
}


    
    public void UpdateMatrix(int [][]upadtedMatrix){
        this.calendar = upadtedMatrix; 
        
        System.err.println("Updated values  : ");
         printFirstValues(); 
    
    
    }
    
//    public void UpdateMatrix(int[][] updatedMatrix) {
//    this.calendar = updatedMatrix;
//
//    System.err.println("Updated values : ");
//    printFirstValues();
//
//    // Persist the updated matrix into the Excel file
//    try (Workbook workbook = new XSSFWorkbook()) {
//        Sheet sheet = workbook.createSheet("Schedule");
//
//        for (int i = 0; i < updatedMatrix.length; i++) {
//            Row row = sheet.createRow(i);
//            for (int j = 0; j < updatedMatrix[i].length; j++) {
//                Cell cell = row.createCell(j);
//                cell.setCellValue(updatedMatrix[i][j]);
//            }
//        }
//
//        // Save into the same file path you loaded from
//        try (FileOutputStream fos = new FileOutputStream(new ClassPathResource(filePath).getFile())) {
//            workbook.write(fos);
//        }
//
//        System.out.println("✅ Excel file updated successfully at " + filePath);
//    } catch (IOException e) {
//        e.printStackTrace();
//    }
//}


//public void UpdateMatrix(int[][] updatedMatrix) {
//    this.calendar = updatedMatrix;
//
//    System.err.println("Updated values  : ");
//    printFirstValues();
//
//    try (Workbook workbook = new XSSFWorkbook()) {
//        Sheet sheet = workbook.createSheet("Schedule");
//
//        for (int i = 0; i < updatedMatrix.length; i++) {
//            Row row = sheet.createRow(i);
//            for (int j = 0; j < updatedMatrix[i].length; j++) {
//                Cell cell = row.createCell(j);
//                cell.setCellValue(updatedMatrix[i][j]);
//            }
//        }
//
//        // ⚠️ IMPORTANT: ClassPathResource files are not writable inside a JAR
//        // Better: configure filePath to point to an external writable directory
//        try (FileOutputStream fos = new FileOutputStream(filePath)) {
//            workbook.write(fos);
//        }
//
//        System.out.println("✅ Excel file updated successfully: " + filePath);
//    } catch (Exception e) {
//        e.printStackTrace();
//    }
//}


}
