package bobst.sp.compocat.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import bobst.sp.compocat.models.SpPage;

public interface SpPageRepository extends CrudRepository<SpPage,String> {

    public List<SpPage> findByIdPage(String idPage);

}
