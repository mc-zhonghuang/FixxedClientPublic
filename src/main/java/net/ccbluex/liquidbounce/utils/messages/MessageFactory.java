package net.ccbluex.liquidbounce.utils.messages;

import net.minecraft.util.EnumChatFormatting;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class MessageFactory {

    @NotNull
    public static TextMessage text(@Nullable String text) {
        return TextMessage.of(text);
    }

    @NotNull
    public static TextMessage text(@Nullable String text, @Nullable EnumChatFormatting color) {
        return TextMessage.of(text, color);
    }

    @NotNull
    public static HelpMessage help(@NotNull String name, @NotNull String command, @NotNull HelpMessage.UsageMessage... subCommands) {
        return HelpMessage.of(name, command, subCommands);
    }

    @NotNull
    public static HelpMessage.UsageMessage usage(@NotNull String command, @NotNull String description) {
        return HelpMessage.UsageMessage.of(command, description);
    }

    @NotNull
    public static Message empty() {
        return EmptyMessage.get();
    }

    private MessageFactory() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}
