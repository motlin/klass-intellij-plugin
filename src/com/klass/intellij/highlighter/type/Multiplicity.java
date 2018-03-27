package com.klass.intellij.highlighter.type;

public enum Multiplicity
{
    ZERO_TO_ONE("0..1"),
    ONE_TO_ONE("1..1"),
    ZERO_TO_MANY("0..*"),
    ONE_TO_MANY("1..*"),;

    private final String prettyName;

    Multiplicity(String prettyName)
    {
        this.prettyName = prettyName;
    }

    public String getPrettyName()
    {
        return this.prettyName;
    }
}
