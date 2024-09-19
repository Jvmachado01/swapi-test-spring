# Star Wars API Consumer

Aplicação RESTful que faz requisições para a SWAPI (Star Wars API) e popula com os dados indicados um banco de dados em memória, no qual, após a população, é possível fazer operações CRUD.

API implementada para mapear dados do nome dos planetas, seu clima, terreno e em quais filmes o planeta aparece.

## Funcionalidades

- Requisição à API externa do Star Wars (SWAPI):
  - Percorre as informações no link da API para filtrar, mapear e popular um banco de dados em memória.
  
- Manipulação dos dados da SWAPI já populados no banco de dados em memória:
  - Listar todos os planetas.
  - Listar buscando por nome ou ID.
  - Criar novos planetas contendo ID, nome, terreno, clima e em quais filmes o planeta aparece.
  - Atualizar informações de um planeta.
  - Deletar um planeta.

## Como usar

- POST para `http://127.0.0.1:8080/planets/populate` vai popular o banco de dados em memória.
- GET para `http://127.0.0.1:8080/planets` vai listar todos os planetas.
- POST para `http://127.0.0.1:8080/planets` enviando os dados no corpo da requisição vai criar um novo planeta.
- PUT para `http://127.0.0.1:8080/planets`, informando o ID do planeta no corpo da requisição e uma lista de links de filmes em que o planeta aparece, vai atualizar um planeta.
- DELETE para `http://127.0.0.1:8080/planets/1`, informando o ID no link da requisição, vai excluir um planeta.

## Informações

Ao iniciar a aplicação, um banco de dados em memória será criado. Usamos a funcionalidade para requisitar dados da API externa e popular o banco em memória. Caso seja enviada mais de uma requisição para popular, métodos da aplicação irão filtrar se existem os mesmos dados já no banco, evitando assim, duplicidade de dados, isto é, planetas.

Posso popular o banco de dados de modo manual, enviando um JSON no corpo da requisição contendo os dados `name`, `terrain`, `climate`, `filmsUrl` (este último deve conter um array de links dos filmes e não o nome literal dos filmes), visto que um método de serviço já irá requisitar os dados dos filmes automaticamente.

Para atualizar um planeta, preciso informar um JSON contendo, dessa vez, o ID do planeta a ser atualizado, e também informar o array de URLs dos filmes. Para deletar, só será necessário informar o ID do planeta na rota da requisição.

A biblioteca `RestTemplate` foi utilizada, porém em novas atualizações será utilizado `WebFlux`.

Os testes realizados são unitários do CRUD após o banco de dados já estar populado.
