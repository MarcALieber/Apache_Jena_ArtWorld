package apache.jena.artWorld.util;
import java.awt.Desktop;
import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.DocumentBuilder;  
import org.w3c.dom.Document;  
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import apache.jena.artWorld.ArtistBase;

import org.w3c.dom.Node;  
import org.w3c.dom.Element;  
//import java.io.File;  

public class generateHTMLPage extends ArtistBase
{
    /***********************************/
    /* Constants                       */
    /***********************************/

    /***********************************/
    /* Static variables                */
    /***********************************/
	static String folder ="scripts/";
	static File file = new File("data/painters/sparql_queries.xml");  
	static long millis = System.currentTimeMillis(); 
    static java.sql.Date date = new java.sql.Date(millis); 
    /***********************************/
    /* Instance variables              */
    /***********************************/

    /***********************************/
    /* Constructors                    */
    /***********************************/

    /***********************************/
    /* External signature methods      */
    /***********************************/


	// Method to encode a string value using `UTF-8` encoding scheme
    private static String encodeValue(String value) {
        try {
            return URLEncoder.encode(value, StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException ex) {
            throw new RuntimeException(ex.getCause());
        }
    }
    
    public static void main( String[] args ) {
        new generateHTMLPage().setArgs( args ).run();
    }

    @Override
    public void run() {
	        //File f = new File(folder+"source.htm");
	        File f = new File(folder+"/sparqlQueries_"+date+".html");  
	        BufferedWriter bw;
			try {
				bw = new BufferedWriter(new FileWriter(f));
	        String query ;
	        String encodedQuery;  
	        String completeUrl;
	        bw.write("<!DOCTYPE html>\n"
	        		+ "<html>\n"
	        		+ "<head>\n"
	        		+ "<style>\n"
	        		+ "table {\n"
	        		+ "  border-collapse: collapse;\n"
	        		+ "  width: 100%;\n"
	        		+ "}\n"
	        		+ "\n"
	        		+ "th, td {\n"
	        		+ "  text-align: left;\n"
	        		+ "  padding: 8px;\n"
	        		+ "}\n"
	        		+ "\n"
	        		+ "tr:nth-child(even) {\n"
	        		+ "  background-color: #D6EEEE;\n"
	        		+ "}\n"
	        		+ "</style>\n"
	        		+ "<title>Fuseki ArtWorld SPARQL</title>\n"
	        		+ "</head>\n"
	        		+ "<body>\n");
	        bw.write("<h1>Jena TDB ArtWorld SPARQL Query examples</h1>\n");
	        
	        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();  
		    //an instance of builder to parse the specified xml file  
		    DocumentBuilder db = dbf.newDocumentBuilder();  
		    Document doc = db.parse(file);  
		    doc.getDocumentElement().normalize();  
		    NodeList nodeList = doc.getElementsByTagName("query");  
		    bw.write("<table border=\"1\">\n"
		    		+ "  <tr>\n"
		    		+ "    <th>ID</th>\n"
		    		+ "    <th>description</th>\n"
		    		+ "    <th>comment</th>\n"
		    		+ "    <th>Query text</th>\n"
		    		+ "    <th>Query type</th>\n"
		    		+ "  </tr>");
			
			
		    try   
		    {  
		    for (int itr = 0; itr < nodeList.getLength(); itr++)   
		    {  
		    Node node = nodeList.item(itr);  
		    System.out.println("\nNode Name :" + node.getNodeName()+" "+itr );  
		    if (node.getNodeType() == Node.ELEMENT_NODE)   
		    {  
		    Element eElement = (Element) node;  
		    bw.write("<tr>");
		    query = eElement.getElementsByTagName("querytext").item(0).getTextContent();
		    encodedQuery = encodeValue(query); // Encoding a query string
	        completeUrl = baseUrl + encodedQuery;
	        //
	        bw.write("<td><a href=\""+completeUrl+"\">"+eElement.getAttribute("queryID")+"</a></td>\n");
		    //bw.write("<td><a href=\""+completeUrl+"\">"+eElement.getElementsByTagName("id").item(0).getTextContent()+"</a></td>\n");
		    bw.write("<td>"+eElement.getElementsByTagName("description").item(0).getTextContent()+"</td>\n");
		    bw.write("<td>"+eElement.getElementsByTagName("comment").item(0).getTextContent()+"</td>\n");
		    bw.write("<td><textarea cols=105 rows=10>"+query+"</textarea></td>\n");
		    bw.write("<td>"+eElement.getAttribute("type")+"</td>\n");
		    bw.write("</tr>\n");
		    }  
		    }  
		    }   
		    catch (Exception e)   
		    {  
		    e.printStackTrace();  
		    }  
		    bw.write("</table><br/>\n");
	        bw.write("</body></html>\n");
	        bw.close();
	        Desktop.getDesktop().browse(f.toURI());
			} catch (IOException | ParserConfigurationException | SAXException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

	        
	    }
	}
