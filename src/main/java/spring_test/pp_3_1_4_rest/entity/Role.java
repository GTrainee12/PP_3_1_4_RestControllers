package spring_test.pp_3_1_4_rest.entity;

import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "roles")

public class Role implements GrantedAuthority {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String role;




    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return role;
    }

    @Override
    public String getAuthority() {
        return role;
    }

    public Role(Long id, String role) {
        this.id = id;
        this.role = role;
    }
    public Role() {

    }
}

