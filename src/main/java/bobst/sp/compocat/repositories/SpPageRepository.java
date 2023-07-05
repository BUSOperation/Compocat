package bobst.sp.compocat.repositories;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

import bobst.sp.compocat.models.SpPage;

public interface SpPageRepository extends CrudRepository<SpPage,UUID> {
    
}
