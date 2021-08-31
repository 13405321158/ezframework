package org.hibernate.annotations.common.util.impl;

import java.util.Locale;
import java.io.Serializable;
import javax.annotation.processing.Generated;
import org.jboss.logging.DelegatingBasicLogger;
import org.jboss.logging.BasicLogger;
import java.lang.Throwable;
import java.lang.String;
import org.jboss.logging.Logger;


import static org.jboss.logging.Logger.Level.ERROR;
import static org.jboss.logging.Logger.Level.INFO;

/**
 * Warning this class consists of generated code.
 */
@Generated(value = "org.jboss.logging.processor.generator.model.MessageLoggerImplementor", date = "2020-10-30T16:06:47+0000")
public class Log_$logger extends DelegatingBasicLogger implements Log, BasicLogger, Serializable {
    private static final long serialVersionUID = 1L;
    private static final String FQCN = Log_$logger.class.getName();
    public Log_$logger(final Logger log) {
        super(log);
    }
    private static final Locale LOCALE = Locale.ROOT;
    protected Locale getLoggingLocale() {
        return LOCALE;
    }
    @Override
    public final void version(final String version) {
        super.log.logf(FQCN, INFO, null, version$str(), version);
    }
    private static final String version = "HCANN000001: Hibernate Commons Annotations {%1$s}";
    protected String version$str() {
        return version;
    }
    @Override
    public final void assertionFailure(final Throwable t) {
        super.log.logf(FQCN, ERROR, t, assertionFailure$str());
    }
    private static final String assertionFailure = "HCANN000002: An assertion failure occurred (this may indicate a bug in Hibernate)";
    protected String assertionFailure$str() {
        return assertionFailure;
    }
}
