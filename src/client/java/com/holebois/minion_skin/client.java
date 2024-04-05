package com.holebois.minion_skin;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.common.ServerboundClientInformationPacket;
import net.minecraft.server.level.ClientInformation;

public class client implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> {
            sendInfoPacket();
        });
    }
    
    public void sendInfoPacket() {
		Minecraft mc = Minecraft.getInstance();
        
		ClientInformation ci = new ClientInformation(mc.getLanguageManager().getSelected(), mc.options.renderDistance().get(), mc.options.chatVisibility().get(), mc.options.chatColors().get(), 0b01111111, mc.player.getMainArm(), false, mc.options.allowServerListing().get());
		FriendlyByteBuf data = PacketByteBufs.create();
		ci.write(data);
		ServerboundClientInformationPacket p = new ServerboundClientInformationPacket(data);
		Minecraft.getInstance().getConnection().send(p);
        mc.player.sendSystemMessage(Component.literal("Sent Packet"));
	}
}
