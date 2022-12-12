package apache.jena.artWorld.util;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

import org.apache.jena.query.Dataset;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.Syntax;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;

import apache.jena.artWorld.ArtistBase;

public class exportOneGraph extends ArtistBase {
	/***********************************/
	/* Constants */
	/***********************************/

	/***********************************/
	/* Static variables */
	/***********************************/
	static long millis = System.currentTimeMillis();
	static java.sql.Date date = new java.sql.Date(millis);
	static PrintStream console = System.out;

	/***********************************/
	/* Instance variables */
	/***********************************/

	/***********************************/
	/* Constructors */
	/***********************************/

	/***********************************/
	/* External signature methods */
	/***********************************/

	public static void main(String[] args) {
		addOption("namedGraph", args[0], true, null);
		new exportOneGraph().setArgs(args).run();
	}

	@Override
	public void run() {
		String namedGraph = getOptions().getOption("namedGraph").getLongOpt();
		boolean result;
		File file = new File(tempfolder + "/ng_" + namedGraph + "_" + date + ".nt");

		try {
			result = file.createNewFile(); // creates a new file
			if (result) // test if successfully created a new file
			{
				System.out.println("file created " + file.getCanonicalPath()); // returns the path string
			} else {
				System.out.println("File already exist at location: " + file.getCanonicalPath());
			}
		} catch (IOException e) {
			e.printStackTrace(); // prints exception if any
		}

		String graph = "<"+graphURI + namedGraph + ">";
		System.out.println("All triples of " + graph);
		String queryString = "CONSTRUCT {?s ?p ?o}\n" + "where {{GRAPH " + graph + " {?s ?p ?o}}\n" + "}";

		Query query = QueryFactory.create(queryString, Syntax.syntaxARQ);
		try (QueryExecution qexec = QueryExecution.service(queryService).query(query).build()) {

			PrintStream o = new PrintStream(file);
			System.setOut(o);
			Dataset d = qexec.execConstructDataset();
			RDFDataMgr.write(System.out, d, Lang.TRIG);
			System.setOut(console);
			System.out.println("--> file " + file + " created");
		} catch (IOException e) {
			System.setOut(console);
			System.out.println("!! An error occurred.");
			e.printStackTrace();
		}
	}
}
