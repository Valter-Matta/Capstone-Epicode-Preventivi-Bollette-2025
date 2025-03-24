import { NavLink } from "react-router-dom";
import { useSelector, useDispatch } from "react-redux";
import { logoutUser } from "../redux/actions";
import { useState } from "react";
import "../css-components/NavBar.css";

function NavBar() {
	const dispatch = useDispatch();
	const token = useSelector(state => state.user.token);
	const user = useSelector(state => state.user.user);
	const [showModal, setShowModal] = useState(false);
	const [menuOpen, setMenuOpen] = useState(false); // Stato per il menu hamburger

	const handleLogout = () => {
		dispatch(logoutUser());
		setShowModal(false);
		setMenuOpen(false);
	};

	const toggleMenu = () => {
		setMenuOpen(!menuOpen);
	};

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

			{/* Menu Hamburger */}
			<div className="hamburger" onClick={toggleMenu}>
				<div className={`bar ${menuOpen ? "open" : ""}`}></div>
				<div className={`bar ${menuOpen ? "open" : ""}`}></div>
				<div className={`bar ${menuOpen ? "open" : ""}`}></div>
			</div>

			<div className={`navbar-links ${menuOpen ? "active" : ""}`}>
				<NavLink onClick={() => setMenuOpen(false)} to="/">
					Home
				</NavLink>
				<NavLink onClick={() => setMenuOpen(false)} to="/contatti">
					Contatti
				</NavLink>
				{token && user && (
					<NavLink
						onClick={() => setMenuOpen(false)}
						className="nav-btn-2"
						to="/upload-bill"
					>
						Prova
					</NavLink>
				)}

				{token && user ? (
					<div
						className="user-avatar"
						onClick={e => {
							e.stopPropagation();
							setShowModal(prev => !prev);
						}}
					>
						<img
							src={`https://api.dicebear.com/7.x/initials/svg?seed=${user.nome}&backgroundColor=19c2ba`}
							alt="avatar"
						/>
						<span>Ciao, {user.nome}</span>
						{showModal && (
							<div
								className={`modal ${showModal ? "show" : ""}`}
								onClick={e => e.stopPropagation()}
							>
								<h4>
									{user.nome} {user.cognome}
								</h4>
								<p>{user.email}</p>
								<button className="logout-btn" onClick={handleLogout}>
									Logout
								</button>
							</div>
						)}
					</div>
				) : (
					<NavLink
						onClick={() => setMenuOpen(false)}
						className="nav-btn-1"
						to="/login"
					>
						Accedi
					</NavLink>
				)}
			</div>
		</nav>
	);
}

export default NavBar;
