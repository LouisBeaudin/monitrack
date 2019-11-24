package com.monitrack.gui.panel;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.monitrack.listener.PersonsTabListener;

public class PersonsTab extends JPanel {

	private static final long serialVersionUID = 1L;
	
	/***** Panels *****/
	private JPanel northPanel;
	private JPanel northPanelActionsChoice;
	private JPanel northPanelForCreate;
	private JPanel northPanelForModify;
	private JPanel northPanelForDelete;
	private JPanel northPanelForShow;
	//private JPanel centerPanel;

	private JLabel newNameLabel;
	private JLabel actionLabel;

	private JComboBox<String> actionsCombobox;
	private JComboBox<String> modifyPersonsCombobox;
	private JComboBox<String> deletePersonsCombobox;
	private JComboBox<String> filter1ForShowCombobox;
	private JComboBox<String> filter2ForShowCombobox;

	private PersonsTabListener listener;


	private JTextField newNameTextField;
	private JTextField filter1TextField;
	private JTextField filter2TextField;

	private JTextArea textArea;


	/***** Buttons for the CRUD (Create, Read, Update and Delete) *****/ 
	private JButton createButton;
	private JButton modifyButton;
	private JButton deleteButton;
	private JButton showButton;

	/***** Dialog for updating a person *****/
	private JPanel modifyPersonPopupPanel;
	private JLabel idLabel;
	private JLabel oldNameLabel;
	private JTextField oldNameTextField;
	private JLabel modifiedNameLabel;
	private JTextField modifiedNameTextField;


	public PersonsTab() {
		super(new BorderLayout());

		//listener = new PersonsTabListener(this);
		northPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		textArea = new JTextArea();	

		//Panel to choose the CRUD Operation to do
		northPanelActionsChoice = new JPanel(new FlowLayout(FlowLayout.LEFT));
		actionLabel = new JLabel("Action : ");
		String[] items = {"Ajouter", "Modifier", "Supprimer", "Visualiser"};
		actionsCombobox = new JComboBox<String>(items);
		northPanelActionsChoice.add(actionLabel);
		northPanelActionsChoice.add(actionsCombobox);	


		setCreateMenu();
		setModifyMenu();
		setShowMenu();
		setDeleteMenu();

		Font textAreaFont = new Font("Calibri", Font.PLAIN, 25);
		textArea.setFont(textAreaFont);
		textArea.setEditable(false);
		JScrollPane scroll = new JScrollPane(textArea);

		actionsCombobox.addActionListener(listener);
		actionsCombobox.setSelectedItem(items[0]);
		
		add(northPanel, BorderLayout.NORTH);
		add(scroll, BorderLayout.CENTER);

		setModifyPersonDialog();

	}

	private void setCreateMenu()
	{
		//Panel for the creation operation
		northPanelForCreate = new JPanel(new FlowLayout(FlowLayout.LEFT));
		createButton = new JButton("Créer");
		createButton.addActionListener(listener);
		newNameLabel = new JLabel("Nouveau nom : ");
		newNameTextField = new JTextField(15);
		northPanelForCreate.add(newNameLabel);
		northPanelForCreate.add(newNameTextField);
		northPanelForCreate.add(createButton);	
	}

	private void setModifyMenu()
	{
		//Panel for the update operation
		northPanelForModify = new JPanel(new FlowLayout(FlowLayout.LEFT));
		modifyPersonsCombobox = new JComboBox<String>();
		modifyButton = new JButton("Modifier");
		modifyButton.addActionListener(listener);
		northPanelForModify.add(new JLabel("Sélectionner une personne : "));
		northPanelForModify.add(modifyPersonsCombobox);
		northPanelForModify.add(modifyButton);
	}

	private void setShowMenu()
	{
		//Panel for the show operation
		northPanelForShow = new JPanel(new FlowLayout(FlowLayout.LEFT));
		showButton = new JButton("Visualiser");
		showButton.addActionListener(listener);
		String[] items1 = {"-", "id"};
		filter1ForShowCombobox = new JComboBox<>(items1);
		String[] items2 = {"-", "nom"};
		filter2ForShowCombobox = new JComboBox<>(items2);
		filter1TextField = new JTextField(7);
		filter2TextField = new JTextField(7);
		northPanelForShow.add(new JLabel("Chercher par : "));
		northPanelForShow.add(filter1ForShowCombobox);
		northPanelForShow.add(filter1TextField);
		northPanelForShow.add(new JLabel(" Et par : "));
		northPanelForShow.add(filter2ForShowCombobox);
		northPanelForShow.add(filter2TextField);
		northPanelForShow.add(showButton);
	}

	private void setDeleteMenu() {
		//Panel for the delete operation
		northPanelForDelete = new JPanel(new FlowLayout(FlowLayout.LEFT));
		deletePersonsCombobox = new JComboBox<String>();
		deleteButton = new JButton("Supprimer");
		deleteButton.addActionListener(listener);
		northPanelForDelete.add(new JLabel("Sélectionner une personne : "));
		northPanelForDelete.add(deletePersonsCombobox);
		northPanelForDelete.add(deleteButton);
	}

	private void setModifyPersonDialog()
	{
		modifyPersonPopupPanel = new JPanel();
		modifyPersonPopupPanel.setLayout(new BoxLayout(modifyPersonPopupPanel, BoxLayout.Y_AXIS));
		idLabel = new JLabel("ID : ");
		oldNameLabel = new JLabel("Ancien nom :");
		oldNameTextField = new JTextField(15);
		oldNameTextField.setEditable(false);
		modifiedNameLabel = new JLabel("Nouveau nom :");
		modifiedNameTextField = new JTextField(15);

		modifyPersonPopupPanel.add(idLabel);
		modifyPersonPopupPanel.add(oldNameLabel);
		modifyPersonPopupPanel.add(oldNameTextField);
		modifyPersonPopupPanel.add(modifiedNameLabel);
		modifyPersonPopupPanel.add(modifiedNameTextField);
	}

	/**
	 * @return the newNameTextField
	 */
	public JTextField getNewNameTextField() {
		return newNameTextField;
	}

	/**
	 * @return the createButton
	 */
	public JButton getCreateButton() {
		return createButton;
	}

	/**
	 * @return the modifyButton
	 */
	public JButton getModifyButton() {
		return modifyButton;
	}

	/**
	 * @return the deleteButton
	 */
	public JButton getDeleteButton() {
		return deleteButton;
	}

	/**
	 * @return the showButton
	 */
	public JButton getShowButton() {
		return showButton;
	}

	/**
	 * @return the northPanel
	 */
	public JPanel getNorthPanel() {
		return northPanel;
	}

	/**
	 * @return the modifyPersonsCombobox
	 */
	public JComboBox<String> getModifyPersonsCombobox() {
		return modifyPersonsCombobox;
	}

	/**
	 * @return the deletePersonsCombobox
	 */
	public JComboBox<String> getDeletePersonsCombobox() {
		return deletePersonsCombobox;
	}

	/**
	 * @return the northPanelForCreate
	 */
	public JPanel getNorthPanelForCreate() {
		return northPanelForCreate;
	}

	/**
	 * @return the northPanelForModify
	 */
	public JPanel getNorthPanelForModify() {
		return northPanelForModify;
	}

	/**
	 * @return the northPanelForDelete
	 */
	public JPanel getNorthPanelForDelete() {
		return northPanelForDelete;
	}

	/**
	 * @return the northPanelForShow
	 */
	public JPanel getNorthPanelForShow() {
		return northPanelForShow;
	}

	/**
	 * @return the northPanelActionsChoice
	 */
	public JPanel getNorthPanelActionsChoice() {
		return northPanelActionsChoice;
	}

	/**
	 * @return the filter1ForShowCombobox
	 */
	public JComboBox<String> getFilter1ForShowCombobox() {
		return filter1ForShowCombobox;
	}

	/**
	 * @return the filter2ForShowCombobox
	 */
	public JComboBox<String> getFilter2ForShowCombobox() {
		return filter2ForShowCombobox;
	}

	/**
	 * @return the filter1TextField
	 */
	public JTextField getFilter1TextField() {
		return filter1TextField;
	}

	/**
	 * @return the filter2TextField
	 */
	public JTextField getFilter2TextField() {
		return filter2TextField;
	}

	/**
	 * @return the textArea
	 */
	public JTextArea getTextArea() {
		return textArea;
	}

	/**
	 * @return the modifyPersonPopupPanel
	 */
	public JPanel getModifyPersonPopupPanel() {
		return modifyPersonPopupPanel;
	}

	/**
	 * @return the idLabel
	 */
	public JLabel getIdLabel() {
		return idLabel;
	}

	/**
	 * @return the oldNameTextField
	 */
	public JTextField getOldNameTextField() {
		return oldNameTextField;
	}

	/**
	 * @return the modifiedNameTextField
	 */
	public JTextField getModifiedNameTextField() {
		return modifiedNameTextField;
	}

	/**
	 * @return the actionsCombobox
	 */
	public JComboBox<String> getActionsCombobox() {
		return actionsCombobox;
	}	
	


}
