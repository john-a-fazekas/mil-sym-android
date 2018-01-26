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
public class SymbolDef {

   private String _strBasicSymbolId = "";
   private String _strDescription = "";
   private String _strSymbolType = "";
   private String _strGeometry = "";
   private String _strDrawCategory = "";
   private int _intDrawCategory = 99;
   private int _intMinPoints = 0;
   private int _intMaxPoints = 0;
   private String _strModifiers = "";
   private String _strHierarchy = "";
   private String _strFullPath = "";


   public SymbolDef(String basicSymbolID, String description, int drawCategory, String hierarchy,
                    int minPoints, int maxPoints, String modifiers, String fullPath)
   {
	   _strBasicSymbolId = basicSymbolID;
	   _strDescription = description;
	   _intDrawCategory = drawCategory;
	   _strHierarchy = hierarchy;
	   _intMinPoints = minPoints;  
	   _intMaxPoints = maxPoints;
	   _strModifiers = modifiers;
	   _strFullPath = fullPath;
	      
   }

   /**
    * The basic 15 character basic symbol Id.
    */

   public String getBasicSymbolId()
   {
           return _strBasicSymbolId;
   }

   /**
    * Defines where the symbol goes in the ms2525 hierarchy.
    * 2.X.etc...
    */
   public String getHierarchy()
   {
           return _strHierarchy;
   }

   public void writeBinary(DataOutputStream dos) throws IOException {
       dos.writeUTF(_strBasicSymbolId);
       dos.writeUTF(_strDescription);
       dos.writeUTF(_strSymbolType);
       dos.writeUTF(_strGeometry);
       dos.writeUTF(_strDrawCategory);
       dos.writeInt(_intDrawCategory);
       dos.writeInt(_intMinPoints);
       dos.writeInt(_intMaxPoints);
       dos.writeUTF(_strModifiers);
       dos.writeUTF(_strHierarchy);
       dos.writeUTF(_strFullPath);
   }
}
