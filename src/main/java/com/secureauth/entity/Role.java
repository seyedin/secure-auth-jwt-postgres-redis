package com.secureauth.entity;

import com.secureauth.entity.enums.RoleType;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;

/**
 * Represents a user role entity that implements GrantedAuthority for Spring Security.
 */
@Entity
@Table(name = "roles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Role implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(unique = true, nullable = false)
    private RoleType name;

    /**
     * Returns the authority name for Spring Security.
     *
     * @return the role name as a string
     */
    @Override
    public String getAuthority() {
        return "ROLE_" + name.name();
    }
}