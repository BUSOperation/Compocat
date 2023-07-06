package bobst.sp.compocat.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import bobst.sp.compocat.models.SpDocMeta;
import bobst.sp.compocat.repositories.SpDocMetaRepository;

@Service
public class SpDocMetaService {

    @Autowired
    SpDocMetaRepository spDocMetaRepositeRepository;

    public boolean saveDocItem( SpDocMeta doc) {
        spDocMetaRepositeRepository.save(doc);
        return true;
    }


    public boolean existDocItem (String refDocType, String refDoc, String refDocPart, String refDocVersion) {
        return (spDocMetaRepositeRepository.findByrefDocTypeAndRefDocAndRefDocPartAndRefDocVersion(refDocType, refDoc, refDocPart, refDocVersion).size()>0)? true : false;
    }
    
}
