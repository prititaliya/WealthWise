package comp3350.wwsys.tests.Utils;



import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.io.Files;

import java.io.File;
import java.io.IOException;

import comp3350.wwsys.application.Main;

public class TestUtils {
    private static final File DB_SRC = new File("src/main/assets/db/WealthWiseDB.script");

    public static File copyDB() throws IOException {
        final File target = File.createTempFile("temp-db", ".script");
        Files.copy(DB_SRC, target);
        Main.setDBPathName(target.getAbsolutePath().replace(".script", ""));
        return target;
    }

}
