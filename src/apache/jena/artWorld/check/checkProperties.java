package apache.jena.artWorld.check;

import org.apache.jena.query.Query;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.ResultSetFormatter;
import org.apache.jena.rdfconnection.RDFConnection;

import apache.jena.artWorld.ArtistBase;

public class checkProperties extends ArtistBase {
	 public static void main( String[] args ) {
		    System.out.println("Show result for "+dsName);
	        new checkProperties().setArgs( args ).run();
	    }
	 
	 @Override
	 public void run() {
		    System.out.println("call "+queryService);
		    System.out.println("Verify if propery found in the DB matches the properties declared in the ontology");
	        Query query = QueryFactory.create("PREFIX owl: <http://www.w3.org/2002/07/owl#>\n"
	        		+ "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n"
	        		+ "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n"
	        		+ "select ?g ?p (COUNT(?p) as ?cnt)\n"
	        		+ "where {\n"
	        		+ "  {?s ?p ?o . }\n"
	        		+ "   UNION\n"
	        		+ "  {GRAPH ?g {?s ?p ?o . }}\n"
	        		+ " FILTER (?g NOT IN ( IRI(<http://www.art.org/ontology>) )) .\n"
	        		+ " FILTER (?p NOT IN ( rdfs:label, rdfs:comment, rdf:type, owl:sameAs )) .\n"
	        		+ " FILTER NOT EXISTS { GRAPH <http://www.art.org/ontology> {?p  rdf:type ?type ; rdfs:range ?range .  }}\n"
	        		+ "}\n"
	        		+ "group by ?g ?p \n"
	        		+ "" );
	        

	        try ( RDFConnection conn = RDFConnection.connect(queryService) ) {
	            conn.queryResultSet(query, ResultSetFormatter::out);
	            
	        }
	        
	        System.out.println("Verify if propery found in the DB has the correct rdfs:domain");
	        System.out.println("Verify if propery found in the DB has the correct rdfs:range");
	        
	 }

}
