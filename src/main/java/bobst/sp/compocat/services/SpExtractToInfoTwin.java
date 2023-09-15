package bobst.sp.compocat.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import bobst.sp.compocat.repositories.SpDocBomRepository;

@Service
public class SpExtractToInfoTwin {

    @Autowired
    SpDocBomRepository docBomRepository;

    public void getMechanics(String id_doc) {
        //docBomRepository.findMechanics(id_doc);
    }
    
}
