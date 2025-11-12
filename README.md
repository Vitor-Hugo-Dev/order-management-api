# Sistema de Gerenciamento de Pedidos (Teste Técnico)

## 1\. O Desafio (O que foi pedido)

O objetivo era construir um sistema de gerenciamento de pedidos e produtos para um e-commerce, garantindo:

* **Autenticação segura com JWT.**
* **CRUD completo de produtos.**
* **Gerenciamento de pedidos com regras específicas.**
* **Otimização de queries SQL para melhor performance.**

### Requisitos

#### Autenticação

* Implementar **JWT** para autenticação.
* Criar dois perfis de usuário:
    * `ADMIN` ? Pode criar, atualizar e deletar produtos.
    * `USER` ? Pode criar pedidos e visualizar produtos.

#### Funcionalidades

* **Produtos:**
    * Criar um CRUD completo para produtos com os campos: ID (UUID), Nome, Descrição (omitido para simplicidade), Preço,
      Categoria, Quantidade em estoque, Data de criação, Data de atualização.
* **Pedidos:**
    * Um `USER` pode criar um pedido contendo múltiplos produtos.
    * O pedido deve iniciar com o status `PENDENTE`.
    * Criar uma rota para realizar o pagamento do pedido.
    * Atualizar o estoque dos produtos apenas após o pagamento do pedido.
    * Se algum produto do pedido não tiver estoque disponível, o pedido deve ser `cancelado automaticamente`, e o
      usuário informado.
    * O valor total do pedido deve ser calculado dinamicamente com base no preço atual dos produtos.
    * Criar um endpoint para listar pedidos do usuário autenticado.

#### Consultas SQL Otimizadas

* Criar endpoints que executem consultas otimizadas no MySQL:
    * Top 5 usuários que mais compraram.
    * Ticket médio dos pedidos de cada usuário.
    * Valor total faturado no mês.

#### Tecnologias

* Java 17+
* Spring Boot (Security, Data, Web, etc.)
* MySQL
* Spring Data JPA

-----

## 2\. Arquitetura e Soluções (O que foi feito)

Para atender aos requisitos, a seguinte stack e arquitetura foram implementadas:

### Stack de Tecnologia

* **Java 17** e **Spring Boot 3.5.7**
* **Spring Data JPA (Hibernate)**
* **Spring Security (JWT)**
* **MySQL 8.0**
* **Flyway**: Para versionamento e migração de schema do banco de dados.
* **Lombok**: Para redução de boilerplate.
* **Docker** & **Docker Compose**: Para a infraestrutura de contêineres.

## 3\. Requisitos para Rodar

* **Docker** e **Docker Compose** (recomendado, versão mais recente).
* **Java 17 (JDK)** e **Maven 3.x** (apenas para desenvolvimento local fora do Docker).
* Um cliente de API como **Postman** ou **Insomnia** para testar os endpoints.

-----

## 4\. Como Executar (Com Docker)

Este é o método mais simples e recomendado.

### 1\. Crie o arquivo `.env`

O `docker-compose.yml` espera um arquivo `.env` para injetar o segredo do JWT. Crie um arquivo chamado `.env` na raiz do
projeto (ele já está no `.gitignore`).

```bash
# Crie o arquivo .env
touch .env

# Adicione o segredo (o mesmo do application.yml)
echo "JWT_SECRET=bXlWZXJ5U2VjdXJlU2VjcmV0S2V5Rm9ySldUU2lnbmluZ1RoYXRJc0xvbmc=" > .env
```

### 2\. Suba os Contêineres

Na raiz do projeto, execute:

```bash
docker-compose up --build
```

A aplicação `ecommerce-app` irá pausar e aguardar o `ecommerce-db` ficar "healthy" (o que pode levar de 15 a 30
segundos). Após o banco estar pronto, a aplicação Spring Boot iniciará, o Flyway executará as migrações (V1 a V7), e o
servidor estará pronto.

A API estará disponível em: `http://localhost:8080`

### 3\. Dump do Banco de Dados

O requisito de "dump do banco" é atendido pelo **Flyway**. As migrações na pasta `src/main/resources/db/migration` criam
todo o schema do zero (V1-V3, V5) e populam o banco com um conjunto rico de dados de teste (V4, V6, V7). Não é
necessário um `dump.sql` manual.

-----

## 5\. Testando com Postman

Na raiz do projeto, você encontrará o arquivo `teste-collection.postman_collection`.

### 1\. Importar a Coleção

* Abra o Postman, vá em **File \> Import...** e selecione o arquivo `teste-collection.postman_collection`.

### 2\. Configurar a Variável de Ambiente

* A coleção usa uma variável `{{localBaseURL}}`.
* Clique na coleção "teste tecnico..." e vá para a aba **"Variables"**.
* No campo `localBaseURL`, defina o **CURRENT VALUE** como `http://localhost:8080`.

### 3\. Fluxo de Teste

1. **Execute o Login:** Vá para a pasta **auth \> login**. O corpo (Body) já está configurado para logar
   como `admin@gmail.com` (usuário do seed V4).
2. **Token Automático:** Ao executar o `login` (ou `register`), o script na aba **"Tests"** captura o token JWT da
   resposta e o salva automaticamente na variável de coleção `{{jwt_token}}`.
3. **Execute outros Endpoints:** Todas as outras requisições (Produtos, Pedidos, Usuários) já estão configuradas para
   usar este token (`Authorization: Bearer {{jwt_token}}`).

Você pode agora testar o fluxo completo:

* Logar como `USER` (user@gmail.com) e tentar criar um produto (falhará com 403).
* Logar como `ADMIN` (admin@gmail.com) e criar um produto (funcionará).
* Logar como `USER`, criar um pedido (`PENDING`).
* Tentar pagar pelo **Pedido 20** (que contém o "Mouse Gamer" sem estoque) e ver o erro
  409 (`InsufficientStockException`).
* Tentar pagar pelo **Pedido 19** (que tem estoque) e receber o status `PAID`.
* Logar como `ADMIN` e consultar os relatórios (ex: `GET /api/v1/reports/top-spenders`).

-----

## 6\. Endpoints da API

| Método   | Endpoint                          | Proteção      | Descrição                                                   |
|:---------|:----------------------------------|:--------------|:------------------------------------------------------------|
| `POST`   | `/auth/register`                  | Público       | Registra um novo `USER`.                                    |
| `POST`   | `/auth/login`                     | Público       | Autentica e retorna um JWT.                                 |
|          |                                   |               |                                                             |
| `GET`    | `/api/v1/users/list-all`          | `ADMIN`       | Lista todos os usuários.                                    |
| `PUT`    | `/api/v1/users/{userId}`          | `Autenticado` | Atualiza um usuário. (Serviço valida se é `ADMIN` ou Dono). |
|          |                                   |               |                                                             |
| `GET`    | `/api/v1/products`                | `Autenticado` | Lista produtos. Aceita `?inStock=true`.                     |
| `GET`    | `/api/v1/products/{id}`           | `Autenticado` | Busca um produto.                                           |
| `POST`   | `/api/v1/products`                | `ADMIN`       | Cria um produto.                                            |
| `PUT`    | `/api/v1/products/{id}`           | `ADMIN`       | Atualiza um produto.                                        |
| `DELETE` | `/api/v1/products/{id}`           | `ADMIN`       | Deleta um produto.                                          |
|          |                                   |               |                                                             |
| `POST`   | `/api/v1/orders`                  | `USER`        | Cria um pedido (Status `PENDING`).                          |
| `GET`    | `/api/v1/orders`                  | `USER`        | Lista os pedidos do usuário logado.                         |
| `GET`    | `/api/v1/orders/all`              | `ADMIN`       | Lista **todos** os pedidos do sistema.                      |
| `PUT`    | `/api/v1/orders/{orderId}`        | `USER`        | Atualiza um pedido `PENDING` (só o dono).                   |
| `POST`   | `/api/v1/orders/{orderId}/pay`    | `USER`        | Tenta pagar um pedido `PENDING` (só o dono).                |
| `POST`   | `/api/v1/orders/{orderId}/cancel` | `Autenticado` | Cancela um pedido (só `ADMIN` ou o dono).                   |
|          |                                   |               |                                                             |
| `GET`    | `/api/v1/reports/top-spenders`    | `ADMIN`       | Top 5 compradores.                                          |
| `GET`    | `/api/v1/reports/average-ticket`  | `ADMIN`       | Ticket médio por usuário.                                   |
| `GET`    | `/api/v1/reports/monthly-revenue` | `ADMIN`       | Faturamento do mês (ex: `?month=2025-10`).                  |