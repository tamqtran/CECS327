import java.io.File;
class SearchInFolder {
  public static void main(String[] args) {

    //Desired Music extension
    final String EXT = ".java";
    
    //System command to grab current working directory
    String currentFolderPath = System.getProperty("user.dir");
    File currentFolder = new File(currentFolderPath);

    //list() vs listFiles()
    //List- string array
    //listFiles - files array
    //Doing list() reduces unnecessary code
    String[] FilesInFolder = currentFolder.list();
    for (int i = 0; i < FilesInFolder.length; i++) {
      if (FilesInFolder[i].endsWith(EXT))
        System.out.println(FilesInFolder[i]);
    };
  }
  
}
