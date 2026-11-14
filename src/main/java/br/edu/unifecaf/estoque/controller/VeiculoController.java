package br.edu.unifecaf.estoque.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

@RestController
@RequestMapping("/api/veiculos")
public class VeiculoController {

	private VeiculoRepository veiculoRepository;
	private MarcaRepository marcaRepository;
	private ModeloRepository modeloRepository;

	public VeiculoController(
			VeiculoRepository veiculoRepository,
			MarcaRepository marcaRepository,
			ModeloRepository modeloRepository) {
		this.veiculoRepository = veiculoRepository;
		this.marcaRepository = marcaRepository;
		this.modeloRepository = modeloRepository;
	}

	// GET /api/veiculos — lista todos ou filtra pelos parametros
	@GetMapping
	public List<Veiculo> listar(
			@RequestParam(required = false) Long marcaId,
			@RequestParam(required = false) Long modeloId,
			@RequestParam(required = false) Integer ano,
			@RequestParam(required = false) StatusVeiculo status,
			@RequestParam(required = false) BigDecimal precoMin,
			@RequestParam(required = false) BigDecimal precoMax) {

		List<Veiculo> todos = veiculoRepository.findAll();
		List<Veiculo> resultado = new ArrayList<>();

		for (Veiculo v : todos) {
			boolean ok = true;

			if (marcaId != null && !v.getMarca().getId().equals(marcaId)) {
				ok = false;
			}
			if (modeloId != null && !v.getModelo().getId().equals(modeloId)) {
				ok = false;
			}
			if (ano != null && !v.getAno().equals(ano)) {
				ok = false;
			}
			if (status != null && v.getStatus() != status) {
				ok = false;
			}
			if (precoMin != null && v.getPreco().compareTo(precoMin) < 0) {
				ok = false;
			}
			if (precoMax != null && v.getPreco().compareTo(precoMax) > 0) {
				ok = false;
			}

			if (ok) {
				resultado.add(v);
			}
		}

		return resultado;
	}

	// GET /api/veiculos/{id}
	@GetMapping("/{id}")
	public Veiculo buscar(@PathVariable Long id) {
		Optional<Veiculo> opcional = veiculoRepository.findById(id);
		if (opcional.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Veiculo nao encontrado");
		}
		return opcional.get();
	}

	// POST /api/veiculos — cadastra um veiculo novo
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Veiculo criar(@RequestBody Veiculo veiculo) {
		veiculo.setId(null);
		buscarMarcaEModelo(veiculo);
		return veiculoRepository.save(veiculo);
	}

	// PUT /api/veiculos/{id} — atualiza preco, km, status etc.
	@PutMapping("/{id}")
	public Veiculo atualizar(@PathVariable Long id, @RequestBody Veiculo dados) {
		Optional<Veiculo> opcional = veiculoRepository.findById(id);
		if (opcional.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Veiculo nao encontrado");
		}

		Veiculo atual = opcional.get();
		buscarMarcaEModelo(dados);

		atual.setMarca(dados.getMarca());
		atual.setModelo(dados.getModelo());
		atual.setAno(dados.getAno());
		atual.setCor(dados.getCor());
		atual.setPreco(dados.getPreco());
		atual.setQuilometragem(dados.getQuilometragem());
		atual.setStatus(dados.getStatus());

		return veiculoRepository.save(atual);
	}

	// DELETE /api/veiculos/{id}
	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void remover(@PathVariable Long id) {
		if (!veiculoRepository.existsById(id)) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Veiculo nao encontrado");
		}
		veiculoRepository.deleteById(id);
	}

	// Garante que marca e modelo existem no banco antes de salvar
	private void buscarMarcaEModelo(Veiculo veiculo) {
		if (veiculo.getMarca() == null || veiculo.getMarca().getId() == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Informe a marca (marca.id)");
		}
		if (veiculo.getModelo() == null || veiculo.getModelo().getId() == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Informe o modelo (modelo.id)");
		}

		Optional<Marca> marcaOpt = marcaRepository.findById(veiculo.getMarca().getId());
		Optional<Modelo> modeloOpt = modeloRepository.findById(veiculo.getModelo().getId());

		if (marcaOpt.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Marca nao encontrada");
		}
		if (modeloOpt.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Modelo nao encontrado");
		}

		veiculo.setMarca(marcaOpt.get());
		veiculo.setModelo(modeloOpt.get());
	}
}
