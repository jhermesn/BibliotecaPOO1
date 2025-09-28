# Sistema de Gerenciamento de Livraria

Este projeto é um sistema de gerenciamento de livraria em Java, desenvolvido para a disciplina de Programação Orientada a Objetos 1.

## ✨ Funcionalidades

*   Gerenciamento de clientes, livros, editoras e compras.
*   Controle de estoque.

## 🛠️ Tecnologias

*   Java
*   MySQL


## 📊 Modelagem de Dados

![Diagrama de Entidades e Relacionamentos](database/DB.png)

## 🚀 Como Executar

1.  **Clone o repositório.**

2.  **Configure o banco de dados:**
    *   Crie um banco de dados MySQL.
    *   Execute o script `database/main.sql`.
    *   Configure a conexão em `resources/application.properties` (use `resources/application.properties.example` como referência).

3.  **Compile o projeto:**
    *   Certifique-se de que todos os arquivos `.java` em `src/` foram compilados para a pasta `bin/`. Você pode usar o seguinte comando a partir da raiz do projeto:
```bash
  javac -d bin -cp "lib/mysql-connector-j-9.4.0.jar;src" src/application/Main.java src/application/usecases/*.java src/domain/entities/*.java src/domain/repositories/*.java src/domain/utils/*.java src/infrastructure/db/*.java src/infrastructure/repositories/*.java src/presentation/controllers/*.java
```

4.  **Execute o projeto:**
    *  Execute o projeto com o seguinte comando:
```bash
  java -cp "bin;lib/mysql-connector-j-9.4.0.jar" application.Main
```

## 🐳 Executar com Docker (recomendado)

Este projeto possui Dockerfile e docker-compose.yml para subir o MySQL e o aplicativo Java juntos.

Modo padrão (sem VcXsrv): a interface Swing abre no navegador via noVNC.

Passos:
1) Na raiz do projeto, construa e suba os serviços em segundo plano:
```cmd
docker compose up -d --build
```
2) Aguarde o MySQL ficar saudável (opcional):
```cmd
docker compose logs -f db
```
3) Abra a UI no navegador: http://localhost:6080/vnc.html/

Observações:
- Não é necessário instalar servidor X no Windows (VcXsrv). A imagem já inclui Xvfb + x11vnc + noVNC.
- O banco de dados inicializa com o schema do arquivo database/main.sql automaticamente (apenas no primeiro start do volume).
- Credenciais padrão de exemplo: usuário `root` e senha `example`. Você pode alterar a senha ajustando `MYSQL_ROOT_PASSWORD` no `docker-compose.yml` e o arquivo `resources/application.properties.docker`.
- A porta do MySQL não é exposta ao host para evitar conflitos. Se quiser acessar o MySQL pelo host (Workbench/DBeaver), adicione no serviço `db` do docker-compose:
```yaml
ports:
  - "3307:3306"
```
E conecte em 127.0.0.1:3307.
- Para derrubar os serviços:
```cmd
docker compose down
```
- Para derrubar e remover os dados do MySQL (volume):
```cmd
docker compose down -v
```

## 📂 Estrutura do Projeto

*   `src/domain`: Entidades de negócio e interfaces dos repositórios.
*   `src/application`: Casos de uso da aplicação.
*   `src/infrastructure`: Implementação dos repositórios e configuração do banco de dados.
*   `src/presentation`: Controladores da interface com o usuário.