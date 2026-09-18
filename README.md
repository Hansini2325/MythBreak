# 1. Project Title: MythBreak

**MythBreak** is a skill-development and opportunity platform designed to help students and freshers move from learning skills to finding real opportunities.

**Tagline:**
**Break Myths. Build Skills. Learn. Apply. Earn.**

---

# 2. Overview of the Project

MythBreak addresses the gap between **learning skills and getting opportunities**. Many students learn different technical and professional skills but struggle to showcase those skills, find suitable opportunities, and understand which opportunities match their abilities.

MythBreak provides a platform where users can **learn through courses and resources, build their skills, create a profile, showcase their abilities, and apply for relevant opportunities** such as internships, jobs, freelance projects, and other skill-based opportunities.

The platform supports different user roles:

* **Learner** – Learns new skills through available courses and resources.
* **Earner** – Builds a profile, showcases skills, and applies for opportunities.
* **Educator** – Creates and manages courses for learners.
* **Company/Recruiter** – Posts opportunities and finds candidates based on required skills.

The main workflow of the project is:

**Learn → Build Skills → Showcase → Apply → Earn**

---

# 3. Features

### User Registration and Login

* Users can register and log in to the platform.
* Different roles are supported for different types of users.
* Authentication is secured using **JWT-based authentication**.

### Course Management

* Educators can create and manage courses.
* Learners can view available courses.
* Course information includes relevant skills and learning content.

### Skill Management

* Users can maintain their skills and abilities.
* A common skill library can be used to connect skills with courses and opportunities.

### Opportunity Management

* Companies/Recruiters can create opportunities such as:

  * Internships
  * Jobs
  * Freelance projects
  * Skill-based opportunities
* Opportunities contain the skills required from candidates.

### Skill-Based Candidate Matching

* The system compares the skills required for an opportunity with the skills possessed by an earner.
* A match percentage is calculated based on the number of required skills matched.

**Formula:**

`Match Percentage = (Matched Skills / Total Required Skills) × 100`

For example, if an opportunity requires **Java, Spring Boot, MySQL, and Git**, and a candidate has **Java, Spring Boot, and MySQL**, the candidate receives a **75% skill match**.

### Application Management

* Earners can apply for available opportunities.
* Companies can view applications received for their opportunities.
* Application status can be managed by the company.

### Validation and Error Handling

* Input validation is provided for important user and application data.
* The system handles invalid requests and application-related errors appropriately.

---

# 4. Technologies/Tools Used

| Category             | Technology/Tool       |
| -------------------- | --------------------- |
| Programming Language | Java 21               |
| Backend Framework    | Spring Boot 3.2.5     |
| Web/API              | Spring Web, REST APIs |
| Database             | MySQL 8               |
| Database Access      | Spring Data JPA       |
| ORM                  | Hibernate             |
| Security             | Spring Security, JWT  |
| Validation           | Jakarta Validation    |
| Frontend             | HTML, CSS, JavaScript |
| Testing              | JUnit 5, Mockito      |
| API Testing          | Postman               |
| Build Tool           | Maven                 |
| IDE                  | IntelliJ IDEA         |
| Database Tool        | MySQL Workbench       |
| Version Control      | Git and GitHub        |

---

# 5. Steps to Install & Run the Project

### Step 1: Install Required Software

Install the following software:

* **Java 21**
* **Maven**
* **MySQL 8**
* **IntelliJ IDEA** or another Java IDE
* **Git**

Verify Java and Maven installation:

```bash
java -version
mvn -version
```

### Step 2: Clone the Repository

Clone the MythBreak repository from GitHub:

```bash
git clone <repository-url>
```

Move into the project directory:

```bash
cd MythBreak
```

### Step 3: Create the MySQL Database

Open MySQL Workbench or MySQL command line and create the database:

```sql
CREATE DATABASE mythbreak;
```

### Step 4: Configure Database Connection

Open:

```text
src/main/resources/application.properties
```

Configure the MySQL username, password, and database connection according to your local MySQL setup.

Example:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/mythbreak
spring.datasource.username=root
spring.datasource.password=YOUR_PASSWORD
```

### Step 5: Build the Project

Open the terminal in the project directory and run:

```bash
mvn clean package
```

This compiles the project, runs the tests, and creates the application JAR file.

### Step 6: Run the Application

Run the Spring Boot application using:

```bash
mvn spring-boot:run
```

The application will start on:

```text
http://localhost:8080
```

Open the application in a web browser to access the MythBreak platform.

---

# 6. Instructions for Testing

Testing is performed using **JUnit 5 and Mockito** for backend unit testing and **Postman** for testing REST APIs.

### Run All Tests

From the project directory, execute:

```bash
mvn test
```

To perform a complete build with testing:

```bash
mvn clean package
```

### Unit Testing

The project contains unit tests for important service-layer functionality, including:

* Authentication
* Application management
* Skill management
* Skill matching

The current test suite contains **20 tests**, covering the major backend services.

### API Testing Using Postman

The REST APIs can be tested using Postman.

Important API areas include:

* User registration
* User login
* Course operations
* Skill operations
* Opportunity operations
* Application operations

For protected APIs:

1. Login using the authentication API.
2. Obtain the JWT token from the login response.
3. Add the token to the request authorization.
4. Send the required API request.
5. Verify the response status and returned data.

### Expected Testing Result

A successful test execution should complete without test failures, and the application should start successfully with the configured MySQL database.

# 7. Screenshots
<img width="947" height="473" alt="landing page" src="https://github.com/user-attachments/assets/5d1ce0f2-a015-4b62-95cc-56274edab763" />
<img width="959" height="468" alt="image" src="https://github.com/user-attachments/assets/1f10ef3e-7684-4f46-9449-14f7ce3e9616" />
<img width="948" height="472" alt="image" src="https://github.com/user-attachments/assets/e8a814a3-a7de-4096-8b35-7ed83e4164bc" />
<img width="959" height="473" alt="image" src="https://github.com/user-attachments/assets/f6293775-02d3-4cb3-aa98-d7bf283624a7" />
<img width="956" height="470" alt="image" src="https://github.com/user-attachments/assets/70c6bdb6-d356-4f01-8ec3-b618e6b4f6e1" />
<img width="959" height="472" alt="image" src="https://github.com/user-attachments/assets/4e874840-b975-4c84-8b93-c65b7417c2d4" />





