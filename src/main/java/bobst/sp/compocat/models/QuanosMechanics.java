package bobst.sp.compocat.models;

public class QuanosMechanics {

    //select b.level_item, b.level_parent, b.id_item, d.id_drawing, b.repere, b.validity from sp_doc_bom b left join sp_link_item_drawing d on b.id_item = d.id_item where id_doc ='8b238c1e-e124-430a-b35a-9bb64c75c8ea' order by b.item_order

    String level_item;
    String level_parent;
    String id_item;
    String id_drawing;
    String repere;
    String validity;


    public QuanosMechanics() {
    }

    
    public String getLevel_item() {
        return level_item;
    }



    public void setLevel_item(String level_item) {
        this.level_item = level_item;
    }



    public String getLevel_parent() {
        return level_parent;
    }



    public void setLevel_parent(String level_parent) {
        this.level_parent = level_parent;
    }



    public String getId_item() {
        return id_item;
    }



    public void setId_item(String id_item) {
        this.id_item = id_item;
    }



    public String getId_drawing() {
        return id_drawing;
    }



    public void setId_drawing(String id_drawing) {
        this.id_drawing = id_drawing;
    }



    public String getRepere() {
        return repere;
    }



    public void setRepere(String repere) {
        this.repere = repere;
    }



    public String getValidity() {
        return validity;
    }



    public void setValidity(String validity) {
        this.validity = validity;
    }



    
    
    
}
