// Fetch quiz questions for a selected role
async function getQuizQuestions(roleId) {
    return await apiRequest(`/quiz/questions?roleId=${roleId}`);
}


// Submit quiz answers for backend evaluation
async function submitQuizAnswers(roleId, answers) {
    return await apiRequest(`/quiz/submit?roleId=${roleId}`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({
            answers: answers
        })
    });
}

// Convert the selected role code into its backend role ID
function getSelectedRoleId() {
    const roleIds = {
        JAVA_BACKEND: 1,
        FRONTEND: 2,
        FULL_STACK: 3
    };

    return roleIds[AppState.targetRole];
}

// Fetch and prepare quiz questions from the backend
async function prepareRoleQuiz() {
    const roleId = getSelectedRoleId();

    if (!roleId) {
        throw new Error("Invalid career role selected.");
    }

    const backendQuestions = await getQuizQuestions(roleId);

    // Convert backend response into the format used by the existing UI
    AppState.currentQuizQuestions = backendQuestions.map(q => ({
        id: q.id,
        question: q.questionText,
        options: [
            q.optionA,
            q.optionB,
            q.optionC,
            q.optionD
        ],
        difficulty: q.difficulty,
        skill: q.skillName
    }));

    // Reset quiz state for the new attempt
    AppState.quizAnswers = {};
    AppState.currentQuizIndex = 0;
}