package com.github.virtualonno.itemcapes.mixins.client;

import com.github.virtualonno.itemcapes.CapeItem;
import com.github.virtualonno.itemcapes.CapeTextureRegistrar;
import com.github.virtualonno.itemcapes.ItemCapes;
import com.mojang.authlib.GameProfile;

import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.ProfilePublicKey;
import net.minecraft.client.multiplayer.PlayerInfo;

import net.minecraft.world.level.Level;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.Nullable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;



import top.theillusivec4.curios.api.CuriosApi;


import java.util.HashMap;
import java.util.Map;

@Mixin(value = AbstractClientPlayer.class)
public abstract class AbstractClientPlayerEntityMixin extends Player
{
public AbstractClientPlayerEntityMixin(Level level, BlockPos blockPos, float f, GameProfile gameProfile, @Nullable ProfilePublicKey profilePublicKey) {
        super(level, blockPos, f, gameProfile, profilePublicKey);
    }

    // private static final Map<String, ResourceLocation> TEXTURE_CACHE = new HashMap<>();
    private static final RegistryObject<Item> CAPE = RegistryObject.create(new ResourceLocation("itemcapes:cape"), ForgeRegistries.ITEMS);

    @Inject(method = "getCloakTextureLocation", at = @At("HEAD"), cancellable = true)
    private void redirectCapeTexture(CallbackInfoReturnable<ResourceLocation> cir)
    {
        //cir.setReturnValue(CapeTextureRegistrar.DEFAULT_CAPE);
        this.findCurio(cir);
    }

    @Inject(method = "getElytraTextureLocation", at = @At("HEAD"), cancellable = true)
    private void redirectElytraTexture(CallbackInfoReturnable<ResourceLocation> cir) {
        this.findCurio(cir);
     }
  
     private void findCurio(CallbackInfoReturnable<ResourceLocation> cir) {
      //deprecated method, use getCuriosInventory for 1.20+ instead
      //item -> ForgeRegistries.ITEMS.getKey(item.getItem()).getNamespace().equals("itemcapes")
        CuriosApi.getCuriosHelper().findEquippedCurio(item -> item.getItem() instanceof CapeItem, (AbstractClientPlayer) (Object) this).map(t -> {
           ItemStack stack = (ItemStack)t.getRight();
           String name = ForgeRegistries.ITEMS.getKey(stack.getItem()).getPath();
            return CapeTextureRegistrar.get(name);

        }).ifPresent(cir::setReturnValue);
     }

    // @Inject(method = "getElytraTextureLocation", at = @At("HEAD"), cancellable = true)
    // private void redirectElytraTexture(CallbackInfoReturnable<ResourceLocation> cir)
    // {
    //     if (ItemCapes.Config.INSTANCE.showOnElytras.get()) findCurio(cir, true);
    // }

    //private void findCurio(CallbackInfoReturnable<ResourceLocation> cir, boolean elytra)
   //{
        // CuriosApi.getCuriosHelper()
        //         .findEquippedCurio(CAPE.get(), (AbstractClientPlayer) (Object) this)
        //         .map(t ->
        //         {
        //             ItemStack stack = t.getRight();
        //             CompoundTag tag = stack.getTag();
        //             if (tag != null)
        //             {
        //                 String type = tag.getString(CapeItem.CAPE_TYPE_NBT);
        //                 if (elytra) type = "elytra_" + type;
        //                 return TEXTURE_CACHE.computeIfAbsent(type, s -> ResourceLocation.tryParse(ItemCapes.MODID + ":" + s));
        //             }

                    //return CapeItem.DEFAULT_CAPE;
                //})
                //.ifPresent(cir::setReturnValue);
                
   // }
}