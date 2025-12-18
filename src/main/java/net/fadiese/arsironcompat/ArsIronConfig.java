package net.fadiese.arsironcompat;

import alexthw.ars_elemental.ArsNouveauRegistry;
import com.hollingsworth.arsnouveau.api.spell.SpellSchool;
import com.hollingsworth.arsnouveau.api.spell.SpellSchools;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.*;

public class ArsIronConfig {

    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    public static final ForgeConfigSpec.DoubleValue MAX_MANA_CONVERSION_RATIO;

    public static final ForgeConfigSpec.DoubleValue MANA_COST_CONVERSION_RATIO;

    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> ABJURATION_SCHOOL;
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> AIR_SCHOOL;
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> CONJURATION_SCHOOL;
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> EARTH_SCHOOL;
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> ELEMENTAL_SCHOOL;
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> FIRE_SCHOOL;
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> MANIPULATION_SCHOOL;
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> NECROMANCY_SCHOOL;
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> WATER_SCHOOL;

    private static final Map<SpellSchool, List<Holder<Attribute>>> AN_POWER_CONVERSION_MAP = new HashMap<>();
    private static final Map<SpellSchool, List<Holder<Attribute>>> AN_RESISTANCE_CONVERSION_MAP = new HashMap<>();

    static final ForgeConfigSpec SPEC;

    static {
        {
            BUILDER.push("Mana");
            MAX_MANA_CONVERSION_RATIO = BUILDER.comment(
                            "ISS max mana bonus to Ars Nouveau bonus conversion ratio. Default : 1.0")
                    .defineInRange("maxManaConversionRatio", 1.0d, 0.0d, 10.0d);
            MANA_COST_CONVERSION_RATIO = BUILDER.comment(
                            "ISS spells mana cost to Ars Nouveau mana cost conversion ratio. Default : 1.0")
                    .defineInRange("manaCostConversionRatio", 1.0d, 0.0d, 10.0d);
            BUILDER.pop();
        }
        {
            BUILDER.push("Spell-Schools");
            ABJURATION_SCHOOL = BUILDER.comment(
                            "List of ISS spell schools associated to Ars Nouveau ABJURATION school. Default : [\"irons_spellbooks:holy\"]")
                    .defineListAllowEmpty("abjurationSchools", List.of("irons_spellbooks:holy"),
                            ArsIronConfig::validateISSSchoolType);
            AIR_SCHOOL = BUILDER.comment(
                            "List of ISS spell schools associated to Ars Nouveau AIR school. Default : [\"irons_spellbooks:lightning\"]")
                    .defineListAllowEmpty("airSchools", List.of("irons_spellbooks:lightning"),
                            ArsIronConfig::validateISSSchoolType);
            CONJURATION_SCHOOL = BUILDER.comment(
                            "List of ISS spell schools associated to Ars Nouveau CONJURATION school. Default : [\"irons_spellbooks:evocation\"]")
                    .defineListAllowEmpty("conjurationSchools", List.of("irons_spellbooks:evocation"),
                            ArsIronConfig::validateISSSchoolType);
            EARTH_SCHOOL = BUILDER.comment(
                            "List of ISS spell schools associated to Ars Nouveau EARTH school. Default : [\"irons_spellbooks:nature\"]")
                    .defineListAllowEmpty("earthSchools", List.of("irons_spellbooks:nature"),
                            ArsIronConfig::validateISSSchoolType);
            ELEMENTAL_SCHOOL = BUILDER.comment(
                            "List of ISS spell schools associated to Ars Nouveau ELEMENTAL school. Default : [\"irons_spellbooks:fire\", \"irons_spellbooks:ice\", \"irons_spellbooks:lightning\", \"irons_spellbooks:nature\"]")
                    .defineListAllowEmpty("elementalSchools",
                            List.of("irons_spellbooks:fire", "irons_spellbooks:ice", "irons_spellbooks:lightning",
                                    "irons_spellbooks:nature"), ArsIronConfig::validateISSSchoolType);
            FIRE_SCHOOL = BUILDER.comment(
                            "List of ISS spell schools associated to Ars Nouveau FIRE school. Default : [\"irons_spellbooks:fire\"]")
                    .defineListAllowEmpty("fireSchools", List.of("irons_spellbooks:fire"),
                            ArsIronConfig::validateISSSchoolType);
            MANIPULATION_SCHOOL = BUILDER.comment(
                            "List of ISS spell schools associated to Ars Nouveau MANIPULATION school. Default : [\"irons_spellbooks:ender\"]")
                    .defineListAllowEmpty("manipulationSchools", List.of("irons_spellbooks:ender"),
                            ArsIronConfig::validateISSSchoolType);
            NECROMANCY_SCHOOL = BUILDER.comment(
                            "List of ISS spell schools associated to Ars Nouveau NECROMANCY school. Default : [\"irons_spellbooks:blood\"]")
                    .defineListAllowEmpty("necromancySchools", List.of("irons_spellbooks:blood"),
                            ArsIronConfig::validateISSSchoolType);
            WATER_SCHOOL = BUILDER.comment(
                            "List of ISS spell schools associated to Ars Nouveau WATER school. Default : [\"irons_spellbooks:ice\"]")
                    .defineListAllowEmpty("waterSchools", List.of("irons_spellbooks:ice"),
                            ArsIronConfig::validateISSSchoolType);
            BUILDER.pop();
        }
        SPEC = BUILDER.build();
    }

    private static boolean validateISSSchoolType(final Object obj) {
        return obj instanceof String schoolId && SchoolRegistry.REGISTRY.get()
                .containsKey(ResourceLocation.parse(schoolId));
    }

    public static void onConfigReload() {
        AN_POWER_CONVERSION_MAP.clear();
        AN_RESISTANCE_CONVERSION_MAP.clear();
        addSchoolMappings(SpellSchools.ABJURATION, ABJURATION_SCHOOL.get());
        addSchoolMappings(SpellSchools.ELEMENTAL_AIR, AIR_SCHOOL.get());
        addSchoolMappings(SpellSchools.CONJURATION, CONJURATION_SCHOOL.get());
        addSchoolMappings(SpellSchools.ELEMENTAL_EARTH, EARTH_SCHOOL.get());
        addSchoolMappings(SpellSchools.ELEMENTAL_FIRE, FIRE_SCHOOL.get());
        addSchoolMappings(SpellSchools.MANIPULATION, MANIPULATION_SCHOOL.get());
        addSchoolMappings(ArsNouveauRegistry.NECROMANCY, NECROMANCY_SCHOOL.get());
        addSchoolMappings(SpellSchools.ELEMENTAL_WATER, WATER_SCHOOL.get());
    }

    private static void addSchoolMappings(SpellSchool arsNouveauSchool, List<? extends String> ISSSchools) {
        List<Holder<Attribute>> spellPowerAttributes = new ArrayList<>();
        List<Holder<Attribute>> magicResistanceAttributes = new ArrayList<>();
        ISSSchools.forEach(elt -> {
            ForgeRegistries.ATTRIBUTES.getHolder(ResourceLocation.parse(elt + "_spell_power"))
                    .ifPresent(spellPowerAttributes::add);
            ForgeRegistries.ATTRIBUTES.getHolder(ResourceLocation.parse(elt + "_magic_resist"))
                    .ifPresent(magicResistanceAttributes::add);
        });
        AN_POWER_CONVERSION_MAP.put(arsNouveauSchool, spellPowerAttributes);
        AN_RESISTANCE_CONVERSION_MAP.put(arsNouveauSchool, magicResistanceAttributes);
    }

    public static List<Holder<Attribute>> getISSPowersForANSchool(SpellSchool arsNouveauSchool) {
        return AN_POWER_CONVERSION_MAP.getOrDefault(arsNouveauSchool, Collections.emptyList());
    }

    public static List<Holder<Attribute>> getISSResistancesForANSchool(SpellSchool arsNouveauSchool) {
        return AN_RESISTANCE_CONVERSION_MAP.getOrDefault(arsNouveauSchool, Collections.emptyList());
    }
}
