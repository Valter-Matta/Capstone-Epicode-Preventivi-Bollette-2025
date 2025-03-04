import { useState } from "react";
import "../css-components/QuoteForm.css";

export default function QuoteForm() {
	const [quoteType, setQuoteType] = useState(null);
	const [formData, setFormData] = useState({});

	const handleChange = e => {
		setFormData({ ...formData, [e.target.name]: e.target.value });
	};

	const handleFileChange = e => {
		setFormData({ ...formData, [e.target.name]: e.target.files[0] });
	};

	const handleSubmit = async (e) => {
		e.preventDefault();
		const formDataToSend = new FormData();
  
		// Aggiunta del file se presente
		if (formData.bolletta) {
			 formDataToSend.append("file", formData.bolletta);
		} else {
			 // Aggiunta dei dati manuali con formato (mese e anno)
			 formDataToSend.append("data_inizio", formData.data_inizio || '');
			 formDataToSend.append("data_fine", formData.data_fine || '');
			 formDataToSend.append("tipo", quoteType); // Energia o Gas
			 formDataToSend.append("consumo", formData.consumo ? parseFloat(formData.consumo) : 0);
			 formDataToSend.append("consumo_smc", formData.consumo_smc ? parseFloat(formData.consumo_smc) : 0);
			 formDataToSend.append("potenza", formData.potenza ? parseFloat(formData.potenza) : 0);
		}
  
		try {
			 const response = await fetch("http://localhost:8080/api/quote", {
				  method: "POST",
				  body: formDataToSend,
			 });
  
			 if (!response.ok) {
				  const errorData = await response.json();
				  throw new Error(errorData.message || "Errore nell'invio della richiesta");
			 }
  
			 const result = await response.json();
			 console.log("Risultato:", result);
			 alert("Dati inviati con successo!");
  
			 // Resetta il form
			 setFormData({
				  bolletta: null,
				  data_inizio: "",
				  data_fine: "",
				  consumo: "",
				  consumo_smc: "",
				  potenza: "",
			 });
		} catch (error) {
			 console.error("Errore:", error);
			 alert(`Errore durante l'invio dei dati: ${error.message}`);
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

						<p className="attention">Oppure</p>

						<div className="form-group">
							<label>Carica la bolletta</label>
							<input
								required
								name="bolletta"
								type="file"
								onChange={handleFileChange}
							/>
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
				{quoteType === "energia" && "l'energia"}c ad un prezzo onesto.
				Calcoleremo il costo della materia prima in base al prezzo di mercato (
				{quoteType === "energia" && "PSV"}
				{quoteType === "gas" && "PUN"}), e lo compareremo con il costo
				addebitato. ②
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
