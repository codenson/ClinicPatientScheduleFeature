/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.Scheduler;
import org.springframework.stereotype.Component;



/**
 *
 * @author guero
 */
@Component 
public class PatientSchedule {
    
    private int [][] patientCalendar ; 
    public PatientSchedule(){
        initilize2DArray(); 
    }
       public void initilize2DArray(){
           
                   patientCalendar = new int [][]
                   {{1,0,1,0},
                    {1,0,1,0}, 
                    {1,0,1,0}, 
                    {0,1,1,0}, 
                    {0,1,1,0}, 
                    {0,0,1,0}, 
                    {0,0,1,0}, 
                    {0,0,1,0}, 
                    {0,0,1,0}, 
                    {0,0,1,1},
                    {0,0,1,1},
                    {0,0,1,1},  
                    {0,0,1,1},
                    {1,0,1,0}, 
                    {1,0,1,0}
                    }; 

       }
       
       public int [][] getPatientCalendar(){
       
         return this.patientCalendar; 
       }
        public void parseMatrixSum(){
     //   System.err.println("Cal*********************** "+ patientCalendar.length + " calll : row : "+ patientCalendar[0].length );
        
//        for (int i  = 0; i < patientCalendar.length ; i++){
//            int sum = 0; 
//            for (int  j =  0; j < patientCalendar[i].length-1; j++){
//                
//                sum += patientCalendar[i][j];
//                System.err.println("sum : "+ sum );
//            
//            }
//            patientCalendar[i][patientCalendar[0].length-1]= sum; 
//        }
//        
//    
    
    }
        public void updatePatientSchedule(int [][] updatedMatrix){
            this.patientCalendar = updatedMatrix; 
        
        
        }
}
