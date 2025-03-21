package tektino.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Data
@Table(name = "roles")
public class RoleModel {

    @Id
    private Integer id;

    // @Column(name = "role_name")
    private String roleName;

    @Override
    public String toString() {
        return roleName; // Menampilkan nama peran
    }

}
