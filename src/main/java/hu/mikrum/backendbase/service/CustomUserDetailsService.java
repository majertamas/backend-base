package hu.mikrum.backendbase.service;

import hu.mikrum.backendbase.model.entity.User;
import hu.mikrum.backendbase.repository.UserRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

import static hu.mikrum.backendbase.util.Util.ROLE_USER;

@Service
@RequiredArgsConstructor
@Data
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
        User user = userRepository.findByName(name);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with name: " + name);
        }
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), List.of((GrantedAuthority) () -> ROLE_USER));
    }
}
