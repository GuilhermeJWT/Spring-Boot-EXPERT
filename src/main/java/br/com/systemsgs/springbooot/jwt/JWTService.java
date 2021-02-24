package br.com.systemsgs.springbooot.jwt;

import br.com.systemsgs.springbooot.VendasApplication;
import br.com.systemsgs.springbooot.entity.Usuario;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Service
public class JWTService {

    @Value("${geracao.token.jwt.time}")
    private String tempoExpiracao;

    @Value("${assinatura.token.jwt}")
    private String chave;

    public String gerarToken (Usuario usuario){
        long expString = Long.parseLong(tempoExpiracao);
        LocalDateTime dataHoraExpiracao = LocalDateTime.now().plusMinutes(expString);
        Instant instant = dataHoraExpiracao.atZone(ZoneId.systemDefault()).toInstant();
        Date data = Date.from(instant);

        return Jwts.builder().setSubject(usuario.getLogin()).setExpiration(data).signWith(SignatureAlgorithm.HS512, chave).compact();

    }

    private Claims obterClaims ( String token ) throws ExpiredJwtException {

        return Jwts.parser().setSigningKey(chave).parseClaimsJws(token).getBody();

    }

    public boolean tokenValido(String token){
        try{

          Claims claims = obterClaims(token);
          Date dataExpiracao = claims.getExpiration();
          LocalDateTime data = dataExpiracao.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

         return !LocalDateTime.now().isAfter(data);

        }catch(Exception e ){
            return false;
        }
    }

    public String obterLoginUsuario(String token) throws ExpiredJwtException {
        return (String) obterClaims(token).getSubject();
    }

}
