package bobst.sp.compocat.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import bobst.sp.compocat.models.SpDocBom;

public interface SpDocBomRepository extends CrudRepository<SpDocBom, String> {

    public List<SpDocBom> findByIdDocAndIdItemParentAndIdItemAndItemOrder(String idDoc, String idItemParent, String idItem, Integer itemOrder);
    public List<SpDocBom> findByIdDoc(String idDoc);

    //@Query("select new bobst.catalog.compoCat4.models.CatBom(idDoc, itemToc, itemParent, toc, path) from CatBom where idDoc = ?1 and path = ?2")
    //@Query("select b.level_item, b.level_parent, b.id_item, d.id_drawing, b.repere, b.validity from sp_doc_bom b left join sp_link_item_drawing d on b.id_item = d.id_item where id_doc ='8b238c1e-e124-430a-b35a-9bb64c75c8ea' order by b.item_order

    // @Query("select new bobst.sp.compocat.models.QuanosMechanics(levelItem,levelParent)")
    // public void findMechanics(String idDoc) {

    // }
    
}
