/*
 *  several utility tools
 *  LoadArtWorld : to reload the default Graph or one the named Graph or the comple triples store DB
 *                 Important : set the parameter LOAD_NG in src/apache/jena/artWorld/ArtistBase.java
 *                             to --> ontology or all or default or movement or world or museum
 *                                --> default is the default Graph where artists informations are stored
 *  exportOneGraph : generate a TTL file for one ng
 *                   parameter : museum or ontology or world or movement
 *                               TODO : default graph
 *  
 * @since 1.0
 * @author Marc Lieber
 * @version 1.1
 */

package apache.jena.artWorld.util;