-- ============================================================
-- MythBreak Database Schema
-- MySQL 8.x
-- Run this script ONCE to create the database before starting
-- the Spring Boot application.
-- ============================================================

-- Create and select database
CREATE DATABASE IF NOT EXISTS mythbreak_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE mythbreak_db;

-- ============================================================
-- 1. Users
-- ============================================================
CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(50)  NOT NULL,
    last_name  VARCHAR(50)  NOT NULL,
    email      VARCHAR(255) NOT NULL UNIQUE,
    password   VARCHAR(255) NOT NULL,
    role       ENUM('LEARNER','EARNER','EDUCATOR','COMPANY','ADMIN') NOT NULL,
    enabled    TINYINT(1)   NOT NULL DEFAULT 1,
    created_at DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_users_email (email),
    INDEX idx_users_role  (role)
);

-- ============================================================
-- 2. Skills (master list)
-- ============================================================
CREATE TABLE IF NOT EXISTS skills (
    id       BIGINT AUTO_INCREMENT PRIMARY KEY,
    name     VARCHAR(100) NOT NULL,
    category VARCHAR(100) NOT NULL,
    UNIQUE KEY uq_skill_name_category (name, category),
    INDEX idx_skills_category (category)
);

-- ============================================================
-- 3. Learner Profiles
-- ============================================================
CREATE TABLE IF NOT EXISTS learner_profiles (
    id                BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id           BIGINT  NOT NULL UNIQUE,
    bio               TEXT,
    phone_number      VARCHAR(20),
    city              VARCHAR(100),
    country           VARCHAR(100),
    profile_image_url VARCHAR(500),
    linkedin_url      VARCHAR(500),
    github_url        VARCHAR(500),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- ============================================================
-- 4. Earner Profiles
-- ============================================================
CREATE TABLE IF NOT EXISTS earner_profiles (
    id                  BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id             BIGINT NOT NULL UNIQUE,
    bio                 TEXT,
    phone_number        VARCHAR(20),
    city                VARCHAR(100),
    country             VARCHAR(100),
    profile_image_url   VARCHAR(500),
    linkedin_url        VARCHAR(500),
    github_url          VARCHAR(500),
    portfolio_url       VARCHAR(500),
    resume_url          VARCHAR(500),
    years_of_experience INT,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- ============================================================
-- 5. Educator Profiles
-- ============================================================
CREATE TABLE IF NOT EXISTS educator_profiles (
    id                  BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id             BIGINT NOT NULL UNIQUE,
    bio                 TEXT,
    phone_number        VARCHAR(20),
    city                VARCHAR(100),
    country             VARCHAR(100),
    profile_image_url   VARCHAR(500),
    linkedin_url        VARCHAR(500),
    website_url         VARCHAR(500),
    expertise           VARCHAR(1000),
    qualification       VARCHAR(500),
    years_of_experience INT,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- ============================================================
-- 6. Company Profiles
-- ============================================================
CREATE TABLE IF NOT EXISTS company_profiles (
    id           BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id      BIGINT       NOT NULL UNIQUE,
    company_name VARCHAR(200) NOT NULL,
    description  TEXT,
    industry     VARCHAR(200),
    website      VARCHAR(500),
    logo_url     VARCHAR(500),
    city         VARCHAR(100),
    country      VARCHAR(100),
    phone_number VARCHAR(20),
    company_size INT,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- ============================================================
-- 7. Earner ↔ Skills (Many-to-Many)
-- ============================================================
CREATE TABLE IF NOT EXISTS earner_skills (
    earner_profile_id BIGINT NOT NULL,
    skill_id          BIGINT NOT NULL,
    PRIMARY KEY (earner_profile_id, skill_id),
    FOREIGN KEY (earner_profile_id) REFERENCES earner_profiles(id) ON DELETE CASCADE,
    FOREIGN KEY (skill_id)          REFERENCES skills(id)           ON DELETE CASCADE
);

-- ============================================================
-- 8. Courses
-- ============================================================
CREATE TABLE IF NOT EXISTS courses (
    id                  BIGINT AUTO_INCREMENT PRIMARY KEY,
    educator_profile_id BIGINT         NOT NULL,
    title               VARCHAR(200)   NOT NULL,
    description         TEXT,
    price               DECIMAL(10,2),
    level               VARCHAR(50),
    category            VARCHAR(100),
    thumbnail_url       VARCHAR(500),
    published           TINYINT(1)     NOT NULL DEFAULT 0,
    created_at          DATETIME       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (educator_profile_id) REFERENCES educator_profiles(id) ON DELETE CASCADE,
    INDEX idx_courses_category  (category),
    INDEX idx_courses_published (published)
);

-- ============================================================
-- 9. Course Contents
-- ============================================================
CREATE TABLE IF NOT EXISTS course_contents (
    id               BIGINT AUTO_INCREMENT PRIMARY KEY,
    course_id        BIGINT        NOT NULL,
    title            VARCHAR(200)  NOT NULL,
    description      TEXT,
    content_url      VARCHAR(500),
    content_type     VARCHAR(50),
    sequence_order   INT           NOT NULL,
    duration_minutes INT,
    FOREIGN KEY (course_id) REFERENCES courses(id) ON DELETE CASCADE,
    INDEX idx_cc_course_seq (course_id, sequence_order)
);

-- ============================================================
-- 10. Enrollments
-- ============================================================
CREATE TABLE IF NOT EXISTS enrollments (
    id                BIGINT AUTO_INCREMENT PRIMARY KEY,
    learner_profile_id BIGINT NOT NULL,
    course_id          BIGINT NOT NULL,
    enrolled_at        DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    progress_percent   INT      NOT NULL DEFAULT 0,
    status             ENUM('ACTIVE','COMPLETED','CANCELLED') NOT NULL DEFAULT 'ACTIVE',
    completed_at       DATETIME,
    UNIQUE KEY uq_enrollment (learner_profile_id, course_id),
    FOREIGN KEY (learner_profile_id) REFERENCES learner_profiles(id) ON DELETE CASCADE,
    FOREIGN KEY (course_id)          REFERENCES courses(id)           ON DELETE CASCADE
);

-- ============================================================
-- 11. Projects (Earner Portfolio)
-- ============================================================
CREATE TABLE IF NOT EXISTS projects (
    id                BIGINT AUTO_INCREMENT PRIMARY KEY,
    earner_profile_id BIGINT       NOT NULL,
    title             VARCHAR(200) NOT NULL,
    description       TEXT,
    project_url       VARCHAR(500),
    github_url        VARCHAR(500),
    demo_url          VARCHAR(500),
    completion_date   DATE,
    FOREIGN KEY (earner_profile_id) REFERENCES earner_profiles(id) ON DELETE CASCADE
);

-- ============================================================
-- 12. Project ↔ Skills (Many-to-Many)
-- ============================================================
CREATE TABLE IF NOT EXISTS project_skills (
    project_id BIGINT NOT NULL,
    skill_id   BIGINT NOT NULL,
    PRIMARY KEY (project_id, skill_id),
    FOREIGN KEY (project_id) REFERENCES projects(id) ON DELETE CASCADE,
    FOREIGN KEY (skill_id)   REFERENCES skills(id)   ON DELETE CASCADE
);

-- ============================================================
-- 13. Opportunities
-- ============================================================
CREATE TABLE IF NOT EXISTS opportunities (
    id                 BIGINT AUTO_INCREMENT PRIMARY KEY,
    company_profile_id BIGINT       NOT NULL,
    title              VARCHAR(200) NOT NULL,
    description        TEXT,
    type               ENUM('JOB','INTERNSHIP','FREELANCE','PROJECT') NOT NULL,
    location           VARCHAR(200),
    salary_range       VARCHAR(100),
    deadline           DATE,
    status             ENUM('OPEN','CLOSED','FILLED') NOT NULL DEFAULT 'OPEN',
    created_at         DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (company_profile_id) REFERENCES company_profiles(id) ON DELETE CASCADE,
    INDEX idx_opp_status (status),
    INDEX idx_opp_type   (type)
);

-- ============================================================
-- 14. Opportunity ↔ Required Skills (Many-to-Many)
-- ============================================================
CREATE TABLE IF NOT EXISTS opportunity_skills (
    opportunity_id BIGINT NOT NULL,
    skill_id       BIGINT NOT NULL,
    PRIMARY KEY (opportunity_id, skill_id),
    FOREIGN KEY (opportunity_id) REFERENCES opportunities(id) ON DELETE CASCADE,
    FOREIGN KEY (skill_id)       REFERENCES skills(id)        ON DELETE CASCADE
);

-- ============================================================
-- 15. Applications
-- ============================================================
CREATE TABLE IF NOT EXISTS applications (
    id                BIGINT AUTO_INCREMENT PRIMARY KEY,
    earner_profile_id BIGINT NOT NULL,
    opportunity_id    BIGINT NOT NULL,
    applied_at        DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    status            ENUM('APPLIED','SHORTLISTED','REJECTED','SELECTED') NOT NULL DEFAULT 'APPLIED',
    cover_letter      TEXT,
    UNIQUE KEY uq_application (earner_profile_id, opportunity_id),
    FOREIGN KEY (earner_profile_id) REFERENCES earner_profiles(id) ON DELETE CASCADE,
    FOREIGN KEY (opportunity_id)    REFERENCES opportunities(id)    ON DELETE CASCADE
);

-- ============================================================
-- 16. Notifications
-- ============================================================
CREATE TABLE IF NOT EXISTS notifications (
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id    BIGINT     NOT NULL,
    title      VARCHAR(200) NOT NULL,
    message    TEXT         NOT NULL,
    type       ENUM('APPLICATION_SUBMITTED','APPLICATION_SHORTLISTED','APPLICATION_REJECTED',
                   'APPLICATION_SELECTED','COURSE_ENROLLED','COURSE_COMPLETED',
                   'OPPORTUNITY_POSTED','GENERAL') NOT NULL,
    is_read    TINYINT(1)   NOT NULL DEFAULT 0,
    created_at DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    INDEX idx_notif_user_read (user_id, is_read)
);
