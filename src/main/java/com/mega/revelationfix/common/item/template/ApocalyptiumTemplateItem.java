package com.mega.revelationfix.common.item.template;

import net.minecraft.world.item.SmithingTemplateItem;

public class ApocalyptiumTemplateItem extends SmithingTemplateItem {
    public ApocalyptiumTemplateItem() {
        super(TemplateFactory.APOCALYPTIUM_UPGRADE_APPLIES_TO, TemplateFactory.APOCALYPTIUM_UPGRADE_INGREDIENTS, TemplateFactory.APOCALYPTIUM_UPGRADE, TemplateFactory.APOCALYPTIUM_UPGRADE_BASE_SLOT_DESCRIPTION, TemplateFactory.APOCALYPTIUM_UPGRADE_ADDITIONS_SLOT_DESCRIPTION, TemplateFactory.createUpgradeIconList(), TemplateFactory.createUpgradeMaterialList());
    }
}
