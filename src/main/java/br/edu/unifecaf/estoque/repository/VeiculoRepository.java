package br.edu.unifecaf.estoque.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.edu.unifecaf.estoque.model.StatusVeiculo;
import br.edu.unifecaf.estoque.model.Veiculo;

public interface VeiculoRepository extends JpaRepository<Veiculo, Long> {

	@Query("""
			SELECT v FROM Veiculo v
			WHERE (:marcaId IS NULL OR v.marca.id = :marcaId)
			  AND (:modeloId IS NULL OR v.modelo.id = :modeloId)
			  AND (:ano IS NULL OR v.ano = :ano)
			  AND (:status IS NULL OR v.status = :status)
			  AND (:precoMin IS NULL OR v.preco >= :precoMin)
			  AND (:precoMax IS NULL OR v.preco <= :precoMax)
			""")
	List<Veiculo> filtrar(
			@Param("marcaId") Long marcaId,
			@Param("modeloId") Long modeloId,
			@Param("ano") Integer ano,
			@Param("status") StatusVeiculo status,
			@Param("precoMin") BigDecimal precoMin,
			@Param("precoMax") BigDecimal precoMax);
}
