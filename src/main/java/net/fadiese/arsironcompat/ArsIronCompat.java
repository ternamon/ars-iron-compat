package net.fadiese.arsironcompat;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(ArsIronCompat.MODID)
public class ArsIronCompat {

    public static final String MODID = "arsironcompat";


    public ArsIronCompat(FMLJavaModLoadingContext context) {
        context.registerConfig(ModConfig.Type.COMMON, ArsIronConfig.SPEC);
    }
}