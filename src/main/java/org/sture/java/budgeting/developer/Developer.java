package org.sture.java.budgeting.developer;

import javafx.scene.input.KeyEvent;

public class Developer {
    public final static boolean DEBUG = true;

    private static int debugMessageIndent = 0;

    /**
     * Prints a debug message to the console.
     * @param message The message to print
     */
    public static void DebugMessage(String message, boolean indentAfter)
    {
        if(DEBUG)
        {
//            String indentStr = "  ".repeat(debugMessageIndent);
            String indentStr = generateIndentString();
            System.out.println(indentStr + message);

            if(indentAfter)
                IndentDebugMessagesOnce();
        }
    }

    public static String generateIndentString(){
        return " ".repeat(debugMessageIndent);
    }

    public static void DebugMessage(String message) {
        DebugMessage(message, false);
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
