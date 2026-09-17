

```
MythBreak/
│
├── README.md
├── statement.md
├── pom.xml
├── .gitignore
│
├── docs/
│   ├── architecture.png
│   ├── workflow.png
│   ├── use-case-diagram.png
│   ├── class-diagram.png
│   ├── sequence-diagram.png
│   └── er-diagram.png
│
├── database/
│   └── schema.sql
│
└── src/
    ├── main/
    └── test/
```

## 1. `README.md`

Copy this into your **README.md**:

````markdown
# MythBreak

## 💥 Break Myths. 🛠️ Build Skills. 📚 Learn. 🚀 Apply. 💰 Earn.

MythBreak is a web-based skill development and opportunity platform designed to connect learning with real-world opportunities.

The platform follows a simple journey:

**Learn → Build Skills → Showcase → Apply → Earn**

MythBreak addresses the gap between learning skills and finding opportunities by allowing learners to discover courses, build their skills, showcase their capabilities, and find relevant opportunities based on their skill sets.

Companies can publish opportunities and identify candidates based on skill matching, while educators can create and manage learning resources.

---

# 1. Project Overview

Many students and freshers learn technical and professional skills through different platforms but struggle to connect those skills with practical opportunities.

The major problems include:

- Difficulty finding relevant learning resources.
- Lack of structured skill development.
- Difficulty showcasing skills effectively.
- Difficulty finding opportunities matching existing skills.
- Lack of a direct connection between educators, learners, earners, and companies.
- Difficulty for companies to identify candidates based on required skills.

MythBreak provides a unified platform that connects these stages into one ecosystem.

Instead of treating learning and employment as separate activities, MythBreak connects them through a skill-based workflow.

---

# 2. Problem Statement

Students and freshers often acquire skills from multiple learning platforms but lack a structured pathway to convert those skills into practical opportunities.

At the same time, companies face difficulties in identifying candidates whose skills match the requirements of internships, jobs, freelance projects, and other opportunities.

MythBreak aims to solve this problem by providing a platform where users can learn skills, build their profiles, discover opportunities, and receive skill-based matching with relevant opportunities.

---

# 3. Objectives

The main objectives of MythBreak are:

1. Provide a centralized platform for skill development.
2. Allow learners to discover and enroll in courses.
3. Allow educators to create and manage courses.
4. Allow users to build skill-based profiles.
5. Allow companies to publish jobs, internships, freelance projects, and other opportunities.
6. Match candidate skills with opportunity requirements.
7. Display matched and missing skills to help users understand their suitability.
8. Provide role-based access to different users.
9. Maintain secure authentication and authorization.
10. Demonstrate a complete real-world Java Spring Boot application.

---

# 4. Target Users

MythBreak supports four primary user roles.

### 👨‍🎓 Learner

Users who want to learn new skills through courses and learning resources.

Responsibilities:

- Browse courses.
- Enroll in courses.
- Track learning progress.
- Develop skills.
- Transition toward practical opportunities.

### 💼 Earner

Users who already possess skills and want to find opportunities.

Responsibilities:

- Maintain a skill profile.
- Showcase skills.
- Browse opportunities.
- Check skill matches.
- Apply for opportunities.

### 👨‍🏫 Educator

Users who create and provide learning content.

Responsibilities:

- Create courses.
- Add course information.
- Manage learning resources.
- Monitor course-related information.

### 🏢 Company / Recruiter

Organizations looking for skilled candidates.

Responsibilities:

- Create opportunities.
- Define required skills.
- View applications.
- Review candidates.
- Use skill matching information.

---

# 5. Core Workflow

```text
                ┌───────────────┐
                │     User      │
                └───────┬───────┘
                        │
                        ▼
                ┌───────────────┐
                │ Authentication│
                └───────┬───────┘
                        │
          ┌─────────────┼─────────────┐
          ▼             ▼             ▼
      Learner         Earner       Educator
          │             │             │
          ▼             ▼             ▼
       Courses      Skill Profile   Courses
          │             │
          ▼             ▼
   Build Skills    Find Opportunities
                        │
                        ▼
                 Skill Matching
                        │
                        ▼
                  Apply / Review
                        │
                        ▼
                   Opportunity
````

---

# 6. Major Features

## Authentication & Authorization

* User registration.
* User login.
* Password-based authentication.
* JWT-based authentication.
* Role-based authorization.
* Protected API endpoints.

## Course Management

* Browse courses.
* View course details.
* Course creation by educators.
* Course management.
* Learner enrollment.

## Skill Management

* Skill library.
* User skill profiles.
* Required opportunity skills.
* Skill-based comparison.

## Opportunity Management

Companies can create opportunities such as:

* Jobs.
* Internships.
* Freelance projects.
* Skill-based projects.

Users can browse and apply for suitable opportunities.

## Skill Matching

MythBreak compares:

**Candidate Skills ↔ Required Opportunity Skills**

The basic matching percentage is calculated as:

```text
Match Percentage =
(Number of matched required skills /
Total number of required skills) × 100
```

For example:

```text
Required Skills:
Java
Spring Boot
MySQL
Git

Candidate Skills:
Java
Spring Boot
MySQL

Match = 3 / 4 × 100
      = 75%
```

The system can also identify:

* Matched skills.
* Missing skills.
* Overall match percentage.

## Application Management

Users can apply for opportunities.

Companies can review applications and update application status.

---

# 7. Functional Modules

The project contains multiple functional modules.

### Module 1 – User Management

* Registration
* Login
* Authentication
* Role management
* Profile management

### Module 2 – Course Management

* Course creation
* Course listing
* Course details
* Enrollment
* Learning progress

### Module 3 – Skill Management

* Skill creation
* Skill listing
* User skills
* Required opportunity skills

### Module 4 – Opportunity Management

* Opportunity creation
* Opportunity listing
* Opportunity details
* Required skills

### Module 5 – Application Management

* Apply for opportunities
* View applications
* Application status management

### Module 6 – Skill Matching

* Compare candidate skills with required skills.
* Calculate match percentage.
* Identify matched skills.
* Identify missing skills.

---

# 8. System Architecture

MythBreak follows a layered architecture.

```text
┌─────────────────────────────┐
│       Frontend              │
│ HTML / CSS / JavaScript     │
└──────────────┬──────────────┘
               │ REST API
               ▼
┌─────────────────────────────┐
│       Controllers           │
│ REST API Endpoints          │
└──────────────┬──────────────┘
               ▼
┌─────────────────────────────┐
│         Services            │
│ Business Logic              │
└──────────────┬──────────────┘
               ▼
┌─────────────────────────────┐
│       Repositories          │
│ Spring Data JPA             │
└──────────────┬──────────────┘
               ▼
┌─────────────────────────────┐
│       MySQL Database        │
└─────────────────────────────┘

Cross-cutting components:

Security → JWT Authentication & Authorization
Validation → Input validation
Exception Handling → Centralized error handling
DTOs → Request/Response data transfer
Testing → JUnit + Mockito
```

---

# 9. Technology Stack

## Backend

* Java 21
* Spring Boot 3.2.5
* Spring Web
* Spring Data JPA
* Hibernate
* Spring Security
* JWT
* Jakarta Validation
* Maven

## Database

* MySQL 8
* MySQL Workbench

## Frontend

* HTML5
* CSS3
* JavaScript

## Testing

* JUnit 5
* Mockito
* Maven Test

## Development Tools

* IntelliJ IDEA
* Postman
* Git
* GitHub

---

# 10. Project Structure

```text
MythBreak/
│
├── README.md
├── statement.md
├── pom.xml
├── .gitignore
│
├── docs/
│   ├── architecture.png
│   ├── workflow.png
│   ├── use-case-diagram.png
│   ├── class-diagram.png
│   ├── sequence-diagram.png
│   └── er-diagram.png
│
├── database/
│   └── schema.sql
│
└── src/
    ├── main/
    │   ├── java/
    │   │   └── com/mythbreak/
    │   │       ├── config/
    │   │       ├── controller/
    │   │       ├── service/
    │   │       ├── repository/
    │   │       ├── entity/
    │   │       ├── dto/
    │   │       ├── security/
    │   │       ├── exception/
    │   │       └── enums/
    │   │
    │   └── resources/
    │       ├── application.properties
    │       ├── static/
    │       │   ├── css/
    │       │   ├── js/
    │       │   └── images/
    │       └── templates/
    │
    └── test/
        └── java/
            └── com/mythbreak/
```

---

# 11. Non-Functional Requirements

## 1. Security

* JWT-based authentication.
* Password protection.
* Role-based authorization.
* Protected API endpoints.
* Validation of user input.

## 2. Performance

* Layered service architecture.
* Efficient database operations using Spring Data JPA.
* Avoid unnecessary database operations.
* REST-based communication.

## 3. Maintainability

* Separation of controller, service, repository, and entity layers.
* Modular package structure.
* Reusable business logic.
* Dedicated matching service.

## 4. Reliability

* Centralized exception handling.
* Input validation.
* Database constraints.
* Unit testing of important business logic.

## 5. Usability

* Simple navigation.
* Role-specific functionality.
* Clear course and opportunity information.
* Clear skill matching information.

## 6. Scalability

* Modular architecture.
* Separation of business logic and data access.
* REST APIs allow future frontend or mobile integration.

---

# 12. Security Design

The application uses Spring Security with JWT authentication.

Authentication flow:

```text
User
 │
 ▼
Login
 │
 ▼
AuthController
 │
 ▼
AuthService
 │
 ▼
Authentication
 │
 ▼
JWT Token
 │
 ▼
Client
 │
 ▼
Request + JWT
 │
 ▼
JwtAuthenticationFilter
 │
 ▼
Authorization
 │
 ▼
Protected API
```

Different roles receive access to their respective resources.

---

# 13. Database

MythBreak uses MySQL as its relational database.

Major entities include:

* User
* Skill
* Course
* Opportunity
* Application
* Enrollment
* Notification

Relationships between these entities are represented in the ER diagram included in the `docs` directory.

---

# 14. Testing

The project uses JUnit 5 and Mockito for testing.

The implemented test suites cover important service-layer functionality including:

* Authentication.
* Skill management.
* Application management.
* Skill matching.

Current test verification:

```text
ApplicationServiceTest   4/4
AuthServiceTest          3/3
MatchingServiceTest      9/9
SkillServiceTest         4/4

Total                    20/20
```

Build verification:

```text
mvn clean package
```

The project successfully compiles and packages into a Spring Boot JAR.

---

# 15. Error Handling

The application uses centralized exception handling for common situations such as:

* Invalid input.
* User not found.
* Course not found.
* Opportunity not found.
* Duplicate applications.
* Unauthorized access.
* Invalid operations.

This helps provide consistent API responses and improves reliability.

---

# 16. Expected Outcome

The completed system demonstrates how a skill-based platform can connect:

```text
Learning
   ↓
Skill Development
   ↓
Profile / Portfolio
   ↓
Opportunity Discovery
   ↓
Skill Matching
   ↓
Application
   ↓
Opportunity
```

The project demonstrates practical implementation of Java, Spring Boot, REST APIs, database management, authentication, authorization, business logic, and automated testing.

---

# 17. Future Enhancements

Possible future enhancements include:

* AI-powered career recommendations.
* Personalized learning paths.
* Resume analysis.
* Advanced candidate ranking based on multiple factors.
* Skill-gap recommendations.
* Notifications and email alerts.
* Portfolio/project verification.
* Advanced analytics.
* Mobile application.
* Integration with external job and learning platforms.

---

# 18. Academic Relevance

MythBreak is designed as a real-world software application demonstrating:

* Object-Oriented Programming.
* Java programming.
* Modular software design.
* REST API development.
* Database management.
* Authentication and authorization.
* CRUD operations.
* Business logic implementation.
* Testing and validation.
* Version control using Git.

---

# 19. How to Run the Project

## Prerequisites

Install:

* Java 21
* Maven
* MySQL 8
* IntelliJ IDEA or another Java IDE
* Git

## Step 1 – Clone Repository

```bash
git clone <YOUR_GITHUB_REPOSITORY_URL>
cd MythBreak
```

## Step 2 – Configure MySQL

Create a MySQL database:

```sql
CREATE DATABASE mythbreak;
```

Configure the database credentials in:

```text
src/main/resources/application.properties
```

Do not commit real database passwords or secret keys to GitHub.

## Step 3 – Build Project

```bash
mvn clean package
```

## Step 4 – Run Application

```bash
mvn spring-boot:run
```

The backend runs on:

```text
http://localhost:8080
```

## Step 5 – Open Frontend

Open:

```text
http://localhost:8080/
```

---

# 20. Testing Instructions

Run:

```bash
mvn test
```

For a complete build:

```bash
mvn clean package
```

---

# 21. Design Documentation

The `docs/` folder contains the project's design artefacts:

* System Architecture Diagram
* Workflow Diagram
* Use Case Diagram
* Class Diagram
* Sequence Diagram
* ER Diagram

These diagrams explain the structure, behavior, workflow, and database design of MythBreak.

---

# 22. Project Status

**Project Type:** Academic / Educational Project

**Architecture:** Layered Modular Monolith

**Backend:** Spring Boot

**Database:** MySQL

**Frontend:** HTML, CSS, JavaScript

**Testing:** JUnit 5 + Mockito

**Version Control:** Git + GitHub

---

## MythBreak

### 💥 Break Myths. 🛠️ Build Skills. 📚 Learn. 🚀 Apply. 💰 Earn.

````

---

# 2. `statement.md`

The college specifically asks `statement.md` to contain the **problem statement, scope, target users and high-level features**. :contentReference[oaicite:2]{index=2}

Put this in `statement.md`:

```markdown
# MythBreak – Project Statement

## 1. Project Title

# MythBreak

### 💥 Break Myths. 🛠️ Build Skills. 📚 Learn. 🚀 Apply. 💰 Earn.

---

## 2. Problem Statement

Students and freshers often learn skills from multiple sources but face difficulties in converting those skills into practical opportunities.

There is often a disconnect between:

- Learning a skill.
- Developing practical capability.
- Showcasing the skill.
- Finding relevant opportunities.
- Understanding whether a candidate's skills match an opportunity.

Companies also face difficulties in identifying candidates with the skills required for their opportunities.

MythBreak addresses this gap by providing a unified skill-based platform connecting learners, earners, educators, and companies.

---

## 3. Project Scope

The scope of MythBreak includes the development of a web-based platform that supports:

- User registration and authentication.
- Role-based access.
- Course discovery and management.
- Skill management.
- User skill profiles.
- Opportunity creation and discovery.
- Opportunity applications.
- Skill-based candidate matching.
- Application status management.
- Database-backed storage.
- Secure REST APIs.

The project focuses on demonstrating a complete software solution using Java and Spring Boot.

---

## 4. Target Users

### Learners

Students and individuals who want to acquire new skills through structured learning resources.

### Earners

Skilled individuals looking for jobs, internships, freelance projects, and other opportunities.

### Educators

Individuals who create and provide courses and learning resources.

### Companies / Recruiters

Organizations looking for candidates with specific skills.

---

## 5. High-Level Features

### Authentication

- Registration.
- Login.
- JWT authentication.
- Role-based authorization.

### Course Management

- Browse courses.
- View course details.
- Create courses.
- Manage learning content.
- Enroll in courses.

### Skill Management

- Browse available skills.
- Add skills to user profiles.
- Define required skills for opportunities.
- Compare user skills with required skills.

### Opportunity Management

- Create opportunities.
- View opportunities.
- Define required skills.
- Browse available opportunities.

### Application Management

- Apply for opportunities.
- View submitted applications.
- Review applications.
- Update application status.

### Skill Matching

The platform calculates the percentage of required opportunity skills possessed by a candidate.

```text
Match Percentage =
Matched Required Skills / Total Required Skills × 100
````

The system can also identify matched and missing skills.

---

## 6. Core Workflow

```text
Learn
  ↓
Build Skills
  ↓
Create Profile
  ↓
Discover Opportunities
  ↓
Skill Matching
  ↓
Apply
  ↓
Review / Opportunity
```

---

## 7. Expected Outcome

The expected outcome is a functional web application that demonstrates how learning, skill development, and opportunity discovery can be integrated into a single platform.

The project also demonstrates practical application of:

* Java.
* Object-Oriented Programming.
* Spring Boot.
* REST APIs.
* Spring Data JPA.
* MySQL.
* Spring Security.
* JWT.
* Validation.
* Exception handling.
* Unit testing.
* Git and GitHub.

---

## 8. Project Motto

**Learn → Build Skills → Showcase → Apply → Earn**

### MythBreak

**Break the myth that learning alone is enough. Build skills. Show what you can do. Find where those skills create opportunities.**

````

---

## 3. Important: don't upload everything blindly

Before pushing to GitHub, **do not upload**:

```text
application.properties
````

if it contains your actual MySQL password or JWT secret.

Instead, we should make a safe version such as:

```text
application-example.properties
```

with:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/mythbreak
spring.datasource.username=YOUR_MYSQL_USERNAME
spring.datasource.password=YOUR_MYSQL_PASSWORD

# JWT secret
jwt.secret=YOUR_SECRET_KEY
```

And `.gitignore` should include:

```gitignore
target/
.idea/
*.iml
.classpath
.project
.settings/

application-local.properties
.env

*.log
```


