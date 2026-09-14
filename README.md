# Monitor

Projeto prático de redes.

## Requisitos

- JRE 21+ (JRE está incluso no JDK).

## Como executar

A partir do Java 11, é possível rodar arquivos `.java` diretamente, sem precisar compilar com `javac`.
Para esse projeto, há dois arquivos `.java` executáveis.

- Server: `server/src/server/ServerMain.java`
- Client: `client/src/client/ClientMain.java`

Cada um possui suporte a linhas de comando, sendo OPCIONAIS:
### Server

```bash
# Server
java server/src/server/ServerMain.java --hostname --port --max-clients
```
### Client
```bash
java client/src/client/ClientMain.java --hostname --port
```

### Valores DEFAULT
- --host 127.0.0.1
- --port 12345
- SERVER-ONLY --max-clients 10

## Exemplos
### Iniciando um servidor no localhost e na porta 3000

### Iniciando um server e 2 clients
#### Server
```bash
java server/src/server/ServerMain.java --hostname 127.0.0.1 --port 3000
```

#### Client
```bash
java client/src/client/ClientMain.java --hostname 127.0.0.1 --port 3030
```
