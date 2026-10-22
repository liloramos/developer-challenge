const API_URL = "http://localhost:8080";

export async function apiRequest(path, options = {}) {
  const token = localStorage.getItem("token");

  const headers = {
    "Content-Type": "application/json",
    ...options.headers,
  };

  if (token) {
    headers.Authorization = `Bearer ${token}`;
  }

  const response = await fetch(`${API_URL}${path}`, {
    ...options,
    headers,
  });

  let body = null;

  const contentType = response.headers.get("content-type");

  if (contentType?.includes("application/json")) {
    body = await response.json();
  }

  if (!response.ok) {
    const error = new Error(
      body?.message || "An unexpected error occurred."
    );

    error.status = response.status;
    error.details = body?.details || [];

    throw error;
  }

  return body;
}
