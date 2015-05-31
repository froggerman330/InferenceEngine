package com.logic.operator;

import java.util.HashMap;
import java.util.LinkedList;

import com.logic.Literal;
import com.logic.Logic;

/**
 * And. A&B is a conjunction between A and B. True if both A and B are true.
 * 
 * @author Devon
 *
 */
public class Conjunction implements Operator
{
    private Logic one, two;

    /**
     * The conjunction constructor separates the first and second terms into the appropriate sub-logical pieces.
     * 
     * @param first
     *            the first logical sentence.
     * @param second
     *            the second logical sentence.
     */
    public Conjunction(String first, String second)
    {// gets the location of a bracket neutral logic symbol from a sentence that has had any outer brackets removed.
        String firstTerm = this.trimOuterBrackets(first);
        String secondTerm = this.trimOuterBrackets(second);
        int neutralPos1 = this.findBracketNeutrality(firstTerm);
        int neutralPos2 = this.findBracketNeutrality(secondTerm);

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

        if(neutralPos2 != -1)
        {// if there is a bracket neutral logic symbol split the sentence at the symbol and create a new Operator of the
         // appropriate type with the two substrings.
            switch(secondTerm.charAt(neutralPos2))
            {
                case '&':
                    this.two = new Conjunction(secondTerm.substring(0, neutralPos2),
                            secondTerm.substring(neutralPos2 + 1));
                    break;
                case '|':
                    this.two = new Disjunction(secondTerm.substring(0, neutralPos2),
                            secondTerm.substring(neutralPos2 + 1));
                    break;
                case '<':
                    this.two = new Biconditional(secondTerm.substring(0, neutralPos2),
                            secondTerm.substring(neutralPos2 + 3));
                    break;
                case '=':
                    this.two = new Conditional(secondTerm.substring(0, neutralPos2),
                            secondTerm.substring(neutralPos2 + 2));
                    break;
            }
        }
        else
        {// if no bracket neutral logic was found this sentence must be a literal or a bigger negation.
            if(!secondTerm.startsWith("~"))
            {// if the sentence isn't a negation it must be a literal.
                this.two = new Literal(secondTerm);
            }
            else
            {// if it is a negation, create the negation and add it to the list of operations.
                this.two = new Negation(secondTerm.substring(1));
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
     * @see logic.operator.Operator#getConclusion()
     */
    @Override
    public Logic getConclusion()
    {
        return this.two;
    }

    /*
     * (non-Javadoc)
     * @see logic.Logic#evaluate()
     */
    @Override
    public boolean evaluate()
    {
        return this.one.evaluate() & this.two.evaluate();
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

        if(this.two instanceof Literal)
        {// if the second logical piece is a literal.
            Literal temp = (Literal) this.two;

            if(tempTerms.containsKey(temp.getName()))
            {// if the hashmap has the literal already set the value to be the value of the second literal.
                tempTerms.get(temp.getName()).setValue(temp.evaluate());
            }
            else
            {// if the hashmap doesn't have the literal in it, put the literal in it.
                tempTerms.put(temp.getName(), temp);
            }
            // set the second literal to be the literal from the hashmap.
            this.two = tempTerms.get(temp.getName());
        }
        else
        {// if the second piece of logic isn't a literal, pass the term setting up the line to sublogic.
            tempTerms = ((Operator) this.two).setTerms(tempTerms);
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
        allLogic.addAll(this.two.getLiterals());
        return allLogic;
    }
}
