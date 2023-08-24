package bobst.sp.compocat.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import bobst.sp.compocat.models.SpDocBom;
import bobst.sp.compocat.repositories.SpDocBomRepository;

@Service
public class SpDocBomService {

    @Autowired
    SpDocBomRepository spDocBomRepository;

    
    public SpDocBom createDocBom(String idDoc, String idItemParent, String idItem, boolean toc, Integer itemOrder, String levelParent, String levelItem, String repere, String validity) {
        SpDocBom docBom;
        List<SpDocBom> docBomList = spDocBomRepository.findByIdDocAndIdItemParentAndIdItemAndItemOrder(idDoc, idItemParent, idItem, itemOrder);
        if (docBomList.size()>0) {
            docBom = docBomList.get(0);
            //System.out.println("Element Bom existe déjà");
        }  else {
            docBom = new SpDocBom(idDoc, idItemParent, idItem, toc, itemOrder, levelParent, levelItem, repere, validity);
            //System.out.println("save docbom");
            spDocBomRepository.save(docBom);
        }

        return docBom;
    }


}
