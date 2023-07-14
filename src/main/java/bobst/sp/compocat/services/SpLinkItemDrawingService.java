package bobst.sp.compocat.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import bobst.sp.compocat.models.SpLinkItemDrawing;
import bobst.sp.compocat.repositories.SpLinkItemDrawingRepository;

@Service
public class SpLinkItemDrawingService {

    @Autowired
    SpLinkItemDrawingRepository spLinkItemDrawingRepository;

    public SpLinkItemDrawing createLinkItemDrawing(String idItem, String idDrawing) {

        SpLinkItemDrawing linkItemDrawing;
        
        List<SpLinkItemDrawing> linkItemDrawingList = spLinkItemDrawingRepository.findByIdItemAndIdDrawing(idItem, idDrawing);
        if (linkItemDrawingList.size()>0) {
            linkItemDrawing = linkItemDrawingList.get(0);
        } else {
            linkItemDrawing = new SpLinkItemDrawing(idItem, idDrawing);
            spLinkItemDrawingRepository.save(linkItemDrawing);  
        }

        return linkItemDrawing;
    }
    
}
