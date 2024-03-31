package com.encuentrame.project.encuentrame.controllers;

import com.encuentrame.project.encuentrame.entities.Pet;
import com.encuentrame.project.encuentrame.repositories.CareGiverRepository;
import com.encuentrame.project.encuentrame.repositories.PetRepository;
import com.encuentrame.project.encuentrame.service.PetService;
import com.encuentrame.project.encuentrame.services.StorageService;
import jakarta.validation.Valid;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.time.Clock;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/")
public class PetController {

    private static final Logger logger = LoggerFactory.getLogger(PetController.class);

    @Autowired
    private PetRepository petRepository;

    @Autowired
    private PetService petService;

    @Autowired
    private CareGiverRepository careGiverRepository;

    @Autowired
    private StorageService storageService;

    //Formulario de creación de animal
    /*@GetMapping("/creation")
    public String createPetForm(Model model) {
        model.addAttribute("Pet","Registre una mascota" );
        return ("FormCreatePet.html");
    }*/

    @GetMapping("/mascotas")
    public String getAllPets (ModelMap model) {
        List<Pet> pets = petService.getAllPets();
        model.addAttribute("pets", pets);
        return "mascotas";
    }
    
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/creation")
    public String createPetForm(Model model) {
        model.addAttribute("pet", new Pet());
        return "FormCreatePet.html";
    }


    @PostMapping("/createAnimalTest")
    public String createPetTest(@RequestParam("image") MultipartFile file) {





        try{
            String uploadImagePath = storageService.uploadImageToFileSystem(file);

            logger.info("Saved in: " + uploadImagePath );


            return ("index.html");

        }catch(Exception exception){
            logger.error("Error creating PET: " + exception.getMessage() + " cause: " + exception.getCause());
            return ("FormCreatePet.html");
        }
    }

    @PostMapping("/createAnimal")
    @PreAuthorize("hasRole('ADMIN')")
    public String createPet(Model model, @ModelAttribute("pet") Pet pet, @RequestParam("image") MultipartFile file) {



        logger.debug("The request PET is " + pet);

        try{
            String uploadImagePath = "img/pets/" + storageService.uploadImageToFileSystem(file);
            pet.setImage_url(uploadImagePath);
            System.out.println(pet.getImage_url());
            Pet createdPet = petService.createPet(pet);
            logger.info("Created pet with ID: " + createdPet.getPet_id());

            model.addAttribute("imagepath",uploadImagePath);

            return ("index.html");

        }catch(Exception exception){
            logger.error("Error creating PET: " + exception.getMessage() + " cause: " + exception.getCause());
            return ("FormCreatePet.html");
        }
    }

    @GetMapping("/descripcionmascotas")
    public String displayDescription (ModelMap model){

        //Usando ModelMap podemos establecer el listado de mascotas.
        List<Pet> pets = petService.getAllPets();
        //Se debe anclar para ser enviado a la interfaz del usuario
        model.addAttribute("Mascotas", pets);
        return "descripcion.html"; //Pendiente de crear Thymeleaf
    }


    @GetMapping("/{id}")
    public String getPetById(@PathVariable UUID id, Model model) {
        Optional<Pet> pet = petService.getPetById(id);
        pet.ifPresent(value -> model.addAttribute("Pet", value));
        return null; //Pendiente de crear Thymeleaf
    }

    @GetMapping("/update/{id}")
    public String updatePetForm(@PathVariable UUID id, Model model) {
        Optional<Pet> pet = petService.getPetById(id);
        pet.ifPresent(value -> model.addAttribute("pet", value));
        return null; // Pendiente Thymeleaf
    }

    @PostMapping("/update/{id}")
    public String updatePet(@PathVariable UUID id, @ModelAttribute Pet updatedPet) {
        petService.updatePet(id, updatedPet);
        return null; // Pendiente Thymeleaf
    }

    @GetMapping("/delete/{id}")
    public String deletePet(@PathVariable UUID id) {
        petService.deletePet(id);
        return null; // Pendiente Thymeleaf
    }

    @RestController
    @Transactional(readOnly = true)
public class PetApiController{
    @GetMapping("/api/pets")
    public ResponseEntity<List<Pet>> getAllPets() {
        List<Pet> pets = petRepository.findAll();
        if (!pets.isEmpty()) {
            return new ResponseEntity<>(pets, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}


}
