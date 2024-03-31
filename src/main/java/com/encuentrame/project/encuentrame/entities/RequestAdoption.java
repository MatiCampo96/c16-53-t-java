package com.encuentrame.project.encuentrame.entities;

import com.encuentrame.project.encuentrame.enumerations.AdoptionStatus;
import com.encuentrame.project.encuentrame.enumerations.HousingType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class RequestAdoption {

    @Id
    @UuidGenerator
    @Column(name = "adoption_id")
    private UUID adoption_id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private MyUser myUser;

    @ManyToOne
    @JoinColumn(name = "pet_id")
    private Pet pet;

    @Enumerated(EnumType.STRING)
    private AdoptionStatus adoptionStatus;

    private LocalDateTime creationDate;

    private double Salary;

    @Enumerated(EnumType.STRING)
    private HousingType housingType;

    private boolean sterilizationCommitment;


}