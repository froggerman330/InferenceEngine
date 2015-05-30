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

    /**
     * The constructor for the read object that sets the file to read from.
     * 
     * @param file
     *            the file path to be read from.
     */
    public Read(String file)
    {

        this.toRead = new File(file);
    }

    /**
     * Reads the file and separates all the different pieces while checking a couple of key things about it.This will
     * also insert brackets where necessary.
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

    /**
     * Takes a string of a clause and adds a set of brackets if necessary
     * 
     * @param str
     * @return
     */
    private static String addBrackets(String str)
    {
        String s = str.trim();
        int neutralPos = 0, bracketAt = -1;
        boolean isLogic = false, needBracket = false, goBack = true;

        goBack = true;
        needBracket = false;
        bracketAt = -1;
        neutralPos = 0;
        for(int i = 0; i < s.toCharArray().length; i++)
        {
            char d = s.toCharArray()[i];
            switch(d)
            {
                case '&':
                    isLogic = true;
                    break;
                case '|':
                    isLogic = true;
                    break;
                case '<':
                    isLogic = true;
                    break;
                case '=':
                    isLogic = true;
                    break;
                case '(':
                    neutralPos++;
                    break;
                case ')':
                    neutralPos--;
                    break;
                case ' ':
                    if(neutralPos != 0)
                    {
                        System.out.println("There is an error with the input. There must be no spaces inside brackets");
                        System.exit(1);
                    }

                    if(goBack)
                    {
                        if(isLogic)
                        {
                            char e = s.toCharArray()[i - 1];
                            switch(e)
                            {
                                case ')':
                                    // no bracket needed
                                    goBack = false;
                                    i += 2;
                                    break;
                                default:
                                    // add bracket, switch sides
                                    s = "(" + s.substring(0, i) + ")" + s.substring(i + 1);
                                    goBack = false;
                                    i += 3;
                                    break;
                            }
                        }
                    }
                    else
                    {
                        char f = s.toCharArray()[i + 1];
                        switch(f)
                        {
                            case '(':
                                // no bracket needed
                                break;
                            default:
                                // may need to bracket
                                needBracket = true;
                                bracketAt = i;
                                break;
                        }
                    }

                    isLogic = false;
                    break;
                default:
                    if(i == (s.toCharArray().length - 1) && bracketAt > 0)
                    {
                        if(isLogic && needBracket)
                        {
                            s = s.substring(0, bracketAt) + "(" + s.substring(bracketAt + 1) + ")";
                            neutralPos++;
                            i++;
                        }
                    }
            }
        }

        return s;
    }

}
