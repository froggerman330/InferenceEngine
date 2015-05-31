package com.logic.operator;

import java.util.HashMap;
import java.util.LinkedList;

import com.logic.Literal;
import com.logic.Logic;

/**
 * Not. ~A. True if A is false.
 * 
 * @author Devon
 *
 */
public class Negation implements Logic, Operator
{
    private Logic one;

    /**
     * The negation constructor separates the first logical piece into relevant sub-logical pieces.
     * 
     * @param first
     *            the logical sentence.
     */
    public Negation(String first)
    {// gets the location of a bracket neutral logic symbol from a sentence that has had any outer brackets removed.
        String firstTerm = this.trimOuterBrackets(first);
        int neutralPos1 = this.findBracketNeutrality(firstTerm);

        if(neutralPos1 != -1)
        {// if there is a bracket neutral logic symbol split the sentence at the symbol and create a new Operator of the
         // appropriate type with the two substrings.
            switch(firstTerm.charAt(neutralPos1))
            {
                case '&':
                    this.one = new Conjunction(firstTerm.substring(0, neutralPos1),
                            firstTerm.substring(neutralPos1 + 1));
                    break;
                case '|':
                    this.one = new Disjunction(firstTerm.substring(0, neutralPos1),
                            firstTerm.substring(neutralPos1 + 1));
                    break;
                case '<':
                    this.one = new Biconditional(firstTerm.substring(0, neutralPos1),
                            firstTerm.substring(neutralPos1 + 3));
                    break;
                case '=':
                    this.one = new Conditional(firstTerm.substring(0, neutralPos1),
                            firstTerm.substring(neutralPos1 + 2));
                    break;
            }
        }
        else
        {// if no bracket neutral logic was found this sentence must be a literal or a bigger negation.
            if(!firstTerm.startsWith("~"))
            {// if the sentence isn't a negation it must be a literal.
                this.one = new Literal(firstTerm);
            }
            else
            {// if it is a negation, create the negation and add it to the list of operations.
                this.one = new Negation(firstTerm.substring(1));
            }
        }
    }

    /*
     * (non-Javadoc)
     * @see logic.operator.Operator#getPremise()
     */
    @Override
    public Logic getPremise()
    {
        return this.one;
    }

    /*
     * (non-Javadoc)
     * @see logic.Logic#evaluate()
     */
    @Override
    public boolean evaluate()
    {
        return !this.one.evaluate();
    }

    /*
     * (non-Javadoc)
     * @see logic.operator.Operator#setTerms(java.util.HashMap)
     */
    @Override
    public HashMap<String, Literal> setTerms(HashMap<String, Literal> terms)
    {
        HashMap<String, Literal> tempTerms = terms;
        if(this.one instanceof Literal)
        {// if the first logical piece is a literal.
            Literal temp = (Literal) this.one;

            if(tempTerms.containsKey(temp.getName()))
            {// if the hashmap has the literal already set the value to be the value of the first literal.
                tempTerms.get(temp.getName()).setValue(temp.evaluate());
            }
            else
            {// if the hashmap doesn't have the literal in it, put the literal in it.
                tempTerms.put(temp.getName(), temp);
            }
            // set the first literal to be the literal from the hashmap.
            this.one = tempTerms.get(temp.getName());
        }
        else
        {// if the first piece of logic isn't a literal, pass the term setting up the line to sublogic.
            tempTerms = ((Operator) this.one).setTerms(tempTerms);
        }

        return tempTerms;
    }

    /*
     * (non-Javadoc)
     * @see logic.Logic#getLiterals()
     */
    @Override
    public LinkedList<Literal> getLiterals()
    {
        LinkedList<Literal> allLogic = new LinkedList<Literal>();
        allLogic.addAll(this.one.getLiterals());
        return allLogic;
    }

    /*
     * (non-Javadoc)
     * @see logic.operator.Operator#getConclusion()
     */
    @Override
    public Logic getConclusion()
    {
        // no second operator
        return null;
    }
}
