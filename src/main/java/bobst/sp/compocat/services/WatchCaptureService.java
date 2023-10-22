package bobst.sp.compocat.services;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.JPEGTranscoder;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;

public class WatchCaptureService {

    @Autowired
    SpSvgService spSvgService;

    //voir comment intégrer ces constantes dans fichier proprieté //@Value("${server.capture}")
    public static final String DIR_CAPTURE = "C:\\capture\\entry\\";
    public static final String DIR_WORK = "src/main/resources/work/";
    public static final String SERVER_CAPTURE = "http://localhost:8080/";

    
    public static void sendFileToUrl(File file, URI url) throws ClientProtocolException, IOException {

        HttpEntity entity = MultipartEntityBuilder.create()
                            .addPart("file", new FileBody(file))
                            .build();
                        
        
        HttpPost request = new HttpPost(url);
        request.setEntity(entity);
    
        HttpClient client = HttpClientBuilder.create().build();
        client.execute(request);
    
    }


    public static File saveAsJPEG (File svg, String outputName) throws Exception {

        // Create a JPEG transcoder
        JPEGTranscoder t = new JPEGTranscoder();
    
        // Set the transcoding hints.
        t.addTranscodingHint(JPEGTranscoder.KEY_QUALITY, new Float(.8));

        // Create the transcoder input.
        //String svgURI = new File(args[0]).toURL().toString();
        String svgURI = svg.toURL().toString();
        TranscoderInput input = new TranscoderInput(svgURI);

        // Create the transcoder output.
        //OutputStream ostream = new FileOutputStream("src/main/resources/"+outputName+".jpg");
        OutputStream ostream = new FileOutputStream(outputName);
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
        
        //File jpgFile = new File("src/main/resources/"+outputName+".jpg");
        File jpgFile = new File(outputName);

        return jpgFile;
     }


    
    public static void run () throws InterruptedException, IllegalStateException, ParserConfigurationException, SAXException {
        try {
                // delete all files
                FileUtils.cleanDirectory(new File(DIR_CAPTURE)); 

                WatchService watchService = FileSystems.getDefault().newWatchService();

                Path pathCapture = Paths.get(DIR_CAPTURE);

                pathCapture.register(watchService, ENTRY_CREATE);
                WatchKey key;

                
                while ((key = watchService.take()) != null) {
                    for (WatchEvent<?> event : key.pollEvents()) {
                        Thread.sleep(5000);
                        System.out.println("Event kind:" + event.kind() + ". File affected: " + event.context());
                        
                        String fileName = event.context().toString();
                        //capture du nom de l'objet à traiter
                        String objName =  event.context().toString();
                        URI url = null;

                        if (!objName.contains(".") & (objName.contains("_D87_"))) {
                            //Identify a directory partsView D87
                            
                            System.out.println("detection directory partsview");

                            File dir = new File(DIR_CAPTURE + objName);
                            File dirDest = new File(DIR_WORK + objName);
                            //move directory
                            Files.move(dir.toPath(), dirDest.toPath(), StandardCopyOption.REPLACE_EXISTING);  

                            File[] liste = dirDest.listFiles();
                            for(File item : liste){
                                if(item.isFile())
                                { 
                                System.out.format("Nom du fichier: %s%n", item.getName()); 
                                } 
                                else if(item.isDirectory()) {
                                    System.out.format("Nom du répertoir: %s%n", item.getName()); 

                                    if (item.getName().equals("svg")) {
                                        File dirSvg = new File(DIR_WORK+fileName+"\\svg");
                                        File[] listeSvg = dirSvg.listFiles();
                                        for (File itemSvg : listeSvg) {
                                            //identify only svg file
                                            if(itemSvg.getName().contains(".svg")){
                                                String name = itemSvg.getName().split(".")[0];
                                                
                                                File dest = new File(DIR_WORK+fileName+"\\svg\\"+fileName+"_"+itemSvg.getName());
                                                if (itemSvg.renameTo(dest)) {
                                                    //saveAsJPEG(dest, DIR_WORK+fileName+"\\svg\\"+fileName+"_"+name+".jpg");
                                                    sendFileToUrl(dest, URI.create(SERVER_CAPTURE + "captureSvg"));
                                                }
                                            }
                                            
                                        }                                       

                                    }
                                    
                                    if (item.getName().equals("xml")) {
                                        File dirXml = new File(DIR_WORK+fileName+"\\xml");
                                        File[] listeXml = dirXml.listFiles();
                                        for (File itemXml : listeXml) {
                                            sendFileToUrl(itemXml, URI.create(SERVER_CAPTURE + "capturePVXml"));
                                        }

                                        
                                    }
                                    
                                    if (item.getName().equals("pdf")) {
                                        File dirPdf = new File(DIR_WORK+fileName+"\\pdf");
                                        File[] listePdf = dirPdf.listFiles();
                                        for (File itemPdf : listePdf) {
                                            File dest = new File(DIR_WORK+fileName+"\\pdf\\"+itemPdf.getName());
                                            if (itemPdf.renameTo(dest)) {
                                                sendFileToUrl(dest, URI.create(SERVER_CAPTURE + "capturePdf"));
                                            }
                                        }                                       

                                    }
                                } 
                            }

                            //url = URI.create(SERVER_CAPTURE + "captureReprisePV");

                        } else {


                            if ( (fileName.contains(".xml")) & (fileName.contains("_D43_")) ) {
                                System.out.println("detection D43 xml ok");
                                url = URI.create(SERVER_CAPTURE + "captureD43Xml");
                            }

                            if ( (fileName.contains(".xml")) & (fileName.contains("_D87_")) ) {
                                System.out.println("detection PV xml ok");
                                url = URI.create(SERVER_CAPTURE + "capturePVXml");
                            }

                            if ( (fileName.contains(".xml")) & (fileName.contains("_E43_")) ) {
                                System.out.println("detection E43 xml ok");
                                url = URI.create(SERVER_CAPTURE + "captureE43Xml");
                            }

                            if ( (fileName.contains(".zip")) & (fileName.contains("_E43_")) ) {
                                System.out.println("detection E43 zip(svg) ok");
                                url = URI.create(SERVER_CAPTURE + "captureZip");
                            }

                            if ( fileName.contains(".svg") ) {
                                System.out.println("detection svg ok");
                                url = URI.create(SERVER_CAPTURE + "captureSvg");
                            }

                            if ( fileName.contains("BOM.csv") ) {
                                System.out.println("detection Bobst Manchester BOM ok");
                                url = URI.create(SERVER_CAPTURE + "captureCsv");
                            }


                            
                            File file = new File(DIR_CAPTURE + fileName);   
                            
                            HttpEntity entity = MultipartEntityBuilder.create()
                                                .addPart("file", new FileBody(file))
                                                .build();
                                            
                            
                            HttpPost request = new HttpPost(url);
                            request.setEntity(entity);
                        
                            HttpClient client = HttpClientBuilder.create().build();
                            client.execute(request);
                        }
                        
                    }

                    key.reset();
                }
            } catch (IOException e) {
                System.err.println(e);
            }

    }

}
