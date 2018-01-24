package armyc2.c2sd.xml.parser;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


public class BinaryGeneratorHelper {

    public static void generate(File resourceDir, File outputDir) throws IOException {
        String unitcontantsb = getXML(resourceDir, "unitconstantsb.xml");
        String unitconstantsc = getXML(resourceDir, "unitconstantsc.xml");
        String symbolconstantsb = getXML(resourceDir, "symbolconstantsb.xml");
        String singlepointmappingsb = getXML(resourceDir, "singlepointb.xml");
        String symbolconstantsc = getXML(resourceDir, "symbolconstantsc.xml");
        String singlepointmappingsc = getXML(resourceDir, "singlepointc.xml");
        String tacticalgraphics = getXML(resourceDir, "tacticalgraphics.xml");
        String unitfontmappingsb = getXML(resourceDir, "unitfontmappingsb.xml");
        String unitfontmappingsc = getXML(resourceDir, "unitfontmappingsc.xml");

        String[] unitConstants = new String[] { unitcontantsb, unitconstantsc };
        String[] unitMappings = new String[] { unitfontmappingsb, unitfontmappingsc };
        String[] symbolConstants = new String[] { symbolconstantsb, symbolconstantsc };
        String[] symbolMappings = new String[] { singlepointmappingsb, singlepointmappingsc };

        UnitDefParser unitDefParser = new UnitDefParser();
        unitDefParser.init(unitConstants);

        UnitFontParser unitFontParser = new UnitFontParser();
        unitFontParser.init(unitMappings);

        SymbolDefParser symbolDefParser = new SymbolDefParser();
        symbolDefParser.init(symbolConstants);

        SinglePointParser singlePointParser = new SinglePointParser();
        singlePointParser.init(symbolMappings);

        TacticalGraphicParser tacticalGraphicParser = new TacticalGraphicParser();
        tacticalGraphicParser.init(tacticalgraphics);

        writeStream(outputDir, "unitconstants.bin", unitDefParser);
        writeStream(outputDir, "symbolconstants.bin", symbolDefParser);
        writeStream(outputDir, "unitfontmappings.bin", unitFontParser);
        writeStream(outputDir, "singlepoint.bin", singlePointParser);
        writeStream(outputDir, "tacticalgraphic.bin", tacticalGraphicParser);
    }

    private static void writeStream(File outputDir, String name, BinaryWriter writer) throws IOException {
        try (DataOutputStream stream = getBinaryStream(outputDir, name)) {
            writer.writeToBinary(stream);
        }
    }

    private static String getXML(File resourceDir, String xmlName) throws IOException {
        return XMLUtility.getXML(new File(resourceDir, xmlName));
    }


    private static DataOutputStream getBinaryStream(File outputDir, String name) throws FileNotFoundException {
        return new DataOutputStream(new FileOutputStream(new File(outputDir, name)));
    }
}
