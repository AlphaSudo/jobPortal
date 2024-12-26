//package com.fullmoon.jopportal.security;
//
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//
//import javax.sql.DataSource;
//import java.util.List;
//import java.util.Optional;
//import java.util.stream.Collectors;
//
//@Service
//public class CustomUserDetailsService implements UserDetailsService {
//
//    private final JdbcTemplate jdbcTemplate;
//
//    public CustomUserDetailsService(DataSource dataSource) {
//        this.jdbcTemplate = new JdbcTemplate(dataSource);
//    }
//
//    @Override
//    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
//        String userQuery = "select email, password, is_active from users where email=?";
//        String authoritiesQuery = "select user_type from users_type where user_type_id=(select user_type_id from users where email=?)";
//
//        List<UserDetails> users = jdbcTemplate.query(userQuery, new Object[]{email}, (rs, rowNum) -> {
//            String username = rs.getString("email");
//            String password = rs.getString("password");
//            boolean isActive = rs.getBoolean("is_active");
//
//            List<GrantedAuthority> authorities = jdbcTemplate.query(authoritiesQuery, new Object[]{email}, (rs2, rowNum2) -> rs2.getString("user_type"))
//                    .stream()
//                    .map(SimpleGrantedAuthority::new)
//                    .collect(Collectors.toList());
//
//            return org.springframework.security.core.userdetails.User
//                    .withUsername(username)
//                    .password(password)
//                    .disabled(!isActive)
//                    .authorities(authorities)
//                    .build();
//        });
//
//        if (users.isEmpty()) {
//            throw new UsernameNotFoundException("User not found with email: " + email);
//        }
//
//        return users.get(0);
//    }
//}