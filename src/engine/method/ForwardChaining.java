package engine.method;

import java.util.HashMap;
import java.util.LinkedList;

import logic.Literal;
import logic.Sentence;
import exception.NotSolvableException;

public class ForwardChaining implements SolveMethod
{
    private HashMap<String, Literal> literals;
    private LinkedList<Sentence> sentances;
    private String ask;

    public ForwardChaining(LinkedList<Sentence> clauseList, HashMap<String, Literal> literals, String ask)
    {
        this.literals = literals;
        this.sentances = clauseList;
        this.ask = ask;
    }

    @Override
    public void solve()
    {
        LinkedList<Literal> agenda = new LinkedList<Literal>();
        HashMap<Sentence, Integer> premiseCount = new HashMap<Sentence, Integer>();

        for(Literal l : this.literals.values())
        {
            try
            {
                if(l.evaluate())
                {
                    agenda.push(l);
                }
            }
            catch(NotSolvableException e)
            {

            }
        }

        for(Sentence sen : this.sentances)
        {
            premiseCount.put(sen, sen.getPremise().size());
        }

        while(!agenda.isEmpty())
        {
            Literal p = agenda.pop();
            if(p.getName() == this.ask)
            {
                printSolution();
            }

            for(Sentence s : this.sentances)
            {
                if(s.getPremise().contains(p))
                {
                    int temp = premiseCount.get(s);
                    premiseCount.put(s, temp--);
                }

                if(premiseCount.get(s) == 0)
                {

                }
            }
        }
    }

    private void printSolution()
    {

    }

}
