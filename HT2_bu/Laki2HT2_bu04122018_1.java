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

        //While loop that presents CLI to user and executes commands
        //until it gets a quit command
        while(!quit){
            //Only proceed if CLP is ok
            if(args.length == 1){
                //Check if arg and save to variable            
                String fileName = args[0];
                //Load to int array
                System.out.println("Here we load the txt file to int array");            
                int[][] iArray = loadToIntArray(fileName);
                
                //Ask for command
                System.out.println("printa/printi/info/filter [n]/reset/quit?");

                //Read command from user
                String command = In.readString();

                //Print as ASCII
                if(command.equals("printa")){                
                    System.out.println("Here we would print the ASCII array");
                }
                //Print as integers
                else if(command.equals("printi")){                
                    print2DIntegerArray(iArray, 2);;                
                }
                //Print file info
                else if(command.equals("info")){                
                    System.out.println("Here we would provide info");                
                }
                //Use filter 
                //>>> WILDCARD DOESN'T WORK YET!!! <<<<
                else if(command.equals("filter")){                
                    System.out.println("Here we would use the filter");                
                }
                else if(command.equals("reset")){                
                    System.out.println("Here we would reset and load the file again");            
                }
                //Quit program
                else if(command.equals("quit")){
                    System.out.println(BYE);
                    quit = true;
                }
            }
            else{
                System.out.println(CLPERR);
                System.out.println(BYE);
                quit = true;
            }
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
    
    //Operation that prints 2D char array
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
    // //Operation that prints 2D int array
    // public static void print2DIntArray(int[][] parArray){        
    //     //Check if param array has memory reserved
    //     if(parArray != null){
    //         //Go through every row
    //         for(int rows = 0; rows < parArray.length; rows++){
    //             //Go through every column in the row
    //             for(int columns = 0; columns < parArray[rows].length; columns++){
    //                 //Print the contents of the column
    //                 System.out.print(parArray[rows][columns]);
    //             }                
    //             //New line
    //             System.out.println("");
    //         }
    //     }        
    // }

    //Operation that prints 2D int array with formatted columns
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