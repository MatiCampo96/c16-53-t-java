package com.encuentrame.project.encuentrame.services;

import com.encuentrame.project.encuentrame.entities.Article;
import com.encuentrame.project.encuentrame.entities.MyUser;
import com.encuentrame.project.encuentrame.entities.Pet;
import com.encuentrame.project.encuentrame.entities.RequestAdoption;
import com.encuentrame.project.encuentrame.enumerations.AdoptionStatus;
import com.encuentrame.project.encuentrame.enumerations.HousingType;
import com.encuentrame.project.encuentrame.repositories.MyUserRepository;
import com.encuentrame.project.encuentrame.repositories.PetRepository;
import com.encuentrame.project.encuentrame.repositories.RequestAdoptionRepository;
import com.encuentrame.project.encuentrame.service.PetService;
import com.encuentrame.project.encuentrame.service.RequestAdoptionService;

import jakarta.persistence.Enumerated;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class RequestAdoptionImpl implements RequestAdoptionService {
    public RequestAdoptionImpl(RequestAdoptionRepository requestAdoptionRepository){
        this.requestAdoptionRepository = requestAdoptionRepository;
        this.petRepository = null;
        this.myUserRepository = null;
    }

    @Autowired
    private final PetRepository petRepository;

    @Autowired
    private final MyUserRepository myUserRepository;

    @Autowired
    private RequestAdoptionRepository requestAdoptionRepository;


    public RequestAdoption createRequestAdoption(UUID userId, UUID petId, double Salary, HousingType housingType, boolean sterializationCommitment) {
        MyUser myUser = myUserRepository.findById(userId).orElseThrow();
        Pet pet = petRepository.findById(petId).orElseThrow();
        // Crear la solicitud de adopción
        RequestAdoption requestAdoption = new RequestAdoption();
        requestAdoption.setAdoptionStatus(AdoptionStatus.WAITING);
        requestAdoption.setCreationDate(LocalDateTime.now());
        requestAdoption.setSalary(Salary);
        requestAdoption.setHousingType(housingType);
        requestAdoption.setSterilizationCommitment(sterializationCommitment);
        requestAdoption.setMyUser(myUser);
        requestAdoption.setPet(pet);
        return requestAdoptionRepository.save(requestAdoption);
    }

    public List<RequestAdoption> getAllRequestAdoption() {
        return requestAdoptionRepository.findAll();
    }

    public Optional<RequestAdoption> getRequestAdoptionById(UUID id) {
        return requestAdoptionRepository.findById(id);
    }


    public Optional<RequestAdoption> updateAdoptionStatus(UUID requestId) {
        Optional<RequestAdoption> maybeRequestAdoption = requestAdoptionRepository.findById(requestId);

        return maybeRequestAdoption.
                filter(adoption -> adoption.getAdoptionStatus() == AdoptionStatus.WAITING).
                map(requestAdoption -> {

                    // Criterios de aprobación: Salario, tipo de vivienda y compromiso de esterilización

                    boolean SalaryCriteria = requestAdoption.getSalary() >= 2000;
                    boolean housingTypeCriteria = HousingType.CASA.equals(requestAdoption.getHousingType())
                            || HousingType.APARTAMENTO.equals(requestAdoption.getHousingType());
                    boolean sterilizationCommitment = requestAdoption.isSterilizationCommitment();

                    if (SalaryCriteria && housingTypeCriteria && sterilizationCommitment) {
                        requestAdoption.setAdoptionStatus(AdoptionStatus.APPROVED);
                        requestAdoptionRepository.save(requestAdoption);

                    } else {
                        requestAdoption.setAdoptionStatus(AdoptionStatus.DENIED);
                        requestAdoptionRepository.save(requestAdoption);
                    }
                    return requestAdoption;
                });
    }


    public Optional<RequestAdoption> getRequestAdoptionByPet(Pet pet) {
        return Optional.ofNullable(requestAdoptionRepository.findByPet(pet));
    }


    public boolean deleteRequestAdoption(UUID id) {
        return requestAdoptionRepository.findById(id)
                .map(requestAdoption -> {
                    requestAdoptionRepository.delete(requestAdoption);
                    return true;
                })
                .orElse(false);
    }

    public boolean aproveAdoptionStatus(UUID requestId) {
        Optional<RequestAdoption> maybeRequestAdoption = requestAdoptionRepository.findById(requestId);
    
        return maybeRequestAdoption
                .filter(adoption -> adoption.getAdoptionStatus() == AdoptionStatus.WAITING)
                .map(requestAdoption -> {
                    requestAdoption.setAdoptionStatus(AdoptionStatus.APPROVED);
                    requestAdoptionRepository.save(requestAdoption);
                    return true;
                })
                .orElse(false);
    }
    public boolean denyAdoptionStatus(UUID requestId) {
        Optional<RequestAdoption> maybeRequestAdoption = requestAdoptionRepository.findById(requestId);
    
        return maybeRequestAdoption
                .filter(adoption -> adoption.getAdoptionStatus() == AdoptionStatus.WAITING)
                .map(requestAdoption -> {
                    requestAdoption.setAdoptionStatus(AdoptionStatus.DENIED);
                    requestAdoptionRepository.save(requestAdoption);
                    return true;
                })
                .orElse(false);
    }

}


