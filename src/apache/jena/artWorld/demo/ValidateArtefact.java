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

public class ValidateArtefact extends ArtistBase
{
   public static void main( String[] args ) {
        new ValidateArtefact().setArgs( args ).run();
    }

    @Override
    public void run() {
        // creates a new, empty in-memory model
        Model m = ModelFactory.createDefaultModel();

        // load some data into the model
        RDFDataMgr.read( m, ARTEFACT_DATA_FILE );
        RDFDataMgr.read( m, MUSEUM_DATA_FILE );
        RDFDataMgr.read( m, PERSON_DATA_FILE );
        RDFDataMgr.read( m, MATERIAL_DATA_FILE );
        RDFDataMgr.read( m, ARTIST_SCHEMA_FILE );

        // generate some output
        System.out.println("------ ArtWorld local files on Artefact ---------");
        showModelSize( m );
        // SHACL
        Graph shapesGraph = RDFDataMgr.loadGraph(ArtefactShapes);
        Shapes shapes = Shapes.parse(shapesGraph);
        Graph dataGraph = m.getGraph();
        ValidationReport report = ShaclValidator.get().validate(shapes, dataGraph);
        ShLib.printReport(report);
        System.out.println();
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