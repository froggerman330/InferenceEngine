package logic.operator;

import logic.Logic;
import logic.Term;
import exception.NotSolvableException;

/**
 * Not
 * 
 * @author Devon
 *
 */
public class Negation implements Logic
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
                    this.one = new Biconditional(firstTerm.substring(0, neutralPos1),
                            firstTerm.substring(neutralPos1 + 2));
                    break;
            }
        }
        else
        {
            if(!firstTerm.startsWith("~"))
            {
                this.one = new Term(firstTerm);
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
    public Logic getOne()
    {
        return this.one;
    }

    @Override
    public boolean evaluate() throws NotSolvableException
    {
        if(this.canSolve())
        {
            return !this.one.evaluate();
        }

        throw new NotSolvableException();
    }

    @Override
    public boolean canSolve()
    {
        return this.one.canSolve();
    }
}
