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

    public static String addClipboardTags(String html) {
        StringBuilder response = new StringBuilder(html.length() + 200);

        response.append("Version:0.9\n");
        response.append("StartHTML:0000000000\n");
        response.append("EndHTML:0000000000\n");
        response.append("StartFragment:0000000000\n");
        response.append("EndFragment:0000000000\n");

        int startPos = response.toString().length();
        int fullLength = startPos + html.getBytes().length;

        response = new StringBuilder(html.length() + 200);
        response.append("Version:0.9\n");
        response.append("StartHTML:" + toOffset(startPos) + "\n");
        response.append("EndHTML:" + toOffset(fullLength) + "\n");
        response.append("StartFragment:" + toOffset(startPos) + "\n");
        response.append("EndFragment:" + toOffset(fullLength) + "\n");

        response.append(html);

        System.out.println("---------------------------------------");
        System.out.println(response.toString());
        System.out.println("---------------------------------------");

        return response.toString();
    }

    private static String toOffset(int startPos) {
        return String.format("%010d", startPos);
    }
}
