package app;

import java.awt.*;

/**
 * Utility class to centralize all application-wide style constants (colors, fonts, dimensions).
 * This ensures UI consistency across all views and allows for easy theming changes (SRP).
 */
public class StyleConstants {

    // --- COLOR PALETTE ---
    public static final Color COLOR_NAV_BAR = new Color(22, 160, 133);       // Main Teal/Green
    public static final Color COLOR_NAV_BAR_HOVER = new Color(20, 140, 113);  // Darker Teal on hover
    public static final Color COLOR_NAV_BAR_TEXT = Color.WHITE;
    public static final Color COLOR_CONTENT_BACKGROUND = new Color(245, 247, 250); // Soft Gray-White
    public static final Color COLOR_PRIMARY_BUTTON = new Color(41, 128, 185); // Nice Blue for CTA
    public static final Color COLOR_TEXT_DARK = new Color(44, 62, 80);       // Dark Blue-Grey for text

    // --- FONT STYLES ---
    public static final String FONT_FAMILY = "Segoe UI"; // Modern cross-platform font choice

    public static final Font FONT_NAV_BAR = new Font(FONT_FAMILY, Font.BOLD, 14);
    public static final Font FONT_TITLE = new Font(FONT_FAMILY, Font.BOLD, 28);
    public static final Font FONT_BODY = new Font(FONT_FAMILY, Font.PLAIN, 16);
    public static final Font FONT_CTA_BUTTON = new Font(FONT_FAMILY, Font.BOLD, 16);

    // --- DIMENSIONS ---
    public static final Dimension DIM_NAV_BUTTON = new Dimension(140, 50);
}