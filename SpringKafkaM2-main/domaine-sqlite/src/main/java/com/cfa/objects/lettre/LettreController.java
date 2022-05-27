package com.cfa.objects.lettre;


import io.micrometer.core.instrument.util.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/lettre")
public class LettreController {
    @Autowired
    private LettreRepository lettreRepository;

    @GetMapping("/getAll")
    public List<Lettre> getAll() {
        return lettreRepository.findAll();
    }

    @GetMapping("/getByid{id}")
    public Optional<Lettre> getByid(@PathVariable(value = "id")final Integer input) {
        return lettreRepository.findById(input);
    }

    @GetMapping("/getByCreationDate{date}")
    public List<Lettre> getByCreationDate(@PathVariable(value = "date")final Date date) {
        return lettreRepository.findByCreationDate(date);
    }

    @GetMapping("/getByTreatmentDate{date}")
    public List<Lettre> getByTreatmentDate(@PathVariable(value = "date")final Date date) {
        return lettreRepository.findByTreatmentDate(date);
    }

    @RequestMapping("/uploadFromCsv")
    public void uploadLettreFromCsv(){

    }

    @PostMapping("/addLettre")
    public void addLettre(@RequestBody Lettre input) {
        lettreRepository.save(input);
    }
}
