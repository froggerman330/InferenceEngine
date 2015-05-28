package engine;

import java.util.HashMap;
import java.util.LinkedList;

import logic.Clause;
import logic.Term;
import engine.method.BackwardChaining;
import engine.method.ForwardChaining;
import engine.method.SolveMethod;
import engine.method.TruthTable;
import exception.NotSolvableException;

public class Infer
{
    public static void main(String... args)
    {
        LinkedList<Clause> clauseList = new LinkedList<Clause>();
        HashMap<String, Term> literals = new HashMap<String, Term>();
        HashMap<String, Term> terms = new HashMap<String, Term>();

        // 0 method
        // 1 file
        String[] readData = new Read(args[1]).getClauses();
        String ask = readData[0];
        String[] clauses = new String[readData.length - 1];
        for(int i = 1; i < readData.length; i++)
        {
            clauses[i - 1] = readData[i];
        }

        for(String clause : clauses)
        {
            System.out.println(clause);
            clauseList.add(new Clause(clause));
        }

        // populate list
        for(Clause c : clauseList)
        {
            HashMap<String, Term> cTerm = c.getTerms();
            for(String key : cTerm.keySet())
            {
                Term t = cTerm.get(key);

                if(!terms.containsKey(key))
                {
                    terms.put(t.getName(), t);
                }// does not contain actual knowns.
            }
        }

        // get values for all literals
        for(Clause c : clauseList)
        {
            HashMap<String, Term> cTerm = c.getTerms();
            for(String key : cTerm.keySet())
            {
                Term t = cTerm.get(key);

                if(!literals.containsKey(key))
                {
                    literals.put(t.getName(), t);
                }// does not contain actual knowns.
                else
                {
                    Term t1 = literals.get(key);
                    try
                    {
                        t1.setValue(t1.evaluate());
                    }
                    catch(NotSolvableException e)
                    {
                        try
                        {
                            t1.setValue(t.evaluate());
                        }
                        catch(NotSolvableException e1)
                        {
                        }
                    }

                }
            }
        }// all terms are now in terms and have values and are linked.

        // add values to the terms
        for(Term lit : terms.values())
        {
            try
            {
                lit.setValue(literals.get(lit.getName()).evaluate());
            }
            catch(NotSolvableException e)
            {
            }
        }

        // replace all literals in clauses with the terms
        for(Clause c : clauseList)
        {
            HashMap<String, Term> cTerm = c.getTerms();
            for(Term t : cTerm.values())
            {
                t = terms.get(t.getName());
            }
        }

        for(Term lit : literals.values())
        {
            lit.setValue(true);
        }

        SolveMethod method;

        switch(args[0])
        {
            case "TT":
                method = new TruthTable(clauseList, literals);
                break;
            case "FC":
                method = new ForwardChaining(clauseList, literals);
                break;
            case "BC":
                method = new BackwardChaining(clauseList, literals);
        }
    }
}
