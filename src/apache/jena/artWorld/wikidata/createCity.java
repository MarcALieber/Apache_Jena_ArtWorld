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

public class createCity extends ArtistBase
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
        new createCity().setArgs( args ).run();
    }

    @Override
    public void run() {
		boolean result; 
		File file = new File(tempfolder2+"/cities_"+date+".nt");  
		File file2 = new File(tempfolder2+"/cities_isCityOf_"+date+".nt");  
		String dataURL = serverURL+"/"+dsName;
		String queryService = dataURL+"/query";
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
		

		System.out.println("get Wikidata City IDs");
		String queryString ="PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n"
				+ "PREFIX world: <http://www.art.org/ontology/world#>\n"
				+ "PREFIX owl: <http://www.w3.org/2002/07/owl#>\n"
				+ "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n"
				+ "prefix mvmt: <http://www.art.org/ontology/movement#> \n"
				+ "PREFIX wdt: <http://www.wikidata.org/prop/direct/>\n"
				+ "PREFIX wd: <http://www.wikidata.org/entity/>\n"
				+ "PREFIX schema: <http://schema.org/>\n"
				+ "prefix wikipedia: <https://en.wikipedia.org/wiki/> \n"
				+ "PREFIX mus: <http://www.art.org/ontology/museum#>\n"
				+ "CONSTRUCT {?city rdf:type owl:NamedIndividual, world:city ; rdfs:label ?cityName1 ,?cityName2 ;\n"
				+ "           rdfs:comment ?desc1,?desc2 ; owl:sameAs ?wdID ;owl:sameAs ?wpLink  . \n"
				+ "}\n"
				+ "where {\n"
				+ "   {select distinct ?city where {\n"
				+ "     { GRAPH <http://www.art.org/museum> {\n"
				+ "                 ?museum mus:inCity ?city .\n"
				+ "                 FILTER NOT EXISTS { GRAPH <http://www.art.org/world> {?city rdf:type  world:city . }}\n"
				+ "        }}\n"
				+ "    }\n"
				+ "  }\n"
				+ "  BIND(replace(str(?city),\"http://www.art.org/ontology/world/city#\",\"\") as ?itemID )\n"
				+ "  BIND(IRI(CONCAT(\"http://www.wikidata.org/entity/\", ?itemID   ))  AS ?wdID ) .\n"
				+ "  SERVICE <"+ serviceURI +"> {\n"
				+ "    ?wdID wdt:P31 ?city1 ; wdt:P17 ?country .\n"
				+ "        OPTIONAL {?wdID rdfs:label ?cityName1 . FILTER(lang(?cityName1)=\"en\") .}\n"
				+ "        OPTIONAL {?wdID rdfs:label ?cityName2 . FILTER(lang(?cityName2)=\"fr\") .}\n"
				+ "        OPTIONAL {?wdID schema:description ?desc1 . FILTER(lang(?desc1)=\"en\") .  }\n"
				+ "        OPTIONAL {?wdID schema:description ?desc2 . FILTER(lang(?desc2)=\"fr\") .  }\n"
				+ "        OPTIONAL{?wpLink schema:isPartOf <https://en.wikipedia.org/>; schema:about ?wdID . } \n"
				+ "    }\n"
				+ "}";
		
		Query query = QueryFactory.create(queryString, Syntax.syntaxARQ);
        try (QueryExecution qexec =  QueryExecution.service(queryService).query(query).build()) { 
		  
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
        
        queryString ="PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n"
        		+ "PREFIX world: <http://www.art.org/ontology/world#>\n"
        		+ "PREFIX owl: <http://www.w3.org/2002/07/owl#>\n"
        		+ "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n"
        		+ "prefix mvmt: <http://www.art.org/ontology/movement#> \n"
        		+ "PREFIX wdt: <http://www.wikidata.org/prop/direct/>\n"
        		+ "PREFIX wd: <http://www.wikidata.org/entity/>\n"
        		+ "PREFIX schema: <http://schema.org/>\n"
        		+ "prefix wikipedia: <https://en.wikipedia.org/wiki/> \n"
        		+ "PREFIX mus: <http://www.art.org/ontology/museum#>\n"
        		+ "#select distinct ?city ?wdID ?country ?countryID\n"
        		+ "CONSTRUCT {?city world:isCityOf ?countryID .}\n"
        		+ "where {\n"
        		+ "   {select distinct ?city where {\n"
        		+ "     { graph <http://www.art.org/world> { ?city rdf:type   world:city . \n"
        		+ "            FILTER NOT EXISTS { ?city  world:isCityOf ?country . }\n"
        		+ "        }}\n"
        		+ "    }\n"
        		+ "  }\n"
        		+ "  BIND(replace(str(?city),\"http://www.art.org/ontology/world/city#\",\"\") as ?itemID )\n"
        		+ "  BIND(IRI(CONCAT(\"http://www.wikidata.org/entity/\", ?itemID   ))  AS ?wdID ) .\n"
        		+ "  SERVICE <"+ serviceURI +"> { ?wdID wdt:P17 ?country\n"
        		+ "  }\n"
        		+ "  BIND(replace(str(?country),\"http://www.wikidata.org/entity/\",\"\") as ?itemID2 ) .\n"
        		+ "  BIND(IRI(CONCAT(\"http://www.art.org/ontology/world/country#\", ?itemID2   ))  AS ?countryID ) .\n"
        		+ " \n"
        		+ "}";
        query = QueryFactory.create(queryString, Syntax.syntaxARQ);
        try (QueryExecution qexec =  QueryExecution.service(queryService).query(query).build()) { 
		  
    		PrintStream o = new PrintStream(file2);
		    //System.out.println(query);
			System.setOut(o);
			Dataset d = qexec.execConstructDataset();
			RDFDataMgr.write(System.out, d, Lang.TRIG);
			System.setOut(console);
			System.out.println("--> file "+file2+" created" );
		} catch (IOException e) {
			  System.setOut(console);
		      System.out.println("!! An error occurred.");
		      e.printStackTrace();
	    }
        
        		
	}
    
}
