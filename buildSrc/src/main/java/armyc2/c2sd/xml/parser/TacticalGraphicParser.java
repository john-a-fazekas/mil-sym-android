package armyc2.c2sd.xml.parser;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class TacticalGraphicParser implements BinaryWriter {
    private Map<String, Integer> symbolMap = new HashMap<String, Integer>();

    /**
     * @name init
     *
     * @desc Simply calls xmlLoaded
     *
     * @return None
     */
    public synchronized void init(String xml)
    {
        populateLookup(xml);
    }

    /**
     * @name populateLookup
     *
     * @desc
     *
     * @param xml - IN -
     * @return None
     */
    private void populateLookup(String xml)
    {
        Document document = XMLParser.getDomElement(xml);

        NodeList symbols = XMLUtil.getItemList(document, "SYMBOL");
        for (int i = 0; i < symbols.getLength(); i++) {
            Node node = symbols.item(i);

            String basicID = XMLUtil.parseTagValue(node, "SYMBOLID");
            // String description = XMLUtil.parseTagValue(data, "<DESCRIPTION>", "</DESCRIPTION>");
            String mapping = XMLUtil.parseTagValue(node, "MAPPING");

            mapping = checkMappingIndex(mapping);

            symbolMap.put(basicID, Integer.valueOf(mapping));

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
        dos.writeInt(symbolMap.size());
        for (Map.Entry<String, Integer> entry : symbolMap.entrySet()) {
            dos.writeUTF(entry.getKey());
            dos.writeBoolean(entry.getValue() != null);
            if (entry.getValue() != null)
                dos.writeInt(entry.getValue());
        }
    }
}
