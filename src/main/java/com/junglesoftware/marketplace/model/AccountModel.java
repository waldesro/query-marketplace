package com.junglesoftware.marketplace.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.UUID;

@Data
@Entity
@EqualsAndHashCode
@Builder
@AllArgsConstructor
@Table(name = "User")
public class AccountModel {

  @Id
  @GeneratedValue
  @Column(name = "uuid")
  private UUID uuid;

  @Column(name = "email")
  private String email;

  @Column(name = "name")
  private String name;

  @Column(name = "countryISO3")
  private String countryISO3;

}
