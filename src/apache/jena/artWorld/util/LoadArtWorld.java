package apache.jena.artWorld.util;

import java.net.Authenticator;
import java.net.http.HttpClient;
import java.time.Duration;

import org.apache.jena.http.auth.AuthLib;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.ResultSetFormatter;
import org.apache.jena.rdfconnection.RDFConnection;
import org.apache.jena.sparql.exec.http.UpdateExecutionHTTP;

import apache.jena.artWorld.ArtistBase;

public class LoadArtWorld extends ArtistBase
{
    /***********************************/
    /* Constants                       */
    /***********************************/

    /***********************************/
    /* Static variables                */
    /***********************************/

    /***********************************/
    /* Instance variables              */
    /***********************************/

    /***********************************/
    /* Constructors                    */
    /***********************************/

    /***********************************/
    /* External signature methods      */
    /***********************************/

    /**
     * Main entry point for running this example. Since every sub-class
     * will be {@link Runnable}, we create an instance, stash the
     * command-line args where we can retrieve them later, and
     * invoke {@link #run}
     */
    public static void main( String[] args ) {
        new LoadArtWorld().setArgs( args ).run();
    }

    @Override
    public void run() {
    	String namedGraph =  LOAD_NG;
		System.out.println( dataURL+" reload "+ namedGraph) ;
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
        countTriples(); // todo: use demo.countTriples;
        
    }
    
	private static void countTriples() {
        Query query = QueryFactory.create("SELECT ?NamedGraph (COUNT(?s) as ?countOfs)  \n"
        		+ "WHERE { GRAPH ?NamedGraph { ?s ?p ?o . } \n"
        		+ "  } GROUP BY (?NamedGraph)" );
        Query  query2 = QueryFactory.create("SELECT (\"default\" as ?graph) (COUNT(?s) as ?countOfs)  \n"
        		+ "WHERE { ?s ?p ?o .  \n"
        		+ "  } GROUP BY (?g)" );
        try ( RDFConnection conn = RDFConnection.connect( queryService) ) {
            conn.queryResultSet(query, ResultSetFormatter::out);
            conn.queryResultSet(query2, ResultSetFormatter::out);
        }
	}
	
	// remove triples from the graph
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
        
        String queryService =  dataURL;
        String namedGraph;
      if (ng.equals("all")) {
	      try ( RDFConnection conn = RDFConnection.connect(queryService) ) {
	    	  System.out.println("Upload "+ng);
	    	  conn.update("CLEAR ALL");
	    	  System.out.println("artOntology");
	    	  conn.load( ng_ontology,"data/painters/artOntology.ttl") ;
	    	  System.out.println("artMuseum");
	    	  conn.load( ng_museum,"data/painters/artMuseum.ttl") ;
	    	  System.out.println("artMuseum2");
	    	  conn.load( ng_museum,"data/painters/artMuseum2.ttl") ;
	    	  System.out.println("artArtefact");
	    	  conn.load( ng_artefact,"data/painters/artArtefacts.ttl") ;
	    	  System.out.println("artMaterial");
	    	  conn.load( ng_artefact,"data/painters/artMaterial.ttl") ;
	    	  System.out.println("artWorld");
	    	  conn.load( ng_world,"data/painters/artWorld.ttl") ;
	    	  System.out.println("artCountry");
	    	  conn.load( ng_world,"data/painters/artCountry.ttl") ;
	    	  System.out.println("artCity");
	    	  conn.load( ng_world,"data/painters/artCity.ttl") ;
	    	  System.out.println("artCity2");
	    	  conn.load( ng_world,"data/painters/artCity2.ttl") ;
	    	  System.out.println("artMovement");
	    	  conn.load( ng_movement,"data/painters/artMovement.ttl") ;
	    	  System.out.println("artMovement2");
	    	  conn.load( ng_movement,"data/painters/artMovement2.ttl") ;
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
      else if (ng.equals("ontology")) {
	      try ( RDFConnection conn = RDFConnection.connect(queryService) ) {
	    	  namedGraph = graphURI+ng;
	    	  System.out.println("Clear and reload GRAPH <"+namedGraph+">");
	    	  conn.update("DELETE where {GRAPH <"+namedGraph+"> {?s ?p ?o}}");
	    	  conn.load(namedGraph,"data/painters/ontologies/artOntology.ttl") ;
		  }}
      else if (ng.equals("world")) {
	      try ( RDFConnection conn = RDFConnection.connect(queryService) ) {
	    	  namedGraph =  graphURI+ng;
	    	  System.out.println("Clear and reload GRAPH <"+namedGraph+">");
	    	  conn.update("DELETE where {GRAPH <"+namedGraph+"> {?s ?p ?o}}");
	    	  conn.load(namedGraph,"data/painters/artWorld.ttl") ;
	    	  System.out.println("artCountry");
	    	  conn.load(namedGraph,"data/painters/artCountry.ttl") ;
	    	  System.out.println("artCity");
	    	  conn.load(namedGraph,"data/painters/artCity.ttl") ;
	    	  System.out.println("artCity2");
	    	  conn.load(namedGraph,"data/painters/artCity2.ttl") ;
		  }}
      else if (ng.equals("movement")) {
	      try ( RDFConnection conn = RDFConnection.connect(queryService) ) {
	    	  namedGraph =  graphURI+ng;
	    	  System.out.println("Clear and reload GRAPH <"+namedGraph+">");
	    	  conn.update("DELETE where {GRAPH <"+namedGraph+"> {?s ?p ?o}}");
	    	  conn.load(namedGraph,"data/painters/artMovement.ttl") ;
	    	  conn.load(namedGraph,"data/painters/artMovement2.ttl") ;
		  }}
      else if (ng.equals("artefact")) {
	      try ( RDFConnection conn = RDFConnection.connect(queryService) ) {
	    	  namedGraph =  graphURI+ng;
	    	  System.out.println("Clear and reload GRAPH <"+namedGraph+">");
	    	  conn.update("DELETE where {GRAPH <"+namedGraph+"> {?s ?p ?o}}");
	    	  conn.load(namedGraph,"data/painters/artArtefacts.ttl") ;
	    	  System.out.println("artMaterial");
	    	  conn.load(namedGraph,"data/painters/artMaterial.ttl") ;
		  }}
      else if (ng.equals("museum")) {
	       try ( RDFConnection conn = RDFConnection.connect(queryService) ) {
	    	   namedGraph = graphURI+ng;
	    	   System.out.println("Clear and reload GRAPH <"+namedGraph+">");
	    	   conn.update("DELETE where {GRAPH <"+namedGraph+"> {?s ?p ?o}}");
	    	   conn.load(namedGraph,"data/painters/artMuseum.ttl") ;
	    	   System.out.println("artMuseum2");
		       conn.load(namedGraph,"data/painters/artMuseum2.ttl") ;
	  }}

    }
}
