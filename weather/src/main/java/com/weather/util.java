package com.weather;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class util {
	
	private static String fullDayMonth(String s) throws Exception {
		if(s.equals("Субота")) {
			return DayOfTheWeekUkr.Субота.toString();
		}
		if(s.equals("Нед")) {
			return DayOfTheWeekUkr.Неділя.toString();
		}
		if(s.equals("Понед")) {
			return DayOfTheWeekUkr.Понеділок.toString();
		}
		if(s.equals("В")) {
			return DayOfTheWeekUkr.Вівторок.toString();
		}
		if(s.equals("Середа")) {
			return DayOfTheWeekUkr.Середа.toString();
		}
		if(s.equals("Четвер")) {
			return DayOfTheWeekUkr.Четвер.toString();
		}
		if(s.equals("П")) {
			return DayOfTheWeekUkr.Пятниця.toString();
		}
		throw new Exception("Can't find such day");
	}
	
	
	private static String monthYear(String s) throws Exception {
		//2018-11-10
		String m = "";
		String[] mas = s.split("-");
		int number = Integer.parseInt(mas[1]);
		switch (number) {
		case 1: 
			m = mas[2] + " " + Months.січня.toString() + " " + mas[0];
			return m;
		case 2:
			m = mas[2] + " " +  Months.лютого.toString() + " " +  mas[0];
			return m;
		case 3:
			m = mas[2] +  " " + Months.березня.toString() + " " +  mas[0];
			return m;
		case 4:
			m = mas[2] +  " " + Months.квітня.toString() + " " +  mas[0];
			return m;
		case 5:
			m = mas[2] +  " " + Months.травня.toString() + " " +  mas[0];
			return m;
		case 6:
			m = mas[2] + " " +  Months.червня.toString() + " " +  mas[0];
			return m;
		case 7:
			m = mas[2] + " " +  Months.липня.toString() + " " +  mas[0];
			return m;
		case 8:
			m = mas[2] + " " +  Months.серпня.toString() + " " +  mas[0];
			return m;
		case 9:
			m = mas[2] + " " +  Months.вересня.toString() + " " +  mas[0];
			return m;
		case 10:
			m = mas[2] + " " +  Months.жовтня.toString() + " " +  mas[0];
			return m;
		case 11:
			m = mas[2] + " " +  Months.листопада.toString() + " " +  mas[0];
			return m;
		case 12:
			m = mas[2] + " " +  Months.грудня.toString() + " " +  mas[0];
			return m;
		default:
			System.out.println("nofind");
		}throw new Exception("Can't find such month");
	}
	
	private static String sinoptikUrl = "https://ua.sinoptik.ua/погода-львів//"; // ukr

	private static Document getPage(int i) throws MalformedURLException, IOException {
		String url = sinoptikUrl + util.dateAfterSevenDays(i);
		Document page = Jsoup.parse(new URL(url), 3000);
		return page;
	}

	private static String rebuildString(String s) {
		s = s.replaceAll("\\s+", " ");
		s = s.replaceAll(" :", ":");
		return s;
	}
	
	private static String dayOfWeek(String s) throws Exception {
		Pattern pattern = Pattern.compile("[А-яа-я]+");
		Matcher matcher = pattern.matcher(s);
		if(matcher.find()) {
			return matcher.group();
		}throw new Exception("Can't extract date from string!");
		//String[] mas = s.split(" ");
		//return mas[0];
	}

	private static String[] infoStringForTwo(String s) {
		int count = 0;
		int i = 0;
		String[] mas = new String[7];
		String[] words = s.split(" ");
		words[0] = "0" + words[0];
		words[1] = "0" + words[1];
		String value = "";
		for (String word : words) {
			if (count < 8) {
				value += String.format("%6s", word);
				count++;
			}
			if (count == 8) {
				mas[i] = value;
				i++;
				count = 0;
				value = "";
			}
		}

		return mas;
	}

	private static String[] infoStringForMany(String s) {
		int count = 0;
		int i = 0;
		String[] mas = new String[7];
		String[] words = s.split(" ");
		words[0] = "0" + words[0];
		words[1] = "0" + words[1];
		String value = "";
		for (String word : words) {
			if (count < 4) {
				value += String.format("%7s", word) + "  ";
				count++;
			}
			if (count == 4) {
				mas[i] = value;
				i++;
				count = 0;
				value = "";
			}
		}
		return mas;
	}

	private static String dateAfterSevenDays(int i) {
		LocalDate localDate = LocalDate.now();
		String date = localDate.plusDays(i).toString();
		return date;
	}

	private static String partsDayForTwo(String s) {
		String last = "";
		// int count = 0;
		String[] mas = s.split(" ");
		for (String string : mas) {
			last += String.format("%9s", string) + "   ";
//			count++;
//			if (count == 4) {
//				last += String.format("%9s", string) + "   ";
//			} else
//				last += String.format("%9s", string) + "   ";
		}
		return last;
	}

	private static String partsDayForMany(String s) {
		String last = "";
		String[] mas = s.split(" ");

		for (String string : mas) {
			last += String.format("%7s", string) + "  ";
		}

		return last;
	}
	
	public static void sample(Integer days) throws Exception {
		for (int i = 0; i < days; i++) {
			String[] mas;
			Document page = getPage(i);
			Element tableWth = page.select("div#blockDays").first();
			Elements dates = tableWth.select("div#bd" + (i + 1) + "");
			Element bdc = tableWth.select("div#bd" + (i + 1) + "c").first();
			Elements partOfDay = bdc.select("thead");
			Elements infoValues = bdc.select("tbody");

			// Main output
			for (Element day : dates) {
				String date = day.select("p").text();
				System.out.println(fullDayMonth(dayOfWeek(date)) + " " + monthYear(dateAfterSevenDays(i))); // Date
				System.out.println();
				for (Element part : partOfDay) {
					String parts = part.select("td").text(); // The part of a day
					if (i < 2) {
						parts = partsDayForTwo(parts);
						System.out.println(parts);
					} else {
						parts = util.partsDayForMany(parts);
						System.out.println(parts);
					}
					for (Element info : infoValues) {
						String value = info.select("tr").text();
						value = rebuildString(value);
						if (i < 2) {
							mas = infoStringForTwo(value);
							for (String ss : mas) {
								System.out.println(ss);			// other info
							}
						}else {
							mas = infoStringForMany(value);
							for(String ss : mas) {
								System.out.println(ss);
							}
						}
					}
				}
				System.out.println("------------------------------------------------");
			}
		}
	}
	
	public static void clearScreen() {  
	    System.out.print("\033[H\033[2J");  
	    System.out.flush();  
	} 
}

