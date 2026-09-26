# Employee Management System
Core Java + JDBC + MySQL + HTML/CSS (no frameworks)

---

## 1. FOLDER STRUCTURE

```
EmployeeManagementSystem/
│
├── src/                          → All Java source code
│   ├── model/                    → Plain Java classes representing database rows
│   │   ├── Employee.java
│   │   ├── Department.java
│   │   ├── Designation.java
│   │   ├── Leave.java
│   │   ├── Salary.java
│   │   └── User.java
│   │
│   ├── dao/                      → Data Access Objects - all SQL/JDBC code lives here
│   │   ├── EmployeeDAO.java
│   │   ├── DepartmentDAO.java
│   │   ├── DesignationDAO.java
│   │   ├── LeaveDAO.java
│   │   ├── SalaryDAO.java
│   │   └── UserDAO.java
│   │
│   ├── util/                     → Shared helper classes
│   │   └── DatabaseConnection.java   (opens/returns the MySQL connection)
│   │
│   ├── service/                  → Business rules & validation, sits between DAO and the user
│   │   ├── EmployeeService.java
│   │   ├── LeaveService.java
│   │   └── SalaryService.java
│   │
│   └── main/
│       └── Main.java              → Program entry point (console menu that drives everything)
│
├── web/                          → Static HTML pages showing the UI screens
│   ├── login.html
│   ├── dashboard.html
│   ├── employee.html
│   ├── add-employee.html
│   ├── departments.html
│   ├── designations.html
│   ├── leave.html
│   ├── salary.html
│   └── reports.html
│
├── css/
│   └── style.css                 → One shared stylesheet for every page
│
├── database/
│   └── employee_management.sql   → Creates the database, tables, and sample data
│
└── README.md                     → This file
```

### Why each folder exists
- **model/** – One class per database table. These classes only hold data (fields + getters/setters). This is where you show **encapsulation**.
- **dao/** – One class per table that talks to MySQL using JDBC (`Connection`, `PreparedStatement`, `ResultSet`). DAO = "Data Access Object". No business rules here, only SQL.
- **util/** – `DatabaseConnection.java` is the single place that knows how to connect to MySQL, so you don't repeat connection code in every DAO.
- **service/** – Sits between the DAO and the user. Validates data (e.g. "salary can't be negative") *before* calling the DAO. Keeps validation logic out of the DAO and out of the UI.
- **main/** – `Main.java` is where the program starts (`public static void main`). It shows a text menu, reads what the admin types, and calls the right Service methods.
- **web/** – Pure HTML+CSS pages showing what each screen looks like (login, dashboard, employee list, forms, reports).
- **css/** – One `style.css` used by every HTML page, so the whole site looks consistent.
- **database/** – One `.sql` file that creates everything in MySQL, including sample data, so anyone can set the project up in one step.

### Important note about how the pieces connect
Plain HTML/CSS cannot call Java code directly — that normally requires a servlet container (Tomcat) or a framework like Spring Boot, both of which this project intentionally avoids to stay beginner-friendly. So this project has two honest, clearly-separated halves:
1. **`web/`** – shows what the screens look like (the UI design).
2. **`Main.java`** – the real, working Core Java + JDBC program (login, CRUD, leave approval, salary calculation) that you run and demo separately, against the same MySQL database.

In an interview, you can say: *"I built the UI screens in HTML/CSS, and the actual business logic and database operations in Core Java using JDBC, structured in Model-DAO-Service layers. If I added a servlet container later, I could wire the two together without changing my DAO or Service code."*

---

## 2. COMPLETE SETUP GUIDE (step by step)

1. **Install JDK** – Download JDK 17+ from Oracle or use OpenJDK. Verify with `java -version` in a terminal.
2. **Install MySQL** – Install MySQL Server (e.g. MySQL Installer on Windows, or `brew install mysql` on Mac). Remember the root password you set.
3. **Install IntelliJ IDEA or Eclipse** – Download the free Community edition of either.
4. **Create the Java project** – Open your IDE → New Project → Java → name it `EmployeeManagementSystem`. Copy the `src/` folder contents into your project's source root, keeping the `model/dao/util/service/main` packages.
5. **Add the MySQL JDBC Driver JAR** – Download "MySQL Connector/J" (a `.jar` file) from the MySQL website. In IntelliJ: File → Project Structure → Libraries → + → add the JAR. In Eclipse: right-click project → Build Path → Add External JARs.
6. **Create the MySQL database** – Open MySQL Workbench or the `mysql` command line and connect as root.
7. **Run the SQL file** – Run all of `database/employee_management.sql`. This creates the `employee_management` database, all 6 tables, and inserts sample data (including an admin login: username `admin`, password `admin123`).
8. **Configure database username/password** – Open `src/util/DatabaseConnection.java` and set `USERNAME` and `PASSWORD` to match your own MySQL setup.
9. **Compile the Java project** – In your IDE, build/compile the project (this checks for errors).
10. **Run the project** – Run `Main.java`. It will ask for a username/password in the console — enter `admin` / `admin123`.
11. **Open the HTML pages** – Open any file in `web/` (e.g. `login.html`) directly in a browser to see the screen designs.
12. **Test the features** – In the running console app, try adding an employee, viewing the list, applying for leave, approving it, and adding a salary record. Then check the MySQL tables to confirm the data was saved.

### Troubleshooting common errors
- **`Class Not Found: com.mysql.cj.jdbc.Driver`** → The MySQL Connector/J JAR isn't added to your project's build path. Re-check step 5.
- **`Communications link failure` / `Connection refused`** → MySQL service isn't running. Start it from Services (Windows) or `brew services start mysql` (Mac).
- **`Access denied for user 'root'@'localhost'`** → Wrong username/password in `DatabaseConnection.java`. Double-check step 8.
- **`Unknown database 'employee_management'`** → You haven't run the SQL file yet, or ran it against the wrong connection. Re-check step 7.
- **`Table doesn't exist`** → The SQL script didn't fully run. Re-run the whole `employee_management.sql` file from the top.
- **Foreign key errors when deleting** → You're trying to delete a department/designation/employee that's still referenced elsewhere. Delete the dependent rows first, or note that `leaves`/`salary` are set to auto-delete with the employee (`ON DELETE CASCADE`).

---

## 3. OOP CONCEPTS USED

| Concept | Where it's used | Why | Interview-ready explanation |
|---|---|---|---|
| **Class & Object** | Every file in `model/`, e.g. `Employee` | Represent real-world entities as Java objects | "A class is a blueprint; an object is a real instance created from it — like `Employee` is the blueprint and each employee record I load is an object." |
| **Encapsulation** | Private fields + public getters/setters in every model class | Protects data from being changed directly from outside the class | "I keep fields private and only allow access through getters/setters, so I control how data is read or changed." |
| **Constructor** | Every model class has 2+ constructors | Initialize objects with starting values | "A constructor runs automatically when an object is created, to set its initial state." |
| **Method Overloading** | `Employee` has two constructors (with/without ID); `Main.java` reuses similarly | Same method/constructor name, different parameters | "Overloading lets me have multiple constructors that do similar jobs but accept different data — like one for a new employee (no ID yet) and one for reading an existing one (has an ID)." |
| **Inheritance** | Not forced — kept simple, since a flat DAO/Service structure doesn't need it here. Could be introduced (e.g. a common `BaseDAO`) if the interviewer asks how you'd extend it. | — | "I didn't force inheritance where it wasn't needed, but I could introduce a `BaseDAO` class if the project grew and DAOs started repeating structure." |
| **Method** | Every DAO/Service method (`addEmployee`, `getAllEmployees`, etc.) | Break logic into small, reusable, named units | "Each method has one clear job, which makes the code easier to test and read." |
| **Exception Handling** | Every DAO method wraps JDBC calls in `try-catch(SQLException)` | JDBC calls can fail (bad SQL, closed connection, etc.) and must not crash the whole program | "I catch `SQLException` around every database call so one failed query doesn't crash the whole application, and I print a clear message instead." |

---

## 4. CRUD OPERATIONS MAP

| Entity | Create | Read | Update | Delete |
|---|---|---|---|---|
| Employee | `EmployeeDAO.addEmployee()` | `getAllEmployees()`, `getEmployeeById()`, `searchEmployees()` | `updateEmployee()` | `deleteEmployee()` |
| Department | `DepartmentDAO.addDepartment()` | `getAllDepartments()` | `updateDepartment()` | `deleteDepartment()` |
| Designation | `DesignationDAO.addDesignation()` | `getAllDesignations()` | `updateDesignation()` | `deleteDesignation()` |
| Leave | `LeaveDAO.applyLeave()` | `getAllLeaves()`, `getLeavesByStatus()` | `updateLeaveStatus()` (Approve/Reject) | — (leaves are kept for record) |
| Salary | `SalaryDAO.addSalary()` | `getAllSalaries()`, `getSalariesByEmployee()` | `updateSalary()` | — (salary history is kept for record) |

Example (Employee — Create):
```java
String sql = "INSERT INTO employees (name, email, ...) VALUES (?, ?, ...)";
PreparedStatement ps = con.prepareStatement(sql);
ps.setString(1, emp.getName());
...
ps.executeUpdate();
```

---

## 5. ONE-MINUTE PROJECT INTRODUCTION (for interviews)

> "I built an Employee Management System using Core Java, JDBC, and MySQL, with a simple HTML and CSS front end. The idea is to let an admin manage employees, departments, designations, leave requests, and salary records. I structured the Java code into Model, DAO, Service, and Main layers — models represent database tables, DAOs handle all the SQL using JDBC's PreparedStatement, and the service layer validates data before saving it, like checking that salary isn't negative or that an email is valid. I used MySQL for storage with proper primary and foreign keys, and I calculate net salary — basic plus allowance minus deduction — directly in Java. I also handled exceptions around every database call so the program doesn't crash on a bad query. It's a simple project, but it let me practice real CRUD operations, JDBC, and core OOP concepts like encapsulation and constructor overloading."

---

## 6. RESUME DESCRIPTIONS

**Short version:**
- Built an Employee Management System in Core Java, JDBC, and MySQL with a simple HTML/CSS interface, implementing full CRUD operations for employees, departments, and leave/salary records.
- Designed a normalized MySQL schema with 6 tables and primary/foreign key relationships, and used PreparedStatement throughout to prevent SQL injection.

**Detailed version:**
- Developed an Employee Management System using Core Java, JDBC, and MySQL, structured into Model, DAO, and Service layers to separate data, database access, and business logic.
- Implemented CRUD operations (Add/View/Search/Update/Delete) for employees, departments, and designations using JDBC `PreparedStatement` and `ResultSet`.
- Built a Leave Management module allowing employees' leave requests to be submitted, approved, or rejected, with status tracked in MySQL.
- Built a Salary Management module that calculates Net Salary (Basic + Allowance − Deduction) in Java and stores payment history per employee.
- Designed the front-end screens using pure HTML5 and CSS3 (no frameworks), including a login page, dashboard with summary cards, and data tables for reports.

---

## 7. PROJECT VIVA — 15 QUESTIONS

1. **What is JDBC?** A Java API that lets Java programs connect to and run SQL against a database.
2. **What is a PreparedStatement, and why use it over Statement?** A precompiled SQL statement with placeholders (`?`); it's faster on repeated use and prevents SQL injection, unlike `Statement` with string concatenation.
3. **What is a ResultSet?** An object that holds the rows returned by a SQL `SELECT` query, which you loop through with `.next()`.
4. **What is a primary key?** A column (or columns) that uniquely identifies each row in a table, e.g. `employee_id`.
5. **What is a foreign key?** A column that references a primary key in another table, linking the two tables — e.g. `department_id` in `employees` referencing `departments`.
6. **Why did you use MySQL?** It's free, widely used, easy to set up locally, and works well with JDBC.
7. **What does `ON DELETE CASCADE` do in your schema?** Automatically deletes related rows (like leave/salary records) when the parent employee row is deleted.
8. **What is encapsulation, and where did you use it?** Hiding fields as `private` and exposing them only via getters/setters — used in every class in `model/`.
9. **What is the DAO pattern?** A design pattern where one class is responsible for all database access for a specific entity, keeping SQL code separate from business logic.
10. **Why is your code split into model/dao/service/main?** To keep each layer responsible for one thing — data, database access, validation, and program flow — making the code easier to read and maintain.
11. **What is SQLException, and how do you handle it?** A checked exception JDBC throws on database errors; I catch it in each DAO method and print a friendly message instead of letting the program crash.
12. **How is Net Salary calculated?** In Java: `netSalary = basicSalary + allowance - deduction`, computed in `SalaryService` before saving.
13. **What is the difference between `DELETE` and `DROP`?** `DELETE` removes rows from a table (table still exists); `DROP` removes the entire table structure.
14. **What is the difference between `WHERE` and `HAVING`?** `WHERE` filters rows before grouping; `HAVING` filters groups after a `GROUP BY`.
15. **How would you improve this project?** Add password hashing for login, connection pooling instead of a new connection per query, and a real web server (servlets/Spring Boot) to connect the HTML pages to the Java backend directly.

---

## 8. PROJECT CHECKLIST

- [x] MySQL database and all 6 tables created (`employee_management.sql`)
- [x] Sample data inserted (admin user, departments, designations, employees, leaves, salary)
- [x] `DatabaseConnection.java` connects to MySQL and handles errors
- [x] Model classes for all 6 entities
- [x] DAO classes with full CRUD using `PreparedStatement`
- [x] Service layer with validation (email format, phone digits, non-negative salary, required fields)
- [x] `Main.java` console menu wiring login + all modules together
- [x] Net Salary calculated in Java (`Basic + Allowance − Deduction`)
- [x] Leave approve/reject flow
- [x] HTML/CSS pages for login, dashboard, employees, departments, designations, leave, salary, reports
- [x] Shared `style.css` for consistent design (sidebar, cards, tables, forms, buttons)
- [x] No JavaScript, Spring Boot, Maven, Hibernate/JPA, React, or Angular used anywhere
- [x] README with setup guide, OOP explanations, CRUD map, interview Q&A, viva questions, and resume bullets

---

## 9. PROJECT INTERVIEW QUESTIONS (30+)

**General**
1. *Tell me about your Employee Management System.* — See the one-minute introduction above.
2. *Why did you choose this project?* — It covers core backend skills (Java, JDBC, SQL) that come up in almost every fresher interview, in a manageable scope.
3. *Which technologies did you use?* — Core Java, JDBC, MySQL, HTML5, CSS3.
4. *Why did you use Core Java instead of a framework?* — To learn and clearly demonstrate the fundamentals — JDBC, OOP, exception handling — without a framework hiding what's happening.

**JDBC & Database**
5. *What is JDBC?* — See viva Q1.
6. *Why did you use JDBC?* — It's the standard way for Java to talk to a relational database like MySQL.
7. *What is CRUD?* — Create, Read, Update, Delete — the four basic data operations.
8. *Where did you implement CRUD?* — In the DAO classes for Employee, Department, Designation, Leave, and Salary.
9. *What is MySQL?* — An open-source relational database management system.
10. *Why did you use MySQL?* — Free, popular, easy to install locally, and integrates well with JDBC.
11. *What is a primary key?* — See viva Q4.
12. *What is a foreign key?* — See viva Q5.
13. *What is a database connection?* — A live link between the Java program and the MySQL server, created via `DriverManager.getConnection()`.
14. *What is PreparedStatement?* — See viva Q2.
15. *Why is PreparedStatement preferred over Statement?* — Prevents SQL injection and is faster for repeated queries since it's precompiled.
16. *What is ResultSet?* — See viva Q3.
17. *What is SQLException?* — See viva Q11.

**OOP**
18. *What OOP concepts did you use?* — Encapsulation, constructors, constructor overloading, and exception handling (see the OOP table above).
19. *Where did you use encapsulation?* — Every model class: private fields, public getters/setters.
20. *What is a class?* — A blueprint for creating objects, defining fields and methods.
21. *What is an object?* — A real instance created from a class, holding actual data.
22. *What is inheritance?* — One class reusing fields/methods of another via `extends`; I kept this project flat/simple and didn't force it in, but could add a `BaseDAO` if it grew.
23. *What is exception handling?* — Using `try-catch(-finally)` to gracefully handle runtime errors like a failed database call, instead of crashing.

**SQL comparisons**
24. *Difference between Statement and PreparedStatement?* — See viva Q2/Q15.
25. *Difference between DELETE and DROP?* — See viva Q13.
26. *Difference between WHERE and HAVING?* — See viva Q14.
27. *Difference between primary key and foreign key?* — Primary key uniquely identifies rows in its own table; foreign key links to a primary key in another table.

**Project flow**
28. *Explain your database tables.* — `users` (admin login), `departments`, `designations`, `employees` (linked to both via foreign keys), `leaves` and `salary` (both linked to `employees`).
29. *Explain the flow of your project.* — Admin logs in → sees dashboard → manages employees/departments/designations → employees apply for leave, admin approves/rejects → admin records salary, net salary is auto-calculated.
30. *What challenges did you face while developing the project?* — Getting JDBC connection setup right (driver JAR, credentials) and structuring the code cleanly into layers instead of putting everything in one class.
31. *How does Java communicate with MySQL?* — Through the JDBC driver, using a `Connection` object created by `DriverManager`, then running SQL through `PreparedStatement`.
32. *How does the login work?* — `UserDAO.validateLogin()` runs a `SELECT` with the entered username and password as parameters; a matching row means success.
33. *How does adding an employee work?* — Data is collected, validated in `EmployeeService`, then inserted via `EmployeeDAO.addEmployee()` using an `INSERT` with `PreparedStatement`.
34. *How does updating an employee work?* — The existing record is loaded by ID, fields are changed, then `EmployeeDAO.updateEmployee()` runs an `UPDATE ... WHERE employee_id = ?`.
35. *How does deleting an employee work?* — After confirmation, `EmployeeDAO.deleteEmployee()` runs `DELETE FROM employees WHERE employee_id = ?`; related leave/salary rows are removed automatically via `ON DELETE CASCADE`.
36. *How would you improve this project in the future?* — See viva Q15.
