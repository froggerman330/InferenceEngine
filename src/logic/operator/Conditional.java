package logic.operator;

import java.util.HashMap;

import logic.Literal;
import logic.Logic;
import exception.NotSolvableException;

/**
 * Implies. A=>B is true if B is true or if A is true and B is false. (If A then B)
 * 
 * @author Devon
 *
 */
public class Conditional implements Logic
{
    private Logic one, two;

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
                this.one = new Literal(firstTerm);
            }
            else
            {
                this.one = new Negation(firstTerm.substring(1));
            }
        }

        if(neutralPos2 != -1)
        {
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
                    this.two = new Biconditional(secondTerm.substring(0, neutralPos2),
                            secondTerm.substring(neutralPos2 + 2));
                    break;
            }
        }
        else
        {
            if(!secondTerm.startsWith("~"))
            {
                this.two = new Literal(secondTerm);
            }
            else
            {
                this.two = new Negation(secondTerm.substring(1));
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

    /**
     * @return the second logical term
     */
    public Logic getTwo()
    {
        return this.two;
    }

    @Override
    public boolean evaluate() throws NotSolvableException
    {
        if(this.canSolve())
        {
            return this.two.evaluate() ? this.two.evaluate() : !(this.one).evaluate();
        }

        throw new NotSolvableException();
    }

    @Override
    public boolean canSolve()
    {
        return this.one.canSolve() && this.two.canSolve();
    }

    @Override
    public void setTerms(HashMap<String, Literal> terms)
    {
        if(this.one instanceof Literal)
        {
            Literal temp = (Literal) this.one;

            if(terms.containsKey(temp.getName()))
            {
                try
                {
                    terms.get(temp.getName()).setValue(temp.evaluate());
                }
                catch(NotSolvableException e)
                {
                }
            }
            else
            {
                terms.put(temp.getName(), temp);
            }

            this.one = terms.get(temp.getName());
        }
        else
        {
            this.one.setTerms(terms);
        }

        if(this.two instanceof Literal)
        {
            Literal temp = (Literal) this.two;

            if(terms.containsKey(temp.getName()))
            {
                try
                {
                    terms.get(temp.getName()).setValue(temp.evaluate());
                }
                catch(NotSolvableException e)
                {
                }
            }
            else
            {
                terms.put(temp.getName(), temp);
            }

            this.two = terms.get(temp.getName());
        }
        else
        {
            this.two.setTerms(terms);
        }
    }
}
