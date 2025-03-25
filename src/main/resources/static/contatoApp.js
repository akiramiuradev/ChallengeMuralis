document.addEventListener('DOMContentLoaded', function() {
    function cadastrarContato() {
        const contatoClienteCpf = document.getElementById("contatoClienteCpf").value.trim(); // CPF inserido
        const contatoTipo = document.getElementById("contatoTipo").value.trim();
        const contatoValor = document.getElementById("contatoValor").value.trim();
        console.log(contatoClienteCpf);

        if (!contatoClienteCpf || contatoClienteCpf.length !== 11) {
            alert("Por favor, insira um CPF válido (11 dígitos).");
            return;
        }

        // Verificando se os campos 'Tipo' e 'Valor' estão preenchidos
        if (!contatoTipo || !contatoValor) {
            alert("Por favor, preencha todos os campos obrigatórios (Tipo e Valor do Contato).");
            return;
        }

        // Primeiro, busca o ID do cliente pelo CPF
        fetch(`http://localhost:8080/clientes/cpf/${contatoClienteCpf}`)
            .then(response => {
                if (!response.ok) {
                    throw new Error("Cliente não encontrado.");
                }
                return response.json();
            })
            .then(cliente => {
                console.log(cliente); // Verifique o que está sendo retornado do servidor
                if (!cliente || !cliente.id) {
                    throw new Error("Cliente não encontrado ou não contém ID.");
                }
                const clienteId = cliente.id; // Pegamos o ID retornado
                cadastrarContatoComId(clienteId); // Agora chamamos a função para cadastrar o contato
            })
            .catch(error => {
                alert("Erro ao buscar cliente: " + error.message);
            });
    }

    // Essa função faz o cadastro do contato usando o ID do cliente
    function cadastrarContatoComId(clienteId) {
        const contato = {
            id: clienteId, // Agora usamos o ID ao invés do CPF
            tipoContato: document.getElementById("contatoTipo").value,
            valorContato: document.getElementById("contatoValor").value,
            observacao: document.getElementById("contatoObservacao").value,
        };

        console.log(contato);

        fetch("http://localhost:8080/contatos/cadastrar", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(contato)
        })
        .then(response => {
            if (!response.ok) {
                throw new Error("Erro ao cadastrar contato.");
            }
            return response.json();
        })
        .then(data => {
            alert("Contato cadastrado com sucesso!");
        })
        .catch(error => {
            alert("Erro ao cadastrar contato: " + error.message);
        });
    }

    // Associa o evento de clique ao botão após o DOM estar carregado
    const botaoCadastro = document.getElementById('cadastrarContatoButton');
    if (botaoCadastro) {
        botaoCadastro.addEventListener('click', cadastrarContato);
    }

});

document.addEventListener('DOMContentLoaded', function() {
    const buscarContatos = document.getElementById('buscarContatos');

    if (buscarContatos) {
        console.log("Botão de busca encontrado, associando evento de clique...");
        buscarContatos.addEventListener('click', buscarClienteContato);
    } else {
        console.log("Botão de busca não encontrado!");
    }
});


// Função para buscar cliente pelo CPF e listar os contatos
function buscarClienteContato(event) {
    event.preventDefault(); // Evita o comportamento de envio de formulário

    console.log("Botão de busca clicado!F");  // Verifica se o clique é detectado

    const buscarContatosCpf = document.getElementById("buscarContatosCpf").value.trim(); // CPF inserido
    console.log(buscarContatosCpf);

    if (buscarContatosCpf.length !== 11) {
        alert("Por favor, insira um CPF válido (11 dígitos).");
        return;
    }

    // Primeiro, busca o ID do cliente pelo CPF
    fetch(`http://localhost:8080/clientes/cpf/${buscarContatosCpf}`)
        .then(response => {
            if (!response.ok) {
                throw new Error("Cliente não encontrado.");
            }
            return response.json();
        })
        .then(cliente => {
            console.log(cliente); // Verifique o que está sendo retornado do servidor
            if (!cliente || !cliente.id) {
                throw new Error("Cliente não encontrado ou não contém ID.");
            }
            const clienteId = cliente.id; // Pegamos o ID retornado
            // Agora que temos o ID do cliente, vamos listar os contatos
            listarContatosPorCpf(clienteId); // Lista os contatos do cliente
        })
        .catch(error => {
            alert("Erro ao buscar cliente: " + error.message);
        });
}

// Função para listar os contatos do cliente após buscar pelo CPF
function listarContatosPorCpf(clienteId) {
    fetch(`http://localhost:8080/contatos/clientes/${clienteId}`)
        .then(response => {
            if (!response.ok) {
                throw new Error("Erro ao listar os contatos.");
            }
            return response.json();
        })
        .then(contatos => {
            console.log(contatos); // Verifica os contatos retornados
            const listaContatos = document.getElementById("listaContatos");
            listaContatos.innerHTML = ""; // Limpa a lista existente

            if (contatos.length === 0) {
                listaContatos.innerHTML = "<li>Este cliente não tem contatos registrados.</li>";
            } else {
                // Se houver contatos, mostramos a lista
                contatos.forEach(contato => {
                    const contatoItem = document.createElement("li");
                    contatoItem.textContent = `${contato.tipoContato}: ${contato.valorContato} - ${contato.observacao}`;

                    // Botões de Editar e Excluir para cada contato
                    const editarBtn = document.createElement("button");
                    editarBtn.textContent = "Editar";
                    editarBtn.onclick = () => editarContato(clienteId, contato.id, contato.tipoContato, contato.valorContato, contato.observacao);


                    const excluirBtn = document.createElement("button");
                    excluirBtn.textContent = "Excluir";
                    excluirBtn.onclick = () => excluirContato(clienteId, contato.id); // Chama a função de excluir

                    // Adiciona os botões de edição e exclusão ao item da lista
                    contatoItem.appendChild(editarBtn);
                    contatoItem.appendChild(excluirBtn);

                    listaContatos.appendChild(contatoItem); // Adiciona o item na lista de contatos
                });
            }
        })
        .catch(error => {
            console.error("Erro ao listar contatos:", error);F
            alert("Erro ao listar contatos.");
        });
}


// Função para excluir um contato
function excluirContato(clienteId, contatoId) {
    fetch(`http://localhost:8080/contatos/clientes/${clienteId}/contatos/${contatoId}`, {
        method: "DELETE",
    })
    .then(response => {
        if (!response.ok) {
            throw new Error("Erro ao excluir o contato.");
        }
        return response.text();  // Usar .text() ao invés de .json() para mensagens simples
    })
    .then(message => {
        console.log("Resposta do servidor:", message);
        alert(message);  // Exibe a mensagem de sucesso (ex: "Contato excluído com sucesso")
        listarContatosPorCpf(clienteId);  // Recarrega a lista de contatos
    })
    .catch(error => {
        console.error("Erro ao excluir o contato:", error);
        alert(error.message);
    });
}


// Função para editar um contato
function editarContato(clienteId, contatoId, tipoContato, valorContato, observacao) {
    // Preenche os campos do formulário de edição
    document.getElementById('ContatoId').value = contatoId;
    document.getElementById('editarContatoTipo').value = tipoContato;
    document.getElementById('editarContatoValor').value = valorContato;
    document.getElementById('editarContatoObservacao').value = observacao;

    // Exibe o formulário de edição
    document.getElementById('editarContatoForm').style.display = 'block';

    // Atualiza o evento do botão "Atualizar Contato"
    document.getElementById('editarContatoBtn').onclick = function() {
        atualizarContato(clienteId, contatoId);
    };
}

function cancelarEdicaoContato() {
    document.getElementById('editarContatoForm').style.display = 'none';
    document.getElementById('contatoForm').style.display = 'block';
}


// Função para atualizar o contato
function atualizarContato(clienteId, contatoId) {
    // Primeiro, verificar se o contato existe antes de atualizar
    fetch(`http://localhost:8080/clientes/${clienteId}/contatos/${contatoId}`)
    .then(response => {
        if (!response.ok) {
            throw new Error("Contato não encontrado.");
        }
        return response.json();
    })
    .then(() => {
        // Se o contato existir, prosseguir com o PATCH para atualizar
        const contatoAtualizado = {
            tipoContato: document.getElementById("editarContatoTipo").value,
            valorContato: document.getElementById("editarContatoValor").value,
            observacao: document.getElementById("editarContatoObservacao").value
        };

        return fetch(`http://localhost:8080/contatos/clientes/${clienteId}/contatos/${contatoId}`, {
            method: "PATCH", // Método PATCH para edição
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(contatoAtualizado)
        });
    })
    .then(response => {
        if (!response.ok) {
            throw new Error("Erro ao atualizar contato.");
        }
        return response.text();
    })
    .then(message => {
        console.log("Contato atualizado:", message);
        alert("Contato atualizado com sucesso!");
        listarContatosPorCpf(clienteId); // Atualiza a lista de contatos
        document.getElementById("editarContatoForm").style.display = "none"; // Esconde o formulário de edição
    })
    .catch(error => {
        console.error("Erro ao atualizar o contato:", error);
        alert(error.message);
    });
}



