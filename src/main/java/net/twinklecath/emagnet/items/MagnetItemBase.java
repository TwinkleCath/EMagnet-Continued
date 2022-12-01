package net.twinklecath.emagnet.items;

import dev.emi.trinkets.api.*;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import team.reborn.energy.api.base.SimpleEnergyItem;

import java.util.List;

public class MagnetItemBase extends TrinketItem implements SimpleEnergyItem {

    private final int range;
    private final long cap;
    private final long max_io;
    public boolean active;

    public MagnetItemBase(Settings settings, int range, int cap, int max_io) {
        super(settings);
        this.range = range;
        this.cap = cap;
        this.max_io = max_io;
    }

    @Override
    public long getEnergyCapacity(ItemStack stack) {
        return cap;
    }

    @Override
    public long getEnergyMaxInput(ItemStack stack) {
        return max_io;
    }

    @Override
    public long getEnergyMaxOutput(ItemStack stack) {
        return max_io;
    }

    @Override
    public void tick(ItemStack stack, SlotReference slot, LivingEntity entity) {
        super.tick(stack, slot, entity);
        if (getStoredEnergy(stack) > getEnergyCapacity(stack)) {
            setStoredEnergy(stack, getEnergyCapacity(stack));
        }
        attractItemsToPlayer(entity, stack);
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        super.inventoryTick(stack, world, entity, slot, selected);
        var optional = TrinketsApi.getTrinketComponent((PlayerEntity) entity);
        TrinketComponent comp = optional.get();

        if (getStoredEnergy(stack) > getEnergyCapacity(stack)) {
            setStoredEnergy(stack, getEnergyCapacity(stack));
        }

        if (comp.isEquipped(stack.getItem()))
            attractItemsToPlayer(entity, stack);


        for (ItemStack s : entity.getHandItems()) {
            //EMagnetConfig config = AutoConfig.getConfigHolder(EMagnetConfig.class).getConfig();
            if (s.getItem() instanceof MagnetItemBase) {
            attractItemsToPlayer(entity, stack);
            }
        }
    }

    private void attractItemsToPlayer(Entity entity, ItemStack stack) {
        double x = entity.getX();
        double y = entity.getY();
        double z = entity.getZ();

        List<ItemEntity> items = entity.getEntityWorld().getEntitiesByType(EntityType.ITEM,
                new Box(x - range, y - range, z - range, x + range, y + range, z + range),
                EntityPredicates.VALID_ENTITY);

        for (ItemEntity item : items) {
            int ForItem = item.getStack().getCount();
            if (getStoredEnergy(stack) >= ForItem) {
                item.setPickupDelay(0);

                Vec3d itemVector = new Vec3d(item.getX(), item.getY(), item.getZ());
                Vec3d playerVector = new Vec3d(x, y + 0.03, z);
                item.move(null, playerVector.subtract(itemVector).multiply(0.5));

                setStoredEnergy(stack, getStoredEnergy(stack) - ForItem);
            }
        }
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        TrinketItem.equipItem(user, user.getStackInHand(hand));
        return TypedActionResult.success(user.getStackInHand(hand));
    }

    public int getRange() {
        return range;
    }

    @Override
    public boolean isItemBarVisible(ItemStack stack) {
        return true;
    }

    @Override
    public int getItemBarColor(ItemStack stack) {
        return 0xFF800600;
    }

    @Override
    public int getItemBarStep(ItemStack stack) {
        return (int) ((float) getStoredEnergy(stack) / getEnergyCapacity(stack) * 13);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        super.appendTooltip(stack, world, tooltip, context);
        tooltip.add(Text.literal(String.format("%s/%s EU", getStoredEnergy(stack), getEnergyCapacity(stack))));
    }
}
