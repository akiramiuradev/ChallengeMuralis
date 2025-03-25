package com.miura.challenge_muralis.repository;

import com.miura.challenge_muralis.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

import java.util.Optional;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    Optional<Cliente> findOptionalByNomeOrCpf(String nome, String cpf);
    List<Cliente> findListByNomeOrCpf(String nome, String cpf);
    boolean existsByCpf(String cpf); 
    Optional<Cliente> findById(Long id);
    Optional<Cliente> findByCpf(String cpf);
}

