import { useEffect, useState } from "react";
import "../css-components/QuoteForm.css";
import { useSelector } from "react-redux";
import SpreadResultCard from "./SpreadResultCard";
import { useNavigate } from "react-router-dom";
import BillResultCard from "./BillResultsPAge";

export default function QuoteForm() {
	const token = useSelector(state => state.user.token);
	const user = useSelector(state => state.user.user);
	const navigate = useNavigate();
	const [quoteType, setQuoteType] = useState(null);
	const [formData, setFormData] = useState({});
	const [spread, setSpread] = useState(null);
	const [showResult, setShowResult] = useState(false); // Stato per gestire la visualizzazione del risultato
	const [billData, setBillData] = useState(null); // Stato per i dati estratti

	// Se l'utente non è autenticato, viene reindirizzato alla pagina di login
	useEffect(() => {
		if (!token) {
			navigate("/login");
		}
	}, [navigate, user]);

	const handleChange = e => {
		setFormData({ ...formData, [e.target.name]: e.target.value });
	};

	const handleFileChange = e => {
		setFormData({ ...formData, bolletta: e.target.files[0] });
	};

	const handleSubmit = async e => {
		e.preventDefault();

		try {
			const response = await fetch(
				"http://localhost:8080/api/preventivi/calcola",
				{
					method: "POST",
					headers: {
						"Content-Type": "application/json",
						Authorization: `Bearer ${token}`,
					},
					body: JSON.stringify({
						tipo: quoteType,
						spesaMateriaCliente: parseFloat(formData.spesaMateria),
						consumo: parseInt(formData.consumo),
						meseAnno: formData.data_fine,
					}),
				},
			);

			if (!response.ok) {
				throw new Error("Errore durante il calcolo dello spread");
			}

			const result = await response.json();
			setSpread(result.spread); // Memorizziamo lo spread
			setShowResult(true); // Mostriamo il risultato
		} catch (error) {
			console.error(error);
			alert(`Errore durante il calcolo dello spread: ${error.message}`);
		}
	};

	// Invia il file della bolletta al backend
	const handleFileUpload = async e => {
		e.preventDefault();
		const file = formData.bolletta;

		if (!file) {
			alert("Seleziona un file prima di inviare.");
			return;
		}

		const formDataObj = new FormData();
		formDataObj.append("file", file);

		try {
			const response = await fetch("http://localhost:8080/ocr/extract", {
				method: "POST",
				headers: {
					Authorization: `Bearer ${token}`,
				},
				body: formDataObj,
			});

			if (!response.ok) {
				throw new Error("Errore durante l'elaborazione della bolletta");
			}

			const result = await response.json();
			console.log("📥 Dati ricevuti dal backend:", result); // Debugging

			// Naviga alla nuova pagina passando i dati della bolletta
			navigate("/risultati-bolletta", { state: { billData: result } });
		} catch (error) {
			console.error(error);
			alert(`Errore durante l'elaborazione della bolletta: ${error.message}`);
		}
	};

	const renderForm = () => {
		if (!quoteType) return null;

		return (
			<div className="card">
				<div className="card-content">
					<h2>Inserisci i dati della tua bolletta</h2>
					<form onSubmit={handleSubmit}>
						<div className="form-group">
							<label>
								Periodo di riferimento fatturazione <span>①</span>
							</label>

							<div className="date-range">
								<input
									required
									className="input-data"
									name="data_inizio"
									type="month"
									onChange={handleChange}
									onFocus={e => (e.target.type = "month")}
									onBlur={e =>
										e.target.value === "" ? (e.target.type = "text") : null
									}
									placeholder="Mese e anno inizio"
								/>

								<p>a</p>

								<input
									required
									className="input-data"
									name="data_fine"
									type="month"
									onChange={handleChange}
									onFocus={e => (e.target.type = "month")}
									onBlur={e =>
										e.target.value === "" ? (e.target.type = "text") : null
									}
									placeholder="Mese e anno fine"
								/>
							</div>
						</div>
						{quoteType === "energia" && (
							<div className="form-group">
								<label>
									Spesa per la materia Energia elettrica (€) <span>②</span>{" "}
								</label>
								<input
									required
									name="consumo"
									type="number"
									onChange={handleChange}
								/>
							</div>
						)}
						{quoteType === "gas" && (
							<div className="form-group">
								<label>
									Spesa per la materia Gas (€)<span>②</span>{" "}
								</label>
								<input
									required
									name="consumo"
									type="number"
									onChange={handleChange}
								/>
							</div>
						)}

						{quoteType === "gas" && (
							<div className="form-group">
								<label>
									Consumo in Smc <span>③</span>
								</label>
								<input
									required
									name="consumo_smc"
									type="number"
									onChange={handleChange}
								/>
							</div>
						)}
						{quoteType === "energia" && (
							<div className="form-group">
								<label>
									Potenza impegnata (kW/h) <span>③</span>{" "}
								</label>
								<input
									required
									name="potenza"
									type="number"
									onChange={handleChange}
								/>
							</div>
						)}
						<button type="submit">Calcola</button>
					</form>
					<form onSubmit={handleFileUpload}>
						<p className="attention">Oppure</p>

						<div className="form-group">
							<label>Carica la bolletta</label>
							<input name="bolletta" type="file" onChange={handleFileChange} />
						</div>
						<button type="submit">Invia</button>
					</form>
				</div>

				{quoteType === "energia" && (
					<img
						className="img-info"
						src="1 (4).png"
						width={"50%"}
						height={"75%"}
						alt=""
					/>
				)}
				{quoteType === "gas" && (
					<img
						className="img-info"
						src="1 (3).png"
						width={"50%"}
						height={"75%"}
						alt=""
					/>
				)}
			</div>
		);
	};

	return (
		<div className="container-prev">
			<h1>Richiedi un preventivo</h1>
			<p>
				Compilando questo modulo, puoi ottenere un preventivo senza rischi:
				<span> nessuna attivazione avverrà senza il tuo consenso.</span>
			</p>
			<p className="attention">
				Inserendo manualmente le 3 voci richieste nella foto saremo in grado di
				decretare se il tuo fornitore ti sta vendendo{" "}
				{quoteType === "energia" && "l'energia"} ad un prezzo onesto.
				Calcoleremo il costo della materia prima in base al prezzo di mercato (
				{quoteType === "energia" && "PSV "}
				{quoteType === "gas" && "PUN "} rif. mese/anno), e lo compareremo con il
				costo addebitato.
				{quoteType === "energia" || quoteType === "gas" ? "②" : ""}
			</p>
			<select onChange={e => setQuoteType(e.target.value)}>
				<option value="">Seleziona il tipo di bolletta</option>
				<option value="energia">Preventivo Energia Elettrica</option>
				<option value="gas">Preventivo Gas</option>
			</select>
			{renderForm()}
		</div>
	);
}
