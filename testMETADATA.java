import java.io.IOException;
import java.nio.file.Paths;
import java.util.Scanner;

public class testMETADATA {

	public static void main(String[] args) throws IOException {
//		System.out.println(Paths.get("METADATA.txt"));
		Scanner sc = new Scanner(Paths.get("METADATA.txt"));
		Metadata meta= new Metadata();
		while (sc.hasNextLine()) {
		      meta.fileList.add(new File(sc.nextLine()));
		}

		for(int i = 0; i < meta.fileList.size(); i++){
			File f = meta.getFile(i);
			
			System.out.println(f.getFileName());				
		}
	}
}