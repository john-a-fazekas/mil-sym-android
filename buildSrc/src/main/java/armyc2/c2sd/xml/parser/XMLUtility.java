package armyc2.c2sd.xml.parser;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class XMLUtility {

    public static String getXML(File path) throws IOException {
        InputStream is = null;
        BufferedReader reader = null;
        try {
            is = new FileInputStream(path);
            reader = new BufferedReader(new InputStreamReader(is));
            StringBuilder total = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                total.append(line);
            }
            return total.toString();
        } finally{
            if(is != null){
                is.close();
            }
            if(reader != null) {
                reader.close();
            }
        }
    }
}
