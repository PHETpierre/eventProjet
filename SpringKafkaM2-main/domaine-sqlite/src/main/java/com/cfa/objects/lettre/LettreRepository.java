package com.cfa.objects.lettre;

import org.springframework.batch.core.Entity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface LettreRepository extends JpaRepository<Lettre, Integer> {
    List<Lettre> findByCreationDate(Date date);
    List<Lettre> findByTreatmentDate(Date date);
//    Lettre addLettre(Lettre input);
}