
# MythBreak – Project Statement

## 💥 Break Myths. 🛠️ Build Skills. 📚 Learn. 🚀 Apply. 💰 Earn.

---

## 1. Project Title

# MythBreak

**Tagline:** Break Myths. Build Skills. Learn. Apply. Earn.

---

## 2. Problem Statement

Students and freshers often acquire technical and professional skills from multiple learning platforms but face significant difficulties converting those skills into practical career opportunities.

There is a structural disconnect between:

* **Learning a skill:** Completing courses without practical application.
* **Developing capability:** Lacking real-world project experience.
* **Showcasing skills:** Ineffective presentation of candidate capabilities.
* **Finding opportunities:** Difficulty locating jobs, internships, or projects aligned with existing skill sets.
* **Matching suitability:** Inability for candidates to quickly identify missing skill gaps for a specific role.

At the same time, companies and recruiters struggle to discover candidates whose specific skill sets match the technical requirements of open internships, jobs, and freelance projects.

MythBreak addresses this gap by providing a unified, skill-centric platform connecting learners, earners, educators, and organizations.

---

## 3. Project Scope

The scope of MythBreak includes the development of a full-stack, web-based software application supporting:

* **Identity Management:** User registration, secure login, password hashing, and stateless JWT authentication.
* **Role-Based Access Control:** Fine-grained authorization across four primary roles (Learner, Earner, Educator, Company).
* **Course Management:** Content creation, metadata management, and course enrollment pipelines.
* **Skill Catalog & Profiling:** Standardized skill directory and dynamic user skill profiles.
* **Opportunity Discovery:** Publishing and browsing jobs, internships, and freelance projects with specific required skill tags.
* **Automated Skill Matching:** Algorithmic calculation of candidate skill fit percentage and missing skill gap analysis.
* **Application Management:** Application submission, tracking, and recruiter status updates.
* **Persistence & APIs:** Relational database schema execution with MySQL and RESTful API endpoints built using Java Spring Boot.

---

## 4. Target Users

MythBreak accommodates four distinct platform actors:

### 👨‍🎓 Learner
Students and beginners seeking structured skill development through educational content and courses.

### 💼 Earner
Skilled individuals looking to showcase their capabilities and find matching jobs, internships, and freelance opportunities.

### 👨‍🏫 Educator
Instructors and content creators providing courses and learning resources to platform users.

### 🏢 Company / Recruiter
Organizations looking to publish opportunities, review candidates, and evaluate candidates through skill-match analytics.

---

## 5. High-Level Features

### Authentication & Authorization
* User registration and login interfaces
* BCrypt password encryption
* Stateless JWT session validation
* Role-based access restrictions on API routes

### Course Management
* Catalog browsing and detailed views
* Instructor course publishing
* Course enrollment tracking

### Skill Management
* Centralized skill library
* User profile skill tagging
* Opportunity skill requirement mapping

### Opportunity Management
* Job, internship, and project listings
* Detailed skill requirement definitions
* Filtering and opportunity search

### Skill Matching Engine
Calculates candidate suitability using required skill coverage:

$$\text{Match Percentage} = \left( \frac{\text{Matched Required Skills}}{\text{Total Required Skills}} \right) \times 100$$

* Identifies matching candidate skills
* Highlights missing required skills

### Application Management
* Direct opportunity application workflows
* Recruiter candidate review interfaces
* Application pipeline status updates

---

## 6. Core Workflow

```text
               ┌──────────────┐
               │    Learn     │
               └──────┬───────┘
                      │
                      ▼
               ┌──────────────┐
               │ Build Skills │
               └──────┬───────┘
                      │
                      ▼
               ┌──────────────┐
               │ Show Profile │
               └──────┬───────┘
                      │
                      ▼
               ┌──────────────┐
               │ Find Match   │
               └──────┬───────┘
                      │
                      ▼
               ┌──────────────┐
               │    Apply     │
               └──────┬───────┘
                      │
                      ▼
               ┌──────────────┐
               │  Earn / Job  │
               └──────────────┘

```

---

## 7. Expected Outcome

The expected outcome is a fully functional, tested, database-backed web application demonstrating how learning, skill building, and recruitment can be integrated into a single ecosystem.

The project validates key software engineering competencies:

* **Object-Oriented Programming:** Modular, scalable Java architecture.
* **Enterprise Frameworks:** Spring Boot 3, Spring Security, Spring Data JPA.
* **REST Services:** Statless API design and structured JSON responses.
* **Relational Storage:** Relational database normalization, indexing, and foreign keys using MySQL 8.
* **Automated Verification:** Unit testing and mock validation via JUnit 5 and Mockito.
* **Software Design:** Complete UML and architectural documentation.

---

## 8. Project Motto

**Learn → Build Skills → Showcase → Apply → Earn**

### MythBreak

> *Break the myth that learning alone is enough. Build skills. Show what you can do. Find where those skills create real opportunities.*

```

<FollowUp label="Shall we generate the 6 design diagrams for the docs/ folder now?" query="Generate the Mermaid and PlantUML scripts for the 6 required diagrams in the docs/ folder (Architecture, Workflow, Use Case, Class, Sequence, and ER diagrams)."/>

```
