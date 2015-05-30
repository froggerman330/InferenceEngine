package logic;

import java.util.HashMap;
import java.util.LinkedList;

import logic.operator.Biconditional;
import logic.operator.Conditional;
import logic.operator.Conjunction;
import logic.operator.Disjunction;
import logic.operator.Negation;
import logic.operator.Operator;

public class Sentence implements Operator
{
    Logic premise, conclusion;
    LinkedList<Operator> operators = new LinkedList<Operator>();
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
        {
            Literal t = null;
            if(!sentence.startsWith("~"))
            {
                t = new Literal(sentence);
                t.setValue(true);
            }
            else
            {
                Operator not = new Negation(sentence.substring(1));
                if(not.getPremise() instanceof Literal)
                {
                    t = (Literal) not.getPremise();
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
        {
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
     * @see logic.Logic#canSolve()
     */
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

    /*
     * (non-Javadoc)
     * @see logic.Logic#setTerms(java.util.HashMap)
     */
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
                    temp.setValue(this.literal.evaluate());
                }

                this.literal = temp;
            }
            else
            {
                tempTerms.put(this.literal.getName(), this.literal);
            }
        }

        for(Operator op : this.operators)
        {
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
