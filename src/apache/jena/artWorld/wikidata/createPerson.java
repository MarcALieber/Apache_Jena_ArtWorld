package apache.jena.artWorld.wikidata;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

import org.apache.jena.query.Dataset;
import org.apache.jena.query.DatasetFactory;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.Syntax;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;

import apache.jena.artWorld.ArtistBase;

public class createPerson extends ArtistBase
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

//wd:Q944834,wd:Q34661
    public static void main( String[] args ) {
    	addOption("painters",args[0],true,null);
    	//addOption("tempfolder",args[1],true,null);
        new createPerson().setArgs( args ).run();
    }

    @Override
    public void run() {
    	String listOfArtist_WD_ID = getOptions().getOption("painters").getLongOpt();
    	//String tempfolder = getOptions().getOption("tempfolder").getLongOpt();
    	System.out.println("---> "+listOfArtist_WD_ID+" into "+tempfolder);
		boolean result; 
		File file = new File(tempfolder+"/person_"+date+".nt");  
		File file2 = new File(tempfolder+"/person_exposed_in_"+date+".nt"); 

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
		
		Model model = ModelFactory.createDefaultModel();
		Dataset dataset = DatasetFactory.create(model);
		
		//String listOfArtists = dsName;
		System.out.println("Wikidata Person IDs : "+listOfArtist_WD_ID);
		String queryString ="PREFIX person: <http://www.art.org/ontology/person#>\n"
        		+ "PREFIX gender: <http://www.art.org/ontology/person/gender#>\n"
        		+ "PREFIX owl: <http://www.w3.org/2002/07/owl#>\n"
        		+ "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n"
        		+ "prefix mvmt: <http://www.art.org/ontology/movement#> \n"
        		+ "PREFIX wdt: <http://www.wikidata.org/prop/direct/>\n"
        		+ "PREFIX wd: <http://www.wikidata.org/entity/>\n"
        		+ "PREFIX schema: <http://schema.org/>\n"
        		+ "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n"
        		+ "prefix mus: <http://www.art.org/ontology/museum#> \n"
        		+ "prefix wikipedia: <https://en.wikipedia.org/wiki/> \n"
        		+ "prefix world: <http://www.art.org/ontology/world#> \n"
        		+ "prefix ctry: <http://www.art.org/ontology/world/country#> \n"
        		+ "prefix city:  <http://www.art.org/ontology/world/city#> \n"
        		+ "CONSTRUCT {?painterID a owl:NamedIndividual ,person:painter ; rdfs:label ?painterName1 ,?painterName2; rdfs:comment ?desc1,?desc2 ;\n"
        		+ "           owl:sameAs ?painter ;owl:sameAs ?wpLink ; person:dateOfBirth ?dateOfBirth ;person:dateOfDeath  ?dateOfDeath; person:hasGender ?genderID ;\n"
        		+ "           person:isPartofMovement ?mvmtID ; person:isCitizenOf ?countryID ; person:familyName ?familyName2 .\n"
        		+ "}\n"
        		+ "WHERE\n"
        		+ "{SERVICE <"+serviceURI +"> \n"
        		+ "{ ?painter wdt:P21 ?sex; wdt:P27 ?country; wdt:P569 ?dateOfBirth \n"
        		+ "    FILTER (?painter in ("+listOfArtist_WD_ID+" )) . \n"
        		+ "  OPTIONAL {?painter wdt:P734 ?name .\n"
        		+ "      OPTIONAL {?name rdfs:label ?familyName . FILTER(lang(?familyName)=\"en\") . }}\n"
        		+ "  OPTIONAL {?painter wdt:P6379 ?museum . }\n"
        		+ "  OPTIONAL { ?painter rdfs:label ?painterName1 . FILTER(lang(?painterName1)=\"en\") . } \n"
        		+ "  OPTIONAL { ?painter rdfs:label ?painterName2 . FILTER(lang(?painterName2)=\"fr\") . } \n"
        		+ "  OPTIONAL { ?painter schema:description ?desc2 . FILTER(lang(?desc2)=\"fr\") .  }\n"
        		+ "  OPTIONAL { ?painter schema:description ?desc1 . FILTER(lang(?desc1)=\"en\") .  }\n"
        		+ "  OPTIONAL{?wpLink schema:isPartOf <https://en.wikipedia.org/>; schema:about ?painter . } \n"
        		+ "    BIND(replace(str(?painter),\"http://www.wikidata.org/entity/\",\"\") as ?itemID )\n"
        		+ "    BIND(IRI(CONCAT(\"http://www.art.org/ontology/person#\", ?itemID   ))  AS ?painterID ) .\n"
        		+ "    BIND(str(?familyName) AS ?familyName2 ) .\n"
        		+ "  OPTIONAL {?painter wdt:P570 ?dateOfDeath . }\n"
        		+ "  OPTIONAL {?painter wdt:P135 ?mvmt . }\n"
        		+ "   BIND(replace(str(?mvmt),\"http://www.wikidata.org/entity/\",\"\") as ?mID )\n"
        		+ "   BIND(IRI(CONCAT(\"http://www.art.org/ontology/movement#\", ?mID   ))  AS ?mvmtID ) .\n"
        		+ "  OPTIONAL { ?sex rdfs:label ?gender . FILTER(lang(?gender)=\"en\") . } \n"
        		+ "  BIND(IRI(CONCAT(\"http://www.art.org/ontology/person/gender#\", ?gender   ))  AS ?genderID ) .\n"
        		+ "  BIND(replace(str(?country),\"http://www.wikidata.org/entity/\",\"\") as ?itemID2 )\n"
        		+ "  BIND(IRI(CONCAT(\"http://www.art.org/ontology/world/country#\", ?itemID2   )) AS ?countryID ) .\n"
        		+ "}\n"
        		+ "}";
		//System.out.println(queryString);    
        Query query = QueryFactory.create(queryString, Syntax.syntaxARQ);
        
		try (QueryExecution qexec = QueryExecutionFactory
				.create(query, dataset)) {
			  
			PrintStream o = new PrintStream(file);
		
			System.setOut(o);
			Dataset d = qexec.execConstructDataset();
			RDFDataMgr.write(System.out, d, Lang.TRIG);
			System.setOut(console);
			System.out.println("--> Successfully wrote "+listOfArtist_WD_ID+" to file "+file );
		} catch (IOException e) {
			  System.setOut(console);
		      System.out.println("!! An error occurred.");
		      e.printStackTrace();
	    }
		queryString = "prefix owl: <http://www.w3.org/2002/07/owl#> \n"
				+ "prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> \n"
				+ "prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#> \n"
				+ "prefix person: <http://www.art.org/ontology/person#> \n"
				+ "PREFIX wdt: <http://www.wikidata.org/prop/direct/>\n"
				+ "PREFIX wd: <http://www.wikidata.org/entity/>\n"
				+ "prefix mus:       <http://www.art.org/ontology/museum#> \n"
				+ "prefix art: <http://www.art.org/ontology/art#> \n"
				+ "CONSTRUCT {?personID art:exposedIn ?museumID }\n"
				+ "where {\n"
				+ "  {SERVICE <"+serviceURI +"> {?painter wdt:P6379 ?museum  FILTER (?painter in ("+listOfArtist_WD_ID+" )) .\n"
				+ "      BIND(replace(str(?museum),\"http://www.wikidata.org/entity/\",\"\") as ?itemID )\n"
				+ "    BIND(IRI(CONCAT(\"http://www.art.org/ontology/museum#\", ?itemID   ))  AS ?museumID ) .\n"
				+ "      BIND(replace(str(?painter),\"http://www.wikidata.org/entity/\",\"\") as ?wdID )\n"
				+ "    BIND(IRI(CONCAT(\"http://www.art.org/ontology/person#\", ?wdID   ))  AS ?personID ) .\n"
				+ "    }\n"
				+ "  }\n"
				+ "}";
		
        query = QueryFactory.create(queryString, Syntax.syntaxARQ);
        
		try (QueryExecution qexec = QueryExecutionFactory
				.create(query, dataset)) {
			  
			PrintStream o = new PrintStream(file2);
			//System.out.println(query);
			System.setOut(o);
			Dataset d = qexec.execConstructDataset();
			RDFDataMgr.write(System.out, d, Lang.TRIG);
			System.setOut(console);
			System.out.println("--> Successfully wrote "+listOfArtist_WD_ID+" to file "+file2 );
		} catch (IOException e) {
			  System.setOut(console);
		      System.out.println("!! An error occurred.");
		      e.printStackTrace();
	    }
		
	}
}
