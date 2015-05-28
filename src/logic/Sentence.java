package logic;

import java.util.HashMap;
import java.util.HashSet;

import logic.operator.Biconditional;
import logic.operator.Conditional;
import logic.operator.Conjunction;
import logic.operator.Disjunction;
import logic.operator.Negation;
import exception.NotSolvableException;

public class Sentence implements Logic
{
    HashSet<Logic> operators = new HashSet<Logic>();
    Literal literal;
    String sentance;

    /**
     * 
     * @param sentence
     *            string containing no spaces of a clause (many terms)
     */
    public Sentence(String sentence)
    {
        this.sentance = sentence;

        int neutralPos1 = this.findBracketNeutrality(this.trimOuterBrackets(sentence));

        if(neutralPos1 != -1)
        {
            switch(sentence.charAt(neutralPos1))
            {
                case '&':
                    this.operators.add(new Conjunction(sentence.substring(0, neutralPos1), sentence
                            .substring(neutralPos1 + 1)));
                    break;
                case '|':
                    this.operators.add(new Disjunction(sentence.substring(0, neutralPos1), sentence
                            .substring(neutralPos1 + 1)));
                    break;
                case '<':
                    this.operators.add(new Biconditional(sentence.substring(0, neutralPos1), sentence
                            .substring(neutralPos1 + 3)));
                    break;
                case '=':
                    this.operators.add(new Conditional(sentence.substring(0, neutralPos1), sentence
                            .substring(neutralPos1 + 2)));
                    break;
            }
        }
        else
        {
            Literal t = null;
            if(!sentence.startsWith("~"))
            {
                t = new Literal(sentence);
                t.setValue(true);
            }
            else
            {
                Negation not = new Negation(sentence.substring(1));
                if(not.getOne() instanceof Literal)
                {
                    t = (Literal) not.getOne();
                    t.setValue(false);
                }
                else
                {
                    this.operators.add(not);
                }
            }

            this.literal = t;
        }
    }

    @Override
    public boolean evaluate() throws NotSolvableException
    {
        boolean value = true;
        if(this.literal != null)
        {
            value &= this.literal.evaluate();
        }

        for(Logic l : this.operators)
        {
            value &= l.evaluate();
        }

        return value;
    }

    @Override
    public boolean canSolve()
    {
        if(!this.literal.canSolve())
        {
            return false;
        }

        for(Logic l : this.operators)
        {
            if(!l.canSolve())
            {
                return false;
            }
        }

        return true;
    }

    @Override
    public HashMap<String, Literal> setTerms(HashMap<String, Literal> terms)
    {
        HashMap<String, Literal> tempTerms = terms;

        if(this.literal != null)
        {
            if(terms.containsKey(this.literal.getName()))
            {
                Literal temp = tempTerms.get(this.literal.getName());
                if(!temp.canSolve())
                {
                    try
                    {
                        temp.setValue(this.literal.evaluate());
                    }
                    catch(NotSolvableException e)
                    {
                    }
                }

                this.literal = temp;
            }
            else
            {
                tempTerms.put(this.literal.getName(), this.literal);
            }
        }

        for(Logic op : this.operators)
        {
            tempTerms.putAll(op.setTerms(tempTerms));
        }

        return tempTerms;
    }
}
