package apache.jena.artWorld.util;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

import org.apache.jena.query.Dataset;
import org.apache.jena.query.DatasetFactory;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.sparql.exec.http.QueryExecutionHTTP;

import apache.jena.artWorld.ArtistBase;

public class generateHTMLArtWorld  extends ArtistBase {
	
	    /***********************************/
	    /* Constants                       */
	    /***********************************/
        static String header ="<!DOCTYPE html>\n"
        		+ "<html>\n"
        		+ "<head>\n"
        		+ "<style>\n"
        		+ "table {\n"
        		+ "  border-collapse: collapse;\n"
        		+ "  width: 100%;\n"
        		+ "}\n"
        		+ "\n"
        		+ "th, td {\n"
        		+ "  text-align: left;\n"
        		+ "  padding: 8px;\n"
        		+ "}\n"
        		+ "\n"
        		+ "tr:nth-child(even) {\n"
        		+ "  background-color: #D6EEEE;\n"
        		+ "}\n"
        		+ "</style>\n"
        		+ "<title>ArtWorld_overview</title>\n"
        		+ "</head>\n"
        		+ "<body>\n"
        		+ "<h1>Fuseki ArtWorld triple store overview</h1>\n"
        		+ "<table border=\"1\">\n"
        		+ "<tr>\n"
        		+ "    <th>Named Graph</th>\n"
        		+ "    <th>Number of triples</th>\n"
        		+ "</tr>\n";
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
	    // delete all files in quadfolder
	    // generate quads for each ng and default
	    // add param ng 

	    public static void main( String[] args ) {
	        new generateHTMLArtWorld().setArgs( args ).run();
	        System.out.println("End");
	    }

	    @Override
	    public void run() {
			boolean result; 
			File file ;  
			Model model;
			System.out.println("start");
			Query queryString = QueryFactory.create("SELECT ?NamedGraph (STR(COUNT(?s)) AS ?countOfs)  \n"
	        		+ "WHERE { GRAPH ?NamedGraph { ?s ?p ?o . } \n"
	        		+ "  } GROUP BY (?NamedGraph)" );
	        Query  query2 = QueryFactory.create("SELECT (\"default Graph\" as ?NamedGraph) (STR(COUNT(?s)) AS ?countOfs)  \n"
	        		+ "WHERE { ?s ?p ?o .  \n"
	        		+ "  }" );
	        Query query3 = QueryFactory.create("PREFIX owl: <http://www.w3.org/2002/07/owl#>\n"
	        		+ "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n"
	        		+ "PREFIX dc:  <http://purl.org/dc/elements/1.1/>\n"
	        		+ "PREFIX artw: <http://www.art.org/>\n"
	        		+ "PREFIX :    <.>\n"
	        		+ "SELECT ?g ?o (STR(count(?o)) as ?countClass)\n"
	        		+ "{\n"
	        		+ "  { ?s a ?o  } UNION { GRAPH ?g { ?s a ?o } }\n"
	        		+ "  filter (?g != artw:ontology || (?g = \"\")) \n"
	        		+ "  filter (?o != owl:NamedIndividual)\n"
	        		+ "}\n"
	        		+ "group by ?g ?o\n"
	        		+ "order by ?g ?o\n");
	        		
		  Dataset dataset;
		  ResultSet results;
		  
		  file = new File(tempfolder+"/overview.html");
	  	  file.delete();
			
			 
		   model = ModelFactory.createDefaultModel();
		   model.read("data/painters/artMuseum2.ttl");
		   dataset = DatasetFactory.create(model);
  	       dataset.addNamedModel(ng_museum, model);
	  	       
	  	   try { result = file.createNewFile();  //creates a new file  
	  	  
			   if(result) {  
					System.out.println("file created "+file.getCanonicalPath()); //returns the path string  
					PrintStream o = new PrintStream(file);
					System.setOut(o);
					System.out.println(header);
					//System.out.println(queryString+"\n");
					// named Graphs
					try ( QueryExecution qExec = QueryExecutionHTTP.create()
			                    .endpoint(queryService)
			                    .query(queryString)
			                    .param("timeout", "10000")
			                    .build() ) {
			            results = qExec.execSelect();
			            for ( ; results.hasNext() ; )
			            {
			            	System.out.println("<tr>");
			            	QuerySolution rb = results.nextSolution() ;
			                RDFNode y = rb.get("countOfs") ;
			                RDFNode x = rb.get("NamedGraph") ;
			                System.out.println("<td>"+x.toString()+"</td><td>"+y.toString()+"</td>") ;
			                System.out.println("</tr>");
     			        }
			            
					}
					// default graph
					try ( QueryExecution qExec2 = QueryExecutionHTTP.create()
		                    .endpoint(queryService)
		                    .query(query2)
		                    .param("timeout", "10000")
		                    .build() ) {
		            results = qExec2.execSelect();
		            for ( ; results.hasNext() ; )
			            {
			            	System.out.println("<tr>");
			            	QuerySolution rb = results.nextSolution() ;
			                RDFNode y = rb.get("countOfs") ;
			                RDFNode x = rb.get("NamedGraph") ;
			                System.out.println("<td>"+x.toString()+"</td><td>"+y.toString()+"</td>") ;
			                System.out.println("</tr>");
	 			        }
					}
					System.out.println("</table><br/>\n"
							+ "<table border=\"1\">\n"
			        		+ "<tr>\n"
			        		+ "    <th>Named Graph</th>\n"
			        		+ "    <th>Class Name</th>\n"
			        		+ "    <th>Number of classes</th>\n"
			        		+ "</tr>\n");
    			    // classes per named Graph
		            try ( QueryExecution qExec1 = QueryExecutionHTTP.create()
		                    .endpoint(queryService)
		                    .query(query3)
		                    .param("timeout", "10000")
		                    .build() ) {
		            results = qExec1.execSelect();
		            for ( ; results.hasNext() ; )
		            {
		            	System.out.println("<tr>");
		            	QuerySolution rb = results.nextSolution() ;
		                RDFNode y = rb.get("?countClass") ;
		                RDFNode x = rb.get("g") ;
		                RDFNode z = rb.get("o") ;
		                System.out.println("<td>"+x.toString()+"</td><td>"+z.toString()+"</td><td>"+y.toString()+"</td>") ;
		                System.out.println("</tr>");
 			        }
				}
			   
			   System.out.println("</table><br/>\n"
			   		+ "</body>\n"
			   		+ "</HTML>\n");
			   System.setOut(console);
			   System.out.println("--> file " + file + " created");
			   }
	  	   }
	      catch (IOException e)   
				  { e.printStackTrace();    //prints exception if any 
				  }		  }       
}
	    
	  	   

