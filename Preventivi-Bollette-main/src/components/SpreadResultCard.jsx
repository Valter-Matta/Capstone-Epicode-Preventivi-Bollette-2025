import "../css-components/SpreadResultCard.css";

function SpreadResultCard({ spread, onShowOffers }) {
	return (
		<div className="spread-result-card">
			<h3>Spread Calcolato: {spread.toFixed(2)}%</h3>
			<p>
				Questo è il risultato dello spread calcolato sulla base dei dati che hai
				fornito.
			</p>
			<button onClick={onShowOffers}>Mostra Offerte Più Vantaggiose</button>
		</div>
	);
}

export default SpreadResultCard;
