import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;

class LipayonItem {
    String category;
    String id;
    String title;
    String author;
    boolean borrowed;

    public LipayonItem(String category, String title, String author, String id, boolean borrowed) {
        this.category = category;
        this.title = title;
        this.author = author;
        this.id = id;
        this.borrowed = borrowed;
    }
}

public class SystemInfante extends JFrame {
    private static final long serialVersionUID = 1L;

    private final ArrayList<LipayonItem> books = new ArrayList<>();

    private JTextField txtId;
    private JTextField txtTitle;
    private JTextField txtAuthor;
    private JTextField txtCategory;

    private JLabel lblStatus;
    private JLabel lblCount;

    private JTable table;
    private DefaultTableModel tableModel;

    private static final String FILE_NAME = "nowBookFile.txt";
    private boolean dataFileValid = true;
    private String loadError;

    // White Theme Color Palette
    private final Color BG_LIGHT = new Color(248, 249, 250);
    private final Color PANEL_LIGHT = new Color(255, 255, 255);
    private final Color TEXT_DARK = new Color(35, 35, 35);
    private final Color FIELD_BG = new Color(255, 255, 255);
    private final Color BUTTON_BG = new Color(37, 99, 235);
    private final Color BUTTON_HOVER = new Color(29, 78, 216);
    private final Color TABLE_GRID = new Color(220, 220, 220);
    private final Color BORDER_SOFT = new Color(210, 210, 210);
    private final Color ACCENT_BLUE = new Color(59, 130, 246);

    public SystemInfante() {
        setTitle("Library Management System");
        setSize(850, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(12, 12));
        getContentPane().setBackground(BG_LIGHT);
        setResizable(false);

        loadBooksFromFile();

        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new GridLayout(2, 1, 0, 3));
        headerPanel.setBackground(ACCENT_BLUE);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(14, 10, 14, 10));
        JLabel lblHeader = new JLabel("LIBRARY MANAGEMENT SYSTEM", JLabel.CENTER);
        lblHeader.setFont(new Font("Arial", Font.BOLD, 22));
        lblHeader.setForeground(Color.WHITE);
        JLabel lblSubtitle = new JLabel("Manage your books, loans, and collection", JLabel.CENTER);
        lblSubtitle.setFont(new Font("Arial", Font.PLAIN, 12));
        lblSubtitle.setForeground(new Color(230, 240, 255));
        headerPanel.add(lblHeader);
        headerPanel.add(lblSubtitle);
        add(headerPanel, BorderLayout.NORTH);

        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBackground(PANEL_LIGHT);
        leftPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(10, 10, 10, 10),
                BorderFactory.createCompoundBorder(
                        BorderFactory.createTitledBorder(
                                BorderFactory.createLineBorder(BORDER_SOFT, 1),
                                "Book Information",
                                0, 0, new Font("Arial", Font.BOLD, 13), TEXT_DARK),
                        BorderFactory.createEmptyBorder(8, 10, 10, 10))));
        leftPanel.setPreferredSize(new Dimension(280, 0));

        txtId = createStyledTextField();
        txtTitle = createStyledTextField();
        txtAuthor = createStyledTextField();
        txtCategory = createStyledTextField();

        leftPanel.add(createFieldLabel("Category:"));
        leftPanel.add(txtCategory);
        leftPanel.add(Box.createVerticalStrut(8));
        leftPanel.add(createFieldLabel("Book ID:"));
        leftPanel.add(txtId);
        leftPanel.add(Box.createVerticalStrut(8));
        leftPanel.add(createFieldLabel("Book Title:"));
        leftPanel.add(txtTitle);
        leftPanel.add(Box.createVerticalStrut(8));
        leftPanel.add(createFieldLabel("Author:"));
        leftPanel.add(txtAuthor);
        leftPanel.add(Box.createVerticalStrut(15));

        JButton btnAdd = createStyledButton("Add Book");
        JButton btnUpdate = createStyledButton("Update Book");
        JButton btnDisplay = createStyledButton("Refresh/Display");
        JButton btnBorrow = createStyledButton("Borrow Book");
        JButton btnReturn = createStyledButton("Return Book");
        JButton btnSearch = createStyledButton("Search Book");
        JButton btnClear = createStyledButton("Clear Fields");

        JPanel buttonGrid = new JPanel(new GridLayout(4, 2, 6, 6));
        buttonGrid.setBackground(PANEL_LIGHT);
        buttonGrid.add(btnAdd);
        buttonGrid.add(btnUpdate);
        buttonGrid.add(btnDisplay);
        buttonGrid.add(btnBorrow);
        buttonGrid.add(btnReturn);
        buttonGrid.add(btnSearch);
        buttonGrid.add(btnClear);

        leftPanel.add(buttonGrid);
        add(leftPanel, BorderLayout.WEST);

        String[] columnNames = { "Category", "Title", "Author", "Book ID", "Status" };
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(tableModel);
        table.setBackground(FIELD_BG);
        table.setForeground(TEXT_DARK);
        table.setGridColor(TABLE_GRID);
        table.setSelectionBackground(ACCENT_BLUE);
        table.setSelectionForeground(Color.WHITE);
        table.setRowHeight(30);
        table.setIntercellSpacing(new Dimension(0, 1));
        table.setShowVerticalLines(false);
        table.setShowHorizontalLines(true);
        table.setFillsViewportHeight(true);
        table.getTableHeader().setBackground(ACCENT_BLUE);
        table.getTableHeader().setForeground(Color.WHITE);
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
        table.getTableHeader().setPreferredSize(new Dimension(0, 36));
        table.getTableHeader().setReorderingAllowed(false);
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(
                    JTable source, Object value, boolean isSelected,
                    boolean hasFocus, int row, int column) {
                Component cell = super.getTableCellRendererComponent(
                        source, value, isSelected, hasFocus, row, column);
                if (!isSelected) {
                    cell.setBackground(row % 2 == 0 ? Color.WHITE : new Color(244, 247, 251));
                    cell.setForeground(TEXT_DARK);
                }
                setHorizontalAlignment(column == 4 ? JLabel.CENTER : JLabel.LEFT);
                setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
                return cell;
            }
        });

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(BG_LIGHT);
        scrollPane.setBorder(BorderFactory.createLineBorder(BORDER_SOFT));
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(PANEL_LIGHT);
        bottomPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, BORDER_SOFT),
                BorderFactory.createEmptyBorder(10, 14, 10, 14)));

        lblStatus = new JLabel("Status: System Ready");
        lblStatus.setForeground(TEXT_DARK);
        lblStatus.setFont(new Font("Arial", Font.PLAIN, 12));
        lblCount = new JLabel("Total Books: 0");
        lblCount.setForeground(ACCENT_BLUE);
        lblCount.setFont(new Font("Arial", Font.BOLD, 12));

        bottomPanel.add(lblStatus, BorderLayout.WEST);
        bottomPanel.add(lblCount, BorderLayout.EAST);
        add(bottomPanel, BorderLayout.SOUTH);

        refreshTable();
        if (loadError != null) {
            SwingUtilities.invokeLater(() -> showError("Data File Error", loadError));
        }

        btnAdd.addActionListener(e -> addBook());
        btnUpdate.addActionListener(e -> updateBook());
        btnDisplay.addActionListener(e -> {
            refreshTable();
            lblStatus.setText("Status: Displaying all books.");
        });
        btnBorrow.addActionListener(e -> borrowBook());
        btnReturn.addActionListener(e -> returnBook());
        btnSearch.addActionListener(e -> searchBook());
        btnClear.addActionListener(e -> clearFields());

        table.getSelectionModel().addListSelectionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                txtCategory.setText(tableModel.getValueAt(selectedRow, 0).toString());
                txtTitle.setText(tableModel.getValueAt(selectedRow, 1).toString());
                txtAuthor.setText(tableModel.getValueAt(selectedRow, 2).toString());
                txtId.setText(tableModel.getValueAt(selectedRow, 3).toString());
            }
        });

        setVisible(true);
    }

    private JTextField createStyledTextField() {
        JTextField tf = new JTextField();
        tf.setBackground(FIELD_BG);
        tf.setForeground(TEXT_DARK);
        tf.setCaretColor(TEXT_DARK);
        tf.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        tf.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(TABLE_GRID),
                BorderFactory.createEmptyBorder(4, 7, 4, 7)));
        return tf;
    }

    private JLabel createFieldLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setForeground(TEXT_DARK);
        lbl.setFont(new Font("Arial", Font.BOLD, 12));
        return lbl;
    }

    private JButton createStyledButton(String text) {
        JButton btn = new JButton(text);
        btn.setBackground(BUTTON_BG);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setOpaque(true);
        btn.setContentAreaFilled(true);
        btn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BUTTON_HOVER),
                BorderFactory.createEmptyBorder(7, 5, 7, 5)));
        btn.setFont(new Font("Arial", Font.BOLD, 11));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        Color[] targetColor = { BUTTON_BG };
        Timer animation = new Timer(15, null);
        animation.addActionListener(e -> {
            Color currentColor = btn.getBackground();
            Color nextColor = blendColors(currentColor, targetColor[0], 0.25f);
            btn.setBackground(nextColor);

            if (Math.abs(nextColor.getRed() - targetColor[0].getRed()) < 3
                    && Math.abs(nextColor.getGreen() - targetColor[0].getGreen()) < 3
                    && Math.abs(nextColor.getBlue() - targetColor[0].getBlue()) < 3) {
                btn.setBackground(targetColor[0]);
                animation.stop();
            }
        });

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                targetColor[0] = BUTTON_HOVER;
                animation.restart();
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                targetColor[0] = BUTTON_BG;
                btn.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(BUTTON_HOVER),
                        BorderFactory.createEmptyBorder(7, 5, 7, 5)));
                animation.restart();
            }

            @Override
            public void mousePressed(java.awt.event.MouseEvent evt) {
                targetColor[0] = new Color(23, 65, 180);
                btn.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(BUTTON_HOVER),
                        BorderFactory.createEmptyBorder(9, 5, 5, 5)));
                animation.restart();
            }

            @Override
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                targetColor[0] = btn.contains(evt.getPoint()) ? BUTTON_HOVER : BUTTON_BG;
                btn.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(BUTTON_HOVER),
                        BorderFactory.createEmptyBorder(7, 5, 7, 5)));
                animation.restart();
            }
        });

        return btn;
    }

    private Color blendColors(Color current, Color target, float amount) {
        int red = Math.round(current.getRed() + (target.getRed() - current.getRed()) * amount);
        int green = Math.round(current.getGreen() + (target.getGreen() - current.getGreen()) * amount);
        int blue = Math.round(current.getBlue() + (target.getBlue() - current.getBlue()) * amount);
        return new Color(red, green, blue);
    }

    private void loadBooksFromFile() {
        File file = new File(FILE_NAME);
        if (!file.exists())
            return;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            int lineNumber = 0;
            while ((line = reader.readLine()) != null) {
                lineNumber++;
                if (line.trim().isEmpty()) {
                    continue;
                }

                String[] parts = line.split("/", -1);
                if (parts.length != 5
                        || parts[0].trim().isEmpty()
                        || parts[1].trim().isEmpty()
                        || parts[2].trim().isEmpty()
                        || parts[3].trim().isEmpty()
                        || !(parts[4].equalsIgnoreCase("true") || parts[4].equalsIgnoreCase("false"))) {
                    throw new IOException("Invalid book data on line " + lineNumber
                            + ". Expected Category/Title/Author/ID/true-or-false.");
                }

                books.add(new LipayonItem(
                        parts[0].trim(),
                        parts[1].trim(),
                        parts[2].trim(),
                        parts[3].trim(),
                        Boolean.parseBoolean(parts[4].trim())));
            }
        } catch (IOException e) {
            dataFileValid = false;
            loadError = "Could not read " + FILE_NAME + ":\n" + e.getMessage()
                    + "\nThe file was not changed.";
        }
    }

    private boolean saveBooksToFile() {
        if (!dataFileValid) {
            showError("Save Error", "Fix the invalid data in " + FILE_NAME
                    + " before changing books. The file was not overwritten.");
            return false;
        }

        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_NAME))) {
            for (LipayonItem book : books) {
                writer.println(book.category + "/" + book.title + "/" + book.author
                        + "/" + book.id + "/" + book.borrowed);
            }
            if (writer.checkError()) {
                showError("Save Error", "Could not save the book data to " + FILE_NAME + ".");
                return false;
            }
            return true;
        } catch (IOException e) {
            showError("Save Error", "Could not save the book data:\n" + e.getMessage());
            return false;
        }
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        for (LipayonItem book : books) {
            tableModel.addRow(new Object[] {
                    book.category,
                    book.title,
                    book.author,
                    book.id,
                    book.borrowed ? "Borrowed" : "Available"
            });
        }
        updateCount();
    }

    private void addBook() {
        String category = txtCategory.getText().trim();
        String id = txtId.getText().trim();
        String title = txtTitle.getText().trim();
        String author = txtAuthor.getText().trim();

        if (category.isEmpty() || id.isEmpty() || title.isEmpty() || author.isEmpty()) {
            showError("Missing Information", "Enter a category, book ID, title, and author.");
            return;
        }
        if (containsFileDelimiter(category, title, author, id)) {
            showError("Invalid Character", "Book details cannot contain a slash (/).");
            return;
        }

        if (findBook(id) != null) {
            showError("Duplicate Book ID", "That book ID is already in use.");
            return;
        }

        LipayonItem newBook = new LipayonItem(category, title, author, id, false);
        books.add(newBook);
        if (!saveBooksToFile()) {
            books.remove(newBook);
            return;
        }
        refreshTable();
        clearFields();
        lblStatus.setText("Status: Book added successfully!");
    }

    private void updateBook() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow < 0) {
            showError("No Book Selected", "Select a book from the table before updating.");
            return;
        }

        String originalId = tableModel.getValueAt(selectedRow, 3).toString();
        LipayonItem book = findBook(originalId);
        if (book == null) {
            showError("Book Not Found", "The selected book could not be found.");
            return;
        }

        String category = txtCategory.getText().trim();
        String id = txtId.getText().trim();
        String title = txtTitle.getText().trim();
        String author = txtAuthor.getText().trim();

        if (category.isEmpty() || id.isEmpty() || title.isEmpty() || author.isEmpty()) {
            showError("Missing Information", "Enter a category, book ID, title, and author.");
            return;
        }
        if (containsFileDelimiter(category, title, author, id)) {
            showError("Invalid Character", "Book details cannot contain a slash (/).");
            return;
        }

        LipayonItem duplicate = findBook(id);
        if (duplicate != null && duplicate != book) {
            showError("Duplicate Book ID", "That book ID is already in use.");
            return;
        }

        String oldId = book.id;
        String oldCategory = book.category;
        String oldTitle = book.title;
        String oldAuthor = book.author;

        book.category = category;
        book.id = id;
        book.title = title;
        book.author = author;

        if (!saveBooksToFile()) {
            book.category = oldCategory;
            book.id = oldId;
            book.title = oldTitle;
            book.author = oldAuthor;
            return;
        }

        refreshTable();
        clearFields();
        lblStatus.setText("Status: Book updated successfully!");
    }

    private void borrowBook() {
        String id = txtId.getText().trim();
        if (id.isEmpty()) {
            showError("Missing Book ID", "Enter a book ID or select a book from the table.");
            return;
        }

        LipayonItem book = findBook(id);
        if (book == null) {
            showError("Book Not Found", "No book was found with ID \"" + id + "\".");
        } else if (book.borrowed) {
            showError("Book Unavailable", "This book is already borrowed.");
        } else {
            book.borrowed = true;
            if (!saveBooksToFile()) {
                book.borrowed = false;
                return;
            }
            refreshTable();
            lblStatus.setText("Status: Book borrowed successfully!");
        }
    }

    private void returnBook() {
        String id = txtId.getText().trim();
        if (id.isEmpty()) {
            showError("Missing Book ID", "Enter a book ID or select a book from the table.");
            return;
        }

        LipayonItem book = findBook(id);
        if (book == null) {
            showError("Book Not Found", "No book was found with ID \"" + id + "\".");
        } else if (!book.borrowed) {
            showError("Book Already Available", "This book has not been borrowed.");
        } else {
            book.borrowed = false;
            if (!saveBooksToFile()) {
                book.borrowed = true;
                return;
            }
            refreshTable();
            lblStatus.setText("Status: Book returned successfully!");
        }
    }

    private void searchBook() {
        String categorySearch = txtCategory.getText().trim();
        String idSearch = txtId.getText() == null ? "" : txtId.getText().trim();
        String titleSearch = txtTitle.getText() == null ? "" : txtTitle.getText().trim();
        String authorSearch = txtAuthor.getText() == null ? "" : txtAuthor.getText().trim();

        if (categorySearch.isEmpty() && idSearch.isEmpty()
                && titleSearch.isEmpty() && authorSearch.isEmpty()) {
            showError("Missing Search Text", "Enter a category, book ID, title, or author to search.");
            refreshTable();
            return;
        }

        tableModel.setRowCount(0);
        boolean found = false;

        for (LipayonItem book : books) {
            if (containsIgnoreCase(book.category, categorySearch)
                    || containsIgnoreCase(book.id, idSearch)
                    || containsIgnoreCase(book.title, titleSearch)
                    || containsIgnoreCase(book.author, authorSearch)) {
                tableModel.addRow(new Object[] {
                        book.category,
                        book.title,
                        book.author,
                        book.id,
                        book.borrowed ? "Borrowed" : "Available"
                });
                found = true;
            }
        }

        if (found) {
            lblStatus.setText("Status: Search results displayed.");
        } else {
            showError("No Results", "No books matched your search.");
        }

        updateCount();
    }

    private void showError(String title, String message) {
        lblStatus.setText("Status: " + message.replace('\n', ' '));
        JOptionPane.showMessageDialog(this, message, title, JOptionPane.ERROR_MESSAGE);
    }

    private boolean containsIgnoreCase(String value, String search) {
        return !search.isEmpty() && value != null
                && value.toLowerCase().contains(search.toLowerCase());
    }

    private boolean containsFileDelimiter(String... values) {
        for (String value : values) {
            if (value.contains("/") || value.contains("\n") || value.contains("\r")) {
                return true;
            }
        }
        return false;
    }

    private LipayonItem findBook(String id) {
        for (LipayonItem book : books) {
            if (book.id != null && book.id.equalsIgnoreCase(id)) {
                return book;
            }
        }
        return null;
    }

    private void clearFields() {
        txtCategory.setText("");
        txtId.setText("");
        txtTitle.setText("");
        txtAuthor.setText("");
        table.clearSelection();
    }

    private void updateCount() {
        lblCount.setText("Total Books: " + books.size());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JWindow splash = new JWindow();
            JPanel panel = new JPanel(new BorderLayout(0, 12));
            panel.setBackground(Color.WHITE);
            panel.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(210, 220, 235)),
                    BorderFactory.createEmptyBorder(24, 28, 24, 28)));

            JLabel title = new JLabel("LIBRARY MANAGEMENT SYSTEM", JLabel.CENTER);
            title.setFont(new Font("Arial", Font.BOLD, 18));
            title.setForeground(new Color(37, 99, 235));
            JLabel loading = new JLabel("Loading your library...", JLabel.CENTER);
            loading.setFont(new Font("Arial", Font.PLAIN, 13));
            loading.setForeground(new Color(70, 75, 85));
            JProgressBar progress = new JProgressBar();
            progress.setIndeterminate(true);
            progress.setForeground(new Color(37, 99, 235));

            JPanel messagePanel = new JPanel(new GridLayout(2, 1, 0, 5));
            messagePanel.setBackground(Color.WHITE);
            messagePanel.add(title);
            messagePanel.add(loading);
            panel.add(messagePanel, BorderLayout.CENTER);
            panel.add(progress, BorderLayout.SOUTH);
            splash.setContentPane(panel);
            splash.setSize(380, 160);
            splash.setLocationRelativeTo(null);
            splash.setVisible(true);

            Timer timer = new Timer(900, e -> {
                splash.dispose();
                new SystemInfante();
            });
            timer.setRepeats(false);
            timer.start();
        });
    }
}