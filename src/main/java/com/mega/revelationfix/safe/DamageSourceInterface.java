package com.mega.revelationfix.safe;

public interface DamageSourceInterface {
    void revelationfix$setBypassArmor(boolean z);

    boolean revelationfix$bypassArmor();

    void revelationfix$trueKill(boolean z);

    boolean revelationfix$trueKill();

    void revelationfix$fePower(boolean z);

    boolean revelationfix$fePower();

    void revelationfix$setBypassAll(boolean z);

    boolean revelationfix$isBypassAll();

    void giveSpecialTag(byte tag);

    void cleanSpecialTag(byte tag);

    boolean hasTag(byte tag);
}
