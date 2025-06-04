package com.mega.revelationfix.safe.entity;

import net.minecraft.CrashReport;
import net.minecraft.CrashReportCategory;
import net.minecraft.ReportedException;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.SynchedEntityData;
import org.apache.commons.lang3.ObjectUtils;
import org.jetbrains.annotations.NotNull;

public class ApollyonEntityData extends SynchedEntityData {
    public ApollyonEntityData(SynchedEntityData data) {
        super(data.entity);
        this.itemsById = data.itemsById;
        this.lock = data.lock;
        this.isDirty = data.isDirty;
    }

    @Override
    public <T> void set(@NotNull EntityDataAccessor<T> p_135382_, @NotNull T p_135383_) {
        this.set(p_135382_, p_135383_, false);
    }

    @Override
    public <T> @NotNull T get(@NotNull EntityDataAccessor<T> p_135371_) {
        return this.getItem(p_135371_).getValue();
    }

    @SuppressWarnings("ALL")
    public <T> void set(EntityDataAccessor<T> p_276368_, T p_276363_, boolean p_276370_) {
        DataItem<T> dataitem = this.getItem(p_276368_);
        if (p_276370_ || ObjectUtils.notEqual(p_276363_, dataitem.getValue())) {
            dataitem.setValue(p_276363_);
            this.entity.onSyncedDataUpdated(p_276368_);
            dataitem.setDirty(true);
            this.isDirty = true;
        }

    }

    @SuppressWarnings("ALL")
    public <T> DataItem<T> getItem(EntityDataAccessor<T> p_135380_) {
        this.lock.readLock().lock();

        DataItem dataitem;
        try {
            dataitem = (DataItem) this.itemsById.get(p_135380_.getId());
        } catch (Throwable var9) {
            CrashReport crashreport = CrashReport.forThrowable(var9, "Getting synched entity data");
            CrashReportCategory crashreportcategory = crashreport.addCategory("Synched entity data");
            crashreportcategory.setDetail("Data ID", p_135380_);
            throw new ReportedException(crashreport);
        } finally {
            this.lock.readLock().unlock();
        }

        return dataitem;
    }
}
