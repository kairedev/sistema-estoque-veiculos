package br.edu.unifecaf.estoque.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.edu.unifecaf.estoque.model.Marca;
import br.edu.unifecaf.estoque.repository.MarcaRepository;

@RestController
@RequestMapping("/api/marcas")
public class MarcaController {

	private MarcaRepository marcaRepository;

	public MarcaController(MarcaRepository marcaRepository) {
		this.marcaRepository = marcaRepository;
	}

	@GetMapping
	public List<Marca> listar() {
		return marcaRepository.findAll();
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Marca criar(@RequestBody Marca marca) {
		marca.setId(null);
		return marcaRepository.save(marca);
	}
}
