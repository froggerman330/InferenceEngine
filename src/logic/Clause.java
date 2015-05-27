package logic;

import java.util.HashSet;

import logic.operator.Biconditional;
import logic.operator.Conjunction;
import logic.operator.Disjunction;
import logic.operator.Negation;
import exception.NotSolvableException;

public class Clause implements Logic
{
    HashSet<Logic> operators = new HashSet<Logic>();
    HashSet<Term> terms = new HashSet<Term>();
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
            if(!clause.startsWith("~"))
            {
                Term t = new Term(clause);
                t.setValue(true);
                this.terms.add(t);
            }
            else
            {
                this.operators.add(new Negation(clause.substring(0)));
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
            this.terms.add(new Term(term));
        }
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
        for(Term t : this.terms)
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
