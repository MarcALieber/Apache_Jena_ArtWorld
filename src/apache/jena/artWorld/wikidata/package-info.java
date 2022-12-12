/*
 *  tools to import data from Wikidata
 *  generateFiles : uploads a list of artists into the tempfolder
 *                  requires a parameter example: "wd:Q944834,wd:Q34661"
 *  generateFiles2 : uploads only the MISSING museum, city and movement
 *                   no parameter required
 * 
 * Steps to import new artists : 
 *  Step1 execute generateFiles example of parameter = wd:Q5582,wd:Q301  (Van Gogh and El Greco)
 *     -- new data are stored in data/tempload in 3 files 
 *     -- copy the set of triples into artPerson.ttl , artPerson2.ttl and artArtefacts.ttl
 *  Step2 validate with SHACL the data : 
 *    execute apache/jena/artWorld/demo/validateArtists.java
 *      -- fix issues
 *      -- remark : missing movement can be added with step4
 *  Step3 reload default Graph an artefact
 *      -- use util/LoadArtWorld.java and set in ArtistBase the value LOAD_NG = "default"; run; then LOAD_NG = "artefact"; run;
 *  Step4  execute generateFiles2 (no parameter)
 *     -- new data are stored in data/tempload in 3 files 
 *     -- copy the set of triples (if there is some) into artMuseum.ttl , artMuseum2.ttl , artMovement.ttl, artCity.ttl, artCity2.ttl 
 *  Step5 execute apache/jena/artWorld/demo/validateArtefact.java
 *  
 *  Step6 reload world Graph an museum and movement
 *      -- use util/LoadArtWorld.java and set in ArtistBase the value LOAD_NG = "default"; run; then LOAD_NG = "artefact"; run;
 *  Step7 validate the triple store
 *     execute checkProperties.java then checkClasses.java then checkMissingLabel.java
 * 
 * @since 1.0
 * @author Marc Lieber
 * @version 1.1
 */

package apache.jena.artWorld.wikidata;