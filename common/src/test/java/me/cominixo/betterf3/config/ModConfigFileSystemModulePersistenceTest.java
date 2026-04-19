package me.cominixo.betterf3.config;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import me.cominixo.betterf3.modules.BaseModule;
import me.cominixo.betterf3.modules.SystemModule;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class ModConfigFileSystemModulePersistenceTest {

    @Test
    void savesAndLoadsSystemModuleCustomSettings(@TempDir final Path tempDir) {
        final String previousWorkingDirectory = System.getProperty("user.dir");
        final List<BaseModule> previousModulesLeft = BaseModule.modules;
        final List<BaseModule> previousModulesRight = BaseModule.modulesRight;
        final List<BaseModule> previousAllModules = new ArrayList<>(BaseModule.allModules);
        System.setProperty("user.dir", tempDir.toString());
        try {
            BaseModule.modules = new ArrayList<>();
            BaseModule.modulesRight = new ArrayList<>();
            BaseModule.allModules.clear();

            final SystemModule systemModuleTemplate = new SystemModule();
            BaseModule.modules.add(systemModuleTemplate);
            BaseModule.allModules.add(systemModuleTemplate);

            systemModuleTemplate.memoryColorToggle = false;
            systemModuleTemplate.timeFormat = "HH:mm";
            ModConfigFile.saveRunnable.run();

            systemModuleTemplate.memoryColorToggle = true;
            systemModuleTemplate.timeFormat = systemModuleTemplate.defaultTimeFormat;

            ModConfigFile.load(ModConfigFile.FileType.JSON);

            final BaseModule loaded = BaseModule.modules.getFirst();
            final SystemModule loadedSystemModule = (SystemModule) loaded;

            assertFalse(loadedSystemModule.memoryColorToggle);
            assertEquals("HH:mm", loadedSystemModule.timeFormat);
        } finally {
            BaseModule.modules = previousModulesLeft;
            BaseModule.modulesRight = previousModulesRight;
            BaseModule.allModules.clear();
            BaseModule.allModules.addAll(previousAllModules);
            System.setProperty("user.dir", previousWorkingDirectory);
        }
    }
}
