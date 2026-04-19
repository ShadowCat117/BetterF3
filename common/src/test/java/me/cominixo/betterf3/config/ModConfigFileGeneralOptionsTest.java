package me.cominixo.betterf3.config;

import static org.junit.jupiter.api.Assertions.assertFalse;

import java.nio.file.Path;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class ModConfigFileGeneralOptionsTest {

    @Test
    void savesAndLoadsPerformanceOptimizationToggle(@TempDir final Path tempDir) {
        final String previousWorkingDirectory = System.getProperty("user.dir");
        System.setProperty("user.dir", tempDir.toString());
        try {
            GeneralOptions.enablePerformanceOptimizations = true;
            ModConfigFile.load(ModConfigFile.FileType.JSON);

            GeneralOptions.enablePerformanceOptimizations = false;
            ModConfigFile.saveRunnable.run();

            GeneralOptions.enablePerformanceOptimizations = true;
            ModConfigFile.load(ModConfigFile.FileType.JSON);

            assertFalse(GeneralOptions.enablePerformanceOptimizations);
        } finally {
            System.setProperty("user.dir", previousWorkingDirectory);
        }
    }
}
