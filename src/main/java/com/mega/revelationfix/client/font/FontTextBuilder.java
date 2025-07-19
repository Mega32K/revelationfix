package com.mega.revelationfix.client.font;

import com.mega.endinglib.api.client.text.TextColorInterface;
import com.mega.revelationfix.common.compat.SafeClass;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Style;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.FormattedCharSink;

import java.util.Arrays;
import java.util.function.Predicate;

public class FontTextBuilder {
    static MyFormattedCharSink NORMAL = new MyFormattedCharSink((t) -> true);
    static MyFormattedCharSink APOLLYON = new MyFormattedCharSink((t) -> t.getColor() != null && ((TextColorInterface) (Object) t.getColor()).endinglib$getCode() == 'q');
    static MyFormattedCharSink EDEN = new MyFormattedCharSink((t) -> t.getColor() != null && ((TextColorInterface) (Object) t.getColor()).endinglib$getCode() == '-');
    static MyFormattedCharSink NO_EDEN = new MyFormattedCharSink(EDEN.stylePredicate.negate());

    public static String formattedCharSequenceToString(FormattedCharSequence text) {
        text.accept(NORMAL);
        String s = NORMAL.getText();
        NORMAL.text = new StringBuilder();
        return s;
    }

    public static String formattedCharSequenceToStringApollyon(FormattedCharSequence text) {
        text.accept(APOLLYON);
        String s = APOLLYON.getText();
        APOLLYON.text = new StringBuilder();
        return s;
    }
    public static String[] formattedCharSequenceToStringEden(FormattedCharSequence text) {
        String[] out = new String[2];
        text.accept(EDEN);
        out[0] = EDEN.getText();
        EDEN.text = new StringBuilder();
        text.accept(NO_EDEN);
        out[1] = NO_EDEN.getText();
        NO_EDEN.text = new StringBuilder();
        return out;
    }
    public static class MyFormattedCharSink implements FormattedCharSink {
        public Predicate<Style> stylePredicate;
        private StringBuilder text = new StringBuilder();
        public static final char[] emptyChars = SafeClass.isModernUILoaded() ? new char[] {' ', ' ', ' ', ' '} : new char[] {' ', ' '};
        public MyFormattedCharSink(Predicate<Style> stylePredicate) {
            this.stylePredicate = stylePredicate;
        }
        @Override
        public boolean accept(int p_13746_, Style p_13747_, int p_13748_) {
            text.append(stylePredicate.test(p_13747_) ? Character.toChars(p_13748_) : emptyChars);
            return true;
        }

        public String getText() {
            return text.toString();
        }
    }
}
