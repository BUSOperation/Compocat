package bobst.sp.compocat.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import bobst.sp.compocat.models.SpPage;
import bobst.sp.compocat.repositories.SpPageRepository;

@Service
public class SpPageService {
    
    @Autowired
    private SpPageRepository spPageRepository;


    public SpPage createPage(String idDrawing, String idPage) {

        SpPage page;

        List<SpPage> listPage = spPageRepository.findByIdPage(idPage);
         
        if (listPage.size()>0) {
            page = listPage.get(0);
        } else {
            page = new SpPage(idDrawing, idPage);
            spPageRepository.save(page);
        }

        return page;
        

    }


}
