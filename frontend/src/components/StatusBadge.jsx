function StatusBadge({ status }) {
  const labels = {
    PENDING: "Pendente",
    APPROVED: "Aprovada",
    REJECTED: "Rejeitada",
    CANCELLED: "Cancelada",
  };

  return (
    <span className={`status-badge status-${status?.toLowerCase()}`}>
      {labels[status] || status}
    </span>
  );
}

export default StatusBadge;