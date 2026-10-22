import { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import { apiRequest } from "../api/api";

function NewRequestPage() {
  const navigate = useNavigate();

  const [form, setForm] = useState({
    title: "",
    description: "",
    amount: "",
    category: "EQUIPMENT",
  });

  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);

  function handleChange(event) {
    const { name, value } = event.target;

    setForm((current) => ({
      ...current,
      [name]: value,
    }));
  }

  async function handleSubmit(event) {
    event.preventDefault();

    setError("");
    setLoading(true);

    try {
      const response = await apiRequest("/requests", {
        method: "POST",
        body: JSON.stringify({
          title: form.title,
          description: form.description,
          amount: Number(form.amount),
          category: form.category,
        }),
      });

      navigate(`/requests/${response.data.id}`);
    } catch (err) {
      if (err.details?.length) {
        setError(err.details.join(" • "));
      } else {
        setError(err.message);
      }
    } finally {
      setLoading(false);
    }
  }

  return (
    <div className="app-shell">
      <header className="app-header">
        <div className="app-header-inner">
          <Link to="/requests" className="app-brand">
            <div className="app-brand-mark">KI</div>

            <div>
              <strong>KINGSPAN ISOESTE</strong>
              <span>Purchase Request Management</span>
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

      <main className="page-container form-page-container">
        <div className="page-heading form-page-heading">
          <div>
            <span className="page-eyebrow">
              NOVA SOLICITAÇÃO
            </span>

            <h1>Solicitação de Compra</h1>

            <p>
              Preencha os dados abaixo. O nível de aprovação
              será calculado automaticamente pelo sistema.
            </p>
          </div>
        </div>

        <div className="request-form-layout">
          <section className="form-card">
            <form onSubmit={handleSubmit}>
              <div className="form-section-heading">
                <span>01</span>

                <div>
                  <h2>Informações da solicitação</h2>
                  <p>
                    Informe o que será adquirido e o motivo
                    da solicitação.
                  </p>
                </div>
              </div>

              <div className="form-group">
                <label htmlFor="title">
                  Título da solicitação
                </label>

                <input
                  id="title"
                  name="title"
                  type="text"
                  value={form.title}
                  onChange={handleChange}
                  placeholder="Ex.: Notebook para equipe comercial"
                  maxLength={200}
                  required
                />
              </div>

              <div className="form-group">
                <label htmlFor="description">
                  Descrição
                </label>

                <textarea
                  id="description"
                  name="description"
                  value={form.description}
                  onChange={handleChange}
                  placeholder="Descreva a necessidade da compra"
                  rows={5}
                  maxLength={2000}
                  required
                />
              </div>

              <div className="form-grid">
                <div className="form-group">
                  <label htmlFor="amount">
                    Valor
                  </label>

                  <input
                    id="amount"
                    name="amount"
                    type="number"
                    min="0.01"
                    step="0.01"
                    value={form.amount}
                    onChange={handleChange}
                    placeholder="0,00"
                    required
                  />
                </div>

                <div className="form-group">
                  <label htmlFor="category">
                    Categoria
                  </label>

                  <select
                    id="category"
                    name="category"
                    value={form.category}
                    onChange={handleChange}
                  >
                    <option value="EQUIPMENT">
                      Equipamentos
                    </option>

                    <option value="SERVICES">
                      Serviços
                    </option>

                    <option value="SUPPLIES">
                      Suprimentos
                    </option>

                    <option value="TRAVEL">
                      Viagens
                    </option>

                    <option value="SOFTWARE">
                      Software
                    </option>

                    <option value="OTHER">
                      Outros
                    </option>
                  </select>
                </div>
              </div>

              {error && (
                <div className="form-error">
                  {error}
                </div>
              )}

              <div className="form-actions">
                <Link
                  to="/requests"
                  className="secondary-link-button"
                >
                  Cancelar
                </Link>

                <button
                  type="submit"
                  className="form-submit-button"
                  disabled={loading}
                >
                  {loading
                    ? "Enviando..."
                    : "Criar solicitação"}
                </button>
              </div>
            </form>
          </section>

          <aside className="approval-guide">
            <span className="approval-guide-eyebrow">
              FLUXO DE APROVAÇÃO
            </span>

            <h3>Nível calculado pelo valor</h3>

            <p>
              O sistema classifica automaticamente a
              solicitação no nível necessário de aprovação.
            </p>

            <div className="approval-rule">
              <div className="approval-rule-level">
                Nível 1
              </div>

              <div>
                <strong>Até R$ 1.000,00</strong>
                <span>Aprovação operacional</span>
              </div>
            </div>

            <div className="approval-rule">
              <div className="approval-rule-level">
                Nível 2
              </div>

              <div>
                <strong>
                  R$ 1.000,01 até R$ 10.000,00
                </strong>
                <span>Aprovação intermediária</span>
              </div>
            </div>

            <div className="approval-rule">
              <div className="approval-rule-level">
                Nível 3
              </div>

              <div>
                <strong>Acima de R$ 10.000,00</strong>
                <span>Somente administrador</span>
              </div>
            </div>
          </aside>
        </div>
      </main>
    </div>
  );
}

export default NewRequestPage;