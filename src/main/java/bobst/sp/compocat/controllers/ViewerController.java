package bobst.sp.compocat.controllers;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import bobst.sp.compocat.services.SpBsaService;
import bobst.sp.compocat.services.SpPageService;
import bobst.sp.compocat.services.SpStorageService;

@RestController
public class ViewerController {
    
    @Autowired
    SpBsaService spBsaService;

    @Autowired
    SpPageService spPageService;

    @Autowired
    SpStorageService spStorageService;

    @GetMapping("/test")
    public String getTest() {

        //"0bc4065e-6563-4628-b3dd-626eaec32793"  BSA05810102_D87
        //"8b238c1e-e124-430a-b35a-9bb64c75c8ea"  BSA03802000066_D87
        spBsaService.getBomStructure("0bc4065e-6563-4628-b3dd-626eaec32793");
        return ("Test");
    }


    @GetMapping("/extractJpg")
    public String getJpg() {
        
            spStorageService.extractFile("jpg", "0b226c8e-c0f7-40f9-bad2-88243535985c_1.jpg" );
            //spPageService.extractJpg("862a43de-ccb6-422d-a4f4-45017440b2b7_1");
            return ("Extract jpg");
    }
    
}
