package bobst.sp.compocat.models;


import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class SpDocMeta {

    @Id
    private String idDoc;  //UUID toString;

    private String refDocType;
    private String refDoc;
    private String refDocPart;
    private String refDocVersion;
    private String langCode; //UUID string;
    private String initialCode;
    private String title;
    private String sub_title;
    private String compl_info;
    private String status;
    private String validityStart;
    private String otp;



    //Contructors ---------------------------------------------------------
    public SpDocMeta() {}

    
    public SpDocMeta(String refDoc) {
        this.idDoc = UUID.randomUUID().toString();
        
        this.refDoc = refDoc;
    }

    public SpDocMeta(String refDocType, String refDoc, String refDocPart, String refDocVersion) {
        this.idDoc = UUID.randomUUID().toString();
        this.refDocType = refDocType;
        this.refDoc = refDoc;
        this.refDocPart = refDocPart;
        this.refDocVersion = refDocVersion;
    }

    //Getteurs & Setteurs -------------------------------------------------
    public void setIdDoc(String idDoc) {
        this.idDoc = idDoc;
    }

    public String getIdDoc() {
        return idDoc;
    }

    public void setRefDocType(String refDocType) {
        this.refDocType = refDocType;
    }

    public String getRefDocType() {
        return refDocType;
    }


    public String getRefDoc() {
        return refDoc;
    }


    public void setRefDoc(String refDoc) {
        this.refDoc = refDoc;
    }


    public String getRefDocPart() {
        return refDocPart;
    }


    public void setRefDocPart(String refDocPart) {
        this.refDocPart = refDocPart;
    }


    public String getRefDocVersion() {
        return refDocVersion;
    }


    public void setRefDocVersion(String refDocVersion) {
        this.refDocVersion = refDocVersion;
    }


    public String getLangCode() {
        return langCode;
    }


    public void setLangCode(String langCode) {
        this.langCode = langCode;
    }


    public String getInitialCode() {
        return initialCode;
    }


    public void setInitialCode(String initialCode) {
        this.initialCode = initialCode;
    }


    public String getTitle() {
        return title;
    }


    public void setTitle(String title) {
        this.title = title;
    }


    public String getSub_title() {
        return sub_title;
    }


    public void setSub_title(String sub_title) {
        this.sub_title = sub_title;
    }


    public String getCompl_info() {
        return compl_info;
    }


    public void setCompl_info(String compl_info) {
        this.compl_info = compl_info;
    }


    public String getStatus() {
        return status;
    }


    public void setStatus(String status) {
        this.status = status;
    }


    public String getValidityStart() {
        return validityStart;
    }


    public void setValidityStart(String validityStart) {
        this.validityStart = validityStart;
    }


    public String getOtp() {
        return otp;
    }


    public void setOtp(String otp) {
        this.otp = otp;
    }

    
}
