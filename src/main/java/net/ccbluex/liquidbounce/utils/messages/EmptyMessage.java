package net.ccbluex.liquidbounce.utils.messages;

import net.minecraft.util.ChatStyle;
import net.minecraft.util.IChatComponent;

import java.util.Spliterator;
import java.util.Spliterators;

final class EmptyMessage extends Message {

    private static EmptyMessage INSTANCE;

    static EmptyMessage get() {
        if (EmptyMessage.INSTANCE == null) {
            EmptyMessage.INSTANCE = new EmptyMessage();
        }

        return EmptyMessage.INSTANCE;
    }

    public IChatComponent appendSibling(IChatComponent component) {
        throw new UnsupportedOperationException();
    }

    public IChatComponent appendText(String text) {
        throw new UnsupportedOperationException();
    }

    public IChatComponent setChatStyle(ChatStyle style) {
        throw new UnsupportedOperationException();
    }

    public Spliterator spliterator() {
        return Spliterators.emptySpliterator();
    }
}
