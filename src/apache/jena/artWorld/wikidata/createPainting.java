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

public class createPainting extends ArtistBase
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
    	addOption("painters",args[0],true,null);
        new createPainting().setArgs( args ).run();
    }

    @Override
    public void run() {
    	String listOfArtist_WD_ID = getOptions().getOption("painters").getLongOpt();
    	System.out.println("Paintings ---> "+listOfArtist_WD_ID+" into "+tempfolder);
		boolean result; 
		File file = new File(tempfolder+"/artefact_"+date+".nt");  

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
		String queryString ="prefix art: <http://www.art.org/ontology/art#> \n"
				+ "prefix mat: <http://www.art.org/ontology/art/material#> \n"
				+ "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n"
				+ "PREFIX person: <http://www.art.org/ontology/person#>\n"
				+ "PREFIX owl: <http://www.w3.org/2002/07/owl#>\n"
				+ "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n"
				+ "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n"
				+ "prefix mvmt: <http://www.art.org/ontology/movement#> \n"
				+ "PREFIX wdt: <http://www.wikidata.org/prop/direct/>\n"
				+ "PREFIX wd: <http://www.wikidata.org/entity/>\n"
				+ "PREFIX schema: <http://schema.org/>\n"
				+ "prefix mus: <http://www.art.org/ontology/museum#> \n"
				+ "prefix wikipedia: <https://en.wikipedia.org/wiki/> \n"
				+ "CONSTRUCT {?artID rdf:type art:painting, owl:NamedIndividual  ; art:createdBy ?painterID ; art:inception ?inception ; \n"
				+ "            rdfs:label ?itemName1 ,?itemName2; rdfs:comment ?desc1,?desc2 ;\n"
				+ "            art:height ?height ; art:width ?width ; art:pictureLink ?pic ; art:madeOf ?materialID ;\n"
				+ "            owl:sameAs ?item ; owl:sameAs ?wpLink ; art:inMuseum ?museumID . \n"
				+ "           #?painter person:isCreatorOf ?artID .\n"
				+ "}\n"
				+ "WHERE\n"
				+ "{SERVICE <"+serviceURI +"> {\n"
				+ "  ?item wdt:P31 wd:Q3305213  .  \n"
				+ "  ?item wdt:P170 ?painter .\n"
				+ "    FILTER (?painter in ("+listOfArtist_WD_ID+" )) . \n"
				+ "  ?item wdt:P18 ?pic ; \n"
				+ "  OPTIONAL { ?item rdfs:label ?itemName1 . FILTER(lang(?itemName1)=\"en\") . } \n"
				+ "  OPTIONAL { ?item rdfs:label ?itemName2 . FILTER(lang(?itemName2)=\"fr\") . } \n"
				+ "  OPTIONAL { ?item schema:description ?desc2 . FILTER(lang(?desc2)=\"fr\") .  }\n"
				+ "  OPTIONAL { ?item schema:description ?desc1 . FILTER(lang(?desc1)=\"en\") .  }\n"
				+ "  OPTIONAL { ?item wdt:P571 ?inceptiond  . \n"
				+ "             BIND(YEAR(?inceptiond)  AS ?inception ) .\n"
				+ "           } # art:inception\n"
				+ "  #?item wdt:P195 ?museum  FILTER EXISTS {?museum wdt:P31 ?type } . \n"
				+ "  OPTIONAL{?wpLink schema:isPartOf <https://en.wikipedia.org/>; schema:about ?item . } \n"
				+ "   { ?item wdt:P276 ?museum . }\n"
				+ "  OPTIONAL { ?item wdt:P2048 ?height . }  #art:height \n"
				+ "  OPTIONAL { ?item wdt:P2049 ?width . }\n"
				+ "  OPTIONAL { ?item wdt:P186 ?material . # ?material is missing in ontology oilPainting \n"
				+ "             ?material wdt:P279 wd:Q174219 .\n"
				+ "           }\n"
				+ "  }\n"
				+ "   BIND(replace(str(?item),\"http://www.wikidata.org/entity/\",\"\") as ?pID )\n"
				+ "   BIND(IRI(CONCAT(\"http://www.art.org/ontology/art#\", ?pID   ))  AS ?artID ) .\n"
				+ "   BIND(replace(str(?museum),\"http://www.wikidata.org/entity/\",\"\") as ?mID )\n"
				+ "   BIND(IRI(CONCAT(\"http://www.art.org/ontology/museum#\", ?mID   ))  AS ?museumID ) .\n"
				+ "   BIND(replace(str(?painter),\"http://www.wikidata.org/entity/\",\"\") as ?wdID )\n"
				+ "   BIND(IRI(CONCAT(\"http://www.art.org/ontology/person#\", ?wdID   ))  AS ?painterID ) .\n"
				+ "   BIND(replace(str(?material),\"http://www.wikidata.org/entity/\",\"\") as ?matID )\n"
				+ "   BIND(IRI(CONCAT(\"http://www.art.org/ontology/art/material#\", ?matID   ))  AS ?materialID ) .\n"
				+ "}";
		
        Query query = QueryFactory.create(queryString, Syntax.syntaxARQ);
        
		try (QueryExecution qexec = QueryExecutionFactory
				.create(query, dataset)) {
			  
			PrintStream o = new PrintStream(file);
		//System.out.println(query);
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
    	
    }
    
}
