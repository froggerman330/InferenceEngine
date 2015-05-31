package com.logic.operator;

import java.util.HashMap;

import com.logic.Literal;
import com.logic.Logic;

/**
 * An operator is either biconditional <=>, conditional =>, disjunction |, conjunction & or negation ~.
 * 
 * @author Devon
 *
 */
public interface Operator extends Logic
{
    /**
     * Set the literals of all following logic operators to be the same literal objects.
     * 
     * @param terms
     *            a hashmap of the name of the literal and the literal itself.
     * @return a new hashmap with the updated literals.
     */
    public HashMap<String, Literal> setTerms(HashMap<String, Literal> terms);

    /**
     * Gets the premise of any logical operator.
     * 
     * @return the premise or the left side of the operator.
     */
    public Logic getPremise();

    /**
     * Gets the conclusion of any logical operator.
     * 
     * @return the conclusion or the right side of any operator.
     */
    public Logic getConclusion();

    /**
     * Takes a clause string and finds the first piece of logic outside of any brackets and returns the start location
     * within the string.
     * 
     * @param sentence
     *            the sentence to check for bracket neutral logic.
     * @return an integer representing the character position of the start of the logic symbol that is not between
     *         brackets or {@link com.logic.operator.Operator#findLogicSymbol(String) the location of the logic symbol}.
     */
    default int findBracketNeutrality(String sentence)
    {
        int bracketNeutrality = 0;

        if(sentence.startsWith("~"))
        {
            return -1;
        }

        for(int i = 0; i < sentence.toCharArray().length; i++)
        {
            char c = sentence.toCharArray()[i];

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
                return i;
            }
        }

        return findLogicSymbol(sentence);
    }

    /**
     * If the input string has a bracket set surrounding it, remove them.
     * 
     * @param string
     *            the string to check for brackets.
     * @return the string or the string without the surrounding brackets if there were any.
     */
    default String trimOuterBrackets(String string)
    {
        int bracketCount = 0;

        for(int i = 0; i < string.toCharArray().length; i++)
        {
            char c = string.toCharArray()[i];

            if(c == '(')
            {
                bracketCount++;
            }

            if(c == ')')
            {
                bracketCount--;
            }

            if(bracketCount == 0 && i != string.toCharArray().length - 1)
            {
                return string;
            }
        }

        if(string.startsWith("(") && string.endsWith(")"))
        {
            return string.substring(1, string.length() - 1);
        }

        return string;
    }

    /**
     * Returns the index of the first logic symbol contained in the string.
     * 
     * @param sentence
     *            String of literals with logic operators.
     * @return index of first logic symbol or -1 if none exists.
     */
    default int findLogicSymbol(String sentence)
    {
        for(int i = 0; i < sentence.toCharArray().length; i++)
        {
            switch(sentence.toCharArray()[i])
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
}
