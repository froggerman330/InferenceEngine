package logic.operator;

import java.util.HashMap;
import java.util.LinkedList;

import logic.Literal;
import logic.Logic;
import exception.NotSolvableException;

/**
 * Implies. A=>B is true if B is true or if A is true and B is false. (If A then B)
 * 
 * @author Devon
 *
 */
public class Conditional implements Operator
{
    private Logic premise, conclusion;

    public Conditional(String first, String second)
    {
        String firstTerm = this.trimOuterBrackets(first);
        String secondTerm = this.trimOuterBrackets(second);
        int neutralPos1 = this.findBracketNeutrality(firstTerm);
        int neutralPos2 = this.findBracketNeutrality(secondTerm);

        if(neutralPos1 != -1)
        {
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
        {
            if(!firstTerm.startsWith("~"))
            {
                this.premise = new Literal(firstTerm);
            }
            else
            {
                this.premise = new Negation(firstTerm.substring(1));
            }
        }

        if(neutralPos2 != -1)
        {
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
        {
            if(!secondTerm.startsWith("~"))
            {
                this.conclusion = new Literal(secondTerm);
            }
            else
            {
                this.conclusion = new Negation(secondTerm.substring(1));
            }
        }
    }

    /**
     * @return the first logical term
     */
    @Override
    public Logic getPremise()
    {
        return this.premise;
    }

    /**
     * @return the second logical term
     */
    @Override
    public Logic getConclusion()
    {
        return this.conclusion;
    }

    @Override
    public boolean evaluate() throws NotSolvableException
    {
        if(this.canSolve())
        {
            boolean value = this.conclusion.evaluate() ? this.conclusion.evaluate() : !(this.premise.evaluate());
            return value;
        }

        throw new NotSolvableException();
    }

    @Override
    public boolean canSolve()
    {
        return this.premise.canSolve() && this.conclusion.canSolve();
    }

    @Override
    public HashMap<String, Literal> setTerms(HashMap<String, Literal> terms)
    {
        HashMap<String, Literal> tempTerms = terms;
        if(this.premise instanceof Literal)
        {
            Literal temp = (Literal) this.premise;

            if(tempTerms.containsKey(temp.getName()))
            {
                try
                {
                    tempTerms.get(temp.getName()).setValue(temp.evaluate());
                }
                catch(NotSolvableException e)
                {
                }
            }
            else
            {
                tempTerms.put(temp.getName(), temp);
            }

            this.premise = tempTerms.get(temp.getName());
        }
        else
        {
            tempTerms = this.premise.setTerms(tempTerms);
        }

        if(this.conclusion instanceof Literal)
        {
            Literal temp = (Literal) this.conclusion;

            if(tempTerms.containsKey(temp.getName()))
            {
                try
                {
                    tempTerms.get(temp.getName()).setValue(temp.evaluate());
                }
                catch(NotSolvableException e)
                {
                }
            }
            else
            {
                tempTerms.put(temp.getName(), temp);
            }

            this.conclusion = tempTerms.get(temp.getName());
        }
        else
        {
            tempTerms = this.conclusion.setTerms(tempTerms);
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
