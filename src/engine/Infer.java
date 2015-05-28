package engine;

import java.util.HashMap;
import java.util.LinkedList;

import logic.Clause;
import logic.Term;
import exception.NotSolvableException;

public class Infer
{
    public static void main(String... args)
    {
        LinkedList<Clause> clauseList = new LinkedList<Clause>();
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

        // String[] clauses = {"~b1", "(A|(B=>~C))&~((D<=>E)&F)", "", ""};

        for(String clause : clauses)
        {
            System.out.println(clause);
            clauseList.add(new Clause(clause));
        }

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
                else
                {
                    Term t1 = terms.get(key);
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
        }// all terms are now in terms and have their values if any are given.

        // clause

    }
}
