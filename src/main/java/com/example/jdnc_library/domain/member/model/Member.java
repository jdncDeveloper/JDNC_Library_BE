package com.example.jdnc_library.domain.member.model;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Table(indexes = @Index(name = "mb_number_index", columnList = "username"))
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //A:년도/B:1반기or2반기/C:기수/:4번호
    //AA/B/CC/DDD
    private String username;

    private String password;

    private String name;

    private String email;

    private String refresh;

    @Enumerated(EnumType.STRING)
    private Role role;

    public Member (String username, String password, String name, String email, String refresh, Role role) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.email = email;
        this.refresh = refresh;
        this.role = role;
    }

    public void updateRefresh(String refresh) {
        this.refresh = refresh;
    }

    public int getPeriod() {
        if (username == null) return 0;
        if (username.length() != 8) return 0;
        return Integer.parseInt(username.substring(3, 5));
    }

    public void update(String name, String encodedPassword, String email) {
        this.password = encodedPassword;
        this.name = name;
        this.email = email;
    }

    public void updateROLE(Role role) {
        this.role = role;
    }
}
