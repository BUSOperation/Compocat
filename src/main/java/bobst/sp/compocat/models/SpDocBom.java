package bobst.sp.compocat.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class SpDocBom {

    @Id
    private String idDoc;           //UUID string
    private String idItemParent;    //UUID string
    private String idItem;          //UUID string
    private boolean toc;
    private Integer itemOrder;

    public SpDocBom() {}

    public SpDocBom(String idDoc, String idItemParent, String idItem, boolean toc, Integer itemOrder) {
        this.idDoc = idDoc;
        this.idItemParent = idItemParent;
        this.idItem = idItem;
        this.toc = toc;
        this.itemOrder = itemOrder;
    }

    public String getIdDoc() {
        return idDoc;
    }

    public void setIdDoc(String idDoc) {
        this.idDoc = idDoc;
    }

    public String getIdItemParent() {
        return idItemParent;
    }

    public void setIdItemParent(String idItemParent) {
        this.idItemParent = idItemParent;
    }

    public String getIdItem() {
        return idItem;
    }

    public void setIdItem(String idItem) {
        this.idItem = idItem;
    }

    public boolean isToc() {
        return toc;
    }

    public void setToc(boolean toc) {
        this.toc = toc;
    }

    public Integer getItemOrder() {
        return itemOrder;
    }

    public void setItemOrder(Integer itemOrder) {
        this.itemOrder = itemOrder;
    }


    
    
}
