package com.miura.challenge_muralis.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

@Entity
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "clienteId")
    private Long id;

    @NotNull(message = "O CPF é obrigatório")
    @Size(min = 11, max = 11)
    @Column(nullable = false, unique = true, name = "cpf")
    private String cpf;

    @NotEmpty(message = "O nome é obrigatório")
    @Column(nullable = false)
    private String nome;
    

    @Column
    @Past(message = "A data de nascimento deve ser no passado.")
    private LocalDate dataNascimento;

    @Column
    private String endereco;

    public Cliente() {}

    public Cliente(String nome, String cpf, LocalDate dataNascimento, String endereco) {
            this.nome = nome;
            this.cpf = cpf;
            this.dataNascimento = dataNascimento;
            this.endereco = endereco;
        }
    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }
}
