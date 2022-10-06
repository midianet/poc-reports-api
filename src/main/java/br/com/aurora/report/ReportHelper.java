package br.com.aurora.report;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import org.springframework.core.io.ClassPathResource;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.*;


public final class ReportHelper {

	private ReportHelper(){}

	public static byte[] makePDF(final String filename, final Map<String, Object> params, final Collection data) {
		try {
			final var dataSource = Objects.isNull(data) ? new JREmptyDataSource() :  new JRBeanCollectionDataSource(data);
			final var print = fillReport(filename, params, dataSource);
			return getByteArray(print);
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	private static byte[] getByteArray(final JasperPrint print) throws JRException {
		final var exporter = new  JRPdfExporter();
		final var output = new ByteArrayOutputStream();
		exporter.setExporterInput(new SimpleExporterInput(print));
		exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(output));
		exporter.exportReport();
		return output.toByteArray();
	}

	private static JasperPrint fillReport(final String filename, final Map<String, Object> params, final JRRewindableDataSource datasource) throws JRException, IOException {
		return JasperFillManager.fillReport(new ClassPathResource(String.format("/jasper/%s.jasper", filename)).getInputStream(), params,datasource);
	}

	public static JasperReport getSubReport(final String subReport) {
		try {
			return (JasperReport)JRLoader.loadObject(ReportHelper.class.getResourceAsStream("/jasper/" + subReport));
		} catch (JRException e) {
            throw new RuntimeException(e.getMessage(), e);
		}
	}

    public static BufferedImage getImage(final String filePath) {
		try {
			return ImageIO.read(ReportHelper.class.getResource(filePath));
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	public static byte[] gerarPdf(final String filename, final Map<String, Object> params, final Collection<?> beanCollection) {
		try {			
			final var dataSource = beanCollection == null ? new JREmptyDataSource() : new JRBeanCollectionDataSource(beanCollection);
			var print = fillReport(filename, params, dataSource);
			return JasperExportManager.exportReportToPdf(print);
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	public static void gerarPdf(final String filename, final Map<String, Object> params, final Collection<?> beanCollection, final OutputStream outputStream) {
		try {			
			final var dataSource = beanCollection == null ? new JREmptyDataSource() : new JRBeanCollectionDataSource(beanCollection);
			final var print = fillReport(filename, params, dataSource);
			JasperExportManager.exportReportToPdfStream(print,outputStream);
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}


}