package com.encuentrame.project.encuentrame.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RestController;

import com.encuentrame.project.encuentrame.entities.Article;
import com.encuentrame.project.encuentrame.entities.MyUser;
import com.encuentrame.project.encuentrame.entities.Pet;
import com.encuentrame.project.encuentrame.entities.RequestAdoption;
import com.encuentrame.project.encuentrame.repositories.RequestAdoptionRepository;
import com.encuentrame.project.encuentrame.service.PetService;
import com.encuentrame.project.encuentrame.services.RequestAdoptionImpl;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
public class RequestAdoptionController {
    @Autowired
     private UserDetailsService userDetailsService;

     @Autowired
     private PetService petService;
       @Autowired

    private RequestAdoptionImpl requestAdoptionServiceImpl;


    private RequestAdoptionRepository requestAdoptionRepository;

    @Autowired
    public RequestAdoptionController(RequestAdoptionRepository requestAdoptionRepository) {
        this.requestAdoptionRepository = requestAdoptionRepository;
    }

  
    //GetAll
    @GetMapping("/api/requestAdoptions")
    public ResponseEntity<List<RequestAdoption>> getAllRequestAdoption() {
        List<RequestAdoption> requestAdoptions = requestAdoptionRepository.findAll(); 
        return new ResponseEntity<>(requestAdoptions, HttpStatus.OK); 
    }
    
    //GetById
    @GetMapping("/api/requestAdoptions/{id}") 
    public ResponseEntity<RequestAdoption> getRequestAdoptionById(@PathVariable UUID id) { 
        return requestAdoptionRepository.findById(id).map(requestAdoption -> new ResponseEntity<>(requestAdoption, HttpStatus.OK)) 
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND)); 
    } 
  
    @PostMapping("/api/requestAdoptions")
    public ResponseEntity<RequestAdoption> createRequestAdoption(@RequestBody RequestAdoption requestAdoption) { 
        RequestAdoption savedRequestAdoption = requestAdoptionServiceImpl.createRequestAdoption(
            requestAdoption.getMyUser().getUser_id(),
            requestAdoption.getPet().getPet_id(),
            requestAdoption.getSalary(),
            requestAdoption.getHousingType(),
            requestAdoption.isSterilizationCommitment()
        );
        return new ResponseEntity<>(savedRequestAdoption, HttpStatus.CREATED);
    }

    @Controller
public class WebAdoptionController{
    @Autowired
     private UserDetailsService userDetailsService;

     @Autowired
     private PetService petService;
       @Autowired

    private RequestAdoptionImpl requestAdoptionServiceImpl;


    private RequestAdoptionRepository requestAdoptionRepository;

    @Autowired
    public WebAdoptionController(RequestAdoptionRepository requestAdoptionRepository) {
        this.requestAdoptionRepository = requestAdoptionRepository;
    }

    @PostMapping("/solicitudes/deny")
    public String denyAdoption(@RequestParam UUID requestId, Model model) {
        boolean operationResult = requestAdoptionServiceImpl.denyAdoptionStatus(requestId);

        if (operationResult) {
            // Operación exitosa, puedes agregar atributos al modelo si es necesario
            model.addAttribute("message", "Adoption request denied successfully.");
        } else {
            // Operación fallida, puedes agregar atributos al modelo si es necesario
            model.addAttribute("message", "Failed to deny adoption request.");
        }

        // Redirige a la página que deseas recargar
        return "redirect:/solicitudes";
    }
    @PostMapping("/solicitudes/aprove")
    public String aproveAdoption(@RequestParam UUID requestId, Model model) {
        boolean operationResult = requestAdoptionServiceImpl.aproveAdoptionStatus(requestId);

        if (operationResult) {
            // Operación exitosa, puedes agregar atributos al modelo si es necesario
            model.addAttribute("message", "Adoption request denied successfully.");
        } else {
            // Operación fallida, puedes agregar atributos al modelo si es necesario
            model.addAttribute("message", "Failed to deny adoption request.");
        }

        // Redirige a la página que deseas recargar
        return "redirect:/solicitudes";
    }

}

}
