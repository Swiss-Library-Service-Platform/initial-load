package ch.slsp.InitialLoadUserStaff.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.util.StackLocatorUtil;

/**
 * <strong>Java 8 lambda support for lazy logging. This factory return a Log4j2 specific logger implementation, because SLF4J currently (beginning of 2017) not support Java 8.</strong>
 *
 * <p>Log4j2 Logger interface added support for lambda expressions. This allows client code to lazily log messages without explicitly checking if the requested log level is enabled. For example, previously you would write:</p>
 * <pre>
 * // pre-Java 8 style optimization: explicitly check the log level
 * // to make sure the expensiveOperation() method is only called if necessary
 * if (logger.isTraceEnabled()) {
 *     logger.trace("Some long-running operation returned {}", expensiveOperation());
 * }
 * </pre>
 * <p>With Java 8 you can achieve the same effect with a lambda expression. You no longer need to explicitly check the log level:</p>
 * <pre>
 * // Java-8 style optimization: no need to explicitly check the log level:
 * // the lambda expression is not evaluated if the TRACE level is not enabled
 * logger.trace("Some long-running operation returned {}", () -&gt;  expensiveOperation());
 * </pre>
 *
 * @since 1.0.0
 */
public final class LoggerFactory {

    /**
     * Hide constructor of utility class
     */
    private LoggerFactory() {
    }

    /**
     * Returns a Logger with the name of the calling class.
     *
     * @return The Logger for the calling class
     */
    public static Logger getLogger() {
        return LogManager.getLogger(StackLocatorUtil.getCallerClass(2));
    }

    /**
     * Returns a Logger with the name of the calling class.
     *
     * @return The Logger for the calling class
     */
    public static Logger getLogger(final Class<?> clazz) {
        return LogManager.getLogger(clazz);
    }
}