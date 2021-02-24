package br.com.systemsgs.springbooot.controller;

import br.com.systemsgs.springbooot.dto.CredenciaisDTO;
import br.com.systemsgs.springbooot.dto.TokenDTO;
import br.com.systemsgs.springbooot.entity.Usuario;
import br.com.systemsgs.springbooot.exception.SenhaInvalidaException;
import br.com.systemsgs.springbooot.jwt.JWTService;
import br.com.systemsgs.springbooot.service.ImplementacaoUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final ImplementacaoUserDetailsService implementacaoUserDetailsService;

    private final PasswordEncoder passwordEncoder;

    private final JWTService jwtService;

    @PostMapping("/salvar")
    @ResponseStatus(HttpStatus.CREATED)
    public Usuario salvar (@RequestBody @Valid Usuario usuario){

        String senhaCriptografada = passwordEncoder.encode(usuario.getSenha());
        usuario.setSenha(senhaCriptografada);

        return implementacaoUserDetailsService.salvarUsuario(usuario);

    }

    @PostMapping("/autenticar")
    public TokenDTO autenticarUsuario(@RequestBody CredenciaisDTO credenciaisDTO){

    try{

      Usuario usuario = Usuario.builder()
              .login(credenciaisDTO.getLogin())
              .senha(credenciaisDTO.getSenha()).build();

      UserDetails usuarioAutenticado = implementacaoUserDetailsService.autenticar(usuario);
      String token = jwtService.gerarToken(usuario);
      return new TokenDTO(usuario.getLogin(), token);

    }catch(UsernameNotFoundException | SenhaInvalidaException e){
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
    }

    }

}
