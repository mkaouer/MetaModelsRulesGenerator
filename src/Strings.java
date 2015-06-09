/**
 *
 * @author MWM
 */

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Strings 
{
    Strings()
    {
        
    }
    
    public static ArrayList<String> readLines(String filename) throws IOException {
        FileReader fileReader = new FileReader(filename);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        ArrayList<String> lines = new ArrayList<String>();
        String line = null;
        while ((line = bufferedReader.readLine()) != null) 
        {
            line = line.trim();
            lines.add(line);
            //lines.add(": ");
        }
        bufferedReader.close();
        return lines;
    }
    
    public static int count_occurrences(String str, char c)
    {
        int count = 0;
        for (int i=0; i < str.length(); i++)
        {
            if (str.charAt(i) == c)
            {
                count++;
            }
        }
        return count;
    }
    
    public static String extract_prefix(String s)
    {
        int start = 0;
        int end = (s.indexOf('(')) ;
        return s.substring(start, end);   // Str
    }

    
}
