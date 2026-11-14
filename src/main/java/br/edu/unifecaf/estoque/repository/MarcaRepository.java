package br.edu.unifecaf.estoque.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.unifecaf.estoque.model.Marca;

public interface MarcaRepository extends JpaRepository<Marca, Long> {
	Optional<Marca> findByNomeIgnoreCase(String nome);
}
