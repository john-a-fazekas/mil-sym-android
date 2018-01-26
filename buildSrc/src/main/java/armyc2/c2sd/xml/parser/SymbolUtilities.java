/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package armyc2.c2sd.xml.parser;

import java.util.regex.Pattern;

/**
 *
 * @author michael.spinelli
 */
public class SymbolUtilities
{
    //this regex is from: https://docs.oracle.com/javase/7/docs/api/java/lang/Double.html
    private static final String Digits     = "(\\p{Digit}+)";
    private static final String HexDigits  = "(\\p{XDigit}+)";
    // an exponent is 'e' or 'E' followed by an optionally
    // signed decimal integer.
    private static final String Exp        = "[eE][+-]?"+Digits;
    private static final String fpRegex    =
            ("[\\x00-\\x20]*"+  // Optional leading "whitespace"
                    "[+-]?(" + // Optional sign character
                    "NaN|" +           // "NaN" string
                    "Infinity|" +      // "Infinity" string

                    // A decimal floating-point string representing a finite positive
                    // number without a leading sign has at most five basic pieces:
                    // Digits . Digits ExponentPart FloatTypeSuffix
                    //
                    // Since this method allows integer-only strings as input
                    // in addition to strings of floating-point literals, the
                    // two sub-patterns below are simplifications of the grammar
                    // productions from section 3.10.2 of
                    // The Java™ Language Specification.

                    // Digits ._opt Digits_opt ExponentPart_opt FloatTypeSuffix_opt
                    "((("+Digits+"(\\.)?("+Digits+"?)("+Exp+")?)|"+

                    // . Digits ExponentPart_opt FloatTypeSuffix_opt
                    "(\\.("+Digits+")("+Exp+")?)|"+

                    // Hexadecimal strings
                    "((" +
                    // 0[xX] HexDigits ._opt BinaryExponent FloatTypeSuffix_opt
                    "(0[xX]" + HexDigits + "(\\.)?)|" +

                    // 0[xX] HexDigits_opt . HexDigits BinaryExponent FloatTypeSuffix_opt
                    "(0[xX]" + HexDigits + "?(\\.)" + HexDigits + ")" +

                    ")[pP][+-]?" + Digits + "))" +
                    "[fFdD]?))" +
                    "[\\x00-\\x20]*");// Optional trailing "whitespace"

    private static final Pattern pIsNumber = Pattern.compile(fpRegex);

    /**
     * Determines if a String represents a valid number
     *
     * @param text
     * @return "1.56" == true, "1ab" == false
     */
    public static boolean isNumber(String text)
    {
        return pIsNumber.matcher(text).matches();
    }

    /**
     * Symbols that don't exist outside of MCS
     *
     * @param sd
     * @return
     */
    public static
            boolean isMCSSpecificTacticalGraphic(SymbolDef sd)
    {
        if (sd.getHierarchy().startsWith("2.X.7") || //Engineering Overlay graphics (ESRI----)
                sd.getHierarchy().startsWith("2.X.5.2.3") || //Route Critical Points
                sd.getBasicSymbolId().startsWith("G*R*") || //Route Critical Points
                sd.getHierarchy().startsWith("21.X") || //JCID (21.X)
                sd.getBasicSymbolId().startsWith("G*E*"))//MCS Eng (20.X)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    /**
     * Symbols that don't exist outside of MCS or units that are no longer
     * supported like those from the SASO Proposal.
     *
     * @param ud
     * @return
     */
    public static
            boolean isMCSSpecificForceElement(UnitDef ud)
    {
        if (isSASO(ud))//SASO
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    /**
     * Symbols from the SASO Proposal. Most were replaced by the USAS 13-14
     * update or 2525C.
     *
     * @param sd
     * @return
     */
    public static
    boolean isSASO(UnitDef sd)
    {
        if (sd.getHierarchy().startsWith("5.X.10") || //SASOP Individuals
                sd.getHierarchy().startsWith("5.X.11") || //SASOP Organization/groups
                sd.getHierarchy().startsWith("5.X.12") ||//SASOP //replaced by USAS 13-14 update
                sd.getHierarchy().startsWith("5.X.13") || //SASOP Structures
                sd.getHierarchy().startsWith("5.X.14")) //SASOP Equipment/Weapons
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    /**
     * @name isInstallation Warfighting ground installations. They are always
     * installations.
     * @desc Returns true if the symbol id is an installation (S*G*I).
     *
     * @param strSymbolID - IN - A MilStd2525B symbolID
     * @return True if symbol is an Installation, false otherwise.
     */
    public static boolean isInstallation(String strSymbolID)
    {
        try
        {
            boolean blRetVal = false;
            if(strSymbolID.charAt(0)=='S')
                blRetVal = ((strSymbolID.charAt(2) == 'G') && (strSymbolID.charAt(4) == 'I'));
            else if((strSymbolID.charAt(0)=='E'))
                blRetVal = isEMSInstallation(strSymbolID);
            return blRetVal;
        }
        catch (Throwable t)
        {
            System.out.println(t);
        }
        return false;
    }

    /**
     * Determines if a symbol is an EMS Installation
     * @param strSymbolID
     * @return
     */
    public static boolean isEMSInstallation(String strSymbolID)
    {
        boolean blRetVal = false;
        try
        {
            if(strSymbolID.charAt(0)=='E')
            {
                if(strSymbolID.charAt(2)=='O' &&
                        strSymbolID.charAt(4)=='D' && (strSymbolID.charAt(6)=='C' || strSymbolID.charAt(5)=='K'))
                {
                    blRetVal = true;
                }
                else if(strSymbolID.charAt(2)=='F' &&
                        strSymbolID.substring(4, 6).equals("BA")==false)
                {
                    blRetVal = true;
                }
                else if(strSymbolID.charAt(2)=='O')
                {
                    if(strSymbolID.charAt(4)=='A')
                    {
                        switch(strSymbolID.charAt(5))
                        {
                            case 'C':
                            case 'D':
                            case 'G':
                            case 'J':
                            case 'K':
                            case 'L':
                            case 'M':
                                blRetVal = true;
                                break;
                            default:
                                break;
                        }
                    }
                    else if(strSymbolID.charAt(4)=='B')
                    {
                        switch(strSymbolID.charAt(5))
                        {
                            case 'C':
                            case 'E':
                            case 'F':
                            case 'G':
                            case 'H':
                            case 'I':
                            case 'K':
                            case 'L':
                                blRetVal = true;
                                break;
                            default:
                                break;
                        }
                    }
                    else if(strSymbolID.charAt(4)=='C')
                    {
                        switch(strSymbolID.charAt(5))
                        {
                            case 'D':
                            case 'E':
                                blRetVal = true;
                                break;
                            default:
                                break;
                        }
                    }
                }
            }
        }
        catch(Throwable t)
        {
            System.out.println(t);
        }
        return blRetVal;
    }
}
