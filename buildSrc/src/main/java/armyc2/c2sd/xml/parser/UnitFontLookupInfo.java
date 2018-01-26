package armyc2.c2sd.xml.parser;

import java.io.DataOutputStream;
import java.io.IOException;

/**
 *
 * @author Michael.Spinelli
 */
public class UnitFontLookupInfo {

    public String _SymbolID = "";
    public String _Description = "";
    public int _mapping1 = 0;
    public int _mapping1U = 0;
    public int _mapping1F = 0;
    public int _mapping1N = 0;
    public int _mapping1H = 0;
    public int _mapping2 = 0;
    public String _color1 = "";
    public String _color2 = "";

    public UnitFontLookupInfo(String SymbolID, String Description, String M1U, String M1F,
                              String M1N, String M1H, String Color1, String M2, String Color2)
    {
        _SymbolID = SymbolID;
        _Description = Description;

        if(M1U != null && !M1U.equals(""))
            _mapping1U = Integer.valueOf(M1U);
        if(M1F != null && !M1F.equals(""))
            _mapping1F = Integer.valueOf(M1F);
        if(M1N != null && !M1N.equals(""))
            _mapping1N = Integer.valueOf(M1N);
        if(M1H != null && !M1H.equals(""))
            _mapping1H = Integer.valueOf(M1H);
        if(M2 != null && !M2.equals(""))
            _mapping2 = Integer.valueOf(M2);

        _color1 = Color1;
        _color2 = Color2;
    }

    public void writeBinary(DataOutputStream dos) throws IOException {
        dos.writeUTF(_SymbolID);
        dos.writeUTF(_Description);
        dos.writeInt(_mapping1);
        dos.writeInt(_mapping1U);
        dos.writeInt(_mapping1F);
        dos.writeInt(_mapping1N);
        dos.writeInt(_mapping1H);
        dos.writeInt( _mapping2);

        boolean isColorValid = _color1 != null && !"".equals(_color1);
        dos.writeBoolean(isColorValid);
        if(isColorValid){
            dos.writeUTF(_color1);
        }

        isColorValid = _color2 != null && !"".equals(_color2);
        dos.writeBoolean(isColorValid);
        if(isColorValid){
            dos.writeUTF(_color2);
        }
    }
}
