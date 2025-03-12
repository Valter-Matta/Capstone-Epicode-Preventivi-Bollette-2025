import { NavLink } from "react-router-dom";
import "../css-components/NavBar.css";
import { useSelector, useDispatch } from "react-redux";
import { logoutUser } from "../redux/actions";
import { useState } from "react";

function NavBar() {
	const dispatch = useDispatch();
	const token = useSelector(state => state.user.token);
	const user = useSelector(state => state.user.user);
	const [showModal, setShowModal] = useState(false);

	const handleLogout = () => {
		dispatch(logoutUser());
		setShowModal(false);
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

			<div className="navbar-links">
				<NavLink
					className={({ isActive }) => (isActive ? "active" : "")}
					to="/"
				>
					Home
				</NavLink>

				<NavLink
					className={({ isActive }) => (isActive ? "active" : "")}
					to="/contatti"
				>
					Contatti
				</NavLink>

				<NavLink className="nav-btn-2" to="/upload-bill">
					Prova
				</NavLink>

				{token && user ? (
					<div className="user-avatar" onClick={() => setShowModal(!showModal)}>
						<img
							src={`https://api.dicebear.com/7.x/initials/svg?seed=${user.nome}&backgroundColor=19c2ba`}
							alt="avatar"
						/>
						<span>Ciao, {user.nome}</span>
						{showModal && (
							<div className="modal">
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
					<NavLink className="nav-btn-1" to="/login">
						Accedi
					</NavLink>
				)}
			</div>
		</nav>
	);
}

export default NavBar;
