package br.edu.unifecaf.estoque.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.unifecaf.estoque.model.Modelo;

public interface ModeloRepository extends JpaRepository<Modelo, Long> {
	List<Modelo> findByMarcaId(Long marcaId);
}
