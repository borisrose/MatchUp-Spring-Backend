package com.matchup.app.model;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
@Entity
@Table(name="users") // This tells Hibernate to make a table out of this class
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Integer id;

  @Column(name = "created_at", nullable = false, updatable = false)
  @CreatedDate
  private LocalDateTime created_at;

  @Column(name = "updated_at")
  @LastModifiedDate
  private LocalDateTime updated_at;
 
  private String firstname;
  private String lastname;
  private String email;
  private String picture;

  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
  private String password;

  public User(String firstname, String lastname, String email, String password){
    this.firstname = firstname;
    this.lastname = lastname; 
    this.email = email;
    this.password = password;
    this.created_at = LocalDateTime.now();
    this.updated_at = LocalDateTime.now();
  }

  public User(String picture, String firstname, String lastname, String email, String password){
    this.firstname = firstname;
    this.lastname = lastname; 
    this.picture = picture;
    this.email = email;
    this.password = password;
    this.created_at = LocalDateTime.now();
    this.updated_at = LocalDateTime.now();
  }

}