import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.FileContent;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.Channel;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import com.google.api.services.drive.Drive.Files;
import com.google.api.services.drive.Drive.Files.Watch;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;

public class DriveQuickstart {
    private static final String APPLICATION_NAME = "Google Drive API Java Quickstart";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final String TOKENS_DIRECTORY_PATH = "tokens";

    /**
     * Global instance of the scopes required by this quickstart.
     * If modifying these scopes, delete your previously saved tokens/ folder.
     */
    private static final List<String> SCOPES = Collections.singletonList(DriveScopes.DRIVE);
    private static final String CREDENTIALS_FILE_PATH = "/credentials.json";

    /**
     * Creates an authorized Credential object.
     * @param HTTP_TRANSPORT The network HTTP Transport.
     * @return An authorized Credential object.
     * @throws IOException If the credentials.json file cannot be found.
     */
    private static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException {
        // Load client secrets.
        InputStream in = DriveQuickstart.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
        if (in == null) {
            throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
        }
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .build();
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }

    public static void main(String... args) throws IOException, GeneralSecurityException {
        // Build a new authorized API client service.
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        Drive service = new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                .setApplicationName(APPLICATION_NAME)
                .build();

        // Creates a folder in Googlr Drive
        /*File fileMetadata = new File();
        fileMetadata.setName("Drive_API");
        fileMetadata.setMimeType("application/vnd.google-apps.folder");

        File file = service.files().create(fileMetadata)
            .setFields("id")
            .execute();
        System.out.println("Folder ID: " + file.getId());*/
        
        //To send a file in the created folder
        /*String folderId = "1VisC0EeeK6RdR457yktGS65dXAur_M8J";
        File fileMetadata = new File();
        fileMetadata.setName("rachit.jpg");
        fileMetadata.setParents(Collections.singletonList(folderId));
        java.io.File filePath = new java.io.File("F:\\Vaishno devi pics\\vaishno.jpg");
        FileContent mediaContent = new FileContent("image/jpeg", filePath);
        File file = service.files().create(fileMetadata, mediaContent)
            .setFields("id, parents")
            .execute();
        System.out.println("File ID: " + file.getId());*/

        /*String folderId = "1VisC0EeeK6RdR457yktGS65dXAur_M8J";
        String fileId = "12-mWTHDi37y5dh8IvQPUR7CRg_Z8zJW9";
        File fileMetadata = new File();
        fileMetadata.setName("renamed.jpg");
        fileMetadata.setParents(Collections.singletonList(folderId));
        
        //java.io.File filePath = new java.io.File("F:\\Vaishno devi pics\\vaishno.jpg");
        //FileContent mediaContent = new FileContent("image/jpeg");
        File file = service.files().create(fileMetadata)
            .setFields("id, parents")
            .execute();
        System.out.println("File ID: " + file.getId());*/
  
      // DriveQuickstart.updateFile(service, "1epaMXA1scAaIf1lTwXMiYPh2MHatGIZq", "renamed.jpg", null, "image/jpeg", "javapic.jpg", true);
       //DriveQuickstart.printFile(service, "1epaMXA1scAaIf1lTwXMiYPh2MHatGIZq");
        DriveQuickstart.deleteFile(service, "1epaMXA1scAaIf1lTwXMiYPh2MHatGIZq");
        
        System.out.println("Done");
        }
    
    private static void deleteFile(Drive service, String fileId) {
        try {
          service.files().delete(fileId).execute();
        } catch (IOException e) {
          System.out.println("An error occurred: " + e);
        }
      }

    
    private static File renameFile(Drive service, String fileId, String newTitle) {
        try {
          File file = new File();
          file.setName(newTitle);

          // Rename the file.
          Files.Update patchRequest = service.files().update(fileId, file);
          patchRequest.setFields("title").execute();

          File updatedFile = patchRequest.execute();
          return updatedFile;
        } catch (IOException e) {
          System.out.println("An error occurred: " + e);
          return null;
        }
      }



    
    
    
    
    
    private static void printFile(Drive service, String fileId) {

        try {
        	
          File file = service.files().get(fileId).execute();
          
          
          
          
          System.out.println("Title: " + file.getName());
          System.out.println("Description: " + file.getDescription());
          System.out.println("MIME type: " + file.getMimeType());
         
          
          
        } catch (IOException e) {
          System.out.println("An error occurred: " + e);
        }
	
      }
    
    private static File updateFile(Drive service, String fileId, String newTitle,
    	      String newDescription, String newMimeType, String newFilename, boolean newRevision) {
    	    try {
    	      // First retrieve the file from the API.
    	      File file = service.files().get(fileId).execute();

    	      // File's new metadata.
    	      file.setName(newTitle);
    	      file.setDescription(newDescription);
    	      file.setMimeType(newMimeType);

    	      // File's new content.
    	      java.io.File fileContent = new java.io.File(newFilename);
    	      FileContent mediaContent = new FileContent(newMimeType, fileContent);

    	      // Send the request to the API.
    	      File updatedFile = service.files().update(fileId, file, mediaContent).execute();

    	      return updatedFile;
    	    } catch (IOException e) {
    	      System.out.println("An error occurred: " + e);
    	      return null;
    	    }
    	  }

    
   

    }
