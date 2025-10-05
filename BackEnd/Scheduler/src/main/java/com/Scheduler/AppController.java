package com.Scheduler;

import java.util.Arrays;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller providing endpoints for clinic scheduling operations.
 *
 * <p>
 * This controller exposes HTTP endpoints to manage both the clinic's master
 * calendar and individual patient schedules. It handles schedule retrieval,
 * updates, and patient appointment reservations.</p>
 *
 * <p>
 * All endpoints accept requests from any origin via CORS configuration.</p>
 *
 * @author guero
 */
@RestController
@RequestMapping("/")
@CrossOrigin(origins = "*")
public class AppController {

    /**
     * Manages the clinic's master schedule and appointment availability.
     */
    private final ClinicCalendar clinicCalender;
    /**
     * Holds the patient's requested appointment schedule.
     */
    private final PatientSchedule patientSchedule;

    /**
     * Constructs the controller with required scheduling dependencies.
     *
     * @param clinicCalender manages the clinic's master schedule
     * @param patientSchedule manages the patient's appointment requests
     */
    public AppController(ClinicCalendar clinicCalender, PatientSchedule patientSchedule) {
        this.clinicCalender = clinicCalender;
        this.patientSchedule = patientSchedule;

    }

    /**
     * Health check endpoint to verify server availability.
     *
     * @return confirmation message indicating server is running
     */
    @GetMapping("/")
    public String helloWorld() {

        return "Server is Alive";

    }

    /**
     * Retrieves the current clinic calendar.
     *
     * @return 2D array representing the clinic schedule (1 = occupied, 0 =
     * available)
     */
    @GetMapping("/schedule")
    public int[][] getSchedule() {
        System.out.println("table Fetched ");
        return clinicCalender.getCalendar();

    }

    /**
     * Updates the entire clinic calendar with a new schedule.
     *
     * @param newSchedule 2D array containing the updated clinic schedule
     * @return success confirmation message
     */
    @PostMapping("/UpdateCalender")
    public String updateSchedule(@RequestBody int[][] newSchedule) {

        clinicCalender.UpdateMatrix(newSchedule);

        return "Schedule updated successfully!";
    }

    /**
     * Attempts to reserve appointment slots for the patient in the clinic
     * calendar.
     *
     * <p>
     * If the patient's requested schedule can be accommodated without
     * conflicts, the clinic calendar is updated and returned. If no valid
     * placement exists, returns null.</p>
     *
     * @return updated clinic calendar if reservation succeeded, null otherwise
     */
    @GetMapping("/patientReservation")
    public int[][] setPatientReservation() {
        if (clinicCalender.patientreservation(patientSchedule.getPatientCalendar())) {
            return clinicCalender.getCalendar();

        }

        return null;

    }

    /**
     * Retrieves the current patient's schedule.
     *
     * @return 2D array representing patient's requested appointments (1 =
     * requested, 0 = available)
     */
    @GetMapping("patientCalendar")
    public int[][] getPatientCalendar() {

        return patientSchedule.getPatientCalendar();

    }

    /**
     * Updates the patient's schedule with new appointment requests.
     *
     * @param newSchedule 2D array containing the patient's updated schedule
     * @return success confirmation message
     */
    @PostMapping("updatePatientCalendar")
    public String updatePatientSchedule(@RequestBody int[][] newSchedule) {;

        patientSchedule.updatePatientSchedule(newSchedule);

        return "Schedule updated successfully!";
    }

}
