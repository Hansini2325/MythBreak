-- ============================================================
-- MythBreak Demo / Seed Data
-- Run AFTER schema.sql
-- Passwords are BCrypt hashes of "Password123"
-- ============================================================

USE mythbreak_db;

-- ============================================================
-- Users  (password = "Password123" BCrypt-encoded)
-- ============================================================
INSERT INTO users (first_name, last_name, email, password, role, enabled) VALUES
('Admin',   'MythBreak', 'admin@mythbreak.com',    '$2a$12$Y5d2VFxQvKJyH3n1jQHN5eExwMpbdqnOlXDuHj6sRF4Uc3kJv4Aam', 'ADMIN',    1),
('Priya',   'Sharma',    'priya@learner.com',       '$2a$12$Y5d2VFxQvKJyH3n1jQHN5eExwMpbdqnOlXDuHj6sRF4Uc3kJv4Aam', 'LEARNER',  1),
('Arjun',   'Nair',      'arjun@earner.com',        '$2a$12$Y5d2VFxQvKJyH3n1jQHN5eExwMpbdqnOlXDuHj6sRF4Uc3kJv4Aam', 'EARNER',   1),
('Deepa',   'Menon',     'deepa@earner.com',        '$2a$12$Y5d2VFxQvKJyH3n1jQHN5eExwMpbdqnOlXDuHj6sRF4Uc3kJv4Aam', 'EARNER',   1),
('Ravi',    'Kumar',     'ravi@educator.com',       '$2a$12$Y5d2VFxQvKJyH3n1jQHN5eExwMpbdqnOlXDuHj6sRF4Uc3kJv4Aam', 'EDUCATOR', 1),
('TechCorp','Recruiter', 'hr@techcorp.com',         '$2a$12$Y5d2VFxQvKJyH3n1jQHN5eExwMpbdqnOlXDuHj6sRF4Uc3kJv4Aam', 'COMPANY',  1),
('DesignHub','Recruit',  'hr@designhub.com',        '$2a$12$Y5d2VFxQvKJyH3n1jQHN5eExwMpbdqnOlXDuHj6sRF4Uc3kJv4Aam', 'COMPANY',  1);

-- ============================================================
-- Skills Master List
-- ============================================================
INSERT INTO skills (name, category) VALUES
-- Programming
('Java', 'Programming'),
('Python', 'Programming'),
('JavaScript', 'Programming'),
('SQL', 'Programming'),
('Spring Boot', 'Programming'),
('React', 'Programming'),
('Node.js', 'Programming'),
('Git', 'Programming'),
-- Data & AI
('Machine Learning', 'Data & AI'),
('Data Analysis', 'Data & AI'),
-- Design
('Graphic Design', 'Design'),
('UI/UX Design', 'Design'),
('Figma', 'Design'),
-- Creative
('Photography', 'Creative'),
('Video Editing', 'Creative'),
('Content Writing', 'Creative'),
-- Marketing
('Digital Marketing', 'Marketing'),
('SEO', 'Marketing'),
('Social Media Marketing', 'Marketing'),
-- Soft Skills
('Public Speaking', 'Soft Skills'),
('Project Management', 'Soft Skills'),
('Communication', 'Soft Skills'),
-- Music & Arts
('Music', 'Arts'),
('Dance', 'Arts');

-- ============================================================
-- Profiles
-- ============================================================
INSERT INTO learner_profiles (user_id, bio, city, country) VALUES
(2, 'Aspiring software engineer learning Java and Spring Boot.', 'Bangalore', 'India');

INSERT INTO earner_profiles (user_id, bio, city, country, years_of_experience) VALUES
(3, 'Full-stack developer with 3 years experience in Java and React.', 'Hyderabad', 'India', 3),
(4, 'Graphic designer and UI/UX specialist with 2 years experience.', 'Mumbai', 'India', 2);

INSERT INTO educator_profiles (user_id, bio, city, country, expertise, qualification, years_of_experience) VALUES
(5, 'Experienced software trainer specializing in Java and Spring Boot.', 'Chennai', 'India',
   'Java, Spring Boot, Microservices', 'M.Tech Computer Science', 8);

INSERT INTO company_profiles (user_id, company_name, description, industry, website, city, country, company_size) VALUES
(6, 'TechCorp Solutions', 'Leading technology solutions company.', 'Information Technology', 'https://techcorp.example.com', 'Bangalore', 'India', 500),
(7, 'DesignHub Creative', 'Creative design agency specializing in branding.', 'Design & Creative', 'https://designhub.example.com', 'Mumbai', 'India', 50);

-- ============================================================
-- Earner Skills
-- ============================================================
-- Arjun has: Java, Spring Boot, Python, SQL, Git, React
INSERT INTO earner_skills (earner_profile_id, skill_id) VALUES
(1, 1), -- Java
(1, 2), -- Python
(1, 4), -- SQL
(1, 5), -- Spring Boot
(1, 8), -- Git
(1, 6); -- React

-- Deepa has: Graphic Design, UI/UX Design, Figma, Photoshop
INSERT INTO earner_skills (earner_profile_id, skill_id) VALUES
(2, 11), -- Graphic Design
(2, 12), -- UI/UX Design
(2, 13); -- Figma

-- ============================================================
-- Courses
-- ============================================================
INSERT INTO courses (educator_profile_id, title, description, price, level, category, published) VALUES
(1, 'Java Spring Boot Masterclass',
   'Complete beginner to advanced guide to Spring Boot 3.x with real projects.',
   1999.00, 'BEGINNER', 'Programming', 1),
(1, 'Full Stack Java + React',
   'Build complete web applications using Java backend and React frontend.',
   2499.00, 'INTERMEDIATE', 'Programming', 1),
(1, 'Introduction to Machine Learning',
   'Learn ML concepts with Python from scratch. No prior AI experience needed.',
   1499.00, 'BEGINNER', 'Data & AI', 1);

-- ============================================================
-- Course Content
-- ============================================================
INSERT INTO course_contents (course_id, title, description, content_url, content_type, sequence_order, duration_minutes) VALUES
(1, 'Introduction to Spring Boot', 'Overview of Spring Boot and its ecosystem.', 'https://example.com/sb-intro', 'VIDEO', 1, 30),
(1, 'Setting Up the Project', 'Create your first Spring Boot project with Maven.', 'https://example.com/sb-setup', 'VIDEO', 2, 45),
(1, 'Building REST APIs', 'Create full CRUD REST APIs with Spring Web.', 'https://example.com/sb-api', 'VIDEO', 3, 60),
(1, 'Spring Security & JWT', 'Secure your API with JWT authentication.', 'https://example.com/sb-security', 'VIDEO', 4, 60),
(2, 'React Fundamentals', 'Components, props, and state in React.', 'https://example.com/react-basics', 'VIDEO', 1, 45),
(3, 'What is Machine Learning?', 'Introduction to ML concepts and terminology.', 'https://example.com/ml-intro', 'VIDEO', 1, 30);

-- ============================================================
-- Opportunities (for skill matching demo)
-- ============================================================
INSERT INTO opportunities (company_profile_id, title, description, type, location, salary_range, deadline, status) VALUES
(1, 'Java Spring Boot Developer',
   'Looking for a Java Spring Boot developer to join our backend team. 2+ years experience required.',
   'JOB', 'Bangalore, India (Hybrid)', '8,00,000 - 12,00,000 INR/year',
   DATE_ADD(CURDATE(), INTERVAL 30 DAY), 'OPEN'),
(1, 'Backend Developer Intern',
   '6-month internship for students learning Java and Spring Boot.',
   'INTERNSHIP', 'Remote',
   '15,000 INR/month',
   DATE_ADD(CURDATE(), INTERVAL 45 DAY), 'OPEN'),
(2, 'UI/UX Designer',
   'Creative UI/UX designer needed for product design team.',
   'JOB', 'Mumbai, India', '6,00,000 - 10,00,000 INR/year',
   DATE_ADD(CURDATE(), INTERVAL 30 DAY), 'OPEN');

-- ============================================================
-- Opportunity Required Skills
-- ============================================================
-- Java Spring Boot Developer needs: Java, Spring Boot, SQL, Git
INSERT INTO opportunity_skills (opportunity_id, skill_id) VALUES
(1, 1), -- Java
(1, 5), -- Spring Boot
(1, 4), -- SQL
(1, 8); -- Git

-- Backend Intern needs: Java, SQL, Git
INSERT INTO opportunity_skills (opportunity_id, skill_id) VALUES
(2, 1), -- Java
(2, 4), -- SQL
(2, 8); -- Git

-- UI/UX Designer needs: UI/UX Design, Figma, Graphic Design
INSERT INTO opportunity_skills (opportunity_id, skill_id) VALUES
(3, 12), -- UI/UX Design
(3, 13), -- Figma
(3, 11); -- Graphic Design

-- ============================================================
-- Demo: Enrollment
-- ============================================================
INSERT INTO enrollments (learner_profile_id, course_id, progress_percent, status) VALUES
(1, 1, 50, 'ACTIVE');

-- ============================================================
-- Demo: Application (Arjun applied to Java Spring Boot Dev role)
-- Arjun matches: Java✓ Spring Boot✓ SQL✓ Git✓ → 100% match!
-- ============================================================
INSERT INTO applications (earner_profile_id, opportunity_id, status, cover_letter) VALUES
(1, 1, 'APPLIED', 'I am a passionate Java developer with 3 years of experience in Spring Boot and RESTful APIs. I look forward to contributing to your team.');
