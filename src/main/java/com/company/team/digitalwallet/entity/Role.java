package com.company.team.digitalwallet.entity;

import javax.persistence.*;

@Entity
@Table(name = "role")
public class Role {

    public Role() {

    }

    @Id
    @GeneratedValue
    @Column(name = "role_id")
    private int roleId;

    @Column(name = "role")
    private String role;

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }
}
