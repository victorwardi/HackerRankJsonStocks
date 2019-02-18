import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.text.*;
import java.math.*;
import java.util.regex.*;
import java.net.*;

import com.google.gson.*;


class Result {
    private int page;
    private int per_page;
    private int total_pages;
    private List<Stock> data;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPer_page() {
        return per_page;
    }

    public void setPer_page(int per_page) {
        this.per_page = per_page;
    }

    public int getTotal_pages() {
        return total_pages;
    }

    public void setTotal_pages(int total_pages) {
        this.total_pages = total_pages;
    }

    public List<Stock> getData() {
        return data;
    }

    public void setData(List<Stock> data) {
        this.data = data;
    }
}

class Stock {

    private String date;
    private Double open;
    private Double high;
    private Double low;
    private Double close;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Double getOpen() {
        return open;
    }

    public void setOpen(Double open) {
        this.open = open;
    }

    public Double getHigh() {
        return high;
    }

    public void setHigh(Double high) {
        this.high = high;
    }

    public Double getLow() {
        return low;
    }

    public void setLow(Double low) {
        this.low = low;
    }

    public Double getClose() {
        return close;
    }

    public void setClose(Double close) {
        this.close = close;
    }
}


public class Solution {

    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("d-MMMM-uuuu");

    public static final String URL_STOCKS_API = "https://jsonmock.hackerrank.com/api/stocks";

    static void openAndClosePrices(String firstDate, String lastDate, String weekDay) {

        List<Stock> stocks = getStocks(URL_STOCKS_API);

        for (Stock stock : stocks) {
            if (isValidDate(stock.getDate(), firstDate, lastDate, weekDay)) {
                System.out.println(stock.getDate() + " " + stock.getOpen() + " " + stock.getClose());
            }
        }
    }

    private static  List<Stock> getStocks(String apiUrl) {

        List<Stock> stocks = new ArrayList<>();

       try {

            Result result = getResult(apiUrl, 0);

            int currentPage = result.getPage();
            int totalPages = result.getTotal_pages();

            System.out.println(result.getPage() + ": " + totalPages);

            while (currentPage <= totalPages) {

                result = getResult(apiUrl, currentPage++);

                stocks.addAll(result.getData());

              }


        } catch (IOException e) {
            e.printStackTrace();
        }


        return stocks;


    }

    private static Result getResult(String apiUrl, int page) throws IOException {

        String requestUrl = page == 0 ? apiUrl : apiUrl + "?page=" + page;

        URL url = new URL(requestUrl);

        InputStreamReader reader = new InputStreamReader(url.openStream());

        Result result = new Gson().fromJson(reader, Result.class);

        return result;
    }

    private static boolean isValidDate(String date, String firstDate, String lastDate, String weekDay) {
        return isDateInTheRange(date, firstDate, lastDate) && isDateWeekDay(date, weekDay);
    }

    private static boolean isDateInTheRange(String date, String firstDate, String lastDate) {


        LocalDate localDate = LocalDate.parse(date, DATE_TIME_FORMATTER);
        LocalDate firstLocalDate = LocalDate.parse(firstDate, DATE_TIME_FORMATTER);
        LocalDate lastLocalDate = LocalDate.parse(lastDate, DATE_TIME_FORMATTER);

        boolean isDateEqualFirstDate = localDate.isEqual(firstLocalDate);
        boolean isDateEqualLastDate = localDate.isEqual(lastLocalDate);

        if ((isDateEqualFirstDate || isDateEqualLastDate)) {
            return true;
        }

        boolean isDateAfterFirstDate = localDate.isAfter(firstLocalDate);
        boolean isEqualBeforeLastDate = localDate.isBefore(lastLocalDate);

        return isDateAfterFirstDate && isEqualBeforeLastDate;
    }

    private static boolean isDateWeekDay(String date, String weekDay) {
        LocalDate localDate = LocalDate.parse(date, DATE_TIME_FORMATTER);
        return weekDay.equalsIgnoreCase(localDate.getDayOfWeek().toString());
    }


    public static void main(String[] args) {

        Scanner in = new Scanner(System.in);
        String _firstDate;
        try {
            _firstDate = in.nextLine();
        } catch (Exception e) {
            _firstDate = null;
        }

        String _lastDate;
        try {
            _lastDate = in.nextLine();
        } catch (Exception e) {
            _lastDate = null;
        }

        String _weekDay;
        try {
            _weekDay = in.nextLine();
        } catch (Exception e) {
            _weekDay = null;
        }


        openAndClosePrices(_firstDate, _lastDate, _weekDay);

    }
}