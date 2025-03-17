import "../css-components/Footer.css";
import { Link } from "react-router-dom";

function Footer() {
	return (
		<footer className="footer">
			<div className="footer-content">
				<p>© 2025 Preventivo Bollette S.r.l. - Tutti i diritti riservati</p>
				<nav>
					<Link to="/">Home</Link>
					<Link to="/contatti">Contatti</Link>
					<Link to="/privacy">Privacy Policy</Link>
				</nav>
			</div>
		</footer>
	);
}

export default Footer;
