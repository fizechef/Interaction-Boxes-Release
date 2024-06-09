package com.becks.interaction_boxes.common.predefinedInteractionBoxBlocktypes.inventoryBoxBlock;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LogicalSidedProvider;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

/**
 * Basic implementation of an inventory handling block-entity capable of supporting implementations of inventory-box (or any other inventory interaction method provided)
 * Example of how inventoryboxes can be used with loottables (items get generated on entity load instead of player interaction, because they need to be displayed)
 * @param <C>
 */
public abstract class InventoryBoxBlockEntity<C extends IItemHandlerModifiable & INBTSerializable<CompoundTag>> extends BlockEntity implements Container{

    public static final String LOOT_TABLE_TAG = "LootTable";
    public static final String LOOT_TABLE_SEED_TAG = "LootTableSeed";
    public static final String INVENTOR_TAG = "inventory";
    @Nullable
    protected ResourceLocation lootTable;
    protected long lootTableSeed;
    public static InventoryFactory<ItemStackHandler> defaultInventory(int slots)
    {
        return self -> new ItemStackHandler(slots);
    }
    protected C inventory;
    public InventoryBoxBlockEntity(BlockEntityType<?> p_155228_, BlockPos p_155229_, BlockState state, InventoryFactory<C> inventoryFactory) {
        super(p_155228_, p_155229_, state);
        this.inventory = inventoryFactory.create(this);
    }
    public Item getItemType(int slot) {
        if (slot > inventory.getSlots()){
            return null;
        }
        return inventory.getStackInSlot(slot).getItem();
    }
    @Override
    public @NotNull ItemStack getItem(int slot) {
        if (slot > inventory.getSlots()){
            return Items.AIR.getDefaultInstance();
        }
        return inventory.getStackInSlot(slot);
    }
    public ItemStack removeItem(int slot) {
        this.setChanged();
        if (slot > inventory.getSlots()){
            return null;
        }
        ItemStack i = inventory.extractItem(slot, 1, false);
        if (this.level != null){
            this.level.sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), 3);
        }
        return i;
    }
    @Override
    public void setItem(int slot, ItemStack stack) {
        this.setChanged();
        if (slot <= inventory.getSlots() && inventory.getStackInSlot(slot).getItem().equals(Items.AIR)){
            stack.setCount(1);
            inventory.insertItem(slot, stack,false);
        }
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag) {
        super.saveAdditional(tag);
        //System.out.println("Saving ItemBoxBlockEntity");
        tag.put(INVENTOR_TAG, inventory.serializeNBT());
        trySaveLootTable(tag);
    }

    @Override
    public void load(@NotNull CompoundTag tag)
    {
        super.load(tag);
        inventory.deserializeNBT(tag.getCompound(INVENTOR_TAG));
        tryLoadLootTable(tag);
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket()
    {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket packet)
    {
        if (packet.getTag() != null)
        {
            handleUpdateTag(packet.getTag());
        }
    }

    @Override
    public @NotNull CompoundTag getUpdateTag()
    {
        CompoundTag tag = super.getUpdateTag();
        tag.put(INVENTOR_TAG, inventory.serializeNBT());
        trySaveLootTable(tag);
        return tag;
    }

    @Override
    public void handleUpdateTag(CompoundTag tag) {
        super.handleUpdateTag(tag);
        inventory.deserializeNBT(tag.getCompound(INVENTOR_TAG));
        tryLoadLootTable(tag);
    }
    @FunctionalInterface
    public interface InventoryFactory<C extends IItemHandlerModifiable & INBTSerializable<CompoundTag>>
    {
        C create(InventoryBoxBlockEntity<C> entity);
    }

    protected boolean tryLoadLootTable(CompoundTag pTag) {
        if (pTag.contains(LOOT_TABLE_TAG, 8)) {
            this.lootTable = new ResourceLocation(pTag.getString(LOOT_TABLE_TAG));
            this.lootTableSeed = pTag.getLong(LOOT_TABLE_SEED_TAG);
            this.unpackLootTable();
            return true;
        } else {
            return false;
        }
    }

    protected boolean trySaveLootTable(CompoundTag pTag) {
        if (this.lootTable == null) {
            return false;
        } else {
            pTag.putString(LOOT_TABLE_TAG, this.lootTable.toString());
            if (this.lootTableSeed != 0L) {
                pTag.putLong(LOOT_TABLE_SEED_TAG, this.lootTableSeed);
            }

            return true;
        }
    }

    public void unpackLootTable() {
        if (this.lootTable == null){
            return;
        }
        if (this.level != null && this.level.isClientSide){
            return;
        }
        MinecraftServer server = (MinecraftServer) LogicalSidedProvider.WORKQUEUE.get(LogicalSide.SERVER);
        if (server == null) {
            return;
        }
        LootTable loottable = server.getLootData().getLootTable(this.lootTable);
        this.lootTable = null;
        LootParams.Builder lootparams$builder = new LootParams.Builder(server.overworld()).withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(this.worldPosition));
        lootparams$builder.withParameter(LootContextParams.BLOCK_ENTITY, null);
        loottable.fill(this, lootparams$builder.create(LootContextParamSets.CHEST), this.lootTableSeed);
        this.setChanged();
    }
    @Override
    public int getContainerSize() {
        return inventory.getSlots();
    }

    @Override
    public boolean isEmpty() {
        for (int i = 0; i < inventory.getSlots(); i++){
            if (!this.getItem(i).isEmpty()){
                return false;
            }
        }
        return true;
    }
    @Override
    public @NotNull ItemStack removeItem(int pSlot, int pAmount) {
        return this.removeItem(pSlot);
    }
    @Override
    public @NotNull ItemStack removeItemNoUpdate(int pSlot) {
        return this.removeItem(pSlot);
    }
    @Override
    public boolean stillValid(@NotNull Player pPlayer) {
        return false;
    }
    @Override
    public void clearContent() {
        for (int i = 0; i < inventory.getSlots(); i++){
            setItem(i, ItemStack.EMPTY);
        }
    }
}
