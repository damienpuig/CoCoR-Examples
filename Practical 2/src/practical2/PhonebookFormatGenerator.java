package practical2;

import java.io.FileNotFoundException;
import java.io.PrintStream;

/**
 * @author Damien
 */

public class PhonebookFormatGenerator {

    PrintStream s;

    public PhonebookFormatGenerator(String fileName) throws FileNotFoundException {
        s = new PrintStream(fileName);
    }

    /*-- Methods used in grammar.atg to print nodes --*/

    public void PrintLastName(String lastname) {
        s.print(lastname);
    }
    
    public void PrintFirstName(String firstname, String secondName, boolean isPeriod){
        s.print(", " + firstname + " " + secondName );
        if(isPeriod) s.print(".");
        NewLine();
    }
    
    public void PrintPhoneType(String phoneType){
        if(phoneType == "") phoneType = "home";
        s.print("\t" + phoneType + ": ");
    }
    
    public void PrintCountryCode(String countryCodeFirst){
        s.print("+" + countryCodeFirst);
    }
    
    public void CheckCityCode(String num, boolean isCountryCode, boolean isCityCode){
        if(isCountryCode == false && isCityCode == false && num.charAt(0) == '0'){
            StringBuffer buffer = new StringBuffer(num);
            buffer.insert(0, '(');
            buffer.insert(2, ')');
            num = buffer.toString();
            
            PrintNumber(num, false, true);
        }
        else
            PrintNumber(num, isCountryCode, isCityCode);
    }
    
    public void PrintNumber(String num, boolean isCountryCode, boolean isCityCode){
        if( isCountryCode == false ) s.print("+44 ");
        if( isCityCode == false) s.print("(0)20 ");
        s.print(num + " ");
    }
    
    public void PrintZero(){
        s.print(" (0)");
    }
    
    public void NewLine(){
        s.println("\n");
    }
    
}
