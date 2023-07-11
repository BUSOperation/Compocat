package bobst.sp.compocat.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;

@Entity
@IdClass(SpLinkItemDrawingCompositeKey.class)
public class SpLinkItemDrawing {
    
    @Id    
    private String idItem;      //UUID string
    @Id
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
