package com.example.lab203.common.util;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * SVG ???????
 */
public final class AvatarSvgGenerator {

    private AvatarSvgGenerator() {
    }

    /**
     * ???????????
     *
     * @param outputPath ????
     * @param displayText ????
     * @param primaryColor ??
     * @param secondaryColor ??
     * @throws IOException ??????
     */
    public static void generate(Path outputPath, String displayText, String primaryColor, String secondaryColor) throws IOException {
        Files.createDirectories(outputPath.getParent());
        String svg = """
                <svg xmlns="http://www.w3.org/2000/svg" width="320" height="320" viewBox="0 0 320 320">
                  <defs>
                    <linearGradient id="g" x1="0%%" y1="0%%" x2="100%%" y2="100%%">
                      <stop offset="0%%" stop-color="%s"/>
                      <stop offset="100%%" stop-color="%s"/>
                    </linearGradient>
                  </defs>
                  <rect width="320" height="320" rx="56" fill="url(#g)"/>
                  <circle cx="160" cy="128" r="62" fill="rgba(255,255,255,0.18)"/>
                  <path d="M82 258c20-42 58-64 78-64s58 22 78 64" fill="rgba(255,255,255,0.18)"/>
                  <text x="160" y="294" text-anchor="middle" font-size="34" font-family="Microsoft YaHei" fill="#ffffff">%s</text>
                </svg>
                """.formatted(primaryColor, secondaryColor, displayText);
        Files.writeString(outputPath, svg, StandardCharsets.UTF_8);
    }
}
