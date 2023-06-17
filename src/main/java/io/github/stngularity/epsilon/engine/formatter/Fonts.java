package io.github.stngularity.epsilon.engine.formatter;

import org.jetbrains.annotations.NotNull;

/**
 * Font formatter
 */
public class Fonts {
    public static final String _default = "abcdefghijklmnopqrstuvwxyzабвгдеёжзиклмнопрстуфхцчшщъыьэюя";
    public static final String smallcaps = "ᴀʙᴄᴅᴇғɢʜɪᴊᴋʟᴍɴᴏᴘǫʀsᴛᴜᴠᴡxʏᴢᴀбʙгдᴇёжзиклᴍʜᴏпᴘᴄᴛʏфxцчшщъыьэюя";

    /**
     * Replaces one font with another
     *
     * @param original Original text for replacement
     * @param from     Font that using in original text
     * @param to       Font that You want to use in output text
     *
     * @return New text with specified font
     */
    public static @NotNull String fromFontToFont(@NotNull String original, String from, String to) {
        StringBuilder output = new StringBuilder();
        for(int i = 0; i < original.length(); i++) {
            int index = from.indexOf(original.toLowerCase().charAt(i));
            if(index == -1) {
                output.append(original.charAt(i));
                continue;
            }

            output.append(to.charAt(index));
        }

        return output.toString();
    }

    public static String getFont(@NotNull String fontName) {
        if(fontName.equals("smallcaps")) return smallcaps;
        return _default;
    }
}
