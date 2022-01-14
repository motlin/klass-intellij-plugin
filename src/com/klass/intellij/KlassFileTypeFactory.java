package com.klass.intellij;

import com.intellij.openapi.fileTypes.FileTypeConsumer;
import com.intellij.openapi.fileTypes.FileTypeFactory;
import org.eclipse.collections.api.factory.Lists;
import org.jetbrains.annotations.NotNull;

public class KlassFileTypeFactory extends FileTypeFactory
{
    public KlassFileTypeFactory()
    {
        ClassLoader current = Thread.currentThread().getContextClassLoader();
        try {
            Thread.currentThread().setContextClassLoader(this.getClass().getClassLoader());
            // Force Eclipse Collections to load
            Lists.immutable.empty();
        } finally {
            Thread.currentThread().setContextClassLoader(current);
        }
    }

    @Override
    public void createFileTypes(@NotNull FileTypeConsumer fileTypeConsumer)
    {
        fileTypeConsumer.consume(KlassFileType.INSTANCE);
    }
}
