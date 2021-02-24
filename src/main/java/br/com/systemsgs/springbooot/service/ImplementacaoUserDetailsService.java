package br.com.systemsgs.springbooot.service;

import br.com.systemsgs.springbooot.entity.Usuario;
import br.com.systemsgs.springbooot.exception.SenhaInvalidaException;
import br.com.systemsgs.springbooot.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ImplementacaoUserDetailsService implements UserDetailsService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Transactional
    public Usuario salvarUsuario(Usuario usuario){
       return usuarioRepository.save(usuario);
    }

    public UserDetails autenticar(Usuario usuario){
       UserDetails user =  loadUserByUsername(usuario.getLogin());
       boolean senhasIguais = passwordEncoder.matches(usuario.getSenha(), user.getPassword());

       if(senhasIguais){
           return user;
       }

       throw new SenhaInvalidaException();

    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

     Usuario usuario =  usuarioRepository.findByLogin(username)
             .orElseThrow(() -> new UsernameNotFoundException("Usuario não Encontrado!"));

        String[] roles = usuario.isAdmin() ? new String[] {"ADMIN", "USER"} : new String[]{"USER"};

        return User.builder()
                .username(usuario.getLogin())
                .password(usuario.getSenha())
                .roles(roles)
                .build();
    }
}
