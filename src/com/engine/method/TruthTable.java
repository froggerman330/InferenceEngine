package com.engine.method;

import java.util.HashMap;
import java.util.LinkedList;

import com.logic.Literal;
import com.logic.operator.Sentence;

/**
 * The truth table solve method iterates through all variations of all literals and evaluates the sentences at each
 * iteration. If all the sentences are true it checks to see if the asked literal is true. If every time the sentence is
 * true the literal is true, the asked literal can be inferred from the knowledge base.
 * 
 * @author Devon
 *
 */
public class TruthTable extends SolveMethod
{
    /**
     * The constructor for the truth table solution. Assigns values for all the literals, sentences and the ask.
     * 
     * @param sentences
     *            all sentences to use with the truth table method.
     * @param literals
     *            all the literals with their values.
     * @param ask
     *            the literal being solved for.
     */
    public TruthTable(LinkedList<Sentence> sentences, HashMap<String, Literal> literals, String ask)
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
        LinkedList<Literal> orderTerms = new LinkedList<Literal>();
        int i = 0, truths = 0;
        int[] masks = new int[this.literals.size()];

        for(Literal l : this.literals.values())
        {// create a bitmask for each literal.
            masks[i] = 1 << i;
            l.setValue(false);
            orderTerms.addFirst(l);
            i++;
        }

        for(int j = 0; j < Math.pow(2, orderTerms.size()); j++)
        {// iterate through the bits until all options have been tried.
            boolean failed = false;
            for(int k = 0; k < orderTerms.size(); k++)
            {
                if((j & masks[k]) == masks[k])
                {// checks if the number has the bit as a 1 in it. If so, sets the corresponding literal to be true.
                    Literal l = orderTerms.get(k);
                    l.setValue(true);
                }
            }

            for(Sentence s : this.sentences)
            {// evaluate each sentence.
                if(!s.evaluate())
                {// if any sentence isn't true we don't need to continue.
                    failed = true;
                    break;
                }
            }

            if(!failed)
            {// if no sentence failed check if the literal that was asked is also true.
                if(this.literals.get(this.ask).evaluate())
                {// if the literal that was asked is true, the number of times it was true increases.
                    truths++;
                }
                else
                {// if the literal wasn't true then it can be inferred that the asked literal cannot be inferred from
                 // the knowledge base.
                    System.out.println("NO");
                    return;
                }
            }

            for(Literal lit : orderTerms)
            {// reset the literals for the next run.
                lit.setValue(false);
            }
        }

        System.out.println("YES: " + truths);
    }
}
