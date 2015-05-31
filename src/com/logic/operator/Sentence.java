package com.logic.operator;

import java.util.HashMap;
import java.util.LinkedList;

import com.logic.Literal;
import com.logic.Logic;

/**
 * A sentence is an operator that has many terms (which may also be operators).
 * 
 * @author Devon
 *
 */
public class Sentence implements Operator
{
    Logic premise, conclusion;
    LinkedList<Operator> operators = new LinkedList<Operator>();
    Literal literal;

    /**
     * 
     * @param sentence
     *            string containing the sentence (many terms and operations).
     */
    public Sentence(String sentence)
    {// gets the location of a bracket neutral logic symbol from a sentence that has had any outer brackets removed.
        int neutralPos1 = this.findBracketNeutrality(this.trimOuterBrackets(sentence));

        if(neutralPos1 != -1)
        {// if there is a bracket neutral logic symbol split the sentence at the symbol and create a new Operator of the
         // appropriate type with the two substrings.
            Operator o = null;
            switch(sentence.charAt(neutralPos1))
            {
                case '&':
                    o = new Conjunction(sentence.substring(0, neutralPos1), sentence.substring(neutralPos1 + 1));
                    this.operators.add(o);
                    break;
                case '|':
                    o = new Disjunction(sentence.substring(0, neutralPos1), sentence.substring(neutralPos1 + 1));
                    this.operators.add(o);
                    break;
                case '<':
                    o = new Biconditional(sentence.substring(0, neutralPos1), sentence.substring(neutralPos1 + 3));
                    this.operators.add(o);
                    break;
                case '=':
                    o = new Conditional(sentence.substring(0, neutralPos1), sentence.substring(neutralPos1 + 2));
                    this.operators.add(o);
                    break;
            }

            this.premise = o.getPremise();
            this.conclusion = o.getConclusion();
        }
        else
        {// if no bracket neutral logic was found this sentence must be a literal or a bigger negation.
            if(!sentence.startsWith("~"))
            {// if the sentence isn't a negation it must be a literal.
                this.literal = new Literal(sentence);
                this.literal.setValue(true);
            }
            else
            {// if it is a negation, create the negation and add it to the list of operations.
                Operator not = new Negation(sentence.substring(1));
                this.operators.add(not);
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
     * @see logic.Logic#getLiterals()
     */
    @Override
    public LinkedList<Literal> getLiterals()
    {
        LinkedList<Literal> allLogic = new LinkedList<Literal>();
        for(Logic l : this.operators)
        {
            allLogic.addAll(l.getLiterals());
        }

        allLogic.add(this.literal);

        return allLogic;
    }

    /*
     * (non-Javadoc)
     * @see logic.Logic#evaluate()
     */
    @Override
    public boolean evaluate()
    {
        boolean value = true;
        if(this.literal != null)
        {// if the sentence has a literal evaluate it.
            value &= this.literal.evaluate();
        }

        for(Logic l : this.operators)
        {
            value &= l.evaluate();
        }

        return value;
    }

    /*
     * (non-Javadoc)
     * @see logic.Logic#setTerms(java.util.HashMap)
     */
    @Override
    public HashMap<String, Literal> setTerms(HashMap<String, Literal> terms)
    {
        HashMap<String, Literal> tempTerms = terms;

        if(this.literal != null)
        {// if the sentence has a literal and therefore is only a literal.
            if(terms.containsKey(this.literal.getName()))
            {// if the hashmap has the literal already get a copy of it.
                Literal temp = tempTerms.get(this.literal.getName());
                if(!temp.canEvaluate())
                {// if you can't evaluate the literal from the map, set the value to the value of this literal (which
                 // should be true).
                    temp.setValue(this.literal.evaluate());
                }
                // set this literal to be the literal from the hashmap.
                this.literal = temp;
            }
            else
            {// if the hashmap doesn't have this literal yet, add it to the hashmap.
                tempTerms.put(this.literal.getName(), this.literal);
            }
        }

        for(Operator op : this.operators)
        {// pass the terms up the chain to be updated in all sub-logic.
            tempTerms.putAll(op.setTerms(tempTerms));

        }

        if(!(this.premise instanceof Literal) && !(this.conclusion instanceof Literal)
                && (this.conclusion != null && this.premise != null))
        {// if it's not an instance of a literal and it's not null it must be a sentence with multiple literals. Update
         // them all to use the same literal objects.
            tempTerms.putAll(((Operator) this.premise).setTerms(tempTerms));
            tempTerms.putAll(((Operator) this.conclusion).setTerms(tempTerms));
        }

        return tempTerms;
    }
}
