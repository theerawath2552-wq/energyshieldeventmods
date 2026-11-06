package com.maxgarden.energyshield;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@Mod(EnergyShieldMod.MODID)
public class EnergyShieldMod {
    public static final String MODID = "energyshield";
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);

    public static final RegistryObject<Item> ENERGY_FIST = ITEMS.register("energy_fist",
            () -> new Item(new Item.Properties().stacksTo(1)));

    public EnergyShieldMod() {
        ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onRightClick(PlayerInteractEvent.RightClickItem event) {
        Player player = event.getEntity();
        Level level = event.getLevel();

        if (player.getMainHandItem().getItem() == ENERGY_FIST.get()) {
            player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 40, 4, false, false));
            level.playSound(null, player.getX(), player.getY(), player.getZ(),
                    SoundEvents.BEACON_ACTIVATE, SoundSource.PLAYERS, 1.0F, 1.5F);

            if (level.isClientSide) {
                Vec3 look = player.getLookAngle();
                double shieldDist = 1.5;
                double centerX = player.getX() + look.x * shieldDist;
                double centerY = player.getY() + player.getEyeHeight() + look.y * shieldDist;
                double centerZ = player.getZ() + look.z * shieldDist;

                for (int i = 0; i < 360; i += 15) {
                    double angle = Math.toRadians(i);
                    double r = 1.2;
                    double px = centerX + Math.cos(angle) * r;
                    double py = centerY + Math.sin(angle) * r;
                    double pz = centerZ;
                    level.addParticle(ParticleTypes.ELECTRIC_SPARK, px, py, pz, 0, 0, 0);
                }
            }
        }
    }
}
