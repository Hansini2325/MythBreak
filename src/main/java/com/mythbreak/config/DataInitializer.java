package com.mythbreak.config;

import com.mythbreak.entity.*;
import com.mythbreak.enums.*;
import com.mythbreak.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements ApplicationRunner {

    private final UserRepository userRepository;
    private final SkillRepository skillRepository;
    private final EarnerProfileRepository earnerProfileRepository;
    private final LearnerProfileRepository learnerProfileRepository;
    private final EducatorProfileRepository educatorProfileRepository;
    private final CompanyProfileRepository companyProfileRepository;
    private final CourseRepository courseRepository;
    private final OpportunityRepository opportunityRepository;
    private final ApplicationRepository applicationRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        if (userRepository.count() > 0) {
            log.info("Demo data already present. Skipping initialization.");
            return;
        }

        log.info("Seeding demo data...");

        Map<String, Skill> skills = seedSkills();
        seedAdmin();
        List<User> learnerUsers = seedLearners();
        List<EarnerProfile> earnerProfiles = seedEarners(skills);
        List<EducatorProfile> educatorProfiles = seedEducators();
        List<CompanyProfile> companyProfiles = seedCompanies();
        List<Course> courses = seedCourses(educatorProfiles, skills);
        List<Opportunity> opps = seedOpportunities(companyProfiles, skills);
        seedApplications(earnerProfiles, opps);
        seedEnrollments(learnerUsers, courses);

        log.info("Demo data seeding complete.");
    }

    private Map<String, Skill> seedSkills() {
        Map<String, Skill> map = new LinkedHashMap<>();

        String[][] skillData = {
            {"Java", "Programming"},
            {"Python", "Programming"},
            {"C++", "Programming"},
            {"JavaScript", "Web"},
            {"HTML", "Web"},
            {"CSS", "Web"},
            {"React", "Web"},
            {"Spring Boot", "Programming"},
            {"MySQL", "Database"},
            {"Git", "Tools"},
            {"GitHub", "Tools"},
            {"REST API", "Programming"},
            {"SQL", "Database"},
            {"Data Structures", "Programming"},
            {"Machine Learning", "Data Science"},
            {"Artificial Intelligence", "Data Science"},
            {"Communication", "Soft Skills"},
            {"Problem Solving", "Soft Skills"},
            {"UI/UX Design", "Design"},
            {"Figma", "Design"},
            {"Data Analysis", "Data Science"}
        };

        for (String[] s : skillData) {
            String name = s[0];
            String category = s[1];
            Skill skill = skillRepository
                .findByNameIgnoreCaseAndCategoryIgnoreCase(name, category)
                .orElseGet(() -> skillRepository.save(
                    Skill.builder().name(name).category(category).build()
                ));
            map.put(name, skill);
        }

        log.info("Seeded {} skills.", map.size());
        return map;
    }

    private void seedAdmin() {
        if (!userRepository.existsByEmail("admin@mythbreak.com")) {
            userRepository.save(User.builder()
                .firstName("Admin")
                .lastName("MythBreak")
                .email("admin@mythbreak.com")
                .password(passwordEncoder.encode("Admin@1234"))
                .role(Role.ADMIN)
                .enabled(true)
                .build());
        }
    }

    private List<User> seedLearners() {
        List<User> learners = new ArrayList<>();

        String[][] learnerData = {
            {"Priya", "Sharma", "priya.sharma@example.com"},
            {"Rohan", "Mehta", "rohan.mehta@example.com"},
            {"Anjali", "Nair", "anjali.nair@example.com"}
        };

        for (String[] d : learnerData) {
            if (!userRepository.existsByEmail(d[2])) {
                User u = userRepository.save(User.builder()
                    .firstName(d[0]).lastName(d[1]).email(d[2])
                    .password(passwordEncoder.encode("Learner@1234"))
                    .role(Role.LEARNER).enabled(true).build());

                learnerProfileRepository.save(LearnerProfile.builder()
                    .user(u)
                    .bio("Passionate learner eager to build technical skills.")
                    .city("Bengaluru").country("India")
                    .build());

                learners.add(u);
            }
        }

        log.info("Seeded {} learners.", learners.size());
        return learners;
    }

    private List<EarnerProfile> seedEarners(Map<String, Skill> skills) {
        List<EarnerProfile> profiles = new ArrayList<>();

        record EarnerData(String first, String last, String email, String bio,
                          String city, int exp, String[] skillNames) {}

        List<EarnerData> earners = List.of(
            new EarnerData("Karthik", "Rajan", "karthik.rajan@example.com",
                "Full-stack Java developer with strong backend experience.",
                "Chennai", 3,
                new String[]{"Java", "Spring Boot", "MySQL", "Git", "REST API"}),

            new EarnerData("Divya", "Krishnan", "divya.krishnan@example.com",
                "Frontend developer skilled in React and modern web technologies.",
                "Hyderabad", 2,
                new String[]{"JavaScript", "React", "HTML", "CSS", "Git", "Figma"}),

            new EarnerData("Arjun", "Patel", "arjun.patel@example.com",
                "Data science enthusiast with Python and ML background.",
                "Pune", 1,
                new String[]{"Python", "Data Analysis", "Machine Learning", "SQL", "Git"})
        );

        for (EarnerData ed : earners) {
            if (!userRepository.existsByEmail(ed.email())) {
                User u = userRepository.save(User.builder()
                    .firstName(ed.first()).lastName(ed.last()).email(ed.email())
                    .password(passwordEncoder.encode("Earner@1234"))
                    .role(Role.EARNER).enabled(true).build());

                Set<Skill> earnerSkills = new HashSet<>();
                for (String sn : ed.skillNames()) {
                    if (skills.containsKey(sn)) earnerSkills.add(skills.get(sn));
                }

                EarnerProfile ep = earnerProfileRepository.save(EarnerProfile.builder()
                    .user(u).bio(ed.bio()).city(ed.city()).country("India")
                    .yearsOfExperience(ed.exp())
                    .githubUrl("https://github.com/" + ed.first().toLowerCase())
                    .skills(earnerSkills).build());

                profiles.add(ep);
            }
        }

        log.info("Seeded {} earner profiles.", profiles.size());
        return profiles;
    }

    private List<EducatorProfile> seedEducators() {
        List<EducatorProfile> profiles = new ArrayList<>();

        record EducatorData(String first, String last, String email,
                            String bio, String expertise, String qual, int exp) {}

        List<EducatorData> educators = List.of(
            new EducatorData("Dr. Suresh", "Kumar", "suresh.kumar@example.com",
                "Senior software engineer turned educator with 10+ years in backend development.",
                "Java, Spring Boot, System Design, REST API",
                "M.Tech Computer Science, IIT Madras", 10),

            new EducatorData("Meera", "Iyer", "meera.iyer@example.com",
                "UX designer and frontend architect passionate about accessible web design.",
                "UI/UX Design, Figma, React, CSS, HTML",
                "B.Des, NID Ahmedabad", 7)
        );

        for (EducatorData ed : educators) {
            if (!userRepository.existsByEmail(ed.email())) {
                User u = userRepository.save(User.builder()
                    .firstName(ed.first()).lastName(ed.last()).email(ed.email())
                    .password(passwordEncoder.encode("Educator@1234"))
                    .role(Role.EDUCATOR).enabled(true).build());

                EducatorProfile ep = educatorProfileRepository.save(EducatorProfile.builder()
                    .user(u).bio(ed.bio()).expertise(ed.expertise())
                    .qualification(ed.qual()).yearsOfExperience(ed.exp())
                    .city("Bengaluru").country("India").build());

                profiles.add(ep);
            }
        }

        log.info("Seeded {} educator profiles.", profiles.size());
        return profiles;
    }

    private List<CompanyProfile> seedCompanies() {
        List<CompanyProfile> profiles = new ArrayList<>();

        record CompanyData(String first, String last, String email,
                           String company, String desc, String industry, int size, String city) {}

        List<CompanyData> companies = List.of(
            new CompanyData("Ravi", "Menon", "ravi.menon@techsolutions.in",
                "TechSolutions India", "End-to-end software product company building enterprise solutions.",
                "Software", 250, "Bengaluru"),

            new CompanyData("Neha", "Gupta", "neha.gupta@databridge.io",
                "DataBridge Analytics", "Data-driven company focused on ML solutions and business intelligence.",
                "Analytics & AI", 80, "Hyderabad")
        );

        for (CompanyData cd : companies) {
            if (!userRepository.existsByEmail(cd.email())) {
                User u = userRepository.save(User.builder()
                    .firstName(cd.first()).lastName(cd.last()).email(cd.email())
                    .password(passwordEncoder.encode("Company@1234"))
                    .role(Role.COMPANY).enabled(true).build());

                CompanyProfile cp = companyProfileRepository.save(CompanyProfile.builder()
                    .user(u).companyName(cd.company()).description(cd.desc())
                    .industry(cd.industry()).companySize(cd.size())
                    .city(cd.city()).country("India").build());

                profiles.add(cp);
            }
        }

        log.info("Seeded {} company profiles.", profiles.size());
        return profiles;
    }

    private List<Course> seedCourses(List<EducatorProfile> educators, Map<String, Skill> skills) {
        List<Course> courses = new ArrayList<>();

        if (educators.size() < 2 || courseRepository.count() > 0) return courses;

        EducatorProfile techEd = educators.get(0);
        EducatorProfile designEd = educators.size() > 1 ? educators.get(1) : educators.get(0);

        record CourseData(String title, String desc, String level, String category,
                          BigDecimal price, EducatorProfile educator) {}

        List<CourseData> courseList = List.of(
            new CourseData("Java Programming Fundamentals",
                "Master core Java concepts including OOP, collections, generics, exception handling, and I/O. Build real-world console and web applications step by step.",
                "BEGINNER", "Programming", new BigDecimal("0.00"), techEd),

            new CourseData("Data Structures and Algorithms",
                "Deep dive into arrays, linked lists, trees, graphs, sorting, and searching. Prepare for technical interviews and improve your problem-solving skills.",
                "INTERMEDIATE", "Programming", new BigDecimal("499.00"), techEd),

            new CourseData("Spring Boot Backend Development",
                "Build production-ready REST APIs using Spring Boot 3, Spring Security, JPA, and MySQL. Learn best practices for backend architecture.",
                "INTERMEDIATE", "Programming", new BigDecimal("799.00"), techEd),

            new CourseData("SQL and Database Management",
                "Learn relational database design, SQL queries, joins, indexing, and transactions. Includes MySQL workbench practice and real-world schema design.",
                "BEGINNER", "Database", new BigDecimal("0.00"), techEd),

            new CourseData("Web Development Fundamentals",
                "Build modern websites from scratch using HTML5, CSS3, and JavaScript. Understand the DOM, events, fetch API, and responsive design principles.",
                "BEGINNER", "Web", new BigDecimal("0.00"), designEd),

            new CourseData("Machine Learning Fundamentals",
                "Introduction to supervised and unsupervised learning, regression, classification, clustering, and neural networks using Python and scikit-learn.",
                "INTERMEDIATE", "Data Science", new BigDecimal("999.00"), techEd),

            new CourseData("Git and GitHub for Developers",
                "Master version control with Git. Learn branching, merging, pull requests, and collaborative development workflows used by professional teams.",
                "BEGINNER", "Tools", new BigDecimal("0.00"), techEd),

            new CourseData("UI/UX Design Fundamentals",
                "Learn user research, wireframing, prototyping, and visual design principles. Practice with Figma to create professional-grade design systems.",
                "BEGINNER", "Design", new BigDecimal("599.00"), designEd)
        );

        for (CourseData cd : courseList) {
            Course course = courseRepository.save(Course.builder()
                .educator(cd.educator())
                .title(cd.title())
                .description(cd.desc())
                .level(cd.level())
                .category(cd.category())
                .price(cd.price())
                .published(true)
                .build());
            courses.add(course);
        }

        log.info("Seeded {} courses.", courses.size());
        return courses;
    }

    private List<Opportunity> seedOpportunities(List<CompanyProfile> companies, Map<String, Skill> skills) {
        List<Opportunity> opps = new ArrayList<>();

        if (companies.size() < 2 || opportunityRepository.count() > 0) return opps;

        CompanyProfile techCo = companies.get(0);
        CompanyProfile dataCo = companies.size() > 1 ? companies.get(1) : companies.get(0);

        record OppData(String title, String desc, OpportunityType type,
                       String location, String salary, LocalDate deadline,
                       CompanyProfile company, String[] skillNames) {}

        List<OppData> oppList = List.of(
            new OppData("Backend Java Developer",
                "We are looking for a backend developer with solid Java and Spring Boot experience to join our platform team. You will design and build REST APIs, integrate with MySQL databases, and contribute to code reviews.",
                OpportunityType.JOB, "Bengaluru, India", "6–10 LPA",
                LocalDate.now().plusMonths(2), techCo,
                new String[]{"Java", "Spring Boot", "MySQL", "Git", "REST API"}),

            new OppData("Internship — Java Developer",
                "6-month internship for final-year students. You will work alongside senior developers on real features, write unit tests, and learn agile development practices.",
                OpportunityType.INTERNSHIP, "Bengaluru, India (Hybrid)", "Stipend: ₹15,000/month",
                LocalDate.now().plusMonths(1), techCo,
                new String[]{"Java", "Spring Boot", "MySQL", "Git"}),

            new OppData("Full-Stack Web Developer",
                "Build and maintain web applications using React on the frontend and Spring Boot on the backend. Experience with REST APIs and SQL databases required.",
                OpportunityType.JOB, "Remote", "8–14 LPA",
                LocalDate.now().plusMonths(3), techCo,
                new String[]{"Java", "JavaScript", "React", "Spring Boot", "SQL", "REST API"}),

            new OppData("Freelance — Frontend Developer",
                "Short-term project to redesign the company's internal dashboard. Looking for a developer skilled in React, CSS, and Figma who can deliver in 4–6 weeks.",
                OpportunityType.FREELANCE, "Remote", "Project-based: ₹40,000",
                LocalDate.now().plusWeeks(3), techCo,
                new String[]{"JavaScript", "React", "HTML", "CSS", "Figma"}),

            new OppData("Data Science Intern",
                "6-month internship focused on building ML models for customer segmentation and churn prediction. Python and data analysis skills are essential.",
                OpportunityType.INTERNSHIP, "Hyderabad, India", "Stipend: ₹20,000/month",
                LocalDate.now().plusMonths(1), dataCo,
                new String[]{"Python", "Machine Learning", "Data Analysis", "SQL"}),

            new OppData("Machine Learning Engineer",
                "Join our AI team to develop and deploy production ML pipelines. Experience with model training, evaluation, and Python ML ecosystem required.",
                OpportunityType.JOB, "Hyderabad, India", "12–20 LPA",
                LocalDate.now().plusMonths(2), dataCo,
                new String[]{"Python", "Machine Learning", "Artificial Intelligence", "SQL", "Data Analysis"}),

            new OppData("Data Analyst",
                "Analyse sales, user behaviour, and product metrics data. Create dashboards and reports to support business decisions. SQL and data analysis skills required.",
                OpportunityType.JOB, "Hyderabad, India (Hybrid)", "5–8 LPA",
                LocalDate.now().plusMonths(2), dataCo,
                new String[]{"SQL", "Data Analysis", "Python", "Communication"}),

            new OppData("UI/UX Designer",
                "Create wireframes, prototypes, and final designs for mobile and web applications. Design user research documentation and collaborate with the development team.",
                OpportunityType.JOB, "Remote", "7–12 LPA",
                LocalDate.now().plusMonths(2), techCo,
                new String[]{"UI/UX Design", "Figma", "Communication", "Problem Solving"}),

            new OppData("Open Source Contribution Project — Spring Boot Module",
                "Contribute to an internal open-source library for Spring Boot utilities. Implementation includes writing tests, documentation, and code review participation.",
                OpportunityType.PROJECT, "Remote", "Certificate + Recognition",
                LocalDate.now().plusMonths(1), techCo,
                new String[]{"Java", "Spring Boot", "Git", "GitHub", "REST API"}),

            new OppData("Freelance — Python Data Pipeline",
                "Build a data ingestion and transformation pipeline in Python. Involves connecting to APIs, processing CSV/JSON data, and loading into MySQL.",
                OpportunityType.FREELANCE, "Remote", "Project-based: ₹25,000",
                LocalDate.now().plusWeeks(4), dataCo,
                new String[]{"Python", "SQL", "MySQL", "Data Analysis", "REST API"})
        );

        for (OppData od : oppList) {
            Set<Skill> oppSkills = new HashSet<>();
            for (String sn : od.skillNames()) {
                if (skills.containsKey(sn)) oppSkills.add(skills.get(sn));
            }

            Opportunity opp = opportunityRepository.save(Opportunity.builder()
                .company(od.company())
                .title(od.title())
                .description(od.desc())
                .type(od.type())
                .location(od.location())
                .salaryRange(od.salary())
                .deadline(od.deadline())
                .status(OpportunityStatus.OPEN)
                .requiredSkills(oppSkills)
                .build());

            opps.add(opp);
        }

        log.info("Seeded {} opportunities.", opps.size());
        return opps;
    }

    private void seedApplications(List<EarnerProfile> earners, List<Opportunity> opps) {
        if (earners.isEmpty() || opps.isEmpty()) return;

        record AppData(int earnerIdx, int oppIdx, ApplicationStatus status, String coverLetter) {}

        List<AppData> apps = List.of(
            new AppData(0, 0, ApplicationStatus.APPLIED,
                "I have 3 years of hands-on Java and Spring Boot experience. I built RESTful microservices at my previous company and am excited to contribute to your platform team."),
            new AppData(0, 1, ApplicationStatus.SHORTLISTED,
                "As a recent graduate with internship experience in Java and Spring Boot, I am eager to apply my skills in a structured team environment."),
            new AppData(0, 8, ApplicationStatus.APPLIED,
                "I would love to contribute to your open-source Spring Boot module. I have experience with unit testing and Git-based collaboration workflows."),
            new AppData(1, 3, ApplicationStatus.APPLIED,
                "I am a React developer with a strong design sense. I can deliver a clean, modern dashboard redesign within the 6-week timeline."),
            new AppData(1, 2, ApplicationStatus.SELECTED,
                "I have built full-stack applications using React and Spring Boot. I am comfortable with both sides of the stack and ready to take ownership of features."),
            new AppData(2, 4, ApplicationStatus.APPLIED,
                "Data science internship aligns perfectly with my background in Python, ML, and SQL. I am enthusiastic about contributing to real predictive modelling projects.")
        );

        for (AppData ad : apps) {
            if (ad.earnerIdx() >= earners.size() || ad.oppIdx() >= opps.size()) continue;

            EarnerProfile earner = earners.get(ad.earnerIdx());
            Opportunity opp = opps.get(ad.oppIdx());

            if (!applicationRepository.existsByEarnerIdAndOpportunityId(earner.getId(), opp.getId())) {
                applicationRepository.save(Application.builder()
                    .earner(earner)
                    .opportunity(opp)
                    .status(ad.status())
                    .coverLetter(ad.coverLetter())
                    .build());
            }
        }

        log.info("Seeded applications.");
    }

    private void seedEnrollments(List<User> learnerUsers, List<Course> courses) {
        if (learnerUsers.isEmpty() || courses.isEmpty()) return;

        record EnrollData(int learnerIdx, int courseIdx, int progress, EnrollmentStatus status) {}

        List<EnrollData> enrollments = List.of(
            new EnrollData(0, 0, 100, EnrollmentStatus.COMPLETED),
            new EnrollData(0, 1, 65, EnrollmentStatus.ACTIVE),
            new EnrollData(0, 6, 100, EnrollmentStatus.COMPLETED),
            new EnrollData(1, 0, 40, EnrollmentStatus.ACTIVE),
            new EnrollData(1, 3, 80, EnrollmentStatus.ACTIVE),
            new EnrollData(2, 4, 30, EnrollmentStatus.ACTIVE),
            new EnrollData(2, 5, 50, EnrollmentStatus.ACTIVE)
        );

        for (EnrollData ed : enrollments) {
            if (ed.learnerIdx() >= learnerUsers.size() || ed.courseIdx() >= courses.size()) continue;

            User learnerUser = learnerUsers.get(ed.learnerIdx());
            Optional<LearnerProfile> lpOpt = learnerProfileRepository.findByUserId(learnerUser.getId());
            if (lpOpt.isEmpty()) continue;

            LearnerProfile lp = lpOpt.get();
            Course course = courses.get(ed.courseIdx());

            if (!enrollmentRepository.existsByLearnerIdAndCourseId(lp.getId(), course.getId())) {
                Enrollment enrollment = Enrollment.builder()
                    .learner(lp)
                    .course(course)
                    .progressPercent(ed.progress())
                    .status(ed.status())
                    .build();

                if (ed.status() == EnrollmentStatus.COMPLETED) {
                    enrollment.setCompletedAt(LocalDateTime.now().minusDays(7));
                }

                enrollmentRepository.save(enrollment);
            }
        }

        log.info("Seeded enrollments.");
    }
}
