async function getDashboardData(resumeId, roleId, assessmentId) {
    return await apiRequest(
        `/dashboard?resumeId=${encodeURIComponent(resumeId)}&roleId=${encodeURIComponent(roleId)}&assessmentId=${encodeURIComponent(assessmentId)}`
    );
}

// Fetch Personalized Roadmap
async function getPersonalizedRoadmap(resumeId, roleId) {
    try {
        const roadmapData = await apiRequest(
            `/roadmap/personalized?resumeId=${encodeURIComponent(resumeId)}&roleId=${encodeURIComponent(roleId)}`
        );

        return roadmapData;
    } catch (error) {
        console.error("Failed to load personalized roadmap:", error);
        return null;
    }
}



// Render Personalized Learning Roadmap
async function renderLearningRoadmap() {

    const container = document.getElementById(
        "learningRoadmapContainer"
    );

    const dashboardRoadmap =
        document.getElementById("dashboardRoadmap");

    if (!container) return;

    const resumeId = AppState.resumeId;
    const roleId = getSelectedRoleId();

    if (!resumeId || !roleId) {
        container.textContent =
            "Please complete your resume analysis first.";

        if (dashboardRoadmap) {
            dashboardRoadmap.textContent =
                "Please complete your resume analysis first.";
        }

        return;
    }

    container.textContent = "Loading your learning roadmap...";

    if (dashboardRoadmap) {
        dashboardRoadmap.textContent =
            "Loading your learning roadmap...";
    }

    try {

        // Fetch personalized learning tasks
        const roadmap = await getPersonalizedRoadmap(
            resumeId,
            roleId
        );

        if (!roadmap || roadmap.length === 0) {

            const emptyMessage =
                "No additional learning tasks are required for your selected role.";

            container.textContent = emptyMessage;

            if (dashboardRoadmap) {
                dashboardRoadmap.textContent = emptyMessage;
            }

            return;
        }

        // Fetch existing progress records
        const progressRecords = await apiRequest(
            "/roadmap/progress"
        );

        const progressMap = new Map(
            progressRecords.map(item => [
                item.roadmapStepId,
                item.status
            ])
        );

        container.innerHTML = "";

        if (dashboardRoadmap) {
            dashboardRoadmap.innerHTML = "";
        }

        roadmap.forEach(step => {

            const status = progressMap.get(step.id) || "NOT_STARTED";

            // --------------------------------
            // STEP 4: Original Roadmap Card
            // --------------------------------

            const card = document.createElement("div");
            card.className = "roadmap-item";

            const details = document.createElement("div");
            details.style.flex = "1";

            const title = document.createElement("strong");
            title.textContent =
                `${step.sequenceNumber}. ${step.skillName}`;

            const objective = document.createElement("p");
            objective.className = "desc";
            objective.textContent = step.learningObjective;

            const practicalTask = document.createElement("p");
            practicalTask.textContent =
                `Practical Task: ${step.practicalTask}`;

            const duration = document.createElement("span");
            duration.className = "project-skill";
            duration.textContent =
                `Estimated time: ${step.estimatedDays} days`;

            const statusSelect = document.createElement("select");

            [
                { value: "NOT_STARTED", label: "Not Started" },
                { value: "IN_PROGRESS", label: "In Progress" },
                { value: "COMPLETED", label: "Completed" }
            ].forEach(optionData => {

                const option = document.createElement("option");
                option.value = optionData.value;
                option.textContent = optionData.label;

                statusSelect.appendChild(option);
            });

            statusSelect.value = status;

            // --------------------------------
            // DASHBOARD: Roadmap Status
            // --------------------------------

            let dashboardStatus = null;

            if (dashboardRoadmap) {

                const dashboardCard =
                    document.createElement("div");

                dashboardCard.className = "roadmap-item";

                const dashboardTitle =
                    document.createElement("strong");

                dashboardTitle.textContent =
                    `${step.sequenceNumber}. ${step.skillName}`;

                const dashboardObjective =
                    document.createElement("p");

                dashboardObjective.textContent =
                    step.learningObjective;

                const dashboardTask =
                    document.createElement("p");

                dashboardTask.textContent =
                    `Practical Task: ${step.practicalTask}`;

                const dashboardDuration =
                    document.createElement("span");

                dashboardDuration.className = "project-skill";

                dashboardDuration.textContent =
                    `Estimated time: ${step.estimatedDays} days`;

                dashboardStatus =
                    document.createElement("p");

                dashboardStatus.textContent =
                    `Status: ${status.replace("_", " ")}`;

                dashboardCard.append(
                    dashboardTitle,
                    dashboardObjective,
                    dashboardTask,
                    dashboardDuration,
                    dashboardStatus
                );

                dashboardRoadmap.appendChild(dashboardCard);
            }

            // Update roadmap progress
            statusSelect.addEventListener("change", async () => {

                const previousStatus = status;

                try {

                    statusSelect.disabled = true;

                    await apiRequest(
                        `/roadmap/progress/${step.id}`,
                        {
                            method: "PUT",
                            headers: {
                                "Content-Type": "application/json"
                            },
                            body: JSON.stringify({
                                status: statusSelect.value,
                                resumeId: resumeId,
                                roleId: roleId
                            })
                        }
                    );

                    if (dashboardStatus) {
                        dashboardStatus.textContent =
                            `Status: ${statusSelect.value.replace("_", " ")}`;
                    }

                    await renderRoadmapProgressSummary();

                    // Refresh the saved status
                    await renderLearningRoadmap();

                } catch (error) {

                    console.error(
                        "Failed to update roadmap progress:",
                        error
                    );

                    alert(error.message);

                    statusSelect.value = previousStatus;

                } finally {

                    statusSelect.disabled = false;
                }
            });

            details.append(
                title,
                objective,
                practicalTask,
                duration,
                document.createElement("br"),
                statusSelect
            );

            card.appendChild(details);
            container.appendChild(card);
        });

    } catch (error) {

        console.error("Failed to render learning roadmap:", error);

        container.textContent =
            "Unable to load your learning roadmap.";

        if (dashboardRoadmap) {
            dashboardRoadmap.textContent =
                "Unable to load your learning roadmap.";
        }
    }
}




// Render Learning Roadmap Progress Summary
async function renderRoadmapProgressSummary() {

    const container = document.getElementById(
        "roadmapProgressSummary"
    );

    if (!container) return;

    const resumeId = AppState.resumeId;
    const roleId = getSelectedRoleId();

    if (!resumeId || !roleId) {
        container.textContent = "Progress data is unavailable.";
        return;
    }

    try {

        const summary = await apiRequest(
            `/roadmap/progress/summary?resumeId=${encodeURIComponent(resumeId)}&roleId=${encodeURIComponent(roleId)}`
        );

        container.innerHTML = "";

        const stats = [
            {
                label: "Total Tasks",
                value: summary.totalSteps
            },
            {
                label: "Completed",
                value: summary.completedSteps
            },
            {
                label: "In Progress",
                value: summary.inProgressSteps
            },
            {
                label: "Not Started",
                value: summary.notStartedSteps
            }
        ];

        const statsGrid = document.createElement("div");
        statsGrid.className = "project-list";

        stats.forEach(stat => {

            const card = document.createElement("div");
            card.className = "project-card";

            const label = document.createElement("p");
            label.textContent = stat.label;

            const value = document.createElement("h3");
            value.textContent = stat.value;

            card.append(label, value);
            statsGrid.appendChild(card);
        });

        const percentage = document.createElement("h3");
        percentage.style.marginTop = "1.5rem";
        percentage.textContent =
            `Overall Completion: ${summary.completionPercentage}%`;

        const progressTrack = document.createElement("div");
        progressTrack.style.width = "100%";
        progressTrack.style.height = "12px";
        progressTrack.style.background = "#e5e7eb";
        progressTrack.style.borderRadius = "10px";
        progressTrack.style.overflow = "hidden";

        const progressBar = document.createElement("div");
        progressBar.style.width =
            `${summary.completionPercentage}%`;
        progressBar.style.height = "100%";
        progressBar.style.background = "#16a34a";
        progressBar.style.transition = "width 0.3s ease";

        progressTrack.appendChild(progressBar);

        const learningDays = document.createElement("p");
        learningDays.className = "desc";
        learningDays.style.marginTop = "0.75rem";
        learningDays.textContent =
            `Completed Learning Days: ${summary.completedLearningDays}`;

        container.append(
            statsGrid,
            percentage,
            progressTrack,
            learningDays
        );

        // Update Career Dashboard learning progress
        const dashboardProgress =
            document.getElementById("dashboardLearningProgress");

        if (dashboardProgress) {
            dashboardProgress.textContent =
                `${summary.completionPercentage}%`;
        }

        const dashboardRoadmap =
            document.getElementById("dashboardRoadmap");

        if (dashboardRoadmap) {
            dashboardRoadmap.innerHTML = container.innerHTML;
        }

    } catch (error) {

        console.error(
            "Failed to load roadmap progress summary:",
            error
        );

        container.textContent =
            "Unable to load learning progress summary.";
    }
}



function renderDashboardData(data) {

    AppState.readinessScore = data.overallReadiness;

    // Display overall readiness
    document.getElementById("readinessScoreVal").textContent =
        `${data.overallReadiness}%`;

    // Update circular progress ring
    const scoreCircle = document.querySelector(".score-circle");

    let progressColor;

    if (data.overallReadiness >= 70) {
        progressColor = "#16a34a"; // Green
    } else if (data.overallReadiness >= 40) {
        progressColor = "#2563eb"; // Blue
    } else {
        progressColor = "#eab308"; // Amber
    }

    scoreCircle.style.setProperty(
        "--score",
        data.overallReadiness
    );

    scoreCircle.style.setProperty(
        "--progress-color",
        progressColor
    );

    // Display readiness status
    const statusBadge = document.getElementById("readinessStatusBadge");

    if (data.overallReadiness >= 70) {
        statusBadge.textContent = "Job Ready";
        statusBadge.className = "badge badge-success";
    } else if (data.overallReadiness >= 40) {
        statusBadge.textContent = "Making Progress";
        statusBadge.className = "badge badge-warning";
    } else {
        statusBadge.textContent = "Needs Improvement";
        statusBadge.className = "badge badge-warning";
    }

    // Display readiness summary
    document.getElementById("readinessSummaryText").textContent =
        `Your current readiness for ${data.roleName} is ${data.overallReadiness}%.`;

    // Display scoring formula
    document.getElementById("formulaBreakdown").textContent =
        `Calculation: (${data.skillCoverage} × 0.60) + (${data.quizScore} × 0.40) = ${data.overallReadiness}%`;

    // Display matched skills
    document.getElementById("finalVerifiedTags").innerHTML =
        data.skillAnalysis.matchedSkills.length > 0
            ? data.skillAnalysis.matchedSkills.map(skill =>
                `<span class="skill-tag">${skill}</span>`
            ).join("")
            : "<p>No matched skills.</p>";

    // Display mandatory skill gaps
    const mandatorySkills = data.skillAnalysis.missingMandatorySkills || [];

    document.getElementById("mandatoryGapTags").innerHTML =
        mandatorySkills.length > 0
            ? mandatorySkills.map(skill =>
                `<span class="skill-tag">${skill}</span>`
            ).join("")
            : "<p>No mandatory skill gaps found.</p>";

    // Display optional skill gaps
    const optionalSkills = data.skillAnalysis.missingOptionalSkills || [];

    document.getElementById("optionalGapTags").innerHTML =
        optionalSkills.length > 0
            ? optionalSkills.map(skill =>
                `<span class="skill-tag">${skill}</span>`
            ).join("")
            : "<p>No optional skill gaps found.</p>";


    // Display Skill Coverage
    const skillAnalysis = data.skillAnalysis;

    const matchedCount = skillAnalysis.matchedCount;
    const missingCount = skillAnalysis.missingCount;
    const totalSkills = skillAnalysis.totalRequiredSkills;

    const coveragePercentage = totalSkills > 0
        ? (matchedCount / totalSkills) * 100
        : 0;

    document.getElementById("skillCoverageCount").textContent =
        `${matchedCount} / ${totalSkills}`;

    document.getElementById("skillCoverageFill").style.width =
        `${coveragePercentage}%`;

    document.getElementById("matchedSkillCount").textContent =
        `${matchedCount} matched`;

    document.getElementById("missingSkillCount").textContent =
        `${missingCount} missing`;

    // Display Project Recommendations
    renderProjectRecommendations(data.projectRecommendations);

    // Display Personalized Learning Roadmap
    renderLearningRoadmap();

    // Display Learning Progress Summary
    renderRoadmapProgressSummary();

    renderCareerDashboard(data);
}

// Render Career Dashboard Overview
function renderCareerDashboard(data) {

    // Target Career Role
    document.getElementById("dashboardTargetRole").textContent =
        data.roleName || "--";

    // Career Readiness
    document.getElementById("dashboardReadinessScore").textContent =
        `${data.overallReadiness ?? 0}%`;

    // Quiz Score
    document.getElementById("dashboardQuizScore").textContent =
        `${data.quizScore ?? 0}%`;

    // Assessment Results
    document.getElementById("dashboardCorrectAnswers").textContent =
        document.getElementById("correctAnswers")?.textContent || "--";

    document.getElementById("dashboardVerifiedSkills").textContent =
        document.getElementById("verifiedSkillsCount")?.textContent || "--";


    // Resume Skills
    const resumeSkillsContainer =
        document.getElementById("dashboardResumeSkills");

    resumeSkillsContainer.innerHTML = "";

    (AppState.extractedSkills || []).forEach(skill => {
        const tag = document.createElement("span");
        tag.className = "skill-tag";
        tag.textContent = skill;
        resumeSkillsContainer.appendChild(tag);
    });


    // Matched Skills
    const matchedContainer =
        document.getElementById("dashboardMatchedSkills");

    matchedContainer.innerHTML = "";

    (data.skillAnalysis.matchedSkills || []).forEach(skill => {
        const tag = document.createElement("span");
        tag.className = "skill-tag";
        tag.textContent = skill;
        matchedContainer.appendChild(tag);
    });


    // Missing Skills
    const missingContainer =
        document.getElementById("dashboardMissingSkills");

    missingContainer.innerHTML = "";

    const missingSkills = [
        ...(data.skillAnalysis.missingMandatorySkills || []),
        ...(data.skillAnalysis.missingOptionalSkills || [])
    ];

    missingSkills.forEach(skill => {
        const tag = document.createElement("span");
        tag.className = "skill-tag";
        tag.textContent = skill;
        missingContainer.appendChild(tag);
    });
}


// Render Project Recommendations

async function renderProjectRecommendations(projects) {

    const projectContainer = document.getElementById(
        "projectRecommendationsGrid"
    );

    const dashboardProjects =
        document.getElementById("dashboardProjects");

    // Clear existing content
    projectContainer.innerHTML = "";

    if (dashboardProjects) {
        dashboardProjects.innerHTML = "";
    }

    // Handle empty project recommendations
    if (!projects || projects.length === 0) {

        projectContainer.innerHTML =
            "<p>No project recommendations available.</p>";

        if (dashboardProjects) {
            dashboardProjects.textContent =
                "No project recommendations available.";
        }

        return;
    }

    // Fetch projects already saved by the user
    const savedProjects = await getSavedProjects();

    const savedProjectIds = new Set(
        savedProjects.map(project => project.projectId)
    );

    projects.forEach(project => {

        // ---------------------------------
        // STEP 4: Recommended Project Card
        // ---------------------------------

        const projectCard = document.createElement("div");
        projectCard.className = "recommended-project-card";

        const title = document.createElement("h3");
        title.textContent = project.projectTitle;

        const description = document.createElement("p");
        description.textContent = project.description;

        const skill = document.createElement("span");
        skill.className = "project-skill";
        skill.textContent = project.skillName;

        const difficulty = document.createElement("span");
        difficulty.className = "project-difficulty";
        difficulty.textContent = project.difficulty;

        const techStack = document.createElement("p");
        techStack.textContent = `Tech Stack: ${project.techStack}`;

        const outcome = document.createElement("p");
        outcome.textContent =
            `Learning Outcome: ${project.learningOutcome}`;

        const addButton = document.createElement("button");
        addButton.className = "btn btn-secondary";

        const alreadyAdded = savedProjectIds.has(project.id);

        addButton.textContent = alreadyAdded
            ? "Added to Roadmap"
            : "Add to Roadmap";

        addButton.disabled = alreadyAdded;

        addButton.addEventListener("click", async () => {

            try {
                addButton.disabled = true;
                addButton.textContent = "Adding...";

                await saveProjectToRoadmap(project.id);

                addButton.textContent = "Added to Roadmap";

                // Refresh the roadmap
                await renderSavedProjects();

            } catch (error) {

                console.error("Failed to save project:", error);

                addButton.disabled = false;
                addButton.textContent = "Add to Roadmap";

                alert(error.message);
            }
        });

        projectCard.append(
            title,
            description,
            skill,
            difficulty,
            techStack,
            outcome,
            addButton
        );

        projectContainer.appendChild(projectCard);


        // ---------------------------------
        // CAREER DASHBOARD: Project Card
        // ---------------------------------

        if (dashboardProjects) {

            const dashboardCard = document.createElement("div");
            dashboardCard.className = "project-card";

            const dashboardTitle = document.createElement("h3");
            dashboardTitle.textContent = project.projectTitle;

            const dashboardDescription = document.createElement("p");
            dashboardDescription.textContent = project.description;

            const dashboardSkill = document.createElement("span");
            dashboardSkill.className = "project-skill";
            dashboardSkill.textContent = project.skillName;

            dashboardCard.append(
                dashboardTitle,
                dashboardDescription,
                dashboardSkill
            );

            dashboardProjects.appendChild(dashboardCard);
        }
    });
}



// Fetch projects saved by the logged-in user
async function getSavedProjects() {
    try {
        return await apiRequest("/projects/saved");
    } catch (error) {
        console.error("Failed to fetch saved projects:", error);
        return [];
    }
}

// Save a recommended project to the user's roadmap
async function saveProjectToRoadmap(projectId) {
    return await apiRequest(`/projects/${projectId}/save`, {
        method: "POST"
    });
}



async function renderSavedProjects() {

    const roadmapContainer = document.getElementById(
        "roadmapTimelineContainer"
    );

    if (!roadmapContainer) return;

    roadmapContainer.innerHTML = "<p>Loading your saved projects...</p>";

    const savedProjects = await getSavedProjects();

    if (savedProjects.length === 0) {
        roadmapContainer.innerHTML =
            "<p>You haven't added any projects to your roadmap yet.</p>";
        return;
    }

    roadmapContainer.innerHTML = "";

    savedProjects.forEach(project => {

        const row = document.createElement("div");
        row.className = "roadmap-item";

        const details = document.createElement("div");
        details.style.flex = "1";

        const title = document.createElement("strong");
        title.textContent = project.projectTitle;

        const description = document.createElement("div");
        description.className = "desc";
        description.textContent = project.description;

        const skill = document.createElement("span");
        skill.className = "project-skill";
        skill.textContent = project.skillName;

        const status = document.createElement("span");
        status.className = "project-difficulty";
        status.textContent = project.status.replaceAll("_", " ");

        details.append(title, description, skill, status);

        const removeButton = document.createElement("button");
        removeButton.className = "btn btn-secondary";
        removeButton.textContent = "Remove";

        removeButton.addEventListener("click", async () => {

            const confirmed = await showRemoveProjectModal(project.projectTitle);

            if (!confirmed) {
                return;
            }

            try {
                removeButton.disabled = true;
                removeButton.textContent = "Removing...";

                await apiRequest(`/projects/saved/${project.id}`, {
                    method: "DELETE"
                });

                // Refresh the saved projects list
                await renderSavedProjects();

                // Refresh recommendation buttons
                const resumeId = AppState.resumeId;
                const roleId = getSelectedRoleId();

                if (resumeId && roleId) {
                    const recommendations = await apiRequest(
                        `/projects/recommendations?resumeId=${encodeURIComponent(resumeId)}&roleId=${encodeURIComponent(roleId)}`
                    );

                    await renderProjectRecommendations(recommendations);
                }

            } catch (error) {
                console.error("Failed to remove project:", error);
                alert(error.message);

                removeButton.disabled = false;
                removeButton.textContent = "Remove";
            }
        });

        row.append(details, removeButton);


        roadmapContainer.appendChild(row);
    });
}


function showRemoveProjectModal(projectTitle) {

    return new Promise((resolve) => {

        // Remove any previously opened modal
        const existingModal = document.getElementById("confirmRemoveModal");
        if (existingModal) {
            existingModal.remove();
        }

        const overlay = document.createElement("div");
        overlay.id = "confirmRemoveModal";
        overlay.className = "confirm-modal-overlay";

        const modal = document.createElement("div");
        modal.className = "confirm-modal";

        const heading = document.createElement("h3");
        heading.textContent = "Remove Project?";

        const message = document.createElement("p");
        message.textContent =
            `Are you sure you want to remove "${projectTitle}" from your roadmap?`;

        const actions = document.createElement("div");
        actions.className = "confirm-modal-actions";

        const cancelButton = document.createElement("button");
        cancelButton.className = "confirm-cancel-btn";
        cancelButton.textContent = "Keep Project";

        const removeButton = document.createElement("button");
        removeButton.className = "confirm-remove-btn";
        removeButton.textContent = "Remove Project";

        actions.append(cancelButton, removeButton);
        modal.append(heading, message, actions);
        overlay.appendChild(modal);
        document.body.appendChild(overlay);

        function closeModal(result) {
            overlay.classList.remove("active");

            setTimeout(() => {
                overlay.remove();
                resolve(result);
            }, 200);
        }

        cancelButton.addEventListener("click", () => closeModal(false));
        removeButton.addEventListener("click", () => closeModal(true));

        overlay.addEventListener("click", (event) => {
            if (event.target === overlay) {
                closeModal(false);
            }
        });

        requestAnimationFrame(() => {
            overlay.classList.add("active");
        });
    });
}
