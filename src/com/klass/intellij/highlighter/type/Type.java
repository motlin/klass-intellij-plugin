package com.klass.intellij.highlighter.type;

import org.eclipse.collections.api.block.HashingStrategy;
import org.eclipse.collections.impl.block.factory.HashingStrategies;
import org.eclipse.collections.impl.list.mutable.ListAdapter;
import org.eclipse.collections.impl.set.strategy.mutable.UnifiedSetWithHashingStrategy;

import java.util.List;
import java.util.Objects;

public class Type
{
    private final PrimitiveTypeType primitiveTypeType;
    private final String typeName;
    private final Multiplicity multiplicity;

    public Type(
            PrimitiveTypeType primitiveTypeType,
            String typeName,
            Multiplicity multiplicity)
    {
        this.primitiveTypeType = Objects.requireNonNull(primitiveTypeType);
        this.typeName = Objects.requireNonNull(typeName);
        this.multiplicity = Objects.requireNonNull(multiplicity);
    }

    public PrimitiveTypeType getPrimitiveTypeType()
    {
        return this.primitiveTypeType;
    }

    public String getTypeName()
    {
        return this.typeName;
    }

    public Multiplicity getMultiplicity()
    {
        return this.multiplicity;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o)
        {
            return true;
        }
        if (o == null || this.getClass() != o.getClass())
        {
            return false;
        }

        Type type = (Type) o;

        if (this.primitiveTypeType != type.primitiveTypeType)
        {
            return false;
        }
        if (!this.typeName.equals(type.typeName))
        {
            return false;
        }
        return this.multiplicity == type.multiplicity;
    }

    @Override
    public int hashCode()
    {
        int result = this.primitiveTypeType.hashCode();
        result = 31 * result + this.typeName.hashCode();
        result = 31 * result + this.multiplicity.hashCode();
        return result;
    }

    @Override
    public String toString()
    {
        return String.format("%s[%s]", this.typeName, this.multiplicity.getPrettyName());
    }

    public static boolean compatible(List<Type> sourceTypes, List<Type> targetTypes)
    {
        HashingStrategy<Type> hashingStrategy = HashingStrategies.fromFunctions(
                Type::getPrimitiveTypeType,
                Type::getTypeName);
        UnifiedSetWithHashingStrategy<Type> set = UnifiedSetWithHashingStrategy.newSet(hashingStrategy);
        set.addAll(sourceTypes);
        return ListAdapter.adapt(targetTypes).anySatisfy(set::contains);
    }
}
