package armyc2.c2sd.xml.parser;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SinglePointParser implements BinaryWriter {
    private static Map<String, SinglePointLookupInfo> hashMapB = null;
    private static Map<String, SinglePointLookupInfo> hashMapC = null;

    /**
     * @name init
     *
     * @desc Simply calls xmlLoaded
     *
     * @return None
     */
    public synchronized void init(String[] singlepointLookupXML)
    {
            hashMapB = new HashMap<>();
            hashMapC = new HashMap<>();

            String lookupXmlB = singlepointLookupXML[0];// FileHandler.InputStreamToString(xmlStreamB);
            String lookupXmlC = singlepointLookupXML[1];// FileHandler.InputStreamToString(xmlStreamC);

            populateLookup(lookupXmlB, 0);
            populateLookup(lookupXmlC, 1);
    }

    /**
     * @name populateLookup
     *
     * @desc
     *
     * @param xml - IN -
     * @return None
     */
    private static void populateLookup(String xml, int symStd)
    {
        Document document = XMLParser.getDomElement(xml);

        NodeList symbols = XMLUtil.getItemList(document, "SYMBOL");
        for (int i = 0; i < symbols.getLength(); i++) {
            Node node = symbols.item(i);

            SinglePointLookupInfo spli = null;
            String basicID = XMLUtil.parseTagValue(node, "SYMBOLID");
            String description = XMLUtil.parseTagValue(node, "DESCRIPTION");
            String mappingP = XMLUtil.parseTagValue(node, "MAPPINGP");
            String mappingA = XMLUtil.parseTagValue(node, "MAPPINGA");
            String width = XMLUtil.parseTagValue(node, "WIDTH");
            String height = XMLUtil.parseTagValue(node, "HEIGHT");

            mappingP = checkMappingIndex(mappingP);
            mappingA = checkMappingIndex(mappingA);

            spli = new SinglePointLookupInfo(basicID, description, mappingP, mappingA, width, height);

            if (symStd == 0)
                hashMapB.put(basicID, spli);
            else if (symStd == 1)
                hashMapC.put(basicID, spli);

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

            return String.valueOf(i + 57000);
        }
        return index;
    }

    public void writeToBinary(DataOutputStream dos) throws IOException
    {
        dos.writeInt(hashMapB.size());
        for (Map.Entry<String, SinglePointLookupInfo> entry : hashMapB.entrySet()) {
            entry.getValue().writeBinary(dos);
        }
        dos.writeInt(hashMapC.size());
        for (Map.Entry<String, SinglePointLookupInfo> entry : hashMapC.entrySet()) {
            entry.getValue().writeBinary(dos);
        }
    }
}
