package bobst.sp.compocat.services;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

import javax.imageio.ImageIO;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import bobst.sp.compocat.models.SpDocMeta;
import bobst.sp.compocat.models.SpPage;

@Service
public class SpPdfService {

    @Autowired
    SpDocMetaService spDocMetaService;

    @Autowired
    SpPageService spPageService;

    private File saveAsSen(File svgFile, String outputName) throws IOException {

        File fSen = new File("src/main/resources/"+outputName+".sen");
        FileWriter fw = new FileWriter(fSen, StandardCharsets.UTF_8);
        
        fw.write("00872002430087800252N1");  //info de base 
        fw.close();

        return fSen;
    }

    public File convertPdfToJpg(File sourceFile, String idPage) {

            //charger le fichier pdf
            try {

                    //String destinationDir = "C:\\Users\\venkataudaykiranp\\Downloads\\Converted_PdfFiles_to_Image/"; // converted images from pdf document are saved here
                    String destinationDir = "src/main/resources/";
                
                    File destinationFile = new File(destinationDir);
                    if (!destinationFile.exists()) {
                        destinationFile.mkdir();
                        System.out.println("Folder Created -> "+ destinationFile.getAbsolutePath());
                    }
                    if (sourceFile.exists()) {
                        System.out.println("Images copied to Folder Location: "+ destinationFile.getAbsolutePath());             
                        //PDDocument document = Loader.loadPDF()  PDDocument.load(sourceFile);
                        PDDocument document = Loader.loadPDF(sourceFile,"");
                        PDFRenderer pdfRenderer = new PDFRenderer(document);

                        int numberOfPages = document.getNumberOfPages();
                        System.out.println("Total files to be converting -> "+ numberOfPages);

                        //String fileName = sourceFile.getName().replace(".pdf", "");             
                        //String fileExtension= "png";
                        String fileExtension= "jpg";
                        /*
                        * 600 dpi give good image clarity but size of each image is 2x times of 300 dpi.
                        * Ex:  1. For 300dpi 04-Request-Headers_2.png expected size is 797 KB
                        *      2. For 600dpi 04-Request-Headers_2.png expected size is 2.42 MB
                        */
                        int dpi = 300;// use less dpi for to save more space in harddisk. For professional usage you can use more than 300dpi 

                        for (int i = 0; i < numberOfPages; ++i) {
                           // File outPutFile = new File(destinationDir + fileName +"_"+ (i+1) +"."+ fileExtension);
                            File outPutFile = new File(destinationDir + idPage +"."+ fileExtension);
                            BufferedImage bImage = pdfRenderer.renderImageWithDPI(i, dpi, ImageType.RGB);
                            ImageIO.write(bImage, fileExtension, outPutFile);
                        }

                        document.close();
                        System.out.println("Converted Images are saved at -> "+ destinationFile.getAbsolutePath());
                    } else {
                        System.err.println(sourceFile.getName() +" File not exists");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                return null;
    }

        public void uploadPdf(MultipartFile file) throws FileNotFoundException, IOException {

            String fName = file.getOriginalFilename().substring(0,file.getOriginalFilename().lastIndexOf(".")).toUpperCase();  //fichier en majuscule
            String dName = fName; //dName drawing Name
            String pageExt = "_1";

            if (fName.contains("_")) {
                //drawing contains several pages
                dName = fName.substring(0, fName.lastIndexOf("_"));
                pageExt = fName.substring(fName.lastIndexOf("_"), fName.length());
            }
                        
            // create or update docMeta
            SpDocMeta docMeta = spDocMetaService.createDocMeta("",dName,"","","","","","","","","","");  
            SpPage page = spPageService.createPage(docMeta.getIdDoc(), docMeta.getIdDoc()+ pageExt);  
            
            File pdfFile = new File("src/main/resources/" + file.getOriginalFilename());

            try (OutputStream os = new FileOutputStream(pdfFile)) {
                os.write(file.getBytes());
                os.close();
            }

            // create Jpg
            File jpgFile = convertPdfToJpg(pdfFile,page.getIdPage());

            //create sen
            File senFile = saveAsSen(pdfFile,page.getIdPage());
            
            //InputStream is = new FileInputStream(jpgFile);
            //page.setFileJpg(is.readAllBytes());
            //pageRepository.save(page);        
            
                
            System.out.println("Process Pdf terminé avec succès");




        }
}


