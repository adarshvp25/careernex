
const AppState = {
  currentCompletedStep: 1, // Only Step 1 unlocked initially
  targetRole: "JAVA_BACKEND",
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
    FRONTEND_DEV: {
      title: "Frontend Developer",
      requiredSkills: ["JavaScript", "HTML", "CSS", "React", "Git"]
    },
    FULLSTACK_DEV: {
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

    FRONTEND_DEV: [
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

    FULLSTACK_DEV: [
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

    FRONTEND_DEV: [
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

    FULLSTACK_DEV: [
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


document.addEventListener("DOMContentLoaded", () => {
  initStepNavigation();
  initRoleSelector();
  initResumeUpload();
});

// Navigation
function initStepNavigation() {
  const stepBtns = document.querySelectorAll(".step-btn");
  const tabPanes = document.querySelectorAll(".tab-pane");

  stepBtns.forEach(btn => {
    btn.addEventListener("click", () => {
      if (btn.classList.contains("disabled")) {
        alert(
          "Action Locked: Please complete the current step first to unlock this section."
        );
        return;
      }

      const targetTab = btn.getAttribute("data-tab");

      stepBtns.forEach(b => b.classList.remove("active"));
      tabPanes.forEach(p => p.classList.remove("active"));

      btn.classList.add("active");

      const targetPane = document.getElementById(targetTab);
      if (targetPane) {
        targetPane.classList.add("active");
      }
    });
  });
}

function unlockStep(stepNumber, btnId) {
  if (AppState.currentCompletedStep < stepNumber) {
    AppState.currentCompletedStep = stepNumber;
  }

  const btn = document.getElementById(btnId);
  if (btn) {
    btn.classList.remove("disabled");
    btn.removeAttribute("disabled");
    const lockIcn = btn.querySelector(".lock-icn");
    if (lockIcn) lockIcn.remove();
  }
}

// Role selector and skill gap matching
function initRoleSelector() {
  const selector = document.getElementById("globalRoleSelect");
  if (!selector) return;

  selector.addEventListener("change", (e) => {
    AppState.targetRole = e.target.value;

    if (AppState.extractedSkills.length > 0) {
      matchResumeToRole();
    }
  });
}


// Extracting PDF text
async function extractPdfText(file) {
  if (typeof pdfjsLib === "undefined") {
    throw new Error("PDF reader library did not load. Please refresh the page.");
  }

  const arrayBuffer = await file.arrayBuffer();

  const pdf = await pdfjsLib.getDocument({
    data: new Uint8Array(arrayBuffer)
  }).promise;

  const pageTexts = [];

  for (let pageNumber = 1; pageNumber <= pdf.numPages; pageNumber++) {
    const page = await pdf.getPage(pageNumber);
    const content = await page.getTextContent();

    const pageText = content.items
      .map(item => ("str" in item ? item.str : ""))
      .filter(Boolean)
      .join(" ");

    pageTexts.push(pageText);
  }

  const extractedText = pageTexts.join("\n").trim();

  if (!extractedText) {
    throw new Error(
      "No selectable text was found in this PDF. It may be a scanned image. Please use a text-based PDF or paste your resume text."
    );
  }

  return extractedText;
}

// Resume upload and fetch data
function initResumeUpload() {
  const dropzone = document.getElementById("dropzone");
  const fileInput = document.getElementById("resumeFileInput");
  const fetchFileBtn = document.getElementById("fetchResumeFileBtn");
  const parseTextBtn = document.getElementById("parseTextBtn");
  const unlockStep2Btn = document.getElementById("unlockStep2Btn");

  const rawResumeInput = document.getElementById("rawResumeText");
  const statusBanner = document.getElementById("uploadStatus");

  if (dropzone && fileInput) {
    dropzone.onclick = (e) => {
      if (
        e.target.tagName !== "BUTTON" &&
        !e.target.closest("button") &&
        e.target !== fileInput
      ) {
        fileInput.click();
      }
    };
  }

  // Validate the selected file and update the upload UI.
  if (fileInput) {
    fileInput.onchange = () => {
      const file = fileInput.files?.[0];

      const fileNameDisplay = document.getElementById("fileNameDisplay");
      const selectedBadge = document.getElementById("selectedFileBadge");

      if (!file) return;

      const isPdf =
        file.type === "application/pdf" || /\.pdf$/i.test(file.name);

      const isTxt =
        file.type === "text/plain" || /\.txt$/i.test(file.name);

      if (!isPdf && !isTxt) {
        alert("Unsupported file type. Please select a PDF or TXT resume.");
        fileInput.value = "";
        if (fetchFileBtn) fetchFileBtn.disabled = true;
        if (selectedBadge) selectedBadge.classList.add("hidden");
        return;
      }

      // Keep uploads within a reasonable size.
      if (file.size > 10 * 1024 * 1024) {
        alert("The resume file must be 10 MB or smaller.");
        fileInput.value = "";
        if (fetchFileBtn) fetchFileBtn.disabled = true;
        if (selectedBadge) selectedBadge.classList.add("hidden");
        return;
      }

      if (fileNameDisplay) fileNameDisplay.innerText = file.name;
      if (selectedBadge) selectedBadge.classList.remove("hidden");

      if (fetchFileBtn) {
        fetchFileBtn.disabled = false;
        fetchFileBtn.classList.remove("btn-secondary");
        fetchFileBtn.classList.add("btn-primary");
      }
    };
  }

  // Extract text from a selected PDF/TXT file or use pasted resume text.
  if (fetchFileBtn) {
    fetchFileBtn.onclick = async (e) => {
      e.preventDefault();

      const file = fileInput?.files?.[0];
      const pastedText = rawResumeInput?.value.trim() || "";

      try {
        let resumeText = pastedText;

        // Pasted text takes priority. Otherwise, read the selected file.
        if (!resumeText && file) {
          const isPdf =
            file.type === "application/pdf" || /\.pdf$/i.test(file.name);

          const isTxt =
            file.type === "text/plain" || /\.txt$/i.test(file.name);

          if (isPdf) {
            resumeText = await extractPdfText(file);
          } else if (isTxt) {
            resumeText = await file.text();
          } else {
            throw new Error("Please select a valid PDF or TXT resume.");
          }

          resumeText = resumeText.trim();
        }

        if (!resumeText) {
          alert("Please select a PDF/TXT resume or paste your resume text.");
          return;
        }

        if (statusBanner) {
          statusBanner.classList.remove("hidden");
          statusBanner.textContent = "Resume processed. Analyzing skills...";
        }

        simulateResumeParse(resumeText);

        if (statusBanner) {
          statusBanner.textContent = "Resume text extracted successfully.";
        }
      } catch (error) {
        console.error("Resume extraction error:", error);

        if (statusBanner) {
          statusBanner.classList.remove("hidden");
          statusBanner.textContent =
            error.message || "Could not process the resume.";
        }

        alert(error.message || "Could not process the resume.");
      }
    };
  }

  // Parse text entered manually.
  if (parseTextBtn) {
    parseTextBtn.onclick = (e) => {
      e.preventDefault();

      const text = rawResumeInput?.value.trim();

      if (!text) {
        alert("Please paste your resume text first.");
        return;
      }

      if (statusBanner) {
        statusBanner.classList.remove("hidden");
        statusBanner.textContent = "Analyzing pasted resume text...";
      }

      simulateResumeParse(text);

      if (statusBanner) {
        statusBanner.textContent = "Resume text analyzed successfully.";
      }
    };
  }

  // Keep your existing Step 2 unlock behavior.
  if (unlockStep2Btn) {
    unlockStep2Btn.onclick = () => {
      unlockStep(2, "btnStep2");
      prepareRoleQuiz();
      renderQuizQuestion();

      const step2Btn = document.getElementById("btnStep2");
      if (step2Btn) step2Btn.click();
    };
  }
}


function simulateResumeParse(text) {
  const knownKeywords = [
    "Java", "Spring Boot", "MySQL", "REST APIs", "HTML", "CSS", "JavaScript", "React", "Git",
    "Linux", "Docker", "Python", "AWS", "SQL", "Agile/Scrum", "SEO", "User Research", "Wireframing", "Excel"
  ];

  const found = knownKeywords.filter(kw => text.toLowerCase().includes(kw.toLowerCase()));

  AppState.extractedSkills = Array.from(new Set(found));
  if (AppState.extractedSkills.length === 0) {
    alert("No recognized skills were found in the resume text.");
  }

  matchResumeToRole();

  const parsedSection = document.getElementById("parsedResultSection");
  if (parsedSection) parsedSection.classList.remove("hidden");
}

function matchResumeToRole() {
  const currentRoleObj = AppState.roles[AppState.targetRole] || AppState.roles["JAVA_BACKEND"];
  const required = currentRoleObj.requiredSkills;

  const foundInResume = required.filter(s => AppState.extractedSkills.includes(s));
  const missingGaps = required.filter(s => !AppState.extractedSkills.includes(s));

  AppState.skillGaps = missingGaps;

  const foundContainer = document.getElementById("foundSkillsTags");
  if (foundContainer) {
    foundContainer.innerHTML = foundInResume.length > 0 ?
      foundInResume.map(s => `<span class="tag verified"><i class="fa-solid fa-check"></i> ${s}</span>`).join('') :
      '<span class="desc">No matching role skills detected in resume.</span>';
  }

  const gapsContainer = document.getElementById("missingGapsTags");
  if (gapsContainer) {
    gapsContainer.innerHTML = missingGaps.length > 0 ?
      missingGaps.map(s => `<span class="tag gap"><i class="fa-solid fa-xmark"></i> ${s}</span>`).join('') :
      '<span class="text-success">Great! All required role skills found in resume.</span>';
  }
}

// Quiz
function prepareRoleQuiz() {
  let pool = AppState.roleQuestionPool[AppState.targetRole] || AppState.roleQuestionPool["JAVA_BACKEND"];
  let shuffled = [...pool].sort(() => 0.5 - Math.random());

  AppState.currentQuizQuestions = shuffled.slice(0, 5);
  AppState.quizAnswers = {};
  AppState.currentQuizIndex = 0;
}

function renderQuizQuestion() {
  const qContainer = document.getElementById("quizContainer");
  if (!qContainer) return;

  if (AppState.currentQuizQuestions.length === 0) {
    prepareRoleQuiz();
  }

  const questions = AppState.currentQuizQuestions;
  const roleTitle = (AppState.roles[AppState.targetRole] || AppState.roles["JAVA_BACKEND"]).title;

  const subtitle = document.getElementById("quizRoleSubtitle");
  if (subtitle) subtitle.innerText = `Dynamic non-repeating assessment questions for ${roleTitle}.`;

  const q = questions[AppState.currentQuizIndex];
  const total = questions.length;

  const quizCounter = document.getElementById("quizCounter");
  const currentSkillPill = document.getElementById("currentQuestionSkill");

  if (quizCounter) quizCounter.innerText = `Question ${AppState.currentQuizIndex + 1} of ${total}`;
  if (currentSkillPill) currentSkillPill.innerText = q.skill;

  let optionsHTML = q.options.map((opt, idx) => `
    <div class="option-row ${AppState.quizAnswers[q.id] === idx ? 'selected' : ''}" onclick="selectQuizOption(${q.id}, ${idx})">
      <strong>${String.fromCharCode(65 + idx)}.</strong> ${escapeHTML(opt)}
    </div>
  `).join("");

  qContainer.innerHTML = `
    <div class="question-text">${escapeHTML(q.question)}</div>
    <div>${optionsHTML}</div>
  `;

  const prevBtn = document.getElementById("prevQuestionBtn");
  const nextBtn = document.getElementById("nextQuestionBtn");
  const submitBtn = document.getElementById("submitQuizBtn");

  if (prevBtn) prevBtn.disabled = AppState.currentQuizIndex === 0;

  const isCurrentAnswered = AppState.quizAnswers[q.id] !== undefined;

  if (AppState.currentQuizIndex === total - 1) {
    if (nextBtn) nextBtn.classList.add("hidden");
    if (submitBtn) {
      submitBtn.classList.remove("hidden");
      submitBtn.disabled = !isCurrentAnswered;
    }
  } else {
    if (nextBtn) {
      nextBtn.classList.remove("hidden");
      nextBtn.disabled = !isCurrentAnswered;
    }
    if (submitBtn) submitBtn.classList.add("hidden");
  }
}

// helper function
function escapeHTML(value) {
  return String(value).replace(/[&<>"']/g, char => ({
    "&": "&amp;",
    "<": "&lt;",
    ">": "&gt;",
    '"': "&quot;",
    "'": "&#39;"
  })[char]);
}


function selectQuizOption(questionId, optionIdx) {
  AppState.quizAnswers[questionId] = optionIdx;
  renderQuizQuestion();
}

document.addEventListener("click", (e) => {
  const target = e.target.closest("button");
  if (!target) return;
  if (target.id === "nextQuestionBtn") {
    const questions = AppState.currentQuizQuestions;
    const currentQ = questions[AppState.currentQuizIndex];

    if (AppState.quizAnswers[currentQ.id] === undefined) {
      alert("Please select an answer option before moving to the next question!");
      return;
    }

    if (AppState.currentQuizIndex < questions.length - 1) {
      AppState.currentQuizIndex++;
      renderQuizQuestion();
    }
  }

  if (target.id === "prevQuestionBtn") {
    if (AppState.currentQuizIndex > 0) {
      AppState.currentQuizIndex--;
      renderQuizQuestion();
    }
  }

  if (target.id === "submitQuizBtn") {
    const questions = AppState.currentQuizQuestions;

    const unanswered = questions.filter(q => AppState.quizAnswers[q.id] === undefined);
    if (unanswered.length > 0) {
      alert(`Please answer all ${questions.length} questions before submitting!`);
      return;
    }

    AppState.verifiedSkills = [];

    questions.forEach(q => {
      const selectedOption = AppState.quizAnswers[q.id];
      if (selectedOption === q.correct) {
        if (!AppState.verifiedSkills.includes(q.skill)) {
          AppState.verifiedSkills.push(q.skill);
        }
      }
    });

    calculateFinalReadiness();
    unlockStep(3, "btnStep3");
    alert("Quiz submitted! Your readiness score and skill gap breakdown have been updated.");
    const step3Btn = document.getElementById("btnStep3");
    if (step3Btn) step3Btn.click();
  }

  if (target.id === "unlockStep4Btn") {
    unlockStep(4, "btnStep4");
    renderProjectsAndRoadmap();
    alert("Step 4 (Targeted Roadmap) unlocked.");
    const step4Btn = document.getElementById("btnStep4");
    if (step4Btn) step4Btn.click();
  }
});

// Resume readiness and gap checker
function calculateFinalReadiness() {
  const currentRoleObj = AppState.roles[AppState.targetRole] || AppState.roles["JAVA_BACKEND"];
  const required = currentRoleObj.requiredSkills;

  const verifiedMatch = required.filter(s => AppState.verifiedSkills.includes(s));
  const gaps = required.filter(s => !verifiedMatch.includes(s));

  AppState.readinessScore = Math.round((verifiedMatch.length / required.length) * 100);
  AppState.skillGaps = gaps;

  const scoreVal = document.getElementById("readinessScoreVal");
  if (scoreVal) scoreVal.innerText = `${AppState.readinessScore}%`;

  const statusBadge = document.getElementById("readinessStatusBadge");
  if (statusBadge) {
    if (AppState.readinessScore >= 80) {
      statusBadge.innerText = "High Readiness";
      statusBadge.className = "badge badge-success";
    } else {
      statusBadge.innerText = "Needs Improvement";
      statusBadge.className = "badge badge-warning";
    }
  }

  const summaryText = document.getElementById("readinessSummaryText");
  if (summaryText) {
    summaryText.innerHTML = `You matched <strong>${verifiedMatch.length}</strong> out of <strong>${required.length}</strong> required skills for <strong>${currentRoleObj.title}</strong>.`;
  }

  const formulaText = document.getElementById("formulaBreakdown");
  if (formulaText) {
    formulaText.innerText = `Calculation: (${verifiedMatch.length} / ${required.length}) * 100 = ${AppState.readinessScore}%`;
  }

  const vContainer = document.getElementById("finalVerifiedTags");
  if (vContainer) {
    vContainer.innerHTML = verifiedMatch.length > 0 ?
      verifiedMatch.map(s => `<span class="tag verified"><i class="fa-solid fa-check"></i> ${s}</span>`).join('') :
      '<span class="desc">No skills verified in quiz assessment.</span>';
  }

  const gContainer = document.getElementById("finalGapTags");
  if (gContainer) {
    gContainer.innerHTML = gaps.length > 0 ?
      gaps.map(s => `<span class="tag gap"><i class="fa-solid fa-xmark"></i> ${s}</span>`).join('') :
      '<span class="text-success">No skill gaps identified! Perfect score.</span>';
  }
}

// Role and projects
function renderProjectsAndRoadmap() {
  const roleTitle = (AppState.roles[AppState.targetRole] || AppState.roles["JAVA_BACKEND"]).title;
  const projectGrid = document.getElementById("projectRecommendationsGrid");

  // 1. RENDER ROLE-SPECIFIC PROJECTS
  if (projectGrid) {
    projectGrid.innerHTML = "";
    const allProjects = AppState.roleProjects[AppState.targetRole] || AppState.roleProjects["JAVA_BACKEND"];

    // Filter projects that address the user's identified skill gaps
    let recommended = allProjects.filter(p => p.targetSkills.some(s => AppState.skillGaps.includes(s)));
    if (recommended.length === 0) recommended = allProjects; // Fallback to role default projects

    recommended.forEach(p => {
      const card = document.createElement("div");
      card.className = "project-card";
      const tags = p.targetSkills.map(s => `<span class="tag ${AppState.skillGaps.includes(s) ? 'gap' : 'verified'}">${s}</span>`).join(" ");

      card.innerHTML = `
        <div>
          <strong>${p.title}</strong>
          <div class="desc" style="margin-bottom: 5px;">${p.description}</div>
          <div>${tags}</div>
        </div>
        <button class="btn btn-secondary" onclick="alert('Project added to your personalized roadmap!')">Add to Roadmap</button>
      `;
      projectGrid.appendChild(card);
    });
  }

  // Roadmap setter
  const roadmapContainer = document.getElementById("roadmapTimelineContainer");
  if (roadmapContainer) {
    roadmapContainer.innerHTML = "";

    const gaps = AppState.skillGaps;
    const gapText = gaps.length > 0 ? gaps.join(", ") : "Advanced Best Practices";

    // Dynamic 14-day timeline based on role and actual missing skills
    const dynamicRoadmap = [
      { day: "Day 01-02", title: `Fundamentals Setup: ${roleTitle}`, desc: `Set up environment & review core concepts for missing areas (${gapText}).` },
      { day: "Day 03-05", title: `Targeted Practice on Missing Skills`, desc: `Complete guided coding exercises & syntax problems specifically for ${gapText}.` },
      { day: "Day 06-09", title: `Capstone Implementation`, desc: `Build dynamic capstone project incorporating missing competencies: ${gapText}.` },
      { day: "Day 10-12", title: `GitHub Portfolio & Documentation`, desc: `Push code repository, write detailed README.md, and optimize project docs.` },
      { day: "Day 13-14", title: `Final Role Verification Audit`, desc: `Re-evaluate readiness metrics for ${roleTitle} technical interviews.` }
    ];

    dynamicRoadmap.forEach((item, index) => {
      const row = document.createElement("div");
      row.className = "roadmap-item";
      row.innerHTML = `
        <span class="day-tag">${item.day}</span>
        <div style="flex: 1;">
          <strong>${item.title}</strong>
          <div class="desc" style="margin-bottom: 0;">${item.desc}</div>
        </div>
        <input type="checkbox" style="width: 16px; height: 16px; cursor: pointer;">
      `;
      roadmapContainer.appendChild(row);
    });
  }
}