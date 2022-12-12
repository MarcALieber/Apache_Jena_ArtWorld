package apache.jena.artWorld.demo;

import apache.jena.artWorld.ArtistBase;
import org.apache.jena.graph.Graph;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.Syntax;
import org.apache.jena.rdf.model.*;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.shacl.ShaclValidator;
import org.apache.jena.shacl.Shapes;
import org.apache.jena.shacl.ValidationReport;
import org.apache.jena.shacl.lib.ShLib;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.RDFS;

public class ShowArtistsTDB
extends ArtistBase
{
    public static void main( String[] args ) {
        new ShowArtistsTDB().setArgs( args ).run();
    }

    @Override
    public void run() {
        // creates a new, empty in-memory model
        Model m = ModelFactory.createDefaultModel();
        String queryString ="CONSTRUCT {?s ?p ?o} where {?s ?p ?o} ";
        Query query = QueryFactory.create(queryString, Syntax.syntaxARQ);
        // load the model
        try (QueryExecution qexec =  QueryExecution.service(queryService).query(query).build()) { 
        	 qexec.execConstruct(m);
        }
        // load the local ontology into the model (instead of the TDB ontology)
        RDFDataMgr.read( m, ARTIST_SCHEMA_FILE );

        // generate some output
        System.out.println("------ ArtWorld TDB ---------");
        showModelSize( m );        
        listArtists( m );
        System.out.println("-----------------------------");
        // SHACL
        Graph shapesGraph = RDFDataMgr.loadGraph(ArtistShapes);
        Shapes shapes = Shapes.parse(shapesGraph);
        Graph dataGraph = m.getGraph();
        ValidationReport report = ShaclValidator.get().validate(shapes, dataGraph);
        ShLib.printReport(report);
        System.out.println();
        RDFDataMgr.write(System.out, report.getModel(), Lang.TTL);
        
        
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

    /**
     * List the names of Artists to stdout
     */
    protected void listArtists( Model m ) {
    	//person:painter
        Resource personClass = m.getResource( ARTIST_SCHEMA + "painter" );

        StmtIterator i = m.listStatements( null, RDF.type, personClass );

        while (i.hasNext()) {
            Resource artist = i.next().getSubject();
            String label = getEnglishLabel( artist );
            System.out.println( String.format( "Artist %s has name: %s", artist.getURI(), label ) );
        }
    }

    /**
     * Get the English-language label for a given resource. In general, a resource
     * may have zero, one or many labels. In this case, we happen to know that
     * the artist resources have mutlilingual labels, so we pick out the English one
     * @param artist
     * @return
     */
    protected String getEnglishLabel( Resource artist ) {
        StmtIterator i = artist.listProperties( RDFS.label );
        while (i.hasNext()) {
            Literal l = i.next().getLiteral();

            if (l.getLanguage() != null && l.getLanguage().equals( "en")) {
                // found the English language label
                return l.getLexicalForm();
            }
        }

        return "An Artist with No Name!";
    }

    /**
     * Get the value of a property as a string, allowing for missing properties
     * @param r A resource
     * @param p The property whose value is wanted
     * @return The value of the <code>p</code> property of <code>r</code> as a string
     */
    protected String getValueAsString( Resource r, Property p ) {
        Statement s = r.getProperty( p );
        if (s == null) {
            return "";
        }
        else {
            return s.getObject().isResource() ? s.getResource().getURI() : s.getString();
        }
    }


    /***********************************/
    /* Inner class definitions         */
    /***********************************/

}

