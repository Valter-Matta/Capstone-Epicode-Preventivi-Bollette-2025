import React, { useState, useEffect } from "react";
import { Bar } from "react-chartjs-2";
import "chart.js/auto";

const PriceComparison = () => {
	const [bollette, setBollette] = useState([]);
	const [selectedBolletta, setSelectedBolletta] = useState("");
	const [confronto, setConfronto] = useState(null);
	const [proposta, setProposta] = useState(null);
	const [error, setError] = useState("");
	const [loading, setLoading] = useState(false);

	// Carica la lista delle bollette disponibili
	useEffect(() => {
		const fetchBollette = async () => {
			try {
				const response = await fetch("http://localhost:8080/api/bollette");
				if (!response.ok)
					throw new Error("Errore nel caricamento delle bollette");
				const data = await response.json();
				setBollette(data);
			} catch (err) {
				setError(err.message);
			}
		};
		fetchBollette();
	}, []);

	const fetchConfrontoPrezzi = async () => {
		if (!selectedBolletta) {
			setError("Seleziona una bolletta.");
			return;
		}
		try {
			setLoading(true);
			setError("");
			const response = await fetch(
				`http://localhost:8080/api/preventivi/confronta/${selectedBolletta}`,
			);
			if (!response.ok) throw new Error("Bolletta non trovata");
			const data = await response.json();
			setConfronto(data);
		} catch (err) {
			setError(err.message);
		} finally {
			setLoading(false);
		}
	};

	const fetchPropostaRisparmio = async () => {
		if (!selectedBolletta) {
			setError("Seleziona una bolletta.");
			return;
		}
		try {
			setLoading(true);
			setError("");
			const response = await fetch(
				`http://localhost:8080/api/preventivi/proposta-risparmio/${selectedBolletta}`,
			);
			if (!response.ok)
				throw new Error("Impossibile generare la proposta di risparmio");
			const data = await response.json();
			setProposta(data);
		} catch (err) {
			setError(err.message);
		} finally {
			setLoading(false);
		}
	};

	return (
		<div className="container">
			<h2>Confronto Prezzi e Risparmio</h2>

			<div className="form-group">
				<label>Seleziona una bolletta</label>
				<select
					value={selectedBolletta}
					onChange={e => setSelectedBolletta(e.target.value)}
				>
					<option value="">-- Seleziona --</option>
					{bollette.map(bolletta => (
						<option key={bolletta.id} value={bolletta.id}>
							Bolletta {bolletta.id} - {bolletta.tipo}
						</option>
					))}
				</select>
			</div>

			<button className="button" onClick={fetchConfrontoPrezzi}>
				Confronta Prezzi
			</button>

			{loading && <p className="loading">Caricamento...</p>}

			{confronto && (
				<div className="result-container">
					<h3>Risultati Confronto</h3>
					<p>
						<strong>Tipo:</strong> {confronto.tipo}
					</p>
					<p>
						<strong>Spread Pagato:</strong> {confronto.spreadPagato.toFixed(4)}{" "}
						€/kWh
					</p>
					<p>
						<strong>Prezzo Mercato:</strong>{" "}
						{confronto.prezzoMercato.toFixed(4)} €/kWh
					</p>
					<p>
						<strong>Risparmio Potenziale:</strong>{" "}
						{confronto.risparmioPotenziale.toFixed(2)} €
					</p>

					<button className="button" onClick={fetchPropostaRisparmio}>
						Genera Proposta Risparmio
					</button>

					<div className="chart-container">
						<Bar
							data={{
								labels: ["Prezzo Pagato", "Prezzo Mercato"],
								datasets: [
									{
										label: "€/kWh",
										data: [confronto.spreadPagato, confronto.prezzoMercato],
										backgroundColor: ["#FF5733", "#33A1FF"],
									},
								],
							}}
							options={{ responsive: true, maintainAspectRatio: false }}
						/>
					</div>
				</div>
			)}

			{proposta && (
				<div className="result-container">
					<h3>Proposta di Risparmio</h3>
					<p>
						<strong>Tipo:</strong> {proposta.tipo}
					</p>
					<p>
						<strong>Nuovo Prezzo:</strong> {proposta.nuovoPrezzo.toFixed(4)}{" "}
						€/kWh
					</p>
					<p>
						<strong>Risparmio Stimato:</strong>{" "}
						{proposta.risparmioStimato.toFixed(2)} €
					</p>
					<p>
						<strong>Suggerimento:</strong> {proposta.suggerimento}
					</p>
				</div>
			)}

			{error && <p className="error">{error}</p>}
		</div>
	);
};

export default PriceComparison;
