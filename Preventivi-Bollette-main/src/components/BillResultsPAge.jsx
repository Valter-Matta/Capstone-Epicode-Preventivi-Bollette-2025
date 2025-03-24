import { useEffect, useState } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import { useSelector } from "react-redux";
import "../css-components/BillResultCard.css";

export default function BillResultsPage() {
	const location = useLocation();
	const navigate = useNavigate();
	const token = useSelector(state => state.user.token); // Prendi il token Redux
	const billData = location.state?.billData; // Recupera i dati dalla navigazione

	const [offerteEnergia, setOfferteEnergia] = useState([]);
	const [offerteGas, setOfferteGas] = useState([]);

	useEffect(() => {
		// Se non ci sono dati di bolletta, torna alla home
		if (!billData) {
			navigate("/");
			return;
		}

		// Recupera le offerte Energia
		const fetchOfferteEnergia = async () => {
			try {
				const response = await fetch("http://localhost:8080/offerte/energia", {
					method: "GET",
					headers: {
						"Content-Type": "application/json",
						Authorization: `Bearer ${token}`, // Inserisce il token
					},
				});
				if (!response.ok)
					throw new Error("Errore nel recupero offerte energia");
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
						Authorization: `Bearer ${token}`, // Inserisce il token
					},
				});
				if (!response.ok) throw new Error("Errore nel recupero offerte gas");
				const data = await response.json();
				setOfferteGas(data);
			} catch (error) {
				console.error("Errore:", error);
			}
		};

		// Esegui le richieste
		fetchOfferteEnergia();
		fetchOfferteGas();
	}, [billData, navigate, token]);

	if (!billData) {
		return (
			<div className="bill-card">
				<h2>Errore</h2>
				<p>Nessun dato di bolletta disponibile. Torna alla home.</p>
				<button onClick={() => navigate("/")}>Torna alla Home</button>
			</div>
		);
	}

	return (
		<div className="bill-card">
			<h2>Risultati della Bolletta</h2>
			<div className="bill-content">
				<div className="bill-row">
					<span className="label">Tipo Bolletta:</span>
					<span className="value">{billData["Tipo Bolletta"] || "N/A"}</span>
				</div>
				<div className="bill-row">
					<span className="label">Periodo di Fatturazione:</span>
					<span className="value">
						{billData["Periodo fatturazione"] || "N/A"}
					</span>
				</div>
				<div className="bill-row">
					<span className="label">Totale Fattura:</span>
					<span className="value">€ {billData["Totale fattura"] || "N/A"}</span>
				</div>
				<div className="bill-row">
					<span className="label">Spesa Materia:</span>
					<span className="value">
						€ {billData["Spesa Materia Energia"] || "N/A"}
					</span>
				</div>
				<div className="bill-row">
					<span className="label">Consumo Fatturato:</span>
					<span className="value">
						{billData["Consumo fatturato"] || "N/A"}{" "}
						{billData["Tipo Bolletta"] === "energia" ? "kW/h" : "Smc"}
					</span>
				</div>
				<div className="bill-row">
					<span className="label">Prezzo Mercato:</span>
					<span className="value">€ {billData["Prezzo Mercato"] || "N/A"}</span>
				</div>
				<div className="bill-row highlight">
					<span className="label">
						Prezzo {billData["Tipo Bolletta"] === "energia" ? "kW/h" : "Smc"}{" "}
						del tuo fornitore attuale{" "}
					</span>
					<span className="value">
						€{" "}
						{(
							billData["Spesa Materia Energia"] / billData["Consumo fatturato"]
						).toFixed(2) || "N/A"}
					</span>
				</div>
			</div>

			{/* Offerte Energia */}
			{billData["Tipo Bolletta"] === "energia" ? (
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
									<strong>Prezzo kW/h:</strong> € {offer.prezzoKwh}
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
			)}

			<button onClick={() => navigate("/")}>Torna alla Home</button>
		</div>
	);
}
