export function ResultDisplay({ decimal }) {
    const valorMostrar = decimal != null ? decimal : '-';
    return (
        <div className="result-container" aria-live="polite">
            <span className="result-label">Resultado Decimal</span>
            <div className="result-value">{valorMostrar}</div>
        </div>
    );
}