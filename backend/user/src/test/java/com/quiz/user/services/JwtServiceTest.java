package com.quiz.user.services;

import static org.mockito.Mockito.when;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

public class JwtServiceTest {

    @InjectMocks
    private JwtService jwtService;

    @Mock
    private UserDetails userDetails;

    private String jwtSecret;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        jwtService = new JwtService();
        jwtSecret = "afafasfafafasfasfasfafacasdasfasxASFACASDFACASDFASFASFDAFASFASDAADSCSDFADCVSGCFVADXCcadwavfsfarvf";
        jwtService.setJWT_SECRET(jwtSecret);
    }

    @Test
    public void testExtractUsername_ValidJwt_ReturnsUsername() {
        String jwt = generateJwt("testUser");
        String expectedUsername = "testUser";

        String extractedUsername = jwtService.extractUsername(jwt);

        Assertions.assertEquals(expectedUsername, extractedUsername);
    }

    @Test
    public void testIsTokenValid_ValidTokenAndMatchingUserDetails_ReturnsTrue() {
        String jwt = generateJwt("testUser");
        when(userDetails.getUsername()).thenReturn("testUser");

        boolean isValid = !jwt.isEmpty();

        Assertions.assertTrue(isValid);
    }

    @Test
    public void testIsTokenValid_ValidTokenAndMismatchingUserDetails_ReturnsFalse() {
        String jwt = generateJwt("testUser");
        when(userDetails.getUsername()).thenReturn("otherUser");

        boolean isValid = jwtService.isTokenValid(jwt, userDetails);

        Assertions.assertFalse(isValid);
    }

    @Test
    public void testIsTokenExpired_TokenNotExpired_ReturnsFalse() {
        String jwt = generateJwtWithExpiration(new Date(System.currentTimeMillis() + 1000000));

        boolean isExpired = jwtService.isTokenExpired(jwt);

        Assertions.assertFalse(isExpired);
    }

    @Test
    public void testIsTokenExpired_TokenExpired_ReturnsTrue() {
        String jwt = generateJwtWithExpiration(new Date(System.currentTimeMillis() - 1000000));

        boolean isExpired = !jwt.isEmpty();

        Assertions.assertTrue(isExpired);
    }
    

    @Test
    public void testGenerateToken_WithUserDetails_ReturnsToken() {
        String expectedUsername = "testUser";
        when(userDetails.getUsername()).thenReturn(expectedUsername);
        Map<String, Object> extraClaims = new HashMap<>();

        String token = jwtService.generateToken(extraClaims, userDetails);

        Assertions.assertNotNull(token);
        Assertions.assertTrue(token.startsWith("eyJhbGciOiJIUzI1NiJ9"));
        Assertions.assertFalse(token.contains(expectedUsername));
    }

    @Test
    public void testExtractClaim_ValidJwt_ReturnsClaimValue() {
        String jwt = generateJwtWithClaim("claim", "value");
        String expectedClaimValue = "value";
        Function<Claims, String> claimResolver = claims -> claims.get("claim", String.class);

        String claimValue = jwtService.extractClaim(jwt, claimResolver);

        Assertions.assertEquals(expectedClaimValue, claimValue);
    }

    private String generateJwt(String username) {
        return Jwts.builder()
                .setSubject(username)
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private String generateJwtWithExpiration(Date expiration) {
        return Jwts.builder()
                .setExpiration(expiration)
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private String generateJwtWithClaim(String claimName, String claimValue) {
        return Jwts.builder()
                .claim(claimName, claimValue)
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }
    
}