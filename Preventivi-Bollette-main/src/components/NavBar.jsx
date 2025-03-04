import { NavLink } from "react-router-dom";
import "../css-components/NavBar.css";

function NavBar() {
	return (
		<nav className="navbar">
			<div className="navbar-brand">
				<box-icon
					className="icon"
					name="bolt-circle"
					type="solid"
					rotate="180"
				></box-icon>
				Consulenza Energetica
			</div>
			<div className="navbar-links">
				<NavLink
					className={({ isActive }) => (isActive ? "active" : "")}
					to="/"
				>
					Home
				</NavLink>
				<NavLink className="nav-btn-1" to="/login">
					Accedi
				</NavLink>
				<NavLink className="nav-btn-2" to="/upload-bill">
					Prova
				</NavLink>
				<NavLink
					className={({ isActive }) => (isActive ? "active" : "")}
					to="/contatti"
				>
					Contatti
				</NavLink>
			</div>
		</nav>
	);
}
export default NavBar;
