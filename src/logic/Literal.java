package logic;

import java.util.HashMap;
import java.util.LinkedList;

import exception.NotSolvableException;

public class Literal implements Logic
{
    private boolean value;
    private boolean canSolve = false;
    private String name;

    public Literal(String name)
    {
        this.name = name;
    }

    public void setValue(boolean value)
    {
        this.value = value;
        this.canSolve = true;
    }

    public String getName()
    {
        return this.name;
    }

    @Override
    public LinkedList<Logic> getLogic()
    {
        LinkedList<Logic> allLogic = new LinkedList<Logic>();
        allLogic.add(this);
        return allLogic;
    }

    @Override
    public boolean evaluate() throws NotSolvableException
    {
        if(this.canSolve)
        {
            return this.value;
        }

        throw new NotSolvableException();
    }

    @Override
    public boolean canSolve()
    {
        return this.canSolve;
    }

    @Override
    public HashMap<String, Literal> setTerms(HashMap<String, Literal> terms)
    {
        return null;
    }
}
