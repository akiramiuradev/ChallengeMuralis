package com.miura.challenge_muralis.controller;

import com.miura.challenge_muralis.dto.ContatoDTO;
import com.miura.challenge_muralis.entity.Cliente;
import com.miura.challenge_muralis.entity.Contato;
import com.miura.challenge_muralis.repository.ClienteRepository;
import com.miura.challenge_muralis.repository.ContatoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/contatos")
public class ContatoController {



    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private ContatoRepository contatoRepository;

    @PostMapping("/cadastrar")
    public ResponseEntity<ContatoDTO> cadastrarContato(@Valid @RequestBody ContatoDTO contatoDTO) {
        // Verificar se o cliente existe com base no clienteId
        Optional<Cliente> clienteExistente = clienteRepository.findById(contatoDTO.getId());

        if (!clienteExistente.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        // Cria um novo Contato com os dados do DTO
        Contato contato = new Contato();
        contato.setTipoContato(contatoDTO.getTipoContato());
        contato.setValorContato(contatoDTO.getValorContato());
        contato.setObservacao(contatoDTO.getObservacao());
        contato.setCliente(clienteExistente.get());

        // Salvar o contato no banco de dados
        contatoRepository.save(contato);

        // Retornar o contato criado no formato DTO
        ContatoDTO novoContatoDTO = new ContatoDTO(contato);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoContatoDTO);
    }




    @GetMapping("/clientes/{clienteId}")
    public ResponseEntity<List<ContatoDTO>> listarContatosPorClienteId(@PathVariable Long clienteId) {
        Optional<Cliente> clienteExistente = clienteRepository.findById(clienteId);
        if (!clienteExistente.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        List<Contato> contatos = contatoRepository.findByClienteId(clienteId);
        List<ContatoDTO> contatoDTOs = contatos.stream()
                                            .map(ContatoDTO::new)
                                            .collect(Collectors.toList());

        return ResponseEntity.ok(contatoDTOs);
    }

    // Exclusão de contato de um cliente
    @DeleteMapping("/clientes/{clienteId}/contatos/{contatoId}")
    public ResponseEntity<String> excluirContato(@PathVariable Long clienteId, @PathVariable Long contatoId) {
        // Verificar se o cliente existe
        Optional<Cliente> clienteExistente = clienteRepository.findById(clienteId);
        if (!clienteExistente.isPresent()) {
            return ResponseEntity.notFound().build(); // Cliente não encontrado
        }

        // Verificar se o contato existe
        Optional<Contato> contatoExistente = contatoRepository.findById(contatoId);
        if (!contatoExistente.isPresent()) {
            return ResponseEntity.notFound().build(); // Contato não encontrado
        }

        // Excluir o contato
        contatoRepository.delete(contatoExistente.get());

        return ResponseEntity.ok("Contato excluído com sucesso.");
    }

    //Editar um contato de um cliente
        @PatchMapping("/clientes/{clienteId}/contatos/{contatoId}")
        public ResponseEntity<String> editarContato(@PathVariable Long clienteId, @PathVariable Long contatoId, @RequestBody Contato contato) {
        // Verificar se o cliente existe
        Optional<Cliente> clienteExistente = clienteRepository.findById(clienteId);
        if (!clienteExistente.isPresent()) {
            return ResponseEntity.notFound().build(); // Cliente não encontrado
        }

        // Verificar se o contato existe
        Optional<Contato> contatoExistente = contatoRepository.findById(contatoId);
        if (!contatoExistente.isPresent()) {
            return ResponseEntity.notFound().build(); // Contato não encontrado
        }

        // Atualiza o contato com as informações recebidas
        Contato contatoAtualizado = contatoExistente.get();
        if (contato.getTipoContato() != null) {
            contatoAtualizado.setTipoContato(contato.getTipoContato());
        }
        if (contato.getValorContato() != null) {
            contatoAtualizado.setValorContato(contato.getValorContato());
        }
        if (contato.getObservacao() != null) {
            contatoAtualizado.setObservacao(contato.getObservacao());
        }

        // Salva o contato atualizado
        contatoRepository.save(contatoAtualizado);

        return ResponseEntity.ok("Contato atualizado com sucesso.");
    }
}
