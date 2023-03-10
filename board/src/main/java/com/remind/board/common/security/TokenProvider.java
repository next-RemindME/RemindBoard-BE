package com.remind.board.common.security;

import com.remind.board.member.service.MemberSignService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Slf4j
@Component
@RequiredArgsConstructor
public class TokenProvider {

  private static final long TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 24 * 10; //10 days
  private static final String KEY_ROLES = "roles";

  private final MemberSignService memberSignService;

  @Value("${spring.jwt.secret}")
  private String secretKey;

  public static final String ROLE_MEMBER = "MEMBER";

  public String generateToken(String username, String roles) {

    Claims claims = Jwts.claims().setSubject(username);
    claims.put(KEY_ROLES, roles);

    var now = new Date();
    var expiredDate = new Date(now.getTime() + TOKEN_EXPIRE_TIME);

    return Jwts.builder()
        .setClaims(claims)
        .setIssuedAt(now)
        .setExpiration(expiredDate)
        .signWith(SignatureAlgorithm.HS512,this.secretKey)
        .compact();
  }

  public Authentication getAuthentication(String jwt) {

    String keyRoles = getKeyRoles(jwt);
    log.info("get key roles by jwt -> {}", keyRoles);

    //MEMBER 권한 부여 (board crud 가능)
    if (ROLE_MEMBER.equals(keyRoles)) {
      UserDetails userDetails = memberSignService.loadUserByUsername(this.getUsername(jwt));
      return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }
    return null;
  }

  public boolean validateToken(String token) {

    if (!StringUtils.hasText(token)) {
      return false;
    }

    var claims = this.parseClaims(token);
    return !claims.getExpiration().before(new Date());
  }

  public String getUsername(String token) {
    return this.parseClaims(token).getSubject();
  }

  public String getKeyRoles(String token) {
    return String.valueOf(this.parseClaims(token).get(KEY_ROLES));
  }

  private Claims parseClaims(String token) {

    try {

      return Jwts.parser().setSigningKey(this.secretKey).parseClaimsJws(token).getBody();
    } catch (ExpiredJwtException e) {
      return e.getClaims();
    }
  }

}
