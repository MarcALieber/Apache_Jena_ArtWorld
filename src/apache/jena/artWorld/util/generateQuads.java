package apache.jena.artWorld.util;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Iterator;

import org.apache.jena.graph.Triple;
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
import org.apache.jena.sparql.core.Quad;
import org.apache.jena.util.PrintUtil;

import apache.jena.artWorld.ArtistBase;

public class generateQuads extends ArtistBase
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
    // delete all files in quadfolder
    // generate quads for each ng and default
    // add param ng 

    public static void main( String[] args ) {
        new generateQuads().setArgs( args ).run();
        System.out.println("End");
    }

    @Override
    public void run() {
    	String namedGraph =  LOAD_NG;
    	System.out.println( quadfolder+" rebuild "+ namedGraph) ;
		boolean result; 
		File file ;  
		Model model;
		System.out.println("start");
		String ngQueryString  = "CONSTRUCT WHERE{ GRAPH ?g {?s ?p ?o}  } ";
		String queryString  = "CONSTRUCT {?s ?p ?o } WHERE{  ?s ?p ?o  } ";
		Dataset dataset;
		Query query;
		
		if (namedGraph.equals("all") || namedGraph.equals("museum"))
		   { 
		   model = ModelFactory.createDefaultModel();
		   model.read("data/painters/artMuseum.ttl");
		   model.read("data/painters/artMuseum2.ttl");
		   dataset = DatasetFactory.create(model);
  	       dataset.addNamedModel(ng_museum, model);
  	       file = new File(quadfolder+"/museum_ng.nt");
  	       file.delete();
  	       try { result = file.createNewFile();  //creates a new file  
			  if(result) 
				{  
				System.out.println("file created "+file.getCanonicalPath()); //returns the path string  
				PrintStream o = new PrintStream(file);
				System.setOut(o);
				query = QueryFactory.create(ngQueryString, Syntax.syntaxARQ);
				   try (QueryExecution qexec = QueryExecutionFactory.create(query, dataset)) {
						Dataset d = qexec.execConstructDataset();
						RDFDataMgr.write(System.out, d, Lang.NQUADS);
				   }
				System.setOut(console);
				System.out.println("--> file " + file + " created");
				}  
			  else  
				{ System.out.println("File already exist at location: "+file.getCanonicalPath()); }  
   			}   
	       catch (IOException e)   
			  { e.printStackTrace();    //prints exception if any 
		   }       
		 }

		if (namedGraph.equals("all") || namedGraph.equals("world"))
		   {model = ModelFactory.createDefaultModel(); 
		   model.read("data/painters/artWorld.ttl");
		   model.read("data/painters/artCity.ttl");
		   model.read("data/painters/artCity2.ttl");
		   model.read("data/painters/artCountry.ttl");
		   dataset = DatasetFactory.create(model);
	       dataset.addNamedModel(ng_world, model);
	       file = new File(quadfolder+"/world_ng.nt");
	       file.delete();
	       try { result = file.createNewFile();  //creates a new file  
			  if(result)   
				{  
				System.out.println("file created "+file.getCanonicalPath()); //returns the path string  
				PrintStream o = new PrintStream(file);
				System.setOut(o);
				query = QueryFactory.create(ngQueryString, Syntax.syntaxARQ);
				   try (QueryExecution qexec = QueryExecutionFactory.create(query, dataset)) {
						Dataset d = qexec.execConstructDataset();
						RDFDataMgr.write(System.out, d, Lang.NQUADS);
				   }
				System.setOut(console);
				System.out.println("--> file " + file + " created");
				}  
			  else  
				{ System.out.println("File already exist at location: "+file.getCanonicalPath()); }  
			}   
	       catch (IOException e)   
			  { e.printStackTrace();    //prints exception if any 
		   }       
		 }		

		if (namedGraph.equals("all") || namedGraph.equals("artefact"))
		   {model = ModelFactory.createDefaultModel(); 
		   model.read("data/painters/artArtefacts.ttl");
		   model.read("data/painters/artMaterial.ttl");
		   dataset = DatasetFactory.create(model);
	       dataset.addNamedModel(ng_artefact, model);
	       file = new File(quadfolder+"/artefact_ng.nt");
	       file.delete();
	       try { result = file.createNewFile();  //creates a new file  
			  if(result)   
				{  
				System.out.println("file created "+file.getCanonicalPath()); //returns the path string  
				PrintStream o = new PrintStream(file);
				System.setOut(o);
				query = QueryFactory.create(ngQueryString, Syntax.syntaxARQ);
				   try (QueryExecution qexec = QueryExecutionFactory.create(query, dataset)) {
						Dataset d = qexec.execConstructDataset();
						RDFDataMgr.write(System.out, d, Lang.NQUADS);
				   }
				System.setOut(console);
				System.out.println("--> file " + file + " created");
				}  
			  else  
				{ System.out.println("File already exist at location: "+file.getCanonicalPath()); }  
			}   
	       catch (IOException e)   
			  { e.printStackTrace();    //prints exception if any 
		   }       
		 }			

		if (namedGraph.equals("all") || namedGraph.equals("movement"))
		   {model = ModelFactory.createDefaultModel(); 
		   model.read("data/painters/artMovement.ttl");
		   model.read("data/painters/artMovement2.ttl");
		   dataset = DatasetFactory.create(model);
	       dataset.addNamedModel(ng_movement, model);
	       file = new File(quadfolder+"/movement_ng.nt");
	       file.delete();
	       try { result = file.createNewFile();  //creates a new file  
			  if(result)   
				{  
				System.out.println("file created "+file.getCanonicalPath()); //returns the path string  
				PrintStream o = new PrintStream(file);
				System.setOut(o);
				query = QueryFactory.create(ngQueryString, Syntax.syntaxARQ);
				   try (QueryExecution qexec = QueryExecutionFactory.create(query, dataset)) {
						Dataset d = qexec.execConstructDataset();
						RDFDataMgr.write(System.out, d, Lang.NQUADS);
				   }
				System.setOut(console);
				System.out.println("--> file " + file + " created");
				}  
			  else  
				{ System.out.println("File already exist at location: "+file.getCanonicalPath()); }  
			}   
	       catch (IOException e)   
			  { e.printStackTrace();    //prints exception if any 
		   }       
		 }		
		
		if (namedGraph.equals("all") || namedGraph.equals("ontology"))
		   { model = ModelFactory.createDefaultModel(); 
		   model.read("data/painters/ontologies/artOntology.ttl");
		   dataset = DatasetFactory.create(model);
	       dataset.addNamedModel(ng_ontology, model);
	       file = new File(quadfolder+"/ontology_ng.nt");
	       file.delete();
	       try { result = file.createNewFile();  //creates a new file  
			  if(result)   
				{  
				System.out.println("file created "+file.getCanonicalPath()); //returns the path string  
				PrintStream o = new PrintStream(file);
				System.setOut(o);
				query = QueryFactory.create(ngQueryString, Syntax.syntaxARQ);
				   try (QueryExecution qexec = QueryExecutionFactory.create(query, dataset)) {
						Dataset d = qexec.execConstructDataset();
						RDFDataMgr.write(System.out, d, Lang.NQUADS);
				   }
				System.setOut(console);
				System.out.println("--> file " + file + " created");
				}  
			  else  
				{ System.out.println("File already exist at location: "+file.getCanonicalPath()); }  
			}   
	       catch (IOException e)   
			  { e.printStackTrace();    //prints exception if any 
		   }       
		 }		
		
		if (namedGraph.equals("all") || namedGraph == "default" )  {
			model = ModelFactory.createDefaultModel();
			model.read("data/painters/artPerson.ttl");
			model.read("data/painters/artPerson2.ttl");
			model.read("data/painters/artIndividual.ttl");
			dataset = DatasetFactory.create(model);
			file = new File(quadfolder+"/default_ng.nt");
			file.delete();
	   		query = QueryFactory.create(queryString, Syntax.syntaxARQ);
			if (namedGraph.equals("all") || namedGraph == "default" )  {
				model.read("data/painters/artPerson.ttl");
				model.read("data/painters/artPerson2.ttl");
				model.read("data/painters/artIndividual.ttl");
				dataset = DatasetFactory.create(model);
				file = new File(quadfolder+"/default_ng.nt");
				file.delete();

				try { result = file.createNewFile();  //creates a new file  
				  if(result)      // test if successfully created a new file  
					{  
					System.out.println("file created "+file.getCanonicalPath()); //returns the path string  
					PrintStream o = new PrintStream(file);
					System.setOut(o);
					try (QueryExecution qexec = QueryExecutionFactory.create(query, dataset)) {
							Dataset d = qexec.execConstructDataset();
							RDFDataMgr.write(System.out, d, Lang.NQUADS);
					   }
					System.setOut(console);
					System.out.println("--> file " + file + " created");
					}  
				  else  
					{ System.out.println("File already exist at location: "+file.getCanonicalPath()); }  
	 			}   
		       catch (IOException e)   
				  { e.printStackTrace();    //prints exception if any 
			   }       
		 }
	}
  }
}

   
 
