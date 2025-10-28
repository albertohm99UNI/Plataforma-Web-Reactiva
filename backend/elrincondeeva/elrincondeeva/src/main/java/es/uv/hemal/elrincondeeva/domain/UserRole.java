package es.uv.hemal.elrincondeeva.domain;

import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import javax.validation.constraints.NotNull;

@Table("user_role")
public class UserRole {


    @Column("user_id")
    @NotNull
    private Integer userId;

    @Column("role_id")
    @NotNull
    private Integer roleId;


  

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    
}
