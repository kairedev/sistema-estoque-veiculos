# Sistema Automotivo — Estoque de Veículos

Aluno: **Kairê Gustavo de Oliveira** · RA **186339**  
Disciplina: Object Oriented Programming (UniFECAF)

API REST em Java + Spring Boot + MySQL para cadastro, filtro, atualização e remoção de veículos.

## Pasta do código

`modulos/object-oriented-programming/trabalho/sistema-estoque-veiculos`

## Endpoints

| Método | URL | Função |
|--------|-----|--------|
| GET | `/api/marcas` | listar marcas |
| POST | `/api/marcas` | criar marca `{"nome":"Toyota"}` |
| GET | `/api/modelos` | listar modelos |
| POST | `/api/modelos` | criar modelo `{"nome":"Corolla","marca":{"id":1}}` |
| GET | `/api/veiculos` | listar / filtrar (`marcaId`, `modeloId`, `ano`, `status`, `precoMin`, `precoMax`) |
| GET | `/api/veiculos/{id}` | buscar por id |
| POST | `/api/veiculos` | cadastrar |
| PUT | `/api/veiculos/{id}` | atualizar |
| DELETE | `/api/veiculos/{id}` | remover |

Status possíveis: `DISPONIVEL`, `VENDIDO`, `DESCONTINUADO`.

## Como rodar (depois do MySQL)

```powershell
cd "C:\Users\Pichau\Desktop\fecaf\modulos\object-oriented-programming\trabalho\sistema-estoque-veiculos"
.\mvnw.cmd spring-boot:run
```

Ajuste usuário/senha em `src/main/resources/application.properties` se precisar.

## Autor
Kairê Gustavo de Oliveira — RA 186339  
UniFECAF — Object Oriented Programming
