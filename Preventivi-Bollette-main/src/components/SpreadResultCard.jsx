import { useLocation } from "react-router-dom";
import { useState, useEffect } from "react";

import "../css-components/SpreadResultCard.css";
import { useSelector } from "react-redux";

export default function SpreadResultCard() {
	const location = useLocation();
	const token = useSelector(state => state.user.token);
	const { spread, prezzoMercato, tipoBolletta } = location.state || {};

	const [offerteEnergia, setOfferteEnergia] = useState([]);
	const [offerteGas, setOfferteGas] = useState([]);

	// Recupera le offerte Energia
	const fetchOfferteEnergia = async () => {
		try {
			const response = await fetch("http://localhost:8080/offerte/energia", {
				method: "GET",
				headers: {
					"Content-Type": "application/json",
					Authorization: `Bearer ${token}`,
				},
			});
			if (!response.ok) throw new Error("Errore nel recupero offerte energia");
			const data = await response.json();
			setOfferteEnergia(data);
		} catch (error) {
			console.error("Errore:", error);
		}
	};

	// Recupera le offerte Gas
	const fetchOfferteGas = async () => {
		try {
			const response = await fetch("http://localhost:8080/offerte/gas", {
				method: "GET",
				headers: {
					"Content-Type": "application/json",
					Authorization: `Bearer ${token}`,
				},
			});
			if (!response.ok) throw new Error("Errore nel recupero offerte gas");
			const data = await response.json();
			setOfferteGas(data);
		} catch (error) {
			console.error("Errore:", error);
		}
	};

	//in base alla bolletta gas o energia
	useEffect(() => {
		if (tipoBolletta?.toUpperCase() === "GAS") {
			fetchOfferteGas();
		} else {
			fetchOfferteEnergia();
		}
	}, [tipoBolletta]);

	return (
		<div className="spread-result-container">
			<h1>Risultato del Calcolo dello Spread</h1>
			{spread !== undefined ? (
				<>
					<div className="spread-result-card">
						<h2>
							Il prezzo al{" "}
							{tipoBolletta?.toUpperCase() === "GAS" ? "m³" : "kWh"} che applica
							il tuo fornitore è di:{" "}
						</h2>
						<p>
							{spread.toFixed(4)}
							{" € al "}
							{tipoBolletta?.toUpperCase() === "GAS" ? "m³" : "kWh"}
						</p>
						<h2>Prezzo di mercato in base al periodo fatturazione</h2>
						<p>
							{prezzoMercato.toFixed(4)}
							{" € al "}
							{tipoBolletta?.toUpperCase() === "GAS" ? "m³" : "kWh"}
						</p>
					</div>

					{tipoBolletta?.toUpperCase() === "GAS" ? (
						<>
							<h2>Offerte Gas</h2>
							<div className="offers-container">
								{offerteGas.map(offer => (
									<div key={offer.id} className="offer-card">
										<img
											src={offer.logoUrl}
											alt={offer.fornitore}
											className="offer-logo"
										/>
										<h3>{offer.nome}</h3>
										<p>{offer.descrizione}</p>
										<p>
											<strong>Prezzo Smc:</strong> € {offer.prezzoSmc}
										</p>
										<p>
											<strong>Fornitore:</strong> {offer.fornitore}
										</p>
									</div>
								))}
							</div>
						</>
					) : (
						<>
							<h2>Offerte Energia</h2>
							<div className="offers-container">
								{offerteEnergia.map(offer => (
									<div key={offer.id} className="offer-card">
										<img
											src={offer.logoUrl}
											alt={offer.fornitore}
											className="offer-logo"
										/>
										<h3>{offer.nome}</h3>
										<p>{offer.descrizione}</p>
										<p>
											<strong>Prezzo kWh:</strong> € {offer.prezzoKwh}
										</p>
										<p>
											<strong>Fornitore:</strong> {offer.fornitore}
										</p>
									</div>
								))}
							</div>
						</>
					)}
				</>
			) : (
				<p className="error-message">
					Errore: Nessun dato disponibile per il calcolo dello spread.
				</p>
			)}
		</div>
	);
}
