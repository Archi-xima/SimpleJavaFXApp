# Student Registration System (MVC)

A Java console application demonstrating the **Model-View-Controller (MVC)** architectural pattern through a clean, robust **"Register Student"** workflow with comprehensive validation and error handling.

---

## 📑 Table of Contents
- [Architecture Overview](#architecture-overview)
- [Architecture Diagrams (Mermaid.js)](#architecture-diagrams-mermaidjs)
  - [Layered Architecture View](#1-layered-architecture-view)
  - [UML Sequence Diagram (Registration Flow)](#2-uml-sequence-diagram-registration-flow)
- [Project Structure](#project-structure)
- [Identified Error Scenarios & System Response](#identified-error-scenarios--system-response)
- [Getting Started in IntelliJ IDEA](#getting-started-in-intellij-idea)
- [Running the Application](#running-the-application)
- [Automated Testing](#automated-testing)

---

## Architecture Overview

The system strictly adheres to the **Model-View-Controller (MVC)** pattern to ensure separation of concerns, testability, and maintainability:

1. **Model Layer (`com.studentapp.model`)**:
   - Encapsulates student data (`Student`) and persistence logic (`StudentRepository`).
   - Contains domain-specific exceptions (`DuplicateStudentException`, `InvalidStudentDataException`).
   - Completely agnostic of the console UI and controller mechanisms.

2. **View Layer (`com.studentapp.view`)**:
   - Manages user interaction and console presentation (`StudentConsoleView`).
   - Collects raw user inputs and renders menus, tables, success prompts, and error notifications.
   - Contains no business logic, domain validation, or storage logic.

3. **Controller Layer (`com.studentapp.controller`)**:
   - Intermediary orchestrator (`StudentController`).
   - Processes user actions triggered in the view, enforces business validation rules, queries/updates the model, catches domain exceptions, and instructs the view to render appropriate feedback.

---

## Architecture Diagrams (Mermaid.js)

### 1. Layered Architecture View

```mermaid
graph TD
    subgraph Presentation_Layer ["Presentation Layer (View)"]
        UI["StudentConsoleView<br/>(CLI Menus, Prompts, Output Formatting)"]
    end

    subgraph Controller_Layer ["Business / Coordination Layer (Controller)"]
        CTRL["StudentController<br/>(Input Validation, Flow Control, Exception Handling)"]
    end

    subgraph Model_Layer ["Data & Domain Layer (Model)"]
        REPO["StudentRepository<br/>(In-Memory Persistence & Lookup)"]
        ENTITY["Student<br/>(Entity Model: id, name, email, course)"]
        EXCEPTIONS["Custom Exceptions<br/>(DuplicateStudentException, InvalidStudentDataException)"]
    end

    UI -->|"Raw Input"| CTRL
    CTRL -->|"Display Commands / Feedback"| UI
    CTRL -->|"Validate & Create"| ENTITY
    CTRL -->|"Persist / Query"| REPO
    REPO -->|"Throws Errors"| EXCEPTIONS
    CTRL -.->|"Handles"| EXCEPTIONS
```

---

### 2. UML Sequence Diagram (Registration Flow)

```mermaid
sequenceDiagram
    autonumber
    actor User
    participant View as StudentConsoleView
    participant Controller as StudentController
    participant Repository as StudentRepository
    participant Model as Student

    User ->> View: Selects "1. Register New Student"
    View ->> Controller: handleRegisterStudent()
    
    Controller ->> View: promptStudentId(), promptStudentName(), promptEmail(), promptCourse()
    View ->> User: Display input prompts
    User -->> View: Enter ID, Name, Email, Course
    View -->> Controller: Return raw input strings

    Note over Controller: Business Validation<br/>(Non-blank, Regex checks)
    
    alt Invalid Format / Empty Data
        Controller ->> Controller: throw InvalidStudentDataException
        Controller ->> View: displayErrorMessage("Validation Error: ...")
        View ->> User: Render error message
    else Data Format Valid
        Controller ->> Repository: existsById(studentId)
        
        alt Student ID Already Exists
            Repository -->> Controller: true
            Controller ->> Controller: throw DuplicateStudentException
            Controller ->> View: displayErrorMessage("Registration Aborted: ...")
            View ->> User: Render duplicate error message
        else Student ID is Unique
            Repository -->> Controller: false
            Controller ->> Model: new Student(id, name, email, course)
            Model -->> Controller: studentInstance
            Controller ->> Repository: save(studentInstance)
            Repository -->> Controller: Confirmation
            Controller ->> View: displaySuccessMessage("Student successfully registered!")
            Controller ->> View: displayStudentDetails(studentInstance)
            View ->> User: Display success banner & student details
        end
    end
```

---

## Project Structure

```
StudentApp/
├── pom.xml                               # Maven project configuration (Java 17+)
├── .gitignore                            # Standard git exclusions for Java/IntelliJ
├── README.md                             # Project documentation & diagrams
└── src/
    ├── main/
    │   └── java/
    │       └── com/
    │           └── studentapp/
    │               ├── Main.java                          # Application entry point
    │               ├── controller/
    │               │   └── StudentController.java         # Orchestrates View & Model
    │               ├── model/
    │               │   ├── Student.java                   # Domain entity
    │               │   ├── StudentRepository.java         # Data storage & lookup
    │               │   └── exception/
    │               │       ├── DuplicateStudentException.java
    │               │       └── InvalidStudentDataException.java
    │               └── view/
    │                   └── StudentConsoleView.java        # CLI presentation & input
    └── test/
        └── java/
            └── com/
                └── studentapp/
                    └── StudentRegistrationTest.java       # Automated unit test suite
```

---

## Identified Error Scenarios & System Response

To ensure system reliability, the registration workflow enforces robust defensive programming:

| Scenario | Trigger Condition | System Response & Exception | User-Facing Output |
| :--- | :--- | :--- | :--- |
| **Duplicate Student ID** | User enters an ID that matches an existing record in the repository (case-insensitive). | `DuplicateStudentException` thrown by business logic. Caught by `StudentController`. | `[ERROR] Registration Aborted: Student ID '<ID>' is already registered in the system.` |
| **Blank / Missing Fields** | User submits whitespace or empty input for ID, Name, Email, or Course. | `InvalidStudentDataException` thrown. Caught by `StudentController`. | `[ERROR] Validation Error: <Field> cannot be blank.` |
| **Invalid Email Format** | Email fails the standard pattern `^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$`. | `InvalidStudentDataException` thrown. Caught by `StudentController`. | `[ERROR] Validation Error: Invalid email address format. Example: student@university.edu` |
| **Invalid ID Format** | ID contains spaces/symbols or is outside 2–15 alphanumeric characters. | `InvalidStudentDataException` thrown. Caught by `StudentController`. | `[ERROR] Validation Error: Invalid Student ID format. Must be 2-15 alphanumeric characters.` |

**Key Design Choice**: Exceptions do **not** crash the console application. The controller catches domain exceptions gracefully and delegates user feedback to the view, returning the user safely back to the main interactive menu.

---

## Getting Started in IntelliJ IDEA

1. **Open IntelliJ IDEA**:
   - Choose **File** > **Open...**
   - Navigate to the `StudentApp` folder and click **OK**.
2. **Import as Maven Project**:
   - IntelliJ IDEA will automatically recognize `pom.xml`.
   - If prompted, click **Trust Project** and select **Load Maven Project**.
3. **Verify Project SDK**:
   - Go to **File** > **Project Structure** > **Project**.
   - Verify that **SDK** is set to Java 17 or higher (e.g., Java 17, 21, or 25).
4. **Locate `Main.java`**:
   - Navigate to `src/main/java/com/studentapp/Main.java`.
   - Right-click on `Main.java` and select **Run 'Main.main()'**.

---

## Running the Application

### Via Command Line (Terminal)
You can compile and run directly without an IDE using standard Java tools:

```bash
# Compile all source files
javac -d target/classes src/main/java/com/studentapp/model/exception/*.java src/main/java/com/studentapp/model/*.java src/main/java/com/studentapp/view/*.java src/main/java/com/studentapp/controller/*.java src/main/java/com/studentapp/Main.java

# Run the application
java -cp target/classes com.studentapp.Main
```

### Via Maven
```bash
mvn clean compile exec:java -Dexec.mainClass="com.studentapp.Main"
```

---

## Automated Testing

Run the included verification test suite to validate success and error scenarios:

```bash
# Compile & run test runner
javac -d target/classes -cp target/classes src/test/java/com/studentapp/StudentRegistrationTest.java
java -ea -cp target/classes com.studentapp.StudentRegistrationTest
```
