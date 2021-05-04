import java.io.File;
import groovy.json.JsonOutput;
import java.io.FileWriter;
import java.text.DecimalFormat;

def writeJson(data, fileNameOutput){ //Function that creates a json file and saves the data
    try{

        println("\n\nSaving file ${fileNameOutput}")
        writeFile = new FileWriter(fileNameOutput);
        def json_str = JsonOutput.toJson(data)         
        def json_beauty = JsonOutput.prettyPrint(json_str)
        
        writeFile.write(json_beauty);   //Write the data inside the created json file
            
        writeFile.close();
        println("File saved")
    }catch(IOException e){
        println("Error saving json file")
		e.printStackTrace();
            
		}
}

def convertSizeToGB(size){ //Function that converts the size to GB standard

    
    DecimalFormat df = new DecimalFormat("#0.00"); //Object that places the decimal format with two spaces after a comma.
    
    //10 ^ (- 9) or 1/1073741824 could be used as a multiplier. Preference for the second is because computers use binary.
    String newSize = ((double)size/1073741824) < 1 ? df.format((double)size/1073741824).toString() : size/1073741824
    return newSize
    
}


def getDadosFromTxt(fileContents, data){ // Function that captures txt data and organize by categories

    
    def firstLine = true //Variable used to ignore the first line of the txt, the header.
    
    try{
        fileContents.eachLine { Line-> 
        
            if (Line && firstLine == false){//If in the Line below used to eliminate the empty lines and the header line.
                
                
                //Using find with Regex to capture column data for non-empty rows other than the header 
                String movieName = Line.find(/([\w\s:.-]+)\s/).replaceAll(/\d\d-\d\d-\d\d\d\d/, '')?.trim()
                Long size = Line.split(" ")[-1].toLong()  
                String date =  Line.find(/\d\d-\d\d-\d\d\d\d/)?.replaceAll('-', '/')
                String path = Line.find(/(\d\d-\d\d-\d\d\d\d)+\s+\S+/)?.replaceAll(/\d\d-\d\d-\d\d\d\d/, '')?.trim()
                
                println("Adding data from the movie ${movieName} to json ")
                //Map used to insert data for each row
                data << [
                name: movieName,
                path: path ,
                date: date ,
                size: "${convertSizeToGB(size)} GB"
                ]       

            } 
            firstLine = false  
    }
    return data
    }catch(Exception e){
            println("Error collecting data from txt columns and inserting 'data' in the map!")
            return data


            
		}
    
}


def main() { // Main function declaration
    try{
        def data = []

        //Listing the current directory 
        String currentDir = new File("").getAbsolutePath()

        
        // If you do not receive a parameter at the time of execution, filename receives " planilha.txt"
        args = this.args
        println("\nChecking filename argument: ${args ? 'Argument sent': 'Argument not sent. Default value planilha.txt will be used '}")
        filename = args ? args[0] : 'planilha.txt'
        def file_input = new File("${currentDir}/${filename}")

    
        if (file_input.exists()){   //Check if the files really exists
            println("File ${filename} found ")
            
            def fileContents = file_input?.getText('UTF-8')//Get the contents of the input file
            println("Getting file contents: ${filename}\n\n")

            data_to_json = getDadosFromTxt(fileContents, data) //Calls the function that collects data from the txt and places it on a map
            
            
            
            outputFileName =  args.size() > 1 ? args[1] : "PlanilhaOut.json" //Checks whether argument has been passed out


            //Calls function to write data verifying if the file outputfilename already has the extension .json
            if (data)
            writeJson(data_to_json, outputFileName.toString().endsWith(".json") ? outputFileName : "${outputFileName}.json")
            else
            println("Error when capturing txt data. It can contain elements with different structure.")

        }else{
            println("File not found")
    }

           
	}catch(IOException e){
        println("Flow error")
        e.printStackTrace();
            
	}
}

//Calls the main function
main()
