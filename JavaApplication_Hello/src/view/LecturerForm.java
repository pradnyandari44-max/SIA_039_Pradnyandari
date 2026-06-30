package view;

import controller.LecturerController;
import model.Lecturer;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class LecturerForm extends JFrame {

    private final LecturerController controller = new LecturerController();

    private int    currentPage = 1;
    private String keyword     = "";

    private JTable            table;
    private DefaultTableModel tableModel;
    private JTextField        txtNIDN, txtNama, txtCardID, txtExpertise, txtSearch;
    private JButton           btnSimpan, btnHapus, btnBatal, btnCari, btnNext, btnPrev;
    private JLabel            lblJudul, lblPage;
    private JPanel            panelForm;
    private JScrollPane       scrollPane;

    private String selectedNIDN = null;

    public LecturerForm() {
        initUI();
        loadData();
    }

    private void initUI() {
        setTitle("Manajemen Dosen");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(720, 560);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        lblJudul = new JLabel("MASTER DATA DOSEN", SwingConstants.CENTER);
        lblJudul.setFont(new Font("Arial", Font.BOLD, 18));
        lblJudul.setBorder(BorderFactory.createEmptyBorder(10, 0, 5, 0));
        add(lblJudul, BorderLayout.NORTH);

        panelForm = new JPanel(new GridBagLayout());
        panelForm.setBorder(BorderFactory.createTitledBorder("Input Data Dosen"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 8, 5, 8);
        gbc.fill   = GridBagConstraints.HORIZONTAL;

        txtNIDN      = new JTextField(12);
        txtNama      = new JTextField(20);
        txtCardID    = new JTextField(15);
        txtExpertise = new JTextField(20);

        gbc.gridx=0; gbc.gridy=0; panelForm.add(new JLabel("NIDN"), gbc);
        gbc.gridx=1;              panelForm.add(txtNIDN, gbc);
        gbc.gridx=2;              panelForm.add(new JLabel("Nama"), gbc);
        gbc.gridx=3;              panelForm.add(txtNama, gbc);

        gbc.gridx=0; gbc.gridy=1; panelForm.add(new JLabel("Card ID"), gbc);
        gbc.gridx=1;              panelForm.add(txtCardID, gbc);
        gbc.gridx=2;              panelForm.add(new JLabel("Keahlian"), gbc);
        gbc.gridx=3;              panelForm.add(txtExpertise, gbc);

        btnSimpan = new JButton("Simpan");
        btnHapus  = new JButton("Hapus");
        btnBatal  = new JButton("Batal");
        btnHapus.setForeground(Color.RED);
        JPanel panelBtn = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelBtn.add(btnBatal); panelBtn.add(btnHapus); panelBtn.add(btnSimpan);
        gbc.gridx=0; gbc.gridy=2; gbc.gridwidth=4;
        panelForm.add(panelBtn, gbc);

        add(panelForm, BorderLayout.NORTH);

        String[] cols = {"Card ID", "Nama", "NIDN", "Keahlian"};
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
        List<Lecturer> list = controller.getPage(keyword, currentPage);
        for (Lecturer l : list) {
            tableModel.addRow(new Object[]{l.getIdCard(), l.getName(), l.getNidn(), l.getExpertise()});
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
        txtNIDN.setText(tableModel.getValueAt(row, 2).toString());
        txtExpertise.setText(tableModel.getValueAt(row, 3).toString());
        selectedNIDN = txtNIDN.getText();
        txtNIDN.setEditable(false);
    }

    private void simpan() {
        Lecturer l = new Lecturer(
            txtCardID.getText().trim(), txtNama.getText().trim(),
            txtNIDN.getText().trim(),   txtExpertise.getText().trim()
        );
        String result = selectedNIDN != null ? controller.update(l) : controller.create(l);
        if ("OK".equals(result)) {
            JOptionPane.showMessageDialog(this,
                selectedNIDN != null ? "Data dosen diupdate!" : "Data dosen disimpan!",
                "Sukses", JOptionPane.INFORMATION_MESSAGE);
            clearForm(); loadData();
        } else {
            JOptionPane.showMessageDialog(this, result, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void hapus() {
        if (selectedNIDN == null) {
            JOptionPane.showMessageDialog(this, "Pilih data dosen dulu!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int c = JOptionPane.showConfirmDialog(this, "Hapus dosen NIDN: " + selectedNIDN + "?",
            "Konfirmasi", JOptionPane.YES_NO_OPTION);
        if (c == JOptionPane.YES_OPTION) {
            String res = controller.delete(selectedNIDN);
            if ("OK".equals(res)) {
                JOptionPane.showMessageDialog(this, "Data dihapus!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
                clearForm(); loadData();
            } else {
                JOptionPane.showMessageDialog(this, res, "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void clearForm() {
        txtNIDN.setText(""); txtNIDN.setEditable(true);
        txtNama.setText(""); txtCardID.setText(""); txtExpertise.setText("");
        selectedNIDN = null;
        table.clearSelection();
    }
}