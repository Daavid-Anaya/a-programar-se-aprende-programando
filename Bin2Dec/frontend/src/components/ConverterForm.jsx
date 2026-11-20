export function ConverterForm({value, onChange, onSubmit, isLoading}) {
    const estaVacio = !value || value.trim() === '';
    // Manejador interno para el envó del formulario
    const ejecutarEnvio = (e) => {  
        e.preventDefault(); // Evita que la página se recargue
        if(estaVacio || isLoading) return; // No se envía si el input está vacío o si ya se está cargando
        onSubmit(); // Avisa al componentepadre: cuando se envía el formulario
    } 

    return (
        <form className="converter-form" onSubmit={ejecutarEnvio}>
            <label className="field-label" htmlFor="binary-input">Número Binario</label>
            <div className="field-row">
                <input
                    id="binary-input"
                    className="binary-input"
                    name="binary"
                    type="text"
                    inputMode="numeric" // Teclado numérico en móviles
                    autoComplete="off"
                    spellCheck={false}
                    aria-describedby="binary-help"
                    value={value}
                    onChange={(e) => onChange(e.target.value)} // Pasa el valor del input al componente padre
                    placeholder = "Ej. 10110101…"
                    disabled={isLoading} // Se deshabilita el input mientras se está cargando la conversión
                />

                <button className="convert-button" type="submit" disabled={isLoading || estaVacio}>
                    {isLoading ? "Convirtiendo…" : "Convertir"}
                </button>
            </div>
            <p id="binary-help" className="field-help">Solo dígitos 0 y 1, hasta 8 dígitos.</p>
        </form>
    );
}