package view;

import javax.swing.*;
import java.awt.*;

public class UI extends JFrame {
    private final JPanel titlePanel = new JPanel(new BorderLayout());
    private final JPanel contentPanel = new JPanel(new GridLayout(4, 2, 10, 10));
    private final JPanel configPanel = new JPanel(new GridLayout(5, 2, 5, 5));
    private final JTextField pageSizeField = new JTextField("default value");
    private final JTextField offsetBitsField = new JTextField("default value");
    private final JTextField virtualMemSizeField = new JTextField("default value");
    private final JTextField tlbEntriesField = new JTextField("default value");
    private final JButton validateButton = new JButton("Validate Inputs");
    private final JButton startButton = new JButton("Start Simulation");
    private final JPanel addressPanel = new JPanel(new GridLayout(2, 1, 5, 5));
    private final JPanel scenariosPanel = new JPanel(new GridBagLayout());
    private final JPanel loadInstructionPanel = new JPanel(new BorderLayout());
    private final JComboBox<String> scenarioComboBox = new JComboBox<>(new String[]{"Scenario 1", "Scenario 2", "Scenario 3"});
    private final JButton seeScenario = new JButton("See Instructions");
    private final JPanel tlbPanel = new JPanel(new BorderLayout());
    private final String[] tlbColumnNames = {"Index", "Virtual Page Number", "Physical Page Number"};

    private final JTable tlbTable = new JTable(new Object[10][3], tlbColumnNames);
    private final JButton loadInstruction = new JButton("Load Instruction");
    private final JTextArea instructions = new JTextArea();
    private final JPanel pageTablePanel = new JPanel(new BorderLayout());
    private final String[] pageTableColumnNames = {"Index", "Virtual Page Number", "Physical Page Number"};
    private final JTable pageTable = new JTable(new Object[10][3], pageTableColumnNames);
    private final JPanel infoPanel = new JPanel(new BorderLayout());
    private final JTextArea infoArea = new JTextArea("Please Configure Memory Settings.");
    private final JButton nextStep = new JButton("Next");
    private final JButton fastForward = new JButton("Fast Forward");
    private final JPanel physicalMemoryPanel = new JPanel(new BorderLayout());
    private final String[] memoryColumnNames = {"Physical Page Number", "Content"};
    private final JTable memoryTable = new JTable(new Object[10][2], memoryColumnNames);
    private final JButton resetSimulation = new JButton("Reset Simulation");

    public UI() {
        setUpUI();
        createTitlePanel();
        add(titlePanel, BorderLayout.NORTH);

        createContentPanel();
        add(contentPanel, BorderLayout.CENTER);

        createResetPanel();
    }

    private void setUpUI() {
        setTitle("Virtual Memory Simulator");
        setSize(1500, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
    }

    private void createTitlePanel() {
        JLabel titleLabel = new JLabel("Virtual Memory Simulator", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        titlePanel.add(titleLabel, BorderLayout.NORTH);
    }

    private void createContentPanel() {
        createConfigurationPanel();
        contentPanel.add(configPanel);
        createAddressPanel();
        contentPanel.add(addressPanel);
        createScenariosPanel();
        contentPanel.add(scenariosPanel);
        createTLBPanel();
        contentPanel.add(tlbPanel);
        createLoadPanel();
        contentPanel.add(loadInstructionPanel);
        createPageTable();
        contentPanel.add(pageTablePanel);
        createInfoPanel();
        contentPanel.add(infoPanel);
        createPhysicalMemory();
        contentPanel.add(physicalMemoryPanel);
    }

    private void createConfigurationPanel() {
        configPanel.setBorder(BorderFactory.createTitledBorder("Simulation Configuration"));
        configPanel.add(new JLabel("Virtual Memory Size"));
        configPanel.add(pageSizeField);
        configPanel.add(new JLabel("Physical Memory Size"));
        configPanel.add(offsetBitsField);
        configPanel.add(new JLabel("Number of Offset Bits"));
        configPanel.add(virtualMemSizeField);
        configPanel.add(new JLabel("Number of TLB Entries"));
        configPanel.add(tlbEntriesField);

        configPanel.add(validateButton);
        configPanel.add(startButton);
    }

    private void createAddressPanel() {
        addressPanel.setBorder(BorderFactory.createTitledBorder("Address Information"));
        addressPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        addressPanel.add(new JLabel("Virtual Address:"), gbc);

        gbc.gridx = 1;
        JTextField virtualAddressHex = new JTextField();
        virtualAddressHex.setMinimumSize(new Dimension(250, 25));
        virtualAddressHex.setPreferredSize(new Dimension(250, 25));
        virtualAddressHex.setEditable(false);
        addressPanel.add(virtualAddressHex, gbc);

        // Spacer Row
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 3;
        addressPanel.add(Box.createVerticalStrut(10), gbc);

        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        addressPanel.add(new JLabel("Page Number:"), gbc);

        gbc.gridx = 1;
        addressPanel.add(new JLabel("Offset:"), gbc);

        gbc.gridy = 3;
        gbc.gridx = 0;
        JTextField pageNumberHex = new JTextField();
        pageNumberHex.setMinimumSize(new Dimension(250, 25));
        pageNumberHex.setPreferredSize(new Dimension(250, 25));
        pageNumberHex.setEditable(false);
        addressPanel.add(pageNumberHex, gbc);

        gbc.gridx = 1;
        JTextField offsetHex = new JTextField();
        offsetHex.setMinimumSize(new Dimension(250, 25));
        offsetHex.setPreferredSize(new Dimension(250, 25));
        offsetHex.setEditable(false);
        addressPanel.add(offsetHex, gbc);
    }



    private void createScenariosPanel() {
        scenariosPanel.setBorder(BorderFactory.createTitledBorder("Simulation Scenarios"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        JLabel scenarioLabel = new JLabel("Choose the simulation scenario:");
        scenariosPanel.add(scenarioLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        scenariosPanel.add(scenarioComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;
        scenariosPanel.add(seeScenario, gbc);
    }

    private void createTLBPanel() {
        tlbPanel.setBorder(BorderFactory.createTitledBorder("Translation Lookaside Buffer"));
        tlbPanel.add(new JScrollPane(tlbTable), BorderLayout.CENTER);
    }

    private void createLoadPanel() {
        loadInstructionPanel.setBorder(BorderFactory.createTitledBorder("Load Instructions"));
        instructions.setEditable(false);
        loadInstructionPanel.add(instructions, BorderLayout.CENTER);
        loadInstructionPanel.add(loadInstruction, BorderLayout.SOUTH);
    }

    private void createPageTable() {
        pageTablePanel.setBorder(BorderFactory.createTitledBorder("Page Table"));
        pageTablePanel.add(new JScrollPane(pageTable), BorderLayout.CENTER);
    }

    private void createInfoPanel() {
        infoPanel.setBorder(BorderFactory.createTitledBorder("Simulation Steps"));
        infoArea.setEditable(false);
        infoPanel.add(infoArea, BorderLayout.CENTER);
        JPanel buttonPanel = new JPanel(new GridLayout(1, 4, 10, 10));
        buttonPanel.add(new Label(" "));
        buttonPanel.add(nextStep);
        buttonPanel.add(fastForward);
        buttonPanel.add(new JLabel(" "));
        infoPanel.add(buttonPanel, BorderLayout.SOUTH);
    }

    private void createPhysicalMemory() {
        physicalMemoryPanel.setBorder(BorderFactory.createTitledBorder("Physical Memory"));
        physicalMemoryPanel.add(new JScrollPane(memoryTable), BorderLayout.CENTER);
    }

    private void createResetPanel() {
        JPanel resetPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        resetPanel.add(resetSimulation);
        add(resetPanel, BorderLayout.SOUTH);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            UI frame = new UI();
            frame.setVisible(true);
        });
    }
}
