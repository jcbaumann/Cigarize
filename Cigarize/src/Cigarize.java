/*
 * @author Jonathan Baumann
 * @version 0.0.1
 * 
 * description: This program reads a reference and a read sequence of an alignment from the command line and returns the corresponding CIGAR string.
 * 
 * test sequences:
 * reference: ACCATCCT GAACTGGCTAAC
 * read:   C TCGTAGAA  GGCT   
 */

import java.io.*;

public class Cigarize {

	public static void main(String[] args) throws IOException {
		
		// reference and read 
		String reference;
		String read;
		
		// BufferedReader to read the input of the command line
		BufferedReader buffer = new BufferedReader(new InputStreamReader(System.in));
		
		// print some informations about this tool
		System.out.println("You will be ask for a reference sequence and a read sequence. These sequences have to have the same length.\nThe position will be given as 1-based. This program is case sensitive so pleace respect this for your input.\r\r");
		
		// read the reference
		System.out.println("Reference:");
		reference = buffer.readLine();
		
		// read the read
		System.out.println("Read:");
		read = buffer.readLine();
		
		// call the cigarize function
		cigarize(reference, read);
	}
	
	/**
	 * cigarize
	 * returns the CIGAR string to a given alignment between a reference and a read
	 * 
	 * @param String reference
	 * @param String read
	 * @return void 
	 */
	public static void cigarize(String reference, String read) {
		
		// store the first position here (1-based)
		int position = -1;
		
		// store the cigar string in this variable
		String cigar = "";
		
		// temporary status (M, D, I, blank => not set 
		String status = " ";
		
		// counter to count the same chars in a row
		int counter = 0;
		
		// iterate over the read because it should be the shorter sequence
		for(int i = 0; i < read.length(); i++) {
			
			// match/missmatch 
			if(reference.charAt(i) != ' ' && read.charAt(i) != ' ') {
				
				// set the positon, counter and status 
				if(position == -1) {
					// set the position
					position = i+1;
					
					// set status
					status = "M";
					
					// start the counter
					counter = 1;
				}
				
				// if we already got our start point
				else {
					// we already got a match 
					if(status == "M") {
						// increase the coutner 
						counter++;
					}
					
					// if the status changes
					else {
						// save the status with the counter
						cigar += counter + status;
						
						// set the counter to 1
						counter = 1;
						
						// set the new status
						status = "M";
					}
				}
			}
			
			// insertion
			else if(reference.charAt(i) == ' ' && read.charAt(i) != ' ') {
				if(position != -1) {
					
					// if we already have an insertion 
					if(status == "I") {
						// increase the counter 
						counter++;
					}
					
					// if the status changes 
					else {
						// save the status with the counter 
						cigar += counter + status;
						
						// set the counter to 1
						counter = 1;
						
						// set the new status
						status = "I";
					}
				}
			}
			
			// deletion
			else if(reference.charAt(i) != ' ' && read.charAt(i) == ' ') {
				if(position != -1) {
					
					// if we already have the status of a deletion
					if(status == "D") {
						// increase the counter 
						counter++;
					}
					
					// if the status changes
					else {
						// save the status with the counter 
						cigar += counter + status;
						
						// set the counter to 1
						counter = 1;
						
						// set the new status
						status = "D";
					}
				}
			}
		}
		
		// if the last elements was a match/missmatch (insertions, deletions can not be at the start/end of an alignment)
		if(status == "M") {
			cigar += counter + status;
		}
		
		// print the cigar string to the console
		System.out.println("Pos: " + position);
		System.out.println("CIGAR:" + cigar);
	}
}
