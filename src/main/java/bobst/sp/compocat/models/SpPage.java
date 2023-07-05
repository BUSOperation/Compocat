package bobst.sp.compocat.models;

import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class SpPage {
    
    @Id
    private UUID idPage;
    private UUID idDoc;
    private Integer pageOrder;

    
    //Constructors
    public SpPage() {}


    //Setteurs & Getteurs
    public UUID getIdPage() {
        return idPage;
    }

    public void setIdPage(UUID idPage) {
        this.idPage = idPage;
    }

    public UUID getIdDoc() {
        return idDoc;
    }

    public void setIdDoc(UUID idDoc) {
        this.idDoc = idDoc;
    }

    public Integer getPageOrder() {
        return pageOrder;
    }

    public void setPageOrder(Integer pageOrder) {
        this.pageOrder = pageOrder;
    }

    
}
