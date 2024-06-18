package com.fujitsu.csvtopdf.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.fujitsu.csvtopdf.FileUploadUtil;
import com.itextpdf.text.Document;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.opencsv.CSVReader;

import jakarta.servlet.http.HttpSession;

@Controller
public class CsvToPdfController {

	@GetMapping("/")
	public String home() {
		return "index";
	}
	
	@PostMapping("/changefiletype")
	public String changeFileType(@RequestParam("upload") MultipartFile file, Model m) {
		String url = "";
		String fileName = StringUtils.cleanPath(file.getOriginalFilename());
 
        String uploadDir = "D:\\java\\Spring\\boot\\SpringCsvToPdf\\src\\main\\resources\\static\\pdf\\";
        try {
        	//System.out.println("FileUploadUtil ");
        	FileUploadUtil.saveFile(uploadDir, fileName, file);
        } catch(IOException ie) {
        	System.out.println("Error! file upload "+ie.getMessage());
        }
		
		try {
			//System.out.println("FileInputStream");
			CSVReader reader = new CSVReader(new FileReader(uploadDir+fileName));
			String [] nextLine;
			//int lnNum = 0;
			Document my_pdf_data = new Document();
			String[] fileNamearr = fileName.split("\\.");
			PdfWriter.getInstance(my_pdf_data, new FileOutputStream(fileNamearr[0]+".pdf"));
			my_pdf_data.open();
			nextLine = reader.readNext();
			int colunm = nextLine.length;
	        PdfPTable my_first_table = new PdfPTable(colunm);
	        PdfPCell table_cell;
			while ((nextLine) != null) {
				for(int i=0;i<colunm;i++) {
	                table_cell=new PdfPCell(new Phrase(nextLine[i]));
	                my_first_table.addCell(table_cell);
	                }
				nextLine = reader.readNext();
			}
			my_pdf_data.add(my_first_table);
			url = "D:\\java\\Spring\\boot\\SpringCsvToPdf\\"+fileNamearr[0]+".pdf";
			//session.setAttribute("downloadurl", url);
			m.addAttribute("downloadurl", url);
			//System.out.println(reader.readNext());
			my_pdf_data.close();
			System.out.println("end");
//			HSSFWorkbook my_xls_workbook = new HSSFWorkbook(input_document);
//
//			System.out.println(my_xls_workbook);
//	        System.out.println("FileInputStream 1");
//	        HSSFSheet my_worksheet = my_xls_workbook.getSheetAt(0);
//	        Iterator<Row> rowIterator = my_worksheet.iterator();
//	        int column =0;
//	        Row rownew = rowIterator.next(); 
//	        Iterator<Cell> cellIteratornew = rownew.cellIterator();
//	        while(cellIteratornew.hasNext()) {
//	             column++;   
//	        }
//	        Document iText_xls_2_pdf = new Document();
//	        PdfWriter.getInstance(iText_xls_2_pdf, new FileOutputStream("D:\\java\\Spring\\boot\\CsvToPdf\\src\\main\\resources\\static\\pdf\\"+file.getOriginalFilename()+".pdf"));
//	        iText_xls_2_pdf.open();
//	        System.out.println("FileInputStream 2");
//	        PdfPTable my_table = new PdfPTable(column);
//	        PdfPCell table_cell;
//	        while(rowIterator.hasNext()) {
//	            Row row = rowIterator.next(); 
//	            Iterator<Cell> cellIterator = row.cellIterator();
//	            while(cellIterator.hasNext()) {
//	                Cell cell = cellIterator.next();
//                	table_cell=new PdfPCell(new Phrase(cell.getStringCellValue()));
//                    my_table.addCell(table_cell);
//	            }
//	        
//	        }
//	        url = "D:\\java\\Spring\\boot\\CsvToPdf\\src\\main\\resources\\pdf\\"+file.getOriginalFilename()+".pdf";
//	        iText_xls_2_pdf.add(my_table);                       
//	        iText_xls_2_pdf.close();
//	        input_document.close();
//	        my_xls_workbook.close();
//	        System.out.println(input_document);
	        return "redirect:/";
			//return url;
			
		}
		catch(Exception fe) {
			System.out.println("Error! file stream "+fe.getMessage());
			return "redirect:/";
			
		}
		
		//System.out.println(file.getOriginalFilename());
		//return "redirect:/";
	}
}
