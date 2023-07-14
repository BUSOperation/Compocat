package bobst.sp.compocat.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import bobst.sp.compocat.models.SpDocMeta;
import bobst.sp.compocat.repositories.SpDocMetaRepository;

@Service
public class SpDocMetaService {

    @Autowired
    SpDocMetaRepository spDocMetaRepository;

    public boolean saveDocItem( SpDocMeta doc) {
        spDocMetaRepository.save(doc);
        return true;
    }


    public boolean existDocItem (String refDocType, String refDoc, String refDocPart, String refDocVersion) {
        return (spDocMetaRepository.findByrefDocTypeAndRefDocAndRefDocPartAndRefDocVersion(refDocType, refDoc, refDocPart, refDocVersion).size()>0)? true : false;
    }

    public SpDocMeta createDocMeta(String refDocType, String refDoc, String refDocPart, String refDocVersion, String langCode,
    String initialCode, String title, String sub_title, String compl_info, String status, String validityStart,
    String otp) {

        SpDocMeta docMeta;
        List<SpDocMeta> docMetaList = spDocMetaRepository.findByrefDocTypeAndRefDocAndRefDocPartAndRefDocVersion(refDocType, refDoc, refDocPart, refDocVersion);
        if (docMetaList.size()>0) {
            docMeta = docMetaList.get(0);
        } else {
            docMeta = new SpDocMeta(refDocType, refDoc, refDocPart, refDocVersion, 
                                    langCode, initialCode, title, sub_title, compl_info, status, validityStart, otp);
            spDocMetaRepository.save(docMeta);
        }

        return docMeta;
    }
    
    public String getNormalizedDocName (SpDocMeta doc) {
        return doc.getRefDocType() + "_" + doc.getRefDoc() + "_" + doc.getRefDocPart() + "_" + doc.getRefDocVersion();
    }


}
