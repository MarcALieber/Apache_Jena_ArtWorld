package apache.jena.artWorld.check;

import org.apache.jena.query.Query;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.ResultSetFormatter;
import org.apache.jena.rdfconnection.RDFConnection;

import apache.jena.artWorld.ArtistBase;

public class checkMissingLabel extends ArtistBase {
	 public static void main( String[] args ) {
		    System.out.println("Show result for "+dsName);
	        new checkMissingLabel().setArgs( args ).run();
	    }
	 
	 @Override
	 public void run() {
		    System.out.println("call "+queryService);
		    System.out.println("Verify if every indivual has a label in english");
		    // todo : if label other than @en or @fr then report
	        Query query = QueryFactory.create("PREFIX owl: <http://www.w3.org/2002/07/owl#>\n"
	        		+ "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n"
	        		+ "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n"
	        		+ "prefix art: <http://www.art.org/ontology/art#> \n"
	        		+ "select ?g ?s ?o \n"
	        		+ "where {\n"
	        		+ "  {?s a ?o FILTER NOT EXISTS { ?s rdfs:label ?label FILTER(lang(?label)=\"en\") . } }\n"
	        		+ "   UNION\n"
	        		+ "  {GRAPH ?g {?s a ?o FILTER NOT EXISTS { ?s rdfs:label ?label FILTER(lang(?label)=\"en\") . } }\n"
	        		+ "    FILTER ( ?g NOT IN ( IRI(<http://www.art.org/ontology>) )) . \n"
	        		+ "  }\n"
	        		+ "  FILTER (?o NOT IN ( art:painting, owl:NamedIndividual)) .\n"
	        		+ "}" );

	        try ( RDFConnection conn = RDFConnection.connect(queryService) ) {
	            conn.queryResultSet(query, ResultSetFormatter::out);
	            
	        }
	        	        
	 }

}
