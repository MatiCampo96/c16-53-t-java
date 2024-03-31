package com.encuentrame.project.encuentrame.controllers;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RestController;

import com.encuentrame.project.encuentrame.entities.MyUser;
import com.encuentrame.project.encuentrame.entities.Pet;
import com.encuentrame.project.encuentrame.entities.RequestAdoption;
import com.encuentrame.project.encuentrame.enumerations.HousingType;
import com.encuentrame.project.encuentrame.repositories.MyUserRepository;
import com.encuentrame.project.encuentrame.repositories.PetRepository;
import com.encuentrame.project.encuentrame.repositories.RequestAdoptionRepository;
import com.encuentrame.project.encuentrame.service.PetService;
import com.encuentrame.project.encuentrame.services.RequestAdoptionImpl;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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

    @Controller
public class WebAdoptionController{
    @Autowired
     private UserDetailsService userDetailsService;
     private final MyUserRepository myUserRepository;
     private final PetRepository petRepository;

     @Autowired
     private PetService petService;
       @Autowired

    private RequestAdoptionImpl requestAdoptionServiceImpl;


    private RequestAdoptionRepository requestAdoptionRepository;

    @Autowired
    public WebAdoptionController(RequestAdoptionRepository requestAdoptionRepository, MyUserRepository myUserRepository, PetRepository petRepository) {
        this.myUserRepository = myUserRepository;
        this.petRepository = petRepository;
        this.requestAdoptionRepository = requestAdoptionRepository;
    }

    @GetMapping("/solicitudes")
    public String getSolicitudes(Model model) {
        List<RequestAdoption> requestAdoptions = requestAdoptionRepository.findAll();
        model.addAttribute("requestAdoptions", requestAdoptions);

        return "solicitudes";
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

    @GetMapping("/request/{petId}")
    public String request(Model model, @PathVariable UUID petId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();
        System.out.println(email);
        MyUser myUser = myUserRepository.findByEmail(email);
        model.addAttribute("user_id", myUser.getUser_id());
        model.addAttribute("name", myUser.getName());
        model.addAttribute("surname", myUser.getSurname());
        model.addAttribute("email", myUser.getEmail());
        Optional<Pet> petOptional = petRepository.findById(petId);
        if (petOptional.isPresent()) {
            Pet pet = petOptional.get();
            model.addAttribute("pet_name", pet.getPet_name());
            model.addAttribute("pet_id", pet.getPet_id());
        } else {
            // Manejo de mascota no encontrada
            // Puedes redirigir a una página de error o agregar un mensaje al modelo
        }
        return "request";
    }

    @PostMapping("/request")
    public String createRequestAdoption(
            @RequestParam("user_id") UUID userId,
            @RequestParam("pet_id") UUID petId,
            @RequestParam("salary") double salary,
            @RequestParam("housingType") HousingType housingType,
            @RequestParam("sterilizationCommitment") boolean sterilizationCommitment,
            Model model) {
    
        RequestAdoption savedRequestAdoption = requestAdoptionServiceImpl.createRequestAdoption(
                userId,
                petId,
                salary,
                housingType,
                sterilizationCommitment
        );
    
        // Realiza cualquier lógica adicional necesaria
    
        // Agrega el mensaje de éxito al modelo
        model.addAttribute("message", "Solicitud de adopción creada exitosamente");
    
        // Devuelve el nombre de la vista que mostrará el mensaje
        return "request";  // Cambia esto según el nombre de tu vista
    }    

}

}
