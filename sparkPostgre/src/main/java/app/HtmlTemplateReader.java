package app;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.servlet.ServletContext;

public class HtmlTemplateReader {
	/*
	public static String readTxtFile(String fileName) throws IOException {
        try (InputStream inputStream = HtmlTemplateReader.class.getClassLoader().getResourceAsStream(fileName);
             InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
             BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) { //empty { seem to represent a try

            StringBuilder stringBuilder = new StringBuilder();
            //stringBuilder seems to minimize the creation of string objects when adding, and this is more recommended
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
            return stringBuilder.toString();
        }
    }*/ //use of bufferedRedaer just lime MyIO
    
	//Faster code that gives more performance
	public static String readTxtFile(String fileName) throws IOException {
        String filePath = "src/main/resources/" + fileName;
        Path path = Paths.get(filePath);
        
        if (Files.exists(path)) {
            byte[] bytes = Files.readAllBytes(path);
            return new String(bytes);
        } else {
            throw new IOException("File not found: " + filePath);
        }
    }

}
