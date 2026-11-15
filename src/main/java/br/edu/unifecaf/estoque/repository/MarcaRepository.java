package br.edu.unifecaf.estoque.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.unifecaf.estoque.model.Marca;

public interface MarcaRepository extends JpaRepository<Marca, Long> {
}
