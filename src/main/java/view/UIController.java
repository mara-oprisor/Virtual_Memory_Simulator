package view;

import logic.ReplacementManager;
import logic.ScenariosManager;
import logic.SimulationManager;
import logic.state.IdleState;
import logic.state.State;
import logic.state.TLBSearchState;
import model.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.HashMap;
import java.util.Objects;

public class UIController {
    private final UI ui;
    private final SimulationManager simulationManager;
    private final ScenariosManager scenariosManager;
    private final ReplacementManager replacementManager;
    private State currentState;

    public UIController(UI ui, SimulationManager simulationManager) {
        this.ui = ui;
        this.simulationManager = simulationManager;
        this.scenariosManager = new ScenariosManager(simulationManager);
        this.replacementManager = new ReplacementManager(simulationManager);
        this.currentState = new IdleState();
        validateInput();
        startSimulation();
        generateScenarios();
        loadInstructionToSimulation();
        nextStep();
        resetSimulation();
    }

    private HashMap<String, Integer> getInputData() {
        HashMap<String, Integer> inputData = new HashMap<>();
        int physicalMemSize, offsetBits, vmSize, tlbEntries;

        try {
            physicalMemSize = Integer.parseInt(ui.getPhysicalMemSizeField().getText().trim());
            inputData.put("physMem", physicalMemSize);
        } catch (NumberFormatException e) {
            System.out.println("Physical Memory Size is not an integer value");
        }

        try {
            offsetBits = Integer.parseInt(ui.getOffsetBitsField().getText().trim());
            inputData.put("offsetBits", offsetBits);
        } catch (NumberFormatException e) {
            System.out.println("The Number of Offset Bits is not an integer value");
        }

        try {
            vmSize = Integer.parseInt(ui.getVirtualMemSizeField().getText());
            inputData.put("vmSize", vmSize);
        } catch (NumberFormatException e) {
            System.out.println("Virtual Memory Size is not an integer value");
        }

        try {
            tlbEntries = Integer.parseInt(ui.getTlbEntriesField().getText());
            inputData.put("tlbEntries", tlbEntries);
        } catch (NumberFormatException e) {
            System.out.println("The Number of TLB Entries is not an integer value");
        }

        return inputData;
    }

    private void validateInput() {
        ui.getValidateButton().addActionListener(e -> {
            HashMap<String, Integer> inputData = getInputData();
            boolean isValid = true;
            StringBuilder errorMessage = new StringBuilder("Please correct the following errors:\n");

            if (!inputData.containsKey("vmSize")) {
                String vmSizeText = ui.getVirtualMemSizeField().getText().trim();
                if (vmSizeText.isEmpty()) {
                    errorMessage.append("- Virtual Memory Size field cannot be empty.\n");
                } else {
                    errorMessage.append("- Virtual Memory Size must be an integer.\n");
                }
                isValid = false;
            } else if (inputData.get("vmSize") <= 0) {
                errorMessage.append("- Virtual Memory Size must be positive.\n");
                isValid = false;
            }


            if (!inputData.containsKey("physMem")) {
                String pageSizeText = ui.getPhysicalMemSizeField().getText().trim();
                if (pageSizeText.isEmpty()) {
                    errorMessage.append("- Physical Memory Size field cannot be empty.\n");
                } else {
                    errorMessage.append("- Physical Memory Size must be an integer.\n");
                }
                isValid = false;
            } else if (inputData.get("physMem") <= 0) {
                errorMessage.append("- Physical Memory Size must be positive.\n");
                isValid = false;
            }


            if (!inputData.containsKey("offsetBits")) {
                String offsetBitsText = ui.getOffsetBitsField().getText().trim();
                if (offsetBitsText.isEmpty()) {
                    errorMessage.append("- Offset Bits field cannot be empty.\n");
                } else {
                    errorMessage.append("- Offset Bits must be an integer.\n");
                }
                isValid = false;
            } else if (inputData.get("offsetBits") <= 0) {
                errorMessage.append("- Offset Bits must be positive.\n");
                isValid = false;
            }


            if (!inputData.containsKey("tlbEntries")) {
                String tlbEntriesText = ui.getTlbEntriesField().getText().trim();
                if (tlbEntriesText.isEmpty()) {
                    errorMessage.append("- Number of TLB Entries field cannot be empty.\n");
                } else {
                    errorMessage.append("- Number of TLB Entries must be an integer.\n");
                }
                isValid = false;
            } else if (inputData.get("tlbEntries") <= 0) {
                errorMessage.append("- Number of TLB Entries must be positive.\n");
                isValid = false;
            }


            if (isValid) {
                JOptionPane.showMessageDialog(ui, "All inputs are valid! You can start the simulation!", "Validation Successful", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(ui, errorMessage.toString(), "Validation Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    private void startSimulation() {
        ui.getStartButton().addActionListener(e -> {
            HashMap<String, Integer> inputData = getInputData();
            String startInfo = simulationManager.initialiseSimulation(inputData);
            ui.getInfoArea().setText(startInfo);
            fillPageTable();
            fillTLB();
            fillPhysicalMemoryTable();
        });
    }

    public void fillPageTable() {
        PageTable pageTable = simulationManager.getPageTable();
        String[] columnNames = {"Index", "Virtual Page Number", "Physical Page Number"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);

        for (PageTableEntry entry : pageTable.getEntries()) {
            Object[] row = {
                    entry.getIndex(),
                    entry.getVirtualPageNr(),
                    entry.getPhysicalPageNr()
            };
            model.addRow(row);
        }

        ui.getPageTable().setModel(model);
    }

    private void fillTLB() {
        TLB tlb = simulationManager.getTlb();
        String[] columnNames = {"Index", "Virtual Page Number", "Physical Page Number"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);

        for (PageTableEntry entry : tlb.getEntries()) {
            Object[] row = {
                    entry.getIndex(),
                    entry.getVirtualPageNr(),
                    entry.getPhysicalPageNr()
            };
            model.addRow(row);
        }

        ui.getTlbTable().setModel(model);
    }

    public void fillPhysicalMemoryTable() {
        PhysicalMemory physicalMemory = simulationManager.getPhysicalMemory();

        String[] columnNames = {"Physical Page Number", "Contents"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);

        for (int i = 0; i < physicalMemory.getMemory().size(); i++) {
            Object[] row = {
                    i,
                    physicalMemory.getMemory().get(i)
            };
            model.addRow(row);
        }

        ui.getMemoryTable().setModel(model);
    }

    private void generateScenarios() {
        ui.getSeeScenario().addActionListener(e -> {
            int typeInstruction = -1;
            if (Objects.equals(String.valueOf(ui.getScenarioComboBox().getSelectedItem()), "Find in TLB")) {
                typeInstruction = 0;
            } else if (Objects.equals(String.valueOf(ui.getScenarioComboBox().getSelectedItem()), "Find in PageTable")) {
                typeInstruction = 1;
            } else if (Objects.equals(String.valueOf(ui.getScenarioComboBox().getSelectedItem()), "Find on Disk")) {
                typeInstruction = 2;
            }

            String instructions = scenariosManager.generateInstructions(typeInstruction);
            ui.getInstructions().setText(instructions);

            scenariosManager.setInstructions(instructions);
        });
    }

    private void loadInstructionToSimulation() {
        ui.getLoadInstruction().addActionListener(e -> {
            currentState = new TLBSearchState();
            VirtualAddress virtualAddress = scenariosManager.loadInstruction();
            if (virtualAddress != null) {
                ui.getVirtualAddressHex().setText(virtualAddress.getValue());
                ui.getVirtualPageNumber().setText(String.valueOf(virtualAddress.getPageNumber()));
                ui.getVirtualOffset().setText(String.valueOf(virtualAddress.getOffset()));

            } else {
                JOptionPane.showMessageDialog(ui, "There are no more instructions. Please load another set.", "Error - load instructions", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    private void nextStep() {
        ui.getNextStep().addActionListener(e -> {
            currentState.execute(this);
        });
    }

    private void resetSimulation() {
        ui.getResetSimulation().addActionListener(e -> {
            ui.getVirtualMemSizeField().setText("8");
            ui.getPhysicalMemSizeField().setText("6");
            ui.getOffsetBitsField().setText("2");
            ui.getTlbEntriesField().setText("5");

            ui.getVirtualAddressHex().setText("");
            ui.getVirtualPageNumber().setText("");
            ui.getVirtualOffset().setText("");

            ui.getPhysicalAddressHex().setText("");
            ui.getPhysicalPageNumber().setText("");
            ui.getPhysicalOffset().setText("");

            ui.getInfoArea().setForeground(Color.BLACK);
            ui.getInfoArea().setText("Please Configure Memory Settings.");

            ui.resetTable(ui.getTlbTable(), new Object[10][3], new String[]{"Index", "Virtual Page Number", "Physical Page Number"});
            ui.resetTable(ui.getPageTable(), new Object[10][3], new String[]{"Index", "Virtual Page Number", "Physical Page Number"});
            ui.resetTable(ui.getMemoryTable(), new Object[10][2], new String[]{"Physical Page Number", "Contents"});

            ui.getInstructions().setText("");

            currentState = new IdleState();

            ui.resetHighlights(ui.getPageTable());
            ui.resetHighlights(ui.getTlbTable());
            ui.resetHighlights(ui.getMemoryTable());
            ui.getTlbTable().setBackground(Color.WHITE);
            ui.getVirtualPageNumber().setBackground(Color.WHITE);
            ui.getPageTable().setBackground(Color.WHITE);
        });
    }

    public UI getUi() {
        return ui;
    }

    public void setCurrentState(State currentState) {
        this.currentState = currentState;
    }

    public SimulationManager getSimulationManager() {
        return simulationManager;
    }

    public ScenariosManager getScenariosManager() {
        return scenariosManager;
    }

    public ReplacementManager getReplacementManager() {
        return replacementManager;
    }
}
