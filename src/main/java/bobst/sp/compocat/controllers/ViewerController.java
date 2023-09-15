package bobst.sp.compocat.controllers;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import bobst.sp.compocat.services.SpBsaService;
import bobst.sp.compocat.services.SpPageService;

@RestController
public class ViewerController {
    
    @Autowired
    SpBsaService spBsaService;

    @Autowired
    SpPageService spPageService;

    @GetMapping("/test")
    public String getTest() {

        //"0bc4065e-6563-4628-b3dd-626eaec32793"  BSA05810102_D87
        //"8b238c1e-e124-430a-b35a-9bb64c75c8ea"  BSA03802000066_D87
        spBsaService.getBomStructure("0bc4065e-6563-4628-b3dd-626eaec32793");
        return ("Test");
    }


    @GetMapping("/extractJpg")
    public void getJpg() {
        try {
            spPageService.extractJpg("862a43de-ccb6-422d-a4f4-45017440b2b7_1");
        } catch (IOException e) {
            e.printStackTrace();
        } 
    }
    
}
