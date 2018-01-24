package armyc2.c2sd.xml.parser;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class UnitFontParser implements BinaryWriter {
    private static HashMap<String, UnitFontLookupInfo> hashMapB = new HashMap<>();
    private static HashMap<String, UnitFontLookupInfo> hashMapC = new HashMap<>();

    public synchronized void init(String[] unitFontMappingsStream)
    {
        String lookupXmlB = unitFontMappingsStream[0];// FileHandler.InputStreamToString(xmlStreamB);
        String lookupXmlC = unitFontMappingsStream[1];// FileHandler.InputStreamToString(xmlStreamC);

        populateLookup(lookupXmlB, 0);
        populateLookup(lookupXmlC, 1);
    }

    private static void populateLookup(String xml, int SymbologyStandard)
    {
        Document document = XMLParser.getDomElement(xml);

        NodeList symbols = XMLUtil.getItemList(document, "SYMBOL");
        for (int i = 0; i < symbols.getLength(); i++) {
            Node node = symbols.item(i);

            String ID = XMLUtil.parseTagValue(node, "SYMBOLID");
            String description = XMLUtil.parseTagValue(node, "DESCRIPTION");
            String m1u = XMLUtil.parseTagValue(node, "MAPPING1U");
            String m1f = XMLUtil.parseTagValue(node, "MAPPING1F");
            String m1n = XMLUtil.parseTagValue(node, "MAPPING1N");
            String m1h = XMLUtil.parseTagValue(node, "MAPPING1H");
            String m2 = XMLUtil.parseTagValue(node, "MAPPING2");
            String c1 = XMLUtil.parseTagValue(node, "MAPPING1COLOR");
            String c2 = XMLUtil.parseTagValue(node, "MAPPING2COLOR");

            UnitFontLookupInfo uflTemp = null;

            // Check for bad font locations and remap
            m1u = checkMappingIndex(m1u);
            m1f = checkMappingIndex(m1f);
            m1n = checkMappingIndex(m1n);
            m1h = checkMappingIndex(m1h);
            m2 = checkMappingIndex(m2);
            ////////////////////////////////////////

            uflTemp = new UnitFontLookupInfo(ID, description, m1u, m1f, m1n, m1h, c1, m2, c2);

            if (uflTemp != null) {
                if (SymbologyStandard == 0)
                    hashMapB.put(ID, uflTemp);
                else if (SymbologyStandard == 1)
                    hashMapC.put(ID, uflTemp);
            }
        }
    }

    /**
     * Until XML files are updated, we need to shift the index
     *
     * @param index
     * @return
     */
    private static String checkMappingIndex(String index)
    {
        int i = -1;
        if (SymbolUtilities.isNumber(index)) {
            i = Integer.valueOf(index);

            if (i < 9000)
                return String.valueOf(i + 57000);
            else
                return String.valueOf(i + 54000);

        }

        return index;
    }

    public void writeToBinary(DataOutputStream dos) throws IOException
    {
        dos.writeInt(hashMapB.size());
        for (Map.Entry<String, UnitFontLookupInfo> entry : hashMapB.entrySet()) {
            entry.getValue().writeBinary(dos);
        }

        dos.writeInt(hashMapC.size());
        for (Map.Entry<String, UnitFontLookupInfo> entry : hashMapC.entrySet()) {
            entry.getValue().writeBinary(dos);
        }
    }
}
