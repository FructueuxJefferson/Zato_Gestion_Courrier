/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import courriel.CourrierModel;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import notification.NotificationModel;
import operations.GetDimension;

/**
 *
 * @author Perfection
 */
public class Utils {
	
	public static String JarPath() {
		File f = new File(System.getProperty("java.class.path"));
		File dir = f.getAbsoluteFile().getParentFile();
		String path = dir.toString();
		
		return path;
	}
	public static void showAlert(Alert.AlertType type, String title, String header, String content) {
		Alert alert = new Alert(type);
		alert.setTitle(title);

		alert.setHeaderText(header);
		alert.setContentText(content);

		alert.showAndWait();
	}

	public static boolean doesCourrierExits(String name, ArrayList<CourrierModel> courriers) {
		for (CourrierModel courrier : courriers) {
			if (courrier.getName().toLowerCase().equals(name))
				return true;
		}

		return false;
	}

	public static HBox setLabelDescription(String label, Node node) {
		HBox hb = new HBox(GetDimension.getDynamicWidth(10));
		Text text = new Text(label);
		text.setFont(new Font(GetDimension.getDynamicWidth(15)));

		hb.getChildren().addAll(text, node);
		hb.setAlignment(Pos.CENTER_LEFT);

		return hb;
	}

	public static boolean SavePDF(String message, String fileName) throws IOException, DocumentException {
		try {
			Document document = new Document(PageSize.A4, 50, 50, 50, 50);

			// 2. Create PdfWriter
			PdfWriter.getInstance(document, new FileOutputStream("courriers/" + fileName));

			// 3. Open document
			document.open();

			// 4. Add content
			boolean saved = document.add(new Paragraph(message));

			// 5. Close document
			document.close();
			return saved;
		} catch (IOException io) {
			return false;
		} catch (DocumentException ex) {
			return false;
		}
	}

	public static boolean validateJavaDate(String strDate) {
		/* Check if date is 'null' */
		if (strDate.trim().equals("")) {
			return true;
		}
		/* Date is not 'null' */
		else {
			/*
			 * Set preferred date format, For example MM-dd-yyyy, MM.dd.yyyy,dd.MM.yyyy etc.
			 */
			SimpleDateFormat sdfrmt = new SimpleDateFormat("yyyy-MM-dd");
			sdfrmt.setLenient(false);
			/*
			 * Create Date object parse the string into date
			 */
			try {
				Date javaDate = sdfrmt.parse(strDate);
				System.out.println(strDate + " is valid date format");
			}
			/* Date format is invalid */
			catch (ParseException e) {
				System.out.println(strDate + " is Invalid Date format");
				return false;
			}
			/* Return true if date format is valid */
			return true;
		}
	}
	
	public static boolean NotifWasShownToday(List<NotificationModel> notifs, String name, String date) {
		for(NotificationModel nm: notifs) {
			if(nm.getName().equals(name) && nm.getDate().equals(date)) {
				return true;
			}
		}
		
		return false;
	}
}
