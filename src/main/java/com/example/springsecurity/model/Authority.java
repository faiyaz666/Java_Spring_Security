package com.example.springsecurity.model;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "authority_table")
public class Authority {

    @Id
    @GeneratedValue
    @Column(name = "authority_id")
    private int id;

    @Column(name = "authority", unique = true)
    private String authority;
}
