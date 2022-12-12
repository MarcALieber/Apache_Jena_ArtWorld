package apache.jena.artWorld.demo;

import org.apache.jena.query.Query;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.ResultSetFormatter;
import org.apache.jena.rdfconnection.RDFConnection;

import apache.jena.artWorld.ArtistBase;

public class countTriples 
extends ArtistBase {
	 public static void main( String[] args ) {
		    System.out.println("Show result for "+dsName);
	        new countTriples().setArgs( args ).run();
	    }
	 
	 @Override
	 public void run() {
		    System.out.println("call "+queryService);
	        Query query = QueryFactory.create("SELECT ?NamedGraph (COUNT(?s) as ?countOfs)  \n"
	        		+ "WHERE { GRAPH ?NamedGraph { ?s ?p ?o . } \n"
	        		+ "  } GROUP BY (?NamedGraph)" );
	        Query  query2 = QueryFactory.create("SELECT (\"default\" as ?graph) (COUNT(?s) as ?countOfs)  \n"
	        		+ "WHERE { ?s ?p ?o .  \n"
	        		+ "  }" );
	        

	        try ( RDFConnection conn = RDFConnection.connect(queryService) ) {
	            conn.queryResultSet(query, ResultSetFormatter::out);
	            
	            conn.queryResultSet(query2, ResultSetFormatter::out);
	            
	        }
	 }

}
