package com.targinou.taskmanagement.auth;

import com.targinou.taskmanagement.commom.view.GenericView;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AuthenticationController {

    private final AuthenticationService service;

    @PostMapping("/register")
    public GenericView<AuthenticationResponse> register(
            @RequestBody RegisterForm form
    ) {
        return GenericView.ok(service.register(form));
    }

    @PostMapping("/authenticate")
    public GenericView<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationForm form
    ) {
        return GenericView.ok(service.authenticate(form));
    }

    @PostMapping("/refresh-token")
    public GenericView<AuthenticationResponse> refreshToken(
            @RequestBody RefreshTokenForm form
    ) {
        return GenericView.ok(service.refreshToken(form));
    }


}
