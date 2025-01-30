package com.securitysystem.logging;

import com.securitysystem.logging.LogEntry;

public interface AuditLogger {
    void logEvent(LogEntry logEntry);
    // Methods to retrieve logs, filter logs, etc., can be added later
}
