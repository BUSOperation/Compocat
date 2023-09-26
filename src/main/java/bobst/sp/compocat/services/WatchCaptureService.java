package bobst.sp.compocat.services;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.tomcat.util.http.fileupload.FileUtils;

public class WatchCaptureService {

    public static final String DIR_CAPTURE = "C:\\capture\\entry\\";

    //@Value("${server.capture}")
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
                        URI url = null;

                        if (!fileName.contains(".") & (fileName.contains("_D87_"))) {
                            //Identify a directory partsView
                            
                            System.out.println("detection directory partsview");

                            File dir  = new File(DIR_CAPTURE+fileName);
                            File[] liste = dir.listFiles();
                            for(File item : liste){
                                if(item.isFile())
                                { 
                                System.out.format("Nom du fichier: %s%n", item.getName()); 
                                } 
                                else if(item.isDirectory()) {
                                    System.out.format("Nom du répertoir: %s%n", item.getName()); 

                                    if (item.getName().equals("svg")) {
                                        File dirSvg = new File(DIR_CAPTURE+fileName+"\\svg");
                                        File[] listeSvg = dirSvg.listFiles();
                                        for (File itemSvg : listeSvg) {
                                            File dest = new File(DIR_CAPTURE+fileName+"\\svg\\"+fileName+"_"+itemSvg.getName());
                                            if (itemSvg.renameTo(dest)) {
                                                sendFileToUrl(dest, URI.create(SERVER_CAPTURE + "captureSvg"));
                                            }
                                        }                                       

                                    }
                                    
                                    if (item.getName().equals("xml")) {
                                        File dirXml = new File(DIR_CAPTURE+fileName+"\\xml");
                                        File[] listeXml = dirXml.listFiles();
                                        for (File itemXml : listeXml) {
                                            sendFileToUrl(itemXml, URI.create(SERVER_CAPTURE + "capturePVXml"));
                                        }

                                        
                                    }
                                    
                                    if (item.getName().equals("pdf")) {
                                        File dirPdf = new File(DIR_CAPTURE+fileName+"\\pdf");
                                        File[] listePdf = dirPdf.listFiles();
                                        for (File itemPdf : listePdf) {
                                            File dest = new File(DIR_CAPTURE+fileName+"\\pdf\\"+itemPdf.getName());
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
