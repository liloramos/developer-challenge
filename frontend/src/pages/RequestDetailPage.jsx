import {
  useCallback,
  useEffect,
  useState,
} from "react";
import {
  Link,
  useNavigate,
  useParams,
} from "react-router-dom";

import { apiRequest } from "../api/api";
import StatusBadge from "../components/StatusBadge";

function RequestDetailPage() {
  const { id } = useParams();
  const navigate = useNavigate();

  const user = JSON.parse(
    localStorage.getItem("user") || "null"
  );

  const [request, setRequest] = useState(null);
  const [history, setHistory] = useState([]);
  const [comment, setComment] = useState("");

  const [loading, setLoading] = useState(true);
  const [actionLoading, setActionLoading] =
    useState(false);

  const [error, setError] = useState("");
  const [success, setSuccess] = useState("");

  const logout = useCallback(() => {
    localStorage.removeItem("token");
    localStorage.removeItem("user");

    navigate("/login");
  }, [navigate]);

  const loadData = useCallback(async () => {
    try {
      const [
        requestResponse,
        historyResponse,
      ] = await Promise.all([
        apiRequest(`/requests/${id}`),
        apiRequest(`/requests/${id}/history`),
      ]);

      setRequest(requestResponse.data);
      setHistory(historyResponse.data);
    } catch (err) {
      if (err.status === 401) {
        logout();
        return;
      }

      setError(err.message);
    } finally {
      setLoading(false);
    }
  }, [id, logout]);

  useEffect(() => {
    const timeoutId = setTimeout(() => {
      loadData();
    }, 0);

    return () => {
      clearTimeout(timeoutId);
    };
  }, [loadData]);

  async function performAction(action) {
    setActionLoading(true);
    setError("");
    setSuccess("");

    try {
      const options = {
        method: "POST",
      };

      if (
        action === "approve" ||
        action === "reject"
      ) {
        options.body = JSON.stringify({
          comment: comment || null,
        });
      }

      await apiRequest(
        `/requests/${id}/${action}`,
        options
      );

      const messages = {
        approve:
          "Solicitação aprovada com sucesso.",
        reject:
          "Solicitação rejeitada com sucesso.",
        cancel:
          "Solicitação cancelada com sucesso.",
      };

      setSuccess(messages[action]);
      setComment("");

      await loadData();
    } catch (err) {
      if (err.details?.length) {
        setError(err.details.join(" • "));
      } else {
        setError(err.message);
      }
    } finally {
      setActionLoading(false);
    }
  }

  function formatCurrency(value) {
    return new Intl.NumberFormat("pt-BR", {
      style: "currency",
      currency: "BRL",
    }).format(value);
  }

  function formatDate(value) {
    return new Intl.DateTimeFormat("pt-BR", {
      dateStyle: "medium",
      timeStyle: "short",
    }).format(new Date(value));
  }

  function categoryLabel(category) {
    const labels = {
      EQUIPMENT: "Equipamentos",
      SERVICES: "Serviços",
      SUPPLIES: "Suprimentos",
      TRAVEL: "Viagens",
      SOFTWARE: "Software",
      OTHER: "Outros",
    };

    return labels[category] || category;
  }

  function historyLabel(action) {
    const labels = {
      CREATED: "Solicitação criada",
      APPROVED: "Solicitação aprovada",
      REJECTED: "Solicitação rejeitada",
      CANCELLED: "Solicitação cancelada",
    };

    return labels[action] || action;
  }

  function hasCompatibleApprovalLevel() {
    if (user?.role === "ADMIN") {
      return true;
    }

    if (user?.role !== "APROVADOR") {
      return false;
    }

    if (
      request.requiredApprovalLevel ===
      "NIVEL_1"
    ) {
      return (
        user.approverLevel === "NIVEL_1" ||
        user.approverLevel === "NIVEL_2"
      );
    }

    if (
      request.requiredApprovalLevel ===
      "NIVEL_2"
    ) {
      return (
        user.approverLevel === "NIVEL_2"
      );
    }

    return false;
  }

  if (loading) {
    return (
      <div className="app-shell">
        <div className="detail-loading">
          Carregando solicitação...
        </div>
      </div>
    );
  }

  if (!request) {
    return (
      <div className="app-shell">
        <main className="page-container">
          <div className="page-error">
            {error ||
              "Solicitação não encontrada."}
          </div>

          <Link
            to="/requests"
            className="details-link"
          >
            ← Voltar
          </Link>
        </main>
      </div>
    );
  }

  const isPending =
    request.status === "PENDING";

  const canApprove =
    isPending &&
    hasCompatibleApprovalLevel();

  const canCancel =
    isPending &&
    (
      user?.role === "ADMIN" ||
      (
        user?.role === "SOLICITANTE" &&
        user?.id === request.requesterId
      )
    );

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

          <Link
            to="/requests"
            className="header-back-link"
          >
            ← Voltar às solicitações
          </Link>
        </div>
      </header>

      <main className="page-container detail-container">
        <div className="detail-heading">
          <div>
            <span className="page-eyebrow">
              SOLICITAÇÃO #{request.id}
            </span>

            <div className="detail-title-row">
              <h1>
                {request.title}
              </h1>

              <StatusBadge
                status={request.status}
              />
            </div>

            <p>
              Criada por{" "}
              {request.requesterName} em{" "}
              {formatDate(
                request.createdAt
              )}
            </p>
          </div>

          <div className="detail-amount">
            <span>
              VALOR DA SOLICITAÇÃO
            </span>

            <strong>
              {formatCurrency(
                request.amount
              )}
            </strong>
          </div>
        </div>

        {error && (
          <div className="page-error">
            {error}
          </div>
        )}

        {success && (
          <div className="page-success">
            {success}
          </div>
        )}

        <div className="detail-layout">
          <div className="detail-main">
            <section className="detail-card">
              <div className="detail-card-header">
                <div>
                  <span className="detail-section-number">
                    01
                  </span>

                  <h2>
                    Dados da solicitação
                  </h2>
                </div>
              </div>

              <div className="detail-description">
                <span>
                  Descrição
                </span>

                <p>
                  {request.description}
                </p>
              </div>

              <div className="detail-info-grid">
                <div>
                  <span>
                    Categoria
                  </span>

                  <strong>
                    {categoryLabel(
                      request.category
                    )}
                  </strong>
                </div>

                <div>
                  <span>
                    Nível necessário
                  </span>

                  <strong>
                    {request
                      .requiredApprovalLevel
                      .replace(
                        "NIVEL_",
                        "Nível "
                      )}
                  </strong>
                </div>

                <div>
                  <span>
                    Solicitante
                  </span>

                  <strong>
                    {request.requesterName}
                  </strong>
                </div>

                <div>
                  <span>
                    Última atualização
                  </span>

                  <strong>
                    {formatDate(
                      request.updatedAt
                    )}
                  </strong>
                </div>
              </div>
            </section>

            <section className="detail-card history-card">
              <div className="detail-card-header">
                <div>
                  <span className="detail-section-number">
                    02
                  </span>

                  <h2>
                    Histórico
                  </h2>
                </div>

                <span className="history-count">
                  {history.length}{" "}
                  {history.length === 1
                    ? "movimentação"
                    : "movimentações"}
                </span>
              </div>

              <div className="timeline">
                {history.map((item) => (
                  <div
                    className="timeline-item"
                    key={item.id}
                  >
                    <div
                      className={
                        `timeline-dot timeline-${item.action.toLowerCase()}`
                      }
                    />

                    <div className="timeline-content">
                      <div className="timeline-top">
                        <strong>
                          {historyLabel(
                            item.action
                          )}
                        </strong>

                        <span>
                          {formatDate(
                            item.createdAt
                          )}
                        </span>
                      </div>

                      <p>
                        Por{" "}
                        <strong>
                          {item.actorName}
                        </strong>
                      </p>

                      {item.comment && (
                        <blockquote>
                          {item.comment}
                        </blockquote>
                      )}
                    </div>
                  </div>
                ))}
              </div>
            </section>
          </div>

          <aside className="detail-sidebar">
            <section className="detail-summary-card">
              <span className="detail-sidebar-eyebrow">
                STATUS ATUAL
              </span>

              <StatusBadge
                status={request.status}
              />

              <div className="summary-divider" />

              <div className="summary-row">
                <span>
                  Nível
                </span>

                <strong>
                  {request
                    .requiredApprovalLevel
                    .replace(
                      "NIVEL_",
                      "Nível "
                    )}
                </strong>
              </div>

              <div className="summary-row">
                <span>
                  Valor
                </span>

                <strong>
                  {formatCurrency(
                    request.amount
                  )}
                </strong>
              </div>

              <div className="summary-row">
                <span>
                  Categoria
                </span>

                <strong>
                  {categoryLabel(
                    request.category
                  )}
                </strong>
              </div>
            </section>

            {canApprove && (
              <section className="action-card">
                <span className="detail-sidebar-eyebrow">
                  DECISÃO
                </span>

                <h3>
                  Avaliar solicitação
                </h3>

                <p>
                  O comentário é opcional
                  e será registrado no
                  histórico.
                </p>

                <textarea
                  value={comment}
                  onChange={(event) =>
                    setComment(
                      event.target.value
                    )
                  }
                  placeholder="Adicionar comentário..."
                  maxLength={1000}
                  rows={4}
                />

                <div className="approval-actions">
                  <button
                    type="button"
                    className="reject-button"
                    disabled={
                      actionLoading
                    }
                    onClick={() =>
                      performAction(
                        "reject"
                      )
                    }
                  >
                    Rejeitar
                  </button>

                  <button
                    type="button"
                    className="approve-button"
                    disabled={
                      actionLoading
                    }
                    onClick={() =>
                      performAction(
                        "approve"
                      )
                    }
                  >
                    Aprovar
                  </button>
                </div>
              </section>
            )}

            {isPending &&
              user?.role ===
                "APROVADOR" &&
              !canApprove && (
                <section className="terminal-state-card">
                  <strong>
                    Nível de aprovação
                    insuficiente
                  </strong>

                  <p>
                    Esta solicitação exige{" "}
                    {request
                      .requiredApprovalLevel
                      .replace(
                        "NIVEL_",
                        "Nível "
                      )}
                    . Seu perfil não
                    possui permissão para
                    avaliá-la.
                  </p>
                </section>
              )}

            {canCancel && (
              <section className="cancel-card">
                <div>
                  <strong>
                    Cancelar solicitação
                  </strong>

                  <p>
                    Solicitações canceladas
                    não podem ser
                    reabertas.
                  </p>
                </div>

                <button
                  type="button"
                  disabled={
                    actionLoading
                  }
                  onClick={() =>
                    performAction(
                      "cancel"
                    )
                  }
                >
                  Cancelar solicitação
                </button>
              </section>
            )}

            {!isPending && (
              <section className="terminal-state-card">
                <strong>
                  Solicitação finalizada
                </strong>

                <p>
                  Este status é terminal
                  e não permite novas
                  transições.
                </p>
              </section>
            )}
          </aside>
        </div>
      </main>
    </div>
  );
}

export default RequestDetailPage;
