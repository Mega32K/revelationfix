package com.mega.revelationfix.common.apollyon.common;

import com.Polarice3.Goety.common.entities.boss.Apostle;
import com.mega.endinglib.mixin.accessor.AccessorSynchedEntityData;
import com.mega.endinglib.util.java.ClassHelper;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import z1gned.goetyrevelation.util.ApollyonAbilityHelper;

public class ApollyonSynchedEntityData extends SynchedEntityData {
    private Apostle apostle;
    private ApollyonAbilityHelper apollyonAbilityHelper;

    public ApollyonSynchedEntityData(Entity p_135351_) {
        super(p_135351_);
        checkApostle();
    }

    public static synchronized void hackData(Apostle apostle, SynchedEntityData srcDataManager) {
            /*
            ApollyonSynchedEntityData entityData = new ApollyonSynchedEntityData(srcDataManager.entity);
            for (Field field : SynchedEntityData.class.getDeclaredFields()) {
                if (!Modifier.isStatic(field.getModifiers())) {
                    VarHandle varHandle = Hack.getImplLookup().unreflectVarHandle(field);
                    varHandle.set(entityData, varHandle.get(srcDataManager));
                }
            }
            entityData.checkApostle();
            apostle.entityData = entityData;
             */
        if (apostle.getEntityData().getClass().equals(SynchedEntityData.class))
            ClassHelper.replaceKlassPtr(apostle.getEntityData(), ApollyonSynchedEntityData.class);
    }

    private void checkApostle() {
        if (apostle == null || apollyonAbilityHelper == null) {
            apostle = (Apostle) ((AccessorSynchedEntityData) this).getEntity();
            apollyonAbilityHelper = ((ApollyonAbilityHelper) apostle);
        }
    }

    /*
    @Override
    public <T> void set(@NotNull EntityDataAccessor<T> dataAccessor, @NotNull T value) {
        if (apollyonAbilityHelper.allTitlesApostle_1_20_1$isApollyon() && apostle.isInNether())
            this.set(dataAccessor, value, false);
        else super.set(dataAccessor, value);
    }

    @Override
    public <T> void set(@NotNull EntityDataAccessor<T> dataAccessor, @NotNull T value, boolean forceDirty) {
        if (apollyonAbilityHelper.allTitlesApostle_1_20_1$isApollyon() && apostle.isInNether())
            this.set0(dataAccessor, value, forceDirty);
        else super.set(dataAccessor, value, forceDirty);
    }

    public <T> void set0(EntityDataAccessor<T> p_276368_, T p_276363_, boolean p_276370_) {
        SynchedEntityData.DataItem<T> dataitem = this.getItem(p_276368_);
        BypassesDataItemAccessor<T> accessorEntity = (BypassesDataItemAccessor<T>) dataitem;
        if (p_276370_ || (ObjectUtils.notEqual(p_276363_, dataitem.getValue()) || ObjectUtils.notEqual(p_276363_, accessorEntity.getValue()))) {
            dataitem.setValue(p_276363_);
            accessorEntity.setValue(p_276363_);
            this.entity.onSyncedDataUpdated(p_276368_);
            dataitem.setDirty(true);
            this.isDirty = true;
            accessorEntity.setDirty(true);
        }

    }

    @Override
    public <T> SynchedEntityData.@NotNull DataItem<T> getItem(@NotNull EntityDataAccessor<T> entityDataAccessor) {
        this.lock.readLock().lock();
        SynchedEntityData.DataItem<T> dataitem;
        try {
            dataitem = (SynchedEntityData.DataItem<T>) this.itemsById.get(entityDataAccessor.getId());
        } catch (Throwable throwable) {
            CrashReport crashreport = CrashReport.forThrowable(throwable, "Getting synched entity data");
            CrashReportCategory crashreportcategory = crashreport.addCategory("Synched entity data");
            crashreportcategory.setDetail("Data ID", entityDataAccessor);
            throw new ReportedException(crashreport);
        } finally {
            this.lock.readLock().unlock();
        }

        return dataitem;
    }
     */
}
