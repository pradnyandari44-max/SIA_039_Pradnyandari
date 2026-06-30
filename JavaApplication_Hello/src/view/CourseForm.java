package view;

import controller.CourseController;
import model.Course;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class CourseForm extends JFrame {

    private final CourseController controller = new CourseController();

    private int    currentPage = 1;
    private String keyword     = "";

    private JTable            table;
    private DefaultTableModel tableModel;
    private JTextField        txtKode, txtNamaMK, txtSKS, txtSemester, txtSearch;
    private JButton           btnSimpan, btnHapus, btnBatal, btnCari, btnNext, btnPrev;
    private JLabel            lblJudul, lblPage;
    private JScrollPane       scrollPane;

    private String selectedCode = null;

    public CourseForm() {
        initUI();
        loadData();
    }

    private void initUI() {
        setTitle("Manajemen Mata Kuliah");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(700, 520);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        lblJudul = new JLabel("MASTER DATA MATA KULIAH", SwingConstants.CENTER);
        lblJudul.setFont(new Font("Arial", Font.BOLD, 18));
        lblJudul.setBorder(BorderFactory.createEmptyBorder(10, 0, 5, 0));
        add(lblJudul, BorderLayout.NORTH);

        JPanel panelForm = new JPanel(new GridBagLayout());
        panelForm.setBorder(BorderFactory.createTitledBorder("Input Data Mata Kuliah"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 8, 5, 8);
        gbc.fill   = GridBagConstraints.HORIZONTAL;

        txtKode     = new JTextField(10);
        txtNamaMK   = new JTextField(20);
        txtSKS      = new JTextField(5);
        txtSemester = new JTextField(5);

        gbc.gridx=0; gbc.gridy=0; panelForm.add(new JLabel("Kode MK"), gbc);
        gbc.gridx=1;              panelForm.add(txtKode, gbc);
        gbc.gridx=2;              panelForm.add(new JLabel("Nama MK"), gbc);
        gbc.gridx=3;              panelForm.add(txtNamaMK, gbc);

        gbc.gridx=0; gbc.gridy=1; panelForm.add(new JLabel("SKS"), gbc);
        gbc.gridx=1;              panelForm.add(txtSKS, gbc);
        gbc.gridx=2;              panelForm.add(new JLabel("Semester"), gbc);
        gbc.gridx=3;              panelForm.add(txtSemester, gbc);

        btnSimpan = new JButton("Simpan");
        btnHapus  = new JButton("Hapus");
        btnBatal  = new JButton("Batal");
        btnHapus.setForeground(Color.RED);
        JPanel panelBtn = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelBtn.add(btnBatal); panelBtn.add(btnHapus); panelBtn.add(btnSimpan);
        gbc.gridx=0; gbc.gridy=2; gbc.gridwidth=4;
        panelForm.add(panelBtn, gbc);

        add(panelForm, BorderLayout.NORTH);

        String[] cols = {"Kode", "Nama Mata Kuliah", "SKS", "Semester"};
        tableModel = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        scrollPane = new JScrollPane(table);

        JPanel panelSearch = new JPanel(new FlowLayout(FlowLayout.LEFT));
        txtSearch = new JTextField(20);
        btnCari   = new JButton("Cari");
        panelSearch.add(new JLabel("Cari:")); panelSearch.add(txtSearch); panelSearch.add(btnCari);

        JPanel panelNav = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnPrev = new JButton("<< Prev");
        btnNext = new JButton("Next >>");
        lblPage = new JLabel("Halaman 1");
        panelNav.add(btnPrev); panelNav.add(lblPage); panelNav.add(btnNext);

        JPanel centerPanel = new JPanel(new BorderLayout(5,5));
        centerPanel.add(panelSearch, BorderLayout.NORTH);
        centerPanel.add(scrollPane,  BorderLayout.CENTER);
        centerPanel.add(panelNav,    BorderLayout.SOUTH);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        add(centerPanel, BorderLayout.CENTER);

        btnSimpan.addActionListener(e -> simpan());
        btnHapus.addActionListener(e  -> hapus());
        btnBatal.addActionListener(e  -> clearForm());
        btnCari.addActionListener(e   -> { keyword = txtSearch.getText(); currentPage = 1; loadData(); });
        btnNext.addActionListener(e   -> { currentPage++; loadData(); });
        btnPrev.addActionListener(e   -> { if (currentPage > 1) { currentPage--; loadData(); } });
        txtSearch.addActionListener(e -> { keyword = txtSearch.getText(); currentPage = 1; loadData(); });
        table.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) { onTableClick(); }
        });
    }

    private void loadData() {
        tableModel.setRowCount(0);
        List<Course> list = controller.getPage(keyword, currentPage);
        for (Course co : list) {
            tableModel.addRow(new Object[]{co.getCode(), co.getCourseName(), co.getSks(), co.getSemester()});
        }
        int total = controller.getTotalPages(keyword);
        if (currentPage > total) currentPage = total;
        lblPage.setText("Halaman " + currentPage + " / " + total);
        btnPrev.setEnabled(currentPage > 1);
        btnNext.setEnabled(currentPage < total);
    }

    private void onTableClick() {
        int row = table.getSelectedRow();
        if (row < 0) return;
        txtKode.setText(tableModel.getValueAt(row, 0).toString());
        txtNamaMK.setText(tableModel.getValueAt(row, 1).toString());
        txtSKS.setText(tableModel.getValueAt(row, 2).toString());
        txtSemester.setText(tableModel.getValueAt(row, 3).toString());
        selectedCode = txtKode.getText();
        txtKode.setEditable(false);
    }

    private void simpan() {
        try {
            String kode = txtKode.getText().trim();
            String nama = txtNamaMK.getText().trim();
            int sks  = Integer.parseInt(txtSKS.getText().trim());
            int sem  = Integer.parseInt(txtSemester.getText().trim());

            Course co = new Course(kode, nama, sks, sem);
            String result = selectedCode != null ? controller.update(co) : controller.create(co);
            if ("OK".equals(result)) {
                JOptionPane.showMessageDialog(this, "Data mata kuliah disimpan!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
                clearForm(); loadData();
            } else {
                JOptionPane.showMessageDialog(this, result, "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "SKS dan Semester harus angka!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void hapus() {
        if (selectedCode == null) {
            JOptionPane.showMessageDialog(this, "Pilih mata kuliah dulu!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int c = JOptionPane.showConfirmDialog(this, "Hapus MK: " + selectedCode + "?",
            "Konfirmasi", JOptionPane.YES_NO_OPTION);
        if (c == JOptionPane.YES_OPTION) {
            String res = controller.delete(selectedCode);
            if ("OK".equals(res)) {
                JOptionPane.showMessageDialog(this, "Data dihapus!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
                clearForm(); loadData();
            } else {
                JOptionPane.showMessageDialog(this, res, "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void clearForm() {
        txtKode.setText(""); txtKode.setEditable(true);
        txtNamaMK.setText(""); txtSKS.setText(""); txtSemester.setText("");
        selectedCode = null;
        table.clearSelection();
    }
}