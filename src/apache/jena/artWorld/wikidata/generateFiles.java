package apache.jena.artWorld.wikidata;

import apache.jena.artWorld.ArtistBase;

public class generateFiles		extends ArtistBase
		{
		    public static void main( String[] args ) {
		    	addOption("painters",args[0],true,null);
		    	addOption("folderName",tempfolder,true,null);
		        new generateFiles().setArgs( args ).run();
		    }

		    @Override
		    public void run() {
		    	String person = getOptions().getOption("painters").getLongOpt();
		    	System.out.println("load from Wikidata : "+ person) ;
				deleteFiles del = new deleteFiles();
				del.delete(tempfolder);

				// artist infos + exposedIn
				createPerson task0 = new createPerson();
				task0.run();
				
				//paintings found in museum
				createPainting task1 = new createPainting();
				task1.run();
				System.out.println("End of file generation");
		    
		    }
		    
		}