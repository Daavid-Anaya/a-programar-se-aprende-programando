import { useState } from 'react'
import './App.css'
import { ConverterForm } from './components/ConverterForm'
import { AlertMessage } from './components/AlertMessage'
import { ResultDisplay } from './components/ResultDisplay'
import { BitViewer } from './components/BitViewer'

const API_BASE_URL = import.meta.env.VITE_API_BASE_URL ?? 'http://localhost:8080'

function App() {
  const [binary, setBinary] = useState('');
  const [decimal, setDecimal] = useState(null);
  const [errorMessage, setErrorMessage] = useState('');
  const [isLoading, setIsLoading] = useState(false);

  const handleBinaryChange = (rawValue) => {
    const valorFiltrado = rawValue.replace(/[^01]/g, '');
    const hasInvalidCharacters = valorFiltrado !== rawValue;
    const hasOverflow = valorFiltrado.length > 8;
    const valorFinalLimpio = valorFiltrado.slice(0, 8);

    let errorMessage = '';
    if (hasInvalidCharacters) {
      errorMessage = 'Solo se permiten los dígitos 0 y 1.';
    } else if (hasOverflow) {
      errorMessage = 'La longitud máxima es de 8 dígitos.';
    }

    if (valorFinalLimpio !== binary && decimal !== null) {
      setDecimal(null);
    }

    setBinary(valorFinalLimpio);
    setErrorMessage(errorMessage);
  };

  const handleSubmit = async () => {
      setIsLoading(true);
      try {
        // 1. fetch con await
        const response = await fetch(`${API_BASE_URL}/api/v1/convert?binary=${binary}`, {
          signal: AbortSignal.timeout(3000) // Timeout de 3s
        });

        const data = await response.json();
        
        if (response.ok) {
          // 2. si todo ok -> setDecimal(...)
          setDecimal(data.decimal);
          setErrorMessage('');
        } else if (response.status === 400) {
          setErrorMessage(data.message);
        } else {
          setErrorMessage("Servidor no disponible en este momento.");
        }
      } catch (error) {
        // 3. si explotó la red
        setErrorMessage("Servidor no disponible en este momento.");
      } finally {
        // 4. siempre se apaga la carga
        setIsLoading(false);
      }
    };

  return (
    <main className="app">
      <header className="app-header">
        <h1 className="app-title" translate="no">Bin2Dec</h1>
        <p className="app-subtitle">Conversor de binario a decimal · 8 bits</p>
      </header>

      <section className="panel" aria-labelledby="input-title">
        <h2 id="input-title" className="panel-title">Entrada</h2>
        <ConverterForm value={binary} onChange={handleBinaryChange} onSubmit={handleSubmit} isLoading={isLoading} />
        <AlertMessage message={errorMessage} />
      </section>

      <section className="panel" aria-labelledby="bits-title">
        <div className="panel-heading">
          <h2 id="bits-title" className="panel-title">Registro de bits</h2>
          <p className="panel-hint">Seleccione un bit para alternarlo entre 0 y 1.</p>
        </div>
        <BitViewer value={binary} onChange={handleBinaryChange} disabled={isLoading} />
      </section>

      <section className="panel panel--result" aria-labelledby="result-title">
        <h2 id="result-title" className="panel-title">Salida</h2>
        <ResultDisplay decimal={decimal} />
      </section>
    </main>
  )
}

export default App
