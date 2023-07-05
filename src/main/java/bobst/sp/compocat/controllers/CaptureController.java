package bobst.sp.compocat.controllers;

import java.io.IOException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import bobst.sp.compocat.services.SpPageService;


@RestController
public class CaptureController {

    @Autowired
    SpPageService spPageService;

    @GetMapping("/begin")
    public String getBegin() {
        Integer tst;
        tst = spPageService.getNbrPageDrawing(null);
        return ("Begin Compocat1 " + tst);
    }


    @GetMapping("/doc")
    public UUID getidDoc() {
        return UUID.randomUUID();
    }
    
    
    @PostMapping("/captureD43Xml")
    public String processXml(@RequestParam("file") MultipartFile file) throws IllegalStateException, IOException {
        //String status = status_error;
        try {
            
            //catPageContentService.uploadXml(file);             
            //status = "done";
        } catch (Exception e) {
            //
        }
        //informLog(file,status);
        return "redirect:/capture";
    }



}
