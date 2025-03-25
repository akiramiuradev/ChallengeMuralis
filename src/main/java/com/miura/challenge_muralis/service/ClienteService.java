package com.miura.challenge_muralis.service;


import com.miura.challenge_muralis.entity.Cliente;
import com.miura.challenge_muralis.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.miura.challenge_muralis.exception.ResourceNotFoundException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    // Método para buscar cliente por nome ou CPF
    public Optional<Cliente> buscarClientePorNomeOuCpf(String nome, String cpf) {
        return clienteRepository.findOptionalByNomeOrCpf(nome, cpf);
    }
 
    // Método para listar todos os clientes
    public List<Cliente> listarTodos() {
        return clienteRepository.findAll();
    }

    // Método para excluir cliente
    public void deletarCliente(Long id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));
        clienteRepository.delete(cliente);
    }

    // Método para editar cliente parcialmente
    public Cliente editarClienteParcial(Long id, Map<String, Object> fields) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));

        if (fields.containsKey("nome")) {
            cliente.setNome((String) fields.get("nome"));
        }
        if (fields.containsKey("cpf")) {
            cliente.setCpf((String) fields.get("cpf"));
        }
        if (fields.containsKey("dataNascimento")) {
            String dataNascimentoStr = (String) fields.get("dataNascimento");
            LocalDate dataNascimento = LocalDate.parse(dataNascimentoStr); // Converte a string para LocalDate
            cliente.setDataNascimento(dataNascimento);
        }
                // Atualize outros campos conforme necessário

        return clienteRepository.save(cliente);
    }


    public Cliente cadastrar(Cliente cliente) {
        // Validações de CPF, Nome, etc.
        if (clienteRepository.existsByCpf(cliente.getCpf())) {
            throw new IllegalArgumentException("CPF já cadastrado.");
        }

        return clienteRepository.save(cliente);  // Salva o cliente no banco
    }


    public Cliente atualizarCliente(Long ID, Cliente cliente) {
        // Verifique se o cliente já existe antes de atualizar
        if (clienteRepository.existsById(cliente.getId())) {
            return clienteRepository.save(cliente); // Salva as atualizações do cliente
        } else {
            throw new ResourceNotFoundException("Cliente não encontrado"); // Caso não exista
        }
    }


    public Optional<Cliente> buscarClientePorId(Long id) {
        return clienteRepository.findById(id);
    }

    
    public Cliente salvarCliente(Cliente cliente) {
        return clienteRepository.save(cliente);
    }
    
}
