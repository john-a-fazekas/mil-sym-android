package armyc2.c2sd.xml.parser;

import java.io.DataOutputStream;
import java.io.IOException;

/**
 *
 * @author michael.spinelli
 */
public class SinglePointLookupInfo {
    
    private String _SymbolID = "";
    private String _Description = "";
    private int _mappingP = 0;
    private int _mappingA = 0;
    private int _height = 0;
    private int _width = 0;
    
    public SinglePointLookupInfo(String basicSymbolID, String description,
                                 String mappingP, String mappingA, String width, String height)
    {
        _SymbolID = basicSymbolID;
        _Description = description;
        if(mappingP != null && mappingP.equals("") == false)
                _mappingP = Integer.valueOf(mappingP);
        if(mappingA != null && mappingA.equals("") == false)
                _mappingA = Integer.valueOf(mappingA);
        if(height != null && height.equals("") == false)
                _height = Integer.valueOf(height);
        if(width != null && width.equals("") == false)
                _width = Integer.valueOf(width);
    }

    public void writeBinary(DataOutputStream dos) throws IOException {
      dos.writeUTF(_SymbolID);
      dos.writeUTF(_Description);
      dos.writeInt(_mappingP);
      dos.writeInt(_mappingA);
      dos.writeInt(_height);
      dos.writeInt(_width);
    }
}
