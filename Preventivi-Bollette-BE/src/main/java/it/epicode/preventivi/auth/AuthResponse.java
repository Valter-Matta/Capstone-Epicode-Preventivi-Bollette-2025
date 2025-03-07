package it.epicode.preventivi.auth;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthResponse {
    private String token;
    private String nome;
    private String cognome;
    private String email;
}
