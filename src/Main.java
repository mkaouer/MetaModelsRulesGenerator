import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;
import java.util.Vector;


public class Main {
	 
	 @SuppressWarnings("null")
	 /*Method - generateRules: will randomly generateRules based on the source MetaModel and the Target Metal Model provided.  
	  *Variable - SourceRandom and TargetRandom are used to determine what random chosen predicate or prefix should be chosen based on the MetaModel.
	  *Variable - randomNumOfRules is the random number of rules that will be generated to be tested.  I times it by 2 in the for loop because of the way I chose
	  *           to implement the randomness.  The first and second spot in the text file is one rule not two.  The first spot is the source and the second is the expected 
	  *           target.  So by multiplying it by two I will randomly get the correct number of random rules.
	  *Variable - prefixSrcMetaModel/prefixTarMetaModel/predicateSrcMetaModel/predicateTarMetaModel is used as an intermediate step to hold the random rules until I combine
	  *           them into the text file at the end of the method.
	  *Variable - fileWriter is to write the random generated rules to the text file(rules) to be tested in the main.
	  *Returns the number of rules generated to be used for the fitness functions.
	  */
	static long generateRules(ArrayList<String> srcMetaModel, ArrayList<String> tarMetaModel) throws IOException
	 {
		 Random random_generator = new Random();
		 int sourceRandom = 0, targetRandom = 0; 
		 long randomNumOfRules = 0;
		 
		 ArrayList<String> generatedRules = new ArrayList<String>();
		 ArrayList<String> prefixSrcMetaModel = getPrefix(srcMetaModel);
		 ArrayList<String> prefixTarMetaModel = getPrefix(tarMetaModel);
		 ArrayList<String> predicateSrcMetaModel = getPredicate(srcMetaModel);
		 ArrayList<String> predicateTarMetaModel = getPredicate(tarMetaModel);
		 randomNumOfRules = random_generator.nextInt(1000);
		 System.out.println("randomNumOfRules" + randomNumOfRules);
		 
		 //Source and target metamodel randomly generate Rules.  Times randomNumOfRules by two if the rule is odd.
		 for(int i = 0; i < randomNumOfRules*2; i++)
		 {
			 if(i % 2 == 0)
			 {
				 //The size of the srcMetaModel because it will go out of bounds if bigger
				 sourceRandom = random_generator.nextInt(prefixSrcMetaModel.size());
			   //Add one of the source metamodel's prefixes to the generated rules
				 generatedRules.add(i,prefixSrcMetaModel.get(sourceRandom));
				//The size of the srcMetaModel because it will go out of bounds if bigger
				 sourceRandom = random_generator.nextInt(prefixSrcMetaModel.size());
			   //Add one of the source metamodel's predicates to the generated rules
				 generatedRules.set(i,generatedRules.get(i).concat(predicateSrcMetaModel.get(sourceRandom)));
			 }
			 else
			 {
				//The size of the tarMetaModel because it will go out of bounds if bigger
				targetRandom = random_generator.nextInt(prefixTarMetaModel.size());
				//Add one of the target metamodel's prefixes to the generated Rules
				generatedRules.add(i, prefixTarMetaModel.get(targetRandom));
			  //The size of the srcMetaModel because it will go out of bounds if bigger
				targetRandom = random_generator.nextInt(prefixTarMetaModel.size());
				//Add one of the target metamodel's predicates to the generated Rules
			  generatedRules.set(i, generatedRules.get(i).concat(predicateTarMetaModel.get(targetRandom)));
			 }
		 }
		 FileWriter fileWriter = null;
		 File rules = new File("src/rules.txt");
		 fileWriter = new FileWriter(rules);
		 for(int i = 0; i < randomNumOfRules*2; i++)
		 {
       fileWriter.write(generatedRules.get(i));
       if(i < (randomNumOfRules*2)-1)
         fileWriter.write("\n");
		 }
     fileWriter.close();
		 System.out.println("generatedRules" + generatedRules);
		 System.out.println("size of generatedRules: " + generatedRules.size());
		 return randomNumOfRules;
	 }
	/*Main Driver
	 * Method - main will be the driver for the program.  Run a loop in the main program to keep going until the desired number of iterations is entered which will run the
	 *          program in a loop to keep mutating and checking rules.
	 * Variable - src holds the source of the model to be tested.  Reads into an ArrayList of Strings from a text file(src).
	 * Variable - srcMetaModel is the source of the MetaModel.  Reads into an ArrayList of Strings from a text file(src_metamodel).
	 * Variable - tarMetaModel is the target of the MetaModel.  Reads into an ArrayList of Strings from a text file(tar_metamodel).
	 * Variable - randomNumOfRules is to be used to pass to the fitness function.
	 * Variable - traceLinks is the traceablity links to be used as a good transformation to be tested against.  Reads into an ArrayList of Strings from a text file(traceLinks).
	 * Variable - rules is the rules to be tested.  Reads into an ArrayList of Strings from a text file(rules).
	 * Variable - numRules is the number of rules created.  Is used in the fitness function.  Divide by two because the link is on two lines.
	 * Variable - numBadRules is to hold the number of bad Rules still there at the end of the program.
	 */
	 public static void main(String[] args) throws IOException 
	 { 
		 
		 ArrayList<String> src = new ArrayList<String>();
		 src = Strings.readLines("src/src.txt");
		 System.out.println("Source: " + src);
		 
		 ArrayList<String> srcMetaModel = new ArrayList<String>();
		 srcMetaModel = Strings.readLines("src/src_metamodel.txt");
		 System.out.println("Source Meta Model: " + srcMetaModel);
		 
		 ArrayList<String> tarMetaModel = new ArrayList<String>();
		 tarMetaModel = Strings.readLines("src/tar_metamodel.txt");
		 System.out.println("Target Meta Model: " + tarMetaModel);
		 
		 long randomNumOfRules = generateRules(srcMetaModel,tarMetaModel);
		 
		 ArrayList<String> traceLinks = new ArrayList<String>();
		 traceLinks = Strings.readLines("src/traceLinks.txt");
		 System.out.println("Traceability Links: " + traceLinks);
		 
		 ArrayList<String> rules = new ArrayList<String>();
		 rules = Strings.readLines("src/rules.txt");
		 double numRules = rules.size()/2;
		 double numBadRules = 0;
		 
		 ArrayList<String> badRules = new ArrayList<String>();
		 for(int w = 0; w < 100; w++)
		 {
			 System.out.println("Iteration: " + w);
			 badRules.clear();
			 if(rules.size()==0)
				 break;
		   checkTransformations(src,rules,traceLinks,badRules);
		   System.out.println("Number of Bad Rules: " + badRules.size()/2);
		   if(badRules.size()!=0)
		     mutate(badRules,traceLinks,srcMetaModel,tarMetaModel);
		   rules.clear();
		   if(badRules.size()!=0)
		     rules.addAll(badRules);
		   else
		  	 break;
		 }
		 numBadRules = badRules.size()/2;
		 System.out.println("BadRules: " + badRules);
		 fitnessFunction(numBadRules,numRules,randomNumOfRules);
		 
	 }
	 
	 /*Filters the target from the traceLinks into an ArrayList<String>.*/
	 public static ArrayList<String> filterTarget (ArrayList<String> traceLinks)
	 {
		 ArrayList<String> temp = new ArrayList<String>();
		 temp.addAll(traceLinks);

	   for(int i = 0; i < temp.size(); i+=1)
	   {
	  	 temp.remove(i);
	  	 temp.trimToSize();
	   }
	   return temp;
	 }
	 
	 /*Filters the source of the traceLinks to a single ArrayList<String>.*/
	 public static ArrayList<String> filterSource (ArrayList<String> traceLinks)
	 {
		 ArrayList<String> temp = new ArrayList<String>();
		 temp.addAll(traceLinks);
	   
	   for(int i = 1; i < temp.size(); i+=1)
	   {
	  	 temp.remove(i);
	  	 temp.trimToSize();
	   }
	   return temp;
	 }
	 
	 /*
	  * Method - checkTransformations is the hierarchy method to call both the checkPrefixes method and checkParams method.
	  */
	 public static void checkTransformations(ArrayList<String> src,ArrayList<String> rules, ArrayList<String> traceLinks, ArrayList<String> badRules) throws IOException
	 {
		 checkPrefix(rules,traceLinks, badRules);
	 }
	 
	 /*
	  * Method - checkPrefix will check the rules source prefix to the rules target prefix against all the transformation links to see if that is a possible transformation
	  *          to be performed.  If it is not, the rule will be outputed as incorrect and put into the badRules ArrayList<String>.  
	  * Variable - subString is used as an intermediate ArrayList<String> so just the prefixes are in the list and don't have to worry about comparing predicates yet. 
	  */
	 public static void checkPrefix(ArrayList<String> rules, ArrayList<String> traceLinks, ArrayList<String> badRules)
	 {
		 ArrayList<String> subString = new ArrayList<String>();
		 subString.addAll(rules);

		 if(subString.isEmpty() != true)
		 {
		   for(int i = 0; i < subString.size(); i++)
		   {
			   int leftParam = subString.get(i).indexOf("(");
			   int rightParam = subString.get(i).indexOf(")");
			   String substring = subString.get(i).substring(leftParam,rightParam+1);
			   String replace = subString.get(i).replace(substring, "");
			   subString.set(i, replace);
			   subString.get(i).trim();
		   }

		   for(int i = 0; i < subString.size(); i+=2)
		   {
			   for(int j = 0; j < traceLinks.size(); j++)
			   {
			     if((traceLinks.get(j).indexOf(subString.get(i))) != -1)
			     {
				     if((traceLinks.get(j+1).indexOf(subString.get(i+1))) == -1)
				     {
					     System.out.println("Rule: " + rules.get(i) + " : " + rules.get(i+1) + " is NOT accecpted");
					     badRules.add(rules.get(i));
					     badRules.add(rules.get(i+1));
				     }
				     break;
			     }
			   }
		   }
		 }

		 checkParams(rules,traceLinks,subString,badRules);
	 }
	 /*
	  * Method - checkParams is a function to check the correct number of parameters against the possible transformation links parameters.  If the rule has
	  *          to few or to many parameters in either the rules source or rules target it will be outputed as a bad Rule and put into the ArrayList<String>
	  *          of bad Rules.  Also it checks to make sure that if it is a special transformation like attribute, generalization, or association it makes sure that
	  *          the primary keys are correct and not primary foreign keys.  
	  * Variable - temp is used as an intermediate step in checkParams.  It takes all of the rules, rules.  
	  */
	 public static void checkParams(ArrayList<String> rules, ArrayList<String> traceLinks,ArrayList<String> subString, ArrayList<String> badRules)
	 {
	   ArrayList<String> temp = new ArrayList<String>();
	   temp.addAll(rules);
	   if(!rules.isEmpty())
	   {
	  	  /*Counts number of params of the Rule*/
			   for(int a = 0; a < subString.size(); a+=2)
			   {
				   for(int j = 0; j < traceLinks.size(); j++)
				   {
				  	 int indexForRuleSource = rules.get(a).indexOf(",");
					   int paramsForRuleSource = 1;
					   int indexForRuleTarget = rules.get(a+1).indexOf(",");
					   int paramsForRuleTarget = 1;

					   while(indexForRuleSource != -1)
					   {
					  	 indexForRuleSource = rules.get(a).indexOf(",", indexForRuleSource+1);
					  	 paramsForRuleSource++;
					   }
					   while(indexForRuleTarget != -1)
					   {
					  	 indexForRuleTarget = rules.get(a+1).indexOf(",", indexForRuleTarget+1);
					  	 paramsForRuleTarget++;
					   }
             /*If the type of the rule is the same as the traceablility link check the traceablility links parameters and compare it*/
				     if((traceLinks.get(j).indexOf(subString.get(a))) != -1)
				     {
					     if((traceLinks.get(j+1).indexOf(subString.get(a+1))) != -1)
					     {
						     int indexForTraceLinkSource = traceLinks.get(j).indexOf(",");
						     int paramsForTraceLinkSource = 1;
						     int indexForTraceLinkTarget = traceLinks.get(j+1).indexOf(",");
						     int paramsForTraceLinkTarget = 1;
						     
						     while(indexForTraceLinkSource != -1)
						     {
						    	 indexForTraceLinkSource = traceLinks.get(j).indexOf(",", indexForTraceLinkSource+1);
						    	 paramsForTraceLinkSource++;
						     }
						     while(indexForTraceLinkTarget != -1)
						     {
						    	 indexForTraceLinkTarget = traceLinks.get(j+1).indexOf(",", indexForTraceLinkTarget+1);
						    	 paramsForTraceLinkTarget++;
						     }
						     /*Checks to see if the Rule is correct or incorrect.*/
						     if((paramsForTraceLinkSource != paramsForRuleSource) || (paramsForTraceLinkTarget != paramsForRuleTarget))
						     {
						    	 if(checkDuplicates(badRules,rules.get(a),rules.get(a+1))!=true)
			    			   {
			    			  	 System.out.println("Rule: " + rules.get(a) + " : " + rules.get(a+1) + " is NOT accecpted");
			    			     badRules.add(rules.get(a));
							       badRules.add(rules.get(a+1));
			    			   }
						     }
						     else if(subString.get(a).equalsIgnoreCase("Attribute"))//Special Case
					    	 {
					    		 String lastParamRuleSource = rules.get(a).substring(rules.get(a).lastIndexOf(",")+1, rules.get(a).length()-1);
					    		 String lastParamRuleTarget = rules.get(a+1).substring(rules.get(a+1).lastIndexOf(",")+1, rules.get(a+1).length()-1);
					    		 String lastParamTraceLinksSource = traceLinks.get(j).substring(traceLinks.get(j).lastIndexOf(",")+1, traceLinks.get(j).length()-1);
					    		 String lastParamTraceLinksTarget = traceLinks.get(j+1).substring(traceLinks.get(j+1).lastIndexOf(",")+1, traceLinks.get(j+1).length()-1);

					    		 //prevents unnecessary loops from being outputted.
					    		 if(lastParamRuleSource.equalsIgnoreCase(lastParamTraceLinksSource) != true)
					    			 break;
					    		 if((lastParamRuleSource.equalsIgnoreCase(lastParamTraceLinksSource) != true) || (lastParamRuleTarget.equalsIgnoreCase(lastParamTraceLinksTarget) != true))
					    		 {
					    			 if(checkDuplicates(badRules,rules.get(a),rules.get(a+1))!=true)
				    			   {
				    			  	 System.out.println("Rule: " + rules.get(a) + " : " + rules.get(a+1) + " is NOT accecpted");
				    			     badRules.add(rules.get(a));
								       badRules.add(rules.get(a+1));
				    			   }
					    		 }
					    	 }
						     else if(subString.get(a).equalsIgnoreCase("Generalization"))//Special Case
						     {
						    	 String lastParamRuleTarget = rules.get(a+1).substring(rules.get(a+1).lastIndexOf(",")+1, rules.get(a+1).length()-1);
						    	 String lastParamTraceLinksTarget = traceLinks.get(j+1).substring(traceLinks.get(j+1).lastIndexOf(",")+1, traceLinks.get(j+1).length()-1);
						    	 if(lastParamRuleTarget.equalsIgnoreCase(lastParamTraceLinksTarget) != true)
						    	 {
						    		 if(checkDuplicates(badRules,rules.get(a),rules.get(a+1))!=true)
				    			   {
				    			  	 System.out.println("Rule: " + rules.get(a) + " : " + rules.get(a+1) + " is NOT accecpted");
				    			     badRules.add(rules.get(a));
								       badRules.add(rules.get(a+1));
								       break;
				    			   }
						    	 }
						     }
						     else if(subString.get(a).equalsIgnoreCase("Association"))//Special Case
						     {
						    	 int findLastMultiplicityTraceLinks = traceLinks.get(j).indexOf(",");
							     int findLastMultiplicityRule = rules.get(a).indexOf(",");
							     for(int b = 0; b < 2; b++)
							     {
							    	 findLastMultiplicityTraceLinks = traceLinks.get(j).indexOf(",", findLastMultiplicityTraceLinks+1);
							    	 findLastMultiplicityRule = rules.get(a).indexOf(",", findLastMultiplicityRule+1);
							     }
							     String checkMultiplicityTraceLinks = traceLinks.get(j).substring(findLastMultiplicityTraceLinks+1, findLastMultiplicityTraceLinks+2);
							     String checkMultiplicityRule = rules.get(a).substring(findLastMultiplicityRule+1, findLastMultiplicityRule+2);
							     if((checkMultiplicityTraceLinks.equalsIgnoreCase(checkMultiplicityRule)) == true)
							     {
							       String lastParamRuleTarget = rules.get(a+1).substring(rules.get(a+1).lastIndexOf(",")+1, rules.get(a+1).length()-1);
						    	   String lastParamTraceLinksTarget = traceLinks.get(j+1).substring(traceLinks.get(j+1).lastIndexOf(",")+1, traceLinks.get(j+1).length()-1);
						    	 
							       if((lastParamRuleTarget.equalsIgnoreCase(lastParamTraceLinksTarget) != true))
					    		   {
					    			   if(checkDuplicates(badRules,rules.get(a),rules.get(a+1))!=true)
					    			   {
					    			  	 System.out.println("Rule: " + rules.get(a) + " : " + rules.get(a+1) + " is NOT accecpted");
					    			     badRules.add(rules.get(a));
									       badRules.add(rules.get(a+1));
					    			   }
					    		   }
						       }
						     }
					     }
				     }
				   }
		   }
	   }
	 }
	 
	 /*
	  * Method - mutate is a method to change the predicates and/or prefixes around to try to correct the badRules that were generated.  It does this by referring to the 
	  *          metaModel to use its predicates and prefixes.  
	  * Variables - sourceRandom and targetRandom are used to determine wheather the prefix or predicate will be mutated respectively to the name of the variable.
	  * Variables - sourceRandomPick and targetRandomPick are used to randomly select a predicate or prefix in the metamodel to switch with the rules.
	  * Variables - sourcePrefixBadRules/targetPrefixBadRules are used as intermediate ArrayList<String> to store the prefixes of the bad rules.
	  * Variables - prefixSrcMetaModel/prefixTarMetaModel are used as intermediate ArrayList<String> to store the prefixes of the metamodels.
	  * Variables - sourcePredicateBadRules/targetPredicateBadRules are used as intermediate ArrayList<String> to store the predicates of the bad rules.
	  * Variables - predicateSrcMetaModel/predicateTarMetaModel are used as intermediate ArrayList<String> to store the predicates of the metamodels.
	  */ 
	 public static void mutate(ArrayList<String> badRules,ArrayList<String>traceLinks,ArrayList<String> srcMetaModel, ArrayList<String> tarMetaModel)
	 {
		 Random random_generator = new Random();
		 int sourceRandom = 0, targetRandom = 0, sourceRandomPick = 0, targetRandomPick;
		 
		 ArrayList<String> sourcePrefixBadRules = filterSource(getPrefix(badRules));
		 ArrayList<String> targetPrefixBadRules = filterTarget(getPrefix(badRules));
		 ArrayList<String> prefixSrcMetaModel = getPrefix(srcMetaModel);
		 ArrayList<String> prefixTarMetaModel = getPrefix(tarMetaModel);
		 ArrayList<String> sourcePredicateBadRules = filterSource(getPredicate(badRules));
		 ArrayList<String> targetPredicateBadRules = filterTarget(getPredicate(badRules));
		 ArrayList<String> predicateSrcMetaModel = getPredicate(srcMetaModel);
		 ArrayList<String> predicateTarMetaModel = getPredicate(tarMetaModel);

		 //Source mutation
		 for(int i = 0; i < sourcePrefixBadRules.size(); i+=2)
		 {
			 sourceRandom = random_generator.nextInt();
			 sourceRandomPick = random_generator.nextInt(prefixSrcMetaModel.size());
			 //Change one of the source prefix badRules with one of the metamodels source prefix
			 if(sourceRandom >= 0)
				 sourcePrefixBadRules.set(i,prefixSrcMetaModel.get(sourceRandomPick));
			 //Change one of the source predicates badRules with one of the metamodels source predicates
			 else
				 sourcePredicateBadRules.set(i,predicateSrcMetaModel.get(sourceRandomPick));
		 }
		 //Target Mutation
		 for(int i = 1; i < targetPrefixBadRules.size();i+=2)
		 {
			 targetRandom = random_generator.nextInt();
			 targetRandomPick = random_generator.nextInt(prefixTarMetaModel.size());
			 //Change one of the target prefix badRules with one of the metamodels target prefix
			 if(targetRandom >= 0)
				 targetPrefixBadRules.set(i, prefixTarMetaModel.get(targetRandomPick));
			 //Change one of the target predicates badRules with one of the metamodels target predicates
			 else
				 targetPredicateBadRules.set(i, predicateTarMetaModel.get(targetRandomPick));
		 }

		 //Combine them back into Rules
		 for(int i = 0; i < sourcePrefixBadRules.size(); i++)
		 {
			 sourcePrefixBadRules.set(i, sourcePrefixBadRules.get(i).concat(sourcePredicateBadRules.get(i)));
			 targetPrefixBadRules.set(i, targetPrefixBadRules.get(i).concat(targetPredicateBadRules.get(i)));
		 }

		 badRules.clear();
		 for(int i = 0; i < sourcePrefixBadRules.size(); i++)
		 {
			 badRules.add(sourcePrefixBadRules.get(i));
			 badRules.add(targetPrefixBadRules.get(i));
		 }
	 }
	 
	 /*
	  * Method - getPrefix will get just the prefixes of the rules.  If the List is empty an error is thrown.
	  * Variable - temp is used to manipulate the strings and get just the prefixes.
	  * Returns the temp of type ArrayList<String> with just the prefixes
	  */
	 public static ArrayList<String> getPrefix(ArrayList<String> prefix)
	 {
		 ArrayList<String> temp = new ArrayList<String>();
		 temp.addAll(prefix);
		 if(temp.isEmpty() != true)
		 {
		   for(int i = 0; i < temp.size(); i++)
		   {
			   int leftParam = temp.get(i).indexOf("(");
			   int rightParam = temp.get(i).indexOf(")");
			   String substring = temp.get(i).substring(leftParam,rightParam+1);
			   String replace = temp.get(i).replace(substring, "");
			   temp.set(i, replace);
			   temp.get(i).trim();
		   }
		 }
		 else
		 {
			 System.out.println("The model you are trying to get prefixes for is empty.");
			 System.exit(1);
		 }
	   return temp;
	 }
	 
	 /*
	  * Method - getPredicate will take an ArrayList<String> and will separate the predicates from the prefixes.  If the List is empty an error is thrown.
	  * Variable - temp is used to manipulate the strings and get just the predicates.
	  * Returns the temp of type ArrayList<String> with just the predicates
	  */
	 public static ArrayList<String> getPredicate(ArrayList<String> predicate)
	 {
		 ArrayList<String> temp = new ArrayList<String>();
		 temp.addAll(predicate);
		 
		 if(temp.isEmpty() != true)
		 {
			 for(int i = 0; i < temp.size(); i++)
			 {
			   int leftParam = temp.get(i).indexOf("(");
		     int rightParam = temp.get(i).indexOf(")");
		     String substring = temp.get(i).substring(leftParam,rightParam+1);
		     temp.set(i, substring);
			   temp.get(i).trim();
			 }
		 }
		 else
		 {
			 System.out.println("The model you are trying to get predicates for is empty.");
			 System.exit(1);
		 }
     return temp;
	 }
	 
	 /*
	  * Method - checkDuplicates is used to make sure that the rule that is about to be evaluated is not a duplicate and not already in the badRules list.
	  * Returns false if no duplicates or true if there are duplicates.
	  */
	 public static boolean checkDuplicates(ArrayList<String> badRules, String source, String target)
	 {
		for(int g = 0; g < badRules.size(); g++)
		{
			if(badRules.get(g).equals(source)&&badRules.get(g+1).equals(target))
				return true;
		}
		return false;	 
	 }
	 
	 /*
	  * Method - fitnessFunction will get the fitnessFunction of this transformation testing.  This transformation testing is just done with precision and not with recall
	  *          because the expected rules is the same number as the total number of rules.
	  * Variable - fitness will hold the number of the fitness.
	  */
	 public static void fitnessFunction(double numBadRules, double numRules, long randomNumOfRules)
	 {
		 double fitness = ((numRules-numBadRules)/numRules);
		 System.out.println("Fitness: " + fitness);
	 }
}
   

