package net.ccbluex.liquidbounce.utils.messages;

import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;

public class TextMessage extends Message {

    private static final ChatStyle RESET_STYLE = (new ChatStyle()).setColor(EnumChatFormatting.RESET);
    private final StringBuilder message;

    protected TextMessage(@Nullable String message, @Nullable EnumChatFormatting color) {
        this.message = message != null ? new StringBuilder(message) : new StringBuilder();
        this.setChatStyle(color != null ? (new ChatStyle()).setColor(color) : TextMessage.RESET_STYLE);
    }

    protected TextMessage(@Nullable String message) {
        this(message, (EnumChatFormatting) null);
    }

    @NotNull
    public static TextMessage of(@Nullable String text) {
        return new TextMessage(text);
    }

    @NotNull
    public static TextMessage of(@Nullable String text, @Nullable EnumChatFormatting color) {
        return new TextMessage(text, color);
    }

    public TextMessage prefix(@Nullable TextMessage prefix) {
        return prefix != null ? prefix.createCopy().appendSibling(this) : this;
    }

    public TextMessage suffix(@Nullable IChatComponent suffix) {
        if (suffix != null) {
            this.siblings.add(suffix);
        }

        return this;
    }

    public TextMessage newLine() {
        this.append("\n");
        return this;
    }

    public TextMessage append(@Nullable String s, @Nullable EnumChatFormatting color) {
        this.appendSibling(of(s, color));
        return this;
    }

    public TextMessage appendText(@Nullable String s) {
        this.append(s, EnumChatFormatting.RESET);
        return this;
    }

    public TextMessage append(@Nullable String s) {
        this.appendText(s);
        return this;
    }

    public TextMessage append(IChatComponent component) {
        this.appendSibling(component);
        return this;
    }

    public TextMessage appendSibling(IChatComponent component) {
        super.appendSibling(component);
        return this;
    }

    public TextMessage createCopy() {
        TextMessage message = of(this.message.toString());

        message.setChatStyle(this.getChatStyle().createShallowCopy());
        Iterator iterator = this.getSiblings().iterator();

        while (iterator.hasNext()) {
            IChatComponent sibling = (IChatComponent) iterator.next();

            message.appendSibling(sibling.createCopy());
        }

        return message;
    }

    public String getUnformattedTextForChat() {
        return this.message.toString();
    }
}
