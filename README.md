# ChallengeMuralis
Descrição
    Challenge Muralis é um sistema de gerenciamento de clientes e contatos. Ele permite cadastrar, listar, editar e excluir clientes e seus respectivos contatos.

# Tecnologias Utilizadas:
    Java 17
    Spring Boot 3
    MySQL
    Hibernate
    Maven
    HTML, CSS e JavaScript

# Dependências
    Confira as dependências utilizadas no projeto no arquivo pom.xml.

# Como Rodar o Projeto
    Clone o repositório:
    git clone https://github.com/seu-usuario/challenge-muralis.git

# Acesse a pasta do projeto:
    cd challenge-muralis
    Configure o banco de dados MySQL:
    Crie um banco de dados chamado comercio_sa
    Atualize as credenciais do banco no application.properties.
    Execute o projeto:
    mvn spring-boot:run
    Acesse a aplicação pelo navegador em http://localhost:8080.

# Estrutura do Projeto
    challenge_muralis
        controller – Contém os controladores da API.
        entity – Modelos das entidades do banco.
        dto – Objetos de transferência de dados.
        services – Lógica de negócios.
        repository – Interfaces para acesso ao banco de dados.


# Funcionalidades
 # Clientes
      Cadastrar Cliente – Preencha o formulário com nome, CPF e outros dados.
      Listar Clientes – Exibe uma lista com todos os clientes cadastrados.
      Buscar Cliente – Permite buscar clientes pelo nome ou CPF.
      Editar Cliente – Atualiza os dados de um cliente cadastrado.
      Excluir Cliente – Remove um cliente da base de dados.
  
  # Contatos
      Cadastrar Contato – Associe um contato a um cliente específico informando nome, telefone e e-mail.
      Listar Contatos – Exibe os contatos associados a um cliente selecionado.
      Buscar Contato – Permite buscar contatos pelo nome dentro de um cliente.
      Editar Contato – Atualiza os dados de um contato existente.
      Excluir Contato – Remove um contato da base de dados.
  
  # Instruções de Uso
      Acesse a aplicação via navegador.
      Para cadastrar um cliente, vá até a seção de clientes e preencha os campos obrigatórios.
      Para adicionar contatos a um cliente, selecione um cliente e preencha o formulário de contatos.
      Use a barra de busca para localizar clientes e contatos.
      Utilize os botões de edição e exclusão conforme necessário.

Autor
    Desenvolvido por Guilherme Akira Miura.
