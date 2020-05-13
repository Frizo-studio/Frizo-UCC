package com.frizo.ucc.server.security;

import com.frizo.ucc.server.dao.UserRepository;
import com.frizo.ucc.server.exception.BadRequestException;
import com.frizo.ucc.server.exception.ResourceNotFoundException;
import com.frizo.ucc.server.model.AuthProvider;
import com.frizo.ucc.server.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional
    public UserPrincipal loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("帳號不存在")
                );
        if (!user.getProvider().equals(AuthProvider.local)){
            throw new BadRequestException("此 email 帳號已由服務商 " + user.getProvider() + " 註冊使用。");
        }
        return UserPrincipal.create(user);
    }

    @Transactional
    public UserPrincipal loadUserById(Long id) {
        User user = userRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("User", "id", id)
        );
        return UserPrincipal.create(user);
    }
}
