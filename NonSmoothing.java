import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.*;
import java.lang.*;

public  class NonSmoothing{
  //    private sortMapKey sortmap = new sortMapKey();
    //private FileSplitter fileSplitter = new FileSplitter();
    //private Cleaning cleaningUp = new Cleaning();
    private HashMap<String , Integer> mapClass1 ;
    private HashMap<String , Integer> mapClass2 ;
    private int wordNumbersClass1;
    private int wordNumbersClass2;

   /* public HashMap<String , Integer> getMap (){
        return  map;
    } */

    private HashMap<String  , Double> probabilitiesClass1 ;
    private HashMap<String , Double>  probabilitiesClass2;

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
            this.mapClass1 =map;}
            else if( inputfile.equals("fekrtokenscleanedUp")){
                this.mapClass2 = map;
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

           // System.out.println("for file " + inputfile + ":");
           // System.out.printf("number of all words is %d \n" , allWords);
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
        probFile = new PrintWriter(new BufferedWriter(new  FileWriter(  "C:/Users/ati/Desktop/pr2/src/"+inputFile+"probMap")));
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

            x = ((Math.log10(values[i]))  -  Math.log10(wordNumbers)) ;
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

    public void TestClass1NoneSmooth(){
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
                double probClass1=0;
                double probClass2=0;
                String[] words = inputLine.split(" ");
                for ( int i =0 ; i<words.length ; i++) {
                    if(probabilitiesClass1.containsKey(words[i])){
                        for (Map.Entry<String, Double> entry : es1) {
                            if (entry.getKey().equals(words[i])) {

                                probClass1 = entry.getValue()  + probClass1;

                            }
                        }
                    }
                    else if(!probabilitiesClass1.containsKey(words[i])){
                        probClass1 += 30;
                    }
                 //   System.out.println(probClass1);
                 /*   for (Map.Entry<String, Double> entry : es1) {
                       if (entry.getKey().equals(words[i])) {

                            probClass1 = entry.getValue()  + probClass1;

                           break;
                        }
                    } */
                  //  loop2:
                    if(probabilitiesClass2.containsKey(words[i])) {
                        for (Map.Entry<String, Double> entry : es2) {
                            if (entry.getKey().equals(words[i])) {
                                probClass2 = entry.getValue() + probClass2;
                            }
                        }
                    }
                    else if(!probabilitiesClass2.containsKey(words[i])){
                        probClass2 += 30;
                    }

                }
                // System.out.println(probClass2);
            if(probClass1 < probClass2){
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
        System.out.println("Test of class1 nonsmoothing");
System.out.printf("right results : %d  \n" , rightResult);
        System.out.printf("wrong results : %d  \n" , wrongResult);
        int falsePos = 177;
        float pre =  (float)rightResult/(float)(rightResult+falsePos);
        float recall =  (float)rightResult/(float)(rightResult+wrongResult);
        System.out.printf("presicion of class 1 in noneSmoothing mode is: %.2f \n" ,pre );
        System.out.printf("recall of class 1 in noneSmoothing mode is: %.2f \n" , recall);

    }

    public void TestClass2NonSmooth(){
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
                double probClass1=0;
                double probClass2=0;
                String[] words = inputLine.split(" ");
                for ( int i =0 ; i<words.length ; i++) {
                   // outerloop:
                    if(probabilitiesClass1.containsKey(words[i])) {
                        for (Map.Entry<String, Double> entry : es1) {

                            if (entry.getKey().equals(words[i])) {
                                probClass1 = entry.getValue() + probClass1;
                                //  break outerloop;
                            }

                        }
                    }
                  //  outlabel:
                    if(probabilitiesClass2.containsKey(words[i])) {
                        for (Map.Entry<String, Double> entry : es2) {
                            if (entry.getKey().equals(words[i])) {
                                probClass2 = entry.getValue() + probClass2;
                                //  break outlabel;
                            }

                        }
                    }
                }

                    if(Math.abs(probClass1 )< Math.abs(probClass2)){
                       // System.out.println("belogs to ClassOne");
                        wrongResult++;
                    }else{
                     //   System.out.println("belogs to ClassTwo");
                        rightResult++;
                    }


            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Test of class2 nonsmoothing");
        System.out.printf("right results : %d  \n" , rightResult);
        System.out.printf("wrong results : %d  \n" , wrongResult);
        int falsepos =149;
        float pre =  ((float) rightResult/(float) (rightResult+falsepos));
        float recall = ((float) rightResult/(float)(rightResult+wrongResult));
        System.out.printf("presicion of class 2 in noneSmoothing mode is: %.2f \n" ,pre);
        System.out.printf("recall of class 2 in noneSmoothing mode is: %.2f \n" ,recall);


    }






    public static void main(String[] args) throws IOException {
        SentenceCleaner w = new SentenceCleaner();
        String s =  w.cleanTests("eshghsentences");
        String s2 =  w.cleanTests("fekrsentences");
        FileSplitter fileSplitter = new FileSplitter();
        Cleaning cleaningUp = new Cleaning();

        NonSmoothing w1 = new NonSmoothing();
       // WordCounter w2 = new WordCounter();
        fileSplitter.tokenizer("eshgh");
        cleaningUp.cleanUp("eshghtokens");
        w1.probabilityFile("eshghtokenscleanedUp");


        fileSplitter.tokenizer("fekr");
        cleaningUp.cleanUp("fekrtokens");
        w1.probabilityFile("fekrtokenscleanedUp");

        w1.TestClass1NoneSmooth();
       w1.TestClass2NonSmooth();


       //now test smoothing
        Smoothing sm = new Smoothing();
        sm.probabilityFile("eshghtokenscleanedUp");
        sm.probabilityFile("fekrtokenscleanedUp");

        sm.TestClass1Smooth();
        sm.TestClass2Smooth();


    }


}