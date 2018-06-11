import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.*;
import java.lang.*;

public  class Smoothing{

    private HashMap<String , Integer> mapClass1 ;
    private HashMap<String , Integer> mapClass2 ;
    private int wordNumbersClass1;
    private int wordNumbersClass2;



    private HashMap<String  , Double> probabilitiesClass1 ;
    private HashMap<String , Double>  probabilitiesClass2;

    private int vocab =1000; //taqriban

    public String counter(String inputfile) throws FileNotFoundException, IOException {

        InputStream CleanedUpFile = getClass().getResourceAsStream(inputfile);
        BufferedReader readFile = new BufferedReader(new InputStreamReader(CleanedUpFile));
        PrintWriter file = null;
        file = new PrintWriter(new BufferedWriter(new  FileWriter(  "C:/Users/ati/Desktop/pr2/src/"+inputfile+"countWordsMap")));
        String inputLine = null;
        HashMap<String, Integer> map = new HashMap<String, Integer>();
        Set<Map.Entry<String, Integer>> entrySet = null;

        try {
            while ((inputLine = readFile.readLine()) != null) {
                String[] words = inputLine.split("[ \n\t\r.,;:!?(){}]");

                for (int wordCounter = 0; wordCounter < words.length; wordCounter++) {
                    String key = words[wordCounter].toLowerCase();
                    if (key.length() > 0) {
                        if (map.get(key) == null) {
                            map.put(key, 1);
                        }
                        else {
                            int value = map.get(key).intValue();
                            value++;
                            map.put(key, value);
                        }
                    }
                    entrySet = map.entrySet();

                }

            }

            if( inputfile.equals("eshghtokenscleanedUp")){
                this.mapClass1 =map;
               // vocab = map.size();
               // System.out.printf("vocab class1 %d \n" , vocab);
                }
            else if( inputfile.equals("fekrtokenscleanedUp")){
                this.mapClass2 = map;
               // vocab = map.size();
                //System.out.printf("vocab class2 %d \n" , vocab);
            }
            int allWords = 0;
            for(Map.Entry<String, Integer> entry : entrySet){
                allWords+=  entry.getValue();
            }
            if( inputfile.equals("eshghtokenscleanedUp")){
                this.wordNumbersClass1 = allWords;}

            else if( inputfile.equals("fekrtokenscleanedUp")){
                this.wordNumbersClass2 = allWords;;
            }

          //  System.out.println("for file " + inputfile + ":");
          //  System.out.printf("number of all words is %d \n" , allWords);
            for (Map.Entry<String, Integer> entry : entrySet) {

                //   sentences[i] = sentences[i].replaceAll(" ", "");
                file.println(entry.getValue() + "\t" + entry.getKey());

                //    System.out.println(entry.getValue() + "\t" + entry.getKey());
            }

        }
        catch (IOException error) {
            System.out.println("Invalid File");
        }

        finally {
            readFile.close();
            file.close();

        }

        return inputfile+"countWordsMap";

    }

    public void probabilityFile(String inputFile) throws IOException {
        PrintWriter probFile = null;
        probFile = new PrintWriter(new BufferedWriter(new  FileWriter(  "C:/Users/ati/Desktop/pr2/src/"+inputFile+"probSmooth")));
        counter(inputFile);
        int wordNumbers;
        HashMap<String , Integer> map = new HashMap<String ,Integer>();
        HashMap<String , Double>  probs = new HashMap<String , Double>();
        if(inputFile.equals("eshghtokenscleanedUp")){
            map = mapClass1;
            wordNumbers=wordNumbersClass1;

        }

        else{
            map = mapClass2;
            wordNumbers=wordNumbersClass2;
        }
        //  HashMap<String ,Integer> numbers = getMap();

        Set<Map.Entry<String, Double>> entSet = null;
        String[] keys = new String[map.size()];
        int[] values = new int[map.size()];
        int j = 0;
        for(Map.Entry<String,Integer> en : map.entrySet()){
            //  System.out.println( en.getKey());
            keys[j] = en.getKey();
            values[j]=en.getValue();
            j++;
        }
        double x = 0;
        for (int i=0 ; i< map.size() ; i ++){
            x = ((Math.log10(values[i])+1)  -  Math.log10(wordNumbers + vocab+1)) ;
            probs.put(keys[i], x);
            // System.out.println(x);
        }
        entSet =  probs.entrySet();

        for (Map.Entry<String, Double> entry : entSet) {

            probFile.println(entry.getValue() + "\t" + entry.getKey());

            //    System.out.println(entry.getValue() + "\t" + entry.getKey());
        }
        probFile.close();
        if(inputFile.equals("eshghtokenscleanedUp")){
            probabilitiesClass1 =probs;
        }
        else{
            probabilitiesClass2 =probs;
        }

    }

    public void TestClass1Smooth(){
        InputStream class1 = getClass().getResourceAsStream("testOfEshghcleanedUp");
        BufferedReader readFile = new BufferedReader(new InputStreamReader(class1));
        String inputLine = null;
        Set<Map.Entry<String, Double>> es1 = null;
        es1 =  probabilitiesClass1.entrySet();
        int wrongResult=0;
        int rightResult=0;
        Set<Map.Entry<String, Double>> es2 = null;
        es2 =  probabilitiesClass2.entrySet();

        try {
            while ((inputLine = readFile.readLine()) != null) {
            //    int counterx  =0;
             //   int countery=0;
                double probClass1=0;
                double probClass2=0;
                String[] words = inputLine.split(" ");
                for ( int i =0 ; i<words.length ; i++) {
                    loop1:
                    if(probabilitiesClass1.containsKey(words[i])) {
                        for (Map.Entry<String, Double> entry : es1) {
                           // counterx++;
                            if (entry.getKey().equals(words[i])) {
                                probClass1 = probClass1 + entry.getValue();
                               // break loop1;
                            }
                        }
                    }
                    else if(probabilitiesClass1.containsKey(words[i]) == false) {
                        probClass1 = (Math.log(1) - Math.log(wordNumbersClass1 + vocab)) + probClass1; //smoothing
                    }
                    loop2:
                    if(probabilitiesClass2.containsKey(words[i])) {
                        for (Map.Entry<String, Double> entry : es2) {
                            // countery++;
                            if (entry.getKey().equals(words[i])) {
                                probClass2 = entry.getValue() + probClass2;
                                //break loop2;
                            }

                        }
                    }
                    else if(probabilitiesClass2.containsKey(words[i]) == false) {
                        probClass2 = (Math.log(1) - Math.log(wordNumbersClass2 + vocab)) + probClass2; //smoothing
                    }
                }

                if(Math.abs(probClass1 )< Math.abs(probClass2)){
                    //  System.out.println("belogs to ClassOne");
                    rightResult++;
                }else{
                    //     System.out.println("belogs to ClassTwo");
                    wrongResult++;
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Test of class1 smoothing");
        System.out.printf("right results : %d  \n" , rightResult);
        System.out.printf("wrong results : %d  \n" , wrongResult);
        int falsePos = 40;
        float pre =  (float)rightResult/(float)(rightResult+falsePos);
        float recall =  (float)rightResult/(float)(rightResult+wrongResult);
        System.out.printf("presicion of class 1 in Smoothing mode is: %.2f \n" ,pre );
        System.out.printf("recall of class 1 in Smoothing mode is: %.2f \n" , recall);

    }

    public void TestClass2Smooth(){
        InputStream class1 = getClass().getResourceAsStream("testOfFekrcleanedUp");
        BufferedReader readFile = new BufferedReader(new InputStreamReader(class1));
        String inputLine = null;
        Set<Map.Entry<String, Double>> es1 = null;
        es1 =  probabilitiesClass1.entrySet();
        int wrongResult=0;
        int rightResult=0;
        Set<Map.Entry<String, Double>> es2 = null;
        es2 =  probabilitiesClass2.entrySet();

        try {
            while ((inputLine = readFile.readLine()) != null) {
              //  int counterx  =0;
              //  int countery=0;
                double probClass1=0;
                double probClass2=0;
                String[] words = inputLine.split(" ");
                for ( int i =0 ; i<words.length ; i++) {
                    outerloop:
                    if(probabilitiesClass1.containsKey(words[i])) {
                        for (Map.Entry<String, Double> entry : es1) {
                            //        counterx ++;
                            if (entry.getKey().equals(words[i])) {
                                probClass1 = entry.getValue() + probClass1;
                               // break outerloop;
                            }

                        }
                    }
                    else if(probabilitiesClass1.containsKey(words[i])== false){
                        probClass1 = (Math.log(1) - Math.log(wordNumbersClass1 + vocab)) + probClass1; //smoothing
                   }
                    outlabel:
                    if(probabilitiesClass2.containsKey(words[i])) {
                        for (Map.Entry<String, Double> entry : es2) {
                            // countery++;
                            if (entry.getKey().equals(words[i])) {
                                probClass2 = entry.getValue() + probClass2;
                             //   break outlabel;
                            }
                        }
                    }
                  else if(probabilitiesClass2.containsKey(words[i])==false){
                        probClass2= (Math.log(1) - Math.log(wordNumbersClass2 + vocab)) + probClass2; //smoothing
                   }
                }

                if(Math.abs(probClass1 )< Math.abs(probClass2)){
                    // System.out.println("belogs to ClassOne");
                    wrongResult++;
                }else{
                    // System.out.println("belogs to ClassTwo");
                    rightResult++;
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Test of class2 smoothing");
        System.out.printf("right results : %d  \n" , rightResult);
        System.out.printf("wrong results : %d  \n" , wrongResult);
        int falsePos = 41;
        float pre =  (float)rightResult/(float)(rightResult+falsePos);
        float recall =  (float)rightResult/(float)(rightResult+wrongResult);
        System.out.printf("presicion of class 2 in noneSmoothing mode is: %.2f \n" ,pre );
        System.out.printf("recall of class 2 in noneSmoothing mode is: %.2f \n" , recall);

    }






    public static void main(String[] args) throws IOException {
        FileSplitter fileSplitter = new FileSplitter();
        Cleaning cleaningUp = new Cleaning();

        Smoothing sm = new Smoothing();
        // WordCounter w2 = new WordCounter();
        fileSplitter.tokenizer("eshgh");
        cleaningUp.cleanUp("eshghtokens");
        sm.probabilityFile("eshghtokenscleanedUp");

         fileSplitter.tokenizer("fekr");
         cleaningUp.cleanUp("fekrtokens");
         sm.probabilityFile("fekrtokenscleanedUp");
     //   fileSplitter.tokenizer("fekr");
     //   cleaningUp.cleanUp("fekrtokens");
     //   sm.probabilityFile("fekrtokenscleanedUp");

        sm.TestClass1Smooth();
        sm.TestClass2Smooth();

    }


}