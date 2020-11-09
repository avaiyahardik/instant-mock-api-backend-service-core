package com.easyapi.core.controller;

import com.easyapi.core.model.DynaModel;
import com.easyapi.core.model.Identifier;
import com.easyapi.core.service.IdentifierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

// identifier is every new request coming in to create new API.
// it has nothing to do with user or resource yet, it is new identifier for each request.

@RestController
@RequestMapping("api/identifier")
public class IdentifierController {

    @Autowired
    private IdentifierService identifierService;

    @GetMapping
    public List<Identifier> readAllIdentifier() {
        return identifierService.readAllIdentifier();
    }

    @GetMapping("{_id}")    // _id is for string id of mongo.
    public Identifier readSingleIdentifier(@PathVariable String _id) {
        return identifierService.readSingleIdentifier(_id);
    }

    @PostMapping
    public Identifier createIdentifier(@RequestBody Identifier identifier, HttpServletRequest request, @RequestParam Integer count) {
        return identifierService.createIdentifier(identifier, count, request);
    }

    // Generate data
    @PostMapping("{_id}")
    public List<DynaModel> generateData(@PathVariable String _id, @RequestParam Integer count) {
        return identifierService.generateData(_id, count);
    }

    // clean data
    @DeleteMapping("{_id}")
    public void cleanData(@PathVariable String _id) {
        identifierService.cleanData(_id);
    }


}
