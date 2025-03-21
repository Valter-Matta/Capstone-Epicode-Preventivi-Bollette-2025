package it.epicode.preventivi.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService {

	@Autowired
	private AppUserRepository appUserRepository;



	@Override
	public UserDetails loadUserByUsername (String email) throws UsernameNotFoundException {
		AppUser user = appUserRepository.findByEmail(email)
			.orElseThrow(() -> new UsernameNotFoundException("Utente non trovato con email: " + email));
		return user;
	}
}
