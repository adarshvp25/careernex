// Store the logged-in user's JWT token
let authToken = localStorage.getItem("careernex_token");

// Register a new user
async function registerUser(fullName, email, password) {
    try {
        const response = await fetch(`${API_BASE_URL}/auth/register`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({
                fullName: fullName,
                email: email,
                password: password
            })
        });

        const result = await response.text();

        if (!response.ok) {
            throw new Error(result || "Registration failed");
        }

        return result;

    } catch (error) {
        throw error;
    }
}


// Login an existing user
async function loginUser(email, password) {
    try {
        const response = await fetch(`${API_BASE_URL}/auth/login`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({
                email: email,
                password: password
            })
        });

        const result = await response.json();

        if (!response.ok) {
            throw new Error(result.message || "Login failed");
        }

        authToken = result.token;

        localStorage.setItem("careernex_token", authToken);

        return result;

    } catch (error) {
        throw error;
    }
}


// Logout the current user
function logoutUser() {
    authToken = null;
    localStorage.removeItem("careernex_token");
}


// Get the JWT authorization header
function getAuthHeaders() {
    return {
        "Authorization": `Bearer ${authToken}`
    };
}



// Initialize authentication interface
document.addEventListener("DOMContentLoaded", () => {

    const authScreen = document.getElementById("authScreen");
    const mainApp = document.getElementById("mainApp");

    const loginForm = document.getElementById("loginForm");
    const registerForm = document.getElementById("registerForm");

    const showRegisterBtn = document.getElementById("showRegisterBtn");
    const showLoginBtn = document.getElementById("showLoginBtn");

    const authStatus = document.getElementById("authStatus");


    // Display authentication messages
    function showAuthMessage(message, isError = false) {
        authStatus.textContent = message;
        authStatus.classList.remove("hidden");
        authStatus.classList.toggle("alert-danger", isError);
        authStatus.classList.toggle("alert-success", !isError);
    }


    // Switch to registration form
    showRegisterBtn.addEventListener("click", () => {
        loginForm.classList.add("hidden");
        registerForm.classList.remove("hidden");
        authStatus.classList.add("hidden");
    });


    // Switch to login form
    showLoginBtn.addEventListener("click", () => {
        registerForm.classList.add("hidden");
        loginForm.classList.remove("hidden");
        authStatus.classList.add("hidden");
    });


    // Handle registration
    registerForm.addEventListener("submit", async (e) => {
        e.preventDefault();

        const fullName = document.getElementById("registerFullName").value.trim();
        const email = document.getElementById("registerEmail").value.trim();
        const password = document.getElementById("registerPassword").value;

        try {
            showAuthMessage("Creating your account...");

            const result = await registerUser(fullName, email, password);

            showAuthMessage(result + " Please log in.");

            registerForm.reset();
            registerForm.classList.add("hidden");
            loginForm.classList.remove("hidden");

        } catch (error) {
            showAuthMessage(error.message, true);
        }
    });


    // Handle login
    loginForm.addEventListener("submit", async (e) => {
        e.preventDefault();

        const email = document.getElementById("loginEmail").value.trim();
        const password = document.getElementById("loginPassword").value;

        try {
            showAuthMessage("Logging in...");

            await loginUser(email, password);

            authScreen.classList.add("hidden");
            mainApp.classList.remove("hidden");

            loginForm.reset();

        } catch (error) {
            showAuthMessage(error.message, true);
        }
    });


    // Restore an existing login session
    async function restoreSession() {

        if (!authToken) {
            return;
        }

        try {
            const response = await fetch(`${API_BASE_URL}/resumes`, {
                method: "GET",
                headers: getAuthHeaders()
            });

            if (response.ok) {
                authScreen.classList.add("hidden");
                mainApp.classList.remove("hidden");
                return;
            }

            if (response.status === 401 || response.status === 403) {
                logoutUser();
                showAuthMessage("Your session has expired. Please log in again.", true);
                return;
            }

            showAuthMessage("Unable to verify your session. Please try again.", true);

        } catch (error) {
            console.error("Session restoration error:", error);
            showAuthMessage("Cannot connect to the server. Please try again.", true);
        }
    }

    // Check for an existing session when the page loads
    restoreSession();

});