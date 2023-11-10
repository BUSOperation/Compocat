package bobst.sp.compocat.services;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Path;

import org.springframework.stereotype.Service;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;

@Service
public class SpStorageService {

    private boolean uploadInStorage(Path source, String typeContainer) {

        boolean res = false;
        //String connectionString = "DefaultEndpointsProtocol=https;AccountName=svgview;AccountKey=I++B93xrQ5mpYyLtTahG08/wM20grjTqRQhFrYDi8w8HvS7mMBt3y09aOLswkmqgS5DNQo8LxXzOAt9MXEvicw==;EndpointSuffix=core.windows.net";
        String connectionString = "DefaultEndpointsProtocol=https;AccountName=bobst1storparts;AccountKey=w69TmEZAiTlX+lkBgEc13eW0usNlbhB3Y2JgEtJa+p5jbWhSY3hYls6s0rig8CyX3Z4alrU8Swvv+AStznOmmQ==;EndpointSuffix=core.windows.net";
        
        // Create a BlobServiceClient object which will be used to return the container client
        BlobServiceClient blobServiceClient = new BlobServiceClientBuilder().connectionString(connectionString).buildClient();
        
        // Return the container client object  typeContainer = jpg
        BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient(typeContainer);

        String localPath = source.toAbsolutePath().toString();
        //String fileName = source.toString(); 
        String fileName = source.getFileName().toString();

        // Get a reference to a blob
        BlobClient blobClient = containerClient.getBlobClient(fileName);

        System.out.println("\nUploading to Blob storage as blob:\n\t" + blobClient.getBlobUrl());
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(localPath);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // Upload or update the blob (overwrite= true)
        try {
           blobClient.upload(fis, true);
           //blobClient.uploadFromFile(localPath, true);          
           res = true;

        } catch (UncheckedIOException ex) {
           System.err.printf("Failed to upload from file %s%n", ex.getMessage());
        } 
        
        return res;
    }

    
    private boolean downloadStorage(String fileToExtract, String typeContainer) {

        boolean res = false;
        //String connectionString = "DefaultEndpointsProtocol=https;AccountName=svgview;AccountKey=I++B93xrQ5mpYyLtTahG08/wM20grjTqRQhFrYDi8w8HvS7mMBt3y09aOLswkmqgS5DNQo8LxXzOAt9MXEvicw==;EndpointSuffix=core.windows.net";
        String connectionString = "DefaultEndpointsProtocol=https;AccountName=bobst1storparts;AccountKey=w69TmEZAiTlX+lkBgEc13eW0usNlbhB3Y2JgEtJa+p5jbWhSY3hYls6s0rig8CyX3Z4alrU8Swvv+AStznOmmQ==;EndpointSuffix=core.windows.net";
        
        // Create a BlobServiceClient object which will be used to return the container client
        BlobServiceClient blobServiceClient = new BlobServiceClientBuilder().connectionString(connectionString).buildClient();
        
        // Return the container client object  typeContainer = jpg
        BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient(typeContainer);

        // Get a reference to a blob
        BlobClient blobClient = containerClient.getBlobClient(fileToExtract);
        try {
            blobClient.download(new FileOutputStream("c:\\capture\\out\\"+fileToExtract)) ;
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        res = true;
         
        System.out.println("\nDownloading to c:\\capture\\out:\n\t" + blobClient.getBlobUrl()); 
        
        return res;
    }

    
    //-------------------------------------------------------------------------------------
    
    public Boolean connectToStorage() {
        
        
        return true;
    }

    //-------------------------------------------------------------------------------------

    public Boolean storeFile(String fileType, File fileToStore) {
        
        uploadInStorage(fileToStore.toPath(), fileType);
        return true;
    }

    //-------------------------------------------------------------------------------------
    
    public File extractFile(String fileType, String fileName) {
        File fExtract = null;
        downloadStorage(fileName, fileType);
        return fExtract;
    }
    
}
