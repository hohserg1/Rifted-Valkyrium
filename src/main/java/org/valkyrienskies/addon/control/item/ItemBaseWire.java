package org.valkyrienskies.addon.control.item;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import org.valkyrienskies.addon.control.ValkyrienSkiesControl;
import org.valkyrienskies.addon.control.capability.lastRelay.ICapabilityLastRelay;
import org.valkyrienskies.addon.control.config.VSControlConfig;
import org.valkyrienskies.addon.control.network.VSSetWireConnectionMessage;
import org.valkyrienskies.addon.control.nodenetwork.EnumWireType;
import org.valkyrienskies.addon.control.nodenetwork.IVSNode;
import org.valkyrienskies.addon.control.nodenetwork.IVSNodeProvider;
import org.valkyrienskies.addon.control.util.BaseItem;

import javax.annotation.Nullable;
import java.util.List;

public class ItemBaseWire extends BaseItem {
    private EnumWireType wireType = EnumWireType.RELAY;

    public ItemBaseWire(EnumWireType wireType) {
		super(wireType.toString(), true);
        this.wireType = wireType;
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World player,
        List<String> itemInformation,
        ITooltipFlag advanced) {
        itemInformation.add(TextFormatting.BLUE + I18n.format("tooltip.vs_control." + this.wireType.toString()));
    }

    @Override
    public EnumActionResult onItemUse(
            EntityPlayer player, World worldIn, BlockPos pos,
            EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ
    ) {
        TileEntity currentTile = worldIn.getTileEntity(pos);
        ItemStack stack = player.getHeldItem(hand);

        if (currentTile instanceof IVSNodeProvider && !worldIn.isRemote) {
            ICapabilityLastRelay inst = stack.getCapability(ValkyrienSkiesControl.lastRelayCapability, null);
            if (inst == null) return EnumActionResult.PASS;
            if (!inst.hasLastRelay()) this.setLastRelay(player, hand, inst, pos);
            else {
                BlockPos lastPos = inst.getLastRelay();
                if (lastPos == null) return EnumActionResult.PASS;

                double distanceSq = lastPos.distanceSq(pos);
                TileEntity lastPosTile = worldIn.getTileEntity(lastPos);

                if (!lastPos.equals(pos) && lastPosTile != null) {
                    if (distanceSq < VSControlConfig.relayWireLength * VSControlConfig.relayWireLength) {
                        IVSNode lastPosNode = ((IVSNodeProvider) lastPosTile).getNode();
                        IVSNode currentPosNode = ((IVSNodeProvider) currentTile).getNode();
                        if (lastPosNode != null && currentPosNode != null) {
                            //inform the user that theres already a connection when they try to connect
                            if (currentPosNode.isLinkedToNode(lastPosNode)) {
                                player.sendMessage(new TextComponentString(TextFormatting.RED +
                                        I18n.format("message.vs_control.error_relay_connection_already_exists")));

                            }
                            //create connection and consume wire
                            else if (currentPosNode.canLinkToOtherNode(lastPosNode)) {
                                currentPosNode.makeConnection(lastPosNode, this.wireType);
                                if (!player.isCreative()) stack.shrink(1);
                            }
                            //warn that no more connections can be made
                            else {
                                player.sendMessage(new TextComponentString(TextFormatting.RED +
                                        I18n.format("message.vs_control.error_relay_wire_limit", VSControlConfig.networkRelayLimit)));
                            }
                            this.setLastRelay(player, hand, inst, null);
                        }
                    }
                    else {
                        player.sendMessage(new TextComponentString(TextFormatting.RED
                                + I18n.format("message.vs_control.error_relay_wire_length")));
                        this.setLastRelay(player, hand, inst, null);
                    }
                }
                else this.setLastRelay(player, hand, inst, pos);
            }
        }

        if (currentTile instanceof IVSNodeProvider) {
            return EnumActionResult.SUCCESS;
        }

        return EnumActionResult.PASS;
    }

    private void setLastRelay(EntityPlayer player, EnumHand hand, ICapabilityLastRelay inst, @Nullable BlockPos pos) {
        inst.setLastRelay(pos);
        if (player instanceof EntityPlayerMP) {
            ValkyrienSkiesControl.controlNodeNetwork.sendTo(
                    new VSSetWireConnectionMessage(hand, pos),
                    (EntityPlayerMP) player
            );
        }
    }
}