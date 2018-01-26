package armyc2.c2sd.xml.parser;

import java.io.DataOutputStream;
import java.io.IOException;

public interface BinaryWriter {
    void writeToBinary(DataOutputStream dos) throws IOException;
}
