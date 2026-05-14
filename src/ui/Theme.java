package ui;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

/** Shared colors, fonts, and UI factory methods used across all panels. */
public class Theme {

    // ─── Colors ──────────────────────────────────────────────────────────────────
    public static final Color BG     = new Color(0xFF, 0xF3, 0xDC);
    public static final Color ORANGE = new Color(0xFF, 0x8C, 0x00);
    public static final Color RED    = new Color(0xC0, 0x39, 0x2B);
    public static final Color GREEN  = new Color(0x27, 0xAE, 0x60);
    public static final Color EASY   = new Color(0x2E, 0xCC, 0x71);
    public static final Color MED    = new Color(0xF3, 0x9C, 0x12);
    public static final Color HARD   = new Color(0xE7, 0x4C, 0x3C);

    // ─── Fonts ───────────────────────────────────────────────────────────────────
    public static final Font F_BOLD  = new Font("Thasadith", Font.BOLD, 15);
    public static final Font F_PLAIN = new Font("Thasadith", Font.BOLD, 14);

    // ─── Decoy ingredients shown alongside real ones ──────────────────────────────
    public static final String[][] DECOY_POOL = {
        {"พริกแกง", "🌶"}, {"หอมแดง", "🧅"}, {"กะทิ", "🥥"}, {"มะนาว", "🍋"}
    };

    // ─── Widget factories ────────────────────────────────────────────────────────

    public static JButton pill(String text, Color bg) {
        JButton b = new JButton(text);
        b.setBackground(bg);
        b.setForeground(Color.WHITE);
        b.setOpaque(true);
        b.setContentAreaFilled(true);
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setFont(F_BOLD);
        return b;
    }

    public static JLabel bold(String text) {
        JLabel l = new JLabel(text);
        l.setFont(F_BOLD);
        return l;
    }

    public static JLabel plainCenter(String text) {
        JLabel l = new JLabel(text, SwingConstants.CENTER);
        l.setFont(F_PLAIN);
        return l;
    }

    public static JLabel emoji(String text, int size) {
        JLabel l = new JLabel(text, SwingConstants.CENTER);
        l.setFont(new Font("Segoe UI Emoji", Font.PLAIN, size));
        return l;
    }

    public static Border roundBorder(Color color, int thickness) {
        return BorderFactory.createLineBorder(color, thickness, true);
    }

    public static EmptyBorder pad(int top, int left, int bottom, int right) {
        return new EmptyBorder(top, left, bottom, right);
    }

    public static JPanel vbox(JComponent... components) {
        JPanel p = new JPanel(new GridLayout(components.length, 1, 0, 5));
        p.setOpaque(false);
        for (JComponent c : components) p.add(c);
        return p;
    }

    public static GridBagConstraints gbc() {
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(10, 10, 10, 10);
        return g;
    }

    // ─── Animations ──────────────────────────────────────────────────────────────

    public static void shake(Component c) {
        Point origin = c.getLocation();
        new javax.swing.Timer(50, new ActionListener() {
            int count = 0;
            public void actionPerformed(ActionEvent e) {
                if (count >= 6) {
                    c.setLocation(origin);
                    ((javax.swing.Timer) e.getSource()).stop();
                } else {
                    c.setLocation(origin.x + (count % 2 == 0 ? 5 : -5), origin.y);
                    count++;
                }
            }
        }).start();
    }

    public static void delay(int ms, Runnable action) {
        javax.swing.Timer t = new javax.swing.Timer(ms, e -> action.run());
        t.setRepeats(false);
        t.start();
    }
}
