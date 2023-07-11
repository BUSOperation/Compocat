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

        spBsaService.getBomStructure("8b238c1e-e124-430a-b35a-9bb64c75c8ea");




        return ("Test");
    }
    
}
