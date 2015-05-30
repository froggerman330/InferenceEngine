package logic.operator;

import java.util.HashMap;
import java.util.LinkedList;

import logic.Literal;
import logic.Logic;

/**
 * Implies. A=>B is true if B is true or if A is true and B is false. (If A then B)
 * 
 * @author Devon
 *
 */
public class Conditional implements Operator
{
    private Logic premise, conclusion;

    /**
     * The condition constructor separates the first and second terms into the appropriate sub-logical pieces.
     * 
     * @param first
     *            the first logical sentence.
     * @param second
     *            the second logical sentence.
     */
    public Conditional(String first, String second)
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
                    this.premise = new Conjunction(firstTerm.substring(0, neutralPos1),
                            firstTerm.substring(neutralPos1 + 1));
                    break;
                case '|':
                    this.premise = new Disjunction(firstTerm.substring(0, neutralPos1),
                            firstTerm.substring(neutralPos1 + 1));
                    break;
                case '<':
                    this.premise = new Biconditional(firstTerm.substring(0, neutralPos1),
                            firstTerm.substring(neutralPos1 + 3));
                    break;
                case '=':
                    this.premise = new Conditional(firstTerm.substring(0, neutralPos1),
                            firstTerm.substring(neutralPos1 + 2));
                    break;
            }
        }
        else
        {// if no bracket neutral logic was found this sentence must be a literal or a bigger negation.
            if(!firstTerm.startsWith("~"))
            {// if the sentence isn't a negation it must be a literal.
                this.premise = new Literal(firstTerm);
            }
            else
            {// if it is a negation, create the negation and add it to the list of operations.
                this.premise = new Negation(firstTerm.substring(1));
            }
        }

        if(neutralPos2 != -1)
        {// if there is a bracket neutral logic symbol split the sentence at the symbol and create a new Operator of the
         // appropriate type with the two substrings.
            switch(secondTerm.charAt(neutralPos2))
            {
                case '&':
                    this.conclusion = new Conjunction(secondTerm.substring(0, neutralPos2),
                            secondTerm.substring(neutralPos2 + 1));
                    break;
                case '|':
                    this.conclusion = new Disjunction(secondTerm.substring(0, neutralPos2),
                            secondTerm.substring(neutralPos2 + 1));
                    break;
                case '<':
                    this.conclusion = new Biconditional(secondTerm.substring(0, neutralPos2),
                            secondTerm.substring(neutralPos2 + 3));
                    break;
                case '=':
                    this.conclusion = new Conditional(secondTerm.substring(0, neutralPos2),
                            secondTerm.substring(neutralPos2 + 2));
                    break;
            }
        }
        else
        {// if no bracket neutral logic was found this sentence must be a literal or a bigger negation.
            if(!secondTerm.startsWith("~"))
            {// if the sentence isn't a negation it must be a literal.
                this.conclusion = new Literal(secondTerm);
            }
            else
            {// if it is a negation, create the negation and add it to the list of operations.
                this.conclusion = new Negation(secondTerm.substring(1));
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
        return this.premise;
    }

    /*
     * (non-Javadoc)
     * @see logic.operator.Operator#getConclusion()
     */
    @Override
    public Logic getConclusion()
    {
        return this.conclusion;
    }

    /*
     * (non-Javadoc)
     * @see logic.Logic#evaluate()
     */
    @Override
    public boolean evaluate()
    {// A=>B. True if B is true or A and B are false.
        return this.conclusion.evaluate() ? this.conclusion.evaluate() : !(this.premise.evaluate());
    }

    /*
     * (non-Javadoc)
     * @see logic.operator.Operator#setTerms(java.util.HashMap)
     */
    @Override
    public HashMap<String, Literal> setTerms(HashMap<String, Literal> terms)
    {
        HashMap<String, Literal> tempTerms = terms;
        if(this.premise instanceof Literal)
        {// if the first logical piece is a literal.
            Literal temp = (Literal) this.premise;

            if(tempTerms.containsKey(temp.getName()))
            {// if the hashmap has the literal already set the value to be the value of the first literal.
                tempTerms.get(temp.getName()).setValue(temp.evaluate());
            }
            else
            {// if the hashmap doesn't have the literal in it, put the literal in it.
                tempTerms.put(temp.getName(), temp);
            }
            // set the first literal to be the literal from the hashmap.
            this.premise = tempTerms.get(temp.getName());
        }
        else
        {// if the first piece of logic isn't a literal, pass the term setting up the line to sublogic.
            tempTerms = ((Operator) this.premise).setTerms(tempTerms);
        }

        if(this.conclusion instanceof Literal)
        {// if the second logical piece is a literal.
            Literal temp = (Literal) this.conclusion;

            if(tempTerms.containsKey(temp.getName()))
            {// if the hashmap has the literal already set the value to be the value of the second literal.
                tempTerms.get(temp.getName()).setValue(temp.evaluate());
            }
            else
            {// if the hashmap doesn't have the literal in it, put the literal in it.
                tempTerms.put(temp.getName(), temp);
            }
            // set the second literal to be the literal from the hashmap.
            this.conclusion = tempTerms.get(temp.getName());
        }
        else
        {// if the second piece of logic isn't a literal, pass the term setting up the line to sublogic.
            tempTerms = ((Operator) this.conclusion).setTerms(tempTerms);
        }

        return tempTerms;
    }

    @Override
    public LinkedList<Literal> getLiterals()
    {
        LinkedList<Literal> allLogic = new LinkedList<Literal>();
        allLogic.addAll(this.premise.getLiterals());
        allLogic.addAll(this.conclusion.getLiterals());
        return allLogic;
    }
}
