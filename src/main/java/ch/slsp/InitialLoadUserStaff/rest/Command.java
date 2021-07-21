package ch.slsp.InitialLoadUserStaff.rest;

import java.util.Properties;

public interface Command {
    void execute(String... args) throws Exception;

    default void addRunConfigProperties(Properties properties) {

    };
}
