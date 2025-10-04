/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.Scheduler;

import java.util.Arrays;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author guero
 */
@RestController
@RequestMapping("/")
@CrossOrigin(origins = "*")
public class AppController {
 
      private  final ClinicCalendar clinicCalender ; 
      private  final PatientSchedule patientSchedule ;
    
     public AppController(ClinicCalendar clinicCalender, PatientSchedule patientSchedule ) {
        this.clinicCalender = clinicCalender;
        this.patientSchedule = patientSchedule; 
       
    }
     
     public boolean reservation(){
         return clinicCalender.patientreservation(patientSchedule.getPatientCalendar()); 
         
     }
  

    
    @GetMapping("/")
    public String helloWorld(){
     
        System.err.println("Matrix : " + Arrays.deepToString(clinicCalender.getCalendar()));
        
       
      
        
        return reservation()+""; 
    
    
    }
    
    @GetMapping("/schedule")
    public int  [][] getSchedule(){
        System.err.println("table Fetched ");
    return clinicCalender.getCalendar(); 
    
    }
    
    @PostMapping("/UpdateCalender")
    public String updateSchedule(@RequestBody int[][] newSchedule) {
    System.out.println("Received new schedule: " + Arrays.deepToString(newSchedule));
    
    clinicCalender.UpdateMatrix(newSchedule);
    
    // TODO: update clinicCalender with newSchedule
    return "Schedule updated successfully!";
   }
    
    @GetMapping("/patientReservation")
    public int [][] setPatientReservation(){
        System.out.println("com.Scheduler.AppController.setPatientReservation()");
   boolean check  =  clinicCalender.patientreservation(patientSchedule.getPatientCalendar()); 
        System.out.println("com.Scheduler.AppController.setPatientReservation()   :  "  + check);
    
    return clinicCalender.getCalendar(); 
    
    }
    
    @GetMapping("patientCalendar")
    public int [][] getPatientCalendar(){
    
    return patientSchedule.getPatientCalendar(); 

    }
    
    @PostMapping("updatePatientCalendar")
     public String updatePatientSchedule(@RequestBody int[][] newSchedule) {
    //System.out.println("Received new schedule: " + Arrays.deepToString(newSchedule));
    
    patientSchedule.updatePatientSchedule(newSchedule);
    
    // TODO: update clinicCalender with newSchedule
    return "Schedule updated successfully!";
   }
    
    
            
    

}
