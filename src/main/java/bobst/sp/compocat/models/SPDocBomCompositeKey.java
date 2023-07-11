package bobst.sp.compocat.models;

import java.io.Serializable;

public class SPDocBomCompositeKey implements Serializable{

    
    private String idDoc;           //UUID string
    private String idItemParent;    //UUID string
    private String idItem;          //UUID string
    private Integer itemOrder;
    
}
