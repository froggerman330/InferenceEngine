package logic;

import java.util.HashMap;
import java.util.HashSet;

import logic.operator.Biconditional;
import logic.operator.Conjunction;
import logic.operator.Disjunction;
import logic.operator.Negation;
import exception.NotSolvableException;

public class Sentance implements Logic
{
    HashSet<Logic> operators = new HashSet<Logic>();
    HashMap<String, Literal> literals = new HashMap<String, Literal>();
    String clause;

    /**
     * 
     * @param clause
     *            string containing no spaces of a clause (many terms)
     */
    public Sentance(String clause)
    {
        this.clause = clause;

        int neutralPos1 = this.findBracketNeutrality(this.trimOuterBrackets(clause));

        if(neutralPos1 != -1)
        {
            switch(clause.charAt(neutralPos1))
            {
                case '&':
                    this.operators.add(new Conjunction(clause.substring(0, neutralPos1), clause
                            .substring(neutralPos1 + 1)));
                    break;
                case '|':
                    this.operators.add(new Disjunction(clause.substring(0, neutralPos1), clause
                            .substring(neutralPos1 + 1)));
                    break;
                case '<':
                    this.operators.add(new Biconditional(clause.substring(0, neutralPos1), clause
                            .substring(neutralPos1 + 3)));
                    break;
                case '=':
                    this.operators.add(new Biconditional(clause.substring(0, neutralPos1), clause
                            .substring(neutralPos1 + 2)));
                    break;
            }
        }
        else
        {
            Literal t = null;
            if(!clause.startsWith("~"))
            {
                t = new Literal(clause);
                t.setValue(true);
            }
            else
            {
                Negation not = new Negation(clause.substring(1));
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

            if(!this.literals.containsKey(t.getName()))
            {
                this.literals.put(t.getName(), t);
            }
            else
            {
                Literal t1 = this.literals.get(t.getName());
                try
                {
                    t1.setValue(t.evaluate());
                }
                catch(NotSolvableException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }

    public HashMap<String, Literal> getTerms()
    {
        return this.literals;
    }

    @Override
    public boolean evaluate() throws NotSolvableException
    {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean canSolve()
    {
        for(Literal t : this.literals.values())
        {
            if(!t.canSolve())
            {
                return false;
            }
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
    public void setTerms(HashMap<String, Literal> terms)
    {
        for(Literal lit : this.literals.values())
        {
            if(terms.containsKey(lit.getName()))
            {
                Literal temp = terms.get(lit.getName());
                if(!temp.canSolve())
                {
                    try
                    {
                        temp.setValue(lit.evaluate());
                    }
                    catch(NotSolvableException e)
                    {
                    }
                }

                lit = temp;
            }
            else
            {
                terms.put(lit.getName(), lit);
            }
        }

        this.literals = terms;

        for(Logic op : this.operators)
        {
            op.setTerms(terms);
        }
    }
}
