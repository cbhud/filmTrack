package me.cbhud.resources;

import at.favre.lib.crypto.bcrypt.BCrypt;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import me.cbhud.auth.JwtService;
import me.cbhud.auth.LoginRequest;
import me.cbhud.auth.LoginResponse;
import me.cbhud.auth.RegisterRequest;
import me.cbhud.exception.ProfileException;
import me.cbhud.model.Profile;
import me.cbhud.repository.ProfileRepository;

import java.util.regex.Pattern;

@Path("/auth")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AuthResource {

    public static String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";

    @Inject
    ProfileRepository profileRepository;

    @Inject
    JwtService jwtService;

    @POST
    @Path("login")
    public Response login(LoginRequest loginRequest) throws ProfileException {
        Profile profile = profileRepository.getProfileByUsername(loginRequest.username).get(0);

        if (profile == null || !BCrypt.verifyer()
                .verify(loginRequest.password.toCharArray(), profile.getPasswordHash())
                .verified) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

        String token = jwtService.generateToken(profile);
        return Response.ok(new LoginResponse(token)).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("create")
    public Response createProfile(RegisterRequest registerRequest) throws ProfileException {

        if (registerRequest.username == null || registerRequest.email == null ||
                registerRequest.password == null || registerRequest.fullName == null ||
                registerRequest.username.isBlank() || registerRequest.email.isBlank() ||
                registerRequest.password.isBlank() || registerRequest.fullName.isBlank()) {
            throw new ProfileException("Username, Email, password and full name are required");
        }

        if (!Pattern.matches(emailRegex, registerRequest.email)){
            throw new ProfileException("Invalid email format");
        }

        if (registerRequest.password.length() < 8) {
            throw new ProfileException("Password must be at least 8 characters long");
        }

        if (registerRequest.username.length() < 3 || registerRequest.username.length() > 16) {
            throw new ProfileException("Username must be between 3 and 16 characters long");
        }

        if (profileRepository.checkEmailExists(registerRequest.email)){
            throw new ProfileException("Email already exists");
        }

        if (profileRepository.checkUsernameExists(registerRequest.username)){
            throw new ProfileException("Username already exists");
        }

        String hashedPassword = BCrypt.withDefaults().hashToString(12, registerRequest.password.toCharArray());

        Profile profile = new Profile();
        profile.setUsername(registerRequest.username);
        profile.setEmail(registerRequest.email);
        profile.setPasswordHash(hashedPassword);
        profile.setFullName(registerRequest.fullName);
        profile.setRole("user");

        Profile p = profileRepository.createProfile(profile);
        return Response.ok(p).build();
    }


}
