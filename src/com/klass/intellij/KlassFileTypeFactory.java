package com.klass.intellij;

import com.intellij.openapi.fileTypes.*;
import org.jetbrains.annotations.NotNull;

public class KlassFileTypeFactory extends FileTypeFactory
{
    @Override
    public void createFileTypes(@NotNull FileTypeConsumer fileTypeConsumer)
    {
        fileTypeConsumer.consume(KlassFileType.INSTANCE);
    }
}
