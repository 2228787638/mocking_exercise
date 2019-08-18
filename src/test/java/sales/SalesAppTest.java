package sales;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class SalesAppTest {

	@Test
	public void testGenerateReport() {
		SalesApp salesApp = new SalesApp();
		salesApp.generateSalesActivityReport("DUMMY", 1000, false, false);
	}

	@Test
	public void should_return_List_contains_Local_Time_when_call_getHeaders_given_false() {
		SalesApp salesApp = new SalesApp();
		List<String> list = salesApp.getHeaders(false);
		assertTrue(list.contains("Local Time"));
	}

	@Test
	public void should_return_List_contains_Time_when_call_getHeaders_given_true() {
		SalesApp salesApp = new SalesApp();
		List<String> list = salesApp.getHeaders(true);
		assertTrue(list.contains("Time"));
	}

	@Test
	public void should_return_sales_when_call_getSales_given_error_time() {
		SalesApp salesApp = spy(new SalesApp());
		Date today = new Date();
		Sales mockSales = mock(Sales.class);
		when(mockSales.getEffectiveFrom()).thenReturn(today);
		when(mockSales.getEffectiveTo()).thenReturn(today);
		SalesDao salesDao = mock(SalesDao.class);
		when(salesDao.getSalesBySalesId(anyString())).thenReturn(mockSales);
		doReturn(salesDao).when(salesApp).getSalesDao();
		Sales sales = salesApp.getSales(anyString());
		assertNull(sales);
	}

	@Test
	public void should_return_sales_when_call_getSales_given_right_time() {
		SalesApp salesApp = spy(new SalesApp());
		Date date = new Date();
		Sales mockSales = mock(Sales.class);
		when(mockSales.getEffectiveFrom()).thenReturn(new Date(date.getTime() - 86400000L));
		when(mockSales.getEffectiveTo()).thenReturn(new Date(date.getTime() + 86400000L));
		SalesDao salesDao = mock(SalesDao.class);
		when(salesDao.getSalesBySalesId(anyString())).thenReturn(mockSales);
		doReturn(salesDao).when(salesApp).getSalesDao();
		Sales sales = salesApp.getSales(anyString());
		assertNotNull(sales);
	}

	@Test
	public void should_call_uploadDocument_methods_when_call_generateSalesActivityReport() {
		SalesApp salesApp = spy(new SalesApp());
		SalesReportDao salesReportDao = mock(SalesReportDao.class);
		when(salesReportDao.getReportData(any())).thenReturn(new ArrayList<>());
		doReturn(new Sales()).when(salesApp).getSales(any());
		doReturn(salesReportDao).when(salesApp).getSalesReportDao();
		doReturn(new ArrayList<>()).when(salesApp).getHeaders(anyBoolean());
		doReturn(new SalesActivityReport()).when(salesApp).generateReport(any(), any());
		salesApp.generateSalesActivityReport(anyString(), anyInt(), anyBoolean(), anyBoolean());
		verify(salesApp, times(1)).uploadDocument(any());
	}
}
