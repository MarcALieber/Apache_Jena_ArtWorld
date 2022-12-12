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

public class createMuseum extends ArtistBase
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
        new createMuseum().setArgs( args ).run();
    }

    @Override
    public void run() {
		boolean result; 
		File file = new File(tempfolder2+"/museum_"+date+".nt");  
		File file2 = new File(tempfolder2+"/museum_inCity_"+date+".nt");
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

		System.out.println("get Wikidata Museum IDs");
		String queryString ="PREFIX art: <http://www.art.org/ontology/art#>\n"
				+ "PREFIX owl: <http://www.w3.org/2002/07/owl#>\n"
				+ "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n"
				+ "prefix art: <http://www.art.org/ontology/art#> \n"
				+ "PREFIX wdt: <http://www.wikidata.org/prop/direct/>\n"
				+ "PREFIX wikidata: <http://www.wikidata.org/entity/>\n"
				+ "PREFIX schema: <http://schema.org/>\n"
				+ "prefix mus: <http://www.art.org/ontology/museum#> \n"
				+ "prefix wikipedia: <https://en.wikipedia.org/wiki/> \n"
				+ "prefix person: <http://www.art.org/ontology/person#> \n"
				+ "CONSTRUCT {?museum a mus:museum , owl:NamedIndividual , ?collectionType ; rdfs:label ?museumName1 ,?museumName2; rdfs:comment ?desc1,?desc2 ;\n"
				+ "    owl:sameAs ?wdID ;owl:sameAs ?wpLink  . }\n"
				+ "where {\n"
				+ "  {select distinct ?museum where {\n"
				+ "  {?painter art:exposedIn ?museum . }\n"
				+ "   UNION\n"
				+ "  {GRAPH <http://www.art.org/artefact> {?painting art:location ?museum }}\n"
				+ "  FILTER NOT EXISTS { GRAPH <http://www.art.org/museum> {?museum rdfs:label ?museumName . }}\n"
				+ "    }\n"
				+ "  }\n"
				+ "  BIND(replace(str(?museum),\"http://www.art.org/ontology/museum#\",\"\") as ?itemID ) .\n"
				+ "  BIND(IRI(CONCAT(\"http://www.wikidata.org/entity/\", ?itemID   ))  AS ?wdID ) .\n"
				+ "  { SERVICE <"+ serviceURI +">  {\n"
				+ "        ?wdID wdt:P31 ?collectionType . \n"
				+ "        OPTIONAL { ?wdID rdfs:label ?museumName1 . FILTER(lang(?museumName1)=\"en\") . } \n"
				+ "        OPTIONAL { ?wdID rdfs:label ?museumName2 . FILTER(lang(?museumName2)=\"fr\") . } \n"
				+ "        OPTIONAL { ?wdID schema:description ?desc2 . FILTER(lang(?desc2)=\"fr\") .  }\n"
				+ "        OPTIONAL { ?wdID schema:description ?desc1 . FILTER(lang(?desc1)=\"en\") .  }\n"
				+ "        OPTIONAL{?wpLink schema:isPartOf <https://en.wikipedia.org/>; schema:about ?wdID . } \n"
				+ "    }}\n"
				+ "}\n"
				+ "";
		
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
        
        System.out.println("Add mus:inCity ");
        queryString ="PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n"
        		+ "PREFIX art: <http://www.art.org/ontology/art#>\n"
        		+ "PREFIX owl: <http://www.w3.org/2002/07/owl#>\n"
        		+ "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n"
        		+ "prefix mvmt: <http://www.art.org/ontology/movement#> \n"
        		+ "PREFIX wdt: <http://www.wikidata.org/prop/direct/>\n"
        		+ "PREFIX wd: <http://www.wikidata.org/entity/>\n"
        		+ "PREFIX schema: <http://schema.org/>\n"
        		+ "prefix wikipedia: <https://en.wikipedia.org/wiki/> \n"
        		+ "PREFIX mus: <http://www.art.org/ontology/museum#>\n"
        		+ "CONSTRUCT {?museum mus:inCity ?cityID . }\n"
        		+ "where {\n"
        		+ "   {select distinct ?museum where {\n"
        		+ "     { GRAPH <http://www.art.org/museum> {\n"
        		+ "                 ?museum rdf:type ?typeMuseum .\n"
        		+ "                 FILTER NOT EXISTS { ?museum mus:inCity ?city . }\n"
        		+ "        }}\n"
        		+ "    }\n"
        		+ "  }\n"
        		+ "  BIND(replace(str(?museum),\"http://www.art.org/ontology/museum#\",\"\") as ?itemID )\n"
        		+ "  BIND(IRI(CONCAT(\"http://www.wikidata.org/entity/\", ?itemID   ))  AS ?museumID ) .\n"
        		+ "{ SERVICE <"+ serviceURI +">  {?museumID wdt:P131 ?city .\n"
        		+ "     ?city wdt:P31 ?loc FILTER(?loc in (wd:Q5119 ,wd:Q515,wd:Q1549591,wd:Q484170,wd:Q1637706,wd:Q2039348,wd:Q532,wd:Q200250,wd:Q1093829,wd:Q133442 )) .\n"
        		+ "      \n"
        		+ "     }\n"
        		+ "  }\n"
        		+ "  BIND(replace(str(?city),\"http://www.wikidata.org/entity/\",\"\") as ?itemID2 ) .\n"
        		+ "  BIND(IRI(CONCAT(\"http://www.art.org/ontology/world/city#\", ?itemID2   )) AS ?cityID ) .\n"
        		+ "}\n"
        		+ "";
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
