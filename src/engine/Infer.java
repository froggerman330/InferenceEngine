package engine;

import java.util.HashMap;
import java.util.LinkedList;

import logic.Literal;
import logic.Sentence;
import engine.method.BackwardChaining;
import engine.method.ForwardChaining;
import engine.method.SolveMethod;
import engine.method.TruthTable;

/**
 * The main class of the Inference engine takes some sentences and figures out logical conclusions from them.
 * 
 * @author Devon
 *
 */
public class Infer
{
    public static void main(String... args)
    {
        LinkedList<Sentence> sentanceList = new LinkedList<Sentence>();
        HashMap<String, Literal> literals = new HashMap<String, Literal>();

        // 0 method
        // 1 file
        String[] readData = new Read(args[1]).getClauses();
        String ask = readData[0].toLowerCase();
        String[] sentances = new String[readData.length - 1];
        for(int i = 1; i < readData.length; i++)
        {
            sentances[i - 1] = readData[i];
        }

        for(String sentance : sentances)
        {
            sentanceList.add(new Sentence(sentance));
        }

        for(Sentence s : sentanceList)
        {
            literals.putAll(s.setTerms(literals));
        }

        SolveMethod method = null;

        switch(args[0].toUpperCase())
        {
            case "TT":
                method = new TruthTable(sentanceList, literals, ask);
                break;
            case "FC":
                method = new ForwardChaining(sentanceList, literals, ask);
                break;
            case "BC":
                method = new BackwardChaining(sentanceList, literals, ask);
        }

        method.solve();
    }
}
