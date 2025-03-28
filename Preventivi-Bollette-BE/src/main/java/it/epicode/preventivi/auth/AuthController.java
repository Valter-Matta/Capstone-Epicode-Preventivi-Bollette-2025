package it.epicode.preventivi.auth;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping ("/api/auth")
@RequiredArgsConstructor
public class AuthController {

	private final AppUserService appUserService;

	@PostMapping ("/register")
	public ResponseEntity<Map<String, String>> register (@RequestBody RegisterRequest registerRequest) {
		System.out.println("richiesta arrivata: + " + registerRequest);
		appUserService.registerUser(
			registerRequest.getNome(),
			registerRequest.getCognome(),
			registerRequest.getPhone(),
			registerRequest.getEmail(),
			registerRequest.getPassword(),
			registerRequest.getConfermaPwd(),
			Set.of(Role.ROLE_USER) // Assegna il ruolo di default
		);
		Map<String, String> response = new HashMap<>();
		response.put("message", "Registrazione avvenuta con successo! Controlla la tua email.");
		return ResponseEntity.ok(response);
	}

	@PostMapping ("/login")
	public ResponseEntity<AuthResponse> login (@RequestBody LoginRequest loginRequest) {
		AppUser user = appUserService.findByEmail(loginRequest.getEmail())
			.orElseThrow(() -> new EntityNotFoundException("Utente non trovato con email: " + loginRequest.getEmail()));
		String token = appUserService.authenticateUser(
			loginRequest.getEmail(),
			loginRequest.getPassword()
		);
		return ResponseEntity.ok(new AuthResponse(token, user.getNome(), user.getCognome(), user.getEmail()));
	}
}
