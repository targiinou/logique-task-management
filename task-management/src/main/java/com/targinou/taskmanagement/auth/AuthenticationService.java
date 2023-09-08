package com.targinou.taskmanagement.auth;

import com.targinou.taskmanagement.commom.exception.ValidationException;
import com.targinou.taskmanagement.commom.util.Util;
import com.targinou.taskmanagement.config.JwtService;
import com.targinou.taskmanagement.token.Token;
import com.targinou.taskmanagement.token.TokenRepository;
import com.targinou.taskmanagement.token.TokenType;
import com.targinou.taskmanagement.user.User;
import com.targinou.taskmanagement.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterForm form) {

        validateUser(form);

        var user = User.builder()
                .name(form.getName())
                .email(form.getEmail())
                .password(passwordEncoder.encode(form.getPassword()))
                .build();
        var savedUser = userRepository.save(user);
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        saveUserToken(savedUser, jwtToken);
        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();

    }

    public AuthenticationResponse authenticate(AuthenticationForm form) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        form.getEmail(),
                        form.getPassword()
                )
        );
        var user = userRepository.findByEmail(form.getEmail())
                .orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        revokeAllUserTokens(user);
        saveUserToken(user, jwtToken);
        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    private void saveUserToken(User user, String jwtToken) {
        var token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }

    private void revokeAllUserTokens(User user) {
        var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }

    public AuthenticationResponse refreshToken(RefreshTokenForm form) {

        var userEmail = jwtService.extractUsername(form.getToken());

        if (userEmail == null) {
            throw new ValidationException("Refresh token inválido");
        }

        var user = this.userRepository.findByEmail(userEmail)
                .orElseThrow();

        if (!jwtService.isTokenValid(form.getToken(), user)) {
            throw new ValidationException("Refresh token inválido");
        }

        var accessToken = jwtService.generateToken(user);
        revokeAllUserTokens(user);
        saveUserToken(user, accessToken);
        return AuthenticationResponse.builder()
                .accessToken(accessToken)
                .refreshToken(form.getToken())
                .build();

    }

    private void validateUser(RegisterForm form) {

        if (Util.isEmpty(form.getEmail())) {
            throw new ValidationException("O email deve ser informado.");
        }

        if (Util.isEmpty(form.getName())) {
            throw new ValidationException("O nome deve ser informado.");
        }

        if (Util.isEmpty(form.getPassword())) {
            throw new ValidationException("A senha deve ser informada.");
        }

        if (form.getPassword().length() < 6) {
            throw new ValidationException("A senha deve conter pelo menos 6 caracteres.");
        }

        if (form.getName().length() < 2) {
            throw new ValidationException("O nome deve conter pelo menos 2 caracteres.");
        }

        if (!Util.isValidEmail(form.getEmail())) {
            throw new ValidationException("O endereço de email informado é inválido.");
        }

        if (userRepository.findByEmail(form.getEmail()).isPresent()) {
            throw new ValidationException("Já existe usuário cadastrado com esse email.");
        }

    }
}
