package armyc2.c2sd.xml.parser;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UnitDefParser implements BinaryWriter {
    private static Map<String, UnitDef> _UnitDefinitionsB = null;
    private static ArrayList<UnitDef> _UnitDefDupsB = null;

    private static Map<String, UnitDef> _UnitDefinitionsC = null;
    private static ArrayList<UnitDef> _UnitDefDupsC = null;

    /**
     * must be called first
     */
    public synchronized void init(String[] unitConstantsXML)
    {
        _UnitDefinitionsB = new HashMap<>();
        _UnitDefDupsB = new ArrayList<>();

        _UnitDefinitionsC = new HashMap<>();
        _UnitDefDupsC = new ArrayList<>();

        String lookupXmlB = unitConstantsXML[0];// FileHandler.InputStreamToString(xmlStreamB);
        String lookupXmlC = unitConstantsXML[1];
        // FileHandler.InputStreamToString(xmlStreamC);
        // String lookupXml = FileHandler.fileToString("C:\\UnitFontMappings.xml");
        populateLookup(lookupXmlB, 0);
        populateLookup(lookupXmlC, 1);
    }

    private static void populateLookup(String xml, int symStd)
    {
        UnitDef ud = null;

        Document document = XMLParser.getDomElement(xml);

        NodeList symbols = XMLUtil.getItemList(document, "SYMBOL");
        for (int i = 0; i < symbols.getLength(); i++) {
            Node node = symbols.item(i);

            String symbolID = XMLUtil.parseTagValue(node, "SYMBOLID");
            String description = XMLUtil.parseTagValue(node, "DESCRIPTION");
            description = description.replaceAll("&amp;", "&");
            String drawCategory = XMLUtil.parseTagValue(node, "DRAWCATEGORY");
            String hierarchy = XMLUtil.parseTagValue(node, "HIERARCHY");
            String alphaHierarchy = XMLUtil.parseTagValue(node, "ALPHAHIERARCHY");
            String path = XMLUtil.parseTagValue(node, "PATH");

            if (SymbolUtilities.isInstallation(symbolID))
                symbolID = symbolID.substring(0, 10) + "H****";

            int idc = 0;
            if (drawCategory != null || drawCategory.equals("") == false)
                idc = Integer.valueOf(drawCategory);

            ud = new UnitDef(symbolID, description, idc, hierarchy, path);

            boolean isMCSSpecificFE = SymbolUtilities.isMCSSpecificForceElement(ud);

            if (symStd == 0) {
                if (_UnitDefinitionsB.containsKey(symbolID) == false && isMCSSpecificFE == false)
                    _UnitDefinitionsB.put(symbolID, ud);// EMS have dupe symbols with same code
                else if (isMCSSpecificFE == false)
                    _UnitDefDupsB.add(ud);
            } else {
                if (_UnitDefinitionsC.containsKey(symbolID) == false && isMCSSpecificFE == false)
                    _UnitDefinitionsC.put(symbolID, ud);// EMS have dupe symbols with same code
                else if (isMCSSpecificFE == false)
                    _UnitDefDupsC.add(ud);
            }
        }
    }// end populateLookup


    public void writeToBinary(DataOutputStream dos) throws IOException
    {
        dos.writeInt(_UnitDefinitionsB.size());
        for (Map.Entry<String, UnitDef> entry : _UnitDefinitionsB.entrySet()) {
            entry.getValue().writeBinary(dos);
        }
        dos.writeInt(_UnitDefDupsB.size());
        for (UnitDef entry : _UnitDefDupsB) {
            entry.writeBinary(dos);
        }

        dos.writeInt(_UnitDefinitionsC.size());
        for (Map.Entry<String, UnitDef> entry : _UnitDefinitionsC.entrySet()) {
            entry.getValue().writeBinary(dos);
        }
        dos.writeInt(_UnitDefDupsC.size());
        for (UnitDef entry : _UnitDefDupsC) {
            entry.writeBinary(dos);
        }
    }
}
