package bobst.sp.compocat.controllers;

import java.io.IOException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import bobst.sp.compocat.services.SpBsaService;
import bobst.sp.compocat.services.SpPageService;


@RestController
public class CaptureController {

    @Autowired
    SpPageService spPageService;

    @Autowired
    SpBsaService spBsaService;

    
    @GetMapping("/doc")
    public UUID getidDoc() {
        return UUID.randomUUID();
    }
    
    
    @PostMapping("/captureD43Xml")
    public String processXml(@RequestParam("file") MultipartFile file) throws IllegalStateException, IOException {
        //String status = status_error;
        try {
            spBsaService.uploadXml(file);            
            //catPageContentService.uploadXml(file);             
            //status = "done";
        } catch (Exception e) {
            //
        }
        //informLog(file,status);
        return "redirect:/capture";
    }

    @PostMapping("/capturePVXml")
    public String processPVXml(@RequestParam("file") MultipartFile file) throws IllegalStateException, IOException {
        //String status = status_error;
        try {
            spBsaService.uploadPVXml(file);            
            //catPageContentService.uploadXml(file);             
            //status = "done";
        } catch (Exception e) {
            System.out.println(e);
        }
        //informLog(file,status);
        return "redirect:/capture";
    }

     @PostMapping("/captureE43Xml")
     public String processE43Xml(@RequestParam("file") MultipartFile file) throws IllegalStateException, IOException {
        try {
            spBsaService.uploadE43Xml(file);
        } catch (Exception e) {
            //
        }
        //informLog(file,status);
        return "redirect:/capture";
    }


}
