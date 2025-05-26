package byteme

import io.jsonwebtoken.*
import io.jsonwebtoken.security.Keys

import javax.crypto.SecretKey
import java.util.Date

class JWTUtil {
    static final String SECRET = "m9Zx2nQjA6LpKc3vWs!NbP8hVg5Jk7XtHs#RwY4TqEnVcB1Lf"
    static final SecretKey KEY = Keys.hmacShaKeyFor(SECRET.bytes)

    static String generateToken(Long userId) {
        return Jwts.builder()
                .setSubject(userId.toString())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24)) // 24h gültig
                .signWith(KEY)
                .compact()
    }

    static Long getUserIdFromToken(String token) {
        try {
            def claims = Jwts.parserBuilder().setSigningKey(KEY).build().parseClaimsJws(token).body
            return claims.getSubject() as Long
        } catch (Exception e) {
            return null
        }
    }
}
