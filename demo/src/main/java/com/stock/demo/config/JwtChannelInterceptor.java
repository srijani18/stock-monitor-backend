package com.stock.demo.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Component
public class JwtChannelInterceptor implements ChannelInterceptor {

    // Replace with your Keycloak public key (from realm JSON, without BEGIN/END lines)
    private final String keycloakPublicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAuA5JwWw99XBlw62/J/6B80H3rWwYN4OcFpXHzk4YqLZatTMEPlJp4t8lpmjUDqRa+njzn+swOWMW+YWuyo/qAApijGqd72KGFT/BRaSuwCstsGHJBthfr5DZUDAgLD9pGSPU+9Ccg6ahT8+ERciC5kmxSV2nZSTBgJxLk5yH9JAnrQ9Ng1K7RRTzd++KFFcvjnuZMfH69C3j4etHylPQz9ui8VDnKprWKpxGMaNC6KO9YPP7Aeqct9v/63vPVjy2WZ1rQj7WXnTQLr8GeYAUaj1C7JijtaEtQIG/8G/AtMgMvdR57oST+mLwjNXLGuoND8mMwUwmS4tkNf09SYbhgwIDAQAB";

    private PublicKey getPublicKey() throws Exception {
        byte[] decoded = Base64.getDecoder().decode(keycloakPublicKey);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(decoded);
        return KeyFactory.getInstance("RSA").generatePublic(keySpec);
    }

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        Object tokenAttr = accessor.getSessionAttributes().get("access_token");

        if (tokenAttr != null) {
            try {
                String token = tokenAttr.toString();
                PublicKey publicKey = getPublicKey();

                Claims claims = Jwts.parserBuilder()
                        .setSigningKey(publicKey)
                        .build()
                        .parseClaimsJws(token)
                        .getBody();

                String username = claims.getSubject();
                accessor.setUser(() -> username);  // Sets Principal
            } catch (Exception ex) {
                System.out.println("JWT validation failed: " + ex.getMessage());
                return null; // Block the message
            }
        }
        return message;
    }
}
