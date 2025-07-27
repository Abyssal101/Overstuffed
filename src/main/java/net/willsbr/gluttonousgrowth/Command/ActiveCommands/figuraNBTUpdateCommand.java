package net.willsbr.gluttonousgrowth.Command.ActiveCommands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.willsbr.gluttonousgrowth.CPMCompat.Capability.CPMDataProvider;
import net.willsbr.gluttonousgrowth.ServerPlayerSettings.PlayerServerSettingsProvider;
import net.willsbr.gluttonousgrowth.StuffedBar.PlayerCalorieMeterProvider;
import net.willsbr.gluttonousgrowth.WeightSystem.PlayerWeightBarProvider;
import net.willsbr.gluttonousgrowth.networking.ModMessages;
import net.willsbr.gluttonousgrowth.networking.packet.*;

public class figuraNBTUpdateCommand {
    private static final SimpleCommandExceptionType ERROR_FAILED = new SimpleCommandExceptionType(Component.translatable("commands.setLayer.failed"));

    public static void register(CommandDispatcher<CommandSourceStack> pDispatcher, CommandBuildContext pContext) {
        pDispatcher.register(Commands.literal("ggconfig").then(Commands.literal("updateNBT").executes((p_138618_) -> {
            if(p_138618_.getSource().isPlayer())
            {
                return updateNBT(p_138618_.getSource(),p_138618_.getSource().getPlayer());
            }
            return 0;
        })));
    }

    private static int updateNBT(CommandSourceStack pSource, Player player) throws CommandSyntaxException {
       updateNBT(player);
        return 0;
    }

    public static void updateNBT(Player player)  {
        CompoundTag cpmNBT= new CompoundTag();
        CompoundTag stuffedNBT= new CompoundTag();
        CompoundTag weightNBT= new CompoundTag();
        CompoundTag serverSettingsNBT= new CompoundTag();


        player.getCapability(CPMDataProvider.PLAYER_CPM_DATA).ifPresent(cpmData -> {
            cpmData.saveNBTData(cpmNBT);
        });

        player.getCapability(PlayerCalorieMeterProvider.PLAYER_CALORIE_METER).ifPresent(stuffedBar -> {
            stuffedBar.saveNBTData(stuffedNBT);
        });
        player.getCapability(PlayerWeightBarProvider.PLAYER_WEIGHT_BAR).ifPresent(weightBar -> {
            weightBar.saveNBTData(weightNBT);
        });
        player.getCapability(PlayerServerSettingsProvider.PLAYER_SERVER_SETTINGS).ifPresent(serverSettings -> {
            serverSettings.saveNBTData(serverSettingsNBT);
        });
        ModMessages.sendToPlayer(new SyncCapsS2C(weightNBT,stuffedNBT,cpmNBT,serverSettingsNBT),(ServerPlayer) player);
    }

}
