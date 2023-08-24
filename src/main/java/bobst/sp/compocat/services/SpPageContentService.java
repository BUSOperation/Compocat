package bobst.sp.compocat.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import bobst.sp.compocat.models.SpPageContent;
import bobst.sp.compocat.repositories.SpPageContentRepository;

@Service
public class SpPageContentService {

    @Autowired
    SpPageContentRepository spPageContentRepository;

    public SpPageContent createPageContent(String idPage, String idItem, String repere, String contentType) {
        SpPageContent pgC;
        List<SpPageContent> listPgC = spPageContentRepository.findByIdPageAndIdItemAndRepereAndContentType(idPage, idItem, repere, contentType);
        if (listPgC.size()>0) {
            pgC = listPgC.get(0);
        } else {
            pgC = new SpPageContent(idPage, idItem, repere, contentType);
            spPageContentRepository.save(pgC);
        }

        return pgC;

    }
    
}
