import java.util.ArrayList;
import java.util.*;

/**
 * ISTE-612-2185 Lab #2
 * Pranav Jain
 * Date : 01-Mar-2019
 */

public class Lab2 {
 //attributes
	String[] myDocs;
	ArrayList<String> termDictionary;                  
	ArrayList<ArrayList<Doc>> docLists;
   int distance = 1;
	
	/**
	 * Constructor : a positional index 
	 * @param docs List of input strings or file names
	 * 
	 */
	public Lab2(String[] docs)
	{
		//TASK1: 
      myDocs = docs;
      termDictionary = new ArrayList<String>();
      
      docLists = new ArrayList<ArrayList<Doc>>();
      ArrayList<Doc> docList;
      
      for(int i=0;i< myDocs.length;i++) {
         String[] words = myDocs[i].split(" ");
         String word;
         
         for(int j=0;j<words.length;j++) {
            boolean match = false;
            word = words[j];
            if(!termDictionary.contains(word)) {
               termDictionary.add(word);
               docList = new ArrayList<Doc>();
               Doc doc = new Doc(i,j);
               docList.add(doc);
               docLists.add(docList);
            }
            else {
               int index = termDictionary.indexOf(word);
               docList = docLists.get(index);
               
               int k=0;
               for(Doc did:docList) {
                  if(did.docId == i) {
                     did.insertPosition(j);
                     docList.set(k, did);
                     match = true;
                     break;
                  }
                  k++;                 
               }
               if(!match) {
                  Doc doc = new Doc(i,j);
                  docList.add(doc);
               }
               
            }
         }
      }
           // System.out.println(termDictionary);
          //  System.out.println(docLists);
	}
	
	/**
	 * Return the string representation of a positional index
	 */
	public String toString()
	{
		String matrixString = new String();
		ArrayList<Doc> docList;
		for(int i=0;i<termDictionary.size();i++){
				matrixString += String.format("%-15s", termDictionary.get(i));
				docList = docLists.get(i);
				for(int j=0;j<docList.size();j++)
				{
					matrixString += docList.get(j)+ "\t";
				}
				matrixString += "\n";
			}
		return matrixString;
	}
	
	/**
	 * 
	 * @param post1 first postings
	 * @param post2 second postings
	 * @return merged result of two postings
	 */

	public ArrayList<Doc> intersect(ArrayList<Doc> post1, ArrayList<Doc> post2) {
      ArrayList<Doc> intersectList = new ArrayList<Doc>();
            
      ArrayList<Doc> qAL1 = post1; //docLists.get(termDictionary.indexOf(termDictionary.get(4)));
      ArrayList<Doc> qAL2 = post2; //docLists.get(termDictionary.indexOf(termDictionary.get(0)));
     
      int pAL1=0,pAL2=0;
      while(pAL1 < qAL1.size() && pAL2 < qAL2.size()) {
         if(qAL1.get(pAL1).docId == qAL2.get(pAL2).docId) {
            ArrayList<Integer> posAL1 = qAL1.get(pAL1).positionList;
            ArrayList<Integer> posAL2 = qAL2.get(pAL2).positionList;
            
            int pposAL1=0, pposAL2=0;
            
            while(pposAL1 < posAL1.size()) {
               while(pposAL2 < posAL2.size()) {
                  if(posAL1.get(pposAL1) - posAL2.get(pposAL2) == -1) {
                     
                        Doc docList = new Doc(post1.get(pAL1).docId, post2.get(pAL2).docId);
                                   
                        intersectList.add(docList);
                     break;
                  }
                  pposAL2++;
               }
               pposAL1++;
            }
            pAL1++;
            pAL2++;
         }
         else if(qAL1.get(pAL1).docId < qAL2.get(pAL2).docId) pAL1++;
         else pAL2++;
      }
      return intersectList;
}


	/**
	 * 
	 * @param query a phrase query that consists of any number of terms in the sequential order
	 * @return ids of documents that contain the phrase
	 */
 	public ArrayList<Doc> phraseQuery(String[] query)
 	{
   
      ArrayList<Doc> intersectList1 = new ArrayList<Doc>();
      String[] q = query;

      for(int x=0;x<q.length-1;x++){
   
           for(int y=1;y<q.length;y++){

       
      ArrayList<Doc> qAL11 = docLists.get(termDictionary.indexOf(q[x]));
      ArrayList<Doc> qAL22 = docLists.get(termDictionary.indexOf(q[y]));
 
      intersectList1 = intersect(qAL11, qAL22);
      }
    
  }  return intersectList1;
}

   /**
     *  main method              
     *  @param args               Main args
     *  docs     :                List of input strings.
     */
	
	public static void main(String[] args)	{
    
      String[] docs = {"data text warehousing over big data",
                       "dimensional data warehouse over big data",
                       "nlp before text mining",
                       "nlp before text classification"};                      
                       
		Lab2 pi = new Lab2(docs);
      System.out.println("\n"+"Task 1"+"\n");
		System.out.print(pi);
      
        System.out.println("--------------------------------------------------------------------------------------------------------------------------------------------");

        System.out.println("Task 2" +"\n");
        System.out.println("Checking for 2nd ('text') and 3rd ('warehousing') posting lists : ");
        
        ArrayList<Doc> result = pi.intersect(pi.docLists.get(1),pi.docLists.get(2));
        if(result == null)
        {
            System.out.println("<>");
        }
        else {
            System.out.println(result);
        }
		  
        System.out.println("--------------------------------------------------------------------------------------------------------------------------------------------");

        System.out.println("Task 3"+"\n");
        System.out.println("Checking for multiple terms");
    
        Scanner s = new Scanner(System.in);
        System.out.println("Enter a phrase query of words");

        String phraseQuerytest = s.nextLine();
        String[] resultwords = phraseQuerytest.split(" ");

        ArrayList<Doc> resulttest = pi.phraseQuery(resultwords);

            //    System.out.println("Found in :"+resulttest);
            
          for(Doc did:resulttest) {
             System.out.println("Found in document index :"+did.docId);         
         }
        
        //TASK4: design and test phrase queries with 2-5 terms
          
        System.out.println("--------------------------------------------------------------------------------------------------------------------------------------------");

        System.out.println("Task 4 : Searching for 'nlp before text'"+"\n");
 
        String phraseQuerytest1 = "nlp before text";
        String[] resultwords1 = phraseQuerytest1.split(" ");
 
       ArrayList<Doc> resulttest1 = pi.phraseQuery(resultwords1);

        //    System.out.println("Found :"+resulttest1);
          
        for(Doc did:resulttest1) {
             System.out.println("Found in document index :"+did.docId);         
         }
  }
}

/**
 * 
 * Document class that contains the document id and the position list
 */
 
class Doc{
	int docId;
	ArrayList<Integer> positionList;
	public Doc(int did)
	{
		docId = did;
		positionList = new ArrayList<Integer>();
      
	}
	public Doc(int did, int position)
	{
		docId = did;
		positionList = new ArrayList<Integer>();
		positionList.add(new Integer(position));
	}
	
	public void insertPosition(int position)
	{
		positionList.add(new Integer(position));
	}
	
	public String toString()
	{
		String docIdString = ""+docId + ":<";
		for(Integer pos:positionList)
			docIdString += pos + ",";
		docIdString = docIdString.substring(0,docIdString.length()-1) + ">";
		
      return docIdString;		
	}
   
}
