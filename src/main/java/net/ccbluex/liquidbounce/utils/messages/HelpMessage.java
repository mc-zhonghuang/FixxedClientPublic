package net.ccbluex.liquidbounce.utils.messages;

import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import org.jetbrains.annotations.NotNull;

public class HelpMessage extends TextMessage {

    private HelpMessage(@NotNull String name, @NotNull String command, @NotNull UsageMessage... subCommands) {
        super(name);
        this.append("\n\n");
        int i = 0;

        for (int length = subCommands.length; i < length; ++i) {
            UsageMessage subCommand = subCommands[i];

            this.append((IChatComponent) subCommand.command(command + " " + subCommand.command));
            if (i + 1 != length) {
                this.newLine();
            }
        }

    }

    @NotNull
    public static HelpMessage of(@NotNull String name, @NotNull String command, @NotNull UsageMessage... subCommands) {
        return new HelpMessage(name, command, subCommands);
    }

    public static final class UsageMessage extends TextMessage {

        @NotNull
        private final String command;
        @NotNull
        private final String description;

        private UsageMessage(@NotNull String command, @NotNull String description) {
            super("> ", EnumChatFormatting.GRAY);
            this.command = command;
            this.description = description;
            this.append(this.command, EnumChatFormatting.GREEN);
            this.append(" - ", EnumChatFormatting.GRAY);
            this.append(this.description + (this.description.endsWith(".") ? "" : "."), EnumChatFormatting.RESET);
        }

        @NotNull
        public static UsageMessage of(@NotNull String command, @NotNull String description) {
            return new UsageMessage(command, description);
        }

        @NotNull
        public UsageMessage command(@NotNull String command) {
            return new UsageMessage(command, this.description);
        }
    }
}
