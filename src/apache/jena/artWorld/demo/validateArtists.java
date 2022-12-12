/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package apache.jena.artWorld.demo;


// Imports
///////////////
import apache.jena.artWorld.ArtistBase;

import org.apache.jena.graph.Graph;
import org.apache.jena.rdf.model.*;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.shacl.ShaclValidator;
import org.apache.jena.shacl.Shapes;
import org.apache.jena.shacl.ValidationReport;
import org.apache.jena.shacl.lib.ShLib;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.RDFS;

/**
 * <h2>Apache Jena Getting Started Guide - Step 1: Hello World</h2>
 * <p>
 * In this step, we illustrate the basic operations of getting some data into
 * a Java program, finding some data, and showing some output.
 * </p>
 */
public class validateArtists
    extends ArtistBase
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
        new validateArtists().setArgs( args ).run();
    }

    @Override
    public void run() {
        // creates a new, empty in-memory model
        Model m = ModelFactory.createDefaultModel();

        // load some data into the model
        RDFDataMgr.read( m, PERSON_DATA_FILE );
        RDFDataMgr.read( m, PERSON2_DATA_FILE );
        RDFDataMgr.read( m, INDIVIDUAL_DATA_FILE);
        RDFDataMgr.read( m, MOVEMENT_DATA_FILE);
        RDFDataMgr.read( m, ARTIST_SCHEMA_FILE );

        // generate some output
        System.out.println("------ ArtWorld local files ---------");
        showModelSize( m );
        listArtists( m );
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

