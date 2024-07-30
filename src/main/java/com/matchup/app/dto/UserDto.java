package com.matchup.app.dto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserDto {
    private Integer id;
    private String firstname;
    private String lastname;
    private String email;
    private String created_at;
    private String updated_at;
    private String picture;

    public UserDto(Integer id, String firstname, String lastname, String email,  LocalDateTime created_at,LocalDateTime updated_at, String picture){
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.picture = (picture != null) ? picture : null;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        this.created_at = created_at.format(formatter);
        this.updated_at = updated_at.format(formatter);
    }
}