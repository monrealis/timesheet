package eu.vytenis.timesheet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;

public class AnalyzerTest {
	private List<String> input = new ArrayList<>();
	private Analyzer analyzer;

	@Test
	public void parsesDate() {
		input.add("I (2016-09-05)");
		run();
		assertEquals("[2016-09-05]", analyzer.dates.toString());
	}

	@Test(expected = Analyzer.InvalidDayOfWeek.class)
	public void failsOnBadDayOfWeek() {
		input.add("II (2016-09-05)");
		run();
	}

	private void run() {
		analyzer = new Analyzer();
		analyzer.run();
	}

	private class Analyzer {
		private Set<LocalDate> dates = new LinkedHashSet<>();

		public void run() {
			Pattern p = Pattern.compile("([A-Z]+) \\(([0-9-]+)\\)");
			Matcher m = p.matcher(input.get(0));
			assertTrue(m.matches());
			MatchResult mr = m.toMatchResult();
			int dayOfWeek = parseRoman(mr.group(1));
			LocalDate date = LocalDate.parse(mr.group(2));
			dates.add(date);
			DayOfWeek w = date.getDayOfWeek();
			if (w.getValue() != dayOfWeek)
				throw new InvalidDayOfWeek();
		}

		private int parseRoman(String roman) {
			Map<String, Integer> map = new HashMap<>();
			map.put("I", 1);
			map.put("II", 2);
			Integer r = map.get(roman);
			assertNotNull(r);
			return r;
		}
		
		public class InvalidDayOfWeek extends RuntimeException {
			private static final long serialVersionUID = 1L;
			
		}
	}
}
