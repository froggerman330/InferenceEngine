package com.engine;

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
    private File toRead;

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
    public String[] getSentences()
    {
        try(FileReader reader = new FileReader(this.toRead);BufferedReader fr = new BufferedReader(reader))
        {
            StringBuilder sb = new StringBuilder();
            String ask, query;
            String tell, knowledgeBase;

            tell = fr.readLine();

            if(!tell.equalsIgnoreCase("TELL"))
            {
                System.out.println("The file is not in valid format.");
                System.exit(1);
            }

            knowledgeBase = fr.readLine();
            String[] sentences = knowledgeBase.split(";");

            ask = fr.readLine();

            if(!ask.equalsIgnoreCase("ASK"))
            {
                System.out.println("The file is not in valid format.");
                System.exit(1);
            }

            query = fr.readLine();

            if(!knowledgeBase.contains(query))
            {
                System.out.println("The query: " + query
                        + " is not included in the knowledgeBase. Therefore it cannot be entailed.");
                System.exit(1);
            }

            sb.append(query + ";");
            for(String sentence : sentences)
            {
                sb.append(addBrackets(sentence));
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
     * @param sentence
     * @return
     */
    private static String addBrackets(String sentence)
    {
        String sen = sentence.trim();
        int neutralPos = 0, bracketAt = -1, logic = 0;
        boolean isLogic = false, needBracket = false, goBack = true;

        for(int i = 0; i < sen.toCharArray().length; i++)
        {// count the amount of logic in the string.
            switch(sen.toCharArray()[i])
            {
                case '=':
                    logic++;
                    break;
                case '&':
                    logic++;
                    break;
                case '|':
                    logic++;
                case '<':
                    logic++;
                    i++; // skip = to not give false amounts of logic.
                    break;
            }
        }

        if(logic < 2)
        {
            return sen;
        }

        for(int i = 0; i < sen.toCharArray().length; i++)
        {
            switch(sen.toCharArray()[i])
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
                    {// space in between brackets, not allowed.
                        System.out.println("There is an error with the input. There must be no spaces inside brackets");
                        System.exit(1);
                    }

                    if(goBack)
                    {// add brackets around the first part of the string.
                        if(isLogic)
                        {
                            switch(sen.toCharArray()[i - 1])
                            {// check the character before the space
                                case ')':
                                    // no bracket needed as there is already one there.
                                    i += 2;
                                    break;
                                case '&':
                                case '|':
                                case '>':
                                    // end of logic piece, no bracket needed before this. There is a space where
                                    // there shouldn't be. There may be a bracket needed after this though.
                                    needBracket = true;
                                    bracketAt = i;
                                    break;
                                default:
                                    // add bracket
                                    sen = "(" + sen.substring(0, i) + ")" + sen.substring(i + 1);
                                    i += 3;
                                    break;
                            }
                        }
                        // after detecting the first space the brackets need to be added going the other direction
                        // (whether they were added or not).
                        goBack = false;
                    }
                    else
                    {// add brackets around the second part of the string.
                        switch(sen.toCharArray()[i + 1])
                        {
                            case '(':
                                // no bracket needed.
                                break;
                            default:
                                // may need to bracket.
                                needBracket = true;
                                bracketAt = i;
                                break;
                        }
                    }

                    isLogic = false;
                    break;
                default:
                    if(i == (sen.toCharArray().length - 1) && isLogic && needBracket)
                    {// if it's the end of the string, there has been logic since the last space and we need a bracket
                     // put a bracket around the end of the sentence.
                        sen = sen.substring(0, bracketAt) + "(" + sen.substring(bracketAt + 1) + ")";
                        neutralPos++;
                        i++;
                    }
            }
        }

        return sen;
    }
}
