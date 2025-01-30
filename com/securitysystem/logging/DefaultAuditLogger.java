package com.securitysystem.logging;

package com.securitysystem.logging;

import com.securitysystem.logging.LogEntry;

public class DefaultAuditLogger implements AuditLogger {

    private LogStorage logStorage; // Dependency on LogStorage

    public DefaultAuditLogger(LogStorage logStorage) {
        this.logStorage = logStorage;
    }

    @Override
    public void logEvent(LogEntry logEntry) {
        logStorage.saveLogEntry(logEntry); // Use LogStorage to persist log
    }
}
