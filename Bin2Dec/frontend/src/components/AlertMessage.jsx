export function AlertMessage({message}) {
    if (!message) return null // No se muestra nada si no hay mensaje
    return (
        <p className="alert-message" role="alert" aria-live="polite">
            {message}
        </p>
    );
}