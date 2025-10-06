package com.Scheduler;


import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Repository;

/**
 * Repository responsible for loading clinic calendar data from Excel files.
 * 
 * <p>Parses schedule data from a configured Excel file during application startup
 * and stores it in memory for retrieval by other components.</p>
 * 
 * @author guero
 */
@Repository
public class ExcelParser {
        /**
     * Default rows used when loading from Excel.
     */
        private final int rows = 37;

    /**
     * Default columns used when loading from Excel.
     */
    private final int cols = 4;
    /**
     * Array to parses clinic's Schedule to. 
    */
      private int[][] calendar;
      
         /**
     * Path to calendar file on the classpath, injected from properties.
     */
      @Value("${calendar.file.path}")
    private String filePath;
      
    /**
     * Loads calendar data from Excel file on startup.
     * Initializes empty calendar if loading fails.
     */
    @PostConstruct
    public void init() {
    

        try {
            ClassPathResource resource = new ClassPathResource(filePath);

            try (InputStream is = resource.getInputStream()) {
                loadFromExcel(is);
 
               // printMatrixValues();
            }
        } catch (IOException e) {

            e.printStackTrace();
            calendar = new int[rows][cols];
        }
    }
    
     /**
     * Replace the in-memory clinic calendar with the provided matrix.
     * @param arr the new calendar matrix (rows x cols). Caller is responsible
     * for ensuring valid dimensions.
     */
    private void loadFromExcel(InputStream is) throws IOException {
        calendar = new int[rows][cols];

        try (Workbook workbook = new XSSFWorkbook(is)) {
            Sheet sheet = workbook.getSheetAt(0);
            for (int i = 0; i < rows; i++) {
                Row row = sheet.getRow(i);
                if (row == null) {
                    continue;
                }
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
        
    }
    
    public int [][] getParsedExcelTable(){
    return this.calendar; 
    
    }
    
}
