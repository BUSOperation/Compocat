package bobst.sp.compocat.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class SpLinkItemDrawing {
    
    @Id    
    private String idItem;      //UUID string
    private String idDrawing;       //UUID string

    public SpLinkItemDrawing() {}
    
    public SpLinkItemDrawing(String idItem, String idDrawing) {
        this.idDrawing = idDrawing;
        this.idItem = idItem;
    }

    

    public String getIdItem() {
        return idItem;
    }

    public void setIdItem(String idItem) {
        this.idItem = idItem;
    }



    public String getIdDrawing() {
        return idDrawing;
    }



    public void setIdDrawing(String idDrawing) {
        this.idDrawing = idDrawing;
    }
    
    
    
}
