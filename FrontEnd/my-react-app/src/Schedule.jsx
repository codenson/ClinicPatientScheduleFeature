// // import React, { useEffect, useState } from "react";

// // function Schedule() {
// //   const [schedule, setSchedule] = useState([]);

// //   useEffect(() => {
// //     fetch("http://localhost:8080/schedule")
// //       .then((res) => res.json())
// //       .then((data) => setSchedule(data))
// //       .catch((err) => console.error(err));
// //   }, []);

// //   const handleClick = (rowIndex, colIndex) => {
// //     const newSchedule = schedule.map((row, i) =>
// //       row.map((cell, j) =>
// //         i === rowIndex && j === colIndex ? (cell === 0 ? 1 : 0) : cell
// //       )
// //     );
// //     setSchedule(newSchedule);
// //   };

// //   const saveSchedule = () => {
// //     fetch("http://localhost:8080/UpdateCalender", {
// //       method: "POST",
// //       headers: { "Content-Type": "application/json" },
// //       body: JSON.stringify(schedule),
// //     })
// //       .then((res) => res.text())
// //       .then((msg) => alert(msg))
// //       .catch((err) => console.error(err));
// //   };

// //   const schedulePatient = () => {
// //     fetch("http://localhost:8080/patientReservation")
// //       .then((res) => res.json()) // backend should return the updated schedule array
// //       .then((updatedSchedule) => {
// //         setSchedule(updatedSchedule); // update the table immediately
// //         alert("Patient scheduled successfully!");
// //       })
// //       .catch((err) => console.error(err));
// //   };

// //   const generateTimeSlots = () => {
// //     const slots = [];
// //     let hour = 8;
// //     let minute = 0;
// //     while (hour < 17 || (hour === 17 && minute === 0)) {
// //       const ampm = hour >= 12 ? "PM" : "AM";
// //       const displayHour = hour > 12 ? hour - 12 : hour;
// //       const displayMinute = minute === 0 ? "00" : minute;
// //       slots.push(`${displayHour}:${displayMinute} ${ampm}`);
// //       minute += 15;
// //       if (minute === 60) {
// //         minute = 0;
// //         hour++;
// //       }
// //     }
// //     return slots;
// //   };

// //   const timeSlots = generateTimeSlots();

// //   // function to assign background colors
// //   const getCellStyle = (colIndex, value) => {
// //     if (value === 0) {
// //       return { backgroundColor: "white" };
// //     }
// //     switch (colIndex) {
// //       case 0: // Doctor
// //         return { backgroundColor: "#e74c3c", color: "white" }; // red
// //       case 1: // NMT
// //         return { backgroundColor: "#3498db", color: "white" }; // blue
// //       case 2: // Patient
// //         return { backgroundColor: "#f1c40f" }; // yellow
// //       case 3: // Scan
// //         return { backgroundColor: "#27ae60", color: "white" }; // green
// //       default:
// //         return { backgroundColor: "white" };
// //     }
// //   };

// //   return (
// //     <div>
// //       <h2>Schedule</h2>
// //       <table border="1" cellPadding="6">
// //         <thead>
// //           <tr>
// //             <th>Time</th>
// //             <th>Doctor</th>
// //             <th>NMT</th>
// //             <th>Patient</th>
// //             <th>Scan</th>
// //           </tr>
// //         </thead>
// //         <tbody>
// //           {schedule.map((row, i) => (
// //             <tr key={i}>
// //               <td>{timeSlots[i]}</td>
// //               {row.map((cell, j) => (
// //                 <td key={j}>
// //                   <button
// //                     onClick={() => handleClick(i, j)}
// //                     style={{
// //                       width: "100%",
// //                       height: "100%",
// //                       border: "none",
// //                       cursor: "pointer",
// //                       ...getCellStyle(j, cell),
// //                     }}
// //                   >
// //                     {cell === 1 ? "1" : ""}
// //                   </button>
// //                 </td>
// //               ))}
// //             </tr>
// //           ))}
// //         </tbody>
// //       </table>

// //       <br />
// //       <button onClick={saveSchedule}>Save Schedule</button>
// //       <br />
// //       <button onClick={schedulePatient}>Schedule a patient </button>
// //     </div>
// //   );
// // }

// // export default Schedule;

// import React, { useEffect, useState } from "react";

// function Schedule() {
//   const [schedule, setSchedule] = useState([]);
//   const [patientSchedule, setPatientSchedule] = useState([]); // NEW

//   useEffect(() => {
//     fetch("http://localhost:8080/schedule")
//       .then((res) => res.json())
//       .then((data) => setSchedule(data))
//       .catch((err) => console.error(err));

//     // fetch patient calendar (could be different endpoint)
//     fetch("http://localhost:8080/patientCalendar")
//       .then((res) => res.json())
//       .then((data) => setPatientSchedule(data))
//       .catch((err) => console.error(err));
//   }, []);

//   const handleClick = (rowIndex, colIndex) => {
//     const newSchedule = schedule.map((row, i) =>
//       row.map((cell, j) =>
//         i === rowIndex && j === colIndex ? (cell === 0 ? 1 : 0) : cell
//       )
//     );
//     setSchedule(newSchedule);
//   };

//   const saveSchedule = () => {
//     fetch("http://localhost:8080/UpdateCalender", {
//       method: "POST",
//       headers: { "Content-Type": "application/json" },
//       body: JSON.stringify(schedule),
//     })
//       .then((res) => res.text())
//       .then((msg) => alert(msg))
//       .catch((err) => console.error(err));
//   };

//   const schedulePatient = () => {
//     fetch("http://localhost:8080/patientReservation")
//       .then((res) => res.json())
//       .then((updatedSchedule) => {
//         setSchedule(updatedSchedule);
//         setPatientSchedule(updatedSchedule); // sync both if backend returns merged data
//         alert("Patient scheduled successfully!");
//       })
//       .catch((err) => console.error(err));
//   };

//   const generateTimeSlots = () => {
//     const slots = [];
//     let hour = 8;
//     let minute = 0;
//     while (hour < 17 || (hour === 17 && minute === 0)) {
//       const ampm = hour >= 12 ? "PM" : "AM";
//       const displayHour = hour > 12 ? hour - 12 : hour;
//       const displayMinute = minute === 0 ? "00" : minute;
//       slots.push(`${displayHour}:${displayMinute} ${ampm}`);
//       minute += 15;
//       if (minute === 60) {
//         minute = 0;
//         hour++;
//       }
//     }
//     return slots;
//   };

//   const timeSlots = generateTimeSlots();

//   const getCellStyle = (colIndex, value) => {
//     if (value === 0) {
//       return { backgroundColor: "white" };
//     }
//     switch (colIndex) {
//       case 0:
//         return { backgroundColor: "#e74c3c", color: "white" }; // Doctor = red
//       case 1:
//         return { backgroundColor: "#3498db", color: "white" }; // NMT = blue
//       case 2:
//         return { backgroundColor: "#f1c40f" }; // Patient = yellow
//       case 3:
//         return { backgroundColor: "#27ae60", color: "white" }; // Scan = green
//       default:
//         return { backgroundColor: "white" };
//     }
//   };

//   return (
//     <div>
//       <h2>Clinic & Patient Calendars</h2>

//       <div style={{ display: "flex", gap: "40px" }}>
//         {/* Clinic Calendar */}
//         <div>
//           <h3>Clinic Calendar</h3>
//           <table border="1" cellPadding="6">
//             <thead>
//               <tr>
//                 <th>Time</th>
//                 <th>Doctor</th>
//                 <th>NMT</th>
//                 <th>Patient</th>
//                 <th>Scan</th>
//               </tr>
//             </thead>
//             <tbody>
//               {schedule.map((row, i) => (
//                 <tr key={i}>
//                   <td>{timeSlots[i]}</td>
//                   {row.map((cell, j) => (
//                     <td key={j}>
//                       <button
//                         onClick={() => handleClick(i, j)}
//                         style={{
//                           width: "100%",
//                           height: "100%",
//                           border: "none",
//                           cursor: "pointer",
//                           ...getCellStyle(j, cell),
//                         }}
//                       >
//                         {cell === 1 ? "1" : ""}
//                       </button>
//                     </td>
//                   ))}
//                 </tr>
//               ))}
//             </tbody>
//           </table>
//         </div>

//         {/* Patient Calendar */}
//         <div>
//           <h3>Patient Calendar</h3>
//           <table border="1" cellPadding="6">
//             <thead>
//               <tr>
//                 <th>Doctor</th>
//                 <th>NMT</th>
//                 <th>Patient</th>
//                 <th>Scan</th>
//               </tr>
//             </thead>
//             <tbody>
//               {patientSchedule.map((row, i) => (
//                 <tr key={i}>
//                   {row.map((cell, j) => (
//                     <td key={j}>
//                       <div
//                         style={{
//                           width: "100%",
//                           height: "40px",
//                           border: "none",
//                           ...getCellStyle(j, cell),
//                         }}
//                       />
//                     </td>
//                   ))}
//                 </tr>
//               ))}
//             </tbody>
//           </table>
//         </div>
//       </div>

//       <br />
//       <button onClick={saveSchedule}>Save Schedule</button>
//       <br />
//       <button onClick={schedulePatient}>Schedule a Patient</button>
//     </div>
//   );
// }

// export default Schedule;

// import React, { useEffect, useState } from "react";

// function Schedule() {
//   const [schedule, setSchedule] = useState([]);
//   const [patientSchedule, setPatientSchedule] = useState([]);

//   useEffect(() => {
//     fetch("http://localhost:8080/schedule")
//       .then((res) => res.json())
//       .then((data) => setSchedule(data))
//       .catch((err) => console.error(err));

//     fetch("http://localhost:8080/patientCalendar")
//       .then((res) => res.json())
//       .then((data) => setPatientSchedule(data))
//       .catch((err) => console.error(err));
//   }, []);

//   const handleClick = (rowIndex, colIndex, isPatientTable = false) => {
//     if (isPatientTable) {
//       const newPatientSchedule = patientSchedule.map((row, i) =>
//         row.map((cell, j) =>
//           i === rowIndex && j === colIndex ? (cell === 0 ? 1 : 0) : cell
//         )
//       );
//       setPatientSchedule(newPatientSchedule);
//     } else {
//       const newSchedule = schedule.map((row, i) =>
//         row.map((cell, j) =>
//           i === rowIndex && j === colIndex ? (cell === 0 ? 1 : 0) : cell
//         )
//       );
//       setSchedule(newSchedule);
//     }
//   };

//   const saveSchedule = () => {
//     fetch("http://localhost:8080/UpdateCalender", {
//       method: "POST",
//       headers: { "Content-Type": "application/json" },
//       body: JSON.stringify(schedule),
//     })
//       .then((res) => res.text())
//       .then((msg) => alert(msg))
//       .catch((err) => console.error(err));
//   };
//   // const savePatientSchedule = () => {
//   //   fetch("http://localhost:8080/updatePatientCalendar", {
//   //     method: "POST",
//   //     headers: { "Content-Type": "application/json" },
//   //     body: JSON.stringify(schedule),
//   //   })
//   //     .then((res) => res.text())
//   //     .then((msg) => alert(msg))
//   //     .catch((err) => console.error(err));
//   // };
//   const savePatientSchedule = () => {
//     fetch("http://localhost:8080/updatePatientCalendar", {
//       method: "POST",
//       headers: { "Content-Type": "application/json" },
//       body: JSON.stringify(patientSchedule), // ✅ send patient schedule
//     })
//       .then((res) => res.text())
//       .then((msg) => {
//         alert(msg);
//         // optionally refresh from backend
//         fetch("http://localhost:8080/patientCalendar")
//           .then((res) => res.json())
//           .then((updatedSchedule) => setPatientSchedule(updatedSchedule))
//           .catch((err) => console.error(err));
//       })
//       .catch((err) => console.error(err));
//   };

//   const schedulePatient = () => {
//     fetch("http://localhost:8080/patientReservation")
//       .then((res) => res.json())
//       .then((updatedSchedule) => {
//         setSchedule(updatedSchedule);
//         setPatientSchedule(updatedSchedule);
//         alert("Patient scheduled successfully!");
//       })
//       .catch((err) => console.error(err));
//   };

//   const generateTimeSlots = () => {
//     const slots = [];
//     let hour = 8;
//     let minute = 0;
//     while (hour < 17 || (hour === 17 && minute === 0)) {
//       const ampm = hour >= 12 ? "PM" : "AM";
//       const displayHour = hour > 12 ? hour - 12 : hour;
//       const displayMinute = minute === 0 ? "00" : minute;
//       slots.push(`${displayHour}:${displayMinute} ${ampm}`);
//       minute += 15;
//       if (minute === 60) {
//         minute = 0;
//         hour++;
//       }
//     }
//     return slots;
//   };

//   const timeSlots = generateTimeSlots();

//   const getCellStyle = (colIndex, value) => {
//     if (value === 0) {
//       return { backgroundColor: "white" };
//     }
//     switch (colIndex) {
//       case 0:
//         return { backgroundColor: "#e74c3c", color: "white" };
//       case 1:
//         return { backgroundColor: "#3498db", color: "white" };
//       case 2:
//         return { backgroundColor: "#f1c40f" };
//       case 3:
//         return { backgroundColor: "#27ae60", color: "white" };
//       default:
//         return { backgroundColor: "white" };
//     }
//   };

//   return (
//     <div>
//       <h2>Clinic & Patient Calendars</h2>

//       <div style={{ display: "flex", gap: "40px" }}>
//         {/* Clinic Calendar */}
//         <div>
//           <h3>Clinic Calendar</h3>
//           <table border="1" cellPadding="6">
//             <thead>
//               <tr>
//                 <th>Time</th>
//                 <th>Doctor</th>
//                 <th>NMT</th>
//                 <th>Patient</th>
//                 <th>Scan</th>
//               </tr>
//             </thead>
//             <tbody>
//               {schedule.map((row, i) => (
//                 <tr key={i}>
//                   <td>{timeSlots[i]}</td>
//                   {row.map((cell, j) => (
//                     <td key={j}>
//                       <button
//                         onClick={() => handleClick(i, j)}
//                         style={{
//                           width: "100%",
//                           height: "100%",
//                           border: "none",
//                           cursor: "pointer",
//                           ...getCellStyle(j, cell),
//                         }}
//                       >
//                         {cell === 1 ? "1" : ""}
//                       </button>
//                     </td>
//                   ))}
//                 </tr>
//               ))}
//             </tbody>
//           </table>
//         </div>

//         {/* Patient Calendar */}
//         <div>
//           <h3>Patient Calendar</h3>
//           <table border="1" cellPadding="6">
//             <thead>
//               <tr>
//                 <th>Doctor</th>
//                 <th>NMT</th>
//                 <th>Patient</th>
//                 <th>Scan</th>
//               </tr>
//             </thead>
//             <tbody>
//               {patientSchedule.map((row, i) => (
//                 <tr key={i}>
//                   {row.map((cell, j) => (
//                     <td key={j}>
//                       <button
//                         onClick={() => handleClick(i, j, true)}
//                         style={{
//                           width: "100%",
//                           height: "100%",
//                           border: "none",
//                           cursor: "pointer",
//                           ...getCellStyle(j, cell),
//                         }}
//                       >
//                         {cell === 1 ? "1" : ""}
//                       </button>
//                     </td>
//                   ))}
//                 </tr>
//               ))}
//             </tbody>
//           </table>
//         </div>
//       </div>

//       <br />
//       <div>
//         <button onClick={saveSchedule}>Save Schedule</button>
//         <br />
//         <button onClick={schedulePatient}>Schedule a Patient</button>
//         <button onClick={savePatientSchedule}>Save New Patient schedule</button>
//       </div>
//     </div>
//   );
// }

// export default Schedule;
//////////////////////////////////////////////////////////////////////////////////////////
// import React, { useEffect, useState } from "react";

// function Schedule() {
//   const [schedule, setSchedule] = useState([]);
//   const [patientSchedule, setPatientSchedule] = useState([]);

//   // Fetch both schedules on mount
//   useEffect(() => {
//     fetch("http://localhost:8080/schedule")
//       .then((res) => res.json())
//       .then((data) => setSchedule(data))
//       .catch((err) => console.error(err));

//     fetch("http://localhost:8080/patientCalendar")
//       .then((res) => res.json())
//       .then((data) => setPatientSchedule(data))
//       .catch((err) => console.error(err));
//   }, []);

//   // Toggle cell for clinic or patient table
//   const handleClick = (rowIndex, colIndex, isPatientTable = false) => {
//     if (isPatientTable) {
//       const newPatientSchedule = patientSchedule.map((row, i) =>
//         row.map((cell, j) =>
//           i === rowIndex && j === colIndex ? (cell === 0 ? 1 : 0) : cell
//         )
//       );
//       setPatientSchedule(newPatientSchedule);
//     } else {
//       const newSchedule = schedule.map((row, i) =>
//         row.map((cell, j) =>
//           i === rowIndex && j === colIndex ? (cell === 0 ? 1 : 0) : cell
//         )
//       );
//       setSchedule(newSchedule);
//     }
//   };

//   // Save clinic schedule
//   const saveSchedule = () => {
//     fetch("http://localhost:8080/UpdateCalender", {
//       method: "POST",
//       headers: { "Content-Type": "application/json" },
//       body: JSON.stringify(schedule),
//     })
//       .then((res) => res.text())
//       .then((msg) => alert(msg))
//       .catch((err) => console.error(err));
//   };

//   // Save patient schedule
//   const savePatientSchedule = () => {
//     fetch("http://localhost:8080/updatePatientCalendar", {
//       method: "POST",
//       headers: { "Content-Type": "application/json" },
//       body: JSON.stringify(patientSchedule),
//     })
//       .then((res) => res.text())
//       .then((msg) => {
//         alert(msg);
//         // refresh patient table from backend
//         fetch("http://localhost:8080/patientCalendar")
//           .then((res) => res.json())
//           .then((updatedSchedule) => setPatientSchedule(updatedSchedule))
//           .catch((err) => console.error(err));
//       })
//       .catch((err) => console.error(err));
//   };

//   // Schedule a patient (updates only clinic schedule)
//   const schedulePatient = () => {
//     fetch("http://localhost:8080/patientReservation")
//       .then((res) => res.json())
//       .then((updatedSchedule) => {
//         setSchedule(updatedSchedule); // update only clinic schedule
//         alert("Patient scheduled successfully!");
//       })
//       .catch((err) => console.error(err));
//   };

//   // Time slots for clinic table
//   const generateTimeSlots = () => {
//     const slots = [];
//     let hour = 8;
//     let minute = 0;
//     while (hour < 17 || (hour === 17 && minute === 0)) {
//       const ampm = hour >= 12 ? "PM" : "AM";
//       const displayHour = hour > 12 ? hour - 12 : hour;
//       const displayMinute = minute === 0 ? "00" : minute;
//       slots.push(`${displayHour}:${displayMinute} ${ampm}`);
//       minute += 15;
//       if (minute === 60) {
//         minute = 0;
//         hour++;
//       }
//     }
//     return slots;
//   };

//   const timeSlots = generateTimeSlots();

//   // Cell colors
//   const getCellStyle = (colIndex, value) => {
//     if (value === 0) return { backgroundColor: "white" };
//     switch (colIndex) {
//       case 0:
//         return { backgroundColor: "#e74c3c", color: "white" };
//       case 1:
//         return { backgroundColor: "#3498db", color: "white" };
//       case 2:
//         return { backgroundColor: "#f1c40f" };
//       case 3:
//         return { backgroundColor: "#27ae60", color: "white" };
//       default:
//         return { backgroundColor: "white" };
//     }
//   };

//   return (
//     <div>
//       <h2>Clinic & Patient Calendars</h2>
//       <div style={{ display: "flex", gap: "40px" }}>
//         {/* Clinic Calendar */}
//         <div>
//           <h3>Clinic Calendar</h3>
//           <table border="1" cellPadding="6">
//             <thead>
//               <tr>
//                 <th>Time</th>
//                 <th>Doctor</th>
//                 <th>NMT</th>
//                 <th>Patient</th>
//                 <th>Scan</th>
//               </tr>
//             </thead>
//             <tbody>
//               {schedule.map((row, i) => (
//                 <tr key={i}>
//                   <td>{timeSlots[i]}</td>
//                   {row.map((cell, j) => (
//                     <td key={j}>
//                       <button
//                         onClick={() => handleClick(i, j)}
//                         style={{
//                           width: "100%",
//                           height: "100%",
//                           border: "none",
//                           cursor: "pointer",
//                           ...getCellStyle(j, cell),
//                         }}
//                       >
//                         {cell === 1 ? "1" : ""}
//                       </button>
//                     </td>
//                   ))}
//                 </tr>
//               ))}
//             </tbody>
//           </table>
//         </div>

//         {/* Patient Calendar */}
//         <div>
//           <h3>Patient Calendar</h3>
//           <table border="1" cellPadding="6">
//             <thead>
//               <tr>
//                 <th>Doctor</th>
//                 <th>NMT</th>
//                 <th>Patient</th>
//                 <th>Scan</th>
//               </tr>
//             </thead>
//             <tbody>
//               {patientSchedule.map((row, i) => (
//                 <tr key={i}>
//                   {row.map((cell, j) => (
//                     <td key={j}>
//                       <button
//                         onClick={() => handleClick(i, j, true)}
//                         style={{
//                           width: "100%",
//                           height: "100%",
//                           border: "none",
//                           cursor: "pointer",
//                           ...getCellStyle(j, cell),
//                         }}
//                       >
//                         {cell === 1 ? "1" : ""}
//                       </button>
//                     </td>
//                   ))}
//                 </tr>
//               ))}
//             </tbody>
//           </table>
//         </div>
//       </div>

//       <br />
//       <div>
//         <button onClick={saveSchedule}>Save Clinic Schedule</button>
//         <button onClick={schedulePatient}>Schedule a Patient</button>
//         <button onClick={savePatientSchedule}>Save Patient Schedule</button>
//       </div>
//     </div>
//   );
// }

// export default Schedule;

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

  // Save clinic schedule
  const saveSchedule = () => {
    fetch("http://localhost:8080/UpdateCalender", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(schedule),
    })
      .then((res) => res.text())
      .then((msg) => alert(msg))
      .catch((err) => console.error(err));
  };

  // Save patient schedule
  const savePatientSchedule = () => {
    fetch("http://localhost:8080/updatePatientCalendar", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(patientSchedule),
    })
      .then((res) => res.text())
      .then((msg) => {
        alert(msg);
        // refresh patient table from backend
        fetch("http://localhost:8080/patientCalendar")
          .then((res) => res.json())
          .then((updatedSchedule) => setPatientSchedule(updatedSchedule))
          .catch((err) => console.error(err));
      })
      .catch((err) => console.error(err));
  };

  // Schedule a patient (updates only clinic schedule)
  const schedulePatient = () => {
    fetch("http://localhost:8080/patientReservation")
      .then((res) => res.json())
      .then((updatedSchedule) => {
        setSchedule(updatedSchedule); // update only clinic schedule
        alert("Patient scheduled successfully!");
      })
      .catch((err) => console.error(err));
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
          Save Clinic Schedule
        </button>
        <button className="action-btn" onClick={schedulePatient}>
          Schedule a Patient
        </button>
        <button className="action-btn" onClick={savePatientSchedule}>
          Save Patient Schedule
        </button>
      </div>
    </div>
  );
}

export default Schedule;
