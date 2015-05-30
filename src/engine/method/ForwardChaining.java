package engine.method;

import java.util.HashMap;
import java.util.LinkedList;

import logic.Literal;
import logic.Sentence;

/**
 * The forward chaining solution class. Scans all sentences and finds the number of premises they have and keeps that
 * information. Finds the knowns from the list of all literals and adds them to the agenda. Pops the agenda and adds it
 * to the solution. If the pop is what was asked, print the solution and finish. Otherwise find all sentences where it
 * is one of the premises, decrease the premise count by one for that sentence. If the sentence has a premise count of
 * zero, all premises have been accounted for so add the conclusion to the agenda.
 * 
 * @author Devon
 *
 */
public class ForwardChaining extends SolveMethod
{
    /**
     * The constructor for the forward chaining solution. Assigns values for all the literals, sentences and the ask.
     * 
     * @param sentences
     *            all sentences to use with forward chaining.
     * @param literals
     *            all the literals with their values.
     * @param ask
     *            the literal being solved for.
     */
    public ForwardChaining(LinkedList<Sentence> sentences, HashMap<String, Literal> literals, String ask)
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
        HashMap<Sentence, Integer> unknownPremiseCount = new HashMap<Sentence, Integer>();
        StringBuilder solution = new StringBuilder();

        for(Literal l : this.literals.values())
        {
            if(l.evaluate())
            {// if the literal is true add it to the agenda.
                agenda.addLast(l);
            }
        }

        for(Sentence sen : this.sentences)
        {
            if(sen.getLiterals().size() != 1)
            {// as long as the sentence isn't just a known (size 1) add it to the sentences that get scanned for
             // premises and conclusions.
                unknownPremiseCount.put(sen, sen.getPremise().getLiterals().size());
            }
        }

        while(!agenda.isEmpty())
        {
            Literal literal = agenda.pop();
            solution.append(literal.getName() + ", ");
            if(literal.equals(new Literal(this.ask)))
            {// if the literal is the same as the ask, print the solution and finish.
                System.out.println("YES: " + solution.substring(0, solution.length() - 2));
                return;
            }

            for(Sentence s : this.sentences)
            {// check each sentence that has more than one literal in it (is not just a known).
                if(s.getLiterals().size() != 1)
                {
                    if(s.getPremise().getLiterals().contains(literal))
                    {// if the sentence contains the literal, subtract one from the unknown premise count.
                        int temp = unknownPremiseCount.get(s) - 1;
                        unknownPremiseCount.put(s, temp);
                    }

                    if(unknownPremiseCount.containsKey(s) && unknownPremiseCount.get(s) == 0)
                    {// if the sentence has no more unknown premises, add all literals from the conclusion to the agenda
                     // if they're not already there.
                        for(Literal lit : s.getConclusion().getLiterals())
                        {
                            if(!agenda.contains(lit))
                            {
                                agenda.addLast(lit);
                            }
                        }
                        // remove finished sentences.
                        unknownPremiseCount.remove(s);
                    }
                }
            }
        }

        System.out.println("No solution found!");
    }
}
