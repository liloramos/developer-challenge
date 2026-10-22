import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { apiRequest } from "../api/api";

function LoginPage() {
  const navigate = useNavigate();

  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);

  async function handleSubmit(event) {
    event.preventDefault();

    setError("");
    setLoading(true);

    try {
      const response = await apiRequest("/auth/login", {
        method: "POST",
        body: JSON.stringify({
          email,
          password,
        }),
      });

      localStorage.setItem("token", response.token);
      localStorage.setItem(
        "user",
        JSON.stringify(response.user)
      );

      navigate("/requests");
    } catch (err) {
      setError(
        err.status === 401
          ? "E-mail ou senha inválidos."
          : err.message
      );
    } finally {
      setLoading(false);
    }
  }

  return (
    <main className="login-page">
      <section className="login-brand">
        <div className="brand-content">
          <div>
            <p className="brand-company">
              KINGSPAN ISOESTE
            </p>

            <h1>
              Purchase Request
              <br />
              Management
            </h1>

            <p className="brand-description">
              Gestão simples, segura e rastreável
              de solicitações de compra.
            </p>
          </div>

          <div className="brand-footer">
            <span className="brand-line" />
            <span>Construindo o Futuro</span>
          </div>
        </div>
      </section>

      <section className="login-area">
        <div className="login-card">
          <div className="login-header">
            <span className="login-eyebrow">
              ÁREA RESTRITA
            </span>

            <h2>Bem-vindo</h2>

            <p>
              Entre com suas credenciais para acessar
              o sistema.
            </p>
          </div>

          <form onSubmit={handleSubmit}>
            <div className="form-group">
              <label htmlFor="email">
                E-mail
              </label>

              <input
                id="email"
                type="email"
                placeholder="nome@empresa.com"
                value={email}
                onChange={(event) =>
                  setEmail(event.target.value)
                }
                required
                autoComplete="email"
              />
            </div>

            <div className="form-group">
              <label htmlFor="password">
                Senha
              </label>

              <input
                id="password"
                type="password"
                placeholder="Digite sua senha"
                value={password}
                onChange={(event) =>
                  setPassword(event.target.value)
                }
                required
                autoComplete="current-password"
              />
            </div>

            {error && (
              <div className="form-error">
                {error}
              </div>
            )}

            <button
              className="primary-button"
              type="submit"
              disabled={loading}
            >
              {loading
                ? "Entrando..."
                : "Entrar no sistema"}
            </button>
          </form>

          <p className="login-security">
            Acesso protegido por autenticação JWT
          </p>
        </div>
      </section>
    </main>
  );
}

export default LoginPage;
