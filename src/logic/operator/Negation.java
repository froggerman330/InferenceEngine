package logic.operator;

import java.util.HashMap;
import java.util.LinkedList;

import logic.Literal;
import logic.Logic;

/**
 * Not
 * 
 * @author Devon
 *
 */
public class Negation implements Logic, Operator
{
    private Logic one;

    public Negation(String first)
    {
        String firstTerm = this.trimOuterBrackets(first);
        int neutralPos1 = this.findBracketNeutrality(firstTerm);

        if(neutralPos1 != -1)
        {
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
        {
            if(!firstTerm.startsWith("~"))
            {
                this.one = new Literal(firstTerm);
            }
            else
            {
                this.one = new Negation(firstTerm.substring(1));
            }
        }
    }

    /**
     * @return the first logical term
     */
    @Override
    public Logic getPremise()
    {
        return this.one;
    }

    @Override
    public boolean evaluate()
    {
        return !this.one.evaluate();
    }

    @Override
    public boolean canSolve()
    {
        return this.one.canSolve();
    }

    @Override
    public HashMap<String, Literal> setTerms(HashMap<String, Literal> terms)
    {
        HashMap<String, Literal> tempTerms = terms;
        if(this.one instanceof Literal)
        {
            Literal temp = (Literal) this.one;

            if(tempTerms.containsKey(temp.getName()))
            {
                tempTerms.get(temp.getName()).setValue(temp.evaluate());
            }
            else
            {
                tempTerms.put(temp.getName(), temp);
            }

            this.one = tempTerms.get(temp.getName());
        }
        else
        {
            tempTerms = this.one.setTerms(tempTerms);
        }

        return tempTerms;
    }

    @Override
    public LinkedList<Literal> getLiterals()
    {
        LinkedList<Literal> allLogic = new LinkedList<Literal>();
        allLogic.addAll(this.one.getLiterals());
        return allLogic;
    }

    @Override
    public Logic getConclusion()
    {
        // no second operator
        return null;
    }
}
