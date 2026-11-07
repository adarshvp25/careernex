const API_BASE_URL = "http://localhost:8081/api";

// Reusable API request function
async function apiRequest(endpoint, options = {}) {
    const response = await fetch(`${API_BASE_URL}${endpoint}`, {
        ...options,
        headers: {
            ...(options.headers || {}),
            ...(authToken ? getAuthHeaders() : {})
        }
    });

    const contentType = response.headers.get("content-type");

    const result = contentType && contentType.includes("application/json")
        ? await response.json()
        : await response.text();

    if (!response.ok) {
        throw new Error(
            typeof result === "string"
                ? result
                : result.message || "Something went wrong"
        );
    }

    return result;
}