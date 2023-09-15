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

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class SpGenService {

    private String getCsvCol(String col,String[] tabLine, String[] formatage){

        
        String res = "";
      
      try {
        //cherche colonne
        for (int i=0; i<formatage.length; i++) {
            if (!formatage[i].isEmpty() & formatage[i].equals(col)) {
                res = tabLine[i];
            }
        }

        } catch (Exception e)  {
            e.printStackTrace();
        }

        
        return res;
    }

    public void uploadCsv(MultipartFile file) throws FileNotFoundException, IOException {

        File csvFile = new File("src/main/resources/" + file.getOriginalFilename());

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
        
        // loop until all lines are read 
        while (line != null) {

            System.out.println(line);
            String[] tabLine = line.split(";");
            
            //identification du formatage / des colonnes du csv
            System.out.println(tabLine[0]);
            if(tabLine[0].contains("Section") & !start){
                for(int i=0; i<tabLine.length; i++) {
                    formatage[i] = tabLine[i];
                }
                start = true;
            }

            //traitement des lignes
            //Section;Sequence;Full Index;Item Code;Item Revision;Item Description;Quantity;Master PDO;CO;FMO;Action;Comment;Whse;Issue Type;Legacy PO;SAP PO;SubCon PO;Master PDO Status;Assembly Drwg;Assembly Drwg Rev
            // 01 - Vessel External;;01:080;903G581;2;Chuck port blanking flanges;1;6973;1111;0;Procure New;Quantity Increased;Pennine;FF;;;;In Assembly;;
            // 01 - Vessel External;01:010 - Vessel Assembly with Roof Tracks K5-Expert;01:010;910G556-1650;0;Vessel Assembly with Roof Tracks K5-Expert;1;6973;0;0;;;Pennine;FF;;;;In Assembly;435G294;
            // 01 - Vessel External;01:010 - Vessel Assembly with Roof Tracks K5-Expert;01:010:M002;419G478-1650;2;Roof Track Beam;1;7566;0;0;;;Pennine;FF;;3742;;Built;435G150;
            // 01 - Vessel External;01:010 - Vessel Assembly with Roof Tracks K5-Expert;01:010:M003;419G479-1650;1;Roof Track Beam;1;7566;0;0;;;Pennine;FF;;3742;;Built;435G150;
            // 01 - Vessel External;01:010 - Vessel Assembly with Roof Tracks K5-Expert;01:010:M004;402G660;2;Roof Track End Plate;1;7566;0;0;;;Pennine;FF;;3742;;Built;435G150;
            // 01 - Vessel External;01:010 - Vessel Assembly with Roof Tracks K5-Expert;01:010:M005;403G770;0;Runway Beam Packer;12;7566;0;0;;;Pennine;FF;;3742;;Built;435G150;

            String fullIndex, itemCode, itemDescription, drawing; 
            //creation d'un Item
            itemCode = getCsvCol("Item Code",tabLine,formatage);
            itemDescription = getCsvCol("Item Description",tabLine,formatage);

            fullIndex = getCsvCol("Full Index",tabLine,formatage);

            drawing =  getCsvCol("Assembly Drwg",tabLine,formatage);


            // read next line before looping 
            line = br.readLine(); 
        }

    }
    
}
