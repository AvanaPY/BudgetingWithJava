package com.we.suck.at.java.budgettingiguess.developer;

public class Developer {
    private static int debugMessageIndent = 0;
    public static boolean DEBUG = false;
    /**
     * Prints a debug message to the console.
     * @param message The message to print
     */
    public static void DebugMessage(String message)
    {
        if(DEBUG)
        {
            String indent = "  ".repeat(debugMessageIndent);
            System.out.println(indent + message);
        }
    }

    /**
     * Adds a layer of indentation for future log messages.
     */
    public static void IndentDebugMessagesOnce()
    {
        debugMessageIndent++;
    }

    /**
     * Removes a layer of indentation for future log messages.
     */
    public static void DeindentDebugMessagesOnce()
    {
        debugMessageIndent = Math.max(0, debugMessageIndent - 1);
    }
}
