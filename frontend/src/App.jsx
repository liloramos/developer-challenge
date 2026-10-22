import {
  Navigate,
  Route,
  Routes,
} from "react-router-dom";

import LoginPage from "./pages/LoginPage";
import RequestsPage from "./pages/RequestsPage";
import NewRequestPage from "./pages/NewRequestPage";

function App() {
  return (
    <Routes>
      <Route
        path="/"
        element={
          <Navigate
            to="/login"
            replace
          />
        }
      />

      <Route
        path="/login"
        element={<LoginPage />}
      />

      <Route
        path="/requests"
        element={<RequestsPage />}
      />

      <Route
        path="/requests/new"
        element={<NewRequestPage />}
      />

      <Route
        path="/requests/:id"
        element={
          <h1>Request Details</h1>
        }
      />
    </Routes>
  );
}

export default App;