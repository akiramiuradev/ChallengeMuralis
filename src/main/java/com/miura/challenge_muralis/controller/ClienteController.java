package com.miura.challenge_muralis.controller;

import com.miura.challenge_muralis.entity.Cliente;
import com.miura.challenge_muralis.entity.Contato;
import com.miura.challenge_muralis.repository.ClienteRepository;
import com.miura.challenge_muralis.repository.ContatoRepository;
import com.miura.challenge_muralis.service.ClienteService;
import com.miura.challenge_muralis.dto.ClienteDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.List;

@RestController
@RequestMapping("/clientes")
@CrossOrigin(origins = "http://localhost:8080")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private ContatoRepository contatoRepository;

    // Cadastro de cliente
    @PostMapping("/cadastrar")
    public ResponseEntity<Object> cadastrarCliente(@RequestBody Cliente cliente) {

        // Validação de campos obrigatórios
        if (cliente.getNome() == null || cliente.getNome().isEmpty()) {
            return ResponseEntity.badRequest().body("O nome do cliente é obrigatório.");
        }
        if (cliente.getCpf() == null || cliente.getCpf().isEmpty()) {
            return ResponseEntity.badRequest().body("O CPF do cliente é obrigatório.");
        }
        if (cliente.getDataNascimento() == null) {
            return ResponseEntity.badRequest().body("A data de nascimento do cliente é obrigatória.");
        }

        // Cadastrar o cliente via ClienteService
        Cliente clienteCadastrado = clienteService.cadastrar(cliente);

        // Agora, criamos o ClienteDTO a partir do Cliente cadastrado
        ClienteDTO clienteDTO = new ClienteDTO(clienteCadastrado);

        // Retorna o ClienteDTO no corpo da resposta
        return ResponseEntity.status(HttpStatus.CREATED).body(clienteDTO);
    }

    // Listagem de todos os clientes
    @GetMapping
    public List<Cliente> listarClientes() {
        return clienteRepository.findAll();
    }

    // Busca de cliente por Nome ou CPF
    @GetMapping("/buscar")
    public ResponseEntity<?> buscarCliente(@RequestParam(required = false) String nome, @RequestParam(required = false) String cpf) {
        List<Cliente> clientes = clienteRepository.findListByNomeOrCpf(nome, cpf);
        return ResponseEntity.ok(clientes);
    }

    // Endpoint para editar um cliente
    @PatchMapping("/clientes/{id}")
    public ResponseEntity<?> editarCliente(@PathVariable Long id, @RequestBody Cliente cliente) {
        Optional<Cliente> clienteExistente = clienteRepository.findById(id);
        if (!clienteExistente.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        Cliente clienteAtualizado = clienteExistente.get();

        if (cliente.getCpf() != null && !cliente.getCpf().isEmpty()) {
            if (clienteRepository.existsByCpf(cliente.getCpf())) {
                return ResponseEntity.badRequest().body("{\"error\":\"CPF já cadastrado.\"}");
            }
            clienteAtualizado.setCpf(cliente.getCpf());
        }

        // Atualiza os outros dados
        if (cliente.getNome() != null && !cliente.getNome().isEmpty()) {
            clienteAtualizado.setNome(cliente.getNome());
        }
        if (cliente.getDataNascimento() != null) {
            clienteAtualizado.setDataNascimento(cliente.getDataNascimento());
        }
        if (cliente.getEndereco() != null && !cliente.getEndereco().isEmpty()) {
            clienteAtualizado.setEndereco(cliente.getEndereco());
        }

        clienteRepository.save(clienteAtualizado);

        return ResponseEntity.ok("{\"success\":\"Cliente atualizado com sucesso.\"}");
    }

    // Exclusão de cliente
    @DeleteMapping("/clientes/{id}")
    public ResponseEntity<String> excluirCliente(@PathVariable Long id) {
        Optional<Cliente> clienteExistente = clienteRepository.findById(id);
        if (!clienteExistente.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        // Exclui os contatos relacionados
        contatoRepository.deleteByClienteId(id);

        // Exclui o cliente
        clienteRepository.delete(clienteExistente.get());
        return ResponseEntity.ok("Cliente e seus contatos excluídos com sucesso.");
    }

    @GetMapping("/clientes/{id}")
    public ResponseEntity<Cliente> buscarClientePorId(@PathVariable Long id) {
        Optional<Cliente> cliente = clienteRepository.findById(id);
        if (cliente.isPresent()) {
            return ResponseEntity.ok(cliente.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/cpf/{cpf}")
    public ResponseEntity<ClienteDTO> buscarClientePorCpf(@PathVariable String cpf) {
        Optional<Cliente> cliente = clienteRepository.findByCpf(cpf);
        if (cliente.isPresent()) {
            ClienteDTO clienteDTO = new ClienteDTO(cliente.get());  // Converte para DTO
            return ResponseEntity.ok(clienteDTO);  // Retorna o ClienteDTO
        }
        return ResponseEntity.notFound().build();  // Cliente não encontrado
    }

    // Método GET para buscar um contato de um cliente específico
    @GetMapping("/{clienteId}/contatos/{contatoId}")
    public ResponseEntity<Contato> getContato(
        @PathVariable Long clienteId, 
        @PathVariable Long contatoId) {

        // Chama o método customizado para buscar o contato
        Contato contato = contatoRepository.findByClienteIdAndId(clienteId, contatoId);

        // Se não encontrar o contato, retorna 404
        if (contato == null) {
            return ResponseEntity.notFound().build();
        }

        // Retorna o contato encontrado com status 200
        return ResponseEntity.ok(contato);
    }

}
