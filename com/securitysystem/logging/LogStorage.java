package com.securitysystem.logging;

package com.securitysystem.logging;

import com.securitysystem.logging.LogEntry;

public interface LogStorage {
    void saveLogEntry(LogEntry logEntry);
    // Methods to load logs, filter logs, etc., can be added later
}
