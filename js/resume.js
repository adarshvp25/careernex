// Upload a PDF resume to the backend
async function uploadResume(file) {
    const formData = new FormData();
    formData.append("file", file);

    return await apiRequest("/resumes/upload", {
        method: "POST",
        body: formData
    });
}


// Get detected skills from an uploaded resume
async function getResumeSkills(resumeId) {
    return await apiRequest(`/resumes/${resumeId}/skills`);
}


// Get extracted text from an uploaded resume
async function getResumeText(resumeId) {
    return await apiRequest(`/resumes/${resumeId}/text`);
}