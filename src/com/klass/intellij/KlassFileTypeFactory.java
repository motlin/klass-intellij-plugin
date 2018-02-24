package com.klass.intellij;

import com.intellij.openapi.fileTypes.FileTypeConsumer;
import com.intellij.openapi.fileTypes.FileTypeFactory;
import org.jetbrains.annotations.NotNull;

public class KlassFileTypeFactory extends FileTypeFactory
{
    @Override
    public void createFileTypes(@NotNull FileTypeConsumer fileTypeConsumer)
    {
        fileTypeConsumer.consume(KlassFileType.INSTANCE);
    }
}
