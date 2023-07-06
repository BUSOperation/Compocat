package bobst.sp.compocat.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import bobst.sp.compocat.models.SpDocBom;

public interface SpDocBomRepository extends CrudRepository<SpDocBom, String> {

    public List<SpDocBom> findByIdDocAndIdItemParentAndIdItemAndItemOrder(String idDoc, String idItemParent, String idItem, Integer itemOrder);
    
}
