import React, { useEffect, useState } from "react";
import "./styles/Schedule.css";

function Schedule() {
  const [schedule, setSchedule] = useState([]);
  const [patientSchedule, setPatientSchedule] = useState([]);

  // Fetch both schedules on mount
  useEffect(() => {
    fetch("http://localhost:8080/schedule")
      .then((res) => res.json())
      .then((data) => setSchedule(data))
      .catch((err) => console.error(err));

    fetch("http://localhost:8080/patientCalendar")
      .then((res) => res.json())
      .then((data) => setPatientSchedule(data))
      .catch((err) => console.error(err));
  }, []);

  // Toggle cell for clinic or patient table
  const handleClick = (rowIndex, colIndex, isPatientTable = false) => {
    if (isPatientTable) {
      const newPatientSchedule = patientSchedule.map((row, i) =>
        row.map((cell, j) =>
          i === rowIndex && j === colIndex ? (cell === 0 ? 1 : 0) : cell
        )
      );
      setPatientSchedule(newPatientSchedule);
    } else {
      const newSchedule = schedule.map((row, i) =>
        row.map((cell, j) =>
          i === rowIndex && j === colIndex ? (cell === 0 ? 1 : 0) : cell
        )
      );
      setSchedule(newSchedule);
    }
  };

  const saveSchedule = async () => {
    try {
      // Saves both schedules in parallel
      const [res1, res2] = await Promise.all([
        fetch("http://localhost:8080/UpdateCalender", {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify(schedule),
        }),
        fetch("http://localhost:8080/updatePatientCalendar", {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify(patientSchedule),
        }),
      ]);

      if (!res1.ok || !res2.ok) {
        throw new Error("One or more requests failed.");
      }

      alert(`Schedule Saved`);
    } catch (err) {
      console.error(err);
      alert("Error saving schedules. Please try again.");
    }
  };

  // Schedule a patient (updates only clinic schedule)
  const schedulePatient = () => {
    fetch("http://localhost:8080/patientReservation")
      .then((res) => {
        if (!res.ok) {
          throw new Error("Failed to reach the server.");
        }
        return res.json();
      })
      .then((data) => {
        if (data === null) {
          alert("Patient wasn't scheduled. Please try again.");
        } else {
          setSchedule(data);
          alert("Patient scheduled successfully!");
        }
      })
      .catch((err) => {
        console.error(err);
        alert("An error occurred while scheduling the patient.");
      });
  };

  // Time slots for clinic table
  const generateTimeSlots = () => {
    const slots = [];
    let hour = 8;
    let minute = 0;
    while (hour < 17 || (hour === 17 && minute === 0)) {
      const ampm = hour >= 12 ? "PM" : "AM";
      const displayHour = hour > 12 ? hour - 12 : hour;
      const displayMinute = minute === 0 ? "00" : minute;
      slots.push(`${displayHour}:${displayMinute} ${ampm}`);
      minute += 15;
      if (minute === 60) {
        minute = 0;
        hour++;
      }
    }
    return slots;
  };

  const timeSlots = generateTimeSlots();

  // Get button class based on column and value
  const getButtonClass = (colIndex, value) => {
    if (value === 0) return "schedule-button empty";
    switch (colIndex) {
      case 0:
        return "schedule-button doctor";
      case 1:
        return "schedule-button nmt";
      case 2:
        return "schedule-button patient";
      case 3:
        return "schedule-button scan";
      default:
        return "schedule-button empty";
    }
  };

  return (
    <div className="schedule-container">
      <h2>Clinic & Patient Calendars</h2>
      <div className="calendars-wrapper">
        {/* Clinic Calendar */}
        <div className="calendar-section">
          <h3>Clinic Calendar</h3>
          <table className="schedule-table">
            <thead>
              <tr>
                <th>Time</th>
                <th>Doctor</th>
                <th>NMT</th>
                <th>Patient</th>
                <th>Scan</th>
              </tr>
            </thead>
            <tbody>
              {schedule.map((row, i) => (
                <tr key={i} style={{ animationDelay: `${i * 0.02}s` }}>
                  <td className="time-cell">{timeSlots[i]}</td>
                  {row.map((cell, j) => (
                    <td key={j}>
                      <button
                        onClick={() => handleClick(i, j)}
                        className={getButtonClass(j, cell)}
                      >
                        {cell === 1 ? "1" : ""}
                      </button>
                    </td>
                  ))}
                </tr>
              ))}
            </tbody>
          </table>
        </div>

        {/* Patient Calendar */}
        <div className="calendar-section">
          <h3>Patient Calendar</h3>
          <table className="schedule-table">
            <thead>
              <tr>
                <th>Doctor</th>
                <th>NMT</th>
                <th>Patient</th>
                <th>Scan</th>
              </tr>
            </thead>
            <tbody>
              {patientSchedule.map((row, i) => (
                <tr key={i} style={{ animationDelay: `${i * 0.02}s` }}>
                  {row.map((cell, j) => (
                    <td key={j}>
                      <button
                        onClick={() => handleClick(i, j, true)}
                        className={getButtonClass(j, cell)}
                      >
                        {cell === 1 ? "1" : ""}
                      </button>
                    </td>
                  ))}
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </div>

      <div className="action-buttons">
        <button className="action-btn" onClick={saveSchedule}>
          Save
        </button>
        <button className="action-btn" onClick={schedulePatient}>
          Book Patient
        </button>
      </div>
    </div>
  );
}

export default Schedule;
