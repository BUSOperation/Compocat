package bobst.sp.compocat.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class SpPage {
    
    @Id
    private String idPage;  //UUID string
    private String idDoc;   //UUID string
    private Integer pageOrder;

    
    //Constructors
    public SpPage() {}


    //Setteurs & Getteurs
    public String getIdPage() {
        return idPage;
    }

    public void setIdPage(String idPage) {
        this.idPage = idPage;
    }

    public String getIdDoc() {
        return idDoc;
    }

    public void setIdDoc(String idDoc) {
        this.idDoc = idDoc;
    }

    public Integer getPageOrder() {
        return pageOrder;
    }

    public void setPageOrder(Integer pageOrder) {
        this.pageOrder = pageOrder;
    }

    
}
