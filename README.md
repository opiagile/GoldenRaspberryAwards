---

# Golden Raspberry Awards API

## Descrição
Esta aplicação RESTful permite consultar os produtores de filmes que ganharam o prêmio de Pior Filme no Golden Raspberry Awards, identificando os que tiveram o menor e o maior intervalo entre prêmios consecutivos.

## Tecnologias Usadas
- Java 17 (Amazon Corretto)
- Spring Boot
- Spring Data JPA
- H2 Database
- Liquibase
- Docker

## Configuração
### Pré-requisitos
- **Com Docker:** Docker e Docker Compose instalados
- **Sem Docker:** Maven e JDK 17 instalados

## Como Rodar a Aplicação

### Opção 1: Usando Docker
1. Clone o repositório.
2. Construa a imagem Docker:
   ```bash
   docker build -t goldenraspberryawards .
   ```
3. Inicie os contêineres usando Docker Compose:
   ```bash
   docker-compose up
   ```
4. A aplicação estará disponível em `http://localhost:8080`.

### Opção 2: Sem Docker (Usando Spring Boot diretamente)
1. Clone o repositório.
2. No diretório raiz do projeto, compile o projeto e execute a aplicação:
   ```bash
   mvn clean package
   mvn spring-boot:run
   ```
3. A aplicação estará disponível em `http://localhost:8080`.

### Como Executar os Testes
1. Dentro do IntelliJ ou via terminal, execute:
   ```bash
   mvn test
   ```
   Isso irá rodar os testes de integração configurados para a aplicação.

## Endpoints
### GET 
### http://localhost:8080/producers/intervals
Retorna os produtores com o menor e maior intervalo entre prêmios consecutivos.

---
