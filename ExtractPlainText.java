import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.StringBuffer;

// https://svn.apache.org/repos/asf/poi/trunk/src/ooxml/testcases/org/apache/poi/extractor/TestExtractorFactory.java
import org.apache.poi.POIOLE2TextExtractor;
import org.apache.poi.POITextExtractor;
//import org.apache.poi.POIDataSamples;
//import org.apache.poi.extractor.*;
import org.apache.poi.extractor.ExtractorFactory;
import org.apache.poi.hdgf.extractor.VisioTextExtractor;
import org.apache.poi.hpbf.extractor.PublisherTextExtractor;
import org.apache.poi.hslf.extractor.PowerPointExtractor;
import org.apache.poi.hsmf.extractor.OutlookTextExtactor;
import org.apache.poi.hssf.extractor.EventBasedExcelExtractor;
import org.apache.poi.hssf.extractor.ExcelExtractor;
import org.apache.poi.hwpf.extractor.Word6Extractor;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.xslf.extractor.XSLFPowerPointExtractor;
import org.apache.poi.xssf.extractor.XSSFEventBasedExcelExtractor;
import org.apache.poi.xssf.extractor.XSSFExcelExtractor;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.poifs.filesystem.OfficeXmlFileException;

import org.apache.poi.xslf.usermodel.XMLSlideShow;	// pptx 2007, http://poi.apache.org/apidocs/org/apache/poi/xslf/
import org.apache.poi.xwpf.usermodel.XWPFDocument;	// docx 2007, http://poi.apache.org/apidocs/org/apache/poi/xwpf/
import org.apache.poi.xssf.usermodel.XSSFWorkbook;	// xlsx 2007, http://poi.apache.org/apidocs/org/apache/poi/xssf/

class ExtractPlainText
{
	public static String file(String path)
	{
		try { return pptx(new FileInputStream(path)); } catch(Exception e) { }
		try { return docx(new FileInputStream(path)); } catch(Exception e) { }
		try { return xlsx(new FileInputStream(path)); } catch(Exception e) { }
		return "";
	}
	public static String pptx(InputStream in) throws Exception
	{
		XSLFPowerPointExtractor o = new XSLFPowerPointExtractor( new XMLSlideShow(in) );
		o.setSlidesByDefault(true);
		o.setNotesByDefault(true);
		return o.getText();
	}
	public static String docx(InputStream in) throws Exception
	{
		XWPFWordExtractor o = new XWPFWordExtractor(new XWPFDocument(in));
		return o.getText();
	}
	public static String xlsx(InputStream in) throws Exception
	{
		XSSFExcelExtractor o = new XSSFExcelExtractor(new XSSFWorkbook(in));
		return o.getText();
	}
	public static void main(String argv[])
	{
		try
		{
			InputStream in = null;
			if( argv.length < 1 )
				in = System.in;
			else
				in = new FileInputStream(argv[0]);
			StringBuffer output = new StringBuffer();
			POITextExtractor textExtractor = ExtractorFactory.createExtractor(in);
			//POIFSFileSystem fileSystem = new POIFSFileSystem(in);
			//POITextExtractor textExtractor = ExtractorFactory.createExtractor(fileSystem);
			//POIOLE2TextExtractor oleTextExtractor = ExtractorFactory.createExtractor(fileSystem);
			//POITextExtractor[] embeddedExtractors = ExtractorFactory.getEmbededDocsTextExtractors(oleTextExtractor);
			//for (POITextExtractor textExtractor : embeddedExtractors)
			{
				if (textExtractor instanceof ExcelExtractor)				// xls, excel 97-2003
				{
					ExcelExtractor extractor = (ExcelExtractor) textExtractor;
					//System.out.println(extractor.getText());
					output.append(extractor.getText());
				}
				else if (textExtractor instanceof XSSFExcelExtractor)			// xlsx, excel 2007
				{
					XSSFExcelExtractor extractor = (XSSFExcelExtractor) textExtractor;
					//System.out.println(extractor.getText());
					output.append(extractor.getText());
				}
				else if (textExtractor instanceof Word6Extractor)			// doc, word 95
				{
					Word6Extractor extractor = (Word6Extractor) textExtractor;
					// http://poi.apache.org/apidocs/org/apache/poi/hwpf/extractor/Word6Extractor.html
					//for (String paragraph : extractor.getParagraphText() )
					//	System.out.println(paragraph);
					//System.out.println(extractor.getText());
					output.append(extractor.getText());
				}
				else if (textExtractor instanceof WordExtractor)			// doc, word 97-2003
				{
					WordExtractor extractor = (WordExtractor) textExtractor;
					// http://poi.apache.org/apidocs/org/apache/poi/hwpf/extractor/WordExtractor.html
					//System.out.println(extractor.getHeaderText());
					//System.out.println(extractor.getFooterText());
					//for (String paragraph : extractor.getParagraphText() )
					//	System.out.println(paragraph);
					//System.out.println(extractor.getText());
					output.append(""+extractor.getText());
				}
				else if (textExtractor instanceof XWPFWordExtractor)			// docx, word 2007
				{
					XWPFWordExtractor extractor = (XWPFWordExtractor) textExtractor;
					//System.out.println(extractor.getText());
					output.append(extractor.getText());
				}
				else if (textExtractor instanceof PowerPointExtractor)			// ppt, ppt 97-2003
				{
					PowerPointExtractor extractor = (PowerPointExtractor) textExtractor;
					//System.out.println(extractor.getText());
					//System.out.println(extractor.getNotes());
					output.append(extractor.getText());
					output.append(extractor.getNotes());
				}
				else if (textExtractor instanceof XSLFPowerPointExtractor )		// pptx, powerpoint 2007
				{
					XSLFPowerPointExtractor extractor = (XSLFPowerPointExtractor) textExtractor;
					extractor.setSlidesByDefault(true);
					extractor.setNotesByDefault(true);
					//System.out.println(extractor.getText());
					output.append(extractor.getText());
				}
				else if (textExtractor instanceof VisioTextExtractor)			// vsd, visio
				{
					VisioTextExtractor extractor = (VisioTextExtractor) textExtractor;
					//System.out.println(extractor.getText());
					output.append(extractor.getText());
				}
				else if (textExtractor instanceof PublisherTextExtractor)		// pub, publisher
				{
					PublisherTextExtractor extractor = (PublisherTextExtractor) textExtractor;
					//System.out.println(extractor.getText());
					output.append(extractor.getText());
				}
				else if (textExtractor instanceof OutlookTextExtactor)			// msg, outlook
				{
					OutlookTextExtactor extractor = (OutlookTextExtactor) textExtractor;
					//System.out.println(extractor.getText());
					output.append(extractor.getText());
				}
			}
			System.out.println(output.toString().replaceAll( "[\n\t\r ]+"," "));
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			//e.printStackTrace();
			//System.out.println("== ERROR =="+e);
		}
	}
}
