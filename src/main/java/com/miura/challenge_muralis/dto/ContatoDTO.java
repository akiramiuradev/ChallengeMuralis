package com.miura.challenge_muralis.dto;

import jakarta.validation.constraints.NotNull;
import com.miura.challenge_muralis.entity.Contato;

public class ContatoDTO {

    private Long id;
    
    @NotNull(message = "Tipo de contato é obrigatório.")
    private String tipoContato;

    @NotNull(message = "Tipo de contato é obrigatório.")
    private String valorContato;
    
    private String observacao;

    public ContatoDTO(Contato contato) {
        this.id = contato.getId();
        this.tipoContato = contato.getTipoContato();
        this.valorContato = contato.getValorContato();
        this.observacao = contato.getObservacao();
    }

    public ContatoDTO() {
        // construtor vazio
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTipoContato() {
        return tipoContato;
    }

    public void setTipoContato(String tipoContato) {
        this.tipoContato = tipoContato;
    }

    public String getValorContato() {
        return valorContato;
    }

    public void setValorContato(String valorContato) {
        this.valorContato = valorContato;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }
}
