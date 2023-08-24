package bobst.sp.compocat.models;

import java.sql.Blob;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class SpPage {
    
    @Id
    private String idPage;  //UUID string
    
    private String idDrawing;   //UUID string
    private String page_name;
    private Integer pageOrder;
    private Blob fileSvg;
    private Blob fileJpg;
    private Blob filePdf;
    private Blob fileSen;

    
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


    public Blob getFileSvg() {
        return fileSvg;
    }


    public void setFileSvg(Blob fileSvg) {
        this.fileSvg = fileSvg;
    }


    public Blob getFileJpg() {
        return fileJpg;
    }


    public void setFileJpg(Blob fileJpg) {
        this.fileJpg = fileJpg;
    }


    public Blob getFilePdf() {
        return filePdf;
    }


    public void setFilePdf(Blob filePdf) {
        this.filePdf = filePdf;
    }


    public Blob getFileSen() {
        return fileSen;
    }


    public void setFileSen(Blob fileSen) {
        this.fileSen = fileSen;
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

    
}
