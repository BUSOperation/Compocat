package bobst.sp.compocat.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

import bobst.sp.compocat.models.SpDocMeta;

public interface SpDocMetaRepository extends CrudRepository<SpDocMeta,String>{

    public boolean existsByRefDoc(String refDoc);
    public List<SpDocMeta> findByrefDocTypeAndRefDocAndRefDocPartAndRefDocVersion(String refDocType, String refDoc, String refDocPart, String refDocVersion);
    public List<SpDocMeta> findByRefDoc(String refDoc);
    //@Query("SELECT CAST(COUNT(1) AS BIT) from spDocMeta")
    //public boolean existsByRefDoc(String refDocType, String refDoc, String refDocPart, String refDocVersion);
    
}
