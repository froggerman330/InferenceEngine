package logic;

import java.util.HashMap;
import java.util.LinkedList;

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
    public LinkedList<Literal> getLiterals()
    {
        LinkedList<Literal> allLogic = new LinkedList<Literal>();
        allLogic.add(this);
        return allLogic;
    }

    @Override
    public boolean evaluate()
    {
        return this.value;
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

    @Override
    public boolean equals(Object o)
    {
        return ((Literal) o).getName().equalsIgnoreCase(this.getName());
    }
}
