package it.epicode.preventivi.auth;

import it.epicode.preventivi.email.EmailService;
import jakarta.mail.MessagingException;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Service
public class AppUserService {

	@Autowired
	private AppUserRepository appUserRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	@Autowired
	private EmailService emailService;

	public AppUser registerUser (String nome, String cognome, String phone, String email, String password, String confermaPwd, Set<Role> roles) {
		if (appUserRepository.existsByEmail(email)) {
			throw new EntityExistsException("Email già in uso");
		}
		AppUser appUser = new AppUser();
		appUser.setNome(nome);
		appUser.setCognome(cognome);
		appUser.setPhone(phone);
		appUser.setEmail(email);
		appUser.setUsername(nome);
		if (Objects.equals(password, confermaPwd)) {
			appUser.setPassword(passwordEncoder.encode(password));
		} else {
			throw new SecurityException("Le password non corrispondono");
		}
		appUser.setRoles(roles);

		// Invio della mail di conferma
		try {
			emailService.sendEmail(email, "Benvenuto!", generateEmailBody(nome));
		} catch (MessagingException e) {
			throw new RuntimeException("Errore durante l'invio della mail", e);
		}

		return appUserRepository.save(appUser);
	}

	public Optional<AppUser> findByEmail (String email) {
		return appUserRepository.findByEmail(email);
	}

	public String authenticateUser (String email, String password) {
		try {
			Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(email, password)
			);

			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			return jwtTokenUtil.generateToken(userDetails);
		} catch (AuthenticationException e) {
			throw new SecurityException("Credenziali non valide", e);
		}
	}


	public AppUser loadUserByUsername (String username) {
		AppUser appUser = appUserRepository.findByEmail(username)
			.orElseThrow(() -> new EntityNotFoundException("Utente non trovato con username: " + username));


		return appUser;
	}

	private String generateEmailBody (String nome) {
		return "<h1>Benvenuto, " + nome + "!</h1>" +
			"<p>Grazie per esserti registrato alla nostra piattaforma.</p>" +
			"<p>Ti auguriamo una buona esperienza!</p>";
	}
}
