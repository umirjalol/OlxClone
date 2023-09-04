package uz.OLXCone.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import uz.OLXCone.model.template.AbsUUIDEntity;
import uz.OLXCone.utils.AppConstants;
import uz.OLXCone.utils.Role;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = User.name)
@DynamicInsert
@DynamicUpdate
@ToString
public class User extends AbsUUIDEntity implements UserDetails {
    public static final String name = "users";
    @Column(nullable = false,
            columnDefinition = "varchar(80) default '" +
                    AppConstants.DEFAULT_IMAGE_PATH + "'")
    private String imagePath;
    @Column(nullable = false, length = 50)
    private String fullName;
    @Column(nullable = false, unique = true, length = 30)
    private String email;
    @Column(nullable = false)
    private String password;
    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(20) default '" + AppConstants.USER + "'")
    private Role role;
    @Column(columnDefinition = "boolean default true ")
    private Boolean isBlocked;

    //region      user details
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_" + role.name()));
        return authorities;
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
    //endregion
}
