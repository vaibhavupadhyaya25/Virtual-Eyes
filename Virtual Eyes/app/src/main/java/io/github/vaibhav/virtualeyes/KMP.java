package io.github.vaibhav.virtualeyes;

/**
 * Created by Ruchir Pokharna on 9/27/2017.
 */

public class KMP {

    public static boolean match(String pattern, String text) {
        int[] lsp = computeLspTable(pattern);

        int j = 0;
        for (int i = 0; i < text.length(); i++) {
            while (j > 0 && text.charAt(i) != pattern.charAt(j)) {
                j = lsp[j - 1];
            }
            if (text.charAt(i) == pattern.charAt(j)) {
                j++;
                if (j == pattern.length())
                    return true;
            }
        }

        return false;
    }

    private static int[] computeLspTable(String pattern) {
        int[] lsp = new int[pattern.length()];
        lsp[0] = 0;
        for (int i = 1; i < pattern.length(); i++) {
            int j = lsp[i - 1];
            while (j > 0 && pattern.charAt(i) != pattern.charAt(j))
                j = lsp[j - 1];
            if (pattern.charAt(i) == pattern.charAt(j))
                j++;
            lsp[i] = j;
        }
        return lsp;
    }
}
