package org.sture.java.budgeting.developer;

import javafx.scene.input.KeyEvent;

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

    public static void DebugKeyEvent(KeyEvent event){
        if(DEBUG){
            DebugMessage("Caught key event: " + event.getCode());
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
