package engine.method;

import java.util.HashMap;
import java.util.LinkedList;

import logic.Literal;
import logic.Sentence;

/**
 * The backward chaining solve method. Takes all literals known, all sentences known and the literal being asked for.
 * Adds ask to the agenda. Pops the agenda and adds that to history and the solution string. If it's not one of our
 * knowns, get all sentences with it in the conclusion, gets all the premises of those and,
 * 
 * @author Devon
 *
 */
public class BackwardChaining implements SolveMethod
{
    private HashMap<String, Literal> literals;
    private LinkedList<Sentence> sentences;
    private String ask;

    /**
     * The constructor for the backward chaining solution. Assigns values for the literals, sentences and the ask.
     * 
     * @param sentences
     *            all sentences to use with backward chaining.
     * @param literals
     *            all the literals with their values.
     * @param ask
     *            the literal being solved for.
     */
    public BackwardChaining(LinkedList<Sentence> sentences, HashMap<String, Literal> literals, String ask)
    {
        this.literals = literals;
        this.sentences = sentences;
        this.ask = ask;
    }

    /*
     * (non-Javadoc)
     * @see engine.method.SolveMethod#solve()
     */
    @Override
    public void solve()
    {
        LinkedList<Literal> agenda = new LinkedList<Literal>();
        LinkedList<Literal> knowns = new LinkedList<Literal>();
        LinkedList<Literal> history = new LinkedList<Literal>();
        StringBuilder solution = new StringBuilder();

        for(Literal l : this.literals.values())
        {
            if(l.evaluate())
            {// if the value of the literal is true, it's already known.
                knowns.add(l);
            }

            if(l.equals(new Literal(this.ask)))
            {// add ask to the agenda.
                agenda.add(l);
            }
        }

        while(!agenda.isEmpty())
        {
            Literal literal = agenda.pop();
            history.add(literal);
            solution.insert(0, literal.getName() + ", ");

            if(!knowns.contains(literal))
            {// if the literal isn't a known
                for(Sentence s : this.sentences)
                {
                    if(s.getLiterals().size() > 1 && s.getConclusion().getLiterals().contains(literal))
                    {// check all sentences to see if they have more than one literal (IE are not a known) and that the
                     // conclusion contains the literal.
                        LinkedList<Literal> temp = s.getPremise().getLiterals();
                        temp.removeAll(history);
                        if(temp.size() == 0)
                        {// if premise only contains things on the agenda
                            System.out.println("No Solution Found!");
                            return;
                        }
                        else
                        {
                            for(Literal lit : temp)
                            {
                                if(!(agenda.contains(lit)))
                                {
                                    agenda.add(lit);
                                }
                            }
                        }
                    }
                }
            }
        }

        System.out.print("YES: " + solution.substring(0, solution.length() - 2));
    }
}
