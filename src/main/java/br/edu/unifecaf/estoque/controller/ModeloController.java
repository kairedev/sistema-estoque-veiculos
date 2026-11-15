package br.edu.unifecaf.estoque.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import br.edu.unifecaf.estoque.model.Marca;
import br.edu.unifecaf.estoque.model.Modelo;
import br.edu.unifecaf.estoque.repository.MarcaRepository;
import br.edu.unifecaf.estoque.repository.ModeloRepository;

@RestController
@RequestMapping("/api/modelos")
public class ModeloController {

	private ModeloRepository modeloRepository;
	private MarcaRepository marcaRepository;

	public ModeloController(ModeloRepository modeloRepository, MarcaRepository marcaRepository) {
		this.modeloRepository = modeloRepository;
		this.marcaRepository = marcaRepository;
	}

	@GetMapping
	public List<Modelo> listar(@RequestParam(required = false) Long marcaId) {
		if (marcaId != null) {
			return modeloRepository.findByMarcaId(marcaId);
		}
		return modeloRepository.findAll();
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Modelo criar(@RequestBody Modelo modelo) {
		if (modelo.getMarca() == null || modelo.getMarca().getId() == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "faltou marca.id");
		}

		Optional<Marca> marca = marcaRepository.findById(modelo.getMarca().getId());
		if (marca.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "marca nao existe");
		}

		modelo.setId(null);
		modelo.setMarca(marca.get());
		return modeloRepository.save(modelo);
	}
}
