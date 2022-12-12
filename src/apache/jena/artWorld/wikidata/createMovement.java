package apache.jena.artWorld.wikidata;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

import org.apache.jena.query.Dataset;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.Syntax;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;

import apache.jena.artWorld.ArtistBase;

public class createMovement extends ArtistBase
{
    /***********************************/
    /* Constants                       */
    /***********************************/

    /***********************************/
    /* Static variables                */
    /***********************************/
	static long millis = System.currentTimeMillis(); 
    static java.sql.Date date = new java.sql.Date(millis); 
    static PrintStream console = System.out;
    /***********************************/
    /* Instance variables              */
    /***********************************/

    /***********************************/
    /* Constructors                    */
    /***********************************/

    /***********************************/
    /* External signature methods      */
    /***********************************/


    public static void main( String[] args ) {
        new createMovement().setArgs( args ).run();
    }

    @Override
    public void run() {
		boolean result; 
		File file = new File(tempfolder2+"/movement_"+date+".nt");  
		try { result = file.createNewFile();  //creates a new file  
		  if(result)      // test if successfully created a new file  
			{  
			System.out.println("file created "+file.getCanonicalPath()); //returns the path string  
			}  
		  else  
			{
			System.out.println("File already exist at location: "+file.getCanonicalPath());  
			}  
			}   
	    catch (IOException e)   
			{ e.printStackTrace();    //prints exception if any 
			}         

		System.out.println("get Wikidata movement IDs");
		String queryString ="PREFIX art: <http://www.art.org/ontology/art#>\n"
				+ "PREFIX owl: <http://www.w3.org/2002/07/owl#>\n"
				+ "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n"
				+ "prefix mvmt: <http://www.art.org/ontology/movement#> \n"
				+ "PREFIX wdt: <http://www.wikidata.org/prop/direct/>\n"
				+ "PREFIX wd: <http://www.wikidata.org/entity/>\n"
				+ "PREFIX schema: <http://schema.org/>\n"
				+ "prefix wikipedia: <https://en.wikipedia.org/wiki/> \n"
				+ "prefix person: <http://www.art.org/ontology/person#> \n"
				+ "CONSTRUCT {?movement a owl:NamedIndividual, mvmt:movement ; rdfs:label ?mvmtName1 ,?mvmtName2; rdfs:comment ?desc1,?desc2 ; owl:sameAs ?mvmt ;owl:sameAs ?wpLink .  }\n"
				+ "where {\n"
				+ "   {select distinct ?movement where {\n"
				+ "    ?painter person:isPartofMovement ?movement .\n"
				+ "     FILTER NOT EXISTS { GRAPH <http://www.art.org/movement> {?movement rdfs:label ?movementName . }}\n"
				+ "     }\n"
				+ "  }\n"
				+ "   BIND(replace(str(?movement),\"http://www.art.org/ontology/movement#\",\"\") as ?itemID )\n"
				+ "   BIND(IRI(CONCAT(\"http://www.wikidata.org/entity/\", ?itemID   ))  AS ?mvmt ) .\n"
				+ "  {SERVICE <"+serviceURI +"> {?mvmt wdt:P31 wd:Q968159 .\n"
				+ "     OPTIONAL { ?mvmt rdfs:label ?mvmtName1 . FILTER(lang(?mvmtName1)=\"fr\") . } \n"
				+ "     OPTIONAL { ?mvmt schema:description ?desc1 . FILTER(lang(?desc1)=\"fr\") .  }\n"
				+ "     OPTIONAL { ?mvmt rdfs:label ?mvmtName2 . FILTER(lang(?mvmtName2)=\"en\") . } \n"
				+ "     OPTIONAL { ?mvmt schema:description ?desc2 . FILTER(lang(?desc2)=\"en\") .  }\n"
				+ "     OPTIONAL{?wpLink schema:isPartOf <https://en.wikipedia.org/>; schema:about ?mvmt . }\n"
				+ "     }\n"
				+ "  }\n"
				+ "}";

		Query query = QueryFactory.create(queryString, Syntax.syntaxARQ);
        try (QueryExecution qexec =  QueryExecution.service( queryService).query(query).build()) { 
		  
    		PrintStream o = new PrintStream(file);
		    //System.out.println(query);
			System.setOut(o);
			Dataset d = qexec.execConstructDataset();
			RDFDataMgr.write(System.out, d, Lang.TRIG);
			System.setOut(console);
			System.out.println("--> file "+file+" created" );
		} catch (IOException e) {
			  System.setOut(console);
		      System.out.println("!! An error occurred.");
		      e.printStackTrace();
	    }
		
	}
}
