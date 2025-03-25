package com.miura.challenge_muralis.service;

import com.miura.challenge_muralis.entity.Cliente;
import com.miura.challenge_muralis.entity.Contato;
import com.miura.challenge_muralis.repository.ClienteRepository;
import com.miura.challenge_muralis.repository.ContatoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContatoService {

    @Autowired
    private ContatoRepository contatoRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    // Método para criar novo contato
    public Contato criarContato(Long clienteId, Contato contato) {
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));
        contato.setCliente(cliente);
        return contatoRepository.save(contato);
    }

    // Método para listar os contatos de um cliente específico
    public List<Contato> listarContatos(Long clienteId) {
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));
        return contatoRepository.findByCliente(cliente);
    }

    // Método para salvar novo contato
    public Contato salvar(Contato contato) {
        return contatoRepository.save(contato);
    }

    // Método para encontrar contato por id
    public Contato buscarContato(Long id) {
        return contatoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Contato não encontrado"));
    }

    public void excluirContato(Long id) {
        contatoRepository.deleteById(id);  // Exclui o contato pelo ID
    }
}
