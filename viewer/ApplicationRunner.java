package viewer;

import javax.swing.*;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;

public class ApplicationRunner {
    public static void main(String[] args) throws InterruptedException, InvocationTargetException {
        new SQLiteViewer();
    }
}
