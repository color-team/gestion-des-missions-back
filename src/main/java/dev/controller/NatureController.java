package dev.controller;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.controller.vm.CreerNatureDto;
import dev.domain.Nature;import dev.domain.Statut;
import dev.exception.CodeErreur;
import dev.exception.MessageErreurDto;
import dev.service.NatureService;

@RestController
@RequestMapping("natures")
@CrossOrigin(origins = "*")
public class NatureController {
	
	private NatureService service;
	
	public NatureController(NatureService service) {
		this.service = service;
	}
	
	@GetMapping
	public List<Nature> getNature(){
		return service.lister();
	}
	
	@PostMapping
	public ResponseEntity<?> creerNature(@RequestBody @Valid CreerNatureDto natureDto) {
		try {
			List<Nature> natures = service.lister();
			for(Nature nat : natures) {
				if(nat.getLibelle().toUpperCase().equals(natureDto.getLibelle().toUpperCase())) {
					return ResponseEntity.status(HttpStatus.CONFLICT).body("Nature existante");
				}
			}
			
			Nature natureCree = service.creer(natureDto.getLibelle(), natureDto.getPayee(), natureDto.getTjm(), 
				natureDto.getVersementPrime(), natureDto.getPourcentagePrime(), natureDto.getDebutValidite(), 
				natureDto.getPlafondFrais(), natureDto.getDepassementFrais());
			
			return ResponseEntity.status(HttpStatus.OK).body(natureDto);
		}catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e);
		}
	}

}
