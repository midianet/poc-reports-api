package br.com.aurora.report.helper;

import br.com.aurora.report.model.Report;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.*;
import net.sf.jasperreports.export.SimpleCsvMetadataExporterConfiguration;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleWriterExporterOutput;
import org.springframework.core.io.ClassPathResource;
import org.springframework.lang.NonNull;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;

public final class ReportHelper {
	public final static String DEFAULT_TYPE = "PDF";

	private ReportHelper() {
	}

	public static byte[] makeReport(@NonNull final Report.Type type, @NonNull final String filename, @NonNull final Map<String, Object> params, @NonNull final Collection data) {
		try {
			final var report = JasperFillManager.fillReport(new ClassPathResource(String.format("/jasper/%s.jasper"
							, filename))
							.getInputStream(),
					params, buildDatasource(data));
			return switch (type) {
				case PDF -> exportPdf(report);
				case CSV -> exportCsv(report);
				case XLS -> exportXls(report);
				default -> throw new RuntimeException("Tipo Relatório Inválido");
			};
		}catch (IOException | JRException e){
			throw new RuntimeException(e.getMessage(),e);
		}
	}

	private static JRDataSource buildDatasource(final Collection data) {
		return  Objects.isNull(data) ? new JREmptyDataSource() :  new JRBeanCollectionDataSource(data);
	}

	private static byte[] exportPdf(final JasperPrint print) throws JRException {
		final var exporter = new JRPdfExporter();
		final var output = new ByteArrayOutputStream();
		exporter.setExporterInput(new SimpleExporterInput(print));
		exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(output));
		exporter.exportReport();
		return output.toByteArray();
	}

	private static byte[] exportCsv(final JasperPrint print) throws JRException {
		final var output = new ByteArrayOutputStream();
		final var exporter = new JRCsvMetadataExporter();
		final var configuration = new SimpleCsvMetadataExporterConfiguration();
		exporter.setExporterInput(new SimpleExporterInput(print));
		exporter.setExporterOutput(new SimpleWriterExporterOutput(output));
		exporter.setConfiguration(configuration);
		exporter.exportReport();
		return output.toByteArray();
	}

	private static byte[] exportXls(final JasperPrint print) throws JRException {
		final var output = new ByteArrayOutputStream();
		final var exporter = new JRXlsExporter();
		//exporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
		//exporter.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
		//exporter.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE, Boolean.TRUE);
		exporter.setExporterInput(new SimpleExporterInput(print));
		exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(output));
		exporter.exportReport();
		return output.toByteArray();
	}


//	public static JasperReport getSubReport(final String subReport) {
//		try {
//			return (JasperReport)JRLoader.loadObject(ReportHelper.class.getResourceAsStream("/reports/" + subReport));
//		} catch (JRException e) {
//            throw new RuntimeException(e.getMessage(), e);
//		}
//	}
//
    public static BufferedImage getImage(final String filePath) {
		try {
			return ImageIO.read(ReportHelper.class.getResource(filePath));
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}
//
//	public static byte[] gerarPdf(final String filename, final Map<String, Object> params, final Collection<?> beanCollection) {
//		try {
//			final var dataSource = beanCollection == null ? new JREmptyDataSource() : new JRBeanCollectionDataSource(beanCollection);
//			var print = fillReport(filename, params, dataSource);
//			return JasperExportManager.exportReportToPdf(print);
//		} catch (Exception e) {
//			throw new RuntimeException(e.getMessage(), e);
//		}
//	}
//
//	public static void gerarPdf(final String filename, final Map<String, Object> params, final Collection<?> beanCollection, final OutputStream outputStream) {
//		try {
//			final var dataSource = beanCollection == null ? new JREmptyDataSource() : new JRBeanCollectionDataSource(beanCollection);
//			final var print = fillReport(filename, params, dataSource);
//			JasperExportManager.exportReportToPdfStream(print,outputStream);
//		} catch (Exception e) {
//			throw new RuntimeException(e.getMessage(), e);
//		}
//	}


}