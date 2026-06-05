package org.valkyrienskies.addon.control.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IThreadListener;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.valkyrienskies.addon.control.ValkyrienSkiesControl;
import org.valkyrienskies.addon.control.capability.lastRelay.ICapabilityLastRelay;
import org.valkyrienskies.addon.control.item.ItemBaseWire;

import javax.annotation.Nullable;

public class VSSetWireConnectionMessage implements IMessage {
    private EnumHand hand;
    @Nullable
    private BlockPos lastRelay;

    public VSSetWireConnectionMessage() {}

    public VSSetWireConnectionMessage(EnumHand hand, @Nullable BlockPos lastRelay) {
        this.hand = hand;
        this.lastRelay = lastRelay;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        PacketBuffer packetBuf = new PacketBuffer(buf);
        this.hand = EnumHand.values()[packetBuf.readVarInt()];
        this.lastRelay = packetBuf.readBoolean() ? packetBuf.readBlockPos() : null;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        PacketBuffer packetBuf = new PacketBuffer(buf);
        packetBuf.writeVarInt(this.hand.ordinal());
        packetBuf.writeBoolean(this.lastRelay != null);
        if (this.lastRelay != null) {
            packetBuf.writeBlockPos(this.lastRelay);
        }
    }

    public static class Handler implements IMessageHandler<VSSetWireConnectionMessage, IMessage> {
        @Override
        @SideOnly(Side.CLIENT)
        public IMessage onMessage(VSSetWireConnectionMessage message, MessageContext ctx) {
            IThreadListener mainThread = Minecraft.getMinecraft();
            mainThread.addScheduledTask(() -> {
                if (Minecraft.getMinecraft().player == null || message.hand == null) return;

                ItemStack stack = Minecraft.getMinecraft().player.getHeldItem(message.hand);
                if (stack.isEmpty() || !(stack.getItem() instanceof ItemBaseWire)) return;

                ICapabilityLastRelay inst = stack.getCapability(ValkyrienSkiesControl.lastRelayCapability, null);
                if (inst != null) inst.setLastRelay(message.lastRelay);
            });
            return null;
        }
    }
}
