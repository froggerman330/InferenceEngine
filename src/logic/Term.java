package logic;

import exception.NotSolvableException;


public class Term implements Logic
{
    private boolean value;
    private boolean canSolve = false;
    private String name;

    public Term(String name)
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
}
