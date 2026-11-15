package br.edu.unifecaf.estoque.controller;

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

	public VeiculoController(VeiculoRepository veiculoRepository, MarcaRepository marcaRepository,
			ModeloRepository modeloRepository) {
		this.veiculoRepository = veiculoRepository;
		this.marcaRepository = marcaRepository;
		this.modeloRepository = modeloRepository;
	}

	// lista e filtro
	@GetMapping
	public List<Veiculo> listar(
			@RequestParam(required = false) Long marcaId,
			@RequestParam(required = false) Long modeloId,
			@RequestParam(required = false) Integer ano,
			@RequestParam(required = false) StatusVeiculo status,
			@RequestParam(required = false) Double precoMin,
			@RequestParam(required = false) Double precoMax) {

		List<Veiculo> todos = veiculoRepository.findAll();
		List<Veiculo> filtrados = new ArrayList<>();

		for (Veiculo v : todos) {
			boolean passa = true;

			if (marcaId != null && !v.getMarca().getId().equals(marcaId)) {
				passa = false;
			}
			if (modeloId != null && !v.getModelo().getId().equals(modeloId)) {
				passa = false;
			}
			if (ano != null && !v.getAno().equals(ano)) {
				passa = false;
			}
			if (status != null && v.getStatus() != status) {
				passa = false;
			}
			if (precoMin != null && v.getPreco() < precoMin) {
				passa = false;
			}
			if (precoMax != null && v.getPreco() > precoMax) {
				passa = false;
			}

			if (passa) {
				filtrados.add(v);
			}
		}

		return filtrados;
	}

	@GetMapping("/{id}")
	public Veiculo buscar(@PathVariable Long id) {
		Optional<Veiculo> veiculo = veiculoRepository.findById(id);
		if (veiculo.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "veiculo nao encontrado");
		}
		return veiculo.get();
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Veiculo criar(@RequestBody Veiculo veiculo) {
		veiculo.setId(null);
		colocarMarcaEModelo(veiculo);
		return veiculoRepository.save(veiculo);
	}

	@PutMapping("/{id}")
	public Veiculo atualizar(@PathVariable Long id, @RequestBody Veiculo dados) {
		Optional<Veiculo> veiculo = veiculoRepository.findById(id);
		if (veiculo.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "veiculo nao encontrado");
		}

		Veiculo atual = veiculo.get();
		colocarMarcaEModelo(dados);

		atual.setMarca(dados.getMarca());
		atual.setModelo(dados.getModelo());
		atual.setAno(dados.getAno());
		atual.setCor(dados.getCor());
		atual.setPreco(dados.getPreco());
		atual.setQuilometragem(dados.getQuilometragem());
		atual.setStatus(dados.getStatus());

		return veiculoRepository.save(atual);
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void remover(@PathVariable Long id) {
		if (!veiculoRepository.existsById(id)) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "veiculo nao encontrado");
		}
		veiculoRepository.deleteById(id);
	}

	private void colocarMarcaEModelo(Veiculo veiculo) {
		if (veiculo.getMarca() == null || veiculo.getMarca().getId() == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "faltou marca.id");
		}
		if (veiculo.getModelo() == null || veiculo.getModelo().getId() == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "faltou modelo.id");
		}

		Optional<Marca> marca = marcaRepository.findById(veiculo.getMarca().getId());
		Optional<Modelo> modelo = modeloRepository.findById(veiculo.getModelo().getId());

		if (marca.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "marca nao existe");
		}
		if (modelo.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "modelo nao existe");
		}

		veiculo.setMarca(marca.get());
		veiculo.setModelo(modelo.get());
	}
}
