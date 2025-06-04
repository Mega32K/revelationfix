package com.mega.revelationfix.common.block.blockentity;

import com.Polarice3.Goety.api.items.magic.IWand;
import com.Polarice3.Goety.api.magic.IBlockSpell;
import com.Polarice3.Goety.api.magic.IBreathingSpell;
import com.Polarice3.Goety.api.magic.IChargingSpell;
import com.Polarice3.Goety.api.magic.ISpell;
import com.Polarice3.Goety.common.blocks.entities.CursedCageBlockEntity;
import com.Polarice3.Goety.common.events.GoetyEventFactory;
import com.Polarice3.Goety.common.items.ModItems;
import com.Polarice3.Goety.common.items.SoulTransferItem;
import com.Polarice3.Goety.common.items.magic.DarkWand;
import com.Polarice3.Goety.common.items.magic.TotemOfSouls;
import com.Polarice3.Goety.common.magic.SpellStat;
import com.Polarice3.Goety.common.magic.SummonSpell;
import com.Polarice3.Goety.common.magic.spells.SonicBoomSpell;
import com.Polarice3.Goety.config.MobsConfig;
import com.Polarice3.Goety.utils.SEHelper;
import com.mega.revelationfix.common.block.RuneReactorBlock;
import com.mega.revelationfix.common.block.RunestoneEngravedTableBlock;
import com.mega.revelationfix.common.config.BlockConfig;
import com.mega.revelationfix.common.entity.BlockShakingEntity;
import com.mega.revelationfix.common.entity.FakeSpellerEntity;
import com.mega.revelationfix.common.init.ModBlocks;
import com.mega.revelationfix.common.init.RunestoneRitualInit;
import com.mega.revelationfix.safe.entity.EntityExpandedContext;
import com.mega.revelationfix.util.entity.EntityFinder;
import com.mega.revelationfix.util.entity.RotationUtils;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.gossip.GossipType;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

public class RuneReactorBlockEntity extends BlockEntity {
    public final int randId = (int) (Math.random() * Util.getMillis());
    private final Object2IntOpenHashMap<Item> structureCodes = new Object2IntOpenHashMap<>();
    public int tickCount = 0;
    private ItemStack insertItem = ItemStack.EMPTY;
    private final BlockPos[] runestonePoses = new BlockPos[] {new BlockPos(0,0,0),new BlockPos(0,0,0),new BlockPos(0,0,0),new BlockPos(0,0,0)};
    //index -> runestone index
    private final BlockPos[] bestSort = new BlockPos[] {new BlockPos(0,0,0),new BlockPos(0,0,0),new BlockPos(0,0,0),new BlockPos(0,0,0)};
    private boolean needSyncItem;
    private boolean needSyncRunestones;
    private boolean needSyncOwner;
    private boolean needSyncSpeller;
    @Nullable
    private Player ownerEntity;
    @Nullable
    private UUID ownerID;
    @Nullable
    private FakeSpellerEntity spellerEntity;
    @Nullable
    private UUID spellerID;
    private int spellUseTimeRemaining = 72000;
    private boolean using;
    private @Nullable LivingEntity currentSpellTarget;
    public RuneReactorBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(ModBlocks.RUNE_REACTOR_ENTITY.get(), blockPos, blockState);
        structureCodes.put(ModItems.ANIMATION_CORE.get(), 0);
        structureCodes.put(ModItems.HUNGER_CORE.get(), 0);
        structureCodes.put(ModItems.MYSTIC_CORE.get(), 0);
        structureCodes.put(ModItems.WIND_CORE.get(), 0);
    }
    public ItemStack getInsertItem() {
        return insertItem;
    }
    public void setInsertItem(ItemStack insertItem) {
        this.insertItem = insertItem;
        this.needSyncItem = true;
        setChanged();
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }
    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        handleUpdateTag(pkt.getTag());
    }
    public int toSimpleStructureCode() {
        return structureCodes.getInt(ModItems.ANIMATION_CORE.get()) * 1000 + structureCodes.getInt(ModItems.HUNGER_CORE.get()) * 100 + structureCodes.getInt(ModItems.MYSTIC_CORE.get()) * 10 + structureCodes.getInt(ModItems.WIND_CORE.get());
    }
    @Override
    public @NotNull CompoundTag getUpdateTag() {
        CompoundTag tag = new CompoundTag();
        if (needSyncItem) {
            if (!this.insertItem.isEmpty()) {
                tag.put("InsertItem", this.insertItem.save(new CompoundTag()));
            } else {
                tag.put("InsertItem", new CompoundTag());
                tag.put("RunestonesPoses", new CompoundTag());
            }
        }
        if (needSyncRunestones) {
            {
                ListTag listTag = new ListTag();
                for (BlockPos pos : runestonePoses) {
                    CompoundTag compoundTag1 = new CompoundTag();
                    compoundTag1.putInt("X", pos.getX());
                    compoundTag1.putInt("Y", pos.getY());
                    compoundTag1.putInt("Z", pos.getZ());
                    listTag.add(compoundTag1);
                }
                tag.put("RunestonesPoses", listTag);
            }
            {
                ListTag listTag = new ListTag();
                for (BlockPos pos : bestSort) {
                    CompoundTag compoundTag1 = new CompoundTag();
                    compoundTag1.putInt("X", pos.getX());
                    compoundTag1.putInt("Y", pos.getY());
                    compoundTag1.putInt("Z", pos.getZ());
                    listTag.add(compoundTag1);
                }
                tag.put("RunestonesSort", listTag);
            }
        }
        if (needSyncOwner) {
            if (this.ownerID != null)
                tag.putUUID("Owner", this.ownerID);
        }
        if (needSyncSpeller) {
            if (this.spellerID != null)
                tag.putUUID("Speller", this.spellerID);
        }
        return tag;
    }

    @Override
    public void handleUpdateTag(CompoundTag tag) {
        if (tag != null)
            load0(tag);
    }

    @Override
    public void load(@NotNull CompoundTag compoundTag) {
        load0(compoundTag);
        {
            structureCodes.put(ModItems.ANIMATION_CORE.get(), compoundTag.getByte("StructureCode1"));
            structureCodes.put(ModItems.HUNGER_CORE.get(), compoundTag.getByte("StructureCode2"));
            structureCodes.put(ModItems.MYSTIC_CORE.get(), compoundTag.getByte("StructureCode3"));
            structureCodes.put(ModItems.WIND_CORE.get(), compoundTag.getByte("StructureCode4"));
        }
        super.load(compoundTag);
    }
    protected void load0(@NotNull CompoundTag compoundTag) {
        if (compoundTag.contains("InsertItem", 10)) {
            this.insertItem = ItemStack.of(compoundTag.getCompound("InsertItem"));
        }
        {
            ListTag listTag = compoundTag.getList("RunestonesPoses", 10);
            if (!listTag.isEmpty()) {
                for (int i=0;i<4;i++) {
                    try {
                        CompoundTag compoundTag1 = listTag.getCompound(i);
                        runestonePoses[i] = new BlockPos(compoundTag1.getInt("X"), compoundTag1.getInt("Y"), compoundTag1.getInt("Z"));
                    } catch (Throwable throwable) {
                        throwable.printStackTrace();
                    }
                }
            }
        }
        {
            ListTag listTag = compoundTag.getList("RunestonesSort", 10);
            if (!listTag.isEmpty()) {
                for (int i=0;i<4;i++) {
                    try {
                        CompoundTag compoundTag1 = listTag.getCompound(i);
                        bestSort[i] = new BlockPos(compoundTag1.getInt("X"), compoundTag1.getInt("Y"), compoundTag1.getInt("Z"));
                    } catch (Throwable throwable) {
                        throwable.printStackTrace();
                    }
                }
            }
        }

        if (compoundTag.hasUUID("Owner")) {
            this.ownerID = compoundTag.getUUID("Owner");
        } else this.ownerID = null;
        if (compoundTag.hasUUID("Speller")) {
            this.spellerID = compoundTag.getUUID("Speller");
        } else this.spellerID = null;
    }

    public Object2IntOpenHashMap<Item> getStructureCodes() {
        return structureCodes;
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag compoundTag) {
        if (!this.insertItem.isEmpty()) {
            compoundTag.put("InsertItem", this.insertItem.save(compoundTag.getCompound("InsertItem")));
            {
                ListTag listTag = new ListTag();
                for (BlockPos pos : runestonePoses) {
                    CompoundTag compoundTag1 = new CompoundTag();
                    compoundTag1.putInt("X", pos.getX());
                    compoundTag1.putInt("Y", pos.getY());
                    compoundTag1.putInt("Z", pos.getZ());
                    listTag.add(compoundTag1);
                }
                compoundTag.put("RunestonesPoses", listTag);
            }
            {
                ListTag listTag = new ListTag();
                for (BlockPos pos : bestSort) {
                    CompoundTag compoundTag1 = new CompoundTag();
                    compoundTag1.putInt("X", pos.getX());
                    compoundTag1.putInt("Y", pos.getY());
                    compoundTag1.putInt("Z", pos.getZ());
                    listTag.add(compoundTag1);
                }
                compoundTag.put("RunestonesSort", listTag);
            }
            {
                compoundTag.putByte("StructureCode1", (byte) structureCodes.getInt(ModItems.ANIMATION_CORE.get()));
                compoundTag.putByte("StructureCode2", (byte) structureCodes.getInt(ModItems.HUNGER_CORE.get()));
                compoundTag.putByte("StructureCode3", (byte) structureCodes.getInt(ModItems.MYSTIC_CORE.get()));
                compoundTag.putByte("StructureCode4", (byte) structureCodes.getInt(ModItems.WIND_CORE.get()));
            }
        } else {
            compoundTag.put("RunestonesPoses", new ListTag());
            compoundTag.put("RunestonesSort", new ListTag());
        }
        if (ownerID != null)
            compoundTag.putUUID("Owner", ownerID);
        if (spellerID != null)
            compoundTag.putUUID("Speller", spellerID);
        super.saveAdditional(compoundTag);
    }

    public Player getOwner() {
        if (this.level != null) {
            if (this.ownerEntity == null && ownerID != null) {
                this.ownerEntity = this.level.getPlayerByUUID(ownerID);
            }
        }
        return ownerEntity;
    }

    public @Nullable UUID getOwnerID() {
        return ownerID;
    }

    public void setOwner(Player ownerEntity) {
        if (this.ownerEntity != ownerEntity) {
            this.ownerEntity = ownerEntity;
            this.ownerID = ownerEntity == null ? null : ownerEntity.getUUID();
            setChanged();
            needSyncOwner = true;
        }
    }
    public void setOwner(UUID ownerID) {
        if (this.ownerID != ownerID) {
            this.ownerID = ownerID;
            if (level != null && ownerID != null)
                this.ownerEntity = level.getPlayerByUUID(ownerID);
            if (ownerID == null)
                this.ownerEntity = null;
            setChanged();
            needSyncOwner = true;
        }
    }
    public FakeSpellerEntity getSpeller() {
        if (this.level != null) {
            if (this.spellerEntity == null && spellerID != null) {
                Optional<FakeSpellerEntity> o = this.level.getEntitiesOfClass(FakeSpellerEntity.class, new AABB(this.getBlockPos()).inflate(3D)).stream().filter(e ->e.getUUID().equals(spellerID)).findFirst();
                if (o != null && o.isPresent())
                    this.spellerEntity = o.get();
            }
        }
        return spellerEntity;
    }

    public @Nullable UUID getSpellerID() {
        return spellerID;
    }

    public void setSpeller(FakeSpellerEntity spellerEntity) {
        if (this.spellerEntity != spellerEntity) {
            this.spellerEntity = spellerEntity;
            this.spellerID = spellerEntity == null ? null : spellerEntity.getUUID();
            setChanged();
            needSyncSpeller = true;
        }
    }
    public void setSpeller(UUID spellerID) {
        if (this.spellerID != spellerID) {
            this.spellerID = spellerID;
            if (level != null && spellerID != null)
                this.spellerEntity = this.level.getEntitiesOfClass(FakeSpellerEntity.class, new AABB(this.getBlockPos()).inflate(3D)).stream().filter(e ->e.getUUID().equals(spellerID)).findFirst().orElseGet(null);
            if (spellerID == null)
                this.spellerEntity = null;
            setChanged();
            needSyncSpeller = true;
        }
    }
    public BlockPos[] getRunestonePoses() {
        return Arrays.copyOf(this.runestonePoses, 4);
    }
    @SuppressWarnings("ManualArrayCopy")
    public void modifyRunestonePoses(BlockPos[] runestonePoses) {
        for (int i=0;i<runestonePoses.length;i++) {
            this.runestonePoses[i] = runestonePoses[i];
        }
        sort();
        this.needSyncRunestones = true;
        setChanged();
    }

    public BlockPos[] getBestSort() {
        return Arrays.copyOf(bestSort, 4);
    }

    public void sort() {
        BlockPos a = runestonePoses[0];
        BlockPos b = runestonePoses[1];
        BlockPos c = runestonePoses[2];
        BlockPos d = runestonePoses[3];
        int[] allX = new int[] {a.getX(), b.getX(), c.getX(), d.getX()};
        int[] allZ = new int[] {a.getZ(), b.getZ(), c.getZ(), d.getZ()};
        Arrays.sort(allX);
        Arrays.sort(allZ);
        bestSort[0] = new BlockPos(allX[0], getBlockPos().getY(), allZ[0]);
        bestSort[1] = new BlockPos(allX[3], getBlockPos().getY(), allZ[0]);
        bestSort[2] = new BlockPos(allX[3], getBlockPos().getY(), allZ[3]);
        bestSort[3] = new BlockPos(allX[0], getBlockPos().getY(), allZ[3]);
        setChanged();
    }
    public AABB getRitualRange() {
        return new AABB(bestSort[0].getX(), bestSort[0].getY()-9, bestSort[0].getZ(), bestSort[2].getX(), bestSort[2].getY()+9, bestSort[2].getZ());
    }
    public AABB getRitualRangeOnePiece() {
        return new AABB(bestSort[0].getX(), bestSort[0].getY(), bestSort[0].getZ(), bestSort[2].getX(), bestSort[2].getY(), bestSort[2].getZ());
    }
    public int getRitualDelay() {
        ItemStack stack = getInsertItem();
        if (stack.getItem() instanceof TotemOfSouls souls) {
            return souls.getMaxSouls() > 100 ? BlockConfig.runeReactor_soulCoreDelay : BlockConfig.runeReactor_rootCoreDelay;
        } else if (stack.getItem() instanceof SoulTransferItem transferItem) {
            return BlockConfig.runeReactor_transferCoreDelay;
        }
        return 2;
    }
    public int soulCost_ns() {
        return structureCodes.getInt(ModItems.ANIMATION_CORE.get()) * BlockConfig.runeReactor_AnimationCoreCost
                + structureCodes.getInt(ModItems.HUNGER_CORE.get()) * BlockConfig.runeReactor_HungerCoreCost
                + structureCodes.getInt(ModItems.MYSTIC_CORE.get()) * BlockConfig.runeReactor_MysticCoreCost
                + structureCodes.getInt(ModItems.WIND_CORE.get()) * BlockConfig.runeReactor_WindCoreCost;
    }
    public int soulCost_as() {
        return (int) ((structureCodes.getInt(ModItems.ANIMATION_CORE.get()) * BlockConfig.runeReactor_AnimationCoreCost_Focus
                + structureCodes.getInt(ModItems.HUNGER_CORE.get()) * BlockConfig.runeReactor_HungerCoreCost_Focus
                + structureCodes.getInt(ModItems.MYSTIC_CORE.get()) * BlockConfig.runeReactor_MysticCoreCost_Focus
                + structureCodes.getInt(ModItems.WIND_CORE.get()) * BlockConfig.runeReactor_WindCoreCost_Focus) * BlockConfig.runeReactor_spellingCostMultiplier);
    }
    public static void serverTick(Level level, BlockPos reactorPos, BlockState reactorState, RuneReactorBlockEntity reactorBlockEntity) {
        reactorBlockEntity.tickCount++;
        ItemStack insertItem = reactorBlockEntity.getInsertItem();

        if (reactorBlockEntity.tickCount == 1) {
            reactorBlockEntity.needSyncItem = true;
            reactorBlockEntity.needSyncRunestones = true;
            reactorBlockEntity.needSyncOwner = true;
            reactorBlockEntity.needSyncSpeller = true;
            level.sendBlockUpdated(reactorPos, level.getBlockState(reactorPos), level.getBlockState(reactorPos), 2);
        } else if (RuneReactorBlock.canUseStructure(level, reactorPos, reactorState)) {
            if (reactorBlockEntity.tickCount % 20 == 0) {
                reactorBlockEntity.checkStructure(level, reactorPos, reactorState);
                level.sendBlockUpdated(reactorPos, level.getBlockState(reactorPos), level.getBlockState(reactorPos), 2);
                if (RuneReactorBlock.isKindOfCore(insertItem)) {
                    if ( RuneReactorBlock.isRitualStructureCore(insertItem)) {
                        if (level.getBlockEntity(reactorPos.above(-1)) instanceof CursedCageBlockEntity cageBlockEntity && cageBlockEntity.getSouls() > 0) {
                            cageBlockEntity.decreaseSouls(Math.min(cageBlockEntity.getSouls(), reactorBlockEntity.soulCost_ns()));
                        } else if (level.getBlockEntity(reactorPos.above(-2)) instanceof CursedCageBlockEntity cageBlockEntity && cageBlockEntity.getSouls() > 0) {
                            cageBlockEntity.decreaseSouls(Math.min(cageBlockEntity.getSouls(), reactorBlockEntity.soulCost_ns()));
                        }
                    }
                    int count = 0;
                    for (BlockShakingEntity blockShakingEntity : level.getEntitiesOfClass(BlockShakingEntity.class, new AABB(reactorPos.above(2)))) {
                        if (blockShakingEntity.getBlockState().is(ModBlocks.RUNE_REACTOR.get())) {
                            count++;
                        }
                    }
                    if (count == 0) {
                        BlockShakingEntity blockShakingEntity = new BlockShakingEntity(level, reactorPos.getX(), reactorPos.getY() + 2, reactorPos.getZ(), reactorState, 160);
                        level.addFreshEntity(blockShakingEntity);
                    }
                }
            }
            if (RuneReactorBlock.isKindOfCore(insertItem)) {
                ISpell spell;
                if ((spell = RuneReactorBlock.getSpell(insertItem)) != null) {
                    reactorBlockEntity.runAutoSpellingEvents(spell, level, reactorPos, reactorState);
                }  else reactorBlockEntity.runNormalStructureEvents(level, reactorPos, reactorState);
            } else {
                reactorBlockEntity.using = false;
                reactorBlockEntity.spellUseTimeRemaining = -1;

            }
        }
    }

    /**
     * 每server tick运行
     */
    public void runAutoSpellingEvents(ISpell spell, Level level, BlockPos reactorPos, BlockState reactorState) {
        Player player = this.getOwner();
        FakeSpellerEntity speller = this.getSpeller();
        ItemStack wand = this.getInsertItem();
        if (speller == null) {
            speller = new FakeSpellerEntity(level, getInsertItem(), reactorPos.above(1));
            speller.setWand(getInsertItem().copy());
            speller.setTrueOwner(player);
            this.setSpeller(speller);
            level.addFreshEntity(speller);
            return;
        }
        if (level instanceof ServerLevel serverLevel && player != null && player.isAlive() && wand.getItem() instanceof DarkWand wandItem) {
            wand.inventoryTick(level, player, 0, true);
            {
                if (spell instanceof IBlockSpell blockSpells) {
                    if (this.tickCount % 10 == 0) {
                        ProfilerFiller profilerfiller = serverLevel.getProfiler();
                        AABB aabb = this.getRitualRange();
                        profilerfiller.push("rune_reactor_block_spells");
                        for (int i = (int) aabb.minX; i <= (int) aabb.maxX; i++) {
                            for (int j = (int) aabb.minZ; j <= (int) aabb.maxZ; j++) {
                                for (int k0 = (int) aabb.minY + 6; k0 <= (int) aabb.maxY - 6; k0++) {
                                    BlockPos pos = new BlockPos(i, k0, j);
                                    if (blockSpells.rightBlock(serverLevel, speller, pos, Direction.DOWN)) {
                                        if (canCastTouch(wandItem, spell, wand, level, speller, player)) {

                                            blockSpells.blockResult(serverLevel, speller, pos, Direction.DOWN);
                                        }
                                    }
                                }

                            }
                        }
                        profilerfiller.pop();
                    }
                } else if (spell != null && !(spell instanceof SummonSpell)) {
                    //speller.
                    simulateOnUseTick(wandItem, spell, level, speller, insertItem);
                }
            }


        }
    }
    public SpellStat modifySpellStats(SpellStat spellStat) {
        int animationCoreCounts = this.structureCodes.getInt(ModItems.ANIMATION_CORE.get());
        int hungerCoreCounts = this.structureCodes.getInt(ModItems.ANIMATION_CORE.get());
        int mysticCoreCounts = this.structureCodes.getInt(ModItems.ANIMATION_CORE.get());
        int windCoreCounts = this.structureCodes.getInt(ModItems.ANIMATION_CORE.get());
        spellStat.setPotency((int) (spellStat.getPotency() * (1+animationCoreCounts / 4F)));
        spellStat.setDuration((int) (spellStat.getDuration() * (1+hungerCoreCounts / 4F)));
        spellStat.setRadius((int) (spellStat.getRadius() * (1+mysticCoreCounts / 4F)));
        spellStat.setVelocity((int) (spellStat.getVelocity() * (1+windCoreCounts / 4F)));
        return spellStat;
    }
    public int spellerSoulUse(DarkWand wand, LivingEntity speller, ItemStack stack) {
        return wand.SoulUse(speller, stack) + soulCost_as();
    }
    public boolean canCastTouch(DarkWand wandB, ISpell spell, ItemStack stack, Level worldIn, LivingEntity speller, Player player) {
        if (!worldIn.isClientSide && spell != null && !this.cannotCast(wandB, speller, stack)) {
            if (player.isCreative()) {

                SEHelper.addCooldown(player, IWand.getFocus(stack).getItem(), spell.spellCooldown());
                return stack.getTag() != null;
            }

            if (this.getSoulsAmount(player, this.spellerSoulUse(wandB, player, stack)) && stack.getTag() != null) {
                SEHelper.decreaseSouls(player, this.spellerSoulUse(wandB, player, stack));
                SEHelper.addCooldown(player, IWand.getFocus(stack).getItem(), spell.spellCooldown());
                SEHelper.sendSEUpdatePacket(player);
                return true;
            }
        }

        return false;
    }
    public int getSpellUseDuration(DarkWand wand, ItemStack stack) {
        return stack.getTag() != null ? stack.getTag().getInt("Cast Time") : wand.CastDuration(stack);
    }
    public boolean cannotCast(DarkWand wandB, LivingEntity livingEntity, ItemStack stack) {
        boolean flag = false;
        Level var5 = livingEntity.level;
        if (var5 instanceof ServerLevel serverLevel) {
            if (wandB.getSpell(stack) != null && !wandB.getSpell(stack).conditionsMet(serverLevel, livingEntity)) {
                flag = true;
            }
        }

        return this.isOnCooldown(wandB, livingEntity, stack) || flag;
    }

    private boolean isOnCooldown(DarkWand wandB, LivingEntity livingEntity, ItemStack stack) {
        if (livingEntity instanceof FakeSpellerEntity fakeSpeller && fakeSpeller.getOwner() instanceof Player player) {
            if (RuneReactorBlock.findFocus(stack) != null) {
                Item item = RuneReactorBlock.findFocus(stack).getItem();
                return SEHelper.getFocusCoolDown(player).isOnCooldown(item);
            }
        }

        return wandB.isOnCooldown(livingEntity, stack);
    }
    public boolean getSoulsAmount(Player player, int souls) {
        return SEHelper.getSoulsAmount(player, souls);
    }
    public void simulateOnUseTick(DarkWand wandB, @NotNull ISpell spellB, Level worldIn, @NotNull LivingEntity livingEntityIn, ItemStack stack) {
        Player playerOwner = getOwner();
        if (currentSpellTarget == null || (currentSpellTarget.isDeadOrDying() || !this.getRitualRange().contains(currentSpellTarget.position())) || currentSpellTarget.isRemoved()) {

            BlockPos blockPos = this.getBlockPos();
            LivingEntity nearestTarget = EntityFinder.getNearestEntity(level, LivingEntity.class, EntityExpandedContext.NO_GODS, EntityFinder.STRICT_NOT_ALLIED_NE, playerOwner, blockPos.getX(), blockPos.getY(), blockPos.getZ(), this.getRitualRange());

            if (nearestTarget != currentSpellTarget) {
                this.currentSpellTarget = nearestTarget;
            }
        }
        if (this.currentSpellTarget != null) {
            RotationUtils.rotationAtoB(livingEntityIn, this.currentSpellTarget.position());
            if (livingEntityIn instanceof Mob mob)
                mob.setTarget(this.currentSpellTarget);
        }
        if (this.cannotCast(wandB, livingEntityIn, stack)) {
            using = false;
        }
        if (wandB.isNotInstant(spellB)) {
            if ((this.getSoulsAmount(getOwner(), this.spellerSoulUse(wandB, getOwner(), insertItem)) || getOwner().getAbilities().instabuild) && !worldIn.isClientSide) {
                using = true;
            }
        } else if (wandB.notTouch(spellB)) {
            if (!this.isOnCooldown(wandB, livingEntityIn, stack) && (currentSpellTarget != null && !currentSpellTarget.isRemoved()))
                this.MagicResults(wandB, spellB, stack, worldIn, livingEntityIn, playerOwner);
        }
        if (!using || spellUseTimeRemaining == -1) {
            spellUseTimeRemaining = getSpellUseDuration(wandB, stack);
        } else {
            spellUseTimeRemaining--;

            if (worldIn instanceof ServerLevel serverLevel && !this.cannotCast(wandB, livingEntityIn, stack) ) {
                if (spellUseTimeRemaining <= 0) {
                    {
                        if ((!(spellB instanceof IChargingSpell) || wandB.isNotInstant(spellB) || wandB.notTouch(spellB)) && !this.cannotCast(wandB, livingEntityIn, stack)) {
                            this.MagicResults(wandB, spellB, stack, worldIn, livingEntityIn, playerOwner);
                        }

                        if (stack.getTag() != null) {
                            if (stack.getTag().getInt("Cool") > 0) {
                                stack.getTag().putInt("Cool", 0);
                            }

                            if (stack.getTag().getInt("Shots") > 0) {
                                stack.getTag().putInt("Shots", 0);
                            }
                        }
                    }
                } else {
                    {
                        spellB.stopSpell(serverLevel, livingEntityIn, stack, spellUseTimeRemaining);
                        if (livingEntityIn instanceof FakeSpellerEntity fakeSpellerEntity) {
                            if (spellB instanceof IChargingSpell spell) {
                                if (spell.shotsNumber(playerOwner, stack) > 0) {
                                    if (wandB.ShotsFired(stack) > 0) {
                                        float coolPercent = (float) wandB.ShotsFired(stack) / (float) spell.shotsNumber(playerOwner, stack);
                                        wandB.setShots(stack, 0);
                                        SEHelper.addCooldown(playerOwner, IWand.getFocus(stack).getItem(), Mth.floor((float) spell.spellCooldown() * coolPercent));
                                    }
                                } else {
                                    SEHelper.addCooldown(playerOwner, IWand.getFocus(stack).getItem(), Mth.floor((float) spell.spellCooldown()));
                                }
                            }
                        }
                    }
                }
                using = false;
            } else {
                int CastTime = stack.getUseDuration() - spellUseTimeRemaining;

                if (/*livingEntityIn.getUseItem() == stack && */ wandB.isNotInstant(spellB)) {
                    SoundEvent soundevent = wandB.CastingSound(stack, livingEntityIn);
                    ServerLevel serverLevel;

                    if (CastTime == 2 && soundevent != null) {
                        if (worldIn instanceof ServerLevel) {
                            serverLevel = (ServerLevel) worldIn;
                            spellB.startSpell(serverLevel, livingEntityIn, stack);
                        }

                        worldIn.playSound(null, livingEntityIn.getX(), livingEntityIn.getY(), livingEntityIn.getZ(), soundevent, SoundSource.PLAYERS, wandB.castingVolume(stack), wandB.castingPitch(stack));
                    }

                    if (worldIn instanceof ServerLevel) {
                        serverLevel = (ServerLevel) worldIn;
                        spellB.useSpell(serverLevel, livingEntityIn, stack, CastTime);
                    }

                    ISpell var8;
                    IChargingSpell spell;
                    label59:
                    {
                        var8 = spellB;
                        if (var8 instanceof IChargingSpell) {
                            spell = (IChargingSpell) var8;
                            if (spell.castUp(livingEntityIn, stack) > 0) {
                                wandB.useParticles(worldIn, livingEntityIn, stack, spellB);
                                break label59;
                            }
                        }

                        if (!(wandB.getSpell(stack) instanceof IChargingSpell)) {
                            wandB.useParticles(worldIn, livingEntityIn, stack, spellB);
                        }
                    }

                    var8 = spellB;

                    if (var8 instanceof IChargingSpell) {
                        spell = (IChargingSpell) var8;

                        if (stack.getTag() != null && (CastTime >= spell.castUp(livingEntityIn, stack) || spell.castUp(livingEntityIn, stack) <= 0)) {
                            stack.getTag().putInt("Cool", stack.getTag().getInt("Cool") + 1);
                            if (stack.getTag().getInt("Cool") >= wandB.Cooldown(stack)) {
                                stack.getTag().putInt("Cool", 0);
                                if (spell.shotsNumber(livingEntityIn, stack) > 0) {
                                    wandB.increaseShots(stack);
                                }

                                this.MagicResults(wandB, spellB, stack, worldIn, playerOwner, playerOwner);
                            }
                        }

                        if (!this.getSoulsAmount(playerOwner, spellB.soulCost(playerOwner)) && !playerOwner.isCreative()) {
                            using = false;
                        }
                    }
                }

            }
        }
    }
    public void MagicResults(DarkWand wandB, ISpell spellB, ItemStack stack, Level worldIn, LivingEntity caster, Player playerOwner) {
        if (spellB != null && caster instanceof FakeSpellerEntity) {
            ISpell spell = GoetyEventFactory.onCastSpell(caster, spellB);
            if (spell != null) {
                if (!worldIn.isClientSide) {
                    ServerLevel serverWorld = (ServerLevel)worldIn;
                    boolean spent;
                    IChargingSpell spell1;
                    if (playerOwner.isCreative()) {
                        if (stack.getTag() != null) {
                            spell.SpellResult(serverWorld, caster, stack, spell.defaultStats());
                            spent = false;
                            if (spell instanceof IChargingSpell) {
                                spell1 = (IChargingSpell)spell;
                                if (spell1.shotsNumber(playerOwner, stack) > 0 && wandB.ShotsFired(stack) >= spell1.shotsNumber(playerOwner, stack)) {
                                    spent = true;
                                }
                            } else {
                                spent = true;
                            }

                            if (spent) {
                                wandB.setShots(stack, 0);
                                SEHelper.addCooldown(playerOwner, IWand.getFocus(stack).getItem(), spell.spellCooldown());
                            }
                        }
                    } else if (this.getSoulsAmount(playerOwner, this.spellerSoulUse(wandB, playerOwner, stack))) {
                        spent = true;
                        if (spell instanceof IChargingSpell) {
                            spell1 = (IChargingSpell)spell;
                            if (spell1.everCharge() && stack.getTag() != null) {
                                stack.getTag().putInt("Seconds", stack.getTag().getInt("Seconds") + 1);
                                if (stack.getTag().getInt("Seconds") != 20) {
                                    spent = false;
                                } else {
                                    stack.getTag().putInt("Seconds", 0);
                                }
                            }
                        }

                        if (spent) {
                            SEHelper.decreaseSouls(playerOwner, this.spellerSoulUse(wandB, playerOwner, stack));
                            SEHelper.sendSEUpdatePacket(playerOwner);
                            if (MobsConfig.VillagerHateSpells.get() > 0) {

                                for (Villager villager : caster.level.getEntitiesOfClass(Villager.class, caster.getBoundingBox().inflate(16.0))) {
                                    if (villager.hasLineOfSight(caster)) {
                                        villager.getGossips().add(caster.getUUID(), GossipType.MINOR_NEGATIVE, MobsConfig.VillagerHateSpells.get());
                                    }
                                }
                            }
                        }

                        if (stack.getTag() != null) {
                            spell.SpellResult(serverWorld, caster, stack, spell.defaultStats());
                            boolean flag = false;
                            if (spell instanceof IChargingSpell chargingSpell) {
                                if (chargingSpell.shotsNumber(playerOwner, stack) > 0 && wandB.ShotsFired(stack) >= chargingSpell.shotsNumber(playerOwner, stack)) {
                                    flag = true;
                                }
                            } else {
                                flag = true;
                            }

                            if (flag) {
                                wandB.setShots(stack, 0);
                                SEHelper.addCooldown(playerOwner, IWand.getFocus(stack).getItem(), spell.spellCooldown());
                            }
                        }
                    } else {
                        worldIn.playSound(null, caster.getX(), caster.getY(), caster.getZ(), SoundEvents.FIRE_EXTINGUISH, SoundSource.NEUTRAL, 1.0F, 1.0F);
                    }
                }

                if (worldIn.isClientSide) {
                    IBreathingSpell breathingSpells;
                    if (playerOwner.isCreative()) {
                        if (spell instanceof IBreathingSpell) {
                            breathingSpells = (IBreathingSpell)spell;
                            breathingSpells.showWandBreath(caster);
                        }
                    } else if (this.getSoulsAmount(playerOwner, this.spellerSoulUse(wandB, playerOwner, stack))) {
                        if (spell instanceof IBreathingSpell) {
                            breathingSpells = (IBreathingSpell)spell;
                            breathingSpells.showWandBreath(caster);
                        }
                    } else {
                        wandB.failParticles(worldIn, caster);
                    }
                }
            } else {
                wandB.failParticles(worldIn, caster);
                worldIn.playSound(null, caster.getX(), caster.getY(), caster.getZ(), SoundEvents.FIRE_EXTINGUISH, SoundSource.NEUTRAL, 1.0F, 1.0F);
            }
        } else {
            wandB.failParticles(worldIn, caster);
            worldIn.playSound(null, caster.getX(), caster.getY(), caster.getZ(), SoundEvents.FIRE_EXTINGUISH, SoundSource.NEUTRAL, 1.0F, 1.0F);
        }

    }
    /**
     * 每server tick运行
     */
    public void runNormalStructureEvents(Level level, BlockPos reactorPos, BlockState reactorState) {
        int simpleCode = this.toSimpleStructureCode();
        if (RuneReactorBlock.isRitualStructureCore(insertItem)) {
            if (this.tickCount % this.getRitualDelay() == 0) {
                if (RunestoneRitualInit.registries.containsKey(simpleCode) && level instanceof ServerLevel serverLevel)
                    RunestoneRitualInit.registries.get(simpleCode).run(serverLevel, reactorPos, reactorState, this);
            }
        }
    }
    public void checkStructure(Level level, BlockPos reactorPos, BlockState reactorState) {
        boolean shouldReCheck = false;
        AABB tempStructureAABB = new AABB(reactorPos).inflate(RuneReactorBlock.RANGE);
        structureCodes.put(ModItems.ANIMATION_CORE.get(), 0);
        structureCodes.put(ModItems.HUNGER_CORE.get(), 0);
        structureCodes.put(ModItems.MYSTIC_CORE.get(), 0);
        structureCodes.put(ModItems.WIND_CORE.get(), 0);
        for (BlockPos pos : this.runestonePoses) {
            if (!tempStructureAABB.contains(pos.getCenter()) || (!(level.getBlockState(pos).getBlock() instanceof RunestoneEngravedTableBlock engravedTableBlock) || level.getBlockState(pos).getValue(RunestoneEngravedTableBlock.CORE) <= 0)) {
                shouldReCheck = true;
                break;
            } else {
                Item runestoneCoreItem = engravedTableBlock.getCore(level.getBlockState(pos).getValue(RunestoneEngravedTableBlock.CORE));
                structureCodes.put(runestoneCoreItem, structureCodes.getInt(runestoneCoreItem)+1);
            }
        }
        if (shouldReCheck) {
            int count = 0;
            BlockPos[] newRunestones = new BlockPos[4];
            for (int i=-9;i<=9;i++) {
                for (int j=-9;j<=9;j++) {
                    for (int k=-9;k<=9;k++) {
                        BlockPos newBlockpos = new BlockPos(reactorPos.getX() + i, reactorPos.getY() + j, reactorPos.getZ() + k);
                        BlockState state = level.getBlockState(newBlockpos);
                        if (level.getBlockState(newBlockpos).getBlock() instanceof RunestoneEngravedTableBlock engravedTableBlock) {
                            if (state.getValue(RunestoneEngravedTableBlock.CORE) > 0) {
                                newRunestones[count] = newBlockpos;
                                count++;
                                Item runestoneCoreItem = engravedTableBlock.getCore(state.getValue(RunestoneEngravedTableBlock.CORE));

                                structureCodes.put(runestoneCoreItem, structureCodes.getInt(runestoneCoreItem)+1);
                                level.levelEvent(232424314, newBlockpos, 2);
                                if (count >= 4) {
                                    for (BlockPos toSpawnParticle : newRunestones) {
                                        level.levelEvent(232424314, toSpawnParticle, 4);
                                    }
                                    this.modifyRunestonePoses(newRunestones);
                                    level.levelEvent(232424314, reactorPos, 3);
                                    break;
                                }
                            }
                        }
                    }
                }
            }

        }
    }
}
