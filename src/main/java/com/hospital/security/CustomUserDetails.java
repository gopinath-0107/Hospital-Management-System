    package com.hospital.security;

    import org.springframework.security.core.GrantedAuthority;
    import org.springframework.security.core.authority.SimpleGrantedAuthority;
    import org.springframework.security.core.userdetails.UserDetails;

    import java.util.Collection;
    import java.util.Collections;

    public class CustomUserDetails implements UserDetails {

        private final String email;
        private final String password;
        private final String role;
        private final Long userId;
        private final String name;

        public CustomUserDetails(String email, String password, String role, Long userId, String name) {
            this.email = email;
            this.password = password;
            this.role = role;
            this.userId = userId;
            this.name = name;
        }

        public Long getId() {
            return userId;
        }

        public String getName() {
            return name;
        }

        public String getRole() {
            return role;
        }

        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
            return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role));
        }

        @Override
        public String getPassword() {
            return password;
        }

        @Override
        public String getUsername() {
            return email;
        }

        @Override
        public boolean isAccountNonExpired() {
            return true;
        }

        @Override
        public boolean isAccountNonLocked() {
            return true;
        }

        @Override
        public boolean isCredentialsNonExpired() {
            return true;
        }

        @Override
        public boolean isEnabled() {
            return true;
        }
    }
