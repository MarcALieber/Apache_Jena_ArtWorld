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

package apache.jena.artWorld;

/**
 * <p>Base class for artist-ontology based examples. Declares
 * common namespaces and provides some basic utilities.</p>
 */
public abstract class ArtistBase extends Base
{
    /***********************************/
    /* Constants                       */
    /***********************************/
	//allowed values : ontology ** all ** movement ** museum ** default ** world ** artefact
	public static final String LOAD_NG = "artefact";

    public static final String ARTIST_SCHEMA = "http://www.art.org/ontology/person#";
    public static final String WORLD_SCHEMA = "http://www.art.org/ontology/world#";
    
    public static final String ARTIST_SCHEMA_FILE = ONTOLOGIES_DIR + "artOntology.ttl";
    public static final String PERSON_DATA_FILE = DATA_DIR + "artPerson.ttl";
    public static final String PERSON2_DATA_FILE = DATA_DIR + "artPerson2.ttl";
    public static final String INDIVIDUAL_DATA_FILE = DATA_DIR + "artIndividual.ttl";
    public static final String MOVEMENT_DATA_FILE = DATA_DIR + "artMovement.ttl";
    
    public static final String MUSEUM_DATA_FILE = DATA_DIR + "artMuseum.ttl";
    public static final String MUSEUM2_DATA_FILE = DATA_DIR + "artMuseum2.ttl";
    public static final String ARTEFACT_DATA_FILE = DATA_DIR + "artArtefacts.ttl";
    public static final String MATERIAL_DATA_FILE = DATA_DIR + "artMaterial.ttl";
    
    public static final String CITY_DATA_FILE = DATA_DIR + "artCity.ttl";
    public static final String CITY2_DATA_FILE = DATA_DIR + "artCity2.ttl";
    public static final String COUNTRY_DATA_FILE = DATA_DIR + "artCountry.ttl";
    public static final String WORLD_DATA_FILE = DATA_DIR + "artWorld.ttl";
    
    public static final String ArtistShapes = SHAPES_DIR +"artistShape.ttl";
    public static final String MuseumShapes = SHAPES_DIR +"museumShape.ttl";
    public static final String WorldShapes = SHAPES_DIR +"worldShape.ttl";
    public static final String ArtefactShapes = SHAPES_DIR +"artefactShape.ttl";
    
    // named graphs
    public static final String graphURI = "http://www.art.org/";
    public static final String ng_ontology ="http://www.art.org/ontology";
    public static final String ng_world ="http://www.art.org/world";
    public static final String ng_person ="http://www.art.org/person";
    public static final String ng_movement ="http://www.art.org/movement";
    public static final String ng_museum ="http://www.art.org/museum";
    public static final String ng_artefact ="http://www.art.org/artefact";

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

    /***********************************/
    /* Internal implementation methods */
    /***********************************/

    /***********************************/
    /* Inner class definitions         */
    /***********************************/

}

