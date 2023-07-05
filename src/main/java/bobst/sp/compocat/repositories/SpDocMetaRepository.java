package bobst.sp.compocat.repositories;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

import bobst.sp.compocat.models.SpDocMeta;

public interface SpDocMetaRepository extends CrudRepository<SpDocMeta,UUID>{
    
}
