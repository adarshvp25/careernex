async function getDashboardData(resumeId, roleId, assessmentId) {
    return await apiRequest(
        `/dashboard?resumeId=${encodeURIComponent(resumeId)}&roleId=${encodeURIComponent(roleId)}&assessmentId=${encodeURIComponent(assessmentId)}`
    );
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
}