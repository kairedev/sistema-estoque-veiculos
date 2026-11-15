# Estoque de veiculos

Aluno: Kaire Gustavo de Oliveira  
RA: 186339  
Materia: Object Oriented Programming - UniFECAF

API em Java (Spring Boot) + MySQL.  
Da pra cadastrar marca, modelo e veiculo, listar, filtrar, atualizar e apagar.

## Rodar no PC

1. MySQL ligado (eu usei usuario root e senha root)
2. Abrir PowerShell na pasta do projeto e rodar:

```
.\mvnw.cmd spring-boot:run
```

3. Testar no navegador:

http://localhost:8080/api/veiculos

## Rotas que usei

- /api/marcas
- /api/modelos
- /api/veiculos

No GET de veiculos da pra filtrar com marcaId, modeloId, ano, status, precoMin e precoMax.

Status: DISPONIVEL, VENDIDO, DESCONTINUADO
