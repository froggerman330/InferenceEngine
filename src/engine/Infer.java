package engine;

import java.util.LinkedList;

import logic.Clause;

public class Infer
{
    public static void main(String... args)
    {
        LinkedList<Clause> clauseList = new LinkedList<Clause>();
        // 0 method
        // 1 file
        // String[] clauses = new Read(args[1]).getClauses();
        String[] clauses = {"(A|(B=>~C))&~((D<=>E)&F)", "", ""};

        for(String clause : clauses)
        {
            System.out.println(clause);
            clauseList.add(new Clause(clause));
        }

        // clause

    }
}
