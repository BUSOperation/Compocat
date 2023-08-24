package bobst.sp.compocat.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import bobst.sp.compocat.models.SpPageContent;

public interface SpPageContentRepository extends CrudRepository<SpPageContent,String> {

    public List<SpPageContent> findByIdPageAndIdItemAndRepereAndContentType(String idPage, String idItem, String repere, String contentType);

}
