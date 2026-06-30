package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Dashboard extends JFrame {

    public Dashboard() {
        initUI();
    }

    private void initUI() {
        setTitle("SIA - Sistem Informasi Akademik");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 380);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JPanel panelHeader = new JPanel(new BorderLayout());
        panelHeader.setBackground(new Color(33, 97, 140));
        panelHeader.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel lblTitle = new JLabel("SISTEM INFORMASI AKADEMIK", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 20));
        lblTitle.setForeground(Color.WHITE);

        JLabel lblSub = new JLabel("Universitas Pendidikan Ganesha", SwingConstants.CENTER);
        lblSub.setFont(new Font("Arial", Font.PLAIN, 13));
        lblSub.setForeground(new Color(200, 230, 255));

        panelHeader.add(lblTitle, BorderLayout.CENTER);
        panelHeader.add(lblSub,   BorderLayout.SOUTH);
        add(panelHeader, BorderLayout.NORTH);

        JPanel panelMenu = new JPanel(new GridLayout(4, 1, 10, 10));
        panelMenu.setBorder(BorderFactory.createEmptyBorder(20, 60, 20, 60));

        JButton btnMahasiswa  = buatButton("Manajemen Mahasiswa",  new Color(41, 128, 185));
        JButton btnDosen      = buatButton("Manajemen Dosen",       new Color(39, 174, 96));
        JButton btnMatakuliah = buatButton("Manajemen Mata Kuliah", new Color(142, 68, 173));
        JButton btnKRS        = buatButton("Input KRS / Nilai",     new Color(231, 76, 60));

        panelMenu.add(btnMahasiswa);
        panelMenu.add(btnDosen);
        panelMenu.add(btnMatakuliah);
        panelMenu.add(btnKRS);
        add(panelMenu, BorderLayout.CENTER);

        JLabel lblFooter = new JLabel("Anak Agung Istri Pradnyandari / 2515101039 / IlKom Denpasar", SwingConstants.CENTER);
        lblFooter.setFont(new Font("Arial", Font.ITALIC, 11));
        lblFooter.setForeground(Color.GRAY);
        lblFooter.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        add(lblFooter, BorderLayout.SOUTH);

        btnMahasiswa.addActionListener(e  -> new StudentForm().setVisible(true));
        btnDosen.addActionListener(e      -> new LecturerForm().setVisible(true));
        btnMatakuliah.addActionListener(e -> new CourseForm().setVisible(true));
        btnKRS.addActionListener(e        -> new KRSForm().setVisible(true));
    }

    private JButton buatButton(String teks, Color warna) {
        JButton btn = new JButton(teks);
        btn.setFont(new Font("Arial", Font.BOLD, 13));
        btn.setBackground(warna);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createRaisedBevelBorder(),
            BorderFactory.createEmptyBorder(8, 20, 8, 20)
        ));
        btn.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) {
                btn.setBackground(warna.darker());
            }
            @Override public void mouseExited(MouseEvent e) {
                btn.setBackground(warna);
            }
        });
        return btn;
    }

    public static void main(String[] args) {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception ignored) {}

        SwingUtilities.invokeLater(() -> new Dashboard().setVisible(true));
    }
}