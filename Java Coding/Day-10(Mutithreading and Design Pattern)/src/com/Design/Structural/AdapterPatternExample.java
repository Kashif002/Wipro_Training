package com.Design.Structural;


//if you have created an application with some legacy features but those will be compatible with new features or requirement
/*for eg: you have legace xml parser but in another system it only accepts JSON input  so instead of rewriting the XML parser again, we 
 * create an adapter ( which is like a bridge in between) that wraps the XML parser and provides a jSON like interface */
// Converts the interface of one class into another interface as per the client request or expectations, It's like a bridge between incompatible classes 
// Arrays.asList() --  adapts an array to a List , JDBC drivers -- different databases are adapted to a common JDBC interface
interface XMLReader {
    void readXML();
}

interface TxtReader {
    void readTxt();
}

class JSONReader {
    public void readJSON() {
        System.out.println("Reading JSON Data");
    }
}

class TxtFileReader {
    public void readTxt() {
        System.out.println("Reading Txt Data");
    }
}

class JSONtoAnyDataAdapter implements XMLReader, TxtReader {
    private JSONReader jsonReader;
    private TxtFileReader txtReader;

    public JSONtoAnyDataAdapter(JSONReader reader) {
        this.jsonReader = reader;
    }

    public JSONtoAnyDataAdapter(TxtFileReader reader) {
        this.txtReader = reader;
    }

    @Override
    public void readXML() {
        System.out.println("Converting XML to JSON...");
        jsonReader.readJSON();
    }

    @Override
    public void readTxt() {
        System.out.println("Converting XML to Txt...");
        txtReader.readTxt();
    }
}

public class AdapterPatternExample {


	    public static void main(String[] args) {
	        XMLReader xmlReader = new JSONtoAnyDataAdapter(new JSONReader());
	        xmlReader.readXML();
	        TxtReader txtReader = new JSONtoAnyDataAdapter(new TxtFileReader());
	        txtReader.readTxt();
	    }

}