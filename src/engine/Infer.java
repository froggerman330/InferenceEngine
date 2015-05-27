package engine;

import java.util.HashMap;
import java.util.LinkedList;

import logic.Clause;
import logic.Term;

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
            for(String key : c.getTerms().keySet())
            {
                if(!terms.containsKey(key))
                {
                    terms.put(key, new Term(key));
                }// does not contain actual knowns.
            }
        }

        System.out.println("done");
        // clause

    }
}
