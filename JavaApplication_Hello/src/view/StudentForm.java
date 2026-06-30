package view;

import controller.StudentController;
import model.Student;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class StudentForm extends JFrame {

    private final StudentController controller = new StudentController();

    private int currentPage = 1;
    private String keyword  = "";

    private JTable       table;
    private DefaultTableModel tableModel;
    private JTextField   txtNIM, txtNama, txtCardID, txtSearch;
    private JComboBox<String> cbProdi;
    private JButton      btnSimpan, btnHapus, btnBatal, btnCari, btnNext, btnPrev;
    private JLabel       lblJudul, lblPage, lblNIM, lblNama, lblCard, lblProdi, lblSearch;
    private JPanel       panelForm, panelSearch, panelNav;
    private JScrollPane  scrollPane;

    private String selectedNIM = null;

    private static final String[] PRODI = {
        "Ilmu Komputer", "Sistem Informasi", "Teknologi Rekayasa PL"
    };

    public StudentForm() {
        initUI();
        loadData();
    }

    private void initUI() {
        setTitle("Manajemen Mahasiswa");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(700, 550);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        lblJudul = new JLabel("MASTER DATA MAHASISWA", SwingConstants.CENTER);
        lblJudul.setFont(new Font("Arial", Font.BOLD, 18));
        lblJudul.setBorder(BorderFactory.createEmptyBorder(10, 0, 5, 0));
        add(lblJudul, BorderLayout.NORTH);

        panelForm = new JPanel(new GridBagLayout());
        panelForm.setBorder(BorderFactory.createTitledBorder("Input Data Mahasiswa"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 8, 5, 8);
        gbc.fill   = GridBagConstraints.HORIZONTAL;

        lblNIM  = new JLabel("NIM");
        lblNama = new JLabel("Nama");
        lblCard = new JLabel("Card ID");
        lblProdi= new JLabel("Prodi");

        txtNIM    = new JTextField(12);
        txtNama   = new JTextField(20);
        txtCardID = new JTextField(15);
        cbProdi   = new JComboBox<>(PRODI);

        btnSimpan = new JButton("Simpan");
        btnHapus  = new JButton("Hapus");
        btnBatal  = new JButton("Batal");
        btnHapus.setForeground(Color.RED);

        gbc.gridx=0; gbc.gridy=0; panelForm.add(lblNIM, gbc);
        gbc.gridx=1;              panelForm.add(txtNIM, gbc);
        gbc.gridx=2;              panelForm.add(lblNama, gbc);
        gbc.gridx=3;              panelForm.add(txtNama, gbc);

        gbc.gridx=0; gbc.gridy=1; panelForm.add(lblCard, gbc);
        gbc.gridx=1;              panelForm.add(txtCardID, gbc);
        gbc.gridx=2;              panelForm.add(lblProdi, gbc);
        gbc.gridx=3;              panelForm.add(cbProdi, gbc);

        JPanel panelBtn = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelBtn.add(btnBatal); panelBtn.add(btnHapus); panelBtn.add(btnSimpan);
        gbc.gridx=0; gbc.gridy=2; gbc.gridwidth=4;
        panelForm.add(panelBtn, gbc);

        add(panelForm, BorderLayout.NORTH);

        panelSearch = new JPanel(new FlowLayout(FlowLayout.LEFT));
        lblSearch = new JLabel("Cari:");
        txtSearch = new JTextField(20);
        btnCari   = new JButton("Cari");
        panelSearch.add(lblSearch);
        panelSearch.add(txtSearch);
        panelSearch.add(btnCari);

        String[] cols = {"Card ID", "Nama", "NIM", "Program Studi"};
        tableModel = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        scrollPane = new JScrollPane(table);

        panelNav = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnPrev  = new JButton("<< Prev");
        btnNext  = new JButton("Next >>");
        lblPage  = new JLabel("Halaman 1");
        panelNav.add(btnPrev); panelNav.add(lblPage); panelNav.add(btnNext);

        JPanel centerPanel = new JPanel(new BorderLayout(5, 5));
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

        table.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) { onTableClick(); }
        });

        txtSearch.addActionListener(e -> { keyword = txtSearch.getText(); currentPage = 1; loadData(); });
    }

    private void loadData() {
        tableModel.setRowCount(0);
        List<Student> list = controller.getPage(keyword, currentPage);
        for (Student s : list) {
            tableModel.addRow(new Object[]{
                s.getIdCard(), s.getName(), s.getNim(), s.getStudyProgram()
            });
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
        txtCardID.setText(tableModel.getValueAt(row, 0).toString());
        txtNama.setText(tableModel.getValueAt(row, 1).toString());
        txtNIM.setText(tableModel.getValueAt(row, 2).toString());
        cbProdi.setSelectedItem(tableModel.getValueAt(row, 3).toString());
        selectedNIM = txtNIM.getText();
        txtNIM.setEditable(false);  // NIM tidak bisa diubah saat edit
    }

    private void simpan() {
        String nim    = txtNIM.getText().trim();
        String nama   = txtNama.getText().trim();
        String cardID = txtCardID.getText().trim();
        String prodi  = cbProdi.getSelectedItem().toString();

        Student s = new Student(cardID, nama, nim, prodi);
        String result;

        if (selectedNIM != null) {
            result = controller.update(s);
        } else {
            result = controller.create(s);
        }

        if ("OK".equals(result)) {
            JOptionPane.showMessageDialog(this,
                selectedNIM != null ? "Data berhasil diupdate!" : "Data berhasil disimpan!",
                "Sukses", JOptionPane.INFORMATION_MESSAGE);
            clearForm();
            loadData();
        } else {
            JOptionPane.showMessageDialog(this, result, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void hapus() {
        if (selectedNIM == null) {
            JOptionPane.showMessageDialog(this, "Pilih data mahasiswa dulu!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this,
            "Yakin hapus mahasiswa dengan NIM: " + selectedNIM + "?",
            "Konfirmasi Hapus", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            String result = controller.delete(selectedNIM);
            if ("OK".equals(result)) {
                JOptionPane.showMessageDialog(this, "Data berhasil dihapus!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
                clearForm();
                loadData();
            } else {
                JOptionPane.showMessageDialog(this, result, "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void clearForm() {
        txtNIM.setText(""); txtNIM.setEditable(true);
        txtNama.setText("");
        txtCardID.setText("");
        cbProdi.setSelectedIndex(0);
        selectedNIM = null;
        table.clearSelection();
    }
}