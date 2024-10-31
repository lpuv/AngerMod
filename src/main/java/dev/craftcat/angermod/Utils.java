package dev.craftcat.angermod;

import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.OutgoingChatMessage;
import net.minecraft.network.chat.PlayerChatMessage;
import net.minecraft.world.entity.player.Player;

public class Utils {

    public static void sendChatMessage(Player player, String message) {
        PlayerChatMessage chatMessage = PlayerChatMessage.unsigned(player.getUUID(), message);

        player.createCommandSourceStack().sendChatMessage(new OutgoingChatMessage.Player(chatMessage), false, ChatType.bind(ChatType.CHAT, player));
    }
}
