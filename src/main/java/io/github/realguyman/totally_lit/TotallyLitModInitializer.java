package io.github.realguyman.totally_lit;

import io.github.realguyman.totally_lit.configuration.Configuration;
import io.github.realguyman.totally_lit.registry.BlockRegistry;
import io.github.realguyman.totally_lit.registry.ItemRegistry;
import io.github.realguyman.totally_lit.registry.TeenyBlockRegistry;
import io.github.realguyman.totally_lit.registry.TeenyItemRegistry;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import me.shedaniel.autoconfig.serializer.PartitioningSerializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;

public class TotallyLitModInitializer implements ModInitializer {
    public static final String IDENTIFIER = "totally_lit";
    private static final Configuration CONFIGURATION = AutoConfig.register(Configuration.class, PartitioningSerializer.wrap(JanksonConfigSerializer::new)).getConfig();

    @Override
    public void onInitialize() {
        new BlockRegistry();
        new ItemRegistry();
        if (FabricLoader.getInstance().isModLoaded("teenycoal")) {
            System.out.println("[Totally Lit!] Found teenycoal, registering additional blocks and items.");
            new TeenyBlockRegistry();
            new TeenyItemRegistry();
        }
    }

    public static Configuration getConfiguration() {
        return CONFIGURATION;
    }
}
