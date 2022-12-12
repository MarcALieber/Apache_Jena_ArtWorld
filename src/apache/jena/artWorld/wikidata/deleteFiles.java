package apache.jena.artWorld.wikidata;

import java.io.File;

import apache.jena.artWorld.ArtistBase;

public class deleteFiles		extends ArtistBase
		{
		    public static void main( String[] args ) {
		    	addOption("folderName",args[0],true,null);
		        new deleteFiles().setArgs( args ).run();
		    }

		    @Override
		    public void run() {
		    	String folderName = getOptions().getOption("folderName").getLongOpt();
		    	System.out.println("Delete files from "+ folderName) ;
		    	delete(folderName);
		    }
		    
			public void delete(String ...args)  {
				String folderName = args[0];
				//delete folder recursively
		        recursiveDelete(new File(folderName));
		        //creating a File object
		        File file = new File(folderName);
		        //creating the directory
		        boolean bool = file.mkdir();
		        if(bool){
		            System.out.println("Directory created successfully");
		        }else{
		            System.out.println("Directory not created");
		        }
		    }
			
			public static void recursiveDelete(File file) {
		        //to end the recursive loop
		        if (!file.exists())
		            return;
		        
		        //if directory, go inside and call recursively
		        if (file.isDirectory()) {
		            for (File f : file.listFiles()) {
		                //call recursively
		                recursiveDelete(f);
		            }
		        }
		        //call delete to delete files and empty directory
		        file.delete();
		        System.out.println("Deleted file/folder: "+file.getAbsolutePath());
		    }
		} 
