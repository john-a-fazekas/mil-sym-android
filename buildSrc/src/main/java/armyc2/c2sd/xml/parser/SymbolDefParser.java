package armyc2.c2sd.xml.parser;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SymbolDefParser implements BinaryWriter {
    private static Map<String, SymbolDef> _SymbolDefinitionsB = null;
    private static ArrayList<SymbolDef> _SymbolDefDupsB = null;

    private static Map<String, SymbolDef> _SymbolDefinitionsC = null;
    private static ArrayList<SymbolDef> _SymbolDefDupsC = null;

    public final synchronized void init(String[] symbolConstantsXML)
    {
            _SymbolDefinitionsB = new HashMap<>();
            _SymbolDefDupsB = new ArrayList<>();

            _SymbolDefinitionsC = new HashMap<>();
            _SymbolDefDupsC = new ArrayList<>();
            String lookupXmlB = symbolConstantsXML[0];// FileHandler.InputStreamToString(xmlStreamB);
            String lookupXmlC = symbolConstantsXML[1];
            ;// FileHandler.InputStreamToString(xmlStreamC);
            populateLookup(lookupXmlB, 0);
            populateLookup(lookupXmlC, 1);
    }

    private void populateLookup(String xml, int symStd)
    {
        Document document = XMLParser.getDomElement(xml);

        SymbolDef sd = null;
        NodeList symbols = XMLUtil.getItemList(document, "SYMBOL");
        for (int i = 0; i < symbols.getLength(); i++) {
            Node node = symbols.item(i);

            String symbolID = XMLUtil.parseTagValue(node, "SYMBOLID");
            String geometry = XMLUtil.parseTagValue(node, "GEOMETRY");
            String drawCategory = XMLUtil.parseTagValue(node, "DRAWCATEGORY");
            String maxpoints = XMLUtil.parseTagValue(node, "MAXPOINTS");
            String minpoints = XMLUtil.parseTagValue(node, "MINPOINTS");
            String modifiers = XMLUtil.parseTagValue(node, "MODIFIERS");
            String description = XMLUtil.parseTagValue(node, "DESCRIPTION");
            description = description.replaceAll("&amp;", "&");
            String hierarchy = XMLUtil.parseTagValue(node, "HIERARCHY");
            String path = XMLUtil.parseTagValue(node, "PATH");

            sd = new SymbolDef(symbolID, description, Integer.valueOf(drawCategory), hierarchy,
                    Integer.valueOf(minpoints), Integer.valueOf(maxpoints), modifiers, path);

            boolean isMCSSpecific = SymbolUtilities.isMCSSpecificTacticalGraphic(sd);
            if (symStd == 0) {
                if (_SymbolDefinitionsB.containsKey(symbolID) == false && isMCSSpecific == false) {
                    _SymbolDefinitionsB.put(symbolID, sd);
                } else if (isMCSSpecific == false) {
                    _SymbolDefDupsB.add(sd);
                }
            } else if (symStd == 1) {
                if (_SymbolDefinitionsC.containsKey(symbolID) == false && isMCSSpecific == false) {
                    _SymbolDefinitionsC.put(symbolID, sd);
                } else if (isMCSSpecific == false) {
                    _SymbolDefDupsC.add(sd);
                }
            }
        }
    }

    public void writeToBinary(DataOutputStream dos) throws IOException
    {
        dos.writeInt(_SymbolDefinitionsB.size());
        for (Map.Entry<String, SymbolDef> entry : _SymbolDefinitionsB.entrySet()) {
            entry.getValue().writeBinary(dos);
        }
        dos.writeInt(_SymbolDefDupsB.size());
        for (SymbolDef entry : _SymbolDefDupsB) {
            entry.writeBinary(dos);
        }

        dos.writeInt(_SymbolDefinitionsC.size());
        for (Map.Entry<String, SymbolDef> entry : _SymbolDefinitionsC.entrySet()) {
            entry.getValue().writeBinary(dos);
        }
        dos.writeInt(_SymbolDefDupsC.size());
        for (SymbolDef entry : _SymbolDefDupsC) {
            entry.writeBinary(dos);
        }
    }
}
