import { useState } from 'react'
import './App.css'
import { ConverterForm } from './components/ConverterForm'
import { AlertMessage } from './components/AlertMessage'
import { ResultDisplay } from './components/ResultDisplay'

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
    <div className="App">
      <h1>Hello, Bin2Dec!</h1>
      <ConverterForm value={binary} onChange={handleBinaryChange} onSubmit={handleSubmit} isLoading={isLoading} />
      <AlertMessage message={errorMessage} />
      <ResultDisplay decimal={decimal} />
    </div>
  )
}

export default App
