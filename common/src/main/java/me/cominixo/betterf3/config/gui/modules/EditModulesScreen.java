package me.cominixo.betterf3.config.gui.modules;

import me.cominixo.betterf3.config.ModConfigFile;
import me.cominixo.betterf3.modules.BaseModule;
import me.cominixo.betterf3.modules.ChunksModule;
import me.cominixo.betterf3.modules.CoordsModule;
import me.cominixo.betterf3.modules.EmptyModule;
import me.cominixo.betterf3.modules.EntityModule;
import me.cominixo.betterf3.modules.FpsModule;
import me.cominixo.betterf3.modules.HelpModule;
import me.cominixo.betterf3.modules.SoundModule;
import me.cominixo.betterf3.modules.SystemModule;
import me.cominixo.betterf3.utils.DebugLine;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import me.shedaniel.clothconfig2.gui.entries.BooleanListEntry;
import me.shedaniel.clothconfig2.gui.entries.ColorEntry;
import me.shedaniel.clothconfig2.gui.entries.IntegerListEntry;
import me.shedaniel.clothconfig2.gui.entries.StringListEntry;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextColor;
import org.apache.commons.lang3.StringUtils;

/**
 * The Edit Modules screen.
 */
public final class EditModulesScreen {

    private EditModulesScreen() {
        // Do nothing
    }

    /**
     * Gets the config builder.
     *
     * @param module the module
     * @param parent the parent module screen
     * @return The ConfigBuilder for the add module screen
     */
    public static ConfigBuilder configBuilder(final BaseModule module, final ModulesScreen parent) {

        final ConfigBuilder builder = ConfigBuilder.create().setParentScreen(parent);

        builder.setSavingRunnable(ModConfigFile.saveRunnable);

        final ConfigEntryBuilder entryBuilder = builder.entryBuilder();

        final ConfigCategory general =
                builder.getOrCreateCategory(Component.translatable("config.betterf3" + ".category.general"));

        final BooleanListEntry moduleEnabled = entryBuilder
                .startBooleanToggle(Component.translatable("config" + ".betterf3.module.enable"), module.enabled)
                .setDefaultValue(true)
                .setTooltip(Component.translatable("config.betterf3.module.enable.tooltip"))
                .setSaveConsumer(newValue -> {
                    module.enabled = newValue;
                    module.enabled(newValue);
                })
                .build();

        general.addEntry(moduleEnabled);

        if (module instanceof CoordsModule coordsModule) {

            final ColorEntry colorX = entryBuilder
                    .startColorField(Component.translatable("config.betterf3.color.x"), coordsModule.colorX.getValue())
                    .setDefaultValue(coordsModule.defaultColorX.getValue())
                    .setTooltip(Component.translatable("config.betterf3.color.x.tooltip"))
                    .setSaveConsumer(newValue -> coordsModule.colorX = TextColor.fromRgb(newValue))
                    .build();

            general.addEntry(colorX);

            final ColorEntry colorY = entryBuilder
                    .startColorField(Component.translatable("config.betterf3.color.y"), coordsModule.colorY.getValue())
                    .setDefaultValue(coordsModule.defaultColorY.getValue())
                    .setTooltip(Component.translatable("config.betterf3.color.y.tooltip"))
                    .setSaveConsumer(newValue -> coordsModule.colorY = TextColor.fromRgb(newValue))
                    .build();

            general.addEntry(colorY);

            final ColorEntry colorZ = entryBuilder
                    .startColorField(Component.translatable("config.betterf3.color.z"), coordsModule.colorZ.getValue())
                    .setDefaultValue(coordsModule.defaultColorZ.getValue())
                    .setTooltip(Component.translatable("config.betterf3.color.z.tooltip"))
                    .setSaveConsumer(newValue -> coordsModule.colorZ = TextColor.fromRgb(newValue))
                    .build();

            general.addEntry(colorZ);
        }

        if (module instanceof FpsModule fpsModule) {

            final ColorEntry colorHigh = entryBuilder
                    .startColorField(
                            Component.translatable("config.betterf3" + ".color.fps.high"),
                            fpsModule.colorHigh.getValue())
                    .setDefaultValue(fpsModule.defaultColorHigh.getValue())
                    .setTooltip(Component.translatable("config.betterf3.color.fps.high.tooltip"))
                    .setSaveConsumer(newValue -> fpsModule.colorHigh = TextColor.fromRgb(newValue))
                    .build();

            general.addEntry(colorHigh);

            final ColorEntry colorMed = entryBuilder
                    .startColorField(
                            Component.translatable("config.betterf3" + ".color.fps.medium"),
                            fpsModule.colorMed.getValue())
                    .setDefaultValue(fpsModule.defaultColorMed.getValue())
                    .setTooltip(Component.translatable("config.betterf3.color.fps.medium.tooltip"))
                    .setSaveConsumer(newValue -> fpsModule.colorMed = TextColor.fromRgb(newValue))
                    .build();

            general.addEntry(colorMed);

            final ColorEntry colorLow = entryBuilder
                    .startColorField(
                            Component.translatable("config.betterf3" + ".color.fps.low"), fpsModule.colorLow.getValue())
                    .setDefaultValue(fpsModule.defaultColorLow.getValue())
                    .setTooltip(Component.translatable("config.betterf3.color.fps.low.tooltip"))
                    .setSaveConsumer(newValue -> fpsModule.colorLow = TextColor.fromRgb(newValue))
                    .build();

            general.addEntry(colorLow);
        }

        if (module instanceof EmptyModule emptyModule) {

            final IntegerListEntry emptyLines = entryBuilder
                    .startIntField(Component.translatable("config.betterf3" + ".empty_lines"), emptyModule.emptyLines)
                    .setDefaultValue(emptyModule.defaultEmptyLines)
                    .setTooltip(Component.translatable("config.betterf3.empty_lines.tooltip"))
                    .setSaveConsumer(newValue -> {
                        if (newValue > 20) {
                            newValue = 20;
                        } else if (newValue < 1) {
                            newValue = 1;
                        }
                        emptyModule.emptyLines = newValue;
                    })
                    .build();

            general.addEntry(emptyLines);
        }

        if (module instanceof ChunksModule chunksModule) {

            final ColorEntry enabledColor = entryBuilder
                    .startColorField(
                            Component.translatable("config.betterf3.color.chunks.enabled"),
                            chunksModule.enabledColor.getValue())
                    .setDefaultValue(chunksModule.defaultEnabledColor.getValue())
                    .setTooltip(Component.translatable("config.betterf3.color.chunks.enabled.tooltip"))
                    .setSaveConsumer(newValue -> chunksModule.enabledColor = TextColor.fromRgb(newValue))
                    .build();

            general.addEntry(enabledColor);

            final ColorEntry disabledColor = entryBuilder
                    .startColorField(
                            Component.translatable("config.betterf3.color.chunks.disabled"),
                            chunksModule.disabledColor.getValue())
                    .setDefaultValue(chunksModule.defaultDisabledColor.getValue())
                    .setTooltip(Component.translatable("config.betterf3.color.chunks.disabled.tooltip"))
                    .setSaveConsumer(newValue -> chunksModule.disabledColor = TextColor.fromRgb(newValue))
                    .build();

            general.addEntry(disabledColor);

            final ColorEntry totalColor = entryBuilder
                    .startColorField(
                            Component.translatable("config.betterf3.color.chunks.total"),
                            chunksModule.totalColor.getValue())
                    .setDefaultValue(chunksModule.defaultTotalColor.getValue())
                    .setTooltip(Component.translatable("config.betterf3.color.chunks.total.tooltip"))
                    .setSaveConsumer(newValue -> chunksModule.totalColor = TextColor.fromRgb(newValue))
                    .build();

            general.addEntry(totalColor);
        }

        if (module instanceof EntityModule entityModule) {
            final ColorEntry totalColor = entryBuilder
                    .startColorField(
                            Component.translatable("config.betterf3.color.entities.total"),
                            entityModule.totalColor.getValue())
                    .setDefaultValue(entityModule.defaultTotalColor.getValue())
                    .setTooltip(Component.translatable("config.betterf3.color.entities.total.tooltip"))
                    .setSaveConsumer(newValue -> entityModule.totalColor = TextColor.fromRgb(newValue))
                    .build();

            general.addEntry(totalColor);
        }

        if (module instanceof HelpModule helpModule) {

            final ColorEntry enabledColor = entryBuilder
                    .startColorField(
                            Component.translatable("config.betterf3.color.help.enabled"),
                            helpModule.enabledColor.getValue())
                    .setDefaultValue(helpModule.defaultEnabledColor.getValue())
                    .setTooltip(Component.translatable("config.betterf3.color.help.enabled.tooltip"))
                    .setSaveConsumer(newValue -> helpModule.enabledColor = TextColor.fromRgb(newValue))
                    .build();

            general.addEntry(enabledColor);

            final ColorEntry disabledColor = entryBuilder
                    .startColorField(
                            Component.translatable("config.betterf3.color.help.disabled"),
                            helpModule.disabledColor.getValue())
                    .setDefaultValue(helpModule.defaultDisabledColor.getValue())
                    .setTooltip(Component.translatable("config.betterf3.color.help.disabled.tooltip"))
                    .setSaveConsumer(newValue -> helpModule.disabledColor = TextColor.fromRgb(newValue))
                    .build();

            general.addEntry(disabledColor);
        }

        if (module instanceof SoundModule soundModule) {
            final ColorEntry maximumColor = entryBuilder
                    .startColorField(
                            Component.translatable("config.betterf3.color.sound.maximum"),
                            soundModule.maximumColor.getValue())
                    .setDefaultValue(soundModule.defaultMaximumColor.getValue())
                    .setTooltip(Component.translatable("config.betterf3.color.sound.maximum.tooltip"))
                    .setSaveConsumer(newValue -> soundModule.maximumColor = TextColor.fromRgb(newValue))
                    .build();

            general.addEntry(maximumColor);
        }

        if (module instanceof SystemModule systemModule) {
            final BooleanListEntry memoryColorToggle = entryBuilder
                    .startBooleanToggle(
                            Component.translatable("config.betterf3.memory_color_toggle"),
                            systemModule.memoryColorToggle)
                    .setDefaultValue(systemModule.defaultMemoryColorToggle)
                    .setTooltip(Component.translatable("config.betterf3.memory_color_toggle.tooltip"))
                    .setSaveConsumer(newValue -> systemModule.memoryColorToggle = newValue)
                    .build();

            general.addEntry(memoryColorToggle);

            final StringListEntry timeFormat = entryBuilder
                    .startStrField(Component.translatable("config.betterf3.time_format"), systemModule.timeFormat)
                    .setDefaultValue(systemModule.defaultTimeFormat)
                    .setTooltip(Component.translatable("config.betterf3.time_format.tooltip"))
                    .setSaveConsumer(newValue -> systemModule.timeFormat = newValue)
                    .build();

            general.addEntry(timeFormat);
        }

        final ColorEntry nameColor = entryBuilder
                .startColorField(Component.translatable("config.betterf3" + ".color.name"), module.nameColor.getValue())
                .setDefaultValue(module.defaultNameColor.getValue())
                .setTooltip(Component.translatable("config.betterf3.color.name.tooltip"))
                .setSaveConsumer(newValue -> module.nameColor = TextColor.fromRgb(newValue))
                .build();

        general.addEntry(nameColor);

        final ColorEntry valueColor = entryBuilder
                .startColorField(
                        Component.translatable("config.betterf3" + ".color.value"), module.valueColor.getValue())
                .setDefaultValue(module.defaultValueColor.getValue())
                .setTooltip(Component.translatable("config.betterf3.color.value.tooltip"))
                .setSaveConsumer(newValue -> module.valueColor = TextColor.fromRgb(newValue))
                .build();

        general.addEntry(valueColor);

        if (module.lines().size() > 1) {
            for (final DebugLine line : module.lines()) {

                if (line.id().isEmpty() || line.id().equals("nothing")) {
                    continue;
                }

                Component name = Component.translatable("text.betterf3.line." + line.id());

                if (name.getString().isEmpty()) {
                    name = Component.nullToEmpty(
                            StringUtils.capitalize(line.id().replace("_", " ")));
                }

                final BooleanListEntry enabled = entryBuilder
                        .startBooleanToggle(name, line.enabled)
                        .setDefaultValue(true)
                        .setTooltip(Component.translatable("config.betterf3.disable_line.tooltip"))
                        .setSaveConsumer(newValue -> line.enabled = newValue)
                        .build();

                general.addEntry(enabled);
            }
        }

        builder.transparentBackground();
        return builder;
    }
}
