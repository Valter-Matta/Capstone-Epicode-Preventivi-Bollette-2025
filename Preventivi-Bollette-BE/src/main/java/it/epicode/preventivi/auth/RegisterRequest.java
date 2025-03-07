package it.epicode.preventivi.auth;

import lombok.Data;

@Data
public class RegisterRequest {
    private String nome;
    private String cognome;
    private String phone;
    private String email;
    private String password;
    private String confermaPwd;
}
