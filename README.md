Projeto CRUD com SQLite e Jetpack Compose

Este projeto foi desenvolvido utilizando Android Studio, com a intenção de demonstrar como construir um aplicativo Android simples que implementa um sistema CRUD (Criar, Ler, Atualizar e Deletar). O aplicativo utiliza SQLite para armazenar os dados localmente no dispositivo e Jetpack Compose para a construção da interface de usuário (UI).

Funcionalidades

O aplicativo permite que o usuário insira, visualize, edite e exclua registros em um banco de dados local. Os dados armazenados consistem em três campos principais:

Título: O título de uma entrada.

Conteúdo: O corpo ou descrição associada ao título.

Autor: O autor que escreveu ou criou o conteúdo.

O sistema de CRUD permite que os usuários realizem as seguintes operações:

Criar: O usuário pode adicionar novos registros ao banco de dados preenchendo os campos de título, conteúdo e autor. Uma vez preenchidos, os dados são salvos no banco de dados SQLite.

Ler: O aplicativo exibe uma lista de todos os registros salvos no banco de dados, com as informações de título, conteúdo e autor.

Atualizar: O usuário pode selecionar qualquer entrada existente e editar os campos de título, conteúdo ou autor. Após a edição, os dados são atualizados no banco de dados.

Deletar: O usuário tem a opção de excluir qualquer entrada da lista. Uma vez deletado, o registro é removido permanentemente do banco de dados local.

Tecnologias Utilizadas

O aplicativo foi desenvolvido utilizando as seguintes tecnologias e bibliotecas:

Jetpack Compose: Framework moderno e declarativo da Google para criação de interfaces de usuário em Android. Ele permite criar layouts de maneira mais simples e intuitiva, utilizando menos código e com uma melhor performance.

SQLite: Banco de dados relacional integrado no Android que é usado para armazenar dados de maneira persistente. O SQLite é uma solução local e eficiente para gerenciamento de dados em dispositivos móveis.

Android Studio: IDE oficial para o desenvolvimento de aplicativos Android. Ela oferece todas as ferramentas necessárias para escrever, testar e depurar aplicativos Android.

Como Usar o Aplicativo

Instalação:
Primeiro, clone o repositório para o seu computador. Abra o projeto no Android Studio e espere a sincronização das dependências ser completada.

Interface de Usuário:
Ao abrir o aplicativo, o usuário verá uma tela com um formulário simples contendo três campos: Título, Conteúdo e Autor. O usuário pode preencher esses campos e salvar a entrada, que será automaticamente registrada no banco de dados local.

Visualização de Registros:
O aplicativo possui uma tela que exibe todos os registros salvos no banco de dados. Essa tela lista as entradas com as informações de título, conteúdo e autor, permitindo que o usuário visualize facilmente todas as informações armazenadas.

Edição e Atualização:
Ao clicar sobre uma entrada na lista, o usuário será levado a uma nova tela onde poderá editar os campos de título, conteúdo ou autor. Após realizar as alterações, o usuário pode salvar os dados novamente, e as informações serão atualizadas no banco de dados.

Exclusão de Entradas:
Na tela de visualização de registros, o usuário pode excluir uma entrada clicando em um botão de exclusão ao lado de cada item da lista. Quando um item é excluído, ele é removido do banco de dados de forma permanente.

Estrutura do Projeto

O projeto está organizado da seguinte forma:

data: Contém as classes responsáveis pela interação com o banco de dados SQLite. Nessa pasta, você encontrará as operações de criação, inserção, leitura, atualização e exclusão dos registros.

ui: Abriga os arquivos de interface de usuário, desenvolvidos utilizando Jetpack Compose. Essa pasta contém as telas do aplicativo, incluindo o formulário de inserção de dados e a lista de registros.

viewmodel: Contém a lógica de controle do aplicativo. O ViewModel gerencia a comunicação entre a UI e o banco de dados, além de realizar as operações necessárias para manipular os dados.

Exemplo de Interface

A interface do aplicativo foi desenvolvida com o objetivo de ser simples e intuitiva. O formulário para inserção de dados permite ao usuário adicionar novas entradas de forma rápida e fácil. A lista de registros é exibida em uma tela separada e inclui opções para editar e excluir entradas de maneira eficiente.
