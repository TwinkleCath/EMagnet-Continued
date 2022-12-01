package net.twinklecath.emagnet.blocks.blockentities;

import me.shedaniel.autoconfig.AutoConfig;
import net.fabricmc.fabric.api.transfer.v1.context.ContainerItemContext;
import net.fabricmc.fabric.api.transfer.v1.item.InventoryStorage;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleSlotStorage;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.twinklecath.emagnet.EMagnetConfig;
import net.twinklecath.emagnet.ImplementedInventory;
import net.twinklecath.emagnet.items.MagnetItemBase;
import net.twinklecath.emagnet.registry.ModBlockEntityTypes;
import org.jetbrains.annotations.Nullable;
import team.reborn.energy.api.EnergyStorage;
import team.reborn.energy.api.EnergyStorageUtil;
import team.reborn.energy.api.base.SimpleEnergyItem;

import java.util.List;

public class MagnetJarBlockEntity extends BlockEntity implements ImplementedInventory, SidedInventory, ContainerItemContext {

    private final DefaultedList<ItemStack> items = DefaultedList.ofSize(2, ItemStack.EMPTY);

    public MagnetJarBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntityTypes.MAGNET_JAR, pos, state);
    }

    @Override
    public void readNbt(NbtCompound tag) {
        super.readNbt(tag);
        Inventories.readNbt(tag, items);
    }

    @Override
    public void writeNbt(NbtCompound tag) {
        super.writeNbt(tag);
        Inventories.writeNbt(tag, items);
    }

    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt() {
        return createNbt();
    }

    @Override
    public DefaultedList<ItemStack> getItems() {
        return items;
    }

    @Override
    public int[] getAvailableSlots(Direction side) {
        int[] result = new int[getItems().size()];
        for (int i = 0; i < result.length; i++) {
            result[i] = i;
        }

        return result;
    }

    @Override
    public boolean canInsert(int slot, ItemStack stack, @Nullable Direction dir) {
        return stack.getItem() instanceof SimpleEnergyItem && slot == 0;
    }

    @Override
    public boolean canExtract(int slot, ItemStack stack, Direction dir) {
        return slot == 1;
    }

    public static <T extends BlockEntity> void tick(World world, BlockPos pos, BlockState state, T t) {
        EMagnetConfig config = AutoConfig.getConfigHolder(EMagnetConfig.class).getConfig();
        MagnetJarBlockEntity e = (MagnetJarBlockEntity) world.getBlockEntity(pos);

        if (e != null) {
            if (e.getStack(0) != ItemStack.EMPTY) {
                MagnetItemBase item = (MagnetItemBase) e.getStack(0).getItem();

                if (item.getStoredEnergy(e.getStack(0)) != item.getEnergyCapacity(e.getStack(0))) {
                    if (!world.getEntitiesByType(EntityType.LIGHTNING_BOLT,new Box(pos.getX(),
                                    pos.getY() + 1, pos.getZ(), pos.getX() + 1, pos.getY() + 3,pos.getZ() + 1),
                            EntityPredicates.VALID_ENTITY).isEmpty()) {
                        item.setStoredEnergy(e.getStack(0), item.getEnergyCapacity(e.getStack(0)));
                        e.markDirty();
                    } else {
                        EnergyStorageUtil.move(EnergyStorage.SIDED.find(world, e.getPos().up(), Direction.DOWN),
                                EnergyStorage.ITEM.find(e.getStack(0),
                                        ContainerItemContext.ofSingleSlot(e.getMainSlot())),
                                item.getEnergyMaxInput(e.getStack(0)), null);
                        e.markDirty();
                    }
                }
                if (e.getStack(0).getItem() instanceof MagnetItemBase) {
                    if (config.blocks.disable_magnet_jar_with_redstone) {
                        if (!world.isReceivingRedstonePower(pos)) {
                            e.attractItemsAroundBlock(pos, e.getStack(0));
                        }
                    } else {
                        e.attractItemsAroundBlock(pos, e.getStack(0));
                    }
                }
            }
        }

        if (e.getStack(1).isEmpty()) {
            e.putItemAroundBlockInInventory();
        }
    }

    private void attractItemsAroundBlock(BlockPos pos, ItemStack stack) {
        int range = ((MagnetItemBase) stack.getItem()).getRange();
        double x = pos.getX();
        double y = pos.getY();
        double z = pos.getZ();

        if (world != null) {
            MagnetItemBase magnetItem = (MagnetItemBase) stack.getItem();

            List<ItemEntity> items = world.getEntitiesByType(EntityType.ITEM,
                    new Box(x - range, y - range, z - range, x + 1 + range, y + 1 + range, z + 1 + range),
                    EntityPredicates.VALID_ENTITY);
            List<ItemEntity> itemsInBlock = world.getEntitiesByType(EntityType.ITEM,
                    new Box(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1, pos.getY() + 1, pos.getZ() + 1),
                    EntityPredicates.VALID_ENTITY);

            for (ItemEntity item : items) {
                if (!itemsInBlock.contains(item)) {
                    int energyForItem = item.getStack().getCount();
                    if (magnetItem.getStoredEnergy(stack) >= energyForItem) {

                        Vec3d itemVector = new Vec3d(item.getX(), item.getY(), item.getZ());
                        Vec3d blockVector = new Vec3d(x + 0.5, y + 0.5, z + 0.5);
                        item.move(null, blockVector.subtract(itemVector).multiply(0.5));

                        EnergyStorageUtil.move(
                                EnergyStorage.ITEM.find(stack, ContainerItemContext.ofSingleSlot(getMainSlot())), null,
                                magnetItem.getEnergyMaxOutput(stack), null);
                        markDirty();
                    }
                }
            }
        }
    }

    private void putItemAroundBlockInInventory() {
        if (world != null) {
            List<ItemEntity> items = world.getEntitiesByType(EntityType.ITEM,
                    new Box(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1, pos.getY() + 1, pos.getZ() + 1),
                    EntityPredicates.VALID_ENTITY);
            if (!items.isEmpty()) {
                ItemEntity item = items.get(0);

                setStack(1, item.getStack().copy());
                item.remove(Entity.RemovalReason.DISCARDED);
                markDirty();
            }
        }
    }

    @Override
    public SingleSlotStorage<ItemVariant> getMainSlot() {
        var invWrapper = InventoryStorage.of(this, null);
        var input = invWrapper.getSlot(0);
        return input;
    }

    @Override
    public long insertOverflow(ItemVariant itemVariant, long maxAmount, TransactionContext transactionContext) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public List<SingleSlotStorage<ItemVariant>> getAdditionalSlots() {
        // TODO Auto-generated method stub
        return null;
    }
}