package Scraper;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.FlowLayout;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.awt.event.ActionEvent;



public class WebScraper {

	private JFrame frame;
	private JTextField inputURL;
	private JTextField depthOfSearch;
	private JTextField searchString;
	private JTextArea editor;
	private ScrapingFunction s;
	private String returnedText;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					WebScraper w = new WebScraper();
					w.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public WebScraper() {
		go();
	}

	private void go() {

		// create JFrame
		frame = new JFrame("Web Scraping Program");

		// create components
		editor = new JTextArea();
		editor.setLineWrap(true);
		editor.setWrapStyleWord(true);
		JScrollPane scrollPane = new JScrollPane(editor);

		inputURL = new JTextField();
		inputURL.setToolTipText("Input URL");
		inputURL.setColumns(10);

		depthOfSearch = new JTextField();
		depthOfSearch.setToolTipText("Input Depth of Search");
		depthOfSearch.setColumns(10);

		searchString = new JTextField();
		searchString.setToolTipText("Input String to Search");
		searchString.setColumns(10);

		JButton searchButton = new JButton("Search");
		searchButton.addActionListener(new SearchButtonActionListener());

		JButton saveButton = new JButton("Save");
		saveButton.addActionListener(new SaveButtonActionListener());
		
		JButton quitButton = new JButton("Quit");
		quitButton.addActionListener(new QuitButtonActionListener());

		JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		topPanel.add(inputURL);
		topPanel.add(depthOfSearch);
		topPanel.add(searchString);
		topPanel.add(searchButton);
		topPanel.add(saveButton);
		topPanel.add(quitButton);

		// add components to JFrame
		frame.add(scrollPane, BorderLayout.CENTER);
		frame.add(topPanel, BorderLayout.NORTH);

		// set JFrame properties
		frame.setSize(800, 600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);

	};

	private class SearchButtonActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			returnedText = s.returnContent(inputURL.getText(), Integer.parseInt(depthOfSearch.getText()),
					searchString.getText());
			editor.setText(returnedText);

		}
	}

	private class SaveButtonActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			s.saveToFile(returnedText);
			frame.repaint();
		}

	}
	
	
	private class QuitButtonActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			System.exit(0);
		}

	}
	
	public static class ScrapingFunction {

		public static void saveToFile(String content) {
			try {
				FileWriter fw = new FileWriter("textFile.csv");
				BufferedWriter bw = new BufferedWriter(fw);
				PrintWriter pw = new PrintWriter(bw);

				pw.println(content);
				pw.flush();
				pw.close();

				JOptionPane.showMessageDialog(null, "Data was saved to the CSV File.");

			} catch (IOException e) {
				JOptionPane.showMessageDialog(null, "There was a problem when saving to the CSV File");
			}
		}

		public static String returnContent(String url, int size, String toSearch) {
			String returnedText = "";
			try {
				final Document document = Jsoup.connect(url).get();
				for (Element p : document.select("p")) {
					String fullText = p.select("p").text();

					if (fullText.contains("to") && size != 0) {
						returnedText += fullText;
						returnedText += "\n \n";
						size--;
					} else {
						continue;
					}
				}
			} catch (Exception e) {
				System.out.println("Invalid url");
				e.printStackTrace();
			}

			return returnedText;
		}
	}



}

