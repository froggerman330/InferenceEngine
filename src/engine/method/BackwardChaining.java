package engine.method;

import java.util.HashMap;
import java.util.LinkedList;

import logic.Literal;
import logic.Sentence;
import exception.NotSolvableException;

public class BackwardChaining implements SolveMethod
{
    private HashMap<String, Literal> literals;
    private LinkedList<Sentence> sentences;
    private String ask;

    public BackwardChaining(LinkedList<Sentence> sentences, HashMap<String, Literal> literals, String ask)
    {
        this.literals = literals;
        this.sentences = sentences;
        this.ask = ask;
    }

    @Override
    public void solve()
    {
        LinkedList<Literal> agenda = new LinkedList<Literal>();
        HashMap<Sentence, Integer> conclusionCount = new HashMap<Sentence, Integer>();
        StringBuilder solution = new StringBuilder();
        HashMap<Literal, Boolean> starters = new HashMap<Literal, Boolean>();

        for(Literal l : this.literals.values())
        {
            try
            {
                if(l.evaluate())
                {
                    starters.put(l, false);
                }
            }
            catch(NotSolvableException e)
            {
                // do nothing
            }

            if(l.equals(new Literal(this.ask)))
            {
                agenda.add(l);
            }
        }

        for(Sentence sen : this.sentences)
        {
            if(sen.getLiterals().size() != 1)
            {
                conclusionCount.put(sen, sen.getConclusion().size());
            }
        }

        while(!agenda.isEmpty())
        {
            Literal p = agenda.pop();
            solution.insert(0, p.getName() + ", ");

            for(Literal starter : starters.keySet())
            {
                if(!starters.get(starter))
                {
                    if(p.equals(starter))
                    {
                        System.out.println("YES: " + solution.substring(0, solution.length() - 2));
                        return;
                    }
                }
            }

            for(Sentence s : this.sentences)
            {
                if(s.getLiterals().size() != 1)
                {
                    if(s.getConclusion().contains(p))
                    {
                        int temp = conclusionCount.get(s) - 1;
                        conclusionCount.put(s, temp);
                    }

                    if(conclusionCount.get(s) == 0)
                    {
                        for(Literal lit : s.getPremise())
                        {
                            if(!agenda.contains(lit))
                            {
                                agenda.addLast(lit);
                            }
                        }

                        int temp = conclusionCount.get(s) - 1;
                        conclusionCount.put(s, temp);
                    }
                }
            }
        }

        System.out.println("No solution found!");
    }
}
