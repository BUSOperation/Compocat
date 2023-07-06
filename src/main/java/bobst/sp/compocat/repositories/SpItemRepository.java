package bobst.sp.compocat.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import bobst.sp.compocat.models.SpItem;

public interface SpItemRepository extends CrudRepository<SpItem, String>{

    public List<SpItem> findByItemNameAndDescriptionEn(String itemName, String descriptionEn);

    
}
