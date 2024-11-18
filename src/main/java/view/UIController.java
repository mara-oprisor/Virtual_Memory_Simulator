package view;

import logic.SimulationManager;
import model.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class UIController {
    private final UI ui;
    private final SimulationManager simulationManager;
    public UIController(UI ui, SimulationManager simulationManager) {
        this.ui = ui;
        this.simulationManager = simulationManager;
        validateInput();
        startSimulation();
        generateScenarios();
        loadInstructionToSimulation();
        nextStep();
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

    private void fillPageTable() {
        PageTable pageTable = simulationManager.getPageTable();
        String[] columnNames = {"Index", "Virtual Page Number", "Physical Page Number"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);

        for(PageTableEntry entry : pageTable.getEntries()) {
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

        for(PageTableEntry entry : tlb.getEntries()) {
            Object[] row = {
                    entry.getIndex(),
                    entry.getVirtualPageNr(),
                    entry.getPhysicalPageNr()
            };
            model.addRow(row);
        }

        ui.getTlbTable().setModel(model);
    }

    private void fillPhysicalMemoryTable() {
        PhysicalMemory physicalMemory = simulationManager.getPhysicalMemory();

        String[] columnNames = {"Physical Page Number", "Contents"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);

        for(int i = 0; i < physicalMemory.getMemory().size(); i++) {
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
            List<Integer> addresses = new ArrayList<>();
            if(Objects.equals(String.valueOf(ui.getScenarioComboBox().getSelectedItem()), "Find in TLB")) {
                addresses.add(simulationManager.generateMappedAddress(true));
                addresses.add(simulationManager.generateMappedAddress(true));
            } else if(Objects.equals(String.valueOf(ui.getScenarioComboBox().getSelectedItem()), "Find in PageTable")) {
                System.out.println("Next");
            } else if(Objects.equals(String.valueOf(ui.getScenarioComboBox().getSelectedItem()), "Find on Disk")){
                System.out.println("Next");
            }

            displayScenarios(addresses);
        });
    }

    private void displayScenarios(List<Integer> addresses) {
        boolean isLoadOperation = true;
        StringBuilder sb = new StringBuilder();
        for(Integer address: addresses) {
            String addressHex = String.format("0x%02X", address);
            if(isLoadOperation) {
                sb.append("load R0, ").append(addressHex).append("\n");
                isLoadOperation = false;
            } else {
                sb.append("store R0, ").append(addressHex).append("\n");
                isLoadOperation = true;
            }
        }

        ui.getInstructions().setText(String.valueOf(sb));
    }

    private void loadInstructionToSimulation() {
        ui.getLoadInstruction().addActionListener(e -> {
            if(!ui.getInstructions().getText().isEmpty()) {
                String[] instructions = ui.getInstructions().getText().split("\n");
                simulationManager.setInstructions(List.of(instructions));

                VirtualAddress virtualAddress = simulationManager.getNextAddress();
                ui.getVirtualAddressHex().setText(virtualAddress.getValue());
                ui.getPageNumber().setText(String.valueOf(virtualAddress.getPageNumber()));
                ui.getOffset().setText(String.valueOf(virtualAddress.getOffset()));

            } else {
                JOptionPane.showMessageDialog(ui, "There are no more instructions. Please load another set.", "Error - load instructions", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    private int currentStep = 0;
    private void nextStep() {
        ui.getNextStep().addActionListener(e -> handleNextStep());
    }

    int pageNr = 0;
    boolean pageInTLB;
    int physicalPageNumber = 0;
    int offset = 0;
    private void handleNextStep() {

        switch (currentStep) {
            case 0 -> {
                ui.getInfoArea().setText("\nWe search for the page in the TLB.");
                ui.getTlbTable().setBackground(Color.YELLOW);
                ui.getPageNumber().setBackground(Color.YELLOW);
            }

            case 1 -> {
                pageNr = Integer.parseInt(ui.getPageNumber().getText());
                pageInTLB = simulationManager.checkPageInTLB(pageNr);

                if (pageInTLB) {
                    ui.getInfoArea().setText("\nPage was found in the TLB.");
                    ui.getInfoArea().setForeground(Color.GREEN); // Change text color to green
                    highlightTLBEntry(pageNr); // Your function to highlight TLB entry
                } else {
                    ui.getInfoArea().setText("\nPage was not found in the TLB.");
                    ui.getInfoArea().setForeground(Color.RED);
                }
            }

            case 2 -> {
                // Step 3: Highlight corresponding physical memory page
                physicalPageNumber = simulationManager.getPhysicalPage(pageNr); // Your logic to fetch physical page
                ui.getInfoArea().append("\nThe corresponding physical page is " + physicalPageNumber + ".");
                highlightPhysicalMemoryPage(physicalPageNumber); // Your function to highlight memory

            }

            case 3 -> {
                // Step 4: Calculate and display the value of the address
                offset = Integer.parseInt(ui.getOffset().getText());
                int physicalAddress = simulationManager.calculatePhysicalAddress(physicalPageNumber, offset); // Your logic
                String value = simulationManager.getValueFromPhysicalMemory(physicalPageNumber, offset); // Fetch value
                ui.getInfoArea().append("\nValue at address " + physicalAddress + " is " + value + ".");
                //ui.getPageNumberHex().setText(value); // Set the value in the register or field
            }
        }
        currentStep ++;
    }

    private void highlightTLBEntry(int pageNumber) {
        for (int i = 0; i < ui.getTlbTable().getRowCount(); i++) {
            if ((int) ui.getTlbTable().getValueAt(i, 1) == pageNumber) {
                ui.getTlbTable().setSelectionBackground(Color.GREEN);
                ui.getTlbTable().setRowSelectionInterval(i, i);
                break;
            }
        }
    }

    private void highlightPhysicalMemoryPage(int physicalPageNumber) {
        for (int i = 0; i < ui.getMemoryTable().getRowCount(); i++) {
            if ((int) ui.getMemoryTable().getValueAt(i, 0) == physicalPageNumber) {
                ui.getMemoryTable().setSelectionBackground(Color.GREEN);
                ui.getMemoryTable().setRowSelectionInterval(i, i);
                break;
            }
        }
    }
}
