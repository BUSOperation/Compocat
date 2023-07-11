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
import bobst.sp.compocat.repositories.SpDocBomRepository;
import bobst.sp.compocat.repositories.SpDocMetaRepository;
import bobst.sp.compocat.repositories.SpItemRepository;
import bobst.sp.compocat.repositories.SpLinkItemDrawingRepository;

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

    private int cptItem = 0;
    private SpItem item;
    private SpItem itemParent;
    private SpDocMeta docMeta;

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
                        docBom = new SpDocBom(this.docMeta.getIdDoc(), "",this.item.getIdItem(), true, 0 );
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
            analyzeXml(listE);
           

            if (xmlFile.exists())
                xmlFile.delete();

        } catch ( IOException e) {
            e.printStackTrace();
        }
    }


    public void analyzeXml(NodeList nodeList) {

       
        for (int i = 0; i < nodeList.getLength(); i++) {
            
            if (nodeList.item(i).hasAttributes()) {
                
                String itemName = null;
                String desFr = null;
                String desEn = null;
                String drawing = "";
                String validity = null;


                for (int j = 0; j < nodeList.item(i).getAttributes().getLength(); j++ ) {
                
                    String attrValue = nodeList.item(i).getAttributes().item(j).getNodeValue();
                    String attrName = nodeList.item(i).getAttributes().item(j).getNodeName();
                    
                    if (attrName == "numBobst") {itemName = attrValue;}  
                    if (attrName == "desfr") {desFr = attrValue;}
                    if (attrName == "desan") {desEn = attrValue;}
                    if (attrName == "drawing") {drawing = attrValue;}
                    if (attrName == "val") {validity = attrValue;}

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
                List<SpDocBom> docBomList = spDocBomRepository.findByIdDocAndIdItemParentAndIdItemAndItemOrder(this.docMeta.getIdDoc(), this.itemParent.getIdItem(), this.item.getIdItem(), this.cptItem);
                if (docBomList.size()>0) {
                    docBom = docBomList.get(0);                    
                    System.err.println(this.cptItem + " - Rec - " + this.item.getItemName());
                }  else {
                    docBom = new SpDocBom(docMeta.getIdDoc(), this.itemParent.getIdItem(), this.item.getIdItem(), true, this.cptItem);
                    spDocBomRepository.save(docBom);
                    System.err.println(this.cptItem + " - New - " + this.item.getItemName());
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
                analyzeXml(nodeList.item(i).getChildNodes());
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
