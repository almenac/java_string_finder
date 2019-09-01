import java.io.*; import java.util.*;
/*
Ville Kauppila
kauppila.ville.k@student.uta.fi
430962

This program loads a txt file, loads its contents and prints
them as either ASCII graphics or numbers. A filtering option 
and output to a new file is also available.
*/

public class Laki2HT2{
    //Constants for messages
    final static String CLPERR = "Invalid command-line argument!";
    final static String BYE = "Bye, see you soon.";

    public static void main(String[] args){
        //Quit flag
        boolean quit = false;
        
        //Greeting text
        printLogo();

        //Only proceed if CLP is ok
        if(args.length == 1){
            //Check if arg and save to variable            
            String fileName = args[0];
            //Load to int array            
            int[][] iArray = loadToIntArray(fileName);
            
            //While loop that presents CLI to user and executes commands
            //until it gets a quit command
            while(!quit){
                //Ask for command
                System.out.println("printa/printi/info/filter [n]/reset/quit?");

                //Read command from user
                String command = In.readString();

                //Print as ASCII
                if(command.equals("printa")){                                    
                    //Pass int array to operation that converts it to char array
                    char[][] cArray = integersToCharacters(iArray);
                    //Print the char array
                    print2DCharacterArray(cArray);
                }
                //Print as integers
                else if(command.equals("printi")){                
                    print2DIntegerArray(iArray, 2);                
                }
                //Print file info
                else if(command.equals("info")){                                    
                    printInfo(iArray);                
                }
                //Use filter                 
                else if(command.equals("filter 3")){                                    
                    //Extract the filter from command
                    int filter = findFilter(command);
                    //Pass the array and filter size to filter operation
                    iArray = filterArray(iArray, filter);    
                }
                //Loads the int array from file again
                else if(command.equals("reset")){                                    
                    iArray = loadToIntArray(fileName);
                }
                //Quit program
                else if(command.equals("quit")){
                    System.out.println(BYE);
                    quit = true;
                }    
            }
        }
        else{
            System.out.println(CLPERR);
            System.out.println(BYE);
            quit = true;
        }       
    }

    //Returns a 2d int array with a CLP arg text file's contents loaded into it
    public static int[][] loadToIntArray(String parFile){
        //Exception flag
        boolean ex = false;
        
        //Get size of file, save to variable
        int[] arraySize = countSize(parFile);
        
        if(arraySize != null){
            //Size of rows and columns
            int rows = arraySize[0];
            int cols = arraySize[1];

            //Create new array
            int[][] retArray = new int[rows][cols];

            //Try/except error handling
            try{
                //Create file object
                File openFile = new File(parFile);
                
                //Create scanner
                Scanner reader = new Scanner(openFile);
                
                //Loop through rows
                for(int i = 0; i < rows; i++){
                    //Initialize row
                    String line = reader.nextLine();
                    //Split by spaces
                    String[] stringParts = line.split(" ");
                    //Enter string array elements into the int array
                    for(int z = 0; z < stringParts.length; z++){
                        retArray[i][z] = Integer.parseInt(stringParts[z]);                        
                    }                                    
                }
                //Close scanner
                reader.close();
            }
            catch(FileNotFoundException e){
                //System.out.println("Exception filenotfound");
                ex = true;
            }
            catch(Exception e){
                //System.out.println("Exception e");
                ex = true;
            }

            //Return the result array if valid, otherwise null
            if(!ex){
                //System.out.println("Ret array no exception.");
                return retArray;
            }
            else{
                //System.out.println("Exception in the ret arra!");
                return null;
            }        
        }
        else{
            return null;
        }
    }

    //Extracts the filter integer from param string
    
    public static int findFilter(String command){
        //Split command into parts separated by spaces
        String[] commParts = command.split(" ");                    
        //Extract the filter size from user command's second part
        int filter = Integer.parseInt(commParts[1]);      
        return filter;
    }

    //Filters the preloaded int array with user's filter
    public static int[][] filterArray(int[][] origArray, int filterSize){
        //Initialize the filtered array with original's parameters
        int[][] filteredArray = new int[origArray.length][origArray[0].length];
        //Fill the filtered array with original's elements
        filteredArray = origArray;

        //Define starting point
        int startPoint = filterSize / 2;        

        //Go through the copy and filter it by averages. Offset by starting point.
        for(int i = startPoint; i < filteredArray.length - startPoint; i++){            
            for(int j = startPoint; j < filteredArray[0].length - startPoint; j++){
                                
                //Variable for filter's sum
                int filterSum = 0;
                
                //Go through the filter area
                for(int q = 0; q < filterSize; q++){    
                    for(int w = 0; w < filterSize; w++){                        
                        //Update the filter's sum with the value of current index
                        filterSum += filteredArray[q][w + j - startPoint];
                    }                    
                }
                                
                //Average of all the filter's contents
                int filterAvg = Math.round(filterSum / (filterSize * filterSize));
                
                //Replace the filter's middle index with the average
                filteredArray[i][j] = filterAvg;
            }            
        }        
        //Return the filtered array with new values
        return filteredArray;
    }
    
    //Counts the number of matches from int array
    //Returns array for results and size of the original array or null
    public static void printInfo(int[][] parArray){
        //Proceed only if param array not null
        if(parArray != null){
            //Load char array for reference
            char[] convArray = loadConvArray();
            
            //Initialize array for counting
            int[] countArray = new int[convArray.length];
            
            //Counting the param array        
            for(int i = 0; i < parArray.length; i++){            
                for(int j = 0; j < parArray[0].length; j++){
                    //Go through the count array for every element, add to corresponding index
                    for(int z = 0; z < countArray.length; z++){
                        if(parArray[i][j] == z){
                            countArray[z] += 1;
                        }
                    }
                }
            }

            //Print info
            //Size
            System.out.println(parArray.length + " x " + parArray[0].length);
            //Print counts of individual chars
            for(int i = 0; i < countArray.length; i++){
                //Convert index to char and print count
                System.out.println(convertToCharacter(i) + " " + countArray[i]);    
            }        
        }        
    }

    //Converts int array into char array and 
    //Returns char array if valid, null otherwise
    public static char[][] integersToCharacters(int[][] parArray){
        //Check that param array is not null
        if(parArray != null){
            //Initialize int array with the same size as param array
            char[][] charArray = new char[parArray.length][parArray[0].length];

            //Loop through param array
            for(int rows = 0; rows < parArray.length; rows++){
                //Go through columns
                for(int cols = 0; cols < parArray[rows].length; cols++){
                    //Place converted char into array
                    charArray[rows][cols] = convertToCharacter(parArray[rows][cols]);
                }
            }            
            //Return result int array
            return charArray;
            }
        else{
            //Return null if null param
            return null;
        }
    }

    //Finds character from array that matches a param integer
    //Returns char if found, Character.MAX_VALUE if not
    public static char convertToCharacter(int parInt){        
        //Load the conversion array
        char[] code = loadConvArray();
        if(parInt >= 0 && parInt <= 15){
            return code[parInt];
        }
        else{
            return Character.MAX_VALUE;
        } 
    }

    //Loads the character conversion array
    //Returns the char array
    public static char[] loadConvArray(){
        //Define the char conversion array
        char[] code = new char[] {'#', '@', '&', '$', '%', 'x', '*', 'o', '|', '!', ';', ':', '\'', ',', '.', ' '};
        return code;
    }

    //Returns the number of lines in a parameter file
    public static int[] countSize(String parFile){
        //Exception boolean
        boolean ex = false;
        
        //Number of counted rows in file
        int countedRows = 0;
        
        //Number of counted columns in file
        int countedCols = 0;

        //Try/except error handling
        try{
            //Create file object
            File openFile = new File(parFile);
            
            //Create scanner
            Scanner reader = new Scanner(openFile);
            
            //Loop through file's lines, count rows and cols
            while(reader.hasNextLine()){
                //Change line
                String line = reader.nextLine();
                //Count cols after first row
                if(countedRows == 1){
                    //Split first row to array to get nr of elements
                    String[] colParts = line.split(" ");
                    //Count cols from array
                    countedCols = colParts.length;
                }
                //Add 1 to rows
                countedRows += 1;
            }
            
            //Close scanner
            reader.close();
        }
        catch(FileNotFoundException e){
            ex = true;
        }
        catch(Exception e){
            ex = true;
        }
        
        //Form result array with rows and cols
        int[] arraySize = new int[] {countedRows, countedCols};

        //Return the size of the array if valid, null otherwise
        if(!ex){
            return arraySize;
        }
        else{
            return null;
        }        
    }
    
    //Prints 2D character array
    public static void print2DCharacterArray(char[][] parArray){        
        //Check if param array has memory reserved
        if(parArray != null){
            //Go through every row
            for(int rows = 0; rows < parArray.length; rows++){
                //Go through every column in the row
                for(int columns = 0; columns < parArray[rows].length; columns++){
                    //Print the contents of the column
                    System.out.print(parArray[rows][columns]);
                }                
                //New line
                System.out.println("");
            }
        }        
    }

    //Prints 2D int array with formatted columns
    public static void print2DIntegerArray(int[][] parArray, int w){        
        //Check if param array has memory reserved
        if(parArray != null){
            //Go through every row
            for(int rows = 0; rows < parArray.length; rows++){
                //Go through every column in the row
                for(int columns = 0; columns < parArray[rows].length; columns++){
                    //Determine the amount of digits
                    String intString = String.valueOf(parArray[rows][columns]);
                    int numSize = intString.length();
                    //Amount of spaces
                    int spaceNum = w - numSize;
                    //System.out.println("Size of num " + parArray[rows][columns] + " is " + numSize);
                    //Generate string of empty spaces
                    String spaces = "";
                    for(int i = 0; i < spaceNum; i++){
                        spaces += " ";
                    }                     
                    if(columns != 0){
                        //Print space before next column if not first
                        System.out.print(" ");
                    }                    
                    //Print the spaces in front of numbers in columns
                    System.out.print(spaces);
                    //Print the contents of the column
                    System.out.print(parArray[rows][columns]);
                }                
                //New line
                System.out.println("");
            }
        }        
    }
    
    //Operation that prints the greeting logo
    public static void printLogo(){
        System.out.println("-------------------");
        System.out.println("| A S C I I A r t |");
        System.out.println("-------------------");
    }     
}