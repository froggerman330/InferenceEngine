package engine.method;

import java.util.HashMap;
import java.util.LinkedList;

import logic.Literal;
import logic.Sentence;

public class ForwardChaining implements SolveMethod
{
    private HashMap<String, Literal> literals;
    private LinkedList<Sentence> sentences;
    private String ask;

    public ForwardChaining(LinkedList<Sentence> clauseList, HashMap<String, Literal> literals, String ask)
    {
        this.literals = literals;
        this.sentences = clauseList;
        this.ask = ask;
    }

    @Override
    public void solve()
    {
        LinkedList<Literal> agenda = new LinkedList<Literal>();
        HashMap<Sentence, Integer> premiseCount = new HashMap<Sentence, Integer>();
        StringBuilder solution = new StringBuilder();

        for(Literal l : this.literals.values())
        {
            if(l.evaluate())
            {
                agenda.addLast(l);
            }
        }

        for(Sentence sen : this.sentences)
        {
            if(sen.getLiterals().size() != 1)
            {
                premiseCount.put(sen, sen.getPremise().getLiterals().size());
            }
        }

        while(!agenda.isEmpty())
        {
            Literal p = agenda.pop();
            solution.append(p.getName() + ", ");
            if(p.getName().equalsIgnoreCase(this.ask))
            {
                System.out.println("YES: " + solution.substring(0, solution.length() - 2));
                return;
            }

            for(Sentence s : this.sentences)
            {
                if(s.getLiterals().size() != 1)
                {
                    if(s.getPremise().getLiterals().contains(p))
                    {
                        int temp = premiseCount.get(s) - 1;
                        premiseCount.put(s, temp);
                    }

                    if(premiseCount.get(s) == 0)
                    {
                        for(Literal lit : s.getConclusion().getLiterals())
                        {
                            if(!agenda.contains(lit))
                            {
                                agenda.addLast(lit);
                            }
                        }

                        int temp = premiseCount.get(s) - 1;
                        premiseCount.put(s, temp);
                    }
                }
            }
        }

        System.out.println("No solution found!");
    }
}
