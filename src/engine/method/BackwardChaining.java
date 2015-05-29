package engine.method;

import java.util.HashMap;
import java.util.LinkedList;

import logic.Literal;
import logic.Sentence;

public class BackwardChaining implements SolveMethod
{
    private HashMap<String, Literal> literals;
    private LinkedList<Sentence> sentances;
    private String ask;

    public BackwardChaining(LinkedList<Sentence> clauseList, HashMap<String, Literal> literals, String ask)
    {
        this.literals = literals;
        this.sentances = clauseList;
        this.ask = ask;
    }

    @Override
    public void solve()
    {

    }

}
