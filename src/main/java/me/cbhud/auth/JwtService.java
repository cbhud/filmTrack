package me.cbhud.auth;

import io.smallrye.jwt.build.Jwt;
import jakarta.enterprise.context.ApplicationScoped;
import me.cbhud.model.Profile;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.PrivateKey;
import java.security.KeyFactory;
import java.security.spec.PKCS8EncodedKeySpec;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;


@ApplicationScoped
public class JwtService {

    @Inject
    @ConfigProperty(name = "jwt.private.key.path")
    String privateKeyPath;
    private final PrivateKey privateKey;

    public JwtService(@ConfigProperty(name = "jwt.private.key.path") String keyPath) {
        this.privateKeyPath = keyPath;
        this.privateKey = getPrivateKey();
    }

    private PrivateKey getPrivateKey() {
        try {
            String key = Files.readString(Paths.get(privateKeyPath))
                    .replace("-----BEGIN PRIVATE KEY-----", "")
                    .replace("-----END PRIVATE KEY-----", "")
                    .replaceAll("\\s+", "");

            byte[] keyBytes = Base64.getDecoder().decode(key);
            PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
            KeyFactory kf = KeyFactory.getInstance("RSA");
            return kf.generatePrivate(spec);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load private key for JWT signing", e);
        }
    }

    public String generateToken(Profile profile) {
        Instant now = Instant.now();
        Instant expiry = now.plus(1, ChronoUnit.HOURS); // Token valid for 1 hour

        return Jwt
                .issuer("filmtrack")
                .subject(profile.getUsername())
                .groups(profile.getRole())
                .issuedAt(now)
                .expiresAt(expiry)
                .sign(privateKey);
    }
}
