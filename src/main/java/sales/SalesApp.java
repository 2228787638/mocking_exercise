package sales;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class SalesApp {

    public void generateSalesActivityReport(String salesId, int maxRow, boolean isNatTrade, boolean isSupervisor) {
        Sales sales = getSales(salesId);
        if (sales == null) {
            return;
        }
        SalesReportDao salesReportDao = new SalesReportDao();
        List<SalesReportData> reportDataList = salesReportDao.getReportData(sales);
        List<String> headers = getHeaders(isNatTrade);
        SalesActivityReport report = this.generateReport(headers, reportDataList);
        if (report == null) {
            return;
        }
        uploadDocument(report);
    }

    protected List<String> getHeaders(boolean isNatTrade) {
        if (isNatTrade) {
            return Arrays.asList("Sales ID", "Sales Name", "Activity", "Time");
        } else {
            return Arrays.asList("Sales ID", "Sales Name", "Activity", "Local Time");
        }
    }

    protected Sales getSales(String id) {
        Sales sales = getSalesDao().getSalesBySalesId(id);
        Date date = new Date();
        if (sales == null || date.after(sales.getEffectiveTo())
                || date.before(sales.getEffectiveFrom())) {
            return null;
        }
        return sales;
    }

    protected SalesDao getSalesDao() {
        return new SalesDao();
    }

    protected SalesActivityReport generateReport(List<String> headers, List<SalesReportData> reportDataList) {
        // TODO Auto-generated method stub
        return null;
    }

    protected SalesReportDao getSalesReportDao() {
        return new SalesReportDao();
    }

    protected void uploadDocument(SalesActivityReport report) {
        new EcmService().uploadDocument(report.toXml());
    }

}
