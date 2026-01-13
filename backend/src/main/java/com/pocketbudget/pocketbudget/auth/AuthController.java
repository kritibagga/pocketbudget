package com.pocketbudget.pocketbudget.auth;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pocketbudget.pocketbudget.auth.dto.AuthResponse;
import com.pocketbudget.pocketbudget.auth.dto.LoginRequest;
import com.pocketbudget.pocketbudget.auth.dto.RegisterOwnerRequest;
import com.pocketbudget.pocketbudget.household.Household;
import com.pocketbudget.pocketbudget.household.HouseholdRepository;
import com.pocketbudget.pocketbudget.security.JwtProperties;
import com.pocketbudget.pocketbudget.security.JwtService;
import com.pocketbudget.pocketbudget.security.RefreshToken;
import com.pocketbudget.pocketbudget.security.RefreshTokenRepository;
import com.pocketbudget.pocketbudget.user.Role;
import com.pocketbudget.pocketbudget.user.User;
import com.pocketbudget.pocketbudget.user.UserRepository;

import jakarta.validation.Valid;

@RestController
@RequestMapping("api/auth")
public class AuthController {

    private final UserRepository users;
    private final HouseholdRepository households;
    private final RefreshTokenRepository refreshTokens;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final JwtProperties jwtProps;

    public AuthController(UserRepository users, HouseholdRepository households, RefreshTokenRepository refreshTokens,
            PasswordEncoder passwordEncoder, JwtService jwtService, JwtProperties jwtProps) {
        this.users = users;
        this.households = households;
        this.refreshTokens = refreshTokens;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.jwtProps = jwtProps;
    }


    @PostMapping("/register-owner")
    public AuthResponse registerOwner(@Valid @RequestBody RegisterOwnerRequest req){

        if(users.findByEmail(req.email).isPresent()){
            throw new RuntimeException("Email is already used");
        }

        //create a household
        Household household= new Household();
        household.setName(req.householdName);
        household.setInviteCode((UUID.randomUUID().toString().substring(0,8)));
        household=households.save(household);

        //create a user
        User user= new User();
        user.setEmail(req.email);
        user.setPasswordHash(passwordEncoder.encode(req.password));
        user.setRole(Role.OWNER);
        user.setHousehold(household);
        user=users.save(user);

        //create an access token (JWT)
        String accessToken=jwtService.createAccessToken(user);

        //create a refresh token
        String refreshTokenValue=UUID.randomUUID().toString();
        RefreshToken rt= new RefreshToken();
        rt.setUser(user);
        rt.setToken(refreshTokenValue);
        rt.setRevoked(false);
        rt.setExpiresAt(
            Instant.now().plus(jwtProps.getRefreshDays(), ChronoUnit.DAYS)
        );
        refreshTokens.save(rt);

        return new AuthResponse(accessToken, refreshTokenValue);

    }

    @PostMapping("/login")
    public AuthResponse login (@Valid @RequestBody LoginRequest req){
        User user =users.findByEmail(req.email)
                    .orElseThrow(()->new RuntimeException("Invalid credentials"));

        if(!passwordEncoder.matches(req.password, user.getPasswordHash())){
            throw new RuntimeException("Invalid Credentials");
        }

        String accessToken =jwtService.createAccessToken(user);

        String refreshTokenValue=UUID.randomUUID().toString();
        RefreshToken rt =new RefreshToken();
        rt.setUser(user);
        rt.setToken(refreshTokenValue);
        rt.setRevoked(false);
        rt.setExpiresAt(
            Instant.now().plus(jwtProps.getRefreshDays(), ChronoUnit.DAYS)
        );
        refreshTokens.save(rt);

        return new AuthResponse(accessToken, refreshTokenValue);


    }



}
