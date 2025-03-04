import "../css-components/Home.css";
import { Link } from "react-router-dom";

function Home() {
	return (
		<div className="home-container">
			<div className="img-container">
				<h1>
					Risparmia tempo e denaro sulle tue spese energetiche, in pochi clic
				</h1>
				<img
					src="https://i.pinimg.com/564x/e8/e4/a5/e8e4a5fe5344f7dbe7decaad145a44b3.jpg"
					alt=""
					width={"250px"}
					height={"150px"}
				/>
			</div>
			<p>
				Siamo un gruppo di esperti che offrono consulenza <span>gratuita</span>{" "}
				per aiutarti a comprendere meglio le tue bollette di{" "}
				<span>luce e gas.</span>
			</p>
			<p>
				<span>In pochi click</span> puoi verificare la tua effettiva spesa
				dell'energia, caricando una bolletta oppure inserendo i dati
				manualmente.
			</p>
			<h2>Tu sai cosa paghi?</h2>
			<div className="img-details-cont">
				<img
					className="img-details"
					src="/public/Cattura.PNG"
					alt=""
					height={"350px"}
					width={"700px"}
				/>
			</div>
			<p>
				<span className=" spesa-variabile">"Spesa per il gas naturale":</span>{" "}
				unico costo variavile, su cui puoi risparmiare, è la spesa che il tuo
				fornitore ti addebita sulla base del{" "}
				<span className=" consumo">Consumo rilevato</span>
			</p>
			<p>
				Tutte le altre voci in dettaglio sono spese standard{" "}
				<span className="fixed">
					<span>Fisse </span> <box-icon type="solid" name="lock"></box-icon>
				</span>{" "}
				calcolate in base al mercato e in proporzione ai consumi
			</p>
			<div className="home-buttons">
				<Link to="/upload-bill" className="btn">
					Fai il Preventivo
				</Link>
			</div>
		</div>
	);
}

export default Home;
