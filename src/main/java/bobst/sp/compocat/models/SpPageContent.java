package bobst.sp.compocat.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;

@Entity
@IdClass(SpPageContentCompositeKey.class)
public class SpPageContent {

    @Id
    private String idPage;
    @Id
    private String idItem;
    @Id
    private String repere;
    @Id
    private String contentType;
    
    private Double x1;
    private Double y1;
    private Double x2;
    private Double y2;
    private Integer attachPointPosition;
    private String validity;
    private String remarque;

    public SpPageContent() {}
    
    public SpPageContent(String idPage, String idItem, String repere, String contentType) {
        this.idPage = idPage;
        this.idItem = idItem;
        this.repere = repere;
        this.contentType = contentType;
    }


    
    public Double getX1() {
        return x1;
    }

    public void setX1(Double x1) {
        this.x1 = x1;
    }

    public Double getY1() {
        return y1;
    }

    public void setY1(Double y1) {
        this.y1 = y1;
    }

    public Double getX2() {
        return x2;
    }

    public void setX2(Double x2) {
        this.x2 = x2;
    }

    public Double getY2() {
        return y2;
    }

    public void setY2(Double y2) {
        this.y2 = y2;
    }

    public Integer getAttachPointPosition() {
        return attachPointPosition;
    }

    public void setAttachPointPosition(Integer attachPointPosition) {
        this.attachPointPosition = attachPointPosition;
    }

    public String getIdPage() {
        return idPage;
    }


    public void setIdPage(String idPage) {
        this.idPage = idPage;
    }


    public String getIdItem() {
        return idItem;
    }


    public void setIdItem(String idItem) {
        this.idItem = idItem;
    }


    public String getRepere() {
        return repere;
    }


    public void setRepere(String repere) {
        this.repere = repere;
    }


    public String getContentType() {
        return contentType;
    }


    public void setContentType(String contentType) {
        this.contentType = contentType;
    }


    


    public String getValidity() {
        return validity;
    }


    public void setValidity(String validity) {
        this.validity = validity;
    }


    public String getRemarque() {
        return remarque;
    }


    public void setRemarque(String remarque) {
        this.remarque = remarque;
    }

    


    
}
