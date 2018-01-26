/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package armyc2.c2sd.xml.parser;

import java.io.DataOutputStream;
import java.io.IOException;

/**
*
* @author michael.spinelli
*/
public class UnitDef {

   private String _basicSymbolId = "";
   private String _description = "";
   private int _drawCategory = 0;
   private String _hierarchy = "";
   private String _path = "";

   public UnitDef(String basicSymbolID, String description, int drawCategory, String hierarchy, String path)
   {
           //Set fields to their default values.
           _basicSymbolId = basicSymbolID;
           _description = description;
           _drawCategory = drawCategory;
           _hierarchy = hierarchy;
           _path = path;
   }

   /**
    * Defines where the symbol goes in the ms2525 hierarchy.
    * 2.X.whatever
    */
   public String getHierarchy()
   {
           return _hierarchy;
   }

   public void writeBinary(DataOutputStream dos) throws IOException {
       dos.writeUTF(_basicSymbolId);
       dos.writeUTF(_description);
       dos.writeInt(_drawCategory);
       dos.writeUTF(_hierarchy);
       dos.writeUTF(_path);
   }
}
