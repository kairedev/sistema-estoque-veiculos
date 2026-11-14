package br.edu.unifecaf.estoque.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.unifecaf.estoque.model.Veiculo;

// Repository = acesso ao banco da entidade Veiculo
public interface VeiculoRepository extends JpaRepository<Veiculo, Long> {
}
