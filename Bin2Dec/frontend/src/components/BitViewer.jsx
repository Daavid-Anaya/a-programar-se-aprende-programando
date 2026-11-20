export function BitViewer ({ value, onChange, disabled}) {
    const bits = value.padStart(8, '0').split('');

    const toggleBit = (index) => {
        const nextBits = [...bits];
        nextBits[index] = nextBits[index] === '0' ? '1' : '0';

        onChange(nextBits.join(''));
    };

    return (
        <div className="bit-viewer" role="group" aria-label="Bits del número binario">
            {bits.map((bit, index) => {
                const position = 7 - index;
                const isActive = bit === '1';

                return (
                <button
                    key={position}
                    type="button"
                    className={`bit-card${isActive ? ' bit-card--active' : ''}`}
                    aria-label={`Bit ${position}`}
                    aria-pressed={isActive}
                    disabled={disabled}
                    onClick={() => toggleBit(index)}
                >
                    <span>Bit {position}</span>
                    <span>2<sup>{position}</sup></span>
                    <strong>{bit}</strong>
                    <span>{2 ** position}</span>
                </button>
                );
            })}

        </div>
    );
} 