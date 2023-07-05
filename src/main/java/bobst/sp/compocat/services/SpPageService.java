package bobst.sp.compocat.services;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import bobst.sp.compocat.repositories.SpPageRepository;

@Service
public class SpPageService {
    
    @Autowired
    private SpPageRepository spPageRepository;


    public Integer getNbrPageDrawing(UUID idDoc) {
        spPageRepository.findById(idDoc);
        return 3; 
    }

}
