package com.engine.method;

import java.util.HashMap;
import java.util.LinkedList;

import com.logic.Literal;
import com.logic.operator.Sentence;

/**
 * Abstract solve method that holds all common information, a list of all literals and the letter that represents them,
 * a list of all sentences and the string of the literal that is being asked for.
 * 
 * @author Devon
 *
 */
public abstract class SolveMethod
{
    /**
     * Solve the given sentences for the asked value.
     */
    public abstract void solve();

    protected HashMap<String, Literal> literals;
    protected LinkedList<Sentence> sentences;
    protected String ask;
}
