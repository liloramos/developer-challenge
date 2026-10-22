import {
  useCallback,
  useEffect,
  useState,
} from "react";
import {
  Link,
  useNavigate,
} from "react-router-dom";

import { apiRequest } from "../api/api";
import StatusBadge from "../components/StatusBadge";

function RequestsPage() {
  const navigate = useNavigate();

  const user = JSON.parse(
    localStorage.getItem("user") || "null"
  );

  const [requests, setRequests] = useState([]);
  const [status, setStatus] = useState("");
  const [page, setPage] = useState(0);
  const [totalPages, setTotalPages] =
    useState(0);
  const [totalElements, setTotalElements] =
    useState(0);

  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  const logout = useCallback(() => {
    localStorage.removeItem("token");
    localStorage.removeItem("user");

    navigate("/login");
  }, [navigate]);

  const loadRequests = useCallback(async () => {
    setLoading(true);
    setError("");

    try {
      const params = new URLSearchParams({
        page: String(page),
        size: "10",
      });

      if (status) {
        params.set("status", status);
      }

      const response = await apiRequest(
        `/requests?${params.toString()}`
      );

      setRequests(response.data.content);
      setTotalPages(response.data.totalPages);
      setTotalElements(
        response.data.totalElements
      );
    } catch (err) {
      if (err.status === 401) {
        logout();
        return;
      }

      setError(err.message);
    } finally {
      setLoading(false);
    }
  }, [page, status, logout]);

  useEffect(() => {
    const timeoutId = setTimeout(() => {
      loadRequests();
    }, 0);

    return () => {
      clearTimeout(timeoutId);
    };
  }, [loadRequests]);

  function handleStatusChange(event) {
    setStatus(event.target.value);
    setPage(0);
  }

  function formatCurrency(value) {
    return new Intl.NumberFormat("pt-BR", {
      style: "currency",
      currency: "BRL",
    }).format(value);
  }

  function formatDate(value) {
    return new Intl.DateTimeFormat("pt-BR", {
      dateStyle: "short",
      timeStyle: "short",
    }).format(new Date(value));
  }

  function roleLabel(role) {
    const roles = {
      SOLICITANTE: "Solicitante",
      APROVADOR: "Aprovador",
      ADMIN: "Administrador",
    };

    return roles[role] || role;
  }

  return (
    <div className="app-shell">
      <header className="app-header">
        <div className="app-header-inner">
          <Link
            to="/requests"
            className="app-brand"
          >
            <div className="app-brand-mark">
              KI
            </div>

            <div>
              <strong>
                KINGSPAN ISOESTE
              </strong>

              <span>
                Purchase Request Management
              </span>
            </div>
          </Link>

          <div className="user-area">
            <div className="user-info">
              <strong>
                {user?.name}
              </strong>

              <span>
                {roleLabel(user?.role)}

                {user?.approverLevel
                  ? ` • ${user.approverLevel.replace(
                      "_",
                      " "
                    )}`
                  : ""}
              </span>
            </div>

            <button
              type="button"
              className="logout-button"
              onClick={logout}
            >
              Sair
            </button>
          </div>
        </div>
      </header>

      <main className="page-container">
        <div className="page-heading">
          <div>
            <span className="page-eyebrow">
              GESTÃO DE COMPRAS
            </span>

            <h1>
              Solicitações de Compra
            </h1>

            <p>
              Acompanhe solicitações,
              aprovações e histórico de
              movimentações.
            </p>
          </div>

          {user?.role === "SOLICITANTE" && (
            <Link
              to="/requests/new"
              className="primary-link-button"
            >
              + Nova solicitação
            </Link>
          )}
        </div>

        <section className="requests-toolbar">
          <div className="requests-summary">
            <strong>
              {totalElements}
            </strong>

            <span>
              {totalElements === 1
                ? "solicitação encontrada"
                : "solicitações encontradas"}
            </span>
          </div>

          <div className="filter-group">
            <label htmlFor="status">
              Status
            </label>

            <select
              id="status"
              value={status}
              onChange={handleStatusChange}
            >
              <option value="">
                Todos
              </option>

              <option value="PENDING">
                Pendentes
              </option>

              <option value="APPROVED">
                Aprovadas
              </option>

              <option value="REJECTED">
                Rejeitadas
              </option>

              <option value="CANCELLED">
                Canceladas
              </option>
            </select>
          </div>
        </section>

        {error && (
          <div className="page-error">
            {error}
          </div>
        )}

        <section className="requests-card">
          {loading ? (
            <div className="empty-state">
              Carregando solicitações...
            </div>
          ) : requests.length === 0 ? (
            <div className="empty-state">
              <div className="empty-icon">
                PR
              </div>

              <h3>
                Nenhuma solicitação
                encontrada
              </h3>

              <p>
                Não existem solicitações
                para os filtros
                selecionados.
              </p>
            </div>
          ) : (
            <div className="table-wrapper">
              <table className="requests-table">
                <thead>
                  <tr>
                    <th>
                      Solicitação
                    </th>

                    <th>
                      Solicitante
                    </th>

                    <th>
                      Valor
                    </th>

                    <th>
                      Nível
                    </th>

                    <th>
                      Status
                    </th>

                    <th>
                      Data
                    </th>

                    <th />
                  </tr>
                </thead>

                <tbody>
                  {requests.map(
                    (request) => (
                      <tr key={request.id}>
                        <td>
                          <div className="request-title-cell">
                            <strong>
                              {request.title}
                            </strong>

                            <span>
                              {request.category}
                            </span>
                          </div>
                        </td>

                        <td>
                          {
                            request.requesterName
                          }
                        </td>

                        <td className="amount-cell">
                          {formatCurrency(
                            request.amount
                          )}
                        </td>

                        <td>
                          <span className="level-badge">
                            {request
                              .requiredApprovalLevel
                              .replace(
                                "NIVEL_",
                                "Nível "
                              )}
                          </span>
                        </td>

                        <td>
                          <StatusBadge
                            status={
                              request.status
                            }
                          />
                        </td>

                        <td className="date-cell">
                          {formatDate(
                            request.createdAt
                          )}
                        </td>

                        <td className="action-cell">
                          <Link
                            to={`/requests/${request.id}`}
                            className="details-link"
                          >
                            Ver detalhes
                            <span>
                              →
                            </span>
                          </Link>
                        </td>
                      </tr>
                    )
                  )}
                </tbody>
              </table>
            </div>
          )}

          {!loading &&
            totalPages > 1 && (
              <div className="pagination">
                <button
                  type="button"
                  disabled={page === 0}
                  onClick={() =>
                    setPage((current) =>
                      Math.max(
                        current - 1,
                        0
                      )
                    )
                  }
                >
                  ← Anterior
                </button>

                <span>
                  Página {page + 1} de{" "}
                  {totalPages}
                </span>

                <button
                  type="button"
                  disabled={
                    page + 1 >= totalPages
                  }
                  onClick={() =>
                    setPage(
                      (current) =>
                        current + 1
                    )
                  }
                >
                  Próxima →
                </button>
              </div>
            )}
        </section>
      </main>
    </div>
  );
}

export default RequestsPage;
