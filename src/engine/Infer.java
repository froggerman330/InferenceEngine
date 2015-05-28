package engine;

import java.util.HashMap;
import java.util.LinkedList;

import logic.Literal;
import logic.Sentance;
import engine.method.BackwardChaining;
import engine.method.ForwardChaining;
import engine.method.SolveMethod;
import engine.method.TruthTable;

public class Infer
{
    public static void main(String... args)
    {
        LinkedList<Sentance> sentanceList = new LinkedList<Sentance>();
        HashMap<String, Literal> literals = new HashMap<String, Literal>();

        // 0 method
        // 1 file
        String[] readData = new Read(args[1]).getClauses();
        String ask = readData[0];
        String[] sentances = new String[readData.length - 1];
        for(int i = 1; i < readData.length; i++)
        {
            sentances[i - 1] = readData[i];
        }

        for(String sentance : sentances)
        {
            System.out.println(sentance);
            sentanceList.add(new Sentance(sentance));
        }

        for(Sentance s : sentanceList)
        {
            s.setTerms(literals);
        }

        SolveMethod method;

        switch(args[0])
        {
            case "TT":
                method = new TruthTable(sentanceList, literals);
                break;
            case "FC":
                method = new ForwardChaining(sentanceList, literals);
                break;
            case "BC":
                method = new BackwardChaining(sentanceList, literals);
        }
    }
}
