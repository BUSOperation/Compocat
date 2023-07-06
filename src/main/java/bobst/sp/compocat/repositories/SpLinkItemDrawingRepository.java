package bobst.sp.compocat.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import bobst.sp.compocat.models.SpLinkItemDrawing;

public interface SpLinkItemDrawingRepository extends CrudRepository<SpLinkItemDrawing, String> {

    public List<SpLinkItemDrawing> findByIdItemAndIdDrawing(String idItem, String idDrawing);

}
