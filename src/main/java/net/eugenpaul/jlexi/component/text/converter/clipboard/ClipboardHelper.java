package net.eugenpaul.jlexi.component.text.converter.clipboard;

import java.util.List;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ClipboardHelper {

    private static final List<String> CLIPBOARD_KEYWORDS = List.of(//
            "Version", //
            "StartHTML", //
            "EndHTML", //
            "StartFragment", //
            "EndFragment", //
            "StartSelection", //
            "EndSelection" //
    );

    public static String extractHtml(String clipboardHtml) {
        int offset = 0;

        for (var tag : CLIPBOARD_KEYWORDS) {
            offset = readTag(clipboardHtml, offset, tag);
        }

        return clipboardHtml.substring(offset);
    }

    private static int readTag(String clipboardHtml, int offset, String tag) {
        if (clipboardHtml.startsWith(tag, offset)) {
            // tag
            offset += tag.length();
            // :
            offset++;
            // length
            while (isDigitOrDot(clipboardHtml.charAt(offset))) {
                offset++;
            }
            // EOL
            while (isEol(clipboardHtml.charAt(offset))) {
                offset++;
            }
        }
        return offset;
    }

    private static boolean isDigitOrDot(char c) {
        return c >= '0' && c <= '9' || c == '.';
    }

    private static boolean isEol(char c) {
        return c == '\n' || c == 'r';
    }
}
