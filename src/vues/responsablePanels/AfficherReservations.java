package vues.responsablePanels;

import controllers.ResponsableController;
import models.Reservation;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class AfficherReservations extends JPanel {
    private JTable table;
    private DefaultTableModel tableModel;

    public AfficherReservations() {
        setLayout(new BorderLayout());

        tableModel = new DefaultTableModel(new Object[]{"ID", "Date", "Heure de Debut", "Heure de Fin", "Salle", "Ressource", "Demandeur", "Etat", "Action"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 8; // Only the "Action" column is editable
            }
        };
        table = new JTable(tableModel);
        table.getColumnModel().getColumn(8).setCellRenderer(new ButtonRenderer());
        table.getColumnModel().getColumn(8).setCellEditor(new ButtonEditor(new JCheckBox()));

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        chargerReservations();
    }

    public void chargerReservations() {
        try {
            tableModel.setRowCount(0);
            List<Reservation> reservations = ResponsableController.getAll();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            for (Reservation r : reservations) {
                tableModel.addRow(new Object[]{
                        r.getId(),
                        r.getDate().format(formatter),
                        r.getHeureDebut(),
                        r.getHeureFin(),
                        r.getId_Salle(),
                        r.getNomRessource(),
                        r.getId_Utilisateur(),
                        r.getEtat(),
                        "" // Placeholder for buttons
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erreur de chargement: " + e.getMessage());
        }
    }

    // Renderer for the Action column to display buttons
    class ButtonRenderer extends JPanel implements TableCellRenderer {
        public ButtonRenderer() {
            setLayout(new FlowLayout(FlowLayout.CENTER, 5, 0));
            JButton validateButton = new JButton("Valider");
            JButton rejectButton = new JButton("Rejeter");
            add(validateButton);
            add(rejectButton);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            return this;
        }
    }

    // Editor for the Action column to handle button clicks
    class ButtonEditor extends DefaultCellEditor {
        private JPanel panel;
        private JButton validateButton;
        private JButton rejectButton;
        private int row;

        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
            validateButton = new JButton("Valider");
            rejectButton = new JButton("Rejeter");
            panel.add(validateButton);
            panel.add(rejectButton);

            validateButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        int reservationId = (Integer) tableModel.getValueAt(row, 0);
                        String currentEtat = (String) tableModel.getValueAt(row, 7);
                        if (!currentEtat.equals("en attente")) {
                            JOptionPane.showMessageDialog(panel, "Cette réservation a déjà été traitée.");
                            return;
                        }
                        ResponsableController.validerReservation(reservationId);
                        tableModel.setValueAt("Validé", row, 7);
                        JOptionPane.showMessageDialog(panel, "Réservation validée avec succès.");
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(panel, "Erreur lors de la validation: " + ex.getMessage());
                    }
                    fireEditingStopped();
                }
            });

            rejectButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        int reservationId = (Integer) tableModel.getValueAt(row, 0);
                        String currentEtat = (String) tableModel.getValueAt(row, 7);
                        if (!currentEtat.equals("en attente")) {
                            JOptionPane.showMessageDialog(panel, "Cette réservation a déjà été traitée.");
                            return;
                        }
                        ResponsableController.rejeterReservation(reservationId);
                        tableModel.setValueAt("Rejeté", row, 7);
                        JOptionPane.showMessageDialog(panel, "Réservation rejetée avec succès.");
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(panel, "Erreur lors du rejet: " + ex.getMessage());
                    }
                    fireEditingStopped();
                }
            });
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            this.row = row;
            return panel;
        }

        @Override
        public Object getCellEditorValue() {
            return "";
        }
    }
}