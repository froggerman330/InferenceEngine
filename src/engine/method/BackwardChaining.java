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
        LinkedList<Literal> knowns = new LinkedList<Literal>();
        LinkedList<Literal> history = new LinkedList<Literal>();
        StringBuilder solution = new StringBuilder();

        for(Literal l : this.literals.values())
        {
            try
            {
                if(l.evaluate())
                {
                    knowns.add(l);
                }
            }
            catch(NotSolvableException e)
            {
            }

            if(l.equals(new Literal(this.ask)))
            {
                agenda.add(l);
            }
        }

        while(!agenda.isEmpty())
        {
            Literal p = agenda.pop();
            history.add(p);
            solution.insert(0, p.getName() + ", ");
            // solution.add(p);

            if(!knowns.contains(p))
            {
                for(Sentence s : this.sentences)
                {
                    if(s.getLiterals().size() > 1)
                    {
                        if(s.getConclusion().getLiterals().contains(p))
                        {
                            LinkedList<Literal> temp = s.getPremise().getLiterals();
                            temp.removeAll(agenda);
                            if(temp.size() == 0)
                            {
                                System.out.println("No Solution Found!");
                                return;
                            }
                            else
                            {
                                for(Literal lit : temp)
                                {
                                    if(!history.contains(lit))
                                    {
                                        agenda.add(lit);
                                    }
                                }
                            }
                        }
                    }
                }
            }
            // get parents on stack
            // peek stack (if given literal, move to end)
            // if stack empty, solution not found
            // if stack contains only given literals, solution!
        }

        System.out.print("YES: " + solution.substring(0, solution.length() - 2));
    }
}
