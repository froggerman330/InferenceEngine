package com.logic;

import java.util.LinkedList;

/**
 * The logic interface defines what all logic can do. An example of logic is any literal or operation.
 * 
 * @author Devon
 *
 */
public interface Logic
{
    /**
     * Gets the value of the logic piece, either true or false, by evaluating every piece of sub-logic.
     * 
     * @return <b>true</b> if the logic evaluates to true.
     */
    public boolean evaluate();

    /**
     * Gets a list of all literals contained in the piece of logic.
     * 
     * @return a linked list of the literals.
     */
    public LinkedList<Literal> getLiterals();
}
