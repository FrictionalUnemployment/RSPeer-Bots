package org.frictional.Muler;


        import java.util.function.Consumer;

public interface IO {
    void write(String output);

    void setReadHandler(Consumer<String> input);

    void stopListener();
}