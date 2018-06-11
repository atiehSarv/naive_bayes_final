import java.io.*;

public class SentenceCleaner {
    public boolean hasChar(String s){
        int counter = 0;
        char[] chars = {'ا','ب','پ','ت','ث', 'ج', 'چ', 'ح', 'خ','د' ,'ذ','ر','ز','س','ش','ص',
                'ض', 'ک', 'ل', 'م', 'ن', 'و', 'ه', 'ی', 'ط', 'ظ', 'ع', 'غ', 'ف', };
        for (int i =0 ; i < s.length() ; i++){
            for(int j=0 ; j< chars.length; j++){
                if (s.charAt(i)==chars[j]){
                    counter++;
                }
            }
        }
        if (counter == 0)
            return false;
        else
            return true;
    }

    public String cleanTests(String fileName) throws IOException {
        PrintWriter file = null;
        char[] chars = {'@','»','«','\f','.', ',', '?', '!', ':','؛' ,'/','Ş','ğ','\'','ç','ü',
                '"', ')', '(', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '؟','#',
                '،', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j','*',']',
                'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u','|','+'
                , 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K',
                'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z' ,'-' ,'-'
                ,'۱','۲','۳','۴','۵','۶','۷','۸','۹','۰'};


        InputStream inputFile = getClass().getResourceAsStream(fileName);
        BufferedReader readFile = new BufferedReader(new InputStreamReader(inputFile));
        String t = "";
        String file_line;
        file = new PrintWriter(new BufferedWriter(new FileWriter("C:/Users/ati/Desktop/pr2/src/"+fileName+"cleaned")));

        try {
            while ((file_line = readFile.readLine()) != null) {

                t = file_line;
                String curr = " ";
                String[] temp = t.split(" ");
                if (temp.length > 2){
                    loop2:
                    for (int m = 0; m < temp.length; m++) {
                        loop1:
                        if (temp[m].length() == 1) {
                            temp[m] = "";
                            m++;
                            if (m < temp.length)
                                break loop1;
                            else
                                break loop2;

                        }
                        temp[m] = temp[m].replaceAll(" ", "");
                        temp[m] = temp[m].replace("ک", "ک");
                        temp[m] = temp[m].replaceAll("ي", "ی");
                        // temp = temp.replaceAll("\u202C", "");
                        //temp = temp.replaceAll("ﮐﻨﺪ\u202A ", "");
                        temp[m] = temp[m].replaceAll("ؤ", "و");
                        temp[m] = temp[m].replaceAll("ﺮ", "ر");


                        for (int j = 0; j < temp[m].length(); j++) {
                            char s = temp[m].charAt(j);
                            for (int i = 0; i < chars.length; i++) {
                                if (chars[i] == s) {

                                    temp[m] = temp[m].replace(s, ' ');
//System.out.println(temp);
                                }

                            }

                        }

                        String empty = "";
                        for (int i = 0; i < temp[m].length(); i++) {
                            if (temp[m].charAt(i) != ' ') {
                                empty += temp[m].charAt(i);
                            }

                        }

                        temp[m] = empty;
                        if (!temp[m].isEmpty() && !temp[m].equals(
                                " ")) {
                            temp[m].replaceAll(" ", "");


                        }
                        curr += temp[m] + " ";
                    }
                    if (hasChar(curr))
                        file.println(curr);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();

        }

        file.close();
        return fileName+"testCleared";

    }
//test of cleaning class is commented below

    public static void main(String[] args) throws IOException {
        SentenceCleaner w = new SentenceCleaner();
        String s =  w.cleanTests("eshghsentences");
        String s2 =  w.cleanTests("fekrsentences");
        System.out.print(s);
    }

}


