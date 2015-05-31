package com.logic;

import java.util.LinkedList;

/**
 * A literal is a piece of logic that is either true or false. (Like a boolean variable). It has a name which is the
 * letter code used to define it, knows whether it can be solved (has been assigned a value) and what it's value is.
 * 
 * @author Devon
 *
 */
public class Literal implements Logic
{
    private boolean value;
    private boolean canSolve = false;
    private String name;

    /**
     * The literal constructor sets the name for the literal.
     * 
     * @param name
     *            the name of the literal.
     */
    public Literal(String name)
    {
        this.name = name;
    }

    /**
     * Sets the value of the literal to be true or false.
     * 
     * @param value
     *            the boolean value to attribute to the literal.
     */
    public void setValue(boolean value)
    {
        this.value = value;
        this.canSolve = true;
    }

    /**
     * Gets the name of the literal.
     * 
     * @return the name of the literal.
     */
    public String getName()
    {
        return this.name;
    }

    /*
     * (non-Javadoc)
     * @see logic.Logic#getLiterals()
     */
    @Override
    public LinkedList<Literal> getLiterals()
    {
        LinkedList<Literal> allLogic = new LinkedList<Literal>();
        allLogic.add(this);
        return allLogic;
    }

    /*
     * (non-Javadoc)
     * @see logic.Logic#evaluate()
     */
    @Override
    public boolean evaluate()
    {
        return this.value;
    }

    /**
     * Checks to see if the value of this literal has been set yet.
     * 
     * @return <b>true</b> if the value of this literal has been set.
     */
    public boolean canEvaluate()
    {
        return this.canSolve;
    }

    /*
     * (non-Javadoc) Compares the names of the literals instead of the objects.
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object o)
    {
        return ((Literal) o).getName().equalsIgnoreCase(this.getName());
    }
}
