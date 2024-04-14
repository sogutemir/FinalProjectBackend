package org.work.personnelinfo.admin.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.work.personnelinfo.personel.model.PersonelEntity;

import java.util.Collection;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Collection<RoleEntity> roles;

    public Collection<RoleEntity> getRoles() {
        return roles;
    }


    public UserEntity(String username, String password) {
        this.username = username;
        this.password = password;
    }


    @OneToOne(orphanRemoval = true)
    @JoinColumn(name = "personel_id")
    private PersonelEntity personel;

}
