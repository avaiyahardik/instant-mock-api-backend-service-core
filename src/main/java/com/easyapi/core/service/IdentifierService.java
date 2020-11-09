package com.easyapi.core.service;

import com.easyapi.core.enums.AttributeType;
import com.easyapi.core.model.DynaModel;
import com.easyapi.core.model.Identifier;
import com.easyapi.core.repository.DynaRepository;
import com.easyapi.core.repository.IdentifierRepository;
import com.easyapi.core.util.Constant;
import com.easyapi.core.util.MockObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

@Service
public class IdentifierService {

    @Autowired
    private IdentifierRepository identifierRepository;

    @Autowired
    private DynaRepository dynaRepository;

    private Random random = new Random();

    @Autowired
    private SequenceGeneratorService sequenceGeneratorService;

    public List<Identifier> readAllIdentifier() {
        return identifierRepository.findAll();
    }

    public Identifier readSingleIdentifier(String _id) {
        return identifierRepository.findById(_id).orElse(null);
    }

    // generate resource url based with unique id in path
    private Identifier generateIdentifierUrl(Identifier identifier, HttpServletRequest request) {
        String identifierUrl = null, generateDataUrl = null;
        try {
            URL requestURL = new URL(request.getRequestURL().toString());
            String port = requestURL.getPort() == -1 ? "" : ":" + requestURL.getPort();

            // http://localhost:8082/api/_identified_id
            String host = requestURL.getProtocol() + "://" + requestURL.getHost() + port;

            identifierUrl = host + Constant.IDENTIFIER_URL_PATH.replace("{{_id}}", identifier.get_id());
            generateDataUrl = host + Constant.GENERATE_URL_PATH.replace("{{_id}}", identifier.get_id());
        } catch (MalformedURLException malEx) {
            malEx.printStackTrace();
        }
        identifier.setIdentifierUrl(identifierUrl);
        identifier.setGenerateDataUrl(generateDataUrl);
        return identifier;
    }

    public Identifier createIdentifier(Identifier identifier, Integer rowCount, HttpServletRequest request) {
        identifier.setId(sequenceGeneratorService.generateSequence(Constant.IDENTIFIER_SEQ_NAME));
        Identifier savedIdentifier = generateIdentifierUrl(identifierRepository.save(identifier), request);
        identifierRepository.save(savedIdentifier); // saving again to save URLs in the database.
        if (rowCount > 0) {
            generateData(identifier.get_id(), rowCount);
        }
        return savedIdentifier;
    }

    private void fillData(DynaModel dynaModel, Map<String, AttributeType> attributes) {
        attributes.forEach((key, type) -> {
            switch (type) {
                case String:
                    dynaModel.set(key, key + " " + dynaModel.getId());
                    break;
                case Number:
                    dynaModel.set(key, dynaModel.getId());
                    break;
                case Boolean:
                    dynaModel.set(key, random.nextBoolean());
                    break;
                case Array:
                    dynaModel.set(key, MockObject.getMockArray());
                    break;
                case Object:
                    dynaModel.set(key, MockObject.getMockObject());
                    break;
                case Date:
                    dynaModel.set(key, new Date());
                    break;
                default:
                    dynaModel.set(key, "Default " + key + " " + dynaModel.getId());
            }
        });
    }

    public List<DynaModel> generateData(String _id, Integer count) {
        int currentCount = dynaRepository.countByResource(_id);
        if (count == null || count == 0 || currentCount >= count)
            return dynaRepository.findByResource(_id);
        List<DynaModel> data = new ArrayList<>();
        DynaModel row;
        Identifier identifier = identifierRepository.findById(_id).orElse(null);
        for (int i = currentCount + 1; i <= count; i++) {
            row = new DynaModel(_id);   // creating object with resource
            row.setId(sequenceGeneratorService.generateSequence(_id));  // attaching sequence id
            fillData(row, identifier.getAttributes());
            data.add(row);
        }
        dynaRepository.saveAll(data);
        return dynaRepository.findByResource(_id);
    }

    public void cleanData(@PathVariable String _id) {
        // deleting by resource as identifier id is used as a resource name in dynamodel.
        dynaRepository.deleteByResource(_id);   // delete all data for given identifier(from dynamodel collection)
        identifierRepository.deleteById(_id);   // delete identifier(remove from identifier collection)
    }

}
