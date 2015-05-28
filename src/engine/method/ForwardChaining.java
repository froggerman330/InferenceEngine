package engine.method;

import java.util.HashMap;
import java.util.LinkedList;

import logic.Literal;
import logic.Sentance;

public class ForwardChaining implements SolveMethod
{
    private HashMap<String, Literal> literals;
    private LinkedList<Sentance> sentances;
    private String ask;

    public ForwardChaining(LinkedList<Sentance> clauseList, HashMap<String, Literal> literals, String ask)
    {
        this.literals = literals;
        this.sentances = clauseList;
        this.ask = ask;
    }

    @Override
    public void solve()
    {
        // TODO Auto-generated method stub

    }

}
