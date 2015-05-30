package logic;

import java.util.HashMap;
import java.util.LinkedList;

public interface Logic
{
    /**
     * Gets the value of the logic piece, either true or false, by evaluating every sub piece of logic.
     * 
     * @return <b>true</b> if the logic evaluates to true.
     */
    public boolean evaluate();

    /**
     * Checks to see if all the literals have values.
     * 
     * @return whether the piece of logic can be solved.
     */
    public boolean canSolve();

    /**
     * Gets a list of all literals contained in the piece of logic.
     * 
     * @return a linked list of the literals.
     */
    public LinkedList<Literal> getLiterals();

    /**
     * Set the literals of all following logic operators to be from the same group.
     * 
     * @param terms
     * @return
     */
    public HashMap<String, Literal> setTerms(HashMap<String, Literal> terms);
}
