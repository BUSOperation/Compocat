package bobst.sp.compocat.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import bobst.sp.compocat.services.SpBsaService;

@RestController
public class ViewerController {
    
    @Autowired
    SpBsaService spBsaService;

    @GetMapping("/test")
    public String getTest() {

        //"0bc4065e-6563-4628-b3dd-626eaec32793"  BSA05810102_D87
        //"8b238c1e-e124-430a-b35a-9bb64c75c8ea"  BSA03802000066_D87
        spBsaService.getBomStructure("0bc4065e-6563-4628-b3dd-626eaec32793");




        return ("Test");
    }
    
}
