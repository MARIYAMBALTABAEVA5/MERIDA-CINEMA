package cinema.ui;

import cinema.facade.CinemaBookingFacade;
import cinema.model.*;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.awt.geom.Arc2D;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.util.*;
import java.util.List;

public class CinemaApp extends JFrame {
    private static final Color BG = new Color(255, 244, 248);
    private static final Color PANEL = new Color(255, 230, 240);
    private static final Color PANEL2 = new Color(255, 214, 230);
    private static final Color TEXT = new Color(86, 33, 58);
    private static final Color MUTED = new Color(150, 96, 123);
    private static final Color ACCENT = new Color(240, 120, 170);
    private static final Color BORDER = new Color(228, 173, 198);

    private final CinemaBookingFacade facade = new CinemaBookingFacade();
    private final Movie[] movies = {
            new Movie("Barbie", "Comedy / Fantasy", 114, "12+"),
            new Movie("Legally Blonde", "Comedy / Romance", 96, "12+"),
            new Movie("The Notebook", "Romance / Drama", 123, "16+"),
            new Movie("Mr. & Mrs. Smith", "Action / Romance", 120, "16+"),
            new Movie("Titanic", "Romance / Drama", 195, "12+")
    };

    private Movie selectedMovie = movies[0];
    private Showtime selectedShowtime;
    private final LinkedHashSet<String> selectedSeats = new LinkedHashSet<>();
    private final JPanel movieCards = new JPanel(new GridLayout(1, 5, 12, 12));
    private final JPanel seatPanel = new JPanel(new GridLayout(5, 8, 7, 7));

    private final JComboBox<String> dateBox = combo(new String[]{"Today", "Tomorrow"});
    private final JComboBox<String> timeBox = combo(new String[]{"11:30", "14:20", "18:10", "21:00"});
    private final JComboBox<String> ticketBox = combo(new String[]{"Standard", "VIP"});
    private final JComboBox<String> discountBox = combo(new String[]{"Regular", "Student -10%", "Disabilities -20%", "Pensioners -15%"});
    private final JCheckBox popcorn = check("Popcorn combo  +1 000 ₸");
    private final JCheckBox drink = check("Soft drink  +500 ₸");
    private final JTextField nameField = field("Your full name");
    private final JTextField emailField = field("you@example.com");
    private final JLabel totalLabel = label("0 ₸", 27, TEXT, true);
    private final JLabel selectionLabel = centeredHtml("Choose seats", 12, MUTED, 260);
    private final JLabel statusLabel = centeredHtml("Select your movie and seats", 11, MUTED, 360);

    public CinemaApp() {
        super("MERIDA CINEMA | Ticket Reservation System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1180, 880);
        setMinimumSize(new Dimension(940, 720));
        setLocationRelativeTo(null);
        getContentPane().setBackground(BG);

        movieCards.setOpaque(false);
        movieCards.setMaximumSize(new Dimension(Integer.MAX_VALUE, 285));
        seatPanel.setOpaque(false);
        seatPanel.setMaximumSize(new Dimension(430, 235));
        seatPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel page = new JPanel();
        page.setBackground(BG);
        page.setLayout(new BoxLayout(page, BoxLayout.Y_AXIS));
        page.setBorder(new EmptyBorder(22, 36, 30, 36));

        page.add(header());
        page.add(gap(20));
        JLabel nowShowing = label("NOW SHOWING • MERIDA MOVIES", 20, TEXT, true);
        nowShowing.setAlignmentX(Component.CENTER_ALIGNMENT);
        page.add(nowShowing);
        page.add(gap(12));
        page.add(movieCards);
        page.add(gap(22));

        JPanel cols = new JPanel(new GridLayout(1, 2, 20, 0));
        cols.setOpaque(false);
        cols.setAlignmentX(Component.CENTER_ALIGNMENT);
        cols.add(leftPanel());
        cols.add(rightPanel());
        page.add(cols);
        page.add(gap(10));

        JScrollPane scroll = new JScrollPane(page);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(BG);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        setContentPane(scroll);

        dateBox.addActionListener(e -> resetShowtime());
        timeBox.addActionListener(e -> resetShowtime());
        ticketBox.addActionListener(e -> updateSummary());
        discountBox.addActionListener(e -> updateSummary());
        popcorn.addActionListener(e -> updateSummary());
        drink.addActionListener(e -> updateSummary());
        selectMovie(movies[0]);
    }

    private JPanel header() {
        JPanel p = new JPanel();
        p.setOpaque(false);
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel logo = label("◆  MERIDA CINEMA", 30, TEXT, true);
        JLabel subtitle = label("ROMANCE, GLAMOUR AND ICONIC MOVIE NIGHTS", 11, MUTED, false);
        JLabel badge = label("●  CINEMA TICKET RESERVATION • DESIGN PATTERNS PROJECT", 11, new Color(214, 88, 144), true);
        logo.setAlignmentX(Component.CENTER_ALIGNMENT);
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        badge.setAlignmentX(Component.CENTER_ALIGNMENT);
        p.add(logo);
        p.add(gap(4));
        p.add(subtitle);
        p.add(gap(8));
        p.add(badge);
        return p;
    }

    private JPanel leftPanel() {
        JPanel outer = card();
        outer.setLayout(new BoxLayout(outer, BoxLayout.Y_AXIS));

        outer.add(sectionTitle("01  SELECT SHOWTIME"));
        outer.add(gap(12));
        JPanel inputs = new JPanel(new GridLayout(1, 2, 12, 0));
        inputs.setOpaque(false);
        inputs.setMaximumSize(new Dimension(Integer.MAX_VALUE, 64));
        inputs.add(labeled("Day", dateBox));
        inputs.add(labeled("Time", timeBox));
        outer.add(inputs);
        outer.add(gap(23));

        outer.add(sectionTitle("02  PICK YOUR SEATS"));
        outer.add(gap(10));
        JLabel hint = label("Choose one or more available seats", 11, MUTED, false);
        hint.setAlignmentX(Component.CENTER_ALIGNMENT);
        outer.add(hint);
        outer.add(gap(14));

        JPanel screenWrap = new JPanel();
        screenWrap.setOpaque(false);
        screenWrap.setLayout(new BoxLayout(screenWrap, BoxLayout.Y_AXIS));
        screenWrap.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel screen = label("S C R E E N", 12, ACCENT, true);
        screen.setAlignmentX(Component.CENTER_ALIGNMENT);
        JPanel screenLine = new JPanel();
        screenLine.setBackground(ACCENT);
        screenLine.setMaximumSize(new Dimension(310, 4));
        screenLine.setPreferredSize(new Dimension(310, 4));
        screenLine.setAlignmentX(Component.CENTER_ALIGNMENT);
        screenWrap.add(screen);
        screenWrap.add(gap(6));
        screenWrap.add(screenLine);
        outer.add(screenWrap);
        outer.add(gap(20));
        outer.add(seatPanel);
        outer.add(gap(14));

        JPanel legend = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 2));
        legend.setOpaque(false);
        legend.setAlignmentX(Component.CENTER_ALIGNMENT);
        legend.add(label("■  Available", 11, MUTED, false));
        legend.add(label("■  Selected", 11, ACCENT, false));
        legend.add(label("■  Occupied", 11, new Color(186, 140, 161), false));
        outer.add(legend);
        return outer;
    }

    private JPanel rightPanel() {
        JPanel outer = card();
        outer.setLayout(new BoxLayout(outer, BoxLayout.Y_AXIS));

        outer.add(sectionTitle("03  CUSTOMIZE YOUR TICKETS"));
        outer.add(gap(11));
        JPanel opt = new JPanel(new GridLayout(1, 2, 12, 0));
        opt.setOpaque(false);
        opt.setMaximumSize(new Dimension(Integer.MAX_VALUE, 64));
        opt.add(labeled("Ticket type", ticketBox));
        opt.add(labeled("Discount", discountBox));
        outer.add(opt);
        outer.add(gap(14));

        popcorn.setAlignmentX(Component.CENTER_ALIGNMENT);
        drink.setAlignmentX(Component.CENTER_ALIGNMENT);
        outer.add(popcorn);
        outer.add(gap(6));
        outer.add(drink);
        outer.add(gap(20));

        outer.add(sectionTitle("04  YOUR DETAILS"));
        outer.add(gap(9));
        outer.add(labeled("Customer name", nameField));
        outer.add(gap(9));
        outer.add(labeled("Email (demo notification)", emailField));
        outer.add(gap(17));
        outer.add(new JSeparator());
        outer.add(gap(10));

        JLabel order = label("ORDER SUMMARY", 11, MUTED, true);
        order.setAlignmentX(Component.CENTER_ALIGNMENT);
        selectionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        totalLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel totalText = label("TOTAL", 11, MUTED, false);
        totalText.setAlignmentX(Component.CENTER_ALIGNMENT);
        outer.add(order);
        outer.add(gap(5));
        outer.add(selectionLabel);
        outer.add(gap(8));
        outer.add(totalText);
        outer.add(totalLabel);
        outer.add(gap(12));

        JButton reserve = button("CONFIRM BOOKING  →", ACCENT);
        reserve.setFont(new Font("SansSerif", Font.BOLD, 15));
        reserve.setMaximumSize(new Dimension(380, 48));
        reserve.setAlignmentX(Component.CENTER_ALIGNMENT);
        reserve.addActionListener(e -> book());
        outer.add(reserve);
        outer.add(gap(8));
        statusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        outer.add(statusLabel);
        return outer;
    }

    private void selectMovie(Movie movie) {
        selectedMovie = movie;
        selectedSeats.clear();
        updateTimesForMovie(movie);
        movieCards.removeAll();

        for (int i = 0; i < movies.length; i++) {
            Movie m = movies[i];
            boolean selected = m == movie;
            JPanel outer = new JPanel(new BorderLayout(0, 8));
            outer.setBackground(selected ? PANEL2 : PANEL);
            outer.setBorder(new CompoundBorder(
                    new LineBorder(selected ? ACCENT : BORDER, selected ? 2 : 1, true),
                    new EmptyBorder(9, 9, 10, 9)
            ));

            PosterPanel poster = new PosterPanel(i, m.title());
            poster.setPreferredSize(new Dimension(145, 190));
            outer.add(poster, BorderLayout.CENTER);

            JPanel bottom = new JPanel();
            bottom.setOpaque(false);
            bottom.setLayout(new BoxLayout(bottom, BoxLayout.Y_AXIS));
            JLabel title = centeredHtml("<b>" + m.title() + "</b>", 14, TEXT, 150);
            JLabel meta = centeredHtml(m.genre() + "<br>" + m.durationMinutes() + " min • " + m.ageRating(), 10, MUTED, 150);
            title.setAlignmentX(Component.CENTER_ALIGNMENT);
            meta.setAlignmentX(Component.CENTER_ALIGNMENT);
            bottom.add(title);
            bottom.add(gap(3));
            bottom.add(meta);
            outer.add(bottom, BorderLayout.SOUTH);

            outer.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            MouseAdapter onClick = new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    selectMovie(m);
                }
            };
            outer.addMouseListener(onClick);
            poster.addMouseListener(onClick);
            title.addMouseListener(onClick);
            meta.addMouseListener(onClick);
            movieCards.add(outer);
        }
        movieCards.revalidate();
        movieCards.repaint();
        resetShowtime();
    }

    private void resetShowtime() {
        selectedSeats.clear();
        selectedShowtime = new Showtime(selectedMovie,
                Objects.toString(dateBox.getSelectedItem()),
                Objects.toString(timeBox.getSelectedItem()), "Hall 1");
        refreshSeats();
    }

    private void refreshSeats() {
        seatPanel.removeAll();
        for (char row = 'A'; row <= 'E'; row++) {
            for (int n = 1; n <= 8; n++) {
                String seat = "" + row + n;
                boolean occupied = facade.isOccupied(selectedShowtime, seat);
                JButton b = button(seat, occupied ? new Color(221, 183, 201) : selectedSeats.contains(seat) ? ACCENT : PANEL2);
                b.setEnabled(!occupied);
                b.setForeground(occupied ? new Color(165, 118, 140) : TEXT);
                b.setFont(new Font("SansSerif", Font.BOLD, 11));
                b.setPreferredSize(new Dimension(42, 36));
                b.addActionListener(e -> {
                    if (selectedSeats.contains(seat)) selectedSeats.remove(seat);
                    else selectedSeats.add(seat);
                    refreshSeats();
                });
                seatPanel.add(b);
            }
        }
        seatPanel.revalidate();
        seatPanel.repaint();
        updateSummary();
    }

    private void updateSummary() {
        int count = selectedSeats.size();
        int total = facade.quote(
                Objects.toString(ticketBox.getSelectedItem(), "Standard"),
                popcorn.isSelected(), drink.isSelected(),
                Objects.toString(discountBox.getSelectedItem(), "Regular"), count
        );
        totalLabel.setText(String.format("%,d ₸", total).replace(',', ' '));

        String seatText = count == 0 ? "Choose seats" : count + " seat(s)<br>" + String.join(" • ", selectedSeats);
        selectionLabel.setText(html(seatText, 260));
        statusLabel.setText(html("Demo project • booking data is stored in memory", 360));
    }

    private void book() {
        try {
            Booking booking = facade.reserve(selectedShowtime, new ArrayList<>(selectedSeats),
                    Objects.toString(ticketBox.getSelectedItem()), popcorn.isSelected(), drink.isSelected(),
                    Objects.toString(discountBox.getSelectedItem()), nameField.getText(), emailField.getText());

            String receipt = "Booking confirmed!\n\nReference: " + booking.id() +
                    "\nFilm: " + booking.showtime().movie().title() +
                    "\nShowtime: " + booking.showtime() +
                    "\nSeats: " + String.join(", ", booking.seats()) +
                    "\nTicket: " + booking.ticketType() +
                    "\nExtras: " + booking.extras() +
                    "\nPricing: " + booking.discount() +
                    "\nTotal: " + booking.total() + " KZT\n\nThank you for choosing MERIDA CINEMA!\nYour tickets are ready.";

            JOptionPane.showMessageDialog(this, receipt, "MERIDA CINEMA Ticket • " + booking.id(), JOptionPane.INFORMATION_MESSAGE);
            selectedSeats.clear();
            refreshSeats();
        } catch (RuntimeException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Please check your booking", JOptionPane.WARNING_MESSAGE);
        }
    }

    private static JPanel card() {
        JPanel p = new JPanel();
        p.setBackground(PANEL);
        p.setBorder(new CompoundBorder(new LineBorder(BORDER, 1, true), new EmptyBorder(22, 22, 22, 22)));
        return p;
    }

    private static JLabel sectionTitle(String text) {
        JLabel l = label(text, 15, TEXT, true);
        l.setAlignmentX(Component.CENTER_ALIGNMENT);
        l.setHorizontalAlignment(SwingConstants.CENTER);
        return l;
    }

    private static JPanel labeled(String title, JComponent item) {
        JPanel p = new JPanel();
        p.setOpaque(false);
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        JLabel text = label(title, 12, MUTED, false);
        text.setAlignmentX(Component.CENTER_ALIGNMENT);
        text.setHorizontalAlignment(SwingConstants.CENTER);
        item.setAlignmentX(Component.CENTER_ALIGNMENT);
        item.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        p.add(text);
        p.add(gap(6));
        p.add(item);
        return p;
    }

    private static JLabel label(String s, int size, Color c, boolean bold) {
        JLabel l = new JLabel(s);
        l.setForeground(c);
        l.setFont(new Font("SansSerif", bold ? Font.BOLD : Font.PLAIN, size));
        return l;
    }

    private static JLabel centeredHtml(String s, int size, Color c, int width) {
        JLabel l = new JLabel(html(s, width));
        l.setForeground(c);
        l.setFont(new Font("SansSerif", Font.PLAIN, size));
        l.setHorizontalAlignment(SwingConstants.CENTER);
        return l;
    }

    private static String html(String s, int width) {
        return "<html><div style='text-align:center;width:" + width + "px'>" + s + "</div></html>";
    }

    private static JTextField field(String hint) {
        JTextField f = new JTextField();
        f.setToolTipText(hint);
        f.setBackground(PANEL2);
        f.setForeground(TEXT);
        f.setCaretColor(TEXT);
        f.setFont(new Font("SansSerif", Font.PLAIN, 14));
        f.setBorder(new CompoundBorder(new LineBorder(BORDER, 1, true), new EmptyBorder(9, 10, 9, 10)));
        return f;
    }

    private static JComboBox<String> combo(String[] items) {
        JComboBox<String> c = new JComboBox<>(items);
        c.setBackground(PANEL2);
        c.setForeground(TEXT);
        c.setFont(new Font("SansSerif", Font.PLAIN, 13));
        return c;
    }

    private static JCheckBox check(String text) {
        JCheckBox b = new JCheckBox(text);
        b.setBackground(PANEL);
        b.setForeground(TEXT);
        b.setFocusPainted(false);
        b.setFont(new Font("SansSerif", Font.PLAIN, 13));
        return b;
    }

    private static JButton button(String s, Color bg) {
        JButton b = new JButton(s);
        b.setOpaque(true);
        b.setBackground(bg);
        b.setForeground(TEXT);
        b.setBorder(new EmptyBorder(9, 8, 9, 8));
        b.setFocusPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return b;
    }

    private static Component gap(int px) {
        return Box.createRigidArea(new Dimension(0, px));
    }

    private void updateTimesForMovie(Movie movie) {
        String[] times = switch (movie.title()) {
            case "Barbie" -> new String[]{"11:30", "14:20", "18:10", "21:00"};
            case "Legally Blonde" -> new String[]{"10:40", "13:15", "17:30", "20:20"};
            case "The Notebook" -> new String[]{"12:00", "15:10", "19:00", "21:40"};
            case "Mr. & Mrs. Smith" -> new String[]{"11:10", "14:45", "18:30", "22:00"};
            case "Titanic" -> new String[]{"10:30", "14:00", "18:00", "21:30"};
            default -> new String[]{"13:00", "16:30", "20:00"};
        };
        Object old = timeBox.getSelectedItem();
        timeBox.setModel(new DefaultComboBoxModel<>(times));
        if (old != null) {
            for (String t : times) if (t.equals(old.toString())) timeBox.setSelectedItem(t);
        }
    }

    private static class CurvedScreen extends JPanel {
        CurvedScreen() {
            setOpaque(false);
            setPreferredSize(new Dimension(330, 35));
            setMaximumSize(new Dimension(330, 35));
            setMinimumSize(new Dimension(330, 35));
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(ACCENT);
            g2.setStroke(new BasicStroke(4f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g2.draw(new Arc2D.Double(12, -38, getWidth() - 24, 66, 200, 140, Arc2D.OPEN));
            g2.dispose();
        }
    }

    /**
     * Movie poster loaded from resources/posters.
     */
    private static class PosterPanel extends JPanel {
        private final int style;
        private final String title;
        private BufferedImage image;
        private static final String[] FILES = {
                "/posters/barbie.jpg", "/posters/legally_blonde.jpg", "/posters/notebook.jpg",
                "/posters/mr_mrs_smith.jpg", "/posters/titanic.jpg"
        };

        PosterPanel(int style, String title) {
            this.style = style;
            this.title = title;
            setOpaque(false);
            try {
                java.net.URL url =
                        CinemaApp.class.getResource(FILES[style]);
                System.out.println("Loading: " + FILES[style]);
                System.out.println("URL: " + url);
                if (url == null) {
                    System.err.println("POSTER NOT FOUND: " + FILES[style]);
                } else {
                    image = ImageIO.read(url);
                    System.out.println("Poster loaded: " + title);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int w = getWidth(), h = getHeight();
            Shape clip = new RoundRectangle2D.Float(0, 0, w, h, 18, 18);
            g2.setClip(clip);
            if (image != null) {
                double scale = Math.min(
                        (double) w / image.getWidth(),
                        (double) h / image.getHeight()
                );

                int dw = (int) Math.round(image.getWidth() * scale);
                int dh = (int) Math.round(image.getHeight() * scale);

                int x = (w - dw) / 2;
                int y = (h - dh) / 2;

                g2.setColor(new Color(255, 240, 247));
                g2.fillRect(0, 0, w, h);

                g2.drawImage(image, x, y, dw, dh, null);
            } else {
                g2.setColor(PANEL2);
                g2.fillRect(0, 0, w, h);
                g2.setColor(TEXT);
                g2.setFont(new Font("SansSerif", Font.BOLD, 14));
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(title, Math.max(8, (w - fm.stringWidth(title)) / 2), h / 2);
            }
            g2.dispose();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            } catch (Exception ignored) {
            }
            new CinemaApp().setVisible(true);
        });
    }
}
