package com.memoire.inptic.base.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.memoire.inptic.base.models.Etudiant;
import com.memoire.inptic.base.models.Evaluation;
import com.memoire.inptic.base.models.Filiere;
import com.memoire.inptic.base.models.Note;
import com.memoire.inptic.base.services.EtudiantsServices;
import com.memoire.inptic.base.services.EvaluationsServices;
import com.memoire.inptic.base.services.FilieresServices;
import com.memoire.inptic.base.services.NoteService;

@Controller
public class NoteController {
	
	@Autowired
	private NoteService noteService;
	
	@Autowired
	private EtudiantsServices etudiantService;
	
	@Autowired 
	private FilieresServices filiereService;
	
	@Autowired 
	private EvaluationsServices evaluationService;
	
	private Integer filiereid;
	
	
	@GetMapping("/notes")
	public String getAll(Model model) {
		List <Note> noteListe = noteService.getAll();
		model.addAttribute("notes", noteListe);
		
		
		List<Filiere> filiereListe = filiereService.getAll();
		model.addAttribute("filieres", filiereListe);
		
		List<Evaluation> evaluationListe = evaluationService.getAll();
		model.addAttribute("evaluations", evaluationListe);
		
		return "notes";
	}
	
	
	public List<Note> getAll(Evaluation evaluation){
		if (evaluation == null) {
            return new ArrayList<>();
        }
        List<Note> notes = noteService.getAll(evaluation);

        if (notes.isEmpty()) {
        	
            List<Etudiant> etudiants = this.getEtudiantsByFiliere(filiereid);
            notes = new ArrayList<>();
            for (Etudiant etudiant : etudiants) {
                Note note = new Note();
                note.setEtudiant(etudiant);
                note.setEvaluation(evaluation);
                notes.add(note);
            }
        }

        return notes;
	}
	
	@GetMapping("/notes/etudiants")
	public List<Etudiant> getEtudiantsByFiliere(Integer id){
		
		Optional<Filiere> filiere = filiereService.getOne(id);
		
		if(filiere.isPresent()) {
			Filiere newfiliere = filiere.get();
			return newfiliere.getEtudiants();
		}
		return null;
		
	}

}
