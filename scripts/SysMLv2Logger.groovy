import com.nomagic.magicdraw.core.Application
import com.nomagic.magicdraw.core.GUILog
import org.apache.log4j.Logger
import java.io.StringWriter
import java.io.PrintWriter
import java.io.File
import java.text.SimpleDateFormat

/**
 * Robust Logger for SysMLv2 Groovy Scripts.
 * Ensures that all messages and exceptions are output to both
 * the standard Log4j system AND the MagicDraw GUI Console.
 *
 * Optionally also writes to a dedicated log file that is CLEARED on the
 * first call (new run = fresh log). This makes it easy for an AI agent or
 * automation to tail the file after a script run and see only THIS run's
 * output — see the constructors that accept a File argument.
 */
class SysMLv2Logger {
    private final Logger log
    private final GUILog gl
    private final File logFile               // nullable — no file when absent
    private final SimpleDateFormat tsFormat =
        new SimpleDateFormat('yyyy-MM-dd HH:mm:ss.SSS')

    /**
     * Initialize logger for a specific class. No dedicated file.
     */
    public SysMLv2Logger(Class<?> clazz) {
        this(clazz, null)
    }

    /**
     * Initialize logger for a specific name. No dedicated file.
     */
    public SysMLv2Logger(String name) {
        this(name, null)
    }

    /**
     * Initialize logger for a class, ALSO writing to a dedicated file
     * that is CLEARED on construction. Pass null for logFile to skip.
     */
    public SysMLv2Logger(Class<?> clazz, File logFile) {
        this.log = Logger.getLogger(clazz)
        this.gl = Application.getInstance().getGUILog()
        this.logFile = prepareLogFile(logFile)
    }

    /**
     * Initialize logger for a name, ALSO writing to a dedicated file
     * that is CLEARED on construction. Pass null for logFile to skip.
     */
    public SysMLv2Logger(String name, File logFile) {
        this.log = Logger.getLogger(name)
        this.gl = Application.getInstance().getGUILog()
        this.logFile = prepareLogFile(logFile)
    }

    /**
     * Ensure the parent directory exists and the file is empty. Return the
     * File, or null on any failure (never throws — logging must not kill
     * the script). Any failure here is reported on stderr once.
     */
    private static File prepareLogFile(File f) {
        if (f == null) return null
        try {
            File parent = f.getParentFile()
            if (parent != null && !parent.exists()) {
                parent.mkdirs()
            }
            // Truncate by overwriting with an empty byte array.
            f.bytes = new byte[0]
            return f
        } catch (Throwable t) {
            System.err.println('[SysMLv2Logger] could not prepare log file ' +
                f.getAbsolutePath() + ': ' + t.getMessage())
            return null
        }
    }

    /**
     * Append a pre-tagged line to the dedicated log file. Never throws.
     */
    private void appendToFile(String level, String message, Throwable t) {
        if (logFile == null) return
        try {
            StringBuilder sb = new StringBuilder()
            sb.append(tsFormat.format(new Date()))
              .append(' [').append(level).append('] ')
              .append(message)
              .append('\n')
            if (t != null) {
                sb.append(stackTraceAsString(t)).append('\n')
            }
            logFile << sb.toString()
        } catch (Throwable ignored) {
            // Do not let logging break the script. If the file is gone or
            // locked, prefer silent degradation.
        }
    }

    /**
     * Log an info message.
     * Note: Does not echo to GUI log to reduce console clutter.
     * Only WARN and ERROR are shown in the user console.
     */
    public void info(String message) {
        log.info(message)
        appendToFile('INFO', message, null)
    }

    /**
     * Log a debug message.
     * Note: Does not echo to GUI log by default to avoid clutter.
     */
    public void debug(String message) {
        log.debug(message)
        appendToFile('DEBUG', message, null)
    }

    /**
     * Log a warning message.
     */
    public void warn(String message) {
        log.warn(message)
        if (gl != null) {
            gl.log('[WARNING] ' + message)
        }
        appendToFile('WARN', message, null)
    }

    /**
     * Log a warning with an exception.
     * STRICT RULE: Never swallow exceptions.
     */
    public void warn(String message, Throwable t) {
        log.warn(message, t)
        if (gl != null) {
            gl.log('[WARNING] ' + message + '\n' + stackTraceAsString(t))
        } else {
            System.err.println('[WARNING] ' + message)
            t.printStackTrace(System.err)
        }
        appendToFile('WARN', message, t)
    }

    /**
     * Log an error message to GUI popup/console and log4j.
     */
    public void error(String message) {
        log.error(message)
        if (gl != null) {
            gl.log('[ERROR] ' + message)
        } else {
            System.err.println('[ERROR] ' + message)
        }
        appendToFile('ERROR', message, null)
    }

    /**
     * Log a critical error with an exception.
     * STRICT RULE: Never swallow exceptions.
     */
    public void error(String message, Throwable t) {
        log.error(message, t)
        if (gl != null) {
            gl.log('[ERROR] ' + message + '\n' + stackTraceAsString(t))
        } else {
            System.err.println('[ERROR] ' + message)
            t.printStackTrace(System.err)
        }
        appendToFile('ERROR', message, t)
    }

    /**
     * Utility method to format exception stack traces into a string.
     */
    private static String stackTraceAsString(Throwable t) {
        if (t == null) {
            return ''
        }
        StringWriter sw = new StringWriter()
        PrintWriter pw = new PrintWriter(sw)
        t.printStackTrace(pw)
        return sw.toString()
    }
}
