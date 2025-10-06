# Clinic Scheduling System

A Spring Boot application that manages clinic appointment scheduling by matching patient availability against the clinic's master calendar.

## Overview

This system solves the scheduling problem of finding available time slots for patients within a clinic's existing schedule. It uses a 2D matrix representation where appointments are checked for conflicts before being reserved.

<img width="1270" height="877" alt="Image" src="https://github.com/user-attachments/assets/b5dde7de-deaf-4d5a-a2e4-15cbbc2a5f67" />
<br><br>
<img width="1241" height="541" alt="Image" src="https://github.com/user-attachments/assets/ef7fc196-9b16-4feb-ba15-f45d792157dc" />

## Features

- **Conflict Detection**: Automatically detects scheduling conflicts between patient requests and existing appointments
- **Calendar Compression**: Optimizes patient schedules by removing empty time slots for efficient matching
- **Excel Integration**: Loads clinic calendar from Excel files for easy configuration
- **REST API**: Provides HTTP endpoints for schedule management and reservations
- **Comprehensive Testing**: Includes unit tests covering edge cases and core scheduling logic

## Technology Stack

- **Java 17+**
- **Spring Boot 3.x**
- **Apache POI** - Excel file parsing
- **JUnit 5** - Unit testing
- **Maven** - Dependency management

## Architecture

The application follows a layered architecture with clear separation of concerns:

```
┌─────────────────────────────────────┐
│     AppController (REST Layer)      │
│         @RestController              │
└──────────────┬──────────────────────┘
               │
               ├──────────────────────┐
               │                      │
               ▼                      ▼
┌──────────────────────┐   ┌─────────────────────┐
│   ClinicCalendar     │   │  PatientSchedule    │
│     @Service         │   │    @Component       │
└──────────┬───────────┘   └─────────────────────┘
           │
           ▼
┌──────────────────────┐
│    ExcelParser       │
│    @Repository       │
└──────────────────────┘
```

### Components

- **`AppController`**: REST endpoints for schedule operations
- **`ClinicCalendar`**: Core scheduling logic and conflict detection
- **`ExcelParser`**: Loads and parses clinic schedule from Excel files
- **`PatientSchedule`**: Manages patient appointment requests

## Calendar Semantics

Both clinic and patient calendars use a 2D integer matrix:

- **`1`** = Occupied/Requested time slot
- **`0`** = Available time slot

### Example

```
Clinic Calendar:          Patient Request:
[0, 0, 0, 0]             [1, 0, 1, 0]
[1, 0, 0, 1]             [1, 0, 1, 0]
[1, 0, 0, 1]             [0, 0, 0, 0]
[0, 0, 0, 0]
```

The algorithm finds the first position where the patient's schedule fits without conflicts.

## API Endpoints

### Health Check

```http
GET /
```

Returns: `"Server is Alive"`

### Get Clinic Schedule

```http
GET /schedule
```

Returns: 2D array of clinic calendar

### Update Clinic Schedule

```http
POST /UpdateCalender
Content-Type: application/json

[[0,0,0,0], [1,0,0,1], ...]
```

Returns: Success message

### Reserve Patient Appointment

```http
GET /patientReservation
```

Returns: Updated clinic calendar if successful, `null` if no slots available

### Get Patient Schedule

```http
GET /patientCalendar
```

Returns: Patient's current schedule

### Update Patient Schedule

```http
POST /updatePatientCalendar
Content-Type: application/json

[[1,0,1,0], [1,0,1,0], ...]
```

Returns: Success message

## Configuration

Set the Excel file path in `application.properties`:

```properties
calendar.file.path=calendar.xlsx
```

The Excel file should be placed in `src/main/resources/`.

## Core Algorithm

### Calendar Compression

Before matching, the system compresses patient calendars by removing leading and trailing empty rows:

```java
Input:  [[0,0,0,0],     Output: [[0,1,0,0],
         [0,1,0,0],              [1,0,0,0]]
         [1,0,0,0],
         [0,0,0,0]]
```

This optimization reduces the search space significantly.

### Conflict Detection

The scheduling algorithm:

1. Compresses the patient calendar
2. Iterates through each possible starting position in the clinic calendar
3. For each position, checks if the entire patient schedule overlaps with occupied slots
4. Returns the first valid position found, or indicates no match exists

Time Complexity: O(n × m × p × q) where:

- n, m = clinic calendar dimensions
- p, q = patient calendar dimensions

## Running the Application

### Prerequisites

- Java 17 or higher
- Maven 3.6+

### Build

```bash
mvn clean install
```

### Run

```bash
mvn spring-boot:run
```

The server starts on `http://localhost:8080`

### Run Tests

```bash
mvn test
```

## Testing

The project includes comprehensive unit tests in `ClinicCalendarTest`:

- **Compression Tests**: Verify correct removal of empty rows
- **Conflict Detection**: Test various collision scenarios
- **Edge Cases**: Empty calendars, full collisions, single slots
- **Special Cases**: Large calendar scenarios (37+ rows)

Example test coverage:

- Patient schedule fits in available slots ✓
- Patient schedule has conflicts ✓
- Empty patient calendars ✓
- Compression with various patterns ✓

## Project Structure

```
src/
├── main/
│   ├── java/com/Scheduler/
│   │   ├── AppController.java
│   │   ├── ClinicCalendar.java
│   │   ├── ExcelParser.java
│   │   └── PatientSchedule.java
│   └── resources/
│       ├── application.properties
│       └── calendar.xlsx
└── test/
    └── java/com/Scheduler/
        └── ClinicCalendarTest.java
```

## Design Decisions

### Fixed-Size Arrays

Both clinic and patient calendars are fixed-size (37 rows × 4 columns) per requirements. This simplifies validation and focuses the implementation on the core algorithm.

### Stateful Matching

The `foundStartRow` field stores the last successful match position. While stateful, this design keeps the API simple and works well for single-threaded scheduling.

### Early Exit Optimization

The collision detection uses short-circuit evaluation (`&& !collision`) to exit early when conflicts are found, improving performance for densely occupied calendars.



