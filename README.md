# Executando o Docker Compose para o Projeto Task Management

Este repositório contém um arquivo `docker-compose.yml` que configura um ambiente de desenvolvimento com um banco de dados PostgreSQL, um backend (API) e um frontend React. Siga as etapas abaixo para construir e executar o projeto.

## Pré-requisitos

- Certifique-se de que o Docker e o Docker Compose estejam instalados em seu sistema. Você pode instalá-los seguindo as instruções em [https://docs.docker.com/get-docker/](https://docs.docker.com/get-docker/).

## Passos para Executar

1. Navegue até o diretório do projeto onde está localizado o arquivo `docker-compose.yml`.

2. Abra o terminal e siga as etapas abaixo:

### 1. Build do Frontend

Navegue até o diretório do frontend:

```
cd ./front-end/
```
Em seguida, construa a imagem do frontend com o seguinte comando:

```
docker build -t frontend .
```

### 2. Build da API (Backend)

Volte para o diretório raiz do projeto:

```
cd ../
```

Navegue até o diretório do backend:

```
cd ./task-management/
```

Limpe os arquivos de compilação anteriores e compile o projeto:

```
mvn clean
mvn package
```

Em seguida, construa a imagem da API com o seguinte comando:

```
docker build -t api .
```

### 3. Inicialização do Docker Compose

Agora que as imagens do frontend e da API estão construídas, você pode iniciar os contêineres com o Docker Compose:

```
docker-compose up
```

Isso iniciará os contêineres em segundo plano. Aguarde até que todos os contêineres estejam em execução.

### 4. Acesse a Aplicação

1. O frontend estará disponível em http://localhost:3000.

2. A API do backend estará disponível em http://localhost:8080.



