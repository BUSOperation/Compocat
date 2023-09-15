package bobst.sp.compocat.models;


import org.hibernate.annotations.Type;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;

@Entity
public class SpPage {
    
    @Id
    private String idPage;  //UUID string
    
    private String idDrawing;   //UUID string
    private String page_name;
    private Integer pageOrder;
    
    private byte[] fileSvg;   
    private byte[] fileJpg;   
    private byte[] fileSen;

    
    //Constructors
    public SpPage() {}   


    public SpPage(String idDrawing, String idPage) {
        this.idPage = idPage;
        this.idDrawing = idDrawing;
    }


    //Setteurs & Getteurs
    public String getIdPage() {
        return idPage;
    }

    public void setIdPage(String idPage) {
        this.idPage = idPage;
    }

    

    public String getPage_name() {
        return page_name;
    }


    public void setPage_name(String page_name) {
        this.page_name = page_name;
    }

    
    


    public byte[] getFileSvg() {
        return fileSvg;
    }


    public void setFileSvg(byte[] fileSvg) {
        this.fileSvg = fileSvg;
    }


    public byte[] getFileJpg() {
        return fileJpg;
    }


    public void setFileJpg(byte[] fileJpg) {
        this.fileJpg = fileJpg;
    }


    public Integer getPageOrder() {
        return pageOrder;
    }

    public void setPageOrder(Integer pageOrder) {
        this.pageOrder = pageOrder;
    }


    public String getIdDrawing() {
        return idDrawing;
    }


    public void setIdDrawing(String idDrawing) {
        this.idDrawing = idDrawing;
    }


    public byte[] getFileSen() {
        return fileSen;
    }


    public void setFileSen(byte[] fileSen) {
        this.fileSen = fileSen;
    }

    
}
