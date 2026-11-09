export function ConverterForm({value, onChange, onSubmit, isLoading}) {
    const estaVacio = !value || value.trim() === '';
    // Manejador interno para el envó del formulario
    const ejecutarEnvio = (e) => {  
        e.preventDefault(); // Evita que la página se recargue
        if(estaVacio || isLoading) return; // No se envía si el input está vacío o si ya se está cargando
        onSubmit(); // Avisa al componentepadre: cuando se envía el formulario
    } 

    return (
        <form onSubmit={ejecutarEnvio}>
            <label htmlFor="binary-input">Número Binario</label>
            <input
                id="binary-input"
                type="text"
                value={value}
                onChange={(e) => onChange(e.target.value)} // Pasa el valor del input al componente padre
                placeholder = "Ingrese un número binario"
                disabled={isLoading} // Se deshabilita el input mientras se está cargando la conversión
            />

            <button type="submit" disabled={isLoading || estaVacio}>
                {isLoading ? "Convirtiendo..." : "Convertir"}
            </button>
        </form>
    );
}