package com.klass.intellij.highlighter.type;

import java.util.List;
import java.util.Objects;

import org.eclipse.collections.api.block.HashingStrategy;
import org.eclipse.collections.impl.block.factory.HashingStrategies;
import org.eclipse.collections.impl.list.mutable.ListAdapter;
import org.eclipse.collections.impl.set.strategy.mutable.UnifiedSetWithHashingStrategy;

public class Type
{
    private final DataTypeType dataTypeType;
    private final String       typeName;
    private final Multiplicity multiplicity;

    public Type(
            DataTypeType dataTypeType,
            String typeName,
            Multiplicity multiplicity)
    {
        this.dataTypeType = Objects.requireNonNull(dataTypeType);
        this.typeName = Objects.requireNonNull(typeName);
        this.multiplicity = Objects.requireNonNull(multiplicity);
    }

    public static boolean compatible(List<Type> sourceTypes, List<Type> targetTypes)
    {
        if (sourceTypes.isEmpty() || targetTypes.isEmpty())
        {
            // No point displaying an incompatible type error when we're already going to have an unresolved type error
            return true;
        }

        HashingStrategy<Type> hashingStrategy = HashingStrategies.fromFunctions(
                Type::getDataTypeType,
                Type::getTypeName);
        UnifiedSetWithHashingStrategy<Type> set = UnifiedSetWithHashingStrategy.newSet(hashingStrategy);
        set.addAll(sourceTypes);
        return ListAdapter.adapt(targetTypes).anySatisfy(set::contains);
    }

    public DataTypeType getDataTypeType()
    {
        return this.dataTypeType;
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
    public int hashCode()
    {
        int result = this.dataTypeType.hashCode();
        result = 31 * result + this.typeName.hashCode();
        result = 31 * result + this.multiplicity.hashCode();
        return result;
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

        if (this.dataTypeType != type.dataTypeType)
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
    public String toString()
    {
        return String.format("%s[%s]", this.typeName, this.multiplicity.getPrettyName());
    }
}
