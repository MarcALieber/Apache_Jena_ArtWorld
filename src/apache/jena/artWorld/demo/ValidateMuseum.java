package apache.jena.artWorld.demo;

import org.apache.jena.graph.Graph;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.shacl.ShaclValidator;
import org.apache.jena.shacl.Shapes;
import org.apache.jena.shacl.ValidationReport;
import org.apache.jena.shacl.lib.ShLib;

import apache.jena.artWorld.ArtistBase;

public class ValidateMuseum extends ArtistBase
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
        new ValidateMuseum().setArgs( args ).run();
    }

    @Override
    public void run() {
        // creates a new, empty in-memory model
        Model m = ModelFactory.createDefaultModel();

        // load some data into the model
        RDFDataMgr.read( m, MUSEUM_DATA_FILE );
        RDFDataMgr.read( m, MUSEUM2_DATA_FILE );
        RDFDataMgr.read( m, ARTEFACT_DATA_FILE );
        RDFDataMgr.read( m, ARTIST_SCHEMA_FILE );

        // generate some output
        System.out.println("------ ArtWorld local files on Museum ---------");
        showModelSize( m );
        // SHACL
        Graph shapesGraph = RDFDataMgr.loadGraph(MuseumShapes);
        Shapes shapes = Shapes.parse(shapesGraph);
        Graph dataGraph = m.getGraph();
        ValidationReport report = ShaclValidator.get().validate(shapes, dataGraph);
        ShLib.printReport(report);
        System.out.println();
       //RDFDataMgr.write(System.out, report.getModel(), Lang.TTL);
    }

    /***********************************/
    /* Internal implementation methods */
    /***********************************/

    /**
     * Show the size of the model on stdout
     */
    protected void showModelSize( Model m ) {
        System.out.println( String.format( "The model contains %d triples", m.size() ) );
    }



}