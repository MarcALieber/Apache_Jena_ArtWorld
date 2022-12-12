package apache.jena.artWorld.check;

import org.apache.jena.query.Query;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.ResultSetFormatter;
import org.apache.jena.rdfconnection.RDFConnection;

import apache.jena.artWorld.ArtistBase;

public class checkClasses extends ArtistBase {
	 public static void main( String[] args ) {
		    System.out.println("Show result for "+dsName);
	        new checkClasses().setArgs( args ).run();
	    }
	 
	 @Override
	 public void run() {
		    System.out.println("call "+queryService);
		    System.out.println("Verify if Class declaration found in the DB matches the Classes declared in the ontology");
		    // todo
	        Query query = QueryFactory.create("PREFIX owl: <http://www.w3.org/2002/07/owl#>\n"
	        		+ "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n"
	        		+ "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n"
	        		+ "select ?g ?o (COUNT(?o) as ?cnt)\n"
	        		+ "where {\n"
	        		+ "  {?s a ?o . }\n"
	        		+ "   UNION\n"
	        		+ "  {GRAPH ?g {?s a ?o . }}\n"
	        		+ "  FILTER (?g NOT IN ( IRI(<http://www.art.org/ontology>) )) .\n"
	        		+ " FILTER (?o NOT IN ( owl:NamedIndividual )) .\n"
	        		+ " FILTER NOT EXISTS { GRAPH <http://www.art.org/ontology> {?o rdf:type owl:Class .  }}\n"
	        		+ "}\n"
	        		+ "group by ?g ?o \n" );

	        try ( RDFConnection conn = RDFConnection.connect(queryService) ) {
	            conn.queryResultSet(query, ResultSetFormatter::out);
	            
	        }
	        	        
	 }

}
