package com.miura.challenge_muralis.repository;

import com.miura.challenge_muralis.entity.Cliente;
import com.miura.challenge_muralis.entity.Contato;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


public interface ContatoRepository extends JpaRepository<Contato, Long> {
    List<Contato> findByCliente(Cliente cliente);

    @Modifying
    @Transactional
    @Query("DELETE FROM Contato c WHERE c.cliente.id = :clienteId")
    void deleteByClienteId(Long clienteId);
    List<Contato> findByClienteCpf(String cpf);
    List<Contato> findByClienteId(Long clienteId);
    Contato findByClienteIdAndId(Long clienteId, Long contatoId);
}
