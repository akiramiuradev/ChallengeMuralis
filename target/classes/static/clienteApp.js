// Função para cadastrar um cliente
function cadastrarCliente() {
    const nome = document.getElementById("clienteNome").value;
    const cpf = document.getElementById("clienteCpf").value;
    const dataNascimento = document.getElementById("clienteDataNascimento").value;
    const endereco = document.getElementById("clienteEndereco").value;

    const cliente = {
        nome: nome,
        cpf: cpf,
        dataNascimento: dataNascimento,
        endereco: endereco
    };

    fetch("http://localhost:8080/clientes/cadastrar", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(cliente)
    })
    .then(response => {
        if (!response.ok) {
            return response.text().then(text => { throw new Error(text || "Erro desconhecido"); });
        }
        return response.json();
    })
    .then(data => {
        alert("Cliente cadastrado com sucesso!");
        console.log("Cliente cadastrado com sucesso:", data);
        listarClientes(); // Chama a função listarClientes para atualizar a lista
        // Limpar campo de pesquisa após cadastro
        document.getElementById("campoPesquisa").value = ""; // Limpa o campo de pesquisa após cadastro
    })
    .catch(error => {
        console.error("Erro ao cadastrar cliente:", error);
        alert("Erro ao cadastrar cliente: " + error.message);
    });
}

// Função para listar todos os clientes
function listarClientes() {
    fetch("http://localhost:8080/clientes")
        .then(response => {
            if (!response.ok) {
                throw new Error("Falha ao obter a lista de clientes");
            }
            return response.json();
        })
        .then(clientes => {
            console.log("Lista de clientes:", clientes);
            const listaClientes = document.getElementById("listaClientes");
            listaClientes.innerHTML = ""; // Limpa a lista existente

            if (clientes.length === 0) {
                listaClientes.innerHTML = "<li>Não há clientes cadastrados.</li>";
            } else {
                // Se houver clientes, mostramos a lista
                clientes.forEach(cliente => {
                    const clienteItem = document.createElement("li");

                    const textoCliente = document.createElement("span");
                    textoCliente.textContent = `${cliente.nome} - ${cliente.cpf} - ${cliente.dataNascimento} - ${cliente.endereco}`;
                    textoCliente.classList.add("texto-cliente");

                    clienteItem.appendChild(textoCliente);

                    const editarBtn = document.createElement("button");
                    editarBtn.textContent = "Editar";
                    editarBtn.onclick = () => editarCliente(cliente.id); // Exibe campos de edição
                    editarBtn.classList.add("editar-btn");

                    const excluirBtn = document.createElement("button");
                    excluirBtn.textContent = "Excluir";
                    excluirBtn.onclick = () => excluirCliente(cliente.id); // Chama a função de excluir ao clicar
                    excluirBtn.classList.add("excluir-btn");

                    // Adiciona os botões aos itens da lista
                    clienteItem.appendChild(editarBtn);
                    clienteItem.appendChild(excluirBtn);

                    listaClientes.appendChild(clienteItem);
                });
            }
        })
        .catch(error => {
            console.error("Erro ao listar clientes:", error);
            alert("Erro ao listar clientes.");
        });
}

// Chama a função de listar clientes ao carregar a página
document.addEventListener("DOMContentLoaded", listarClientes);

// Função para editar um cliente
function editarCliente(clienteId) {
    console.log(`Tentando editar cliente com ID: ${clienteId}`);

    document.getElementById("editarClienteNome").value = "";
    document.getElementById("editarClienteCpf").value = "";
    document.getElementById("editarClienteDataNascimento").value = "";
    document.getElementById("editarClienteEndereco").value = "";
    
    // Exibe o formulário de edição
    document.getElementById("editarClienteForm").style.display = "block";

    // Recupera os dados do cliente da API ou da lista
    fetch(`http://localhost:8080/clientes/clientes/${clienteId}`)
        .then(response => response.json())
        .then(cliente => {
            // Preenche os campos do formulário de edição com os dados do cliente
            document.getElementById("editarClienteId").value = cliente.id;
        })
        .catch(error => {
            console.error('Erro ao buscar dados do cliente para edição:', error);
            alert("Erro ao buscar dados do cliente.");
        });
}

// Função para buscar por CPF ou Nome

function buscarCliente() {
    const pesquisa = document.getElementById("campoPesquisa").value.trim(); // Pega o valor da pesquisa

    if (pesquisa === "") {
        // Se o campo de pesquisa estiver vazio, não faz nada
        alert("Por favor, insira um CPF ou nome para buscar.");
        return;
    }

    // Verifica se o valor parece com um CPF ou nome e ajusta a requisição
    let url = "http://localhost:8080/clientes/buscar?";

    if (pesquisa.length === 11 && !isNaN(pesquisa)) {
        // Caso o valor seja um CPF (11 caracteres numéricos)
        url += `cpf=${pesquisa}`;
    } else {
        // Caso o valor seja um nome (ou qualquer coisa diferente de um CPF)
        url += `nome=${pesquisa}`;
    }

    fetch(url)
        .then(response => {
            if (!response.ok) {
                throw new Error("Falha ao realizar a busca.");
            }
            return response.json();
        })
        .then(clientes => {
            console.log("Clientes encontrados:", clientes);
            const listaPesquisa = document.getElementById("listaPesquisa");
            listaPesquisa.innerHTML = ""; // Limpa a lista existente

            if (clientes.length === 0) {
                listaPesquisa.innerHTML = "<li>Nenhum cliente encontrado.</li>";
            } else {
                // Se houver resultados, mostramos a lista
                clientes.forEach(cliente => {
                    const clienteItem = document.createElement("li");
                    clienteItem.textContent = `${cliente.nome} - ${cliente.cpf} - ${cliente.dataNascimento} - ${cliente.endereco}`;

                    // Botão de Editar
                    const editarBtn = document.createElement("button");
                    editarBtn.textContent = "Editar";
                    editarBtn.onclick = () => editarCliente(cliente.id); // Chama a função de editar

                    // Botão de Excluir
                    const excluirBtn = document.createElement("button");
                    excluirBtn.textContent = "Excluir";
                    excluirBtn.onclick = () => excluirCliente(cliente.id); // Chama a função de excluir

                    // Adiciona os botões de edição e exclusão ao item da lista
                    clienteItem.appendChild(editarBtn);
                    clienteItem.appendChild(excluirBtn);

                    listaPesquisa.appendChild(clienteItem); // Adiciona o item na lista de pesquisa
                });
            }
        })
        .catch(error => {
            console.error("Erro ao buscar clientes:", error);
            alert("Erro ao buscar clientes.");
        });
}

// Adicionando o evento de clique no botão de busca
document.addEventListener("DOMContentLoaded", function() {
    const buscarBtn = document.querySelector("#botaoBuscar"); // Certifique-se de usar o botão correto
    buscarBtn.addEventListener("click", buscarCliente);
});

// Função para atualizar os dados do cliente
function atualizarCliente() {
    const id = document.getElementById("editarClienteId").value;
    const nome = document.getElementById("editarClienteNome").value;
    const cpf = document.getElementById("editarClienteCpf").value;
    const dataNascimento = document.getElementById("editarClienteDataNascimento").value;
    const endereco = document.getElementById("editarClienteEndereco").value;

    const clienteAtualizado = {
        nome: nome,
        cpf: cpf,
        dataNascimento: dataNascimento,
        endereco: endereco
    };

    fetch(`http://localhost:8080/clientes/clientes/${id}`, {
        method: "PATCH",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(clienteAtualizado)
    })
    .then(response => {
        if (!response.ok) {
            return response.text().then(text => { throw new Error(text || "Erro desconhecido"); });
        }
        return response.json();
    })
    .then(data => {
        alert("Cliente atualizado com sucesso!");
        listarClientes(); // Atualiza a lista de clientes
        cancelarEdicao(); // Fecha o formulário de edição
    })
    .catch(error => {
        console.error("Erro ao atualizar cliente:", error);
        alert("Erro ao atualizar cliente: " + error.message);
    });
}

// Função para cancelar a edição e esconder o formulário de edição
function cancelarEdicao() {
    document.getElementById('editarClienteForm').style.display = 'none';
}

// Função para excluir um cliente
function excluirCliente(id) {
    if (confirm("Tem certeza que deseja excluir este cliente?")) {
        fetch(`http://localhost:8080/clientes/clientes/${id}`, {
            method: "DELETE"
        })
        .then(response => {
            if (!response.ok) {
                throw new Error("Erro ao excluir cliente.");
            }
            return response.text(); // Retorna a mensagem da resposta (caso seja texto)
        })
        .then(data => {
            console.log(data); // Mostra a resposta (mensagem) no console
            alert("Cliente excluído com sucesso!");
            listarClientes(); // Atualiza a lista de clientes
        })
        .catch(error => {
            console.error("Erro ao excluir cliente:", error);
            alert("Erro ao excluir cliente.");
        });
    }
}
