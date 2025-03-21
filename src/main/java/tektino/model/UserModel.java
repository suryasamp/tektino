package tektino.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Data
@Table(name = "users")
public class UserModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String password;
    private String name;
    private String email;
    private String alamat;
    private String no_ktp;
    private String no_npwp;
    private String no_handphone;
    private String jenis_kelamin;

    @Column(name = "avatar_path")
    private String avatarPath;

    @ManyToOne
    @JoinColumn(name = "role")
    private RoleModel role;

    public String getRoleName() {
        return role != null ? role.getRoleName() : "";
    }

}
