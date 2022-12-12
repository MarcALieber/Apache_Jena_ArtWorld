package apache.jena.artWorld.wikidata;

import apache.jena.artWorld.ArtistBase;

public class generateFiles2		extends ArtistBase
		{
		    public static void main( String[] args ) {
		    	//addOption("painters",args[0],true,null);
		    	//addOption("folderName",tempfolder2,true,null);
		        new generateFiles2().setArgs( args ).run();
		    }

		    @Override
		    public void run() {
		    	
		    	//String person = getOptions().getOption("folderName").getLongOpt();
		    	System.out.println("load missing Museum ,Movement and City from Wikidata ") ;
				deleteFiles del = new deleteFiles();
				del.delete(tempfolder2);

		    	// check missing mvmt
				createMovement task0 = new createMovement();
		    	task0.run();

		    	// check missing museum
		    	createMuseum task1 = new createMuseum();
		    	task1.run();
		    	
		    	// check missing City
		    	createCity task2 = new createCity();
		    	task2.run();
   
		    }
		    
		}