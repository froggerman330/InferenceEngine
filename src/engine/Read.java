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

            sb.insert(0, fr.readLine() + ";");
            return sb.toString().replaceAll(" ", "").split(";");
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
}
