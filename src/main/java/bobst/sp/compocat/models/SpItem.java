package bobst.sp.compocat.models;

import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class SpItem {
    
    @Id
    private String idItem;      //UUID string
    
    private String itemName;
    private String descriptionFr;
    private String descriptionEn;
    private String descriptionDe;
    private String descriptionEs;
    private String descriptionIt;
    private String descriptionPt;
    private String descriptionZh;
    private String externMatGroup;
    private String criticality;
    private String revision;
    private String state;
    private String unitOfMesure;
    private String material;
    private String partType;
    private String preconisationCode;
    private String smtClassName;

    public SpItem() {
        this.idItem = UUID.randomUUID().toString();
    }

    public SpItem(String itemName, String descriptionFr, String descriptionEn) {
        this.idItem = UUID.randomUUID().toString();
        this.itemName = itemName;
        this.descriptionFr = descriptionFr;
        this.descriptionEn = descriptionEn;
    }

    

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getDescriptionEs() {
        return descriptionEs;
    }

    public void setDescriptionEs(String descriptionEs) {
        this.descriptionEs = descriptionEs;
    }

    public String getIdItem() {
        return idItem;
    }

    public void setIdItem(String idItem) {
        this.idItem = idItem;
    }

    public String getDescriptionFr() {
        return descriptionFr;
    }

    public void setDescriptionFr(String descriptionFr) {
        this.descriptionFr = descriptionFr;
    }

    public String getDescriptionEn() {
        return descriptionEn;
    }

    public void setDescriptionEn(String descriptionEn) {
        this.descriptionEn = descriptionEn;
    }

    public String getDescriptionDe() {
        return descriptionDe;
    }

    public void setDescriptionDe(String descriptionDe) {
        this.descriptionDe = descriptionDe;
    }

    public String getDescriptionIt() {
        return descriptionIt;
    }

    public void setDescriptionIt(String descriptionIt) {
        this.descriptionIt = descriptionIt;
    }

    public String getDescriptionPt() {
        return descriptionPt;
    }

    public void setDescriptionPt(String descriptionPt) {
        this.descriptionPt = descriptionPt;
    }

    public String getDescriptionZh() {
        return descriptionZh;
    }

    public void setDescriptionZh(String descriptionZh) {
        this.descriptionZh = descriptionZh;
    }

    public String getExternMatGroup() {
        return externMatGroup;
    }

    public void setExternMatGroup(String externMatGroup) {
        this.externMatGroup = externMatGroup;
    }

    public String getCriticality() {
        return criticality;
    }

    public void setCriticality(String criticality) {
        this.criticality = criticality;
    }

    public String getRevision() {
        return revision;
    }

    public void setRevision(String revision) {
        this.revision = revision;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getUnitOfMesure() {
        return unitOfMesure;
    }

    public void setUnitOfMesure(String unitOfMesure) {
        this.unitOfMesure = unitOfMesure;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public String getPartType() {
        return partType;
    }

    public void setPartType(String partType) {
        this.partType = partType;
    }

    public String getPreconisationCode() {
        return preconisationCode;
    }

    public void setPreconisationCode(String preconisationCode) {
        this.preconisationCode = preconisationCode;
    }

    public String getSmtClassName() {
        return smtClassName;
    }

    public void setSmtClassName(String smtClassName) {
        this.smtClassName = smtClassName;
    }

    
}
