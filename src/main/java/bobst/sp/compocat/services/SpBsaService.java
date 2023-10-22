package bobst.sp.compocat.services;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import bobst.sp.compocat.models.SpDocBom;
import bobst.sp.compocat.models.SpDocMeta;
import bobst.sp.compocat.models.SpItem;
import bobst.sp.compocat.models.SpLinkItemDrawing;
import bobst.sp.compocat.models.SpPage;
import bobst.sp.compocat.models.SpPageContent;
import bobst.sp.compocat.repositories.SpDocBomRepository;
import bobst.sp.compocat.repositories.SpDocMetaRepository;
import bobst.sp.compocat.repositories.SpItemRepository;
import bobst.sp.compocat.repositories.SpLinkItemDrawingRepository;
import bobst.sp.compocat.repositories.SpPageContentRepository;

@Service
public class SpBsaService {

    @Autowired
    SpDocMetaRepository spDocMetaRepository;

    @Autowired
    SpDocBomRepository spDocBomRepository;

    @Autowired
    SpItemRepository spItemRepository;

    @Autowired
    SpLinkItemDrawingRepository spLinkItemDrawingRepository;

    @Autowired
    SpItemService itemService;

    @Autowired
    SpDocBomService docBomService;

    @Autowired
    SpDocMetaService docMetaService;

    @Autowired
    SpLinkItemDrawingService linkItemDrawingService;

    @Autowired
    SpPageService pageService;

    @Autowired
    SpPageContentService pageContentService;

    @Autowired
    SpPageContentRepository spPageContentRepository;

    private int cptItem = 0;
    private SpItem item;
    private SpItem itemParent;
    private SpItem itemM, itemPM, itemP;
    private SpDocMeta docMeta;
    private SpDocMeta docMetaD;
    private String path;
    private SpPage pg;

    private String identPage = "";
    private String identGoc = "";


    public void uploadE43Xml(MultipartFile file) throws ParserConfigurationException, SAXException, IllegalStateException, IOException {

        //créer l'objet docMeta pour le document E43
        String fName = file.getOriginalFilename().substring(0,file.getOriginalFilename().lastIndexOf("."));
        String[] tab = fName.split("_");
        this.docMeta = docMetaService.createDocMeta(tab[0], tab[1]+"_"+tab[2], tab[3], tab[4], "", tab[2], "", "", "", "", "", "");
                       
        File xmlFile = new File("src/main/resources/" + file.getOriginalFilename());


        try (OutputStream os = new FileOutputStream(xmlFile)) {
            os.write(file.getBytes());
            os.close();
        }
        
        // Instantiate the Factory
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

        try {
            // parse XML file
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(xmlFile);
            doc.getDocumentElement().normalize();
            
            NodeList listE = doc.getChildNodes(); 
            analyzeE43Xml(listE);

        } catch ( IOException e) {
                e.printStackTrace();
            }
            
    System.out.println("Process E43 XML terminé avec succès");

    }

 
    public String getPageNumber (String pgNum) {
        String pg = pgNum;

        if (pg.substring(0, 1).equals("P")) pg = pg.substring(1, pg.length());
        if (pg.substring(0, 1).equals("0")) pg = pg.substring(1, pg.length());
        if (pg.substring(0, 1).equals("0")) pg = pg.substring(1, pg.length()); //deuxième 0 si existe
        
        return "_" + pg;
    }

    public void analyzeE43Xml(NodeList nodeList) {
        SpPage pg = null;

        for (int i = 0; i < nodeList.getLength(); i++) {

            if (nodeList.item(i).getNodeName().equals("items")) {
                //détecte le noeud items - page

                String pageNumber, page, reference, revision, localDescription, englishDescription;
                pageNumber = page = reference = revision = localDescription = englishDescription = "";                
                
                for (int j = 0; j < nodeList.item(i).getAttributes().getLength(); j++ ) {
                
                    String attrValue = nodeList.item(i).getAttributes().item(j).getNodeValue();
                    String attrName = nodeList.item(i).getAttributes().item(j).getNodeName();
                    
                    if (attrName == "PageNumber") {pageNumber = attrValue;}  
                    if (attrName == "Page") {page = attrValue;}
                    if (attrName == "Reference") {reference = attrValue;}
                    if (attrName == "Revision") {revision = attrValue;}
                    if (attrName == "LocalDescription") {localDescription = attrValue;}
                    if (attrName == "EnglishDescription") {englishDescription = attrValue;}

                }

                //créer un objet sp_page
                this.pg = pageService.createPage(this.docMeta.getIdDoc(),this.docMeta.getIdDoc()+getPageNumber(pageNumber));


            } //end noeud items - page

            if (nodeList.item(i).getNodeName().equals("item")) {
                //détecte le noeud item
                String repere, criticality, preconisationCode, itemMaterialGroup, itemNumber, itemRevision, itemState, descDe, descEn, descEs, descFr,descIt, descPt;
                repere = criticality = preconisationCode = itemMaterialGroup = itemNumber = itemRevision = itemState = descDe = descEn = descEs = descFr = descIt = descPt = "";


                NodeList childsItem = nodeList.item(i).getChildNodes();
                for (int j= 0; j < childsItem.getLength(); j++) {
                    Node itemAttrNode = childsItem.item(j);
                    if ( (itemAttrNode.getNodeType() == Node.ELEMENT_NODE) &
                         (itemAttrNode.hasChildNodes()) ) {

                            if (!itemAttrNode.getFirstChild().getNodeValue().isBlank()) {

                                if (itemAttrNode.getNodeName().equals("BomId")) { repere = itemAttrNode.getFirstChild().getNodeValue(); }
                                if (itemAttrNode.getNodeName().equals("Criticality")) { criticality = itemAttrNode.getFirstChild().getNodeValue(); }
                                if (itemAttrNode.getNodeName().equals("ItemMaterialFroup")) { itemMaterialGroup = itemAttrNode.getFirstChild().getNodeValue(); }
                                if (itemAttrNode.getNodeName().equals("ItemNumber")) { itemNumber = itemAttrNode.getFirstChild().getNodeValue(); }
                                if (itemAttrNode.getNodeName().equals("ItemRevision")) { itemRevision = itemAttrNode.getFirstChild().getNodeValue(); }
                                if (itemAttrNode.getNodeName().equals("ItemState")) { itemState = itemAttrNode.getFirstChild().getNodeValue(); }
                                if (itemAttrNode.getNodeName().equals("ShortDescriptionDe")) { descDe = itemAttrNode.getFirstChild().getNodeValue(); }
                                if (itemAttrNode.getNodeName().equals("ShortDescriptionEn")) { descEn = itemAttrNode.getFirstChild().getNodeValue(); }
                                if (itemAttrNode.getNodeName().equals("ShortDescriptionEs")) { descEs = itemAttrNode.getFirstChild().getNodeValue();}
                                if (itemAttrNode.getNodeName().equals("ShortDescriptionFr")) { descFr = itemAttrNode.getFirstChild().getNodeValue(); }
                                if (itemAttrNode.getNodeName().equals("ShortDescriptionIt")) { descIt = itemAttrNode.getFirstChild().getNodeValue(); }
                                if (itemAttrNode.getNodeName().equals("ShortDescriptionPt")) { descPt = itemAttrNode.getFirstChild().getNodeValue(); }
                                if (itemAttrNode.getNodeName().equals("PreconisationCode")) { preconisationCode = itemAttrNode.getFirstChild().getNodeValue(); }

                            }


                         }
                    
                }

                //Créer ou compléter un objet item
                SpItem itemP = itemService.createItem(itemNumber,descFr,descEn,descDe);
                itemP.setCriticality(criticality);
                itemP.setExternMatGroup(itemMaterialGroup);
                itemP.setRevision(itemRevision);
                itemP.setState(itemState);
                itemP.setPreconisationCode(preconisationCode);
                itemP.setDescriptionEs(descEs);
                itemP.setDescriptionIt(descIt);
                itemP.setDescriptionPt(descPt);
                spItemRepository.save(itemP);

                //Créer ou compléter un objet pageContent , contentType = I pour Item ou L pour Link
                SpPageContent pgContent = pageContentService.createPageContent(this.pg.getIdPage(), itemP.getIdItem(), repere, "I"); 
                pgContent.setValidity(itemRevision);
                pgContent.setRemarque(itemState);
                spPageContentRepository.save(pgContent);

                //Créer ou compléter un objet  docBom -> modifier le repère
                //docBomService.createDocBom(descEn, descEs, itemState, false, null, descFr, descIt, repere, descPt)


            } //end noeud items

                //     <link>
                //     <BomId>1</BomId>
                //     <Id>BSA00340000BP</Id>
                //     <Coordinates xMin="34.24" yMin="94.77" xMax="56.07" yMax="96.98" attachPointPosition="2" />
                //   </link>

            if (nodeList.item(i).getNodeName().equals("link")) {
                //détecte le noeud link
                String repere, id;
                repere = id = "";
                Double xMin, xMax, yMin, yMax;
                Integer attachPointPosition = 1;
                xMin=xMax=yMin=yMax=0.0;


                NodeList childsItem = nodeList.item(i).getChildNodes();
                for (int j= 0; j < childsItem.getLength(); j++) {
                    Node itemAttrNode = childsItem.item(j);
                    
                    if ( itemAttrNode.getNodeType() == Node.ELEMENT_NODE ) {

                            if (itemAttrNode.getNodeName().equals("BomId")) { repere = itemAttrNode.getFirstChild().getNodeValue(); }
                            if (itemAttrNode.getNodeName().equals("Id")) { id = itemAttrNode.getFirstChild().getNodeValue(); }
                            if (itemAttrNode.getNodeName().equals("Coordinates")) {
                                //Extract coordinates
                                if (itemAttrNode.hasAttributes()) {
                                    Element element = (Element) itemAttrNode;
                                    xMin = Double.valueOf(element.getAttribute("xMin"));
                                    xMax = Double.valueOf(element.getAttribute("xMax"));
                                    yMin = Double.valueOf(element.getAttribute("yMin"));
                                    yMax = Double.valueOf(element.getAttribute("yMax"));
                                    attachPointPosition = Integer.valueOf(element.getAttribute("attachPointPosition"));
                                } 
                            }

                         }
                    
                }

                //Récuppérer idItem
                SpItem itemP = itemService.createItem(id,"", "", "");


                //Créer ou compléter un objet pageContent , contentType = I pour Item ou L pour Link
                SpPageContent pgContent = pageContentService.createPageContent(this.pg.getIdPage(), itemP.getIdItem(), id, "L"); 
                pgContent.setX1(xMin);
                pgContent.setX2(xMax);
                pgContent.setY1(yMin);
                pgContent.setY2(yMax);
                pgContent.setAttachPointPosition(attachPointPosition);
                spPageContentRepository.save(pgContent);

            } //end noeud link




            
            if (nodeList.item(i).hasChildNodes()) {
                //this.itemParent = this.item;
                analyzeE43Xml(nodeList.item(i).getChildNodes());
            }

        }      

          
    }


    public void uploadXml(MultipartFile file) throws ParserConfigurationException, SAXException, IllegalStateException, IOException {

        //String idD43 = file.getOriginalFilename().substring(0,file.getOriginalFilename().lastIndexOf("."));
        File xmlFile = new File("src/main/resources/" + file.getOriginalFilename());

        try (OutputStream os = new FileOutputStream(xmlFile)) {
            os.write(file.getBytes());
            os.close();
        }
        
        // Instantiate the Factory
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

        try {
            // parse XML file
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(xmlFile);
            doc.getDocumentElement().normalize();

            //get <doc>
            NodeList list = doc.getElementsByTagName("doc");
            Node node = list.item(0);
                          
            


            if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    // get doc's attributes
                    String drawing = element.getAttribute("drawing");
                    System.out.println( "drawing = " + drawing);
                    //String cmd = element.getAttribute("cmd");
                    String numeclatement = element.getAttribute("numeclatement");  //21.06.2021  dd.mm.yyyy
                    String numequipement = element.getAttribute("numequipement");
                    String desequipement = element.getAttribute("desequipement");
                    String numdocD43 = element.getAttribute("numdoc");
                    String desfr = element.getAttribute("desfr");
                    String desan = element.getAttribute("desAN");

                    //Traitement SpDocMeta - Test si le document existe déja ZNC_BSA03802000066_D43_017_-
                    String[] tabRef = numdocD43.split("_");
                    //Création d'un objet docMeta pour le document D87
                    //SpDocMeta docMeta;
                    List<SpDocMeta> docMetaList = spDocMetaRepository.findByrefDocTypeAndRefDocAndRefDocPartAndRefDocVersion(tabRef[0], tabRef[1] + "_D87", tabRef[3], tabRef[4]);
                    if (docMetaList.size()>0) {
                        this.docMeta = docMetaList.get(0);
                        System.out.println("D87 existe déjà");
                    } else {
                        this.docMeta = new SpDocMeta(tabRef[0], tabRef[1] + "_D87", tabRef[3], tabRef[4]);
                        //docMeta.setLangCode(null);
                        this.docMeta.setInitialCode("D87");
                        this.docMeta.setValidityStart(numeclatement);
                        this.docMeta.setTitle(desequipement);
                        this.docMeta.setSub_title("");
                        this.docMeta.setCompl_info(numequipement);                        
                        spDocMetaRepository.save(this.docMeta);
                    }
                    
                    
                    String[] tabDrawing = drawing.split("_");
                    //création d'un objet docMeta pour le document E43 vue générale
                    SpDocMeta docMetaE;
                    List<SpDocMeta> docMetaEList = spDocMetaRepository.findByrefDocTypeAndRefDocAndRefDocPartAndRefDocVersion(tabDrawing[0], tabDrawing[1] + "_E43", tabDrawing[3], tabDrawing[4]);
                    if (docMetaEList.size()>0) {
                        docMetaE = docMetaEList.get(0);
                        System.out.println("E43 existe déjà");
                    } else {
                        docMetaE= new SpDocMeta(tabDrawing[0], tabDrawing[1] + "_E43", tabDrawing[3], tabDrawing[4]);
                        docMetaE.setInitialCode(tabDrawing[2]);
                        spDocMetaRepository.save(docMetaE);
                    }
                    
                    //Création d'un objet item pour supporter les désignations / traductions de la vue générale
                    //SpItem item;
                    List<SpItem> itemList = spItemRepository.findByItemNameAndDescriptionEn(tabDrawing[1], desan); 
                    if (itemList.size()>0) {
                        this.item = itemList.get(0);
                        System.out.println("Item existe déjà plus");
                    } else {
                        this.item = new SpItem(tabDrawing[1], desfr, desan );
                        spItemRepository.save(this.item);
                    }
                

                    //Création d'un objet docBom pour référencer l'item qui supporte le drawing vue générale du document
                    //SpDocBom docBom = new SpDocBom(numdocD43, desfr, desan, false, null);
                    SpDocBom docBom;
                    List<SpDocBom> docBomList = spDocBomRepository.findByIdDocAndIdItemParentAndIdItemAndItemOrder(this.docMeta.getIdDoc(), "", this.item.getIdItem(), 0);
                    if (docBomList.size()>0) {
                        docBom = docBomList.get(0);
                        System.out.println("Bom vue générale existe déjà");
                    }  else {
                        docBom = new SpDocBom(this.docMeta.getIdDoc(), "",this.item.getIdItem(), true, 0, "","1","0", numeclatement );
                        System.out.println("save docbom");
                        spDocBomRepository.save(docBom);
                    }
                    this.itemParent = this.item;

                    //Création d'un objet LinkItemDrawing pour lier le document E43 au dessin vue générale
                    SpLinkItemDrawing linkItemDrawing;
                    List<SpLinkItemDrawing> linkItemDrawingList = spLinkItemDrawingRepository.findByIdItemAndIdDrawing(item.getIdItem(),docMetaE.getIdDoc());
                    if (linkItemDrawingList.size()>0) {
                        linkItemDrawing = linkItemDrawingList.get(0);
                        System.out.println("Lien E43 - item existe déjà");
                    } else {
                        linkItemDrawing = new SpLinkItemDrawing(item.getIdItem(),docMetaE.getIdDoc());
                        spLinkItemDrawingRepository.save(linkItemDrawing);    
                        System.out.println("Lien E43 -item en création");
                    }



                    }

            
            NodeList listE = doc.getChildNodes(); 
            String pathParent="1";

            //donne les enfants de la première balise , en général doc
            analyzeXml(listE.item(0).getChildNodes(), pathParent);
           

            if (xmlFile.exists())
                xmlFile.delete();

        } catch ( IOException e) {
            e.printStackTrace();
        }
    }


    public void analyzeXml(NodeList nodeList, String pathParent) {

        int level=0;
        

       
        for (int i = 0; i < nodeList.getLength(); i++) {
            
            if (nodeList.item(i).hasAttributes()) {
                
                level++;

                String itemName = null;
                String desFr = null;
                String desEn = null;
                String drawing = "";
                String validity = null;
                String rep = null;


                for (int j = 0; j < nodeList.item(i).getAttributes().getLength(); j++ ) {
                
                    String attrValue = nodeList.item(i).getAttributes().item(j).getNodeValue();
                    String attrName = nodeList.item(i).getAttributes().item(j).getNodeName();
                    
                    if (attrName == "numBobst") {itemName = attrValue;}  
                    if (attrName == "desfr") {desFr = attrValue;}
                    if (attrName == "desan") {desEn = attrValue;}
                    if (attrName == "drawing") {drawing = attrValue;}
                    if (attrName == "val") {validity = attrValue;}
                    if (attrName == "noeud") {rep = attrValue;}

                }
                
                

                //créer un objet item pour chaque élement si n'existe pas déjà - mémoriser iten et item parent en fonction de la position dans l'abre récursif
                List<SpItem> itemList = spItemRepository.findByItemNameAndDescriptionEn(itemName, desEn); 
                if (itemList.size()>0) {
                    this.item = itemList.get(0);
                    //System.out.println("Item existe déjà");
                } else {
                    this.item = new SpItem(itemName, desFr, desEn );
                    spItemRepository.save(item);
                }

                
                
                //Créer un objet docBom pour chaque élement si n'existe pas déjà - attention récuppérer l'idDoc
                SpDocBom docBom;
                this.cptItem++;
                path = pathParent + "." + getLevel(level);
                
                List<SpDocBom> docBomList = spDocBomRepository.findByIdDocAndIdItemParentAndIdItemAndItemOrder(this.docMeta.getIdDoc(), this.itemParent.getIdItem(), this.item.getIdItem(), this.cptItem);
                if (docBomList.size()>0) {
                    docBom = docBomList.get(0);                    
                    System.err.println(this.cptItem + " - Rec - " + this.item.getItemName());
                }  else {
                    docBom = new SpDocBom(docMeta.getIdDoc(), this.itemParent.getIdItem(), this.item.getIdItem(), true, this.cptItem, pathParent, path, rep, validity);
                    spDocBomRepository.save(docBom);
                    //System.err.println(this.cptItem + " - New - " + this.item.getItemName());
                }

                              
                
                
                if (!(drawing.isBlank())) {
                    //créer un objet doc pour le drawing
                    String[] tabDrawing = drawing.split("_");
                    SpDocMeta docMetaE;
                    List<SpDocMeta> docMetaEList = spDocMetaRepository.findByrefDocTypeAndRefDocAndRefDocPartAndRefDocVersion(tabDrawing[0], tabDrawing[1] + "_" + tabDrawing[2] , tabDrawing[3], tabDrawing[4]);
                    if (docMetaEList.size()>0) {
                        docMetaE = docMetaEList.get(0);
                        System.out.println("E43 existe déjà");
                    } else {
                        docMetaE= new SpDocMeta(tabDrawing[0], tabDrawing[1] + "_" + tabDrawing[2] , tabDrawing[3], tabDrawing[4]);
                        docMetaE.setInitialCode(tabDrawing[2]);
                        spDocMetaRepository.save(docMetaE);
                    }

                    //créer un objet link item drawing si drawing exist
                    SpLinkItemDrawing linkItemDrawing;
                    List<SpLinkItemDrawing> linkItemDrawingList = spLinkItemDrawingRepository.findByIdItemAndIdDrawing(item.getIdItem(),docMetaE.getIdDoc());
                    if (linkItemDrawingList.size()>0) {
                        linkItemDrawing = linkItemDrawingList.get(0);
                        System.out.println("Lien E43 - item existe déjà");
                    } else {
                        linkItemDrawing = new SpLinkItemDrawing(item.getIdItem(),docMetaE.getIdDoc());
                        spLinkItemDrawingRepository.save(linkItemDrawing);    
                        System.out.println("Lien E43 -item en création");
                    }


                }

                

            }

            if (nodeList.item(i).hasChildNodes()) {
                this.itemParent = this.item;
                analyzeXml(nodeList.item(i).getChildNodes(),path);
            }
        }
        
    }

//----------------------------------------------------------------------------------------------------------------------------------------
   
   public String getLevel(int level) {
    String lvl = Integer.toString(level);
    if (lvl.length() == 1) lvl="00" + lvl;
    if (lvl.length() == 2) lvl="0" + lvl;
    return lvl;
   } 
//----------------------------------------------------------------------------------------------------------------------------------------
   
   public void uploadPVXml (MultipartFile file) throws ParserConfigurationException, SAXException, IllegalStateException, IOException {

    //créer l'objet docMeta pour le document D87
    String fName = file.getOriginalFilename().substring(0,file.getOriginalFilename().lastIndexOf("."));
    String[] tab = fName.split("_");
    this.docMeta = docMetaService.createDocMeta(tab[0], tab[1]+"_"+tab[2], tab[3], tab[4], "", tab[2], "", "", "", "", "", "");
    
    //créer un objet item
    this.item = itemService.createItem(tab[1],"LOCALISATION DES PARTIES","LOCATION OF SECTIONS","");
    this.itemM = this.item;

    //créer un objet docBom
    docBomService.createDocBom(this.docMeta.getIdDoc(), "", this.item.getIdItem(), true, this.cptItem, "", "1", "0", "");
    String levelParent = "1";

    //créer un objet doc drawing pour page 0
    SpDocMeta docMetaD;
    docMetaD = docMetaService.createDocMeta("", docMetaService.getNormalizedDocName(this.docMeta) + "_" + "0", 
                                "", "", "", "", "", "",
                                "", "", "", "");

    //créer un objet link drawing item si numpage
    linkItemDrawingService.createLinkItemDrawing(this.item.getIdItem(), docMetaD.getIdDoc());

    //créer un objet page 
    SpPage pg0;
    pg0 = pageService.createPage(docMetaD.getIdDoc(), docMetaD.getIdDoc()+"_1"); // _1 permet d'identifier la première page du dessin

    //créer un objet page_content
    pageContentService.createPageContent(pg0.getIdPage(), this.item.getIdItem(), "0", "I");
    
    
    File xmlFile = new File("src/main/resources/" + file.getOriginalFilename());


     try (OutputStream os = new FileOutputStream(xmlFile)) {
         os.write(file.getBytes());
         os.close();
     }
     
     // Instantiate the Factory
     DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

     try {
         // parse XML file
         DocumentBuilder db = dbf.newDocumentBuilder();
         Document doc = db.parse(xmlFile);
         doc.getDocumentElement().normalize();
         
         NodeList listE = doc.getChildNodes(); 
         analyzePVXml(listE,levelParent);

     } catch ( IOException e) {
            e.printStackTrace();
        }
           
   System.out.println("Process PV XML terminé avec succès");

   } //end of uploadPVxml

//----------------------------------------------------------------------------------------------------------------------------------------

   public String[] getAttributesPV(Node node) {

    //0=numpage,1=ident,2=desFr,3=desEn,4=desDe
    String[] tabAttr = {"","","","",""};
    for (int j = 0; j < node.getAttributes().getLength(); j++ ) {
                    
        String attrValue = node.getAttributes().item(j).getNodeValue();
        String attrName = node.getAttributes().item(j).getNodeName();
        
        if (attrName == "numpage") {tabAttr[0] = attrValue;}  
        if (attrName == "ident") {tabAttr[1] = attrValue;}
        if (attrName == "desfr") {tabAttr[2] = attrValue;}
        if (attrName == "desan") {tabAttr[3] = attrValue;}
        if (attrName == "desal") {tabAttr[4] = attrValue;}

    }

    return tabAttr;

   } 

//----------------------------------------------------------------------------------------------------------------------------------------

   



//----------------------------------------------------------------------------------------------------------------------------------------
   public void analyzePVXml(NodeList nodeList, String pathParent) {

    int level = 0;
    path = pathParent;

    for (int i = 0; i < nodeList.getLength(); i++) {

        Node node = nodeList.item(i);

        //test si Element
        if (node.getNodeType() == Node.ELEMENT_NODE) {

            if (node.getNodeName().equals("partie_machine")) {
                //gestion de l'élement partie_machine
                if (node.hasAttributes()) {
                    
                    //0=numpage,1=ident,2=desFr,3=desEn,4=desDe
                    String[] tabAttr = getAttributesPV(node);

                    if (!tabAttr[1].equals("")) {
                        //traitement uniquement si ident existe
                        level++;

                        //créer un objet item
                        this.item = itemService.createItem(tabAttr[1], tabAttr[2], tabAttr[3], tabAttr[4]);
                        this.itemPM = this.item;
    
                        //créer un objet docBom
                        this.cptItem++;
                        path = pathParent + "." + getLevel(level);
                        SpDocBom docBom = docBomService.createDocBom(this.docMeta.getIdDoc(), this.itemM.getIdItem(), this.item.getIdItem(), true, this.cptItem,pathParent,path, tabAttr[1], "");

                        String numPage = tabAttr[0];
                        if (!numPage.equals("")) {

                            //créer un objet doc drawing si numpage
                            SpDocMeta docMetaD;
                            docMetaD = docMetaService.createDocMeta("", docMetaService.getNormalizedDocName(this.docMeta) + "_" + numPage, 
                                                        "", "", "", "", "", "",
                                                        "", "", "", "");

                            //créer un objet link drawing item si numpage
                            //linkItemDrawingService.createLinkItemDrawing(this.item.getIdItem(), docMetaD.getIdDoc());
                            //add drawing on doc_bom
                            docBom.setIdDrawing(docMetaD.getIdDoc());
                            spDocBomRepository.save(docBom);

                            //créer un objet page
                            SpPage pg1;
                            pg1 = pageService.createPage(docMetaD.getIdDoc(), docMetaD.getIdDoc()+"_1");

                            //créer un objet page contentM
                            pageContentService.createPageContent(pg1.getIdPage(), this.item.getIdItem(), tabAttr[1], "I");
                            
                        }
                    }

                }

            }



            if (node.getNodeName().equals("page")) {
                //gestion de l'élement page
                if (node.hasAttributes()) {
                    //0=numpage,1=ident,2=desFr,3=desEn,4=desDe
                    String[] tabAttr = getAttributesPV(node);
                    identPage = tabAttr[1];

                    
                    if (!tabAttr[1].equals("")) {
                        //traitement uniquement si ident existe
                        
                        //identification du Goc représenté sur la page
                        if (identPage.contains("-")) {
                            identGoc = identPage.substring(0,identPage.lastIndexOf("-"));
                        } else {
                            identGoc = identPage; //traitment ancien PV
                        }
                                            
                        level++;
  
                        //créer un objet item
                        this.item = itemService.createItem(tabAttr[1], tabAttr[2], tabAttr[3], tabAttr[4]);
                        //this.item = itemService.createItem(identGoc, tabAttr[2], tabAttr[3], tabAttr[4]);
                        this.itemP = this.item;

                        //créer un objet docBom
                        this.cptItem++;
                        path = pathParent + "." + getLevel(level);
                        SpDocBom docBom = docBomService.createDocBom(this.docMeta.getIdDoc(), this.itemPM.getIdItem(), this.item.getIdItem(), true, this.cptItem,pathParent, path, tabAttr[1], "");
                        //SpDocBom docBom = docBomService.createDocBom(this.docMeta.getIdDoc(), this.itemPM.getIdItem(), this.item.getIdItem(), true, this.cptItem,pathParent, path, identGoc, "");

                        String numPage = tabAttr[0];
                        if (!numPage.equals("")) {

                            //créer un objet doc drawing si numpage
                            this.docMetaD = docMetaService.createDocMeta("", docMetaService.getNormalizedDocName(this.docMeta) + "_" + numPage, 
                                                        "", "", "", "", "", "",
                                                        "", "", "", "");

                            //créer un objet link drawing item si numpage
                            //linkItemDrawingService.createLinkItemDrawing(this.item.getIdItem(), this.docMetaD.getIdDoc());
                            //add drawing to docBom
                            docBom.setIdDrawing(this.docMetaD.getIdDoc());
                            spDocBomRepository.save(docBom);

                             //créer un objet page
                            SpPage pg1;
                            pg1 = pageService.createPage(this.docMetaD.getIdDoc(), this.docMetaD.getIdDoc()+"_1");

                            //créer un objet page content
                            pageContentService.createPageContent(pg1.getIdPage(), this.item.getIdItem(), tabAttr[1], "I");
                            
                        }
                    }

                }

            }


            if (node.getNodeName().equals("piece")) {
                //gestion de l'élement piece
                level++;

                String id,numident,numbobst,desfr,desan,desal,val,remarque,goc;
                id=numident=numbobst=desfr=desan=desal=val=remarque=goc="";
                NodeList pieceAttrList = node.getChildNodes();

                for (int j=0;j<pieceAttrList.getLength();j++) {
                    Node pieceAttrNode = pieceAttrList.item(j);
                    if ( (pieceAttrNode.getNodeType() == Node.ELEMENT_NODE) &
                         (pieceAttrNode.hasChildNodes()) ) {

                            if (!pieceAttrNode.getFirstChild().getNodeValue().isBlank())  {

                                if(pieceAttrNode.getNodeName().equals("id")) {
                                    id=pieceAttrNode.getFirstChild().getNodeValue();
                                    //l' id est identifié par la page + le repère 
                                    //if (identPage.contains("-")) id = identPage.substring(identPage.lastIndexOf("-"), identPage.length()) + "-" + id;
                                }
                                if(pieceAttrNode.getNodeName().equals("numident")) {numident=pieceAttrNode.getFirstChild().getNodeValue();}
                                if(pieceAttrNode.getNodeName().equals("numbobst")) {
                                    numbobst=pieceAttrNode.getFirstChild().getNodeValue();
                                    if (!numbobst.startsWith("BSA")) {
                                        numbobst = "BSA" + numbobst.replaceAll(" ", "");
                                    }
                                }
                                if(pieceAttrNode.getNodeName().equals("desfr")) {desfr=pieceAttrNode.getFirstChild().getNodeValue();}
                                if(pieceAttrNode.getNodeName().equals("desan")) {desan=pieceAttrNode.getFirstChild().getNodeValue();}
                                if(pieceAttrNode.getNodeName().equals("desal")) {desal=pieceAttrNode.getFirstChild().getNodeValue();}
                                if(pieceAttrNode.getNodeName().equals("val")) {val=pieceAttrNode.getFirstChild().getNodeValue();}
                                if(pieceAttrNode.getNodeName().equals("remarque")) {remarque=pieceAttrNode.getFirstChild().getNodeValue();}
                                if(pieceAttrNode.getNodeName().equals("GOC")) {goc=pieceAttrNode.getFirstChild().getNodeValue();}

                            }
                    
                    }
                }

                //créer un objet item pour la pièce
                SpItem itemp = itemService.createItem(numbobst,desfr,desan,desal);
    
                //créer un objet docBom
                this.cptItem++;
                path = pathParent + "." + getLevel(level);
                docBomService.createDocBom(this.docMeta.getIdDoc(), this.itemP.getIdItem(), itemp.getIdItem(), true, this.cptItem,pathParent, path, id, val);

                 //créer un objet page
                SpPage pg1;
                pg1 = pageService.createPage(this.docMetaD.getIdDoc(), this.docMetaD.getIdDoc()+"_1");

                //créer un objet page content
                SpPageContent pgc = pageContentService.createPageContent(pg1.getIdPage(), itemp.getIdItem(), id, "I");
                pgc.setValidity(val);
                pgc.setRemarque(remarque);
                spPageContentRepository.save(pgc);
            

                             
            }


                        
        }
                
        if (nodeList.item(i).hasChildNodes()) {
                analyzePVXml(nodeList.item(i).getChildNodes(), path);
        } 
         
    }

   }

//----------------------------------------------------------------------------------------------------------------------------------------

    public void getBomStructure(String idDoc) {

           List<SpDocBom> docBomList = spDocBomRepository.findByIdDoc(idDoc);

           for (SpDocBom spDocBom : docBomList) {
            
            if (!( spDocBom.getIdItemParent().isEmpty() )) {
                System.out.println(spDocBom.getItemOrder() + " : " + spItemRepository.findByIdItem(spDocBom.getIdItemParent()).getItemName() + "  " + spItemRepository.findByIdItem(spDocBom.getIdItem()).getItemName());
            } else {
                System.out.println(spDocBom.getItemOrder() + " : " + "  " + "  " + spItemRepository.findByIdItem(spDocBom.getIdItem()).getItemName());
            }
            
           }
           

    }



    
}
