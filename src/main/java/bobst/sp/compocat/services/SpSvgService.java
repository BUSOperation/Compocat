package bobst.sp.compocat.services;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Blob;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import bobst.sp.compocat.models.SpDocMeta;
import bobst.sp.compocat.models.SpPage;
import bobst.sp.compocat.repositories.SpPageRepository;

import org.apache.batik.transcoder.image.JPEGTranscoder;
import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;

@Service
public class SpSvgService {

    @Autowired
    SpDocMetaService docMetaService;

    @Autowired
    SpPageService pageService;

    @Autowired
    SpPageRepository pageRepository;



     public File saveAsJPEG (File svg, String outputName) throws Exception {

        // Create a JPEG transcoder
        JPEGTranscoder t = new JPEGTranscoder();
    
        // Set the transcoding hints.
        t.addTranscodingHint(JPEGTranscoder.KEY_QUALITY, new Float(.8));

        // Create the transcoder input.
        //String svgURI = new File(args[0]).toURL().toString();
        String svgURI = svg.toURL().toString();
        TranscoderInput input = new TranscoderInput(svgURI);

        // Create the transcoder output.
        OutputStream ostream = new FileOutputStream("src/main/resources/"+outputName+".jpg");
        TranscoderOutput output = new TranscoderOutput(ostream);

        // Save the image.
        try {
            t.transcode(input, output);
        } catch ( Exception e) {
                e.printStackTrace();
        }

        // Flush and close the stream.
        ostream.flush();
        ostream.close();
        File jpgFile = new File("src/main/resources/"+outputName+".jpg");

        return jpgFile;
     }


    public void uploadZip(MultipartFile file) throws IOException {
        unzip(file);
        System.out.println("Process Zip terminé avec succès");
    }


    private void copy(InputStream inputStream, OutputStream outputStream) throws IOException {
        byte[] buffer = new byte[1024];
        int length;
        while ((length = inputStream.read(buffer)) >= 0) {
          outputStream.write(buffer, 0, length);
        }
      }

    
    public void unzip(MultipartFile file) throws IOException {    
      ZipInputStream zis = new ZipInputStream(file.getInputStream()); 
      ZipEntry entry = zis.getNextEntry();
      while (entry != null) {
        Path outputEntryPath = Paths.get(file.getOriginalFilename().substring(0, file.getOriginalFilename().lastIndexOf("."))+".svg");
        if (entry.isDirectory() && !Files.exists(outputEntryPath)) {
          Files.createDirectory(outputEntryPath);
        } else if (!entry.isDirectory()) {
          try (FileOutputStream fos = new FileOutputStream(outputEntryPath.toFile())) {
            copy(zis, fos);
            fos.close();
            processSvg(outputEntryPath);

            // if (uploadInStorage(outputEntryPath, "DEV")) {
            //   insertBDcatPage(outputEntryPath);
            // }

            if (outputEntryPath.toFile().delete()) {
              System.out.println("Delete successfuly");
            } else {
              System.out.println("Failed to delete");
            }
          }
        }
        entry = zis.getNextEntry();
      }
      zis.closeEntry();
    }


    public void processSvg(Path p) {
        System.out.println("Traitement du fichier svg :" + p.getFileName());
        
        File svgFile = p.toFile();
        
        // Instantiate the Factory
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

        try {
            // parse svg file
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(svgFile);
            doc.getDocumentElement().normalize();
            
            NodeList listE = doc.getChildNodes(); 
            analyzeSvg(listE,"new");

        } catch ( IOException | ParserConfigurationException | SAXException e) {
                e.printStackTrace();
            }
            
        System.out.println("Process Svg terminé avec succès");

    }

    


    public void uploadSvg(MultipartFile file) throws Exception {

        String fName = file.getOriginalFilename().substring(0,file.getOriginalFilename().lastIndexOf("."));
        String[] tab = fName.split("_");
        
        // create or update docMeta
        SpDocMeta docMeta = docMetaService.createDocMeta("",fName,"","","","","","","","","","");  
        SpPage page = pageService.createPage(docMeta.getIdDoc(), docMeta.getIdDoc()+"_1");  
        
        


        File svgFile = new File("src/main/resources/" + file.getOriginalFilename());

        try (OutputStream os = new FileOutputStream(svgFile)) {
            os.write(file.getBytes());
            os.close();
        }

        // create Jpg
        File jpgFile = saveAsJPEG(svgFile,page.getIdPage());
        
        InputStream is = new FileInputStream(jpgFile);
        page.setFileJpg(is.readAllBytes());
        pageRepository.save(page);        
        
        
        // Instantiate the Factory
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

        try {
            // parse svg file
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(svgFile);
            doc.getDocumentElement().normalize();
            
            NodeList listE = doc.getChildNodes(); 
            analyzeSvg(listE,"old");

        } catch ( IOException e) {
                e.printStackTrace();
            }
            
        System.out.println("Process Svg terminé avec succès");

    }


    public void analyzeSvg(NodeList enf,String typeSvg) {
        //traitement svg - récupére les hotspots et leurs positions  - créer les fichiers jpg et les stocker en base ou sur Azure storage

        // <g id="hotspot.0" 
        //    onmouseover="ShowHotSpot(evt,'0')" 
        //    onmouseout="HideHotSpot(evt,'0')" 
        //    onmousemove="ShowToolTip(evt,'0',&quot;BSA00340000BP&quot;)" 
        //    opacity="0">
           
        //    <a xlink:href="">
        //          <path style="fill:#ff8000;fill-rule:evenodd" 
        //                d="M56.07,95.10L56.04,96.43L55.06,96.65L54.82,97.32L54.74,94.83L55.77,94.80zM54.31,95.12L54.28,97.32L52.67,97.26L52.45,96.95L52.06,97.51L51.29,97.54L50.97,96.62L50.31,97.58L49.82,97.50L49.50,97.19L49.11,97.19L48.78,97.50L48.31,97.55L47.74,97.21L47.44,97.34L46.81,97.58L46.44,97.32L46.53,94.91L47.12,94.82L47.46,95.13L47.87,95.14L48.21,94.82L48.91,94.82L49.48,95.21L49.72,94.87L50.40,94.77L50.97,95.84L51.46,94.78L52.45,95.21L52.68,94.89L53.99,94.81zM44.48,95.11L44.73,96.14L45.55,94.78L45.93,94.81L45.92,97.36L45.61,97.33L45.29,96.93L44.70,96.91L44.08,97.57L43.55,97.54L42.99,97.21L42.69,97.34L42.06,97.58L41.69,97.32L41.77,94.92L42.64,94.87L42.99,95.24L43.46,94.82L44.17,94.78zM41.21,95.10L41.17,97.32L40.82,97.57L39.42,97.18L39.09,96.89L36.70,97.58L36.33,97.29L36.42,96.60L36.75,96.90L37.34,97.03L37.35,96.67L36.33,96.12L36.42,94.92L37.32,94.81L37.93,95.45L36.89,95.45L36.90,95.77L38.20,96.14L38.74,94.81L39.07,94.81L39.69,96.13L40.30,94.92L40.90,94.79zM35.87,95.10L35.85,97.33L34.24,97.33L34.24,94.83L35.58,94.81zM50.41,95.68L50.41,96.78L50.11,97.07L50.11,95.38zM40.90,95.68L40.90,96.78L40.61,97.07L40.61,95.38zM55.77,95.83L55.01,95.83L55.01,95.43L55.77,95.43zM53.66,95.83L53.26,95.83L53.26,95.44L53.66,95.44zM35.24,95.83L34.85,95.83L34.85,95.44L35.24,95.44zM43.90,97.00L43.16,96.38L43.14,95.76L43.89,95.47zM48.28,95.78L48.63,95.42L48.63,97.03L48.28,96.67zM51.51,95.39L51.85,95.38L51.88,97.03L51.60,96.98zM46.85,95.48L47.13,95.42L47.13,97.03L46.85,96.98zM42.10,95.48L42.38,95.42L42.38,97.03L42.10,96.98zM53.66,97.02L53.26,97.02L53.26,96.62L53.66,96.62zM35.48,96.93L34.81,97.02L34.83,96.64L35.53,96.58z"/>
        //    </a>
        // </g>
               

        //onclick="javascript:viewDrawing('page48')"
        //onclick="javascript:viewPart(1)"


        for (int i = 0; i < enf.getLength(); i++) {

            if (enf.item(i).getNodeName().equals("g")) {

                Element elementG = (Element) enf.item(i);
                String attrId = elementG.getAttribute("id");

                if (attrId.contains("hotspot.")) {

                    String attrMove = elementG.getAttribute("onmousemove");
                    attrMove = attrMove.split(",")[2].replace(")","").replace("\"", "");

                    String attrOnclick = elementG.getAttribute("onclick");
                    
                    if (attrOnclick.contains("page")) {
                        //lien GOC
                        attrOnclick = attrOnclick.substring(24); //      split("\(")[1].replace("\)","");  //javascript:viewDrawing(","");
                    } else {
                        //lien pièce                        
                        attrOnclick = attrOnclick.substring(21);
                        attrOnclick = attrOnclick.replace("'","");
                    }

                    // si element g avec un attribut id = hotspot...
                    for (int j = 0; j < elementG.getChildNodes().getLength(); j++) {

                        if (elementG.getChildNodes().item(j).getNodeName().equals("a")) {
                            
                            Element elementA = (Element) elementG.getChildNodes().item(j);
                            for (int k=0;k<elementA.getChildNodes().getLength();k++) {

                                if (elementA.getChildNodes().item(k).getNodeName().equals("path")) {
                                Element elePath = (Element) elementA.getChildNodes().item(k);
                                String d = elePath.getAttribute("d");
                                processPathD(d);                        }

                            }

                        }

                    }

                }



            }

            if (enf.item(i).hasChildNodes()) {
                analyzeSvg(enf.item(i).getChildNodes(), typeSvg);
            }

        }

            
    }


    public void processPathD(String d) {

        System.out.println(d);

        String[] tabObj = d.split("z");

        String xPos = tabObj[0].replace("M",",").replace("L",",").split(",")[1];
        String yPos = tabObj[0].replace("M",",").replace("L",",").split(",")[2];


        double xDpos = Double.parseDouble(xPos);
        double multi = 3.78d;
        xDpos = xDpos * multi;

        String tmp =  Integer.toString((int) Math.round(xDpos));    //récupérer l'entier
       

         


    }


}
