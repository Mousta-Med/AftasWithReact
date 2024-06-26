package com.med.aftas.serverside.model;

import com.med.aftas.serverside.enums.IdentityDocumentType;
import com.med.aftas.serverside.enums.Role;
import com.med.aftas.serverside.enums.Status;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "member")
@Builder
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer num;

    private String name;

    private String familyName;

    private LocalDate accessionDate;

    private String nationality;

    private String username;

    private String password;

    @Enumerated(value = EnumType.STRING)
    private Role role;

    @Enumerated(value = EnumType.STRING)
    private Status status;

    @Enumerated(EnumType.STRING)
    private IdentityDocumentType identityDocument;

    @Column(unique = true)
    private String identityNumber;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Ranking> rankings;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Hunting> huntings;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
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
