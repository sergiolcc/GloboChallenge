import java.io.File;
import groovy.json.JsonOutput;
import java.io.FileWriter;
import java.text.DecimalFormat;



def writeJson(data, fileNameOutput){ //Função que cria um arquivo json e salva os dados
    try{

        println("\n\nSalvando arquivo ${fileNameOutput} em json")
        writeFile = new FileWriter(fileNameOutput);
        def json_str = JsonOutput.toJson(data)         
        def json_beauty = JsonOutput.prettyPrint(json_str)
        
        writeFile.write(json_beauty);   //Escreve o dado dentro do arquivo json criado    
            
        writeFile.close();
        println("Arquivo salvo")
    }catch(IOException e){
        println("Erro ao salvar arquivo json")
		e.printStackTrace();
            
		}
}

def convertSizeToGB(size){ //Função que converte o tamanho para padrão GB
    
    DecimalFormat df = new DecimalFormat("#0.00"); //Objeto que coloca o formato decimal com duas casas após virgula.
    //Poderia ser utilizado 10^(-9) ou 1/1073741824 como multiplicador. Preferência pela segunda é devido os computadores usarem binário. 
    String newSize = ((double)size/1073741824) < 1 ? df.format((double)size/1073741824).toString() : size/1073741824
    return newSize
    
}


def getDadosFromTxt(fileContents, data){ // Função que captura os dados do txt e organiza por categorias
    
    def firstLine = true //variável utilizada para ignorar a primeira linha do cabeçalho
    
    try{
        fileContents.eachLine { Line-> 
        
            if (Line && firstLine == false){//If na Linha abaixo usado para eliminar as linhas vazias e a linha do cabeçalho  
                
                
                //Usando o find com Regex para capturar os dados da coluna das linhas não vazias e que não seja o cabeçalho
                String movieName = Line.find(/([\w\s:.-]+)\s/).replaceAll(/\d\d-\d\d-\d\d\d\d/, '')?.trim()
                Long size = Line.split(" ")[-1].toLong()  
                String date =  Line.find(/\d\d-\d\d-\d\d\d\d/)?.replaceAll('-', '/')
                String path = Line.find(/(\d\d-\d\d-\d\d\d\d)+\s+\S+/)?.replaceAll(/\d\d-\d\d-\d\d\d\d/, '')?.trim()
                
                println("Adicionando dados do filme ${movieName} ao json")
                //Map usado para inserir os dados de cada linha
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
            println("Erro coletar dados das colunas do txt e inserir no map 'data' !!!!!")
            return data


            
		}
    
}


def main() { // Declaração de função principal
    try{
    def data = []

    //Listando o diretório atual
    String currentDir = new File("").getAbsolutePath()

    
    // Caso não receba parametro na hora de executar, filename recebe "planilha.txt"
    args = this.args
    println("\nChecagem de argumento filename: ${args ? 'Argumento passado' : 'Argumento não passado. Será utilizado valor default planilha.txt'}")
    filename = args ? args[0] : 'planilha.txt'
    def file_input = new File("${currentDir}/${filename}")

 
    if (file_input.exists()){   //Checa se o arquivo  realmente existem
        println("Arquivo  ${filename} encontrado ")
        
        def fileContents = file_input?.getText('UTF-8')//Pegar o conteúdo do arquivo de entrada
        println("Pegando conteúdo do arquivo: ${filename}\n\n")

        data_to_json = getDadosFromTxt(fileContents, data) //Chama função que coleta dados do txt e coloca em um map
        
        
         
        outputFileName =  args.size() > 1 ? args[1] : "PlanilhaOut.json" //Verifica se argumento foi passado para saída


        //Chama função de escrever dados verificando se o arquivo outputfilename já tem presente a extensão .json
        if (data)
        writeJson(data_to_json, outputFileName.toString().endsWith(".json") ? outputFileName : "${outputFileName}.json")
        else
        println("Problemas ao capturar dados do txt. Ela pode conter elementos com estrutura diferente.")

    }else{
        println("Arquivo não encontrado")
    }

           
	}catch(IOException e){
            println("Erro no fluxo")
			e.printStackTrace();
            
		}
}

//Chama a função principal
main()
