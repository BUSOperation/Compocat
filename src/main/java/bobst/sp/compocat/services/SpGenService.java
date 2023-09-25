package bobst.sp.compocat.services;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import bobst.sp.compocat.models.SpDocBom;
import bobst.sp.compocat.models.SpDocMeta;
import bobst.sp.compocat.models.SpItem;
import bobst.sp.compocat.models.SpLinkItemDrawing;

@Service
public class SpGenService {

    @Autowired
    SpDocMetaService spDocMetaService;

    @Autowired
    SpItemService spItemService;

    @Autowired 
    SpDocBomService spDocBomService;

    @Autowired
    SpLinkItemDrawingService spLinkItemDrawingService;

    @Autowired
    SpPageService spPageService;

    @Autowired
    SpPageContentService spPageContentService;


    private String getCsvCol(String col,String[] tabLine, String[] formatage){

        String res = "";
      
      try {
        //cherche colonne
        for (int i=0; i<formatage.length; i++) {

            if (!formatage[i].isBlank() & formatage[i].equals(col)) {
                res = tabLine[i];
            }
        }

        } catch (Exception e)  {
            //e.printStackTrace();
        }

        
        return res;
    }


    public String getLevel(int level) {
        String lvl = Integer.toString(level);
        if (lvl.length() == 1) lvl="00" + lvl;
        if (lvl.length() == 2) lvl="0" + lvl;
        return lvl;
       } 

    public void uploadCsv(MultipartFile file) throws FileNotFoundException, IOException {

        File csvFile = new File("src/main/resources/" + file.getOriginalFilename());

        //create id_doc avec le nom du fichier quand il sera normé
        SpDocMeta docP = spDocMetaService.createDocMeta("ZNC", "GENV21G026_D87", "052", "-", "", "D87", "", "", "", "", "", "");

        //create itemParent pour Localisation des parties
        SpItem itemP = spItemService.createItem("GENV21G026", "LOCALISATION DES PARTIES", "LOCATION OF SECTIONS", "");

        try (OutputStream os = new FileOutputStream(csvFile)) {
            os.write(file.getBytes());
            os.close();
        }

        Path pathToFile =  Paths.get(csvFile.getAbsolutePath());       

        // create an instance of BufferedReader 
        BufferedReader br = Files.newBufferedReader(pathToFile, StandardCharsets.UTF_8);

        String[] formatage = new String[30];
        Boolean start = false;
        
        // read the first line from the csv file 
        String line = br.readLine(); 
        


        String section, actualSection, actualSequence, sequence, fullIndex, itemCode, itemDescription, drawing, drawingVersion; 
        section=sequence=fullIndex=itemCode=itemDescription=drawing=drawingVersion="";

        int levelSequence, levelSection, levelParts, itemOrder;
        levelSection=levelSequence=levelParts=itemOrder=0;

        String lvlSequence, lvlSection, lvlParts;
        lvlSequence=lvlSection=lvlParts="";

        SpItem itemSection, itemSequence, itemParts;
        itemSection=itemSequence=itemParts=null;

        // loop until all lines are read 
        while (line != null) {

            SpItem currentItem;
            
            System.out.println(line);
            String[] tabLine = line.split(";");
            
            //identification du formatage / des colonnes du csv
            if(tabLine[0].contains("Section") & !start){
                for(int i=0; i<tabLine.length; i++) {
                    formatage[i] = tabLine[i];
                }
                start = true;
            } else {

            //traitement des lignes
            //Section;Sequence;Full Index;Item Code;Item Revision;Item Description;Quantity;Master PDO;CO;FMO;Action;Comment;Whse;Issue Type;Legacy PO;SAP PO;SubCon PO;Master PDO Status;Assembly Drwg;Assembly Drwg Rev
            // 01 - Vessel External;;01:080;903G581;2;Chuck port blanking flanges;1;6973;1111;0;Procure New;Quantity Increased;Pennine;FF;;;;In Assembly;;
            // 01 - Vessel External;01:010 - Vessel Assembly with Roof Tracks K5-Expert;01:010;910G556-1650;0;Vessel Assembly with Roof Tracks K5-Expert;1;6973;0;0;;;Pennine;FF;;;;In Assembly;435G294;
            // 01 - Vessel External;01:010 - Vessel Assembly with Roof Tracks K5-Expert;01:010:M002;419G478-1650;2;Roof Track Beam;1;7566;0;0;;;Pennine;FF;;3742;;Built;435G150;
            // 01 - Vessel External;01:010 - Vessel Assembly with Roof Tracks K5-Expert;01:010:M003;419G479-1650;1;Roof Track Beam;1;7566;0;0;;;Pennine;FF;;3742;;Built;435G150;
            // 01 - Vessel External;01:010 - Vessel Assembly with Roof Tracks K5-Expert;01:010:M004;402G660;2;Roof Track End Plate;1;7566;0;0;;;Pennine;FF;;3742;;Built;435G150;
            // 01 - Vessel External;01:010 - Vessel Assembly with Roof Tracks K5-Expert;01:010:M005;403G770;0;Runway Beam Packer;12;7566;0;0;;;Pennine;FF;;3742;;Built;435G150;
            /*
             * Section permet de créer un item 
             * 
             * 
             * 
             */
            
            itemCode = getCsvCol("Item Code",tabLine,formatage);
            itemDescription = getCsvCol("Item Description",tabLine,formatage);
            drawing = getCsvCol("Assembly Drwg",tabLine,formatage);
            drawingVersion = getCsvCol("Assembly Drwg Rev",tabLine,formatage);
            itemOrder++;
            
            
            actualSection = getCsvCol("Section", tabLine, formatage);
            if(!actualSection.equals(section)) {
                //manage new section
                levelSection++;
                lvlSection = "1." + getLevel(levelSection);

                //create item for section
                itemSection = spItemService.createItem(actualSection,"",actualSection,"");

                //create bom 
                SpDocBom bomSection = spDocBomService.createDocBom(docP.getIdDoc(),itemP.getIdItem(),itemSection.getIdItem(),true,itemOrder,"1",lvlSection,"","");
                
                //create id_drawing
                if (!drawing.isEmpty()) {
                    SpDocMeta docDrawing = spDocMetaService.createDocMeta("", drawing, "", drawingVersion, "", "","","","","","","");
                    SpLinkItemDrawing linkItem = spLinkItemDrawingService.createLinkItemDrawing(itemSection.getIdItem(), docDrawing.getIdDoc());               
                }

                section = actualSection;
            }


            actualSequence = getCsvCol("Sequence", tabLine, formatage);
            if(!actualSequence.equals(sequence)) {
                //manage new sequence
                levelSequence++;
                lvlSequence = lvlSection + "." + getLevel(levelSequence);

                //create item for sequence
                itemSequence = spItemService.createItem(itemCode,"",actualSequence,"");

                //create bom
                SpDocBom bomSequence = spDocBomService.createDocBom(docP.getIdDoc(), itemSection.getIdItem(), itemSequence.getIdItem(), true, itemOrder, lvlSection, lvlSequence, "", "");

                //create id_drawing
                if (!drawing.isEmpty()) {
                    SpDocMeta docDrawing = spDocMetaService.createDocMeta("", drawing, "", drawingVersion, "", "","","","","","","");
                    SpLinkItemDrawing linkItem = spLinkItemDrawingService.createLinkItemDrawing(itemSequence.getIdItem(), docDrawing.getIdDoc());               
                }

                sequence = actualSequence;
            }

            fullIndex = getCsvCol("Full Index",tabLine,formatage);
            if(fullIndex.split(":").length == 3) {
                //manage new parts
                levelParts++;
                lvlParts = lvlSequence + "." + getLevel(levelParts);

                //create item for parts
                itemParts = spItemService.createItem(itemCode,"",itemDescription,"");

                //create bom
                SpDocBom bomParts = spDocBomService.createDocBom(docP.getIdDoc(), itemSequence.getIdItem(), itemParts.getIdItem(), true, itemOrder, lvlSequence, lvlParts, fullIndex.split(":")[2], "");


            } 
            
            } //end else

            // read next line before looping 
            line = br.readLine(); 
        }

        System.out.println("Process CSV terminé avec succès");

    }
    
}
