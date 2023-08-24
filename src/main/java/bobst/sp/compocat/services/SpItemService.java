package bobst.sp.compocat.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import bobst.sp.compocat.models.SpItem;
import bobst.sp.compocat.repositories.SpItemRepository;

@Service
public class SpItemService {

    @Autowired
    SpItemRepository spItemRepository;

    public SpItem createItem(String itemName, String descriptionFr, String descriptionEn, String descriptionDe) {
        SpItem item;
        List<SpItem> itemList = spItemRepository.findByItemNameAndDescriptionEn(itemName, descriptionEn); 
        if (itemList.size()>0) {
            item = itemList.get(0);
        } else {            
            itemList = spItemRepository.findByItemName(itemName);
            if(itemList.size()>0){
                item = itemList.get(0);
                if(!descriptionEn.equals("")) {                    
                    item.setDescriptionFr(descriptionFr);
                    item.setDescriptionEn(descriptionEn);
                    item.setDescriptionDe(descriptionDe);
                }

            } else {                
                item = new SpItem(itemName, descriptionFr, descriptionEn, descriptionDe);
            }
            //item = new SpItem(itemName, descriptionFr, descriptionEn, descriptionDe);
            spItemRepository.save(item);
        }

        return item;
        
    }
    
}
