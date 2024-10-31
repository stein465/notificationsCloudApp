
```markdown
# Notifications Cloud App

Este é um aplicativo Spring Boot containerizado em Docker, que faz o deploy na Amazon EC2. A aplicação é responsável por escutar uma fila do AWS SQS, processar as mensagens recebidas e armazenar os dados com metadados no AWS S3.

## Funcionalidades

- **Escuta mensagens do AWS SQS**: A aplicação utiliza um cliente SQS para receber mensagens de uma fila especificada.
- **Processamento de Mensagens**: As mensagens recebidas são processadas e convertidas em um formato estruturado.
- **Armazenamento no AWS S3**: Os dados, junto com metadados relevantes, são armazenados em um bucket do AWS S3.

## Tecnologias Utilizadas

- Java 17
- Spring Boot
- Docker
- AWS SDK for Java
- AWS SQS
- AWS S3
- Maven

## Estrutura do Projeto

```
MV.NotificacaoApp
│
├── config
│   └── AwsConfig.java            # Configurações para AWS SQS e S3
│
├── model
│   └── MessageForm.java           # Modelo para as mensagens recebidas
│
├── service
│   └── SqsMessageProcessor.java    # Lógica para processar mensagens do SQS
│
└── util
    └── S3Util.java                # Utilitário para armazenar dados no S3
```

## Padrão de Mensagem

Para que a aplicação processe corretamente as mensagens, envie-as no seguinte formato JSON:

```json
{
  "name": "John Doe",
  "email": "johndoe@example.com",
  "phone": "+1-202-555-0123",
  "message": "This is a sample notification message."
}
```

## Como Executar Local

### Pré-requisitos

- Java 17
- Docker
- Maven

### Configuração

1. **Clone o repositório**:

   ```bash
   git clone https://github.com/stein465/notificationsCloudApp.git
   cd notificationsCloudApp
   ```

2. **Construir a imagem**

   Passar por processo de BuildDocker a cada pull request feita para a branch `main` (`.github/workflows/docker-build.yml`).

   ```bash
   docker run -p 8080:8080 stein465/notifications-app:latest
   ```

## Deploy na Amazon EC2

1. **Suba a imagem Docker para o Docker Hub**:

   ```bash
   docker push stein465/notifications-app:latest
   ```

2. **Execute a imagem em uma instância EC2**:

   - Faça login na sua instância EC2.
   - Rode o seguinte comando:

   ```bash
   docker run -e AWS_ACCESS_KEY_ID=acessKey -e AWS_SECRET_ACCESS_KEY=secretKey -e AWS_REGION=us-east-2 --name notifications-app -p 8080:8080 stein465/notifications-app:latest
   ```

## Contribuições

Contribuições são bem-vindas! Sinta-se à vontade para abrir issues ou pull requests.

## Licença

Este projeto está licenciado sob a [MIT License](LICENSE).
```
