package artWorld;
// param http://www.art.org/museum  or all
import java.net.Authenticator;
import java.net.http.HttpClient;
import java.time.Duration;

import org.apache.jena.http.auth.AuthLib;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.ResultSetFormatter;
import org.apache.jena.rdfconnection.RDFConnection;
import org.apache.jena.sparql.exec.http.UpdateExecutionHTTP;
//import artWorld.countTriples;

public class LoadArtWorld {
	static String dsName = "ArtWorld";
	static String serverURL ="http://localhost:3030";
	static String dataURL = serverURL+"/"+dsName;
	static String queryService = dataURL+"/query";
	
	public static void main(String ...args) {
		String namedGraph = args[0];
		System.out.println("reload "+ namedGraph) ;
		Query query ;
		if (namedGraph == "all" ) {
			query = QueryFactory.create("SELECT (COUNT(?s) as ?countOfs)  { ?s ?p ?o . }" );
			//clearDS();
		}
		else if (namedGraph == "default" )  {
            query = QueryFactory.create("SELECT (COUNT(?s) as ?countOfs)  {?s ?p ?o . }" );
		}
		else {
            query = QueryFactory.create("SELECT (COUNT(?s) as ?countOfs)  {GRAPH <"+namedGraph+"> { ?s ?p ?o . }}" );
		}

        try ( RDFConnection conn = RDFConnection.connect(queryService) ) {
            conn.queryResultSet(query, ResultSetFormatter::out);
        }
        loadDS(namedGraph);
        countTriples();
    }
	
	private static void countTriples() {
        Query query = QueryFactory.create("SELECT ?NamedGraph (COUNT(?s) as ?countOfs)  \n"
        		+ "WHERE { GRAPH ?NamedGraph { ?s ?p ?o . } \n"
        		+ "  } GROUP BY (?NamedGraph)" );
        Query  query2 = QueryFactory.create("SELECT (\"default\" as ?graph) (COUNT(?s) as ?countOfs)  \n"
        		+ "WHERE { ?s ?p ?o .  \n"
        		+ "  } GROUP BY (?g)" );
        try ( RDFConnection conn = RDFConnection.connect(queryService) ) {
            conn.queryResultSet(query, ResultSetFormatter::out);
            conn.queryResultSet(query2, ResultSetFormatter::out);
        }
	}

	// remove
    public static void clearDS() {
        System.out.println();
        System.out.println("HttpClient + clear all");
        String queryService = dataURL+"/update";
        Authenticator authenticator = AuthLib.authenticator("u", "p");
        HttpClient httpClient = HttpClient.newBuilder()
                //.followRedirect
                .connectTimeout(Duration.ofSeconds(10))
                .authenticator(authenticator)
                .build();
        
        UpdateExecutionHTTP.service(queryService)
                .httpClient(httpClient)
                .update("CLEAR ALL")
                .execute();
    }
    
	// upload
    public static void loadDS(String ng) {
        
        String queryService = dataURL;
       //String test1 = "all"; //"http://www.art.org/ontology";
      if (ng.equals("all")) {
	      try ( RDFConnection conn = RDFConnection.connect(queryService) ) {
	    	  System.out.println("Upload "+ng);
	    	  conn.update("CLEAR ALL");
	    	  System.out.println("artOntology");
	    	  conn.load("http://www.art.org/ontology","data/painters/artOntology.ttl") ;
	    	  System.out.println("artMuseum");
	    	  conn.load("http://www.art.org/museum","data/painters/artMuseum.ttl") ;
	    	  System.out.println("artArtefact");
	    	  conn.load("http://www.art.org/artefact","data/painters/artArtefacts.ttl") ;
	    	  System.out.println("artMaterial");
	    	  conn.load("http://www.art.org/artefact","data/painters/artMaterial.ttl") ;
	    	  System.out.println("artWorld");
	    	  conn.load("http://www.art.org/world","data/painters/artWorld.ttl") ;
	    	  System.out.println("artCountry");
	    	  conn.load("http://www.art.org/world","data/painters/artCountry.ttl") ;
	    	  System.out.println("artCity");
	    	  conn.load("http://www.art.org/world","data/painters/artCity.ttl") ;
	    	  System.out.println("artMovement");
	    	  conn.load("http://www.art.org/movement","data/painters/artMovement.ttl") ;
	    	  System.out.println("artMovement2");
	    	  conn.load("http://www.art.org/movement","data/painters/artMovement2.ttl") ;
	    	  System.out.println("artIndividual");
	    	  conn.load("data/painters/artIndividual.ttl") ;
	    	  System.out.println("artPerson");
	    	  conn.load("data/painters/artPerson.ttl") ;
	    	  System.out.println("artPerson2");
	    	  conn.load("data/painters/artPerson2.ttl") ;
	      }}
      else if (ng.equals("default")) {
	      try ( RDFConnection conn = RDFConnection.connect(queryService) ) {
	    	  System.out.println("Clear and reload GRAPH <"+ng+">");
	    	  conn.update("DELETE where {?s ?p ?o}");
	    	  conn.load("data/painters/artPerson.ttl") ;
	    	  System.out.println("artIndividual");
	    	  conn.load("data/painters/artIndividual.ttl") ;
	    	  System.out.println("artPerson2");
	    	  conn.load("data/painters/artPerson2.ttl") ;
		  }}    
      else if (ng.equals("http://www.art.org/ontology")) {
	      try ( RDFConnection conn = RDFConnection.connect(queryService) ) {
	    	  System.out.println("Clear and reload GRAPH <"+ng+">");
	    	  conn.update("DELETE where {GRAPH <"+ng+"> {?s ?p ?o}}");
	    	  conn.load(ng,"data/painters/artOntology.ttl") ;
		  }}
      else if (ng.equals("http://www.art.org/world")) {
	      try ( RDFConnection conn = RDFConnection.connect(queryService) ) {
	    	  System.out.println("Clear and reload GRAPH <"+ng+">");
	    	  conn.update("DELETE where {GRAPH <"+ng+"> {?s ?p ?o}}");
	    	  conn.load(ng,"data/painters/artWorld.ttl") ;
	    	  System.out.println("artCountry");
	    	  conn.load(ng,"data/painters/artCountry.ttl") ;
	    	  System.out.println("artCity");
	    	  conn.load("http://www.art.org/world","data/painters/artCity.ttl") ;
		  }}
      else if (ng.equals("http://www.art.org/movement")) {
	      try ( RDFConnection conn = RDFConnection.connect(queryService) ) {
	    	  System.out.println("Clear and reload GRAPH <"+ng+">");
	    	  conn.update("DELETE where {GRAPH <"+ng+"> {?s ?p ?o}}");
	    	  conn.load(ng,"data/painters/artMovement.ttl") ;
	    	  conn.load(ng,"data/painters/artMovement2.ttl") ;
		  }}
      else if (ng.equals("http://www.art.org/artefact")) {
	      try ( RDFConnection conn = RDFConnection.connect(queryService) ) {
	    	  System.out.println("Clear and reload GRAPH <"+ng+">");
	    	  conn.update("DELETE where {GRAPH <"+ng+"> {?s ?p ?o}}");
	    	  conn.load(ng,"data/painters/artArtefacts.ttl") ;
	    	  System.out.println("artMaterial");
	    	  conn.load("http://www.art.org/artefact","data/painters/artMaterial.ttl") ;
		  }}
      else if (ng.equals("http://www.art.org/museum")) {
	       try ( RDFConnection conn = RDFConnection.connect(queryService) ) {
	    	   System.out.println("Clear and reload GRAPH <"+ng+">");
	    	   conn.update("DELETE where {GRAPH <"+ng+"> {?s ?p ?o}}");
	    	   conn.load(ng,"data/painters/artMuseum.ttl") ;
	    	   System.out.println("artArtefacts");
	    	   conn.load(ng,"data/painters/artArtefacts.ttl") ;
	  }}

    }
}

