package logic;

import java.util.HashMap;

import exception.NotSolvableException;

public interface Logic
{
    /**
     * 
     * @return
     * @throws NotSolvableException
     */
    public boolean evaluate() throws NotSolvableException;

    public boolean canSolve();

    /**
     * Takes a clause string and finds the first piece of logic outside of any brackets and returns the start location
     * within the string.
     * 
     * @param clause
     *            the clause to check for bracket neutral logic.
     * @return an int representing the character position of the start of the logic symbol or -1 if no logic is found.
     */
    default int findBracketNeutrality(String clause)
    {
        int bracketNeutrality = 0;

        if(clause.startsWith("~"))
        {
            return -1;
        }

        for(int i = 0; i < clause.toCharArray().length; i++)
        {
            char c = clause.toCharArray()[i];

            if(c == '(')
            {
                bracketNeutrality++;
            }
            else if(c == ')')
            {
                bracketNeutrality--;
            }

            if((bracketNeutrality == 0) && (c == '&' | c == '|' | c == '<' | c == '='))
            {
                switch(c)
                {
                    case '&':
                        return i;
                    case '|':
                        return i;
                    case '<':
                        return i;
                    case '=':
                        return i;
                }
            }
        }

        return findLogicSymbol(clause);
    }

    /**
     * 
     * @param clause
     * @return
     */
    default String trimOuterBrackets(String clause)
    {
        int bracketCount = 0;

        for(int i = 0; i < clause.toCharArray().length; i++)
        {
            char c = clause.toCharArray()[i];

            if(c == '(')
            {
                bracketCount++;
            }

            if(c == ')')
            {
                bracketCount--;
            }

            if(bracketCount == 0 && i != clause.toCharArray().length - 1)
            {
                return clause;
            }
        }

        if(clause.startsWith("(") && clause.endsWith(")"))
        {
            return clause.substring(1, clause.length() - 1);
        }

        return clause;
    }

    /**
     * Returns the index of the first logic symbol contained in the string 
     * @param clause String of literals with logic operators
     * @return index of first logic symbol
     */
    default int findLogicSymbol(String clause)
    {
        for(int i = 0; i < clause.toCharArray().length; i++)
        {
            char d = clause.toCharArray()[i];
            switch(d)
            {
                case '&':
                    return i;
                case '|':
                    return i;
                case '<':
                    return i;
                case '=':
                    return i;
            }
        }

        return -1;
    }

    /**
     * Set the literals of all following logic operators to be from the same group.
     * 
     * @param terms
     */
    public void setTerms(HashMap<String, Literal> terms);
}
