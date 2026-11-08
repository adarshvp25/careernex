const AppState = {
    currentCompletedStep: 1, // Only Step 1 unlocked initially
    targetRole: "JAVA_BACKEND",
    resumeId: null,
    assessmentId: null,
    extractedSkills: [],
    verifiedSkills: [],
    skillGaps: [],
    quizAnswers: {},
    currentQuizQuestions: [],
    currentQuizIndex: 0,
    readinessScore: 0,

    // ROLE PREREQUISITES
    roles: {
        JAVA_BACKEND: {
            title: "Java Backend Developer",
            requiredSkills: ["Java", "Spring Boot", "MySQL", "REST APIs", "Git"]
        },
        FRONTEND: {
            title: "Frontend Developer",
            requiredSkills: ["JavaScript", "HTML", "CSS", "React", "Git"]
        },
        FULL_STACK: {
            title: "Full Stack Developer (MERN)",
            requiredSkills: ["JavaScript", "React", "Node.js", "Express", "MySQL"]
        }
    },

    // Role specific project catalogs
    roleProjects: {
        JAVA_BACKEND: [
            {
                id: 101,
                title: "Enterprise Candidate Management REST API",
                targetSkills: ["Spring Boot", "REST APIs", "MySQL"],
                description: "Build scalable CRUD REST endpoints using Spring Boot, JPA Hibernate, and MySQL."
            },
            {
                id: 102,
                title: "Git Version Control Workflow & CI Automation",
                targetSkills: ["Git", "Java"],
                description: "Set up multi-branch Git workflow and automated testing suite for Java applications."
            }
        ],

        FRONTEND: [
            {
                id: 201,
                title: "Interactive E-Commerce Product Dashboard",
                targetSkills: ["React", "JavaScript", "CSS"],
                description: "Build a responsive React web app with state management, custom CSS, and cart logic."
            },
            {
                id: 202,
                title: "Responsive Semantic Landing Page",
                targetSkills: ["HTML", "CSS", "Git"],
                description: "Create mobile-first accessible HTML5/CSS3 landing page and deploy via GitHub Pages."
            }
        ],

        FULL_STACK: [
            {
                id: 301,
                title: "MERN Stack Hospital Resource & Bed Tracker",
                targetSkills: ["React", "Node.js", "Express", "MySQL"],
                description: "Develop end-to-end full stack web app with JWT authentication and relational storage."
            }
        ]
    },

    // Role specific quiz questions
    roleQuestionPool: {
        JAVA_BACKEND: [
            {
                id: 101,
                skill: "Java",
                question: "Which keyword is used to inherit a class in Java?",
                options: ["implements", "extends", "inherits", "super"],
                correct: 1
            },
            {
                id: 102,
                skill: "Spring Boot",
                question: "Which annotation marks a REST controller in Spring Boot?",
                options: ["@Entity", "@RestController", "@Table", "@BeanOnly"],
                correct: 1
            },
            {
                id: 103,
                skill: "MySQL",
                question: "Which SQL command retrieves data?",
                options: ["GET", "SELECT", "OPEN", "FETCH"],
                correct: 1
            },
            {
                id: 104,
                skill: "REST APIs",
                question: "Which HTTP method commonly retrieves a resource?",
                options: ["POST", "GET", "DELETE", "PATCH"],
                correct: 1
            },
            {
                id: 105,
                skill: "Git",
                question: "Which command creates a commit?",
                options: ["git push", "git status", "git commit", "git fetch"],
                correct: 2
            }
        ],

        FRONTEND: [
            {
                id: 201,
                skill: "JavaScript",
                question: "Which method converts a JSON string into a JavaScript object?",
                options: ["JSON.stringify()", "JSON.parse()", "Object.parse()", "JSON.toObject()"],
                correct: 1
            },
            {
                id: 202,
                skill: "HTML",
                question: "Which HTML5 element is used for standalone self-contained content?",
                options: ["<section>", "<article>", "<aside>", "<div>"],
                correct: 1
            },
            {
                id: 203,
                skill: "CSS",
                question: "Which CSS Flexbox property aligns items along the main axis?",
                options: ["align-items", "justify-content", "align-content", "flex-direction"],
                correct: 1
            },
            {
                id: 204,
                skill: "React",
                question: "Which React Hook is used to manage side effects in functional components?",
                options: ["useState", "useContext", "useEffect", "useReducer"],
                correct: 2
            },
            {
                id: 205,
                skill: "Git",
                question: "Which command creates and switches to a new branch simultaneously?",
                options: [
                    "git branch <name>",
                    "git checkout -b <name>",
                    "git switch-create <name>",
                    "git merge <name>"
                ],
                correct: 1
            }
        ],

        FULL_STACK: [
            {
                id: 301,
                skill: "JavaScript",
                question: "Which keyword declares a block-scoped variable that can be reassigned?",
                options: ["const", "let", "static", "final"],
                correct: 1
            },
            {
                id: 302,
                skill: "React",
                question: "Which Hook manages component state?",
                options: ["useEffect", "useState", "useMemo", "useRef"],
                correct: 1
            },
            {
                id: 303,
                skill: "Node.js",
                question: "What is Node.js primarily used for?",
                options: [
                    "Running JavaScript outside the browser",
                    "Styling HTML",
                    "Managing CSS",
                    "Creating SQL tables only"
                ],
                correct: 0
            },
            {
                id: 304,
                skill: "Express",
                question: "What is Express.js?",
                options: [
                    "A database",
                    "A CSS framework",
                    "A web framework for Node.js",
                    "A programming language"
                ],
                correct: 2
            },
            {
                id: 305,
                skill: "MySQL",
                question: "Which clause filters rows in an SQL query?",
                options: ["ORDER BY", "WHERE", "GROUP BY", "JOIN"],
                correct: 1
            }
        ]
    }
};