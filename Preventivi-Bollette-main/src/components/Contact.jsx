import { useNavigate } from "react-router-dom";
import "../css-components/Contact.css";

function Contact() {
	const navigate = useNavigate();

	return (
		<div className="contact-container">
			<div className="contact-card">
				<h2>Contattaci per un Preventivo</h2>
				<p>
					Per ricevere un preventivo personalizzato sulle tue bollette,
					contattaci ai seguenti recapiti:
				</p>
				<div className="contact-info">
					<p>
						<strong>📍 Indirizzo:</strong> Via Roma 123, Milano, Italia
					</p>
					<p>
						<strong>📞 Telefono:</strong> +39 0123 456 789
					</p>
					<p>
						<strong>✉️ Email:</strong> info@preventivobollette.it
					</p>
					<p>
						<strong>🏢 Ragione Sociale:</strong> Preventivo Bollette S.r.l.
					</p>
				</div>
				<button
					className="contact-button"
					onClick={() => navigate("/upload-bill")}
				>
					Richiedi un Preventivo
				</button>
			</div>
		</div>
	);
}

export default Contact;
