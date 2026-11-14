package br.edu.unifecaf.estoque.controller;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import br.edu.unifecaf.estoque.model.Marca;
import br.edu.unifecaf.estoque.model.Modelo;
import br.edu.unifecaf.estoque.model.StatusVeiculo;
import br.edu.unifecaf.estoque.model.Veiculo;
import br.edu.unifecaf.estoque.repository.MarcaRepository;
import br.edu.unifecaf.estoque.repository.ModeloRepository;
import br.edu.unifecaf.estoque.repository.VeiculoRepository;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/veiculos")
public class VeiculoController {

	private final VeiculoRepository veiculoRepository;
	private final MarcaRepository marcaRepository;
	private final ModeloRepository modeloRepository;

	public VeiculoController(
			VeiculoRepository veiculoRepository,
			MarcaRepository marcaRepository,
			ModeloRepository modeloRepository) {
		this.veiculoRepository = veiculoRepository;
		this.marcaRepository = marcaRepository;
		this.modeloRepository = modeloRepository;
	}

	@GetMapping
	public List<Veiculo> listar(
			@RequestParam(required = false) Long marcaId,
			@RequestParam(required = false) Long modeloId,
			@RequestParam(required = false) Integer ano,
			@RequestParam(required = false) StatusVeiculo status,
			@RequestParam(required = false) BigDecimal precoMin,
			@RequestParam(required = false) BigDecimal precoMax) {
		return veiculoRepository.filtrar(marcaId, modeloId, ano, status, precoMin, precoMax);
	}

	@GetMapping("/{id}")
	public Veiculo buscar(@PathVariable Long id) {
		return veiculoRepository.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Veiculo nao encontrado"));
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Veiculo criar(@Valid @RequestBody Veiculo veiculo) {
		veiculo.setId(null);
		vincularMarcaEModelo(veiculo);
		return veiculoRepository.save(veiculo);
	}

	@PutMapping("/{id}")
	public Veiculo atualizar(@PathVariable Long id, @Valid @RequestBody Veiculo entrada) {
		Veiculo atual = veiculoRepository.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Veiculo nao encontrado"));
		vincularMarcaEModelo(entrada);
		atual.setMarca(entrada.getMarca());
		atual.setModelo(entrada.getModelo());
		atual.setAno(entrada.getAno());
		atual.setCor(entrada.getCor());
		atual.setPreco(entrada.getPreco());
		atual.setQuilometragem(entrada.getQuilometragem());
		atual.setStatus(entrada.getStatus());
		return veiculoRepository.save(atual);
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void remover(@PathVariable Long id) {
		if (!veiculoRepository.existsById(id)) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Veiculo nao encontrado");
		}
		veiculoRepository.deleteById(id);
	}

	private void vincularMarcaEModelo(Veiculo veiculo) {
		Long marcaId = veiculo.getMarca() != null ? veiculo.getMarca().getId() : null;
		Long modeloId = veiculo.getModelo() != null ? veiculo.getModelo().getId() : null;
		if (marcaId == null || modeloId == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Informe marca.id e modelo.id");
		}
		Marca marca = marcaRepository.findById(marcaId)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Marca nao encontrada"));
		Modelo modelo = modeloRepository.findById(modeloId)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Modelo nao encontrado"));
		if (!modelo.getMarca().getId().equals(marca.getId())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Modelo nao pertence a marca informada");
		}
		veiculo.setMarca(marca);
		veiculo.setModelo(modelo);
	}
}
