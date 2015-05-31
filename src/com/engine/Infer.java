package com.engine;

import java.util.HashMap;
import java.util.LinkedList;

import com.engine.method.BackwardChaining;
import com.engine.method.ForwardChaining;
import com.engine.method.SolveMethod;
import com.engine.method.TruthTable;
import com.logic.Literal;
import com.logic.operator.Sentence;

/**
 * The main class of the Inference engine takes some sentences and uses a method to solve for the asked literal.
 * 
 * @author Devon
 *
 */
public class Infer
{
    /**
     * The main method, takes the solving method and the file name.
     * 
     * @param args
     *            <i>0)</i> the solving method (TT,FC,BC). <br>
     *            <i>1)</i> the path to file.
     */
    public static void main(String... args)
    {
        LinkedList<Sentence> sentenceList = new LinkedList<Sentence>();
        HashMap<String, Literal> literals = new HashMap<String, Literal>();

        // 0 method
        // 1 file
        String[] readData = new Read(args[1]).getSentences();
        String ask = readData[0].toLowerCase();
        String[] sentences = new String[readData.length - 1];

        for(int i = 1; i < readData.length; i++)
        {// skips the first item which is the asked literal
            sentences[i - 1] = readData[i];
        }

        for(String sentence : sentences)
        {// creates all the sentences and logic
            sentenceList.add(new Sentence(sentence));
        }

        for(Sentence s : sentenceList)
        {// updates all the literals used in all the logic to be the same objects.
            literals.putAll(s.setTerms(literals));
        }

        SolveMethod method = null;

        switch(args[0].toUpperCase())
        {
            case "TT":
                method = new TruthTable(sentenceList, literals, ask);
                break;
            case "FC":
                method = new ForwardChaining(sentenceList, literals, ask);
                break;
            case "BC":
                method = new BackwardChaining(sentenceList, literals, ask);
        }

        method.solve();
    }
}
