package com.easyapi.core.controller;

import com.easyapi.core.model.DynaModel;
import com.easyapi.core.repository.DynaRepository;
import com.easyapi.core.service.SequenceGeneratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/{resource}")
public class ResourceController {

    @Autowired
    private DynaRepository dynaRepository;

    @Autowired
    private SequenceGeneratorService sequenceGeneratorService;

//    @GetMapping
//    public String api(@PathVariable("username") String username) {
//        return "Hey " + username;
//    }

    @GetMapping
    public List<DynaModel> readAll(@PathVariable String resource, @RequestParam Map<String, String> params) {
        return dynaRepository.findByResource(resource);
    }

    @GetMapping("{id}")
    public DynaModel readSingle(@PathVariable String resource, @PathVariable Long id) {
        return dynaRepository.findByResourceAndId(resource, id);
    }

    @PostMapping
    public DynaModel create(@PathVariable String resource, @RequestBody DynaModel dynaModel) {
        dynaModel.setResource(resource);
        dynaModel.setId(sequenceGeneratorService.generateSequence(resource));
        return dynaRepository.save(dynaModel);
    }

    @PutMapping("{id}")
    public DynaModel update(@PathVariable String resource, @RequestBody DynaModel dynaModel, @PathVariable Long id) {
        DynaModel temp = dynaRepository.findByResourceAndId(resource, id);
        dynaModel.set_id(temp.get_id());
        dynaModel.setResource(resource);
        return dynaRepository.save(dynaModel);
    }

    // delete all data for given resource
    @DeleteMapping
    public void deleteAllData(@PathVariable String resource) {
        dynaRepository.deleteByResource(resource);
    }

    // delete a row from given resource
    @DeleteMapping("{id}")
    public void delete(@PathVariable String resource, @PathVariable Long id) {
        DynaModel temp = dynaRepository.findByResourceAndId(resource, id);
        dynaRepository.delete(temp);
    }

}
