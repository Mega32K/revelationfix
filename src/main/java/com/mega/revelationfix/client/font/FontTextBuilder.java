package com.mega.revelationfix.client.font;

import com.mega.revelationfix.safe.TextColorInterface;
import net.minecraft.network.chat.Style;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.FormattedCharSink;

import java.util.function.Predicate;

public class FontTextBuilder {
    static MyFormattedCharSink NORMAL = new MyFormattedCharSink((t) -> true);
    static MyFormattedCharSink APOLLYON = new MyFormattedCharSink((t) -> t.getColor() != null && ((TextColorInterface) (Object) t.getColor()).revelationfix$getCode() == 'q');
    static MyFormattedCharSink EDEN = new MyFormattedCharSink((t) -> t.getColor() != null && ((TextColorInterface) (Object) t.getColor()).revelationfix$getCode() == '-');

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
    public static String formattedCharSequenceToStringEden(FormattedCharSequence text) {
        text.accept(EDEN);
        String s = EDEN.getText();
        EDEN.text = new StringBuilder();
        return s;
    }
    public static class MyFormattedCharSink implements FormattedCharSink {
        public Predicate<Style> stylePredicate;
        private StringBuilder text = new StringBuilder();

        public MyFormattedCharSink(Predicate<Style> stylePredicate) {
            this.stylePredicate = stylePredicate;
        }

        @Override
        public boolean accept(int p_13746_, Style p_13747_, int p_13748_) {
            text.append(stylePredicate.test(p_13747_) ? Character.toChars(p_13748_) : new char[]{' '});
            return true;
        }

        public String getText() {
            return text.toString();
        }
    }
}
