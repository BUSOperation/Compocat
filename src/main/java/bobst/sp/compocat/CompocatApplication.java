package bobst.sp.compocat;

import javax.xml.parsers.ParserConfigurationException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.xml.sax.SAXException;

import bobst.sp.compocat.services.WatchCaptureService;

@SpringBootApplication
public class CompocatApplication {

	public static void main(String[] args) throws IllegalStateException, InterruptedException, ParserConfigurationException, SAXException {
		SpringApplication.run(CompocatApplication.class, args);
		//WatchCaptureService.run();
	}

}
