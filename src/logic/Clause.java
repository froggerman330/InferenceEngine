package logic;

import java.util.HashMap;
import java.util.HashSet;

import logic.operator.Biconditional;
import logic.operator.Conjunction;
import logic.operator.Disjunction;
import logic.operator.Negation;
import exception.NotSolvableException;

public class Clause implements Logic
{
    HashSet<Logic> operators = new HashSet<Logic>();
    HashMap<String, Term> terms = new HashMap<String, Term>();
    String clause;

    /**
     * 
     * @param clause
     *            string containing no spaces of a clause (many terms)
     */
    public Clause(String clause)
    {
        this.clause = clause;
        this.findTerms();

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
            Term t = null;
            if(!clause.startsWith("~"))
            {
                t = new Term(clause);
                t.setValue(true);
            }
            else
            {// TODO: make sure it is actually a term (I think not always)
                Negation not = new Negation(clause.substring(1));
                if(not.getOne() instanceof Term)
                {
                    t = (Term) not.getOne();
                    t.setValue(false);
                }
                else
                {
                    this.operators.add(not);
                }
            }

            if(!this.terms.containsKey(t.getName()))
            {
                this.terms.put(t.getName(), t);
            }
            else
            {
                Term t1 = this.terms.get(t.getName());
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

    private void findTerms()
    {
        String terms = this.clause.replaceAll("<=>", ",");
        terms = terms.replaceAll("=>", ",");
        terms = terms.replaceAll("\\|", ",");
        terms = terms.replaceAll("&", ",");
        terms = terms.replaceAll("\\(", "");
        terms = terms.replaceAll("\\)", "");
        terms = terms.replaceAll("~", "");

        for(String term : terms.split(","))
        {
            this.terms.put(term, new Term(term));
        }
    }

    public HashMap<String, Term> getTerms()
    {
        return this.terms;
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
        for(Term t : this.terms.values())
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
}
