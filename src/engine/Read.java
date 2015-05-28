package engine;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * Read in the clauses from the file passed in.
 * 
 * @author Devon
 *
 */
public class Read
{
    File toRead;

    public Read(String file)
    {

        this.toRead = new File(file);
    }

    /**
     * 
     * @return an array of strings, each with no spaces. Containing terms and logical operators.
     */
    public String[] getClauses()
    {
        try(FileReader reader = new FileReader(this.toRead);BufferedReader fr = new BufferedReader(reader))
        {
            StringBuffer sb = new StringBuffer();
            String ask;
            String tell = fr.readLine();

            if(!tell.equalsIgnoreCase("TELL"))
            {
                System.out.println("The file is not in valid format.");
                System.exit(1);
            }

            sb.append(fr.readLine());

            ask = fr.readLine();

            if(!ask.equalsIgnoreCase("ASK"))
            {
                System.out.println("The file is not in valid format.");
                System.exit(1);
            }

            String clauseLine = sb.toString();
            String[] clauses = clauseLine.split(";");

            sb.delete(0, sb.length());

            sb.append(fr.readLine() + ";");
            for(String str : clauses)
            {
                sb.append(addBrackets(str));
                sb.append(";");
            }

            return sb.toString().toLowerCase().replaceAll(" ", "").split(";");

        }
        catch(FileNotFoundException ex)
        {
            System.out.println("Error: File \"" + this.toRead.getName()
                    + "\" not found.\nPlease check the path to the file.");
            System.exit(1);
        }
        catch(IOException ex)
        {
            System.out
                    .println("Error in reading \""
                            + this.toRead.getName()
                            + "\". Try closing it and programs that may be accessing it.\nIf that fails, restart your computer.");
            System.exit(1);
        }

        return null;
    }

    private static String addBrackets(String str)
    {
        String s = str.trim();
        char first = '&';
        int logic = 0;
        boolean bracketed = false;

        for(int i = 0; i < s.toCharArray().length; i++)
        {
            char c = s.toCharArray()[i];
            switch(c)
            {
                case '&':
                    logic++;
                    if(logic == 1)
                    {
                        first = c;
                    }
                    break;
                case '|':
                    logic++;
                    if(logic == 1)
                    {
                        first = c;
                    }
                    break;
                case '<':
                    logic++;
                    if(logic == 1)
                    {
                        first = c;
                    }
                    i++;
                    break;
                case '=':
                    logic++;
                    if(logic == 1)
                    {
                        first = c;
                    }
                    break;
            }
        }

        if(logic > 1)
        { // more than 1 logic symbol, need to add brackets.
            for(int j = 0; j < s.toCharArray().length; j++)
            {
                char d = s.toCharArray()[j];
                if(d == ' ')
                {
                    if(j > s.indexOf(first))
                    {
                        if(!bracketed)
                        {
                            String[] ss = s.split(Character.toString(s.charAt(j)), 2);
                            s = "(" + ss[0] + ")" + ss[1];
                            bracketed = true;
                        }
                    }
                    else if(!bracketed)
                    {
                        switch(first)
                        {
                            case '<':
                                j += 4;
                                break;
                            case '=':
                                j += 3;
                                break;
                        }

                        s = s.substring(0, j) + "(" + s.substring(j, s.length()) + ")";
                        bracketed = true;
                    }
                }
            }
        }

        return s;
    }

}
