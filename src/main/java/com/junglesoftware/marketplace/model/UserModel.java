package com.junglesoftware.marketplace.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.UUID;

@Data
@Entity
@EqualsAndHashCode
public class UserModel {

  @Id
  @GeneratedValue
  @Column(name = "uuid")
  private UUID uuid;

  @Column(name = "email")
  private String email;

  @Column(name = "password")
  private String password;

  @Column(name = "name")
  private String name;

  @Column(name = "countryISO3")
  private String countryISO3;

  @Column(name = "registerCode")
  private String registerCode;
}
