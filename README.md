# Gestor de Projetos API

Uma API RESTful robusta desenvolvida para o gerenciamento de projetos e membros.

## 🚀 Tecnologias e Ferramentas

*   **Linguagem:** Java
*   **Framework:** Spring Boot
*   **Segurança:** Spring Security (Autenticação e Autorização)
*   **Gerenciador de Dependências:** Maven
*   **Doc de API:** Swagger / OpenAPI
*   **Testes:** JUnit 5 e Mockito
*   **Containerização:** Docker
*   **Mapeamento de Objetos:** Padrão DTO (Data Transfer Object)

## 🧠 Estratégias e Decisões de Arquitetura

Para manter o código limpo, escalável e de fácil manutenção, foram adotadas as seguintes estratégias durante o desenvolvimento:

### 1. Arquitetura em Camadas (Layered Architecture)
O projeto segue o padrão de camadas clássico (`Controllers`, `Services`, `Repositories`), garantindo que a regra de negócio fique isolada na camada de serviço, as requisições fluam corretamente pelos controladores e o acesso aos dados seja responsabilidade exclusiva dos repositórios.

### 2. Padrão DTO (Data Transfer Object)
As entidades do banco de dados não são expostas diretamente nas respostas da API. Utilizei DTOs (`models/dtos`) para validar e formatar os dados de entrada e saída, evitando vulnerabilidades (como *Mass Assignment*) e mantendo o contrato da API desacoplado da estrutura do banco de dados.

### 3. Tratamento Global de Exceções
Foi implementado um `ControllerAdvice` no pacote `config/exceptions` para capturar exceções de maneira centralizada. Isso garante retornos padronizados, com códigos HTTP corretos (404, 400, 401, etc.) e mensagens claras para quem for consumir a API, melhorando a experiência do desenvolvedor (DX).

### 4. Validações Customizadas e Regras de Negócio
Além das anotações padrão do *Bean Validation*, criei validadores específicos (pacote `validators`) e separei algumas lógicas no pacote `helpers`. Isso despolui a camada de serviço e permite reaproveitar regras complexas, como a atribuição de membros aos projetos.

### 5. Segurança (Spring Security)
A API está protegida na camada `config/security`. Controladores como o `AuthController` lidam com a emissão e validação de permissões de acesso (tokens, roles ou sessões), garantindo que apenas usuários autorizados interajam com os dados que lhes cabem.

### 6. Isolamento de Ambientes (Profiles)
O projeto conta com *profiles* configurados (`application.properties`, `application-prod.properties`, `application-test.properties`), o que permite rodá-lo localmente com banco de dados em memória ou H2, e prepará-lo facilmente para produção com bancos relacionais robustos.

### 7. Cultura de Testes
A qualidade e estabilidade são garantidas através de testes de unidade (com JUnit e Mockito). Classes como `ProjetoServiceTest` e `MembroServiceTest` asseguram que regras de negócio cruciais funcionem como o esperado, sendo executadas a cada *build*.

## 🛠️ Como Executar o Projeto

**Via Maven (Local):**
```bash
./mvnw spring-boot:run
```

**Via Docker:**
```bash
docker build -t gestor-projetos .
docker run -p 8080:8080 gestor-projetos
```

## 📚 Documentação da API
Após iniciar o servidor, a documentação interativa (Swagger UI) pode ser acessada através do endpoint configurado (geralmente em `http://localhost:8080/swagger-ui.html` ou `/v3/api-docs`).
