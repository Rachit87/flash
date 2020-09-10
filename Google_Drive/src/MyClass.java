import com.google.api.client.http.FileContent;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;

import java.io.IOException;
// ...

public class MyClass {

  // ...

  /**
   * Update an existing file's metadata and content.
   *
   * @param service Drive API service instance.
   * @param fileId ID of the file to update.
   * @param newTitle New title for the file.
   * @param newDescription New description for the file.
   * @param newMimeType New MIME type for the file.
   * @param newFilename Filename of the new content to upload.
   * @param newRevision Whether or not to create a new revision for this
   *        file.
   * @return Updated file metadata if successful, {@code null} otherwise.
   */
  public File updateFile(Drive service, String fileId, String newTitle,
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

  // ...
}