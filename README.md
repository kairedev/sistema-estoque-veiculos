# Sistema Automotivo - Estoque de Veiculos

Aluno: Kaire Gustavo de Oliveira  
RA: 186339  
Disciplina: Object Oriented Programming - UniFECAF

Trabalho: API REST com Java, Spring Boot e MySQL.  
Cadastro de marcas, modelos e veiculos, com consulta, filtro, atualizacao e remocao.

## Como rodar

1. MySQL ligado (usuario root / senha root, ou ajuste no application.properties)
2. No PowerShell:

```
cd caminho-da-pasta-sistema-estoque-veiculos
.\mvnw.cmd spring-boot:run
```

3. Testar no navegador: http://localhost:8080/api/veiculos

## Endpoints principais

- GET/POST /api/marcas
- GET/POST /api/modelos
- GET/POST/PUT/DELETE /api/veiculos

Filtros do GET /api/veiculos: marcaId, modeloId, ano, status, precoMin, precoMax

Status: DISPONIVEL, VENDIDO, DESCONTINUADO
