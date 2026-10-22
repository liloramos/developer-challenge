# Solução — Kingspan Isoeste Developer Challenge

## Visão geral

Neste desafio desenvolvi um sistema de solicitações de compra com autenticação, controle de acesso por perfil, níveis de aprovação, histórico e controle de estados.

Minha ideia foi manter o projeto organizado de uma forma simples de entender, separando a API, as regras de negócio, o acesso ao banco e o frontend.

O fluxo principal funciona da seguinte forma:

- O solicitante cria uma solicitação.
- O sistema calcula automaticamente o nível necessário de aprovação de acordo com o valor.
- Aprovadores ou administradores podem aprovar ou rejeitar conforme suas permissões.
- O solicitante pode cancelar uma solicitação própria enquanto ela estiver pendente.
- Todas as movimentações ficam registradas no histórico.

## Tecnologias utilizadas

### Backend

- Java 21
- Spring Boot
- Spring Web
- Spring Security
- Spring Data JPA
- Bean Validation
- JWT
- BCrypt
- PostgreSQL
- Maven

### Frontend

- React
- Vite
- React Router
- JavaScript
- Fetch API
- CSS

### Ambiente local

- Docker
- PostgreSQL 17

## Organização do backend

Organizei o backend seguindo principalmente o fluxo:

Controller -> Service -> Repository -> PostgreSQL

### Controller

Os controllers recebem as requisições HTTP, validam os dados recebidos e chamam os services responsáveis pela regra de negócio.

### Service

Deixei as principais regras de negócio nos services.

É nessa camada que o sistema decide:

- Qual nível uma solicitação precisa.
- Se o usuário possui permissão para aprovar ou rejeitar.
- Se a solicitação ainda pode mudar de estado.
- Quem pode cancelar.
- Quando uma movimentação deve ser registrada no histórico.

Preferi deixar essas regras no backend em vez de depender do frontend.

### Repository

Os repositories fazem o acesso ao PostgreSQL utilizando Spring Data JPA.

### DTOs

Usei DTOs para controlar os dados recebidos e retornados pela API.

Dessa forma, as entidades do banco não precisam ser retornadas diretamente e campos internos, como o hash da senha, não são expostos para o frontend.

## Autenticação

A autenticação foi feita com JWT.

No login, o sistema recebe o e-mail e a senha, procura o usuário cadastrado, verifica a senha utilizando BCrypt e, caso esteja correta, gera um token JWT.

Nas rotas protegidas, o frontend envia esse token no cabeçalho Authorization usando o padrão Bearer.

O JwtAuthenticationFilter recebe o token, faz a validação e identifica o usuário antes da requisição chegar ao controller.

As senhas são armazenadas utilizando BCrypt e não ficam salvas em texto puro.

A API foi configurada como stateless, portanto não existe sessão de autenticação mantida no servidor.

## Perfis de usuário

Implementei três perfis:

- SOLICITANTE
- APROVADOR
- ADMIN

O perfil APROVADOR também possui um nível:

- NIVEL_1
- NIVEL_2

Não existe um aprovador NIVEL_3, porque solicitações desse nível só podem ser aprovadas por um administrador.

## Regras de aprovação

O nível necessário é calculado automaticamente de acordo com o valor da solicitação.

Até R$ 1.000,00:

NIVEL_1

De R$ 1.000,01 até R$ 10.000,00:

NIVEL_2

Acima de R$ 10.000,00:

NIVEL_3

As permissões ficaram da seguinte forma:

- APROVADOR NIVEL_1 pode avaliar solicitações NIVEL_1.
- APROVADOR NIVEL_2 pode avaliar solicitações NIVEL_1 e NIVEL_2.
- ADMIN pode avaliar solicitações de qualquer nível.

Quando um aprovador possui permissão para aprovar solicitações, mas não possui nível suficiente para aquela solicitação específica, a API retorna 422.

Quando o perfil do usuário não permite aquela operação, a API retorna 403.

Por exemplo:

- APROVADOR NIVEL_1 tentando aprovar uma solicitação NIVEL_2 retorna 422.
- SOLICITANTE tentando aprovar uma solicitação retorna 403.

## Estados da solicitação

Toda solicitação começa com o status PENDING.

As transições permitidas são:

- PENDING para APPROVED.
- PENDING para REJECTED.
- PENDING para CANCELLED.

Os estados APPROVED, REJECTED e CANCELLED são estados finais.

Depois que uma solicitação chega em um desses estados, ela não pode mudar novamente.

Caso seja feita uma tentativa, a API retorna 422.

## Cancelamento

Uma solicitação pendente pode ser cancelada:

- Pelo próprio solicitante que criou a solicitação.
- Por um administrador.

Um aprovador não pode cancelar solicitações.

## Visibilidade das solicitações

### SOLICITANTE

O solicitante:

- Cria solicitações.
- Visualiza somente suas próprias solicitações.
- Pode cancelar suas próprias solicitações enquanto estiverem pendentes.

### APROVADOR

O aprovador:

- Visualiza as solicitações.
- Pode aprovar ou rejeitar solicitações quando o seu nível for compatível.

### ADMIN

O administrador:

- Visualiza todas as solicitações.
- Pode aprovar ou rejeitar solicitações de qualquer nível.
- Pode cancelar qualquer solicitação pendente.

Também validei que um solicitante não consegue acessar diretamente a solicitação criada por outro solicitante.

## Histórico

Cada solicitação possui um histórico de movimentações.

São registradas as ações:

- CREATED
- APPROVED
- REJECTED
- CANCELLED

Além da ação, também são armazenados:

- O usuário que realizou a movimentação.
- A data e a hora.
- O comentário, quando informado.

Isso permite acompanhar o caminho completo da solicitação desde a sua criação.

## Paginação e filtro

A listagem de solicitações possui paginação.

Exemplo de chamada:

GET /requests?page=0&size=10

Também é possível filtrar pelo status.

Exemplo:

GET /requests?status=APPROVED&page=0&size=10

Quando o usuário é SOLICITANTE, a listagem continua mostrando somente as solicitações pertencentes a ele.

## Concorrência

Na entidade PurchaseRequest utilizei a anotação @Version para controle otimista de concorrência.

A ideia é evitar que duas alterações feitas praticamente ao mesmo tempo sobrescrevam silenciosamente uma à outra.

Quando esse tipo de conflito é detectado, a API trata a situação como 409 Conflict.

## Tratamento de erros

Centralizei o tratamento das exceções da API.

Os principais códigos utilizados são:

- 400 para dados de entrada inválidos.
- 401 quando o usuário não está autenticado ou o token é inválido.
- 403 quando o usuário está autenticado, mas não possui permissão para a operação.
- 404 quando o recurso não é encontrado.
- 409 para situações de conflito.
- 422 quando uma regra de negócio impede a operação.
- 500 para erros inesperados.

Uma diferença importante no projeto é entre 403 e 422.

Usei 403 quando o perfil do usuário não permite determinada ação.

Usei 422 quando o usuário possui um perfil que pode executar aquele tipo de ação, mas uma regra de negócio impede a operação naquele caso específico.

## Principais endpoints

### Autenticação

- POST /auth/register
- POST /auth/login
- GET /auth/me

### Solicitações

- POST /requests
- GET /requests
- GET /requests/{id}

### Aprovação e cancelamento

- POST /requests/{id}/approve
- POST /requests/{id}/reject
- POST /requests/{id}/cancel

### Histórico

- GET /requests/{id}/history

## Frontend

O frontend foi desenvolvido em React.

Implementei as principais telas necessárias para o fluxo do desafio:

- Login.
- Listagem de solicitações.
- Filtro por status.
- Criação de solicitação.
- Detalhes da solicitação.
- Histórico.
- Aprovação.
- Rejeição.
- Cancelamento.

Também adicionei proteção nas rotas que exigem autenticação.

A interface exibe ou esconde ações de acordo com o perfil, o nível do usuário e o estado atual da solicitação.

Mesmo assim, essas verificações não ficam somente no frontend.

O backend sempre valida novamente a permissão antes de executar qualquer operação.

Também procurei manter uma identidade visual simples e próxima de um sistema corporativo da Kingspan Isoeste.

## Como executar

### Banco de dados

Na raiz do projeto, execute:

docker compose up -d

O PostgreSQL ficará disponível na porta 5433.

Banco utilizado:

kingspan_challenge

### Backend

Entre na pasta backend:

cd backend

No Linux ou macOS:

./mvnw spring-boot:run

No Windows PowerShell:

.\mvnw.cmd spring-boot:run

A API estará disponível em:

http://localhost:8080

### Frontend

Em outro terminal:

cd frontend

npm install

npm run dev

O frontend estará disponível em:

http://localhost:5173

## Build e testes

Para testar o backend no Windows:

.\mvnw.cmd clean test

Para gerar o build do frontend:

npm run build

No último teste realizado no backend, o resultado foi:

Tests run: 1

Failures: 0

Errors: 0

Esse teste verifica principalmente se o contexto do Spring consegue iniciar corretamente.

Não implementei uma suíte completa de testes automatizados para todas as regras de negócio dentro do tempo disponível.

Por isso, além do teste automatizado existente, validei manualmente os principais fluxos da API e depois repeti esses fluxos através do frontend.

## Cenários que validei

Durante o desenvolvimento testei uma solicitação no valor de R$ 500,00.

O sistema classificou corretamente como NIVEL_1 e um aprovador NIVEL_1 conseguiu aprovar.

Também testei uma solicitação de R$ 5.000,00.

Ela foi classificada como NIVEL_2.

Um aprovador NIVEL_1 tentou aprovar e recebeu 422.

Depois, um aprovador NIVEL_2 conseguiu aprovar normalmente.

Para testar o terceiro nível, criei uma solicitação no valor de R$ 50.000,00.

O sistema classificou como NIVEL_3.

Um aprovador NIVEL_2 tentou aprovar e recebeu 422.

Um administrador conseguiu aprovar a mesma solicitação.

Também validei os seguintes casos:

- Aprovação.
- Rejeição.
- Cancelamento.
- Tentativa de alterar uma solicitação já finalizada.
- 403 para solicitante tentando aprovar.
- Isolamento entre solicitantes.
- Acesso global do administrador.
- Criação do histórico.
- Comentários no histórico.
- Filtro por status.
- Paginação.
- Login utilizando JWT.
- Acesso sem token retornando 401.

## Algumas decisões técnicas

### BigDecimal

Usei BigDecimal nos valores monetários para evitar problemas de precisão que podem acontecer utilizando double.

### EnumType.STRING

Armazenei os enums como texto no banco para facilitar a leitura e não depender da posição numérica de cada valor.

### Regras no backend

Mesmo que o frontend esconda ou mostre botões dependendo do usuário, a decisão final sempre fica no backend.

### JWT

Utilizei autenticação stateless com JWT para não depender de sessão de autenticação no servidor.

### @Version

Utilizei controle otimista de concorrência para evitar alterações simultâneas silenciosas na mesma solicitação.

### Histórico separado

Mantive o histórico em uma entidade própria para registrar cada movimentação separadamente da entidade principal da solicitação.

## Considerações finais

Minha prioridade durante o desafio foi fazer primeiro o fluxo obrigatório funcionar de ponta a ponta.

Comecei pela autenticação, depois implementei a criação das solicitações, as regras de aprovação, a máquina de estados, o histórico e por último o frontend.

Depois que esse fluxo ficou funcionando, adicionei melhorias como paginação, filtro por status, controle de concorrência, tratamento centralizado de erros e refinamento da interface.

Procurei manter a solução simples o suficiente para conseguir explicar as decisões que tomei e, ao mesmo tempo, atender as principais regras propostas no desafio.