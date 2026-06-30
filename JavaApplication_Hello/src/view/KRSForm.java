package view;

import controller.CourseController;
import controller.KRSController;
import controller.LecturerController;
import controller.StudentController;
import model.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class KRSForm extends JFrame {

    private final KRSController     krsCtrl     = new KRSController();
    private final StudentController studentCtrl = new StudentController();
    private final CourseController  courseCtrl  = new CourseController();
    private final LecturerController lecCtrl    = new LecturerController();

    private JComboBox<Student>  cbMahasiswa;
    private JComboBox<Course>   cbMatakuliah;
    private JComboBox<Lecturer> cbDosen;
    private JComboBox<String>   cbSemester;
    private JTextField          txtSikap, txtUTS, txtUAS;
    private JLabel              lblNIM, lblProdi, lblKodeSKS, lblNilaiAkhir, lblGrade, lblPage;
    private JTable              table;
    private DefaultTableModel   tableModel;
    private JButton             btnSimpan, btnHapus, btnBatal, btnFilter;

    private int selectedKrsID = -1;

    public KRSForm() {
        initUI();
        loadComboData();
        loadTable();
    }

    private void initUI() {
        setTitle("Manajemen KRS / Input Nilai");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(850, 620);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JLabel lblJudul = new JLabel("KARTU RENCANA STUDI (KRS)", SwingConstants.CENTER);
        lblJudul.setFont(new Font("Arial", Font.BOLD, 18));
        lblJudul.setBorder(BorderFactory.createEmptyBorder(10, 0, 5, 0));
        add(lblJudul, BorderLayout.NORTH);

        JPanel panelTop = new JPanel(new GridLayout(1, 2, 10, 0));
        panelTop.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        JPanel pMhs = new JPanel(new GridBagLayout());
        pMhs.setBorder(BorderFactory.createTitledBorder("Identitas Mahasiswa"));
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(4, 6, 4, 6);
        g.fill   = GridBagConstraints.HORIZONTAL;

        cbMahasiswa = new JComboBox<>();
        lblNIM   = new JLabel("-");
        lblProdi = new JLabel("-");

        g.gridx=0; g.gridy=0; pMhs.add(new JLabel("Mahasiswa:"), g);
        g.gridx=1;            pMhs.add(cbMahasiswa, g);
        g.gridx=0; g.gridy=1; pMhs.add(new JLabel("NIM:"), g);
        g.gridx=1;            pMhs.add(lblNIM, g);
        g.gridx=0; g.gridy=2; pMhs.add(new JLabel("Prodi:"), g);
        g.gridx=1;            pMhs.add(lblProdi, g);

        JPanel pKRS = new JPanel(new GridBagLayout());
        pKRS.setBorder(BorderFactory.createTitledBorder("Data Mata Kuliah & Nilai"));

        cbMatakuliah   = new JComboBox<>();
        cbDosen        = new JComboBox<>();
        cbSemester     = new JComboBox<>(new String[]{"1","2","3","4","5","6","7","8"});
        txtSikap       = new JTextField("0", 6);
        txtUTS         = new JTextField("0", 6);
        txtUAS         = new JTextField("0", 6);
        lblKodeSKS     = new JLabel("-");
        lblNilaiAkhir  = new JLabel("-");
        lblGrade       = new JLabel("-");
        lblGrade.setFont(new Font("Arial", Font.BOLD, 14));

        g.gridx=0; g.gridy=0; pKRS.add(new JLabel("Mata Kuliah:"), g);
        g.gridx=1;            pKRS.add(cbMatakuliah, g);
        g.gridx=0; g.gridy=1; pKRS.add(new JLabel("Kode/SKS:"), g);
        g.gridx=1;            pKRS.add(lblKodeSKS, g);
        g.gridx=0; g.gridy=2; pKRS.add(new JLabel("Dosen Pengampu:"), g);
        g.gridx=1;            pKRS.add(cbDosen, g);
        g.gridx=0; g.gridy=3; pKRS.add(new JLabel("Semester:"), g);
        g.gridx=1;            pKRS.add(cbSemester, g);

        JPanel pNilai = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 2));
        pNilai.add(new JLabel("Sikap:")); pNilai.add(txtSikap);
        pNilai.add(new JLabel("UTS:"));   pNilai.add(txtUTS);
        pNilai.add(new JLabel("UAS:"));   pNilai.add(txtUAS);
        g.gridx=0; g.gridy=4; pKRS.add(new JLabel("Nilai:"), g);
        g.gridx=1;            pKRS.add(pNilai, g);

        g.gridx=0; g.gridy=5; pKRS.add(new JLabel("Nilai Akhir:"), g);
        g.gridx=1;            pKRS.add(lblNilaiAkhir, g);
        g.gridx=0; g.gridy=6; pKRS.add(new JLabel("Grade:"), g);
        g.gridx=1;            pKRS.add(lblGrade, g);

        panelTop.add(pMhs);
        panelTop.add(pKRS);

        btnSimpan = new JButton("Simpan KRS");
        btnHapus  = new JButton("Hapus");
        btnBatal  = new JButton("Batal");
        JButton btnHitung = new JButton("Hitung");
        btnHapus.setForeground(Color.RED);

        JPanel panelBtn = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelBtn.add(btnBatal); panelBtn.add(btnHapus);
        panelBtn.add(btnHitung); panelBtn.add(btnSimpan);

        JPanel panelNorth = new JPanel(new BorderLayout());
        panelNorth.add(panelTop,  BorderLayout.CENTER);
        panelNorth.add(panelBtn,  BorderLayout.SOUTH);
        add(panelNorth, BorderLayout.NORTH);

        String[] cols = {"ID","Mahasiswa","NIM","Mata Kuliah","SKS",
                         "Sikap","UTS","UAS","Nilai Akhir","Grade","Dosen","Sem"};
        tableModel = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 11));
 
        table.getColumnModel().getColumn(0).setMinWidth(0);
        table.getColumnModel().getColumn(0).setMaxWidth(0);

        JScrollPane scroll = new JScrollPane(table);

        JPanel panelFilter = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JComboBox<String> cbFilterMhs = new JComboBox<>();
        cbFilterMhs.addItem("-- Semua Mahasiswa --");
        btnFilter = new JButton("Tampilkan");
        panelFilter.add(new JLabel("Filter:")); panelFilter.add(cbFilterMhs); panelFilter.add(btnFilter);

        JPanel centerPanel = new JPanel(new BorderLayout(5,5));
        centerPanel.add(panelFilter, BorderLayout.NORTH);
        centerPanel.add(scroll,      BorderLayout.CENTER);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
        add(centerPanel, BorderLayout.CENTER);

        cbMahasiswa.addActionListener(e -> {
            Student s = (Student) cbMahasiswa.getSelectedItem();
            if (s != null) {
                lblNIM.setText(s.getNim());
                lblProdi.setText(s.getStudyProgram());
            }
        });

        cbMatakuliah.addActionListener(e -> {
            Course co = (Course) cbMatakuliah.getSelectedItem();
            if (co != null) lblKodeSKS.setText(co.getCode() + " | " + co.getSks() + " SKS");
        });

        btnHitung.addActionListener(e -> hitungPreview());
        btnSimpan.addActionListener(e -> simpan());
        btnHapus.addActionListener(e  -> hapus());
        btnBatal.addActionListener(e  -> clearForm());

        table.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) { onTableClick(); }
        });
    }

    private void loadComboData() {
        cbMahasiswa.removeAllItems();
        for (Student s : studentCtrl.getAll()) cbMahasiswa.addItem(s);

        cbMatakuliah.removeAllItems();
        for (Course co : courseCtrl.getAll()) cbMatakuliah.addItem(co);

        cbDosen.removeAllItems();
        for (Lecturer l : lecCtrl.getAll()) cbDosen.addItem(l);
    }

    private void loadTable() {
        tableModel.setRowCount(0);
        List<KRS> list = krsCtrl.getAll();
        for (KRS k : list) {
            tableModel.addRow(new Object[]{
                k.getKrsID(),
                k.getStudent().getName(),
                k.getStudent().getNim(),
                k.getCourse().getCourseName(),
                k.getCourse().getSks(),
                k.getNilaiSikap(),
                k.getNilaiUTS(),
                k.getNilaiUAS(),
                String.format("%.2f", k.getNilaiAkhir()),
                k.getGrade(),
                k.getLecturer().getName(),
                k.getSemester()
            });
        }
    }

    private void hitungPreview() {
        try {
            double sikap = Double.parseDouble(txtSikap.getText().trim());
            double uts   = Double.parseDouble(txtUTS.getText().trim());
            double uas   = Double.parseDouble(txtUAS.getText().trim());
            double akhir = KRS.hitungNilaiAkhir(sikap, uts, uas);
            String grade = KRS.hitungGrade(akhir);
            lblNilaiAkhir.setText(String.format("%.2f", akhir));
            lblGrade.setText(grade);

            switch (grade) {
                case "A": lblGrade.setForeground(new Color(0, 150, 0)); break;
                case "B": lblGrade.setForeground(new Color(0, 100, 200)); break;
                case "C": lblGrade.setForeground(new Color(200, 150, 0)); break;
                default:  lblGrade.setForeground(Color.RED);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Nilai harus berupa angka!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void simpan() {
        try {
            Student  s  = (Student)  cbMahasiswa.getSelectedItem();
            Course   co = (Course)   cbMatakuliah.getSelectedItem();
            Lecturer l  = (Lecturer) cbDosen.getSelectedItem();
            int sem     = Integer.parseInt(cbSemester.getSelectedItem().toString());
            double sikap = Double.parseDouble(txtSikap.getText().trim());
            double uts   = Double.parseDouble(txtUTS.getText().trim());
            double uas   = Double.parseDouble(txtUAS.getText().trim());

            KRS krs;
            String result;

            if (selectedKrsID > 0) {
                krs = new KRS(selectedKrsID, s, co, l, sikap, uts, uas, sem);
                result = krsCtrl.update(krs);
            } else {
                krs = new KRS(s, co, l, sikap, uts, uas, sem);
                result = krsCtrl.create(krs);
            }

            if ("OK".equals(result)) {
                lblNilaiAkhir.setText(String.format("%.2f", krs.getNilaiAkhir()));
                lblGrade.setText(krs.getGrade());
                JOptionPane.showMessageDialog(this,
                    "KRS disimpan! Nilai Akhir: " + String.format("%.2f", krs.getNilaiAkhir()) +
                    " | Grade: " + krs.getGrade(),
                    "Sukses", JOptionPane.INFORMATION_MESSAGE);
                clearForm();
                loadTable();
            } else {
                JOptionPane.showMessageDialog(this, result, "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Nilai harus berupa angka!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onTableClick() {
        int row = table.getSelectedRow();
        if (row < 0) return;
        selectedKrsID = (int) tableModel.getValueAt(row, 0);

        String nim = tableModel.getValueAt(row, 2).toString();
        for (int i = 0; i < cbMahasiswa.getItemCount(); i++) {
            if (cbMahasiswa.getItemAt(i).getNim().equals(nim)) {
                cbMahasiswa.setSelectedIndex(i); break;
            }
        }

        String mkNama = tableModel.getValueAt(row, 3).toString();
        for (int i = 0; i < cbMatakuliah.getItemCount(); i++) {
            if (cbMatakuliah.getItemAt(i).getCourseName().equals(mkNama)) {
                cbMatakuliah.setSelectedIndex(i); break;
            }
        }

        txtSikap.setText(tableModel.getValueAt(row, 5).toString());
        txtUTS.setText(tableModel.getValueAt(row, 6).toString());
        txtUAS.setText(tableModel.getValueAt(row, 7).toString());
        lblNilaiAkhir.setText(tableModel.getValueAt(row, 8).toString());
        lblGrade.setText(tableModel.getValueAt(row, 9).toString());
        cbSemester.setSelectedItem(tableModel.getValueAt(row, 11).toString());
    }

    private void hapus() {
        if (selectedKrsID <= 0) {
            JOptionPane.showMessageDialog(this, "Pilih data KRS dulu!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int c = JOptionPane.showConfirmDialog(this, "Hapus KRS ini?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
        if (c == JOptionPane.YES_OPTION) {
            String res = krsCtrl.delete(selectedKrsID);
            if ("OK".equals(res)) {
                JOptionPane.showMessageDialog(this, "Data KRS dihapus!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
                clearForm(); loadTable();
            } else {
                JOptionPane.showMessageDialog(this, res, "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void clearForm() {
        selectedKrsID = -1;
        if (cbMahasiswa.getItemCount() > 0) cbMahasiswa.setSelectedIndex(0);
        if (cbMatakuliah.getItemCount() > 0) cbMatakuliah.setSelectedIndex(0);
        if (cbDosen.getItemCount() > 0) cbDosen.setSelectedIndex(0);
        cbSemester.setSelectedIndex(0);
        txtSikap.setText("0"); txtUTS.setText("0"); txtUAS.setText("0");
        lblNilaiAkhir.setText("-"); lblGrade.setText("-");
        lblGrade.setForeground(Color.BLACK);
        table.clearSelection();
    }
}