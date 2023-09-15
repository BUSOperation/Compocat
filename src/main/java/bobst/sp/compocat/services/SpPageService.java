package bobst.sp.compocat.services;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
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


    public File extractJpg(String idPage) throws FileNotFoundException, IOException {

        List<SpPage> listPage = spPageRepository.findByIdPage(idPage);
        if (listPage.size()>0) {
            SpPage page = listPage.get(0);
            String pathName = "src/main/resources/Quanos/"+idPage+".jpg";
            File f = new File(pathName);
        
            try (FileOutputStream fos = new FileOutputStream(pathName)) {
            fos.write(page.getFileJpg());
            }

            return f;
        } else {
            return null;
        }
    }


}
